package com.boco.generator.excel;

/**
 * 导出excel指定title
 */
public class ExcelTitle {
    private int columnNum;
    private String name;
    private int width = 3000;
    private String fieldName;
    private boolean isSum;
    private String[] replace = {};
    private String formatTime;

    public int getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isSum() {
        return isSum;
    }

    public void setSum(boolean sum) {
        isSum = sum;
    }

    public String[] getReplace() {
        return replace;
    }

    public void setReplace(String[] replace) {
        this.replace = replace;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }
}
