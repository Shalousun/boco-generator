package com.shalou.generator.util;



import com.power.poi.excel.ExcelToHtml;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yu on 2017/6/26.
 */
public class ExcelToHtmlTest {

    public static void main(String[] args) throws IOException {
        String excelFileName = "d:/test2.xls";
        Map<String,StringBuffer> map = ExcelToHtml.excelToHtml(excelFileName);
        StringBuffer buffer = map.get("Sheet1");
        System.out.println(buffer.toString());
    }
}
