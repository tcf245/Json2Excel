package com.bfd.crawl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Map;


/**
 * Created by tcf24 on 2016/7/2.
 */
public class ImgUtils {

//    /**
//     * get 请求示例代码
//     * @param url
//     * @param charset
//     * @param headers
//     * @return
//     * @throws Exception
//     */
//    public static byte[] httpGet(String url, String charset, Map<String,String> headers, String ip) throws Exception {
//        HttpClientBuilder httpBuilder = HttpClientBuilder.create();
//        httpBuilder.setUserAgent(ua);
//
//        HttpClient client = httpBuilder.build();
//        HttpGet httpget = new HttpGet(url);
//
//        if (null != ip && !"".equals(ip.trim())) {
//            RequestConfig requestConfig = getRequestConfigByIp(ip);
//            if (null != requestConfig) {
//                httpget.setConfig(requestConfig);
//            }
//        }
//
//        if (headers != null && headers.size() > 0) {
//            for (String key : headers.keySet()) {
//                httpget.setHeader(key, headers.get(key));
//            }
//        }
//        HttpResponse response = client.execute(httpget);
//        HttpEntity en = response.getEntity();
//        byte[] b = EntityUtils.toByteArray(en);
//        return b;
//    }
//
//    /**
//     *  指定ip
//     * @param ip
//     * @return
//     */
//    public static RequestConfig getRequestConfigByIp(String ip) {
//
//        RequestConfig requestConfig = null;
//        try {
//            RequestConfig.Builder config_builder = RequestConfig.custom();
//            InetAddress inetAddress = null;
//            inetAddress = InetAddress.getByName(ip);
//            config_builder.setLocalAddress(inetAddress);
//            requestConfig = config_builder.build();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return requestConfig;
//    }

    public static byte[] mReaderPicture(String filePath) {
        byte[] arr = null;
        try {
            File file = new File(filePath);
            FileInputStream fReader = new FileInputStream(file);
            arr = new byte[1024 * 100];
            fReader.read(arr);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return arr;
    }

    //根据byte数组，创建一张新图。
    public static void mWriterPicture(String newFileName, byte[] b) {
        try {
            File file = new File(newFileName);
            FileOutputStream fStream = new FileOutputStream(file);
            fStream.write(b);
            fStream.close();
            System.out.println("图片创建成功    " + b.length);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    //获取指定网址的图片，返回其byte[]
    public static byte[] mReaderPictureToInternet(String strUr1) {
        byte[] imgData = null;
        URL url;
        try {
            url = new URL(strUr1);
            URLConnection connection = url.openConnection();
            int length = (int) connection.getContentLength();
            InputStream is = connection.getInputStream();
            if (length != -1) {
                imgData = new byte[length];
                byte[] temp = new byte[500 * 1024];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, imgData, destPos, readLen);
                    //arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
                    //从指定源数组中复制一个数组，复制从指定的位置开始，到目标数组的指定位置结束
                    destPos += readLen;
                }
            }
            return imgData;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imgData;
    }

    //直接获取指定网址的图片

    public static void DownPictureToInternet(String filePath, String strUr1) {
        try {
            URL url = new URL(strUr1);
            InputStream fStream = url.openConnection().getInputStream();
            int b = 0;
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            while ((b = fStream.read()) != -1) {
                fos.write(b);
            }
            fStream.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        mWriterPicture("etc/1.jpg", mReaderPicture("res/me.jpg"));
        mWriterPicture("etc/2.jpg", mReaderPictureToInternet(
                "http://img.alicdn.com/bao/uploaded/i1/TB1a00IHVXXXXXIXXXXXXXXXXXX_!!0-item_pic.jpg_60x60q90.jpg"));
        DownPictureToInternet("etc/下载.jpg",
                "http://img.alicdn.com/bao/uploaded/i1/TB1a00IHVXXXXXIXXXXXXXXXXXX_!!0-item_pic.jpg_60x60q90.jpg");

    }
}