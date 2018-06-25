package com.gxysj.shareysj.delegate.main.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.allen.library.SuperButton;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.NearbyMachineBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.main.home.adapter.InfoWindowAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/6.
 */

public class MapActivity extends AppCompatActivity implements AMap.OnMapClickListener, AMap.OnMarkerClickListener {

    AMap aMap = null;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.btn_scan)
    SuperButton mBtnScan;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.edit_key)
    EditText mEditKey;
    @BindView(R.id.btn_clean)
    ImageView mBtnClean;
    @BindView(R.id.btn_location)
    Button mBtnLocation;
    private AMapLocationClient mAMapLocationClient;
    private AMapLocationClientOption mAMapLocationClientOption;
    private RxDialog mDialog;
    private double mLatitude;
    private double mLongitude;
    private Marker mMarker;
    private SensorManager mSM;
    private Sensor mSensor;
    private InfoWindowAdapter adapter;
    private Marker oldMarker = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSON_REQUESTCODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;
    private LatLng mLatLng;

    //----------以下动态获取权限---------
    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheck) {
            checkPermissions(needPermissions);
        } else {
            mMapView.onResume();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMapView.onResume();
    }

    /**
     * 检查权限
     *
     * @param
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        //获取权限列表
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            //list.toarray将集合转化为数组
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        //for (循环变量类型 循环变量名称 : 要被遍历的对象)
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {      //没有授权
                showMissingPermissionDialog();              //显示提示信息
                isNeedCheck = false;
            }
        }
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mDialog = new RxDialog(this);
        setContentView(R.layout.delegate_hone_map);
        RxBarTool.setTransparentStatusBar(this);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        initLocation();
        // initEditListener();

    }

    private void initMarker() {

        RxLogTool.e("Marker", mLatitude + "    " + mLongitude);
        RequestParams params = new RequestParams();
        params.put("coordinate_x", mLatitude);
        params.put("coordinate_y", mLongitude);
        RequestCenter.GetNearbyMachine(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                NearbyMachineBean nearbyMachineBean = (NearbyMachineBean) responseObj;
                List<NearbyMachineBean.DataBean> data = nearbyMachineBean.getData();
                if (data != null && data.size() > 0) {
                    ArrayList<MarkerOptions> markerOptionseList = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        NearbyMachineBean.DataBean dataBean = data.get(i);
                        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker))
                                .position(new LatLng(Double.valueOf(dataBean.getCoordinate_x()), Double.valueOf(dataBean.getCoordinate_y())))
                                .title(dataBean.getName() + "," + dataBean.getId())
                                .snippet("编号:" + dataBean.getNumber());
                        Log.e("map", "snippet:==============" + dataBean.getNumber());

                        markerOptionseList.add(markerOptions);
                    }
                    aMap.addMarkers(markerOptionseList, false);

                } else {
                    RxToast.normal("附近没有售货机");
                }
            }

            @Override
            public void onFailure(Object responseObj) {
                RxToast.normal("获取附近收货机失败");
            }
        });


    }


 /*   private void initEditListener() {
        mEditKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBtnClean.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mBtnClean.setVisibility(View.INVISIBLE);
                }
            }
        });
    }*/

    private void initLocation() {
        // 设置定位监听
        aMap.setLocationSource(new LocationSource() {
            public OnLocationChangedListener mListener;

            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mListener = onLocationChangedListener;
                if (mAMapLocationClient == null) {
                    //初始化定位
                    mAMapLocationClient = new AMapLocationClient(ShareYSJ.getApplicationContext());
                    //初始化定位参数
                    mAMapLocationClientOption = new AMapLocationClientOption();
                    //设置定位回调监听
                    mAMapLocationClient.setLocationListener(new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation aMapLocation) {
                            if (aMapLocation != null
                                    && aMapLocation.getErrorCode() == 0) {
                                aMapLocation.getCity();
                                //  RxToast.normal(aMapLocation.getCity() + ":" + aMapLocation.getCityCode() +":"+ aMapLocation.getAddress());
                                mLatitude = aMapLocation.getLatitude();
                                mLongitude = aMapLocation.getLongitude();
                                mLatLng = new LatLng(mLatitude, mLongitude);
                                if (isFirstLoc) {
                                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                                    mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                                    initMarker();
                                    adapter = new InfoWindowAdapter(MapActivity.this, aMapLocation.getAddress(), aMapLocation, aMapLocation.getCity());
                                    aMap.setInfoWindowAdapter(adapter);

                                    isFirstLoc = false;
                                }


                            } else {
                                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                                Log.e("AmapErr", errText);
                            }
                        }
                    });
                    //设置为高精度定位模式
                    mAMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

                    //设置定位参数
                    mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
                    // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                    // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                    // 在定位结束后，在合适的生命周期调用onDestroy()方法
                    // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                    mAMapLocationClient.startLocation();//启动定位
                }
            }

            @Override
            public void deactivate() {
                if (mAMapLocationClient != null) {
                    mAMapLocationClient.stopLocation();
                    mAMapLocationClient.onDestroy();
                }
                mAMapLocationClient = null;
            }
        });
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(50000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_location);
        myLocationStyle.myLocationIcon(bitmapDescriptor);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (null != mAMapLocationClient) {
            mAMapLocationClient.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    @OnClick({R.id.btn_location, R.id.btn_back, R.id.btn_clean})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_location:
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 17));
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_clean:
               /* LatLng position = mMarker.getPosition();*/

                mEditKey.setText("");
                mBtnClean.setVisibility(View.INVISIBLE);
                break;
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        //点击地图上没marker 的地方，隐藏inforwindow
        if (oldMarker != null) {
            oldMarker.hideInfoWindow();

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        oldMarker = marker;
        return false; //返回 “false”，除定义的操作之外，默认操作也将会被执行
    }

}
