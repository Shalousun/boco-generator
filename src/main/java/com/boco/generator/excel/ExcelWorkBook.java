package com.boco.generator.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author sunyu
 */
public class ExcelWorkBook {

    /**
     * create work book By File name
     * @param fileName
     * @return
     */
    public static Workbook createWorkbook(String fileName){
        Workbook workbook = null;
        try {
            FileInputStream is = new FileInputStream(new File(fileName));
            workbook = ExcelWorkBook.createWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("找不到指定的excel文件");
        }
        return workbook;
    }
    /**
     * create work book By File
     *
     * @param file File
     * @return Workbook
     */
    public static Workbook createWorkbook(File file) {
        Workbook workbook = null;
        try {
            FileInputStream is = new FileInputStream(file);
            workbook = ExcelWorkBook.createWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     * create work book by inputStream
     * @param inputStream InputStream
     * @return
     */
    public static Workbook createWorkbook(InputStream inputStream){
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(inputStream);
        }catch (IOException|InvalidFormatException e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(inputStream);
        }
        return workbook;
    }
}
