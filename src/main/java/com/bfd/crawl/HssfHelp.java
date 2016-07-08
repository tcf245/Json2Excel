package com.bfd.crawl;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Created by tcf24 on 2016/7/3.
 */
public class HssfHelp {

    /**
     * 设置单元格样式
     *
     * @param book
     * @return
     */
    public static CellStyle creatStyle(Workbook book) {
        CellStyle setBorder = book.createCellStyle();
        setBorder.setBorderBottom(HSSFCellStyle.BORDER_NONE); //下边框
        setBorder.setBorderLeft(HSSFCellStyle.BORDER_NONE);//左边框
        setBorder.setBorderTop(HSSFCellStyle.BORDER_NONE);//上边框
        setBorder.setBorderRight(HSSFCellStyle.BORDER_NONE);//右边框
        setBorder.setWrapText(true);//设置自动换行
        return setBorder;
    }

    public static Drawing creatHSSFPatriarch(SXSSFSheet sheet) {
        return sheet.createDrawingPatriarch();
    }
}
