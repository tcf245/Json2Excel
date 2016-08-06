package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

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
    private static final Log LOG = LogFactory.getLog("TaskStart.class");
    public static int threadNum;
    public static String target;

    public static void main(String[] args) throws IOException {

        PropertyConfigurator.configure(TaskStart.class.getClassLoader().getResource("log4j.properties").getFile());
        LOG.info("loading log properties...");

        Properties pro = new Properties();
        InputStreamReader is =new InputStreamReader(new FileInputStream(TaskStart.class.getClassLoader().getResource("config.properties").getFile()),"utf-8");
        pro.load(is);
        String ipStr = (String) pro.get("ip");
        target = (String) pro.get("target");
        String ips[] = ipStr.split(",");

        List<QueueBean> queueBeanList = TaskUtils.getTaskByBrand((String)pro.get("jsonDir"));
        System.out.println("get queue list size is :" + queueBeanList.size());

        for(QueueBean qb : queueBeanList){
            threadNum++;
            LOG.info("add thread num is : " + threadNum);
            Thread t = new Thread(new ThreadTask(qb,ips));
            t.start();
        }
    }

}
