package com.boco.generator.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

import java.io.*;

/**
 * @author sunyu
 */
public class ExcelPubUtil {
    private static final String NULL_WORKBOOK = "Excel workbook is null";
    /**
     * @param workbook HSSFWorkbook
     * @return InputStream
     */
    public static InputStream getInputStream(HSSFWorkbook workbook) {
        ByteArrayOutputStream byteOs = null;
        InputStream excelStream;
        try {
            if (workbook == null) {
                return null;
            } else {
                byteOs = new ByteArrayOutputStream();
                workbook.write(byteOs);
                byteOs.flush();
                byte[] bt = byteOs.toByteArray();
                excelStream = new ByteArrayInputStream(bt,0,bt.length);
                byteOs.close();
                return excelStream;
            }
        } catch (IOException e) {
            return null;
        }finally {
            IOUtils.closeQuietly(byteOs);
        }
    }
    /**
     * if client specific OutputStream then use this method
     *
     * @param workbook HSSFWorkbook
     * @param os OutputStream
     * @return boolean
     */
    public static boolean getInputStreamIsSuccess(HSSFWorkbook workbook, OutputStream os) {
        boolean flag = false;
        if (null != workbook) {
            try {
                os.flush();
                workbook.write(os);
                flag = true;
            } catch (IOException e) {
                e.printStackTrace();
                flag = false;
            } finally {
                IOUtils.closeQuietly(os);
            }
        } else {
            throw new NullPointerException(NULL_WORKBOOK);
        }
        return flag;
    }
    /**
     * @param workbook HSSFWorkbook
     * @param file File
     * @return boolean
     */
    public static boolean getInputStreamIsSuccess(HSSFWorkbook workbook, File file) {
        boolean flag = false;
        InputStream inputStream;
        try {
            inputStream = getInputStream(workbook, file);
            if (null != inputStream) {
                flag = true;
            }
        } catch (NullPointerException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
    /**
     * @param workbook HSSFWorkbook
     * @param file File
     * @return InputStream
     * @throws NullPointerException
     */
    public static InputStream getInputStream(HSSFWorkbook workbook, File file) {
        InputStream is = null;
        OutputStream os = null;
        if (null != workbook) {
            try {
                os = new FileOutputStream(file);
                is = new FileInputStream(file);
                os.flush();
                workbook.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
               IOUtils.closeQuietly(os);
            }
        } else {
            throw new NullPointerException(NULL_WORKBOOK);
        }
        return is;
    }
    /**
     * get excel column name
     * @param i index of column
     * @return String
     */
    public final static String getColumnName(int i) {
        i = (i == 0)?1:i;
        String strResult = "";
        int intRound = i / 26;
        int intMod = i % 26;
        if (intRound != 0) {
            strResult = String.valueOf(((char) (intRound + 64)));
        }
        strResult += String.valueOf(((char) (intMod + 64)));
        return strResult;
    }
    /**
     * calculate total of sheet
     *
     * @param totalRecord totalRecord
     * @param size size
     * @return int int
     */
    public final static int totalSheet(int totalRecord, int size) {
        return totalRecord % size == 0 ? totalRecord / size : totalRecord
                / size + 1;
    }
    /**
     * 判断单元格在不在合并单元格范围内，如果是，获取其合并的列数。
     *
     * @param sheet   工作表
     * @param cellRow 被判断的单元格的行号
     * @param cellCol 被判断的单元格的列号
     * @return
     * @throws IOException
     */
    public static int getMergerCellRegionCol(Sheet sheet, int cellRow, int cellCol){
        int retVal = getMergerCellOrRowNum(sheet,cellRow,cellCol,false);
        return retVal;
    }
    /**
     * 判断单元格是否是合并的单格，如果是，获取其合并的行数。
     *
     * @param sheet   表单
     * @param cellRow 被判断的单元格的行号
     * @param cellCol 被判断的单元格的列号
     * @return
     * @throws IOException
     */
    public static int getMergerCellRegionRow(Sheet sheet, int cellRow, int cellCol) {
        int retVal = getMergerCellOrRowNum(sheet,cellRow,cellCol,true);
        return retVal;
    }

    /**
     * 返回合并的行号或者列号,isRow设置为true则返回行号
     * @param sheet 工作表
     * @param cellRow 被判断的单元格行号
     * @param cellCol 被判断的单元格列号
     * @param isRow 对否是行
     * @return
     */
    public static int getMergerCellOrRowNum(Sheet sheet, int cellRow, int cellCol,boolean isRow){
        int retVal = 0;
        int sheetMergerCount = sheet.getNumMergedRegions();
        CellRangeAddress cra1;
        for (int i = 0; i < sheetMergerCount; i++) {
            cra1 = sheet.getMergedRegion(i);
            int firstRow = cra1.getFirstRow(); // 合并单元格CELL起始行
            int firstCol = cra1.getFirstColumn(); // 合并单元格CELL起始列
            int lastRow = cra1.getLastRow(); // 合并单元格CELL结束行
            int lastCol = cra1.getLastColumn(); // 合并单元格CELL结束列
            if (cellRow >= firstRow && cellRow <= lastRow) { // 判断该单元格是否是在合并单元格中
                if (cellCol >= firstCol && cellCol <= lastCol) {
                    if(isRow){
                        retVal = lastRow - firstRow + 1; // 得到合并的行数
                    }else{
                        retVal = lastCol - firstCol + 1; // 得到合并的列数
                    }
                    break;
                }
            }
        }
        return retVal;
    }

    /**
     * 单元格背景色转换
     *
     * @param hc
     * @return
     */
    public static String convertToStardColor(HSSFColor hc) {
        StringBuffer sb = new StringBuffer("");
        if (hc != null) {
            int a = HSSFColor.AUTOMATIC.index;
            int b = hc.getIndex();
            if (a == b) {
                return null;
            }
            sb.append("#");
            for (int i = 0; i < hc.getTriplet().length; i++) {
                String str;
                String str_tmp = Integer.toHexString(hc.getTriplet()[i]);
                if (str_tmp != null && str_tmp.length() < 2) {
                    str = "0" + str_tmp;
                } else {
                    str = str_tmp;
                }
                sb.append(str);
            }
        }
        return sb.toString();
    }

    /**
     * 单元格小平对齐
     *
     * @param alignment
     * @return
     */
    public static String convertAlignToHtml(short alignment) {
        String align = "left";
        switch (alignment) {
            case HSSFCellStyle.ALIGN_LEFT:
                align = "left";
                break;
            case HSSFCellStyle.ALIGN_CENTER:
                align = "center";
                break;
            case HSSFCellStyle.ALIGN_RIGHT:
                align = "right";
                break;
            default:
                break;
        }
        return align;
    }

    /**
     * 单元格垂直对齐
     *
     * @param verticalAlignment
     * @return
     */
    public static String convertVerticalAlignToHtml(short verticalAlignment) {
        String align = "middle";
        switch (verticalAlignment) {
            case HSSFCellStyle.VERTICAL_BOTTOM:
                align = "bottom";
                break;
            case HSSFCellStyle.VERTICAL_CENTER:
                align = "center";
                break;
            case HSSFCellStyle.VERTICAL_TOP:
                align = "top";
                break;
            default:
                break;
        }
        return align;
    }
}
