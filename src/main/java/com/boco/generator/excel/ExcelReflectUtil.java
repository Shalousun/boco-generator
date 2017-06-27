package com.boco.generator.excel;


import com.boco.generator.annotation.ExcelAnnotation;
import com.boco.generator.util.DateTimeUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author sunyu
 */
public class ExcelReflectUtil {
    /**
     * get all field of object that marked by ExcelAnnotation annotation
     *
     * @param clazz Class
     * @return List
     */
    public static <T> List<Field> getFields(Class<T> clazz) {
        // obtain all Fields
        Field[] allFields = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        for (Field field : allFields) {
            // A field which marked with ExcelAnnotation
            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                fields.add(field);
            }
        }
        return fields;
    }
    /**
     * get all field of object that marked by ExcelAnnotation annotation
     *
     * @param clazz Class
     * @return Map
     */
    public static <T> Map<Integer, Field> getFieldsMap(Class<T> clazz) {
        Field[] allFields = clazz.getDeclaredFields();
        Map<Integer, Field> fieldsMap = new HashMap<>();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                ExcelAnnotation attr = field
                        .getAnnotation(ExcelAnnotation.class);
                field.setAccessible(true);
                fieldsMap.put(attr.columnNum(), field);
            }
        }
        return fieldsMap;
    }

    /**
     * set value into object if success then return true
     *
     * @param entity T
     * @param value the value is read from excel
     * @param field Field
     * @param fieldType field type
     * @return boolean
     */
    public static <T> boolean setValue(T entity, String value, Field field, Class<?> fieldType) {
        boolean flag;
        try {
            if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                field.set(entity, NumberUtils.toInt(value, 0));
            } else if (String.class == fieldType) {
                field.set(entity, String.valueOf(value));
            } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                field.set(entity, NumberUtils.toLong(value, 0));
            } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                field.set(entity, NumberUtils.toFloat(value, 0.0f));
            } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                field.set(entity, Short.valueOf(value));
            } else if ((Double.TYPE == fieldType)
                    || (Double.class == fieldType)) {
                field.set(entity, NumberUtils.toDouble(value, 0.0));
            } else if (Character.TYPE == fieldType) {
                if ((value != null) && (value.length() > 0)) {
                    field.set(entity, Character.valueOf(value.charAt(0)));
                }
            }
            flag = true;
        } catch (IllegalAccessException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据数据类型设置列的类型
     *
     * @param cell
     * @param field
     */
    public static <T> void setCellTypeAndValue(HSSFCell cell, Field field, T t)
            throws IllegalArgumentException, IllegalAccessException {
        Object value = field.get(t);
        ExcelAnnotation attr = field.getAnnotation(ExcelAnnotation.class);
        if (field.getType().toString().equals("byte")
                || field.getType() == Byte.class) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(value == null ? "" : String.valueOf(field
                    .get(t)));
        } else if (field.getType().toString().equals("boolean")
                || field.getType() == Boolean.class) {
            cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
            cell.setCellValue(value == null ? "" : String.valueOf(value));
        } else if (field.getType().toString().equals("double")
                || field.getType() == Double.class) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(value == null ? null : Double.valueOf(String.valueOf(value)));
        } else if (field.getType().toString().equals("float")
                || field.getType() == Float.class) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue((Float) (value == null ? "" : (Float) (value)));
        } else if (field.getType().toString().equals("int")
                || field.getType() == Integer.class) {
            if(attr.replace().length>0){
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue((String)replaceValue(attr.replace(),String.valueOf(value)));
            }else{
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(value == null ? 0 : (Integer) (value));
            }
        } else if (field.getType().toString().equals("long")
                || field.getType() == Long.class) {
            if(!"".equals(attr.formatTime())&&attr.formatTime() != null){
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(DateTimeUtil.long2Str((Long)value,attr.formatTime()));
            }else {
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue((value == null ? 0 : (Long) (value)));
            }
        } else if (field.getType().toString().equals("short")
                || field.getType() == Short.class) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(value == null ? "" : String.valueOf(value));
        } else if (field.getType() == Timestamp.class) {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(value == null ? "" : String.valueOf(value));
        } else if (field.getType() == Date.class) {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(value == null ? "" : String.valueOf(value));
        } else if (field.getType() == Time.class) {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(value == null ? "" : String.valueOf(value));
        } else {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(value == null ? "" : String.valueOf(value));
        }

    }

    /**
     *
     * @param cell HSSFCell
     * @param data Map data
     * @param title ExcelTitle
     */
    public static void setCellTypeAndValue(HSSFCell cell, Map<String, Object> data, ExcelTitle title){
        Object object = data.get(title.getFieldName());
        if(object instanceof String){
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(object == null ? "" : String.valueOf(object));
        }else if(object instanceof Integer){
            if(title.getReplace().length>0){
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue((String)replaceValue(title.getReplace(),String.valueOf(object)));
            }else{
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(object == null ? 0 : (Integer)object);
            }
        }else if(object instanceof Float){
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(object == null ? 0.0 : (Float)object);
        }else if(object instanceof Long){
            if(title.getFormatTime()!= null){
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(DateTimeUtil.long2Str((Long)object,title.getFormatTime()));
            }else{
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(object == null ? 0.0 : (Long)object);
            }
        }else if(object instanceof Boolean){
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(object == null ? false : (Boolean)object);
        }else if(object instanceof Double){
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(object == null ? 0.0 : (Double)object);
        }else if(object instanceof BigDecimal){
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(object == null ? 0 : Double.parseDouble(object.toString()));
        }
    }

    private static Object replaceValue(String[] replace, String value) {
        String[] temp;
        for (String str : replace) {
            temp = str.split("_");
            if (value.equals(temp[1])) {
                value = temp[0];
                break;
            }
        }
        return value;
    }
}
