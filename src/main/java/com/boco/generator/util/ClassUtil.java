package com.boco.generator.util;

import com.boco.generator.filter.FileNameFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ClassUtil {
    /**
     * 根据包名的到该报下的类
     *
     * @param modelPackage String
     * @param root         String
     * @return List
     */
    public static List<Class> getClasses(String modelPackage, String root) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(root.replaceAll("\\\\", "/"));
        buffer.append("/src/main/java/");
        buffer.append(modelPackage.replace(".", "/"));
        List<Class> list = new ArrayList<>();
        File entryFile = new File(buffer.toString());
        File[] eFiles = entryFile.listFiles(new FileNameFilter("java"));
        try {
            for (File ef : eFiles) {
                String alName = ef.getName();
                String name = alName.split("\\.")[0];
                Class<?> c = Class.forName(modelPackage + "." + name);
                list.add(c);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
