package com.bfd.crawl;

import com.bfd.crawl.ExcelInsert;
import com.bfd.crawl.module.QueueBean;
import com.bfd.crawler.utils.JsonUtils;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by BFD_303 on 2016/7/4.
 */
public class ThreadTask implements Runnable {

    private QueueBean queueBean;

    public ThreadTask(QueueBean queueBean){
        this.queueBean = queueBean;
    }

    @Override
    public void run() {
        try {
            BlockingQueue queue = queueBean.getQueue();

            while(queue.size() > 0){
                String jsonString = (String) queue.take();
                System.out.println("queue size is :" + queue.size());

                Map<String,Object> data = JsonUtils.parseObject(jsonString);
                ExcelInsert.insertRow2Excel(data, queueBean);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
