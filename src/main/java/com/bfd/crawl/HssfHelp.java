package com.bfd.crawl;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by tcf24 on 2016/7/3.
 */
public class HssfHelp {

//        // 创建Excel文档
//        public static HSSFWorkbook hwb = new HSSFWorkbook();
//        // sheet 对应一个工作页
//        public static HSSFSheet sheet = hwb.createSheet("商品基本信息");
//        //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
//        public static HSSFPatriarch patriarch =HssfHelp.creatHSSFPatriarch(sheet);

    /**
     * 设置单元格样式
     * @param book
     * @return
     */
    public  static  HSSFCellStyle    creatStyle(HSSFWorkbook book){
        HSSFCellStyle  setBorder = book.createCellStyle();
        setBorder.setBorderBottom(HSSFCellStyle.BORDER_NONE); //下边框
        setBorder.setBorderLeft(HSSFCellStyle.BORDER_NONE);//左边框
        setBorder.setBorderTop(HSSFCellStyle.BORDER_NONE);//上边框
        setBorder.setBorderRight(HSSFCellStyle.BORDER_NONE);//右边框
        setBorder.setWrapText(true);//设置自动换行
        return setBorder;
    }

public static  HSSFPatriarch   creatHSSFPatriarch (HSSFSheet sheet){
    return sheet.createDrawingPatriarch();

}
}
