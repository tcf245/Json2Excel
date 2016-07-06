package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import com.bfd.crawler.utils.JsonUtils;

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

            while(queue.size() > 0){
                String jsonString = (String) queue.take();
                System.out.println("queue size is :" + queue.size());
                Map<String,Object> data = JsonUtils.parseObject(jsonString);
                String imgPath = (String) data.get("large_img");

                String ip = ipList[(int) Math.floor(Math.random() * ipList.length)];
                System.out.println("get ip is :" + ip);

                byte[] byteArray = JavaHttpDemo.httpGet(imgPath,"utf-8",headers,ip);

                ExcelInsert.insertRow2Excel(data, queueBean ,byteArray);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
