package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import com.bfd.crawler.utils.JsonUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
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

    private Workbook xwb ;
    private Sheet sheet;
    private int lastRowNum;
    //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
    private Drawing patriarch;

    @Override
    public void run() {
        try {
            BlockingQueue queue = queueBean.getQueue();

            initWorkbook();

            int rowNum = queue.size();
            for (int i = 0; i < rowNum; i++) {
                System.out.println(queueBean.getName() + "queue size is :" + queue.size());
                String jsonString = (String) queue.take();
                insertRow2Excel(queueBean,jsonString,ipList,xwb,sheet,patriarch);

//                if(i % 100 == 0 && i != 0){
//                    OutputStream out = new FileOutputStream("etc/" + queueBean.getName() + ".xlsx",true);
//                    xwb.write(out);
//                    out.close();
//                    System.out.println(queueBean.getName() + " has flush 100 rows to disk.");
//                    patriarch = null;
//                    initWorkbook();
//                }
            }
            // 创建文件输出流，准备输出电子表格
            OutputStream out = new FileOutputStream("etc/" + queueBean.getName() + ".xlsx",true);
            xwb.write(out);
            out.close();

            System.out.println(queueBean.getName() + " OK!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(queueBean.getName() + "Exception!");
        }
    }

    /**
     * 初始化Workbook
     */
    private void initWorkbook() {
        xwb = new SXSSFWorkbook(100);
        sheet = xwb.createSheet("商品基本信息");
        lastRowNum = sheet.getLastRowNum();
        System.out.println("get last row num is " + lastRowNum);
        patriarch = sheet.createDrawingPatriarch();
    }

    /**
     * 插入一条记录
     * @param queueBean 任务所需的queueBean
     * @param jsonString  任务信息
     * @param ipList   可选ip列表
     * @param xwb  SXSSFWorkbook
     * @param sheet
     * @param patriarch
     * @throws Exception 在导入Excel的过程中抛出异常
     */
    public void insertRow2Excel(QueueBean queueBean,String jsonString,String[] ipList,Workbook xwb,Sheet sheet,Drawing patriarch) throws Exception {

        Map<String,String> headers = new HashMap<String, String>();
        headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        Map<String,Object> xlsRow = JsonUtils.parseObject(jsonString);
        String imgPath = (String) xlsRow.get("large_img");
        //随机分配ip
        String ip = ipList[(int) Math.floor(Math.random() * ipList.length)];

        byte[] byteArray = new byte[0];
        try {
            byteArray = JavaHttpDemo.httpGet(imgPath, "utf-8", headers, ip);
        }catch(Exception e){
            FileUtils.write(new File("etc/failedTask.txt"),queueBean.getName() + " " +imgPath,"utf-8",true);
        }

        System.out.println("current row num is :" + lastRowNum);
        try {
            String[] titles = {"名称","url","图片","图片url","市场价","促销价","促销信息","销售渠道","货号","材料","积分","月销量","评价数量","品牌"};
            String[] items = {"itemname","url","large_img","large_img","normalprice","promotionprice","promotionMess","tianmaoxiaoshouqudao","tianmaohuohao","tianmaoxiaocailiao",
                    "tianmaojifen","sellCount","rateTotal","tianmao"};
            //首行插入表头
            if(lastRowNum == 0){
                Row firstRow = sheet.createRow(lastRowNum);
                lastRowNum ++;
                Constants.rowNum.put(queueBean.getName(), 1);
                for (int i = 0; i < titles.length; i++) {
                    Cell cell = firstRow.createCell(i);
                    cell.setCellValue(titles[i]);
                }
            }

            //添加一行内容
            Row row = sheet.createRow(lastRowNum);
            lastRowNum ++;
            Constants.rowNum.put(queueBean.getName(), lastRowNum);
            row.setHeight((short) 1000);

            //循环添加一行中每个单元格的内容
            for (int i = 0; i < items.length; i++) {
                Cell cell = row.createCell(i);
                Object obj = xlsRow.get(items[i]);
                if (obj != null) {
                    String data = (String) obj;
                    if (i == 2) {
                        sheet.setColumnWidth(2, 10 * 256);
                        //anchor主要用于设置图片的属性
                        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 100, 100, (short) 2, row.getRowNum(), (short) 3, row.getRowNum() + 1);
                        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                        //插入图片
                        patriarch.createPicture(anchor, xwb.addPicture(byteArray, XSSFWorkbook.PICTURE_TYPE_JPEG));
                        continue;
                    }
                    cell.setCellValue(data.replace("null","").trim());
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
