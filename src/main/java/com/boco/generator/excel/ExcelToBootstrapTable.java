package com.boco.generator.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yu on 2016/11/25.
 */
public class ExcelToBootstrapTable {

    public static Map<String,StringBuilder> generateHtml(InputStream inputStream){

        Workbook workbook = ExcelWorkBook.createWorkbook(inputStream);
        //get total of sheets
        int totalSheets = workbook.getNumberOfSheets();
        //封装每个sheet的数据
        Map<String,StringBuilder> map = new HashMap<>(totalSheets);
        Sheet sheet;
        String sheetName;
        try {
            for (int numSheet = 0; numSheet < totalSheets; numSheet++) {
                sheet = workbook.getSheetAt(numSheet);
                boolean hasMerge = false;//是否有做个合并
                if (null != sheet) {
                    sheetName = sheet.getSheetName();
                    StringBuilder builder = new StringBuilder();
                    builder.append("$('#table').bootstrapTable({\n");
                    builder.append("    url:\"\",\n");
                    builder.append("    method:'get',\n");
                    builder.append("    height: 310,\n");

                    builder.append("    showPaginationSwitch: false,\n");
                    builder.append("    clickToSelect: true,\n");
                    builder.append("    singleSelect: true,\n");
                    builder.append("    pagination:false,//是否显示分页\n");
                    builder.append("    detailView:false,//是否显示详细页模式\n");
                    builder.append("  //sidePagination:'server',//服务器端分页\n");
                    builder.append("  //pageList:[10, 25, 50, 100, All],\n");
                    builder.append("    queryParams: function (params) {\n");
                    builder.append("        var queryParams = {\n");
                    builder.append("           param1:'test'\n");
                    builder.append("        }\n");
                    builder.append("        return queryParams;\n");
                    builder.append("    },\n");
                    builder.append("    columns: [");
                    int firstRowNum = sheet.getFirstRowNum(); // 第一行
                    int lastRowNum = sheet.getLastRowNum(); // 最后一行
                    for(int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++){
                        if(sheet.getRow(rowNum) != null){
                            Row row = sheet.getRow(rowNum);
                            short firstCellNum = row.getFirstCellNum(); // 该行的第一个单元格
                            short lastCellNum = row.getLastCellNum(); // 该行的最后一个单元格
                            //遍历每一个单元格
                            for(short cellNum = firstCellNum; cellNum <= lastCellNum; cellNum++){
                                Cell cell = row.getCell(cellNum);
                                if(null != cell){
                                    if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                                        continue;
                                    } else {
                                        //// 合并的列（solspan）
                                        int cellRegionCol = ExcelPubUtil.getMergerCellRegionCol(sheet, rowNum, cellNum);
                                        ////合并的行（rowspan）
                                        int cellRegionRow = ExcelPubUtil.getMergerCellRegionRow(sheet, rowNum, cellNum);
                                        if(cellRegionCol>0||cellRegionRow>0){
                                            hasMerge = true;
                                            String value = "";
                                            if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                                                value = "";
                                            }else{
                                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                                value = cell.getStringCellValue();
                                            }
                                            if(cellNum==firstCellNum){
                                                builder.append("[");
                                            }
                                            if(cellRegionRow>0&&cellRegionCol==1){
                                                builder.append("{\n");
                                                builder.append("        title:'").append(value).append("',\n");
                                                builder.append("        field:'").append(cellNum).append("',\n");
                                                builder.append("        rowspan:").append(cellRegionRow).append(",\n");
                                                builder.append("        valign: 'middle',\n");
                                                builder.append("        align:'center'\n");
                                                if(cellNum+cellRegionCol<lastCellNum){
                                                    builder.append("    },");
                                                }else{
                                                    builder.append("    }],[");
                                                }
                                            }
                                            if(cellRegionCol>0&&cellRegionRow==1){
                                                builder.append("{\n");
                                                builder.append("        title:'").append(value).append("',\n");
                                                builder.append("        colspan:").append(cellRegionCol).append(",\n");
                                                builder.append("        align: 'center',\n");
                                                if(cellNum+cellRegionCol<lastCellNum){
                                                    builder.append("    },");
                                                }else{
                                                    builder.append("    }],[");
                                                }

                                            }
                                            builder.append("");
                                        }else{
                                            String value = "";
                                            if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                                                value = "";
                                            }else{
                                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                                value = cell.getStringCellValue();
                                            }
                                            value.trim();
                                            builder.append("{\n");
                                            builder.append("        title:'").append(value).append("',\n");
                                            builder.append("        field:'").append(cellNum).append("',\n");
                                            builder.append("        align:'center'\n");
                                            if(cellNum<lastCellNum-1){
                                                builder.append("    },");
                                            }else{
                                                builder.append("    }");
                                            }

                                        }

                                    }
                                }
                            }
                        }
                    }

                    if (',' == builder.charAt(builder.length() - 1)){
                        builder = builder.deleteCharAt(builder.length() - 1);
                    }
                    if(hasMerge){
                        builder.append("]]\n");
                    }else{
                        builder.append("]\n");
                    }
                    builder.append("});\n");
                    map.put(sheetName,builder);
                }
            }
        }catch (Exception e){

        }
        return map;
    }
}
