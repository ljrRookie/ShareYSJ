package com.gxysj.shareysj.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * jdk 1.7
 *
 * @author Administrator
 */
public class Labi_HttpPostUploadUtil extends Thread {


    public Labi_HttpPostUploadUtil(Context context, Handler handler2,
                                   String url_str, Map<String, String> text_map,
                                   Map<String, String> file_map) {

        this.url_str = url_str;
        this.text_map = text_map;
        this.file_map = file_map;
        this.context = context;
        this.handler = handler2;
    }

    private Context context;
    private Handler handler;
    private String url_str = "";
    private Map<String, String> text_map = null;
    private Map<String, String> file_map = null;


    public void run() {

        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------";

        try {
            URL url = new URL(url_str);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text


            if (text_map != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = text_map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();

                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();

                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(
                            "\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            if (file_map != null) {
                Iterator iter = file_map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }


                    String contentType = "images/png";
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(
                            "\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"; filename=\"" + inputValue
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

                    out.write(strBuf.toString().getBytes());

//	                        DataInputStream in = new DataInputStream(context.openFileInput(inputValue));  

                    FileInputStream inputStream = new FileInputStream(new File(inputValue));

                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = inputStream.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
//	                            Log.i("app", "image>>"+new String(bufferOut, 0, bytes));
                    }
                    inputStream.close();
                }
            }


            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            // System.out.println("发送POST请求出错。" + url);
            Log.e("app", "发送Post 请求出错" + e);
            Bundle bundle = new Bundle();
            bundle.putString("data", "10000");
            Message message = new Message();
            message.setData(bundle);
            handler.sendMessage(message);
            return;
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
//			Log.i("app", "访问成功" + res );
        Bundle bundle = new Bundle();
        bundle.putString("data", res);
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);

    }


}
