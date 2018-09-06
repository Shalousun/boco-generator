package com.shalou.generator.util;

import com.alibaba.fastjson.JSON;
import com.power.common.util.FileUtil;
import com.power.poi.excel.ExcelImportUtil;
import com.shalou.generator.builder.JavaBeanBuilder;
import com.shalou.generator.model.DataModel;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author yu 2018/9/6.
 */
public class JavaBeanBuilderTest {

    public static void main(String[] args) {
        String excelDir = "d:\\template.xls";
        String outDir = "d:\\javabean";
        String packageName = "com.iflytek.iot.platform.smart.platform.third.ctcc.model";
        createSqlFromExcel(excelDir,outDir,packageName);
    }

    public static void createSqlFromExcel(String excelDir, String outDir,String packageName) {
        File file = new File(excelDir);
        Map<String,String> beanNames = ExcelImportUtil.readExcelIntoMap(file,0,1);
        System.out.println("beanNames:"+beanNames);
        Map<String, List<DataModel>> map = ExcelImportUtil.readExcelIntoMap(file, 2, DataModel.class);
        System.out.println("Json:"+ JSON.toJSONString(map));
        Map<String,String> beanData = JavaBeanBuilder.createBean(map,beanNames,packageName);
        for(Map.Entry<String,String> entry:beanData.entrySet()){
            FileUtil.nioWriteFile(entry.getValue(),outDir+File.separator+entry.getKey()+".java");
        }

    }
}
