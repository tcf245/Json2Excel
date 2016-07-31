package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * Created by BFD_303 on 2016/7/4.
 */
public class TaskStart {

    public static void main(String[] args) throws IOException {
        Properties pro = new Properties();
        InputStreamReader is =new InputStreamReader(new FileInputStream(new File("etc/config.properties")),"utf-8");
        pro.load(is);
        String ipStr = (String) pro.get("ip");
        String ips[] = ipStr.split(",");

        List<QueueBean> queueBeanList = TaskUtils.getTaskByBrand((String)pro.get("jsonDir"));
        System.out.println("get queue list size is :" + queueBeanList.size());

        for(QueueBean qb : queueBeanList){
            Thread t = new Thread(new ThreadTask(qb,ips));
            t.start();
        }
    }

}
