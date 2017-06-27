package com.boco.generator.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author sunyu
 */
public class ExcelExportStyle {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     *
     * @param workbook POI HSSFWorkbook
     * @param isWrap is wrap
     * @return HSSFCellStyle
     */
    public static HSSFCellStyle getTitleStyle(HSSFWorkbook workbook,boolean isWrap) {
        // create cell style
        HSSFCellStyle style = getCommonStyle(workbook);
        if(isWrap){
            style.setWrapText(true);
        }
        // set cell border style
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // set groundColor
        // style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        // style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return style;
    }

    /**
     *
     * @param workbook HSSFWorkbook
     * @return HSSFCellStyle
     */
    public static HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook) {
        // create cell style
        HSSFCellStyle style = getCommonStyle(workbook);
        // set cell border style
        // style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return style;
    }

    /**
     * create notes style
     * @param workbook HSSFWorkbook
     * @return HSSFCellStyle
     */
    public static HSSFCellStyle createNoteStyle(HSSFWorkbook workbook) {
        // create cell style
        HSSFCellStyle style = getCommonStyle(workbook);
        // set cell border style
        // style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        return style;
    }

    /**
     *
     * @param workbook HSSFWorkbook
     * @param isWrap is wrap
     * @return HSSFCellStyle
     */
    public static HSSFCellStyle getBodyStyle(HSSFWorkbook workbook,boolean isWrap) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        if(isWrap){
            style.setWrapText(true);
        }
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return style;
    }

    /**
     * create common cell style
     * @param workbook HSSFWorkbook
     * @return HSSFCellStyle
     */
    private static HSSFCellStyle getCommonStyle(HSSFWorkbook workbook){
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT));
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

}
