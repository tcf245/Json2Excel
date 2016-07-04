import com.bfd.crawl.Constants;
import com.bfd.crawl.ExcelInsert;
import com.bfd.crawl.module.QueueBean;
import com.bfd.crawler.utils.JsonUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by BFD_303 on 2016/7/4.
 */
public class ExcelInsertTest {

    public static BlockingQueue arrayQueue = new LinkedBlockingQueue(1000);
    public static QueueBean queueBean = new QueueBean("partTest",arrayQueue);

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

    @Test
    public void ExcelInsertTest(){
        for (int i = 0; i < arrayQueue.size(); i++) {
            try {
                String json = (String) arrayQueue.take();
                Map<String,Object> map = JsonUtils.parseObject(json);

                ExcelInsert.insertRow2Excel(map,queueBean);
                System.out.println(i);


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
