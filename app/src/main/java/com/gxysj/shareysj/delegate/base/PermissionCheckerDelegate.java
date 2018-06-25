package com.gxysj.shareysj.delegate.base;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.delegate.home.ScannerDelegate;
import com.gxysj.shareysj.utils.callback.CallbackManager;
import com.gxysj.shareysj.utils.callback.CallbackType;
import com.gxysj.shareysj.utils.callback.IGlobalCallback;
import com.gxysj.shareysj.utils.camear.CameraImageBean;
import com.gxysj.shareysj.utils.camear.CameraUtils;
import com.gxysj.shareysj.utils.camear.RequestCodes;
import com.vondear.rxtools.view.RxToast;
import com.yalantis.ucrop.UCrop;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2018/1/29.
 */
@RuntimePermissions
public abstract class PermissionCheckerDelegate extends BaseDelegate {

    @NeedsPermission(Manifest.permission.CAMERA)
    void startCamera() {
        CameraUtils.start(this);
    }

    public void startCameraWithCheck() {
        PermissionCheckerDelegatePermissionsDispatcher.startCameraWithCheck(this);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void onCameraDenied() {
        RxToast.normal("不允许拍照");
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onCameraNever() {
        RxToast.normal("永久拒绝了拍照权限");
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void onCameraRationale(final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setPositiveButton("同意使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("权限管理")
                .show();
    }
    //扫描二维码(不直接调用)
    @NeedsPermission(Manifest.permission.CAMERA)
    void startScan(BaseDelegate delegate) {
        delegate.getSupportDelegate().startForResult(new ScannerDelegate(), RequestCodes.SCAN);
    }
    public void startScanWithCheck(BaseDelegate delegate){
        PermissionCheckerDelegatePermissionsDispatcher.startScanWithCheck(this,delegate);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCheckerDelegatePermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UCrop.Options options = initUcrop();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.TAKE_PHOTO:
                    final Uri resultUri = CameraImageBean.getInstance().getPath();
                    UCrop.of(resultUri, resultUri)
                            .withMaxResultSize(500, 500)
                            .withOptions(options)
                            .start(getContext(), this);
                    break;
                case RequestCodes.PICK_PHOTO:
                    final Uri pickPath = data.getData();
                    //从相册选择后需要有个路径存放剪裁过的图片
                    final String pickCropPath = CameraUtils.createCropFile().getPath();
                    UCrop.of(pickPath, Uri.parse(pickCropPath))
                            .withMaxResultSize(500, 500)
                            .withOptions(options)
                            .start(getContext(), this);
                    break;
                case RequestCodes.CROP_PHOTO:
                    final Uri cropUri = UCrop.getOutput(data);
                    //拿到剪裁后的数据进行处理
                    IGlobalCallback<Uri> callback = CallbackManager.getIntance().getCallback(CallbackType.ON_CROP);
                    if (callback != null) {
                        callback.excuteCallback(cropUri);
                    }
                    break;
                case RequestCodes.CROP_ERROR:
                    RxToast.normal("剪裁出现错误");
                    break;

                default:
                    RxToast.normal("剪裁出现错误");
                    break;
            }
        }
    }

    private UCrop.Options initUcrop() {
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        // options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        // options.setCircleDimmedLayer(true);
        //设置是否展示矩形裁剪框
        //  options.setShowCropFrame(false);
        return options;
    }
}
