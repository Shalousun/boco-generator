package com.boco.generator.excel;

/**
 * @author sunyu
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boco.generator.annotation.ExcelAnnotation;
import com.boco.generator.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;


public class ExcelUtil<T> {
	private static final String EXCEPTION_MSG = " is unmarked by ExcelAnnotion annotation";
	private static final String NULL_MSG = " The collection of List is null";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String FONT_NAME = "宋体";
	private static final String SHEET_NAME_ERROR = "(This Sheet name is not exist in your specify excel)";
	private static final String HEADER_CONTENT_ERROR = "Header content can not be null";
	private static final String NULL_WORKBOOK = "Excel workbook is null";
	private static final int SHEET_SIZE = 6500;
	Class<T> clazz;

	public ExcelUtil(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * this method could auto Merged a header area
	 * 
	 * @param list
	 *            datas
	 * @param sheetName
	 * 
	 * @param headerContent
	 *            content of header
	 * @return
	 */
	public InputStream generateExcel(List<T> list, String sheetName,
			String headerContent) {
		HSSFWorkbook workbook = null;
		if (null == list) {
			throw new NullPointerException(NULL_MSG);
		}
		if (StringUtil.isEmpty(headerContent)) {
			throw new NullPointerException(HEADER_CONTENT_ERROR);
		} else {
			workbook = new HSSFWorkbook();
			// set style of title
			HSSFCellStyle titleStyle = this.createTitleStyle(workbook);
			// set font style of title
			HSSFFont titleFont = this.setStyleOfTitleFont(workbook);
			titleStyle.setFont(titleFont);
			// create style of header
			HSSFCellStyle headerStyle = this.createHeaderStyle(workbook);
			// create style of header font
			HSSFFont headerFont = this.createStyleOfHeaderFont(workbook);
			headerStyle.setFont(headerFont);
			// get the fields that marked by ExcelAnnotation
			List<Field> fields = this.getFields(clazz);
			if (fields.size() <= 0) {
				throw new RuntimeException(clazz + EXCEPTION_MSG);
			}
			// create body style
			HSSFCellStyle bodyStyle = this.createBodyStyle(workbook);
			// set default size of a sheet
			int sheetSize = SHEET_SIZE;

			// Take out a total of how much a sheet
			int sheetNo = this.totalSheet(list.size(), sheetSize);

			for (int index = 0; index < sheetNo; index++) {
				// generate Excel sheet
				HSSFSheet sheet = workbook.createSheet();
				// set Excel sheet name
				workbook.setSheetName(index, sheetName + (index + 1));
				HSSFRow row;

				row = sheet.createRow(0);
				// set header
				this.setHeader(headerStyle, sheet, row, fields.size() - 1,
						headerContent);
				// create area of header
				row = sheet.createRow(1);
				// set content of header
				this.setTitle(fields, titleStyle, sheet, row);

				int startNo = index * sheetSize;
				int endNo = Math.min(startNo + sheetSize, list.size());
				// Write each record, each record corresponds to a row in the
				// excel
				// table
				for (int i = startNo; i < endNo; i++) {
					row = sheet.createRow(i + 2 - startNo);
					// obtain export object
					T t = (T) list.get(i);
					// set body
					this.setBody(fields, t, bodyStyle, row);
				}
			}
		}
		return getInputStream(workbook);
	}

	/**
	 * if you use struts framework ,you could use this method you must put list
	 * of data,name of excel sheet and file
	 * 
	 * @param list
	 * @param sheetName
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws NullPointerException
	 */
	public InputStream generateExcel(List<T> list, String sheetName) {
		HSSFWorkbook workbook = null;
		if (null == list) {
			throw new NullPointerException(NULL_MSG);
		} else {
			workbook = new HSSFWorkbook();
			// set style of title
			HSSFCellStyle headerStyle = this.createTitleStyle(workbook);
			// set font style of title
			HSSFFont headerFont = this.setStyleOfTitleFont(workbook);
			headerStyle.setFont(headerFont);
			// get the fields that marked by ExcelAnnotation
			List<Field> fields = this.getFields(clazz);
			if (fields.size() <= 0) {
				throw new RuntimeException(clazz + EXCEPTION_MSG);
			}
			// create body style
			HSSFCellStyle bodyStyle = this.createBodyStyle(workbook);
			// set default size of a sheet
			int sheetSize = SHEET_SIZE;

			// Take out a total of how much a sheet
			int sheetNo = this.totalSheet(list.size(), sheetSize);

			for (int index = 0; index < sheetNo; index++) {
				// generate Excel sheet
				HSSFSheet sheet = workbook.createSheet();
				// set Excel sheet name
				workbook.setSheetName(index, sheetName + (index + 1));
				HSSFRow row;
				// create area of header
				row = sheet.createRow(0);
				// set content of header
				this.setTitle(fields, headerStyle, sheet, row);

				int startNo = index * sheetSize;
				int endNo = Math.min(startNo + sheetSize, list.size());
				// Write each record, each record corresponds to a row in the
				// excel
				// table
				for (int i = startNo; i < endNo; i++) {
					row = sheet.createRow(i + 1 - startNo);
					// obtain export object
					T t = (T) list.get(i);
					// set body
					this.setBody(fields, t, bodyStyle, row);
				}
			}

		}
		return getInputStream(workbook);
	}

	/**
	 * Used for servlet or spring mvc
	 * resp.setContentType("application/vnd.ms-excel;charset=utf-8");
	 * resp.setHeader("Content-Disposition", "attachment;filename=" + new
	 * String("video.xls".getBytes(), "iso-8859-1")); ServletOutputStream os =
	 * resp.getOutputStream();
	 * 
	 * input sheetName ,ServletOutputStream,dataList
	 * 
	 * @param sheetName
	 * @param out
	 * @param list
	 * @return
	 */
	public boolean exportExcel(String sheetName, OutputStream out, List<T> list) {
		HSSFWorkbook workbook = null;
		if (null == list) {
			throw new NullPointerException(NULL_MSG);
		} else {
			workbook = new HSSFWorkbook();
			// set style of title
			HSSFCellStyle headerStyle = this.createTitleStyle(workbook);
			// set font style of title
			HSSFFont headerFont = this.setStyleOfTitleFont(workbook);
			headerStyle.setFont(headerFont);
			// get the fields that marked by ExcelAnnotation
			List<Field> fields = this.getFields(clazz);
			if (fields.size() <= 0) {
				throw new RuntimeException(clazz + EXCEPTION_MSG);
			}
			// create body style
			HSSFCellStyle bodyStyle = this.createBodyStyle(workbook);
			// set default size of a sheet
			int sheetSize = SHEET_SIZE;

			// Take out a total of how much a sheet
			int sheetNo = this.totalSheet(list.size(), sheetSize);

			for (int index = 0; index < sheetNo; index++) {
				// generate Excel sheet
				HSSFSheet sheet = workbook.createSheet();
				// set Excel sheet name
				workbook.setSheetName(index, sheetName + (index + 1));
				HSSFRow row;
				// create area of title
				row = sheet.createRow(0);
				// set content of title
				this.setTitle(fields, headerStyle, sheet, row);

				int startNo = index * sheetSize;
				int endNo = Math.min(startNo + sheetSize, list.size());
				// Write each record, each record corresponds to a row in the
				// excel
				// table
				for (int i = startNo; i < endNo; i++) {
					row = sheet.createRow(i + 1 - startNo);
					// obtain export object
					T t = (T) list.get(i);
					// set body
					this.setBody(fields, t, bodyStyle, row);
				}
			}

		}
		return getInputStream(workbook, out);
	}

	/**
	 * Used for servlet or spring mvc
	 * resp.setContentType("application/vnd.ms-excel;charset=utf-8");
	 * resp.setHeader("Content-Disposition", "attachment;filename=" + new
	 * String("video.xls".getBytes(), "iso-8859-1")); ServletOutputStream os =
	 * resp.getOutputStream();
	 * 
	 * sheetName,headerContent,ServletOutputStream,dataList
	 * 
	 * @param sheetName
	 *            工作表名称
	 * @param headerContent
	 *            标题头
	 * @param out
	 *            输出流
	 * @param list
	 *            导出数据
	 * @return
	 */
	public boolean exportExcelWithHeader(String sheetName,
			String headerContent, OutputStream out, List<T> list) {
		HSSFWorkbook workbook = null;
		if (null == list) {
			throw new NullPointerException(NULL_MSG);
		}
		if (StringUtil.isEmpty(headerContent)) {
			throw new NullPointerException(HEADER_CONTENT_ERROR);
		} else {
			workbook = new HSSFWorkbook();
			// set style of title
			HSSFCellStyle titleStyle = this.createTitleStyle(workbook);
			// set font style of title
			HSSFFont titleFont = this.setStyleOfTitleFont(workbook);
			titleStyle.setFont(titleFont);
			// create style of header
			HSSFCellStyle headerStyle = this.createHeaderStyle(workbook);
			// create style of header font
			HSSFFont headerFont = this.createStyleOfHeaderFont(workbook);
			headerStyle.setFont(headerFont);
			// get the fields that marked by ExcelAnnotation
			List<Field> fields = this.getFields(clazz);
			if (fields.size() <= 0) {
				throw new RuntimeException(clazz + EXCEPTION_MSG);
			}
			// create body style
			HSSFCellStyle bodyStyle = this.createBodyStyle(workbook);
			// set default size of a sheet
			int sheetSize = SHEET_SIZE;

			// Take out a total of how much a sheet
			int sheetNo = this.totalSheet(list.size(), sheetSize);

			for (int index = 0; index < sheetNo; index++) {
				// generate Excel sheet
				HSSFSheet sheet = workbook.createSheet();
				// set Excel sheet name
				workbook.setSheetName(index, sheetName + (index + 1));
				HSSFRow row;

				row = sheet.createRow(0);
				// set header
				this.setHeader(headerStyle, sheet, row, fields.size() - 1,
						headerContent);
				// create area of header
				row = sheet.createRow(1);
				// set content of header
				this.setTitle(fields, titleStyle, sheet, row);

				int startNo = index * sheetSize;
				int endNo = Math.min(startNo + sheetSize, list.size());
				// Write each record, each record corresponds to a row in the
				// excel
				// table
				for (int i = startNo; i < endNo; i++) {
					row = sheet.createRow(i + 2 - startNo);
					// obtain export object
					T t = (T) list.get(i);
					// set body
					this.setBody(fields, t, bodyStyle, row);
				}
			}
		}
		return getInputStream(workbook, out);
	}

	public boolean exportExcelWithHeader(String sheetEndding,
			String headerContent, OutputStream out, Map<String, List<T>> map) {
		HSSFWorkbook workbook = null;
		if (null == map) {
			throw new NullPointerException(NULL_MSG);
		}
		if (StringUtil.isEmpty(headerContent)) {
			throw new NullPointerException(HEADER_CONTENT_ERROR);
		} else {
			workbook = new HSSFWorkbook();
			// set style of title
			HSSFCellStyle titleStyle = this.createTitleStyle(workbook);
			// set font style of title
			HSSFFont titleFont = this.setStyleOfTitleFont(workbook);
			titleStyle.setFont(titleFont);
			// create style of header
			HSSFCellStyle headerStyle = this.createHeaderStyle(workbook);
			// create style of header font
			HSSFFont headerFont = this.createStyleOfHeaderFont(workbook);
			headerStyle.setFont(headerFont);
			// get the fields that marked by ExcelAnnotation
			List<Field> fields = this.getFields(clazz);
			if (fields.size() <= 0) {
				throw new RuntimeException(clazz + EXCEPTION_MSG);
			}
			// create body style
			HSSFCellStyle bodyStyle = this.createBodyStyle(workbook);
			for (Map.Entry<String, List<T>> entry : map.entrySet()) {
				// generate Excel sheet
				HSSFSheet sheet = workbook.createSheet(entry.getKey());
				HSSFRow row;
				// create first row
				row = sheet.createRow(0);
				// set header
				this.setHeader(headerStyle, sheet, row, fields.size() - 1,
						headerContent);
				// create area of header
				row = sheet.createRow(1);
				// set content of header
				this.setTitle(fields, titleStyle, sheet, row);
				List<T> list = entry.getValue();
				for (int i = 0; i < list.size(); i++) {
					row = sheet.createRow(i + 2);
					// obtain export object
					T t = (T) list.get(i);
					// set body
					this.setBody(fields, t, bodyStyle, row);
				}
			}
		}
		return getInputStream(workbook, out);
	}

	/**
	 * 
	 * @param sheetName
	 *            sheet name
	 * @param headerContent
	 *            header
	 * @param note
	 *            note
	 * @param out
	 *            out
	 * @param list
	 *            list
	 * @return
	 */
	public boolean exportExcelWithHeaderAndNote(String sheetName,
			String headerContent, String note, OutputStream out, List<T> list) {
		HSSFWorkbook workbook = null;
		if (null == list) {
			throw new NullPointerException(NULL_MSG);
		}
		if (StringUtil.isEmpty(headerContent)) {
			throw new NullPointerException(HEADER_CONTENT_ERROR);
		} else {
			workbook = new HSSFWorkbook();
			// set style of title
			HSSFCellStyle titleStyle = this.createTitleStyle(workbook);
			// set font style of title
			HSSFFont titleFont = this.setStyleOfTitleFont(workbook);
			titleStyle.setFont(titleFont);
			// create style of header
			HSSFCellStyle headerStyle = this.createHeaderStyle(workbook);
			HSSFCellStyle noteStyle = this.createNoteStyle(workbook);
			// create style of header font
			HSSFFont headerFont = this.createStyleOfHeaderFont(workbook);
			headerStyle.setFont(headerFont);
			// get the fields that marked by ExcelAnnotation
			List<Field> fields = this.getFields(clazz);
			if (fields.size() <= 0) {
				throw new RuntimeException(clazz + EXCEPTION_MSG);
			}
			// create body style
			HSSFCellStyle bodyStyle = this.createBodyStyle(workbook);
			// set default size of a sheet
			int sheetSize = SHEET_SIZE;

			// Take out a total of how much a sheet
			int sheetNo = this.totalSheet(list.size(), sheetSize);

			for (int index = 0; index < sheetNo; index++) {
				// generate Excel sheet
				HSSFSheet sheet = workbook.createSheet();
				// set Excel sheet name
				workbook.setSheetName(index, sheetName + (index + 1));
				HSSFRow row;

				row = sheet.createRow(0);
				// set header
				this.setHeader(headerStyle, sheet, row, fields.size() - 1,
						headerContent);

				// create note
				row = sheet.createRow(1);
				this.annotate(noteStyle, sheet, row, fields.size() - 1, note);
				// create area of header
				row = sheet.createRow(2);
				// set content of header
				this.setTitle(fields, titleStyle, sheet, row);

				int startNo = index * sheetSize;
				int endNo = Math.min(startNo + sheetSize, list.size());
				// Write each record, each record corresponds to a row in the
				// excel
				// table
				for (int i = startNo; i < endNo; i++) {
					row = sheet.createRow(i + 3 - startNo);
					// obtain export object
					T t = (T) list.get(i);
					// set body
					this.setBody(fields, t, bodyStyle, row);
				}
			}
		}
		return getInputStream(workbook, out);
	}

	/**
	 * 
	 * @param sheetName
	 *            sheetName
	 * @param headerContent
	 *            header
	 * @param note
	 *            note
	 * @param out
	 *            OutputStream
	 * @param map
	 *            map
	 * @param sumContent
	 * 			  sumContent
	 * @return
	 */
	public boolean exportExcelWithHeaderAndNote(String sheetName,
			String headerContent, String note, OutputStream out,
			Map<String, List<T>> map,String sumContent) {
		HSSFWorkbook workbook = null;
		if (null == map) {
			throw new NullPointerException(NULL_MSG);
		}
		if (StringUtil.isEmpty(headerContent)) {
			throw new NullPointerException(HEADER_CONTENT_ERROR);
		} else {
			workbook = new HSSFWorkbook();
			// set style of title
			HSSFCellStyle titleStyle = this.createTitleStyle(workbook);
			// set font style of title
			HSSFFont titleFont = this.setStyleOfTitleFont(workbook);
			titleStyle.setFont(titleFont);
			// create style of header
			HSSFCellStyle headerStyle = this.createHeaderStyle(workbook);
			HSSFCellStyle noteStyle = this.createNoteStyle(workbook);
			// create style of header font
			HSSFFont headerFont = this.createStyleOfHeaderFont(workbook);
			headerStyle.setFont(headerFont);
			// get the fields that marked by ExcelAnnotation
			List<Field> fields = this.getFields(clazz);
			if (fields.size() <= 0) {
				throw new RuntimeException(clazz + EXCEPTION_MSG);
			}
			// create body style
			HSSFCellStyle bodyStyle = this.createBodyStyle(workbook);
			for (Map.Entry<String, List<T>> entry : map.entrySet()) {
				// generate Excel sheet
				HSSFSheet sheet = workbook.createSheet(entry.getKey());
				HSSFRow row;

				row = sheet.createRow(0);

				// set header
				this.setHeader(headerStyle, sheet, row, fields.size() - 1,
						headerContent);
				// create note
				row = sheet.createRow(1);
				this.annotate(noteStyle, sheet, row, fields.size() - 1, note);
				// create area of header
				row = sheet.createRow(2);
				// set content of header
				this.setTitle(fields, titleStyle, sheet, row);
				// get value from map
				List<T> list = entry.getValue();
				int size = list.size();
				for (int i = 0; i < size; i++) {
					row = sheet.createRow(i + 3);
					row.setHeightInPoints(25);
					// obtain export object
					T t = (T) list.get(i);
					// set body
					this.setBody(fields, t, bodyStyle, row);
				}
				row = sheet.createRow(size + 3);
				this.annotate(headerStyle, sheet, row, fields.size()-1,sumContent);
				row = sheet.createRow(size + 4);
				this.setTotal(fields, bodyStyle, row, 3,size+4);
			}
		}
		return getInputStream(workbook, out);
	}

	/**
	 * import data from excel,if not specific sheet name import all record else
	 * import record from specific sheet
	 * 
	 * @param sheetName
	 *            excel sheetName
	 * @param file
	 *            file
	 * @return list
	 */
	public List<T> importExcel(String sheetName, File file) {
		List<T> list = null;
		if (StringUtil.isEmpty(sheetName)) {
			list = this.readExcel(file);
		} else {
			list = this.readExcel(sheetName, file);
		}
		return list;
	}

	/**
	 * you must put list of data,name of excel sheet and file
	 * 
	 * @param list
	 * @param sheetName
	 * @param file
	 * @return boolean
	 */
	public boolean exportExcel(List<T> list, String sheetName, File file) {
		boolean flag = false;
		HSSFWorkbook workbook = null;
		if (null == list) {
			throw new NullPointerException(NULL_MSG);
		} else {
			workbook = new HSSFWorkbook();
			// set style of title
			HSSFCellStyle headerStyle = this.createTitleStyle(workbook);
			// set font style of title
			HSSFFont headerFont = this.setStyleOfTitleFont(workbook);
			headerStyle.setFont(headerFont);
			// get the fields that marked by ExcelAnnotation
			List<Field> fields = this.getFields(clazz);
			if (fields.size() <= 0) {
				throw new RuntimeException(clazz + EXCEPTION_MSG);
			}
			// create body style
			HSSFCellStyle bodyStyle = this.createBodyStyle(workbook);
			// set default size of a sheet
			int sheetSize = SHEET_SIZE;
			// Take out a total of how much a sheet.
			int sheetNo = this.totalSheet(list.size(), sheetSize);
			for (int index = 0; index <= sheetNo; index++) {
				// generate Excel sheet
				HSSFSheet sheet = workbook.createSheet();
				// set Excel sheet name
				workbook.setSheetName(index, sheetName + (index + 1));
				HSSFRow row;
				// create area of title
				row = sheet.createRow(0);
				// set content of title
				this.setTitle(fields, headerStyle, sheet, row);

				int startNo = index * sheetSize;
				int endNo = Math.min(startNo + sheetSize, list.size());
				// Write each record, each record corresponds to a row in the
				// excel
				// table
				for (int i = startNo; i < endNo; i++) {
					row = sheet.createRow(i + 1 - startNo);
					// obtain export object
					T t = (T) list.get(i);
					// set body
					this.setBody(fields, t, bodyStyle, row);
				}
			}
		}
		try {
			InputStream is = this.getInputStream(workbook, file);
			if (null != is) {
				flag = true;
			}
		} catch (NullPointerException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public boolean exportExcel(Map<String, List<T>> map, File file) {
		boolean flag = false;
		HSSFWorkbook workbook = null;
		if (null == map) {
			throw new NullPointerException(NULL_MSG);
		} else {
			workbook = new HSSFWorkbook();
			// set style of title
			HSSFCellStyle headerStyle = this.createTitleStyle(workbook);
			// set font style of title
			HSSFFont headerFont = this.setStyleOfTitleFont(workbook);
			headerStyle.setFont(headerFont);
			// get the fields that marked by ExcelAnnotation
			List<Field> fields = this.getFields(clazz);
			if (fields.size() <= 0) {
				throw new RuntimeException(clazz + EXCEPTION_MSG);
			}
			// create body style
			HSSFCellStyle bodyStyle = this.createBodyStyle(workbook);
			for (Map.Entry<String, List<T>> entry : map.entrySet()) {
				// generate Excel sheet
				HSSFSheet sheet = workbook.createSheet(entry.getKey());
				// set Excel sheet name
				// workbook.setSheetName(index,entry.getKey());
				HSSFRow row;
				// create area of title
				row = sheet.createRow(0);
				// set content of title
				this.setTitle(fields, headerStyle, sheet, row);

				// Write each record, each record corresponds to a row in the
				// excel
				// table
				List<T> list = entry.getValue();
				for (int i = 0; i < list.size(); i++) {
					row = sheet.createRow(i + 1);
					row.setHeightInPoints(20);
					// obtain export object
					T t = (T) list.get(i);
					// set body
					this.setBody(fields, t, bodyStyle, row);
				}
				//row = sheet.createRow(list.size()+1);//创建合计行
				//this.annotate(bodyStyle, sheet, row, fields.size() - 1,"各项数据有效统计");
				//row = sheet.createRow(list.size()+2);
				//this.setTotal(fields, bodyStyle, row,1, list.size()+1);
			}
		}
		try {
			InputStream is = this.getInputStream(workbook, file);
			if (null != is) {
				flag = true;
			}
		} catch (NullPointerException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * read record from a specific sheet
	 * 
	 * @param sheetName
	 * @param file
	 * @return
	 */
	private List<T> readExcel(String sheetName, File file) {
		HSSFWorkbook workbook = this.createWorkbook(file);
		List<T> list = null;
		Map<Integer, Field> fieldsMap = this.getFieldsMap(clazz);
		if (fieldsMap.size() <= 0) {
			throw new RuntimeException(clazz + EXCEPTION_MSG);
		}
		int totalRow = 0;
		HSSFSheet sheet = workbook.getSheet(sheetName);
		if (null != sheet) {
			totalRow = sheet.getLastRowNum();
		} else {
			throw new NullPointerException(sheetName + SHEET_NAME_ERROR);
		}
		// create list
		list = new ArrayList<T>(totalRow);

		for (int rowNum = 1, len = sheet.getLastRowNum(); rowNum <= len; rowNum++) {
			HSSFRow row = sheet.getRow(rowNum);
			if (null == row) {
				continue;
			}
			T entity = null;
			boolean flag = false;
			try {
				entity = clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			for (Field field : fieldsMap.values()) {
				ExcelAnnotation attr = field
						.getAnnotation(ExcelAnnotation.class);
				HSSFCell cell = row.getCell(attr.columnNum());
				row.getCell(attr.columnNum()).setCellType(
						HSSFCell.CELL_TYPE_STRING);
				String value = cell.getStringCellValue();
				Class<?> fieldType = field.getType();
				// populate value into object
				flag = this.setValue(entity, value, field, fieldType);
			}
			if (flag && entity != null) {
				list.add(entity);
			}
		}
		return list;
	}

	/**
	 * read all record from excel
	 * 
	 * @param file
	 * @return
	 */
	private List<T> readExcel(File file) {
		HSSFWorkbook workbook = this.createWorkbook(file);
		List<T> list = null;
		Map<Integer, Field> fieldsMap = this.getFieldsMap(clazz);
		if (fieldsMap.size() <= 0) {
			throw new RuntimeException(clazz + EXCEPTION_MSG);
		}
		int totalRow = 0;
		HSSFSheet sheet = null;

		// calculate the total record
		for (int numSheet = 0, len = workbook.getNumberOfSheets(); numSheet < len; numSheet++) {
			sheet = workbook.getSheetAt(numSheet);
			totalRow += sheet.getLastRowNum();
		}

		// create list
		list = new ArrayList<T>(totalRow);
		for (int numSheet = 0, len = workbook.getNumberOfSheets(); numSheet < len; numSheet++) {
			sheet = workbook.getSheetAt(numSheet);
			if (null == sheet) {
				continue;
			}
			for (int rowNum = 1, size = sheet.getLastRowNum(); rowNum <= size; rowNum++) {
				HSSFRow row = sheet.getRow(rowNum);

				if (null == row) {
					continue;
				}
				T entity = null;
				boolean flag = false;
				try {
					entity = clazz.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				for (Field field : fieldsMap.values()) {
					ExcelAnnotation attr = field
							.getAnnotation(ExcelAnnotation.class);
					HSSFCell cell = row.getCell(attr.columnNum());
					row.getCell(attr.columnNum()).setCellType(
							HSSFCell.CELL_TYPE_STRING);
					String value = cell.getStringCellValue();
					Class<?> fieldType = field.getType();
					// populate value into object
					flag = this.setValue(entity, value, field, fieldType);
				}
				if (flag && entity != null) {
					list.add(entity);
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * @param workbook
	 * @return
	 */
	private HSSFCellStyle createTitleStyle(HSSFWorkbook workbook) {
		// create cell style
		HSSFCellStyle style = workbook.createCellStyle();
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT));
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setWrapText(true);

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

	private HSSFCellStyle createHeaderStyle(HSSFWorkbook workbook) {
		// create cell style
		HSSFCellStyle style = workbook.createCellStyle();
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT));
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		// set cell border style
		// style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		return style;
	}

	private HSSFCellStyle createNoteStyle(HSSFWorkbook workbook) {
		// create cell style
		HSSFCellStyle style = workbook.createCellStyle();
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT));
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// set cell border style
		// style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		return style;
	}

	/**
	 * set style of excel titles font
	 * 
	 * @param workbook
	 * @return
	 */
	private HSSFFont setStyleOfTitleFont(HSSFWorkbook workbook) {
		HSSFFont font = workbook.createFont();
		font.setFontName(FONT_NAME);
		// font.setBoldweight((short) 100);
		// font.setFontHeight((short) 250);
		font.setFontHeightInPoints((short) 10);
		font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		return font;
	}

	/**
	 * create font style of Header font
	 * 
	 * @param workbook
	 * @return
	 */
	private HSSFFont createStyleOfHeaderFont(HSSFWorkbook workbook) {
		HSSFFont font = workbook.createFont();
		font.setFontName(FONT_NAME);
		font.setBoldweight((short) 100);
		font.setFontHeight((short) 250);
		// font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		return font;
	}

	/**
	 * 
	 * @param workbook
	 * @return
	 */
	private HSSFCellStyle createBodyStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setWrapText(true);

		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		return style;
	}

	/**
	 * get all field of object that marked by ExcelAnnotation annotation
	 * 
	 * @param clazz
	 * @return
	 */
	private List<Field> getFields(Class<T> clazz) {
		// obtain all Fields
		Field[] allFields = clazz.getDeclaredFields();
		List<Field> fields = new ArrayList<Field>();
		for (Field field : allFields) {
			// A field which marked with ExcelAnnotation
			if (field.isAnnotationPresent(ExcelAnnotation.class)) {
				fields.add(field);
			}
		}
		return fields;
	}

	/**
	 * get all field of object that marked by ExcelAnnotation annotation
	 * 
	 * @param clazz
	 * @return
	 */
	private Map<Integer, Field> getFieldsMap(Class<T> clazz) {
		Field[] allFields = clazz.getDeclaredFields();
		Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
		for (Field field : allFields) {
			if (field.isAnnotationPresent(ExcelAnnotation.class)) {
				ExcelAnnotation attr = field
						.getAnnotation(ExcelAnnotation.class);
				field.setAccessible(true);
				fieldsMap.put(attr.columnNum(), field);
			}
		}
		return fieldsMap;
	}

	/**
	 * set content of header
	 * 
	 * @param fields
	 * @param headerStyle
	 * @param sheet
	 * @param row
	 */
	private void setTitle(List<Field> fields, HSSFCellStyle headerStyle,
			HSSFSheet sheet, HSSFRow row) {
		for (int i = 0, len = fields.size(); i < len; i++) {
			Field field = fields.get(i);
			ExcelAnnotation attr = field.getAnnotation(ExcelAnnotation.class);

			HSSFCell cell = row.createCell(attr.columnNum());
			// set type of content to cells
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			// set column width
			sheet.setColumnWidth(attr.columnNum(), attr.width());
			// populate name into cell in first row
			cell.setCellValue(attr.name());

			row.setHeightInPoints(25);
			// set header style
			cell.setCellStyle(headerStyle);
		}
	}

	private void setHeader(HSSFCellStyle headerStyle, HSSFSheet sheet,
			HSSFRow row, int endColum, String content) {
		HSSFCell cell = row.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, endColum));
		// set type of content to cells
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);

		// populate name into cell in first row
		cell.setCellValue(content);
		row.setHeightInPoints(20);
		// set header style
		headerStyle.setWrapText(true);
		cell.setCellStyle(headerStyle);
	}

	private void annotate(HSSFCellStyle headerStyle, HSSFSheet sheet,
			HSSFRow row, int endColum, String content) {
		HSSFCell cell = row.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, endColum));
		// set type of content to cells
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		headerStyle.setWrapText(true);
		// set header style
		cell.setCellStyle(headerStyle);
		row.setHeight((short) 500);
		// populate name into cell in first row
		cell.setCellValue(new HSSFRichTextString(content));

	}

	/**
	 * set content of body
	 * 
	 * @param fields
	 * @param t
	 * @param bodyStyle
	 * @param row
	 */
	private void setBody(List<Field> fields, T t, HSSFCellStyle bodyStyle,
			HSSFRow row) {
		for (Field field : fields) {
			// Set the entity class private property can be accessed
			field.setAccessible(true);
			ExcelAnnotation attr = field.getAnnotation(ExcelAnnotation.class);
			try {
				if (attr.isExport()) {
					// create Excel cell
					HSSFCell cell = row.createCell(attr.columnNum());
					cell.setCellStyle(bodyStyle);
					setCellTypeAndValue(cell, field, t);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private void setTotal(List<Field> fields, HSSFCellStyle bodyStyle,
			HSSFRow row,int start,int end) {
		for (Field field : fields) {
			// Set the entity class private property can be accessed
			field.setAccessible(true);
			ExcelAnnotation attr = field.getAnnotation(ExcelAnnotation.class);
			try {
				if (attr.isExport()) {
					// create Excel cell
					int columnNumber = attr.columnNum();
					HSSFCell cell = row.createCell(attr.columnNum());
					cell.setCellStyle(bodyStyle);
					if(attr.isSum()){
						String columnName = this.getColumnNum(columnNumber+1);
						StringBuilder builder = new StringBuilder();
						builder.append("SUM(").append(columnName);
						builder.append(start).append(":").append(columnName);
						builder.append(end).append(")");
						cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
						cell.setCellFormula(builder.toString());
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} 
		}
	}

	/**
	 * 根据数据类型设置列的类型
	 * 
	 * @param cell
	 * @param field
	 */
	public void setCellTypeAndValue(HSSFCell cell, Field field, T t)
			throws IllegalArgumentException, IllegalAccessException {
		if (field.getType().toString().equals("byte")
				|| field.getType() == Byte.class) {
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(field.get(t) == null ? "" : String.valueOf(field
					.get(t)));
		} else if (field.getType().toString().equals("boolean")
				|| field.getType() == Boolean.class) {
			cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
			cell.setCellValue(field.get(t) == null ? "" : String.valueOf(field
					.get(t)));
		} else if (field.getType().toString().equals("double")
				|| field.getType() == Double.class) {
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(field.get(t) == null ? null : Double
					.valueOf(String.valueOf(field.get(t))));
		} else if (field.getType().toString().equals("float")
				|| field.getType() == Float.class) {
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue((Float) (field.get(t) == null ? ""
					: (Float) (field.get(t))));
		} else if (field.getType().toString().equals("int")
				|| field.getType() == Integer.class) {
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(field.get(t) == null ? null : (Integer) (field
					.get(t)));
		} else if (field.getType().toString().equals("long")
				|| field.getType() == Long.class) {
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue((Long) (field.get(t) == null ? "" : (Long) (field
					.get(t))));
		} else if (field.getType().toString().equals("short")
				|| field.getType() == Short.class) {
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(field.get(t) == null ? "" : String.valueOf(field
					.get(t)));
		} else if (field.getType() == Timestamp.class) {
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(field.get(t) == null ? "" : String.valueOf(field
					.get(t)));
		} else if (field.getType() == Date.class) {
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(field.get(t) == null ? "" : String.valueOf(field
					.get(t)));
		} else if (field.getType() == Time.class) {
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(field.get(t) == null ? "" : String.valueOf(field
					.get(t)));
		} else {
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(field.get(t) == null ? "" : String.valueOf(field
					.get(t)));
		}

	}

	/**
	 * calculate total of sheet
	 * 
	 * @param totalRecord
	 * @param size
	 * @return
	 */
	private int totalSheet(int totalRecord, int size) {
		return totalRecord % size == 0 ? totalRecord / size : totalRecord
				/ size + 1;
	}

	/**
	 * set value into object if success then return true
	 * 
	 * @param entity
	 * @param value
	 * @param field
	 * @param fieldType
	 * @return
	 */
	private boolean setValue(T entity, String value, Field field,
			Class<?> fieldType) {
		boolean flag = false;
		try {
			if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
				field.set(entity, Integer.parseInt(value));
			} else if (String.class == fieldType) {
				field.set(entity, String.valueOf(value));
			} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
				field.set(entity, Long.valueOf(value));
			} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
				field.set(entity, Float.valueOf(value));
			} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
				field.set(entity, Short.valueOf(value));
			} else if ((Double.TYPE == fieldType)
					|| (Double.class == fieldType)) {
				field.set(entity, Double.valueOf(value));
			} else if (Character.TYPE == fieldType) {
				if ((value != null) && (value.length() > 0)) {
					field.set(entity, Character.valueOf(value.charAt(0)));
				}
			}
			flag = true;
		} catch (IllegalAccessException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * create work book
	 * 
	 * @param file
	 * @return
	 */
	private HSSFWorkbook createWorkbook(File file) {
		FileInputStream is = null;
		HSSFWorkbook workbook = null;
		try {
			is = new FileInputStream(file);
			workbook = new HSSFWorkbook(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return workbook;
	}

	/**
	 * 
	 * @param workbook
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws NullPointerException
	 */
	private InputStream getInputStream(HSSFWorkbook workbook, File file) {
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
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new NullPointerException(NULL_WORKBOOK);
		}
		return is;
	}

	/**
	 * 
	 * 
	 * @param workbook
	 * @return
	 */
	private InputStream getInputStream(HSSFWorkbook workbook) {
		ByteArrayOutputStream baos = null;
		try {
			if (workbook == null) {
				return null;
			} else {
				baos = new ByteArrayOutputStream();
				workbook.write(baos);
				baos.flush();
				byte[] bt = baos.toByteArray();
				InputStream excelStream = new ByteArrayInputStream(bt, 0,
						bt.length);
				baos.close();
				return excelStream;
			}

		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * if client specific OutputStream then use this method
	 * 
	 * @param workbook
	 * @param os
	 * @return
	 */
	private boolean getInputStream(HSSFWorkbook workbook, OutputStream os) {
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
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			flag = false;
			throw new NullPointerException(NULL_WORKBOOK);
		}
		return flag;
	}
	 private String getColumnNum(int i) {  
		    String strResult = "";  
		    int intRound = i / 26;  
		    int intMod = i % 26;  
		    if (intRound != 0) {  
		        strResult = String.valueOf(((char) (intRound + 64)));  
		    }  
		    strResult += String.valueOf(((char) (intMod + 64)));  
		    return strResult; 
	 }
}
