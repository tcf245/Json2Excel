package com.bfd.crawl.module;

import com.bfd.crawl.HssfHelp;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.concurrent.BlockingQueue;

/**
 * Created by tcf24 on 2016/7/3.
 */
public class QueueBean {

    private String name;
    private BlockingQueue queue;
    // 创建Excel文档
    private  HSSFWorkbook hwb = new HSSFWorkbook();
    // sheet 对应一个工作页
    private  HSSFSheet sheet = hwb.createSheet("商品基本信息");
    //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
    private  HSSFPatriarch patriarch = HssfHelp.creatHSSFPatriarch(sheet);

    public QueueBean() {
    }

    public QueueBean(String name, BlockingQueue queue) {
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

    public HSSFWorkbook getHwb() {
        return hwb;
    }

    public void setHwb(HSSFWorkbook hwb) {
        this.hwb = hwb;
    }

    public HSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(HSSFSheet sheet) {
        this.sheet = sheet;
    }

    public HSSFPatriarch getPatriarch() {
        return patriarch;
    }

    public void setPatriarch(HSSFPatriarch patriarch) {
        this.patriarch = patriarch;
    }
}
