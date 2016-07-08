package com.bfd.crawl.module;

import com.bfd.crawl.HssfHelp;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

/**
 * Created by tcf24 on 2016/7/3.
 */
public class QueueBean {

    private int lastRowNum = 0;

    private String name;
    private BlockingQueue queue;
    // 创建Excel文档
    private SXSSFWorkbook hwb = new SXSSFWorkbook(1000);
    // sheet 对应一个工作页
    private SXSSFSheet sheet = hwb.createSheet("商品基本信息");
    //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
    private Drawing patriarch = HssfHelp.creatHSSFPatriarch(sheet);

    public QueueBean() throws FileNotFoundException {
    }

    public QueueBean(String name, BlockingQueue queue) throws FileNotFoundException {
        this.name = name;
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BlockingQueue getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue queue) {
        this.queue = queue;
    }

    public SXSSFWorkbook getHwb() {
        return hwb;
    }

    public void setHwb(SXSSFWorkbook hwb) {
        this.hwb = hwb;
    }

    public SXSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(SXSSFSheet sheet) {
        this.sheet = sheet;
    }

    public Drawing getPatriarch() {
        return patriarch;
    }

    public void setPatriarch(Drawing patriarch) {
        this.patriarch = patriarch;
    }
    public int getLastRowNum() {
        return lastRowNum;
    }

    public void setLastRowNum(int lastRowNum) {
        this.lastRowNum = lastRowNum;
    }
}
