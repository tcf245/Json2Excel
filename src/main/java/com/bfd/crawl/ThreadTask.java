package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import com.bfd.crawler.utils.JsonUtils;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by BFD_303 on 2016/7/4.
 */
public class ThreadTask implements Runnable {

    private QueueBean queueBean;
    private String[] ipList;

    public ThreadTask(QueueBean queueBean,String[] ips){
        this.queueBean = queueBean;
        this.ipList = ips;
    }

    @Override
    public void run() {
        try {
            BlockingQueue queue = queueBean.getQueue();

            Map<String,String> headers = new HashMap<String, String>();
            headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

//            while(queue.size() > 0){
//                String jsonString = (String) queue.take();
//                System.out.println("queue size is :" + queue.size());
//                Map<String,Object> data = JsonUtils.parseObject(jsonString);
//                String imgPath = (String) data.get("large_img");
//
//                String ip = ipList[(int) Math.floor(Math.random() * ipList.length)];
//                System.out.println(queue.size() + " get ip is :" + ip);
//
//                byte[] byteArray = JavaHttpDemo.httpGet(imgPath,"utf-8",headers,ip);
//                System.out.println(queue.size() + " img.. waiting for write ..");
//
//                ExcelInsert.insertRow2Excel(data, queueBean ,byteArray);
//            }


            SXSSFWorkbook hwb = new SXSSFWorkbook(1000);
            // sheet 对应一个工作页
            SXSSFSheet sheet = hwb.createSheet("商品基本信息");
            //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            Drawing patriarch = sheet.createDrawingPatriarch();

            int rowNum = queue.size();

            for (int i = 0; i < rowNum; i++) {
                System.out.println(queueBean.getName() + "queue size is :" + queue.size());
                String jsonString = (String) queue.take();
                insertRow2Excel(queueBean ,jsonString,ipList,hwb,sheet,patriarch);

            }
            // 创建文件输出流，准备输出电子表格
            OutputStream out = new FileOutputStream("etc/" + queueBean.getName().replace(".txt","") + ".xlsx",true);
            hwb.write(out);
            out.close();

            System.out.println(queueBean.getName() + " OK!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(queueBean.getName() + "Exception!");
        }
    }


    /**
     * @param queueBean  任务所需的queueBean
     * @param xlsRow   存储任务信息的map
     * @throws Exception
     *             在导入Excel的过程中抛出异常
     */
    public static synchronized void insertRow2Excel(QueueBean queueBean,String jsonString,String[] ipList,SXSSFWorkbook hwb,SXSSFSheet sheet,Drawing patriarch) throws Exception {

        Map<String,String> headers = new HashMap<String, String>();
        headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        Map<String,Object> xlsRow = JsonUtils.parseObject(jsonString);
        String imgPath = (String) xlsRow.get("large_img");

        String ip = ipList[(int) Math.floor(Math.random() * ipList.length)];

        byte[] byteArray = JavaHttpDemo.httpGet(imgPath,"utf-8",headers,ip);


        int lastRowNum = sheet.getLastRowNum();

        Integer rowNum = Constants.rowNum.get(queueBean.getName());
        if(rowNum != null){
            lastRowNum = Integer.valueOf(rowNum);
        }

        System.out.println("current row num is :" + lastRowNum);

        String targetFile = "etc/" + queueBean.getName().replace(".txt","") + ".xlsx";

        try {

            String[] titles = {"名称","url","图片","图片url","市场价","促销价","促销信息","销售渠道","货号","材料","积分","月销量","评价数量","品牌"};
            if(lastRowNum == 0){
                SXSSFRow firstRow = sheet.createRow(lastRowNum);
                lastRowNum ++;
                Constants.rowNum.put(queueBean.getName(), 1);
                for (int i = 0; i < titles.length; i++) {
                    SXSSFCell cell = firstRow.createCell(i);
                    cell.setCellValue(titles[i]);
                }
            }
            // 创建一行
            SXSSFRow row = sheet.createRow(lastRowNum);
            lastRowNum ++;
//            queueBean.setLastRowNum(lastRowNum ++);
            Constants.rowNum.put(queueBean.getName(), lastRowNum);
            row.setHeight((short) 1000);
            // 得到要插入的每一条记录
            SXSSFCell name = row.createCell(0);
            name.setCellValue((String) xlsRow.get("itemname"));
            System.out.println((String) xlsRow.get("itemname"));
            SXSSFCell url = row.createCell(1);
            url.setCellValue((String) xlsRow.get("url"));

            SXSSFCell img = row.createCell(2);
            sheet.setColumnWidth(2,10 * 256);

            //anchor主要用于设置图片的属性
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 100, 100, (short) 2, row.getRowNum(), (short) 3, row.getRowNum() +1);
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
            //插入图片
            patriarch.createPicture(anchor, hwb.addPicture(byteArray, SXSSFWorkbook.PICTURE_TYPE_JPEG));

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

            xiaoliang.setCellValue((String)cellCount);

            SXSSFCell pingjia = row.createCell(12);
            Object rateTotal = xlsRow.get("rateTotal");
            if(rateTotal != null){
                pingjia.setCellValue((String)rateTotal);
            }
            SXSSFCell pinpai = row.createCell(13);
            pinpai.setCellValue((String) xlsRow.get("tianmao"));

        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
