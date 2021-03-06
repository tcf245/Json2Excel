package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by BFD_303 on 2016/7/4.
 */
public class TaskUtils {
    public static BlockingQueue queue = new LinkedBlockingQueue();
    public static List<QueueBean> queueBeans = new ArrayList<QueueBean>();

    /**
     * 读取json文件  将任务放到队列中
     *
     * @return
     */
    public static List<QueueBean> getTask(String filePath, int rowNum) {
        File jsonFile = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {

            List<String> jsonList =  FileUtils.readLines(jsonFile,"utf-8");

            for (String s : jsonList){}

//            String temp = "";
//            int i = 1;
//
//            QueueBean queueBean = new QueueBean("part" + i, queue);
//
//            while ((temp = reader.readLine()) != null) {
//                if (queue.size() >= rowNum) {
//                    queueBean.setQueue(queue);
//                    queueBeans.add(queueBean);
//                    queue = new LinkedBlockingQueue();
//                    i++;
//                    queueBean = new QueueBean("part" + i, queue);
//                }
//                queue.add(temp);
//            }
//            if (queue.size() > 0) {
//                queueBean.setQueue(queue);
//                queueBeans.add(queueBean);
//            }
            System.out.println("all json has add to queue" + queue.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queueBeans;
    }

    public static List<QueueBean> getTaskByBrand(String filePath) {
        File dir = new File(filePath);
        System.out.println(filePath);
        System.out.println(dir.isDirectory());
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();

            System.out.println("get file count is : " + files.length);

            for (File f : files) {

                try {
                    List<String> jsonList = FileUtils.readLines(f,"utf-8");
                    QueueBean queueBean;
                    for(String s : jsonList){
                        queue.put(s);
                    }
                    queueBean = new QueueBean(f.getName().replace(".txt",""), queue);
                    queueBeans.add(queueBean);
                    System.out.println(f.getName() + " has add to queue , and queue size is :" + queue.size());
                    queue = new LinkedBlockingQueue();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                return queueBeans;
        }
        System.out.println(dir.getAbsolutePath() + " is not a directory ..");
        return null;
    }

    public static void getTaskFromJson() {
        File jsonFile = new File(Constants.ORIGIN_FILE);

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {

            String temp = "";
            int i = 1;


            while ((temp = reader.readLine()) != null) {
                queue.put(temp);
            }
            System.out.println("all json has add to queue" + queue.size());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
