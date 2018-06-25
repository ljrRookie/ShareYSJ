package com.gxysj.shareysj.utils.camear;

import android.net.Uri;

/**
 * Created by Administrator on 2018/2/1.
 */

public class CameraImageBean {
    private Uri mPath = null;
    private static final CameraImageBean INSTANCE = new CameraImageBean();
    public static CameraImageBean getInstance (){
        return INSTANCE;
    }
    public Uri getPath(){
        return mPath;
    }
    public void setPath(Uri realUri){
        this.mPath = realUri;
    }
}
