package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor;
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
    public synchronized static void insertRow2Excel(Map<String,Object> xlsRow, QueueBean queueBean) throws Exception {

        HSSFWorkbook hwb = queueBean.getHwb();
        HSSFSheet sheet = queueBean.getSheet();
        HSSFPatriarch patriarch= queueBean.getPatriarch();
        String targetFile = "etc/" + queueBean.getName() + ".xls";

            // 创建一行
            HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
                row.setHeight((short)1000);
                row.setRowStyle(HssfHelp.creatStyle(hwb));
            // 得到要插入的每一条记录
                HSSFCell name = row.createCell(0);

                name.setCellValue((String) xlsRow.get("itemname"));
                System.out.println((String) xlsRow.get("itemname"));
                HSSFCell url = row.createCell(1);
                url.setCellValue((String) xlsRow.get("url"));

                HSSFCell img = row.createCell(2);
                String imgPath = (String) xlsRow.get("small_img");

                //----------------------------------
                byte[] byteArray = ImgUtils.mReaderPictureToInternet(imgPath);

                //anchor主要用于设置图片的属性
                HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,60,60,(short)2,row.getRowNum(),(short)3,row.getRowNum() + 1);
                anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
                //插入图片
                patriarch.createPicture(anchor, hwb.addPicture(byteArray, HSSFWorkbook.PICTURE_TYPE_JPEG));

                HSSFCell imgUrl = row.createCell(3);
                imgUrl.setCellValue((String) xlsRow.get("small_img"));
                HSSFCell price = row.createCell(4);
                price.setCellValue((String) xlsRow.get("normalprice"));
                HSSFCell promotionMess = row.createCell(5);
                promotionMess.setCellValue((String) xlsRow.get("promotionMess"));
                HSSFCell qudao = row.createCell(6);
                qudao.setCellValue((String) xlsRow.get("tianmaoxiaoshouqudao"));

        // 创建文件输出流，准备输出电子表格
        OutputStream out = new FileOutputStream(targetFile);
        hwb.write(out);
        out.close();
        System.out.println("数据导出成功");
    }
}
