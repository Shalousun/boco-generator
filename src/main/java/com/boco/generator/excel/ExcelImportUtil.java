package com.boco.generator.excel;

import com.boco.generator.annotation.ExcelAnnotation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunyu
 */
public class ExcelImportUtil {
    private static final String EXCEPTION_MSG = " is unmarked by ExcelAnnotation annotation";
    private static final String SHEET_NAME_ERROR = "(This Sheet name is not exist in your specify excel)";

    /**
     * read record from a specific sheet
     *
     * @param sheetName work sheet
     * @param file      File
     * @param readStart read start row number
     * @param pojoClass Class
     * @return List
     */
    public static <T> List<T> readExcel(String sheetName, File file, int readStart, Class<T> pojoClass) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readExcel(sheetName, inputStream, readStart, pojoClass);
    }

    /**
     * read record from a specific sheet,
     * used sheet name as the HashMap key
     * @param sheetName sheet name
     * @param file File
     * @param readStart read start
     * @param pojoClass Class
     * @return HashMap
     */
    public static <T> Map<String,List<T>> readExcelIntoMap(String sheetName,File file,int readStart,Class<T> pojoClass){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        return readExcelIntoMap(sheetName,inputStream,readStart,pojoClass);

    }

    /**
     * read excel from input stream
     *
     * @param sheetName   work sheet
     * @param inputStream input stream
     * @param readStart   read start row number
     * @return List
     */
    public static <T> List<T> readExcel(String sheetName, InputStream inputStream, int readStart, Class<T> pojoClass) {
        Workbook workbook = ExcelWorkBook.createWorkbook(inputStream);
        List<T> list;
        Map<Integer, Field> fieldsMap = ExcelReflectUtil.getFieldsMap(pojoClass);
        if (fieldsMap.size() <= 0) {
            throw new RuntimeException(pojoClass + EXCEPTION_MSG);
        }
        int totalRow;
        Sheet sheet = workbook.getSheet(sheetName);
        if (null != sheet) {
            totalRow = sheet.getLastRowNum();
        } else {
            throw new NullPointerException(sheetName + SHEET_NAME_ERROR);
        }
        // create list
        list = new ArrayList<>(totalRow);
        importSetValue(list, pojoClass, sheet, fieldsMap, readStart);
        return list;
    }

    /**
     *
     * @param sheetName
     * @param inputStream
     * @param readStart
     * @param pojoClass
     * @param <T>
     * @return
     */
    public static <T> Map<String,List<T>> readExcelIntoMap(String sheetName,InputStream inputStream,int readStart,Class<T> pojoClass){
        Workbook workbook = ExcelWorkBook.createWorkbook(inputStream);
        Map<Integer, Field> fieldsMap = ExcelReflectUtil.getFieldsMap(pojoClass);
        if (fieldsMap.size() <= 0) {
            throw new RuntimeException(pojoClass + EXCEPTION_MSG);
        }
        int totalRow;
        Sheet sheet = workbook.getSheet(sheetName);
        if (null != sheet) {
            totalRow = sheet.getLastRowNum();
        } else {
            throw new NullPointerException(sheetName + SHEET_NAME_ERROR);
        }
        Map<String,List<T>> sheetData = new HashMap<>(1);
        List<T> sheetRowsData = new ArrayList<>(totalRow);
        importSetValue(sheetRowsData,pojoClass,sheet,fieldsMap,readStart);
        sheetData.put(sheetName,sheetRowsData);
        return sheetData;

    }

    /**
     * read all record from excel
     *
     * @param file      File
     * @param readStart read start row number
     * @param pojoClass Class
     * @return List
     */
    public static <T> List<T> readExcel(File file, int readStart, Class<T> pojoClass) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readExcel(inputStream, readStart, pojoClass);
    }

    /**
     * read data from excel
     * @param file File
     * @param readStart read start
     * @param pojoClass Class
     * @return Map<String,List<T>>
     */
    public static <T> Map<String,List<T>> readExcelIntoMap(File file,int readStart,Class<T> pojoClass){
        FileInputStream inputStream = null;
        try{
            inputStream = new FileInputStream(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        return readExcelIntoMap(inputStream,readStart,pojoClass);
    }

    /**
     * readExcel by inputStream
     *
     * @param is        InputStream
     * @param readStart read start
     * @return List<T> List<T>
     */
    public static <T> List<T> readExcel(InputStream is, int readStart, Class<T> pojoClass) {
        Workbook workbook = ExcelWorkBook.createWorkbook(is);
        List<T> list;
        Map<Integer, Field> fieldsMap = ExcelReflectUtil.getFieldsMap(pojoClass);
        if (fieldsMap.size() <= 0) {
            throw new RuntimeException(pojoClass + EXCEPTION_MSG);
        }
        int totalRow = 0;
        Sheet sheet;
        // calculate the total record
        for (int numSheet = 0, len = workbook.getNumberOfSheets(); numSheet < len; numSheet++) {
            sheet = workbook.getSheetAt(numSheet);
            totalRow += sheet.getLastRowNum();
        }
        // create list
        list = new ArrayList<>(totalRow);
        for (int numSheet = 0, len = workbook.getNumberOfSheets(); numSheet < len; numSheet++) {
            sheet = workbook.getSheetAt(numSheet);
            if (null == sheet) {
                continue;
            }
            //binding data to entity and populate entity into list
            importSetValue(list, pojoClass, sheet, fieldsMap, readStart);
        }
        return list;
    }

    /**
     * read data from excel
     * @param inputStream InputStream
     * @param readStart read start
     * @param pojoClass Class
     * @return Map<String,List<T>>
     */

    public static <T> Map<String, List<T>> readExcelIntoMap(InputStream inputStream, int readStart, Class<T> pojoClass) {
        Workbook workbook = ExcelWorkBook.createWorkbook(inputStream);
        Map<Integer, Field> fieldsMap = ExcelReflectUtil.getFieldsMap(pojoClass);
        if (fieldsMap.size() <= 0) {
            throw new RuntimeException(pojoClass + EXCEPTION_MSG);
        }
        //get total of sheets
        int totalSheets = workbook.getNumberOfSheets();
        //init map,the key is the sheet name,the value is the every sheet rows data
        Map<String, List<T>> sheetDataMap = new HashMap<>(totalSheets);
        Sheet sheet;
        //populate every sheet's rows data
        List<T> sheetRowsData;
        int everSheetTotalRows = 0;
        String sheetName;
        for (int numSheet = 0; numSheet < totalSheets; numSheet++) {
            sheet = workbook.getSheetAt(numSheet);
            if (null == sheet) {
                continue;
            }
            sheetName = sheet.getSheetName();
            everSheetTotalRows = sheet.getLastRowNum();
            sheetRowsData = new ArrayList<>(everSheetTotalRows);
            importSetValue(sheetRowsData, pojoClass, sheet, fieldsMap, readStart);
            sheetDataMap.put(sheetName, sheetRowsData);
        }
        return sheetDataMap;

    }

    /**
     * binding data to entity and populate into list
     *
     * @param list      List<T>
     * @param clazz     class
     * @param sheet     HSSFSheet
     * @param fieldsMap entity annotated fields
     * @param readStart read start
     */
    private static <T> void importSetValue(List<T> list, Class<T> clazz, Sheet sheet, Map<Integer, Field> fieldsMap, int readStart) {
       // readStart = (readStart < 1) ? 1 : readStart;//min start 1
    	int  rowNum=0;
  	  	int size=0;
    	try {
    	  
    	for ( rowNum = readStart, size = sheet.getLastRowNum(); rowNum <= size; rowNum++) {
            Row row = sheet.getRow(rowNum);
            boolean flag0 = isRowEmpty(row, fieldsMap);
            if (null == row || flag0) {
                continue;
            }
            T entity = null;
            boolean flag = false;
            try {
                entity = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            ExcelAnnotation attr;
            for (Field field : fieldsMap.values()) {
                attr = field.getAnnotation(ExcelAnnotation.class);
                Cell cell = row.getCell(attr.columnNum());
                String value = "";
                if (cell != null) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    value = cell.getStringCellValue();
                }

                Class<?> fieldType = field.getType();
                // populate value into object
                flag = ExcelReflectUtil.setValue(entity, value, field, fieldType);
            }
            if (flag && entity != null) {
                list.add(entity);
            }
        }
       } catch (Exception e) {
   		// TODO: handle exception
    	   e.printStackTrace();
    	   System.out.println("sheet:"+sheet.getSheetName()+"====rowNum:"+rowNum);
       }
    }

    /**
     * check Empty
     *
     * @param row       excelRow
     * @param fieldsMap entity annotated fields
     * @return boolean
     */
    private static boolean isRowEmpty(Row row, Map<Integer, Field> fieldsMap) {
        List<String> errors = new ArrayList<>();
        for (int i = 0, length = fieldsMap.size(); i < length; i++) {
            Cell cell = row.getCell(i);
            String value = "";
            if (cell != null) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                value = cell.getStringCellValue();
            }
            if (value == null || "".equals(value)) {
                errors.add("error");
            }
        }
        return (errors.size() >= fieldsMap.size());
    }

}
