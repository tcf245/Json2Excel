/**
 * RejectedExecutionHandlerImpl.java
 *
 * created by zhangliang, 2016-04-28
 */
package com.bfd.crawl.task;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;


public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//        SingletonPausableWorkerPool.arrayQueue.put();
        System.out.println(r.toString() + " is rejected");
    }
}
