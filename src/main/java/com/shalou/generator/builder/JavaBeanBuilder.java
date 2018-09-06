package com.shalou.generator.builder;

import com.power.common.util.StringUtil;
import com.shalou.generator.model.DataModel;

import java.util.*;

/**
 * 构建javabean
 *
 * @author yu 2018/9/6.
 */
public class JavaBeanBuilder {


    public static Map<String, String> createBean(Map<String, List<DataModel>> data, Map<String, String> beanNames, String packageName) {
        Map<String, String> beanData = new HashMap<>();
        for (Map.Entry<String, List<DataModel>> entry : data.entrySet()) {
            String key = entry.getKey();
            String beanName = StringUtil.firstToUpperCase(beanNames.get(key));
            List<DataModel> dataModels = entry.getValue();
            StringBuilder builder = new StringBuilder();
            builder.append("package ").append(packageName).append(";\n\n");
            StringBuilder importBuilder = new StringBuilder();
            importBuilder.append("import java.io.Serializable;\n");
            importBuilder.append(generateImport(dataModels));
            builder.append(importBuilder.toString());
            builder.append("public class ").append(beanName).append(" implements Serializable {\n\n");

            builder.append("	private static final long serialVersionUID = ");
            builder.append(String.valueOf(UUID.randomUUID().getLeastSignificantBits())).append("L;\n\n");

            StringBuilder fieldsBuilder = new StringBuilder();

            for (DataModel model : dataModels) {
                String fieldName = StringUtil.firstToLowerCase(model.getParamName());
                if (StringUtil.isNotEmpty(model.getDesc())) {
                    fieldsBuilder.append("	/** \n");
                    fieldsBuilder.append("	 * ").append(model.getDesc()).append("\n");
                    fieldsBuilder.append("	 */\n ");
                }
                String type = StringUtil.firstToUpperCase(model.getType());
                if ("Timestamp".equals(type)) {
                    fieldsBuilder.append("	@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n");
                }
                String required = model.getRequired().toLowerCase();
                if ("y".equals(required) || "true".equals(required)) {
                    if ("String".equals(type)) {
                        fieldsBuilder.append("	@NotBlank(message = \"").append(fieldName);
                        fieldsBuilder.append(" can't be null or empty.\")\n");
                    } else {
                        fieldsBuilder.append("	@NotNull(message = \"").append(fieldName);
                        fieldsBuilder.append(" can't be null.\")\n");
                    }

                }
                if(beanNames.containsValue(type)){
                    builder.append("import ").append(packageName).append(type).append(";\n");
                }

                fieldsBuilder.append("	private ").append(type).append(" ");
                fieldsBuilder.append(StringUtil.underlineToCamel(fieldName));
                fieldsBuilder.append(";\n\n");
            }
            builder.append(fieldsBuilder.toString());

            builder.append("}");
            beanData.put(beanName,builder.toString());
        }
        return beanData;
    }

    private static String generateImport(List<DataModel> dataModels) {
        StringBuilder builder = new StringBuilder();

        List<String> list = new ArrayList<>();
        boolean flag = true;
        for (DataModel model : dataModels) {
            String type = StringUtil.firstToUpperCase(model.getType());
            if ("BigDecimal".equals(type)) {
                list.add("import java.math.BigDecimal;\n");
            }
            if ("Date".equals(type)) {
                list.add("import java.sql.Date;\n");
            }
            if ("Timestamp".equals(type)) {
                list.add("import java.sql.Timestamp;\n");
                flag = true;
            }
            if ("Time".equals(type)) {
                list.add("import java.sql.Time;\n");
            }

            String required = model.getRequired().toLowerCase();
            if ("y".equals(required) || "true".equals(required)) {
                list.add("import javax.validation.constraints.*;\n");
            }

        }
        if (flag) {
            list.add("import com.fasterxml.jackson.annotation.JsonFormat;\n\n");
            Collections.reverse(list);
        }
        Set<String> set = new HashSet<>();
        set.addAll(list);
        for (String str : set) {
            builder.append(str);
        }
        return builder.toString();
    }

    public boolean isPrimary(String type) {
        switch (type) {
            case "Boolean":
                return true;
            case "String":
                return true;
            case "int":
                return true;
            case "Double":
                return true;
            case "Float":
                return true;
            case "byte":
                return true;
            case "Long":
                return true;
            default:
                return false;

        }
    }
}
