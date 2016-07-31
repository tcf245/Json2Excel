package com.bfd.crawl.module;

import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;

/**
 *
 * Created by tcf24 on 2016/7/3.
 */
public class QueueBean {
    private String name;
    private BlockingQueue queue;

    public QueueBean(String name, BlockingQueue queue) throws FileNotFoundException {
        this.name = name;
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BlockingQueue getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue queue) {
        this.queue = queue;
    }

}
