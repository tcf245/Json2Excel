package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;

import java.io.*;
import java.util.Map;

/**
 * Created by tcf24 on 2016/7/3.
 */
public class ExcelInsert {

    /**
     * @param queueBean  任务所需的queueBean
     * @param xlsRow   存储任务信息的map
     * @throws Exception
     *             在导入Excel的过程中抛出异常
     */
    public static void insertRow2Excel(Map<String,Object> xlsRow, QueueBean queueBean,byte[] byteArray) throws Exception {

        Workbook hwb = queueBean.getHwb();
        SXSSFSheet sheet = queueBean.getSheet();
        Drawing patriarch= queueBean.getPatriarch();
        String targetFile = "etc/" + queueBean.getName() + ".xlsx";

        try {

            String[] titles = {"名称","url","图片","图片url","市场价","促销价","促销信息","销售渠道","货号","材料","积分","月销量","评价数量","品牌"};
            if(sheet.getLastRowNum() == 0){
                SXSSFRow firstRow = sheet.createRow(0);
                for (int i = 0; i < titles.length; i++) {
                    SXSSFCell cell = firstRow.createCell(i);
                    cell.setCellValue(titles[i]);
                }
            }
            // 创建一行
            SXSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.setHeight((short) 1000);
//                row.setRowStyle(HssfHelp.creatStyle(hwb));
            // 得到要插入的每一条记录
            SXSSFCell name = row.createCell(0);
            name.setCellValue((String) xlsRow.get("itemname"));
            System.out.println((String) xlsRow.get("itemname"));
            SXSSFCell url = row.createCell(1);
            url.setCellValue((String) xlsRow.get("url"));

            SXSSFCell img = row.createCell(2);
            String imgPath = (String) xlsRow.get("large_img");
            sheet.setColumnWidth(2,10 * 256);

            //anchor主要用于设置图片的属性
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 100, 40, (short) 2, row.getRowNum(), (short) 3, row.getRowNum() + 1);
            anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
            //插入图片
            patriarch.createPicture(anchor, hwb.addPicture(byteArray, HSSFWorkbook.PICTURE_TYPE_JPEG));

            SXSSFCell imgUrl = row.createCell(3);
            imgUrl.setCellValue((String) xlsRow.get("large_img"));
            SXSSFCell price = row.createCell(4);
            price.setCellValue((String) xlsRow.get("normalprice"));
            SXSSFCell promotionprice = row.createCell(5);
            promotionprice.setCellValue((String) xlsRow.get("promotionprice"));
            SXSSFCell promotionMess = row.createCell(6);
            promotionMess.setCellValue((String) xlsRow.get("promotionMess"));
            SXSSFCell qudao = row.createCell(7);
            qudao.setCellValue((String) xlsRow.get("tianmaoxiaoshouqudao"));
            SXSSFCell huohao = row.createCell(8);
            huohao.setCellValue((String) xlsRow.get("tianmaohuohao"));
            SXSSFCell cailiao = row.createCell(9);
            cailiao.setCellValue((String) xlsRow.get("tianmaoxiaocailiao"));
            SXSSFCell jifen = row.createCell(10);
            jifen.setCellValue((String) xlsRow.get("tianmaojifen"));
            SXSSFCell xiaoliang = row.createCell(11);
            Object cellCount = xlsRow.get("sellCount");
            if(cellCount != null){
                xiaoliang.setCellValue((int)cellCount);
            }
            SXSSFCell pingjia = row.createCell(12);
            Object rateTotal = xlsRow.get("rateTotal");
            if(rateTotal != null){
                pingjia.setCellValue((int)rateTotal);
            }
            SXSSFCell pinpai = row.createCell(13);
            pinpai.setCellValue((String) xlsRow.get("tianmaopinpai"));

            // 创建文件输出流，准备输出电子表格
            OutputStream out = new FileOutputStream(targetFile);
            hwb.write(out);
            out.close();
            System.out.println("数据导出成功" + targetFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
