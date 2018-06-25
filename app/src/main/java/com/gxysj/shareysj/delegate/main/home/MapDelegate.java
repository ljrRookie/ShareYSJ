package com.gxysj.shareysj.delegate.main.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.allen.library.SuperButton;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.activity.ActivityScanerCode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.anim.DefaultNoAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Administrator on 2018/2/6.
 */

public class MapDelegate extends YSJDelegate {
    @BindView(R.id.map)
    MapView mMapView;
    Unbinder unbinder;
    AMap aMap = null;
    @BindView(R.id.btn_scan)
    SuperButton mBtnScan;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.edit_key)
    EditText mEditKey;
    @BindView(R.id.btn_clean)
    ImageView mBtnClean;
    Unbinder unbinder1;
    private AMapLocationClient mAMapLocationClient;
    private AMapLocationClientOption mAMapLocationClientOption;

    @Override
    public Object setLayout() {
        return R.layout.delegate_hone_map;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    @Override
    public void onEnterAnimationEnd(@Nullable Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.setMinZoomLevel(18);
        }
        initLocation();
        initEditListener();
    }

    private void initEditListener() {
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
            if(TextUtils.isEmpty(s)){
                mBtnClean.setVisibility(View.INVISIBLE);
            }
            }
        });
    }

    private void initLocation() {
        // 设置定位监听
        aMap.setLocationSource(new LocationSource() {
            public OnLocationChangedListener mListener;

            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mListener = onLocationChangedListener;
                if (mAMapLocationClient == null) {
                    //初始化定位
                    mAMapLocationClient = new AMapLocationClient(getContext());
                    //初始化定位参数
                    mAMapLocationClientOption = new AMapLocationClientOption();
                    //设置定位回调监听
                    mAMapLocationClient.setLocationListener(new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation aMapLocation) {
                            if (aMapLocation != null
                                    && aMapLocation.getErrorCode() == 0) {

                                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

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
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
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


    @OnClick({R.id.btn_scan, R.id.btn_back, R.id.btn_clean})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                RxActivityTool.skipActivity(getActivity(), ActivityScanerCode.class);
                break;
            case R.id.btn_back:
                getSupportDelegate().pop();

                break;
            case R.id.btn_clean:
                mEditKey.setText("");
                mBtnClean.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
