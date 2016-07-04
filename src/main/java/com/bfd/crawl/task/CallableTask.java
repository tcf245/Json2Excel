
package com.bfd.crawl.task;

import com.bfd.crawl.ExcelInsert;
import com.bfd.crawl.HssfHelp;
import com.bfd.crawl.module.QueueBean;
import com.bfd.crawler.utils.JsonUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;


/**
 * 处理线程任务
 * @author tcf24
 *
 */
public  class CallableTask  implements Callable<Integer> {

	private QueueBean queueBean;

	public CallableTask(QueueBean queueBean){
		this.queueBean = queueBean;
	}

    @Override
    public synchronized Integer call() throws Exception {

		try {

			BlockingQueue queue = queueBean.getQueue();

			String jsonString = (String) queue.take();
			System.out.println("queue size is :" + queue.size());

			Map<String,Object> data = JsonUtils.parseObject(jsonString);
			ExcelInsert.insertRow2Excel(data, queueBean);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}


    	return 0 ;
    }

}
