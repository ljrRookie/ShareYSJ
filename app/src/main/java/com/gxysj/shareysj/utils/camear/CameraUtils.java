package com.gxysj.shareysj.utils.camear;

import android.net.Uri;

import com.gxysj.shareysj.delegate.base.PermissionCheckerDelegate;
import com.gxysj.shareysj.utils.FileUtil;

import java.io.File;

/**
 * Created by Administrator on 2018/2/1.
 */

public class CameraUtils {
    public static Uri createCropFile(){
        return Uri.parse(FileUtil.createFile("crop_image", FileUtil.getFileNameByTime("IMG", "jpg")).getPath());
    }
    public static void start(PermissionCheckerDelegate delegate){
        new CameraHandler(delegate).beginCameraDialog();
    }
}
