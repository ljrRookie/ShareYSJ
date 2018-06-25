package com.gxysj.shareysj.utils;

import android.text.format.Time;

import com.gxysj.shareysj.config.ShareYSJ;
import com.vondear.rxtools.RxEncryptTool;
import com.vondear.rxtools.RxTimeTool;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 林佳荣 on 2018/3/28.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class TimeUtils {
    public static String getDateFromTime(){
        Time t = new Time();
        t.setToNow();
        int temp = t.month;
        int month = temp + 1;
        return t.year+"-"+month+"-"+t.monthDay;
    }
    public static String getSecretKey(String s){

        if(s==null||s.equals("")){

            return "0000-00-00 " ;
        }

        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);

        String secret_key = "AH20181818123999system.com.out.gc.pc.do_"+res+SPUtil.get(ShareYSJ.getApplicationContext(),"userId","")+s;

        return StringToMd5(secret_key);
    }
    public static String StringToMd5(String psw) {
        {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(psw.getBytes("UTF-8"));
                byte[] encryption = md5.digest();

                StringBuffer strBuf = new StringBuffer();
                for (int i = 0; i < encryption.length; i++) {
                    if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                        strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                    } else {
                        strBuf.append(Integer.toHexString(0xff & encryption[i]));
                    }
                }

                return strBuf.toString();
            } catch (NoSuchAlgorithmException e) {
                return "";
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
    }
}
