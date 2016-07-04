package com.bfd.crawl.task;

import com.bfd.crawl.Constants;
import com.bfd.crawl.TaskUtils;
import com.bfd.crawl.ThreadTask;
import com.bfd.crawl.module.QueueBean;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by BFD_303 on 2016/7/4.
 */
public class TaskStart {
    public static void main(String[] args) {

        List<QueueBean> queueBeanList = TaskUtils.getTask();

        for(QueueBean qb : queueBeanList){
            ExecutorService threadPool = Executors.newCachedThreadPool();

                Thread t = new Thread(new ThreadTask(qb));
                System.out.println("queue number is : " + queueBeanList.size() + "  current queue is no. ：" +  queueBeanList.indexOf(qb) + "  queue size is " + qb.getQueue().size());
                threadPool.execute(t);

        }

    }

}
