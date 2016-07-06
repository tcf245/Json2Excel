package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor;
import java.io.*;
import java.util.HashMap;
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
    public synchronized static void insertRow2Excel(Map<String,Object> xlsRow, QueueBean queueBean,byte[] byteArray) throws Exception {

        HSSFWorkbook hwb = queueBean.getHwb();
        HSSFSheet sheet = queueBean.getSheet();
        HSSFPatriarch patriarch= queueBean.getPatriarch();
        String targetFile = "etc/" + queueBean.getName() + ".xls";

        try {
            // 创建一行
            HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.setHeight((short) 1000);
//                row.setRowStyle(HssfHelp.creatStyle(hwb));
            // 得到要插入的每一条记录
            HSSFCell name = row.createCell(0);
            name.setCellValue((String) xlsRow.get("itemname"));
            System.out.println((String) xlsRow.get("itemname"));
            HSSFCell url = row.createCell(1);
            url.setCellValue((String) xlsRow.get("url"));

            HSSFCell img = row.createCell(2);
            String imgPath = (String) xlsRow.get("large_img");
            sheet.setColumnWidth(2,10 * 256);

            //anchor主要用于设置图片的属性
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 100, 40, (short) 2, row.getRowNum(), (short) 3, row.getRowNum() + 1);
            anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
            //插入图片
            patriarch.createPicture(anchor, hwb.addPicture(byteArray, HSSFWorkbook.PICTURE_TYPE_JPEG));

            HSSFCell imgUrl = row.createCell(3);
            imgUrl.setCellValue((String) xlsRow.get("large_img"));
            HSSFCell price = row.createCell(4);
            price.setCellValue((String) xlsRow.get("normalprice"));
            HSSFCell promotionprice = row.createCell(5);
            promotionprice.setCellValue((String) xlsRow.get("promotionprice"));
            HSSFCell promotionMess = row.createCell(6);
            promotionMess.setCellValue((String) xlsRow.get("promotionMess"));
            HSSFCell qudao = row.createCell(7);
            qudao.setCellValue((String) xlsRow.get("tianmaoxiaoshouqudao"));
            HSSFCell huohao = row.createCell(8);
            huohao.setCellValue((String) xlsRow.get("tianmaohuohao"));
            HSSFCell cailiao = row.createCell(9);
            cailiao.setCellValue((String) xlsRow.get("tianmaoxiaocailiao"));
            HSSFCell jifen = row.createCell(10);
            jifen.setCellValue((String) xlsRow.get("tianmaojifen"));
            HSSFCell xiaoliang = row.createCell(11);
            Object cellCount = xlsRow.get("sellCount");
            if(cellCount != null){
                xiaoliang.setCellValue((int)cellCount);
            }
            HSSFCell pingjia = row.createCell(12);
            Object rateTotal = xlsRow.get("rateTotal");
            if(rateTotal != null){
                pingjia.setCellValue((int)rateTotal);
            }
            HSSFCell pinpai = row.createCell(13);
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
