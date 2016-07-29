package com.bfd.crawl;

import com.bfd.crawl.module.QueueBean;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by tcf24 on 2016/7/29.
 */
public class GetTask implements Runnable{
    private String filePath;

    public GetTask(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        BlockingQueue queue = new LinkedBlockingQueue();
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
                    queueBean = new QueueBean(f.getName(), queue);
                    queue = new LinkedBlockingQueue();
                    TaskStart.taskQueue.put(queueBean);
                    System.out.println(f.getName() + " has add to queue , and queue size is :" + queue.size());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
