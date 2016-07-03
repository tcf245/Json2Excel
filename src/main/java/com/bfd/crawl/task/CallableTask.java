
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

	public CallableTask( QueueBean queueBean){
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

	public static void insertRow2Excel(Map<String,Object> xlsRow) throws Exception {
		// 获取总列数
		int CountColumnNum = xlsRow.size();
		// 创建Excel文档
		HSSFWorkbook hwb = new HSSFWorkbook();
		// sheet 对应一个工作页
		HSSFSheet sheet = hwb.createSheet("test");
//        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
//        HSSFCell[] firstcell = new HSSFCell[CountColumnNum];
//        String[] names = new String[CountColumnNum];
//        names[0] = "商品名称";
//        names[1] = "商品url";
//        names[2] = "图片";
//        names[3] = "图片url";
//        names[4] = "价格";
//        names[5] = "促销信息";
//        names[6] = "销售渠道";
//        for (int j = 0; j < CountColumnNum; j++) {
//            firstcell[j] = firstrow.createCell(j);
//            firstcell[j].setCellValue(new HSSFRichTextString(names[j]));
//        }

		// 创建一行
		HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
		// 得到要插入的每一条记录

		for (int colu = 0; colu <= CountColumnNum; colu++) {
			// 在一行内循环
			HSSFCell name = row.createCell(0);
			name.setCellValue((String) xlsRow.get("itemname"));
			HSSFCell url = row.createCell(1);
			url.setCellValue((String) xlsRow.get("url"));
			HSSFCell img = row.createCell(2);
			String imgPath = (String) xlsRow.get("small_img");
//



			//-----------------------------------
//                byte[] byteArray = ImgUtils.mReaderPictureToInternet(imgPath);
//                //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
//                HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
//                //anchor主要用于设置图片的属性
//                HSSFClientAnchor anchor = new HSSFClientAnchor();
//                anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
//                //插入图片
//                patriarch.createPicture(anchor, hwb.addPicture(byteArray, HSSFWorkbook.PICTURE_TYPE_JPEG));


			HSSFCell imgUrl = row.createCell(3);
			imgUrl.setCellValue((String) xlsRow.get("small_img"));
			HSSFCell price = row.createCell(4);
			price.setCellValue((String) xlsRow.get("normalprice"));
			HSSFCell promotionMess = row.createCell(5);
			promotionMess.setCellValue((String) xlsRow.get("promotionMess"));
			HSSFCell qudao = row.createCell(6);
			qudao.setCellValue((String) xlsRow.get("tianmaoxiaoshouqudao"));
		}
		// 创建文件输出流，准备输出电子表格
		OutputStream out = new FileOutputStream("etc/test.xls");
		hwb.write(out);
		out.close();
		System.out.println("数据导出成功");
	}


}
