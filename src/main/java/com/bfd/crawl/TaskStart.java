package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by BFD_303 on 2016/7/4.
 */
public class TaskStart {

    public static void main(String[] args) throws IOException {
        Properties pro = new Properties();
        InputStream is = FileUtils.openInputStream(new File("etc/config.properties"));
        pro.load(is);
        String ipStr = (String) pro.get("ip");
        String ips[] = ipStr.split(",");
        int threadNum = Integer.parseInt(pro.get("threadNum").toString());
        int rowNum = Integer.parseInt(pro.get("rowNum").toString());

        List<QueueBean> queueBeanList = TaskUtils.getTaskByBrand((String)pro.get("jsonDir"));
        System.out.println("get queue list size is :" + queueBeanList.size());

        for(QueueBean qb : queueBeanList){
            Thread t = new Thread(new ThreadTask(qb,ips));
            t.start();
        }
    }

}
