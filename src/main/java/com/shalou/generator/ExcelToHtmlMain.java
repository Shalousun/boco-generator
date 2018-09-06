package com.shalou.generator;

import com.shalou.generator.util.BeetlTemplateUtil;
import com.shalou.generator.util.PropertiesUtil;
import com.power.common.util.FileUtil;
import com.power.poi.excel.ExcelToHtml;
import org.beetl.core.Template;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Created by yu on 2017/6/27.
 */
public class ExcelToHtmlMain {

    public static void main(String[] args) throws IOException {
        Properties properties = PropertiesUtil.loadFromFile("generator.properties");
        String excelFile = properties.getProperty("generator.excel");
        String outputPath = properties.getProperty("generator.outDir");
        Map<String,StringBuffer> bufferMap = ExcelToHtml.excelToHtml(excelFile);

        Template template;
        for(Map.Entry<String,StringBuffer> entry:bufferMap.entrySet()){
            template = BeetlTemplateUtil.getByName("form-table-view.btl");
            template.binding("title",entry.getKey());
            template.binding("table",entry.getValue());
            FileUtil.writeFile(template.render(),outputPath+"/"+entry.getKey()+".html",false);
        }
    }
}
