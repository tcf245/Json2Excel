package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by BFD_303 on 2016/7/4.
 */
public class TaskStart {
    public static BlockingQueue<QueueBean> taskQueue = new LinkedBlockingQueue(10);

    public static void main(String[] args) throws IOException {
        Properties pro = new Properties();
        InputStream is = FileUtils.openInputStream(new File("etc/config.properties"));
        pro.load(is);
        String ipStr = (String) pro.get("ip");
        String ips[] = ipStr.split(",");
        int threadNum = Integer.parseInt(pro.get("threadNum").toString());
        int rowNum = Integer.parseInt(pro.get("rowNum").toString());

//        List<QueueBean> queueBeanList = TaskUtils.getTaskByBrand((String)pro.get("jsonDir"));
//        List<QueueBean> queueBeanList = TaskUtils.getTaskByBrand("C:\\Users\\tcf24\\Desktop\\data");
//        System.out.println("get queue list size is :" + queueBeanList.size());

        Thread getTask = new Thread(new GetTask("C:\\Users\\tcf24\\OneDrive\\文档\\工作文档\\天猫\\data"));
        getTask.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(taskQueue.size() > 0){
            QueueBean qb = null;
            try {
                qb = taskQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread t = new Thread(new ThreadTask(qb,ips));
            t.start();
        }

//        for(QueueBean qb : queueBeanList){
//            Thread t = new Thread(new ThreadTask(qb,ips));
//            t.start();
//        }
    }

}
