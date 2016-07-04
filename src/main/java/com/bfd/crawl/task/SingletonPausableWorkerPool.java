package com.bfd.crawl.task;

import com.bfd.crawl.Constants;
import com.bfd.crawl.module.QueueBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
 * 线程池单例工具调用实现
 *
 */
public class SingletonPausableWorkerPool {

    private PausableWorkerPool workerPool;

    private static enum Singleton {
        INSTANCE;

        private static final SingletonPausableWorkerPool singleton = new SingletonPausableWorkerPool();

        public SingletonPausableWorkerPool getSingleton() {
            return singleton;
        }
    }

    public static BlockingQueue arrayQueue = new LinkedBlockingQueue(1000);
    public static QueueBean queueBean = new QueueBean("part",arrayQueue);


    private SingletonPausableWorkerPool() {
        try {
            // TODO: load config here

            this.workerPool = new PausableWorkerPool(/* config */);
        } catch (InterruptedException e) {

        }
    }


    public static SingletonPausableWorkerPool getInstance() {
        return Singleton.INSTANCE.getSingleton();
    }


    public PausableWorkerPool getWorkerPool() {
        return workerPool;
    }


    public void startWorkerPool() {
        this.workerPool.startup();
    }


    public void stopWorkerPool() {
        this.workerPool.shutdown();
    }


    public static void main(String args[]) throws InterruptedException {
        // when application begin:
        SingletonPausableWorkerPool.getInstance().startWorkerPool();

//        List<QueueBean> queueBeanList = getTask();

        // do with tasks:
        PausableWorkerPool wp = SingletonPausableWorkerPool.getInstance().getWorkerPool();
        ThreadPoolExecutor executorPool = wp.getExecutorPool();

        // Submit tasks to the thread pool
//        for (int i = 0; i < queueBeanList.size(); i++) {
//            Thread  t =  new Thread(new SubmitThread(queueBeanList.get(i),executorPool));
//            t.start();
//        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getTaskFromJson();
            }
        });
        thread.start();

        Thread  t =  new Thread(new SubmitThread(queueBean,executorPool));
        t.start();


        Thread.sleep(30000);

        // when application exit:
        SingletonPausableWorkerPool.getInstance().stopWorkerPool();
    }


    public static BlockingQueue queue = new LinkedBlockingQueue();
    public static List<QueueBean> queueBeans = new ArrayList<QueueBean>();

    /**
     * 读取json文件  将任务放到队列中
     * @return
     */
    public static List<QueueBean> getTask() {
        File jsonFile = new File(Constants.ORIGIN_FILE);

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))){

            String temp = "";
            int i = 1;

            QueueBean queueBean = new QueueBean("part" + i,queue);

            while(( temp = reader.readLine()) != null){
                if(queue.size() >= Constants.ROW_NUMBER){
                    queueBean.setQueue(queue);
                    queueBeans.add(queueBean);
                    queue = new LinkedBlockingQueue();
                    i++;
                    queueBean = new QueueBean("part" + i,queue);
                }
                queue.add(temp);
                System.out.println(queue.size());
            }
            if(queue.size() > 0){
                queueBean.setQueue(queue);
                queueBeans.add(queueBean);
            }
            System.out.println("all json has add to queue" + queue.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queueBeans;
    }

    public static void getTaskFromJson(){
        File jsonFile = new File(Constants.ORIGIN_FILE);

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))){

            String temp = "";
            int i = 1;


            while(( temp = reader.readLine()) != null) {
                arrayQueue.put(temp);
            }
            System.out.println("all json has add to queue" + arrayQueue.size());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}


class  SubmitThread  implements  Runnable{

    private QueueBean qb ;


    ThreadPoolExecutor  executorPool;
    public SubmitThread (QueueBean qb , ThreadPoolExecutor executorPool) {
        this.qb = qb;
        this.executorPool = executorPool;
    }
    @Override
    public void run() {
        int numTasks =  Constants.ROW_NUMBER;
        for(int i = 0; i < numTasks; i++) {
            Future<Integer> future = executorPool.submit(new CallableTask(qb));

            try {
                Integer ret = future.get(10, TimeUnit.MILLISECONDS);


                System.out.println("Finished task with result:" + ret);
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (TimeoutException e) {
            } finally {
            }
        }
    }
}