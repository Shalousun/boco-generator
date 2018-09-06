package com.shalou.generator.builder;


import com.shalou.generator.annotation.Table;
import com.power.common.util.StringUtil;

import java.lang.reflect.Field;

public class MyBatisSqlBuilder {
    private static final String EXCEPTION_MSG = " is unmarked by Table annotation";

    public static String insertSql(Class clazz) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            String simpleName = getTableName(clazz);
            StringBuffer sql = new StringBuffer();
            sql.append("insert into ").append(simpleName).append("(");
            for (int i = 0; i < fields.length; i++) {
                if (!fields[i].getName().equalsIgnoreCase(fields[0].getName())) {
                    if (!fields[i].getType().getName().equals("java.util.List")) {
                        sql.append(StringUtil.camelToUnderline(fields[i].getName())).append(",");
                    } else {
                        //sql.append(StringUtil.camelToUnderline(fields[i].getName())).append(",");
                    }
//					if(fields[i].isAnnotationPresent(AssociationField.class)){
//						AssociationField ano = fields[i].getAnnotation(AssociationField.class);
//						sql.append(StringUtil.camelToUnderline(ano.column())).append(",");
//					}else{
//						sql.append(StringUtil.camelToUnderline(fields[i].getName())).append(",");
//					}

                }
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")\n").append("		values(");
            for (int i = 1; i < fields.length; i++) {
                if (!fields[i].getType().getName().equals("java.util.List")) {
                    sql.append("#{");
                    sql.append(fields[i].getName());
                    sql.append("},");
                }
//				if(fields[i].isAnnotationPresent(AssociationField.class)){
//					AssociationField ano = fields[i].getAnnotation(AssociationField.class);
//					sql.append(fields[i].getName()).append(".");
//					sql.append(ano.refField());
//				}else{
//					sql.append(fields[i].getName());
//				}
                //sql.append("},");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String batchInsert(Class clazz) {
        StringBuilder sql = new StringBuilder();
        try {
            Field[] fields = clazz.getDeclaredFields();
            String simpleName = getTableName(clazz);
            sql.append("insert into ").append(simpleName).append("(");
            for (int i = 0; i < fields.length; i++) {
                if (!fields[i].getName().equalsIgnoreCase(fields[0].getName())) {
                    sql.append(StringUtil.camelToUnderline(fields[i].getName())).append(",");
                }
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")\n").append("		values \n");
            sql.append("		<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\"> \n");
            sql.append("			(");
            for (int i = 1; i < fields.length; i++) {
                sql.append("#{item.");
                sql.append(fields[i].getName());
                sql.append("},");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")\n");
            sql.append("		</foreach>\n");
            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String deleteSql(Class clazz) {
        try {
            String simpleName = getTableName(clazz);
            StringBuffer sql = new StringBuffer();
            Field[] fields = clazz.getDeclaredFields();
            sql.append("delete from ").append(simpleName);
            sql.append(" where ").append(fields[0].getName()).append(" = #{");
            sql.append(fields[0].getName()).append("}");
            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String findByIdSql(Class clazz) {
        try {
            String simpleName = getTableName(clazz);
            StringBuffer sql = new StringBuffer();
            Field[] fields = clazz.getDeclaredFields();
            sql.append("select ").append(fields[0].getName());
            for (int i = 1; i < fields.length; i++) {
                String toUnderLine = StringUtil.camelToUnderline(fields[i].getName());
                String noUnderLine = fields[i].getName();
                if (!toUnderLine.endsWith(noUnderLine)) {
                    sql.append(",").append(toUnderLine).append(" as ").append(noUnderLine);
                } else {
                    sql.append(",").append(noUnderLine);
                }

            }
            sql.append(" from ");
            sql.append(simpleName);
            sql.append(" where ").append(fields[0].getName());
            sql.append("=#{").append(fields[0].getName());
            sql.append("}");
            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String findAllSql(Class clazz) {
        try {
            String simpleName = getTableName(clazz);
            StringBuffer sql = new StringBuffer();
            Field[] fields = clazz.getDeclaredFields();
            sql.append("select ").append(fields[0].getName());
            for (int i = 1; i < fields.length; i++) {
                sql.append(",").append(StringUtil.camelToUnderline(fields[i].getName()));
            }
            sql.append(" from ");
            sql.append(simpleName);
            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String updateSql(Class clazz) {
        try {
            String simpleName = getTableName(clazz);
            StringBuffer sql = new StringBuffer();
            sql.append("update ").append(simpleName);
            sql.append(" set \n");
            Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                if (!fields[i].isAccessible())
                    fields[i].setAccessible(true);
                if (!fields[i].getName().equals(fields[0].getName())) {
                    sql.append("		");
                    sql.append(StringUtil.camelToUnderline(fields[i].getName())).append("=#{");
                    sql.append(fields[i].getName()).append("}").append(",\n");
                }
            }
            sql.deleteCharAt(sql.lastIndexOf(","));
            sql.append(" ");
            sql.append("		where ").append(fields[0].getName()).append(" = #{");
            sql.append(fields[0].getName()).append("}");
            return sql.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static String getTableName(Class clazz) {
        String str;
        Table annotation = (Table) clazz.getAnnotation(Table.class);
        if (annotation != null) {
            str = annotation.name();
        } else {
            str = "t" + StringUtil.camelToUnderline(clazz.getSimpleName());
        }
        return str;
    }
}
