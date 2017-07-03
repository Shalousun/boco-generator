package com.boco.generator.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yu on 2016/11/24.
 */
public class ExcelToHtml {

    /**
     * 将excle转化成html
     * @param fileName
     *          文件名称
     * @return
     */
    public static Map<String,StringBuffer> excelToHtml(final String fileName){
        Workbook workbook = ExcelWorkBook.createWorkbook(fileName);
        return ExcelToHtml.excelToHtml(workbook);
    }

    /**
     * 将excel转化成html
     * @param workbook
     * @return
     */
    private static Map<String, StringBuffer> excelToHtml(Workbook workbook) {
        //get total of sheets
        int totalSheets = workbook.getNumberOfSheets();
        //封装每个sheet的数据
        Map<String, StringBuffer> map = new HashMap<>(totalSheets);

        Sheet sheet;
        StringBuffer sheetBuffer;
        for (int sheetIndex = 0; sheetIndex < totalSheets; sheetIndex++) {
            sheetBuffer = new StringBuffer();
            String sheetName = workbook.getSheetName(sheetIndex); // sheetName
            if (workbook.getSheetAt(sheetIndex) != null) {
                sheet = workbook.getSheetAt(sheetIndex);// 获得不为空的这个sheet
                if (sheet != null) {
                    int firstRowNum = sheet.getFirstRowNum(); // 第一行
                    int lastRowNum = sheet.getLastRowNum(); // 最后一行
                    sheetBuffer.append("<table width=\"100%\" class=\"form-table\">\n");
                    for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                        if (sheet.getRow(rowNum) != null) {// 如果行不为空，
                            Row row = sheet.getRow(rowNum);
                            if(row.getFirstCellNum()<0){
                                throw new RuntimeException("The "+rowNum+" row is empty in the  sheet of "+sheetName);
                            }
                            int height = (int) (row.getHeight() / 15.625); // 行的高度
                            sheetBuffer.append("    <tr height=\"" + height + "\" class=\"form-table-tr\">\n");
                            createTD(sheetBuffer, sheet, rowNum);
                            sheetBuffer.append("    </tr>\n");
                        }
                    }
                    sheetBuffer.append("</table>");
                }
            }
            map.put(sheetName, sheetBuffer);
        }
        return map;
    }

    /**
     * 创建td
     *
     * @param buffer
     * @param sheet
     * @param rowNum
     */
    private static void createTD(StringBuffer buffer, Sheet sheet, int rowNum) {
        Row row = sheet.getRow(rowNum);
        short firstCellNum = row.getFirstCellNum(); // 该行的第一个单元格
        short lastCellNum = row.getLastCellNum(); // 该行的最后一个单元格
        // 循环该行的每一个单元格
        for (short cellNum = firstCellNum; cellNum <= lastCellNum; cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null) {
                if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                    continue;
                } else {
                    StringBuffer tdStyle = new StringBuffer("       <td class=\"form-table-td");

                    CellStyle cellStyle = cell.getCellStyle();
                    buffer.append(tdStyle + "\"");

                    int width = (int) (sheet.getColumnWidth(cellNum) / 35.7); //
                    int cellRegionCol = ExcelPubUtil.getMergerCellRegionCol(sheet, rowNum, cellNum);
                    int cellRegionRow = ExcelPubUtil.getMergerCellRegionRow(sheet, rowNum, cellNum);// 合并的行（rowspan）

                    String align = ExcelPubUtil.convertAlignToHtml(cellStyle.getAlignment()); //
                    String vAlign = ExcelPubUtil.convertVerticalAlignToHtml(cellStyle.getVerticalAlignment());

                    buffer.append(" align=\"").append(align).append("\"");
                    buffer.append(" valign=\"").append(vAlign).append("\"");
                    buffer.append(" width=\"").append(width).append("\"");

                    buffer.append(" colspan=\"").append(cellRegionCol).append("\"");
                    buffer.append(" rowspan=\"").append(cellRegionRow).append("\"");
                    buffer.append(">").append(getCellValue(cell)).append("</td>\n");

                }
            }
        }
    }

    /**
     * 取得单元格的值
     *
     * @param cell
     * @return
     * @throws IOException
     */
    private static Object getCellValue(Cell cell) {
        Object value = "";
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            value = cell.getRichStringCellValue().toString();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                value = sdf.format(date);
            } else {
                double value_temp = cell.getNumericCellValue();
                BigDecimal bd = new BigDecimal(value_temp);
                BigDecimal bd1 = bd.setScale(3, bd.ROUND_HALF_UP);
                value = bd1.doubleValue();
            }
        }
        if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
            value = "";
        }
        return value;
    }
}
