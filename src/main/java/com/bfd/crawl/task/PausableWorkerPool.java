/**
 *线程池工具入口类
 */
package com.bfd.crawl.task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class PausableWorkerPool {
    private int maxTasks = 400000;
    private int keepAliveSeconds = 300;
    private int corePoolSize = 10;
    private int maxPoolSize = 64;
    private int MonitorTime = 5;
    
    private final RejectedExecutionHandler rejectionHandler;
    private final ThreadFactory threadFactory;
    private final ThreadPoolExecutor executorPool;
    private final MonitorThread monitor;
    private final Thread monitorThread;


    public PausableWorkerPool(/* config */) throws InterruptedException {
       

    	// TODO: Read config here
        // maxTasks = 2000;
        // keepAliveSeconds = 120;
        // corePoolSize = 8;
        // maxPoolSize = 64;
    
        // RejectedExecutionHandler implementation
        rejectionHandler = new RejectedExecutionHandlerImpl();

        // Get the ThreadFactory implementation to use
        threadFactory = Executors.defaultThreadFactory();

        // Creating the PausableThreadPoolExecutor
        executorPool =
            new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(maxTasks), // BlockingQueue
                threadFactory,
                rejectionHandler);

        // Start the monitoring thread
        monitor = new MonitorThread(executorPool, MonitorTime);

        monitorThread = new Thread(monitor);
    }

    
    public ThreadPoolExecutor getExecutorPool() {
        return this.executorPool;        
    }

    
    MonitorThread getMonitor() {
        return this.monitor;
    }

   
    public void startup() {
        monitorThread.start();
    }
    

    public void shutdown() {
        executorPool.shutdown();
        
        // Thread.sleep(5000);

        monitor.shutdown(); 
    }


    
}