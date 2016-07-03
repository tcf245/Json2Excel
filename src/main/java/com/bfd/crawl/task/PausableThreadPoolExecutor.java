/**
 * 线程池
 */
package com.bfd.crawl.task;

import java.lang.reflect.Field;
import java.lang.reflect.Array;
import java.lang.ref.Reference;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionHandler;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;


public class PausableThreadPoolExecutor extends ThreadPoolExecutor {
    private boolean isPaused;

    private ReentrantLock pauseLock = new ReentrantLock();

    private Condition unpaused = pauseLock.newCondition();


    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
    
    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    private static void cleanThreadLocals(Thread thread) {
        try {
            // Get a reference to the thread locals table of the current thread
            Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);
            Object threadLocalTable = threadLocalsField.get(thread);

            // Get a reference to the array holding the thread local variables inside the
            // ThreadLocalMap of the current thread
            Class threadLocalMapClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
            Field tableField = threadLocalMapClass.getDeclaredField("table");
            tableField.setAccessible(true);
            Object table = tableField.get(threadLocalTable);

            // The key to the ThreadLocalMap is a WeakReference object.
            // The referent field of this object is a reference to the
            //   actual ThreadLocal variable.
            Field referentField = Reference.class.getDeclaredField("referent");
            referentField.setAccessible(true);

            for (int i = 0; i < Array.getLength(table); i++) {
                // Each entry in the table array of ThreadLocalMap is an Entry object
                // representing the thread local reference and its value
                Object entry = Array.get(table, i);
                if (entry != null) {
                    // Get a reference to the thread local object and remove it from the table
                    ThreadLocal threadLocal = (ThreadLocal) referentField.get(entry);
                    threadLocal.remove();
                }
            }
        } catch(Exception e) {
            // We will tolerate an exception here and just log it
            throw new IllegalStateException(e);
        }
    }


    // called before execution of each task. These can be used to manipulate
    // the execution environment; for example, reinitializing ThreadLocals,
    // gathering statistics, or adding log entries.
    protected void beforeExecute(Thread t, Runnable r) {
        pauseLock.lock();
     
        try {
            while (isPaused) {
                unpaused.await();
            }

            System.out.println(">>>>beforeExecute on thread#" + t.getId());
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }

        super.beforeExecute(t, r);
    }


    // called after execution of each task.
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        long threadId = Thread.currentThread().getId();

        if (t == null && r instanceof Future<?>) {
            try {
                Object result = ((Future<?>) r).get();
                
                System.out.println("<<<<afterExecute on thread#" + threadId );
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt(); // ignore/reset
            }
        }
     
        if (t != null) {
            System.out.println(t);
        }
    }


    // method terminated() can be overridden to perform any special
    // processing that needs to be done once the Executor has fully terminated.
    protected void terminated() {

        cleanThreadLocals(Thread.currentThread());

        System.out.println("----terminated: cleanupConnection");
        super.terminated();
    }


    public void pause() {
        pauseLock.lock();
     
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    
    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }
}
