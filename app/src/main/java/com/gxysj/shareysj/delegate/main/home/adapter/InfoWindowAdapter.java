package com.gxysj.shareysj.delegate.main.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.main.home.MapActivity;
import com.gxysj.shareysj.utils.OpenLocalMapUtil;
import com.gxysj.shareysj.utils.SPUtil;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.view.RxTextAutoZoom;
import com.vondear.rxtools.view.RxToast;

/**
 * Created by 林佳荣 on 2018/4/13.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class InfoWindowAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {
    private final String mAddress;
    private final AMapLocation mLatitude;
    private final String mCity;
    private final MapActivity mActivity;
    private Context mContext = ShareYSJ.getApplicationContext();
    private LatLng latLng;
    private TextView navigation;
    private TextView nameTV;
    private String agentName;
    private RxTextAutoZoom addrTV;
    private String snippet;
    private String title = "";
    private String id = "";
    private TextView btnBind;

    public InfoWindowAdapter(MapActivity mapActivity, String address, AMapLocation latitude, String city) {
        this.mAddress = address;
        this.mLatitude = latitude;
        this.mCity = city;
        this.mActivity = mapActivity;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.navigation_LL) {
            RxToast.normal("正在调用导航组件");
       /* double[] startDoubles = OpenLocalMapUtil.gaoDeToBaidu(mLatitude.getLatitude(), mLatitude.getLongitude());
        double[] endDoubles = OpenLocalMapUtil.gaoDeToBaidu(latLng.latitude, latLng.longitude);
        String baiduMapUri = OpenLocalMapUtil.getBaiduMapUri(startDoubles[0],startDoubles[1], mAddress, endDoubles[0], endDoubles[1], agentName, mCity, (mContext.getString(R.string.app_name)));
        RxLogTool.e("map",baiduMapUri );
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(baiduMapUri));
        mActivity.startActivity(intent);*/
            Poi start = new Poi("", new LatLng(mLatitude.getLatitude(), mLatitude.getLongitude()), "");
            Poi end = new Poi(agentName, new LatLng(latLng.latitude, latLng.longitude), "");
            AmapNaviPage.getInstance().showRouteActivity(mContext, new AmapNaviParams(start, null, end, AmapNaviType.WALK), new INaviInfoCallback() {
                @Override
                public void onInitNaviFailure() {

                }

                @Override
                public void onGetNavigationText(String s) {

                }

                @Override
                public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

                }

                @Override
                public void onArriveDestination(boolean b) {

                }

                @Override
                public void onStartNavi(int i) {

                }

                @Override
                public void onCalculateRouteSuccess(int[] ints) {

                }

                @Override
                public void onCalculateRouteFailure(int i) {

                }

                @Override
                public void onStopSpeaking() {

                }

                @Override
                public void onReCalculateRoute(int i) {

                }

                @Override
                public void onExitPage(int i) {

                }
            });
        } else if (viewId == R.id.btn_bind) {
          if(id.isEmpty()){
              RxToast.normal("绑定失败");
          }else{
              SPUtil.put(ShareYSJ.getApplicationContext(),"MACHINEID",id);
              RxToast.normal("绑定成功，前往首页可查看本机详情");

          }
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        initData(marker);
        View view = initView();
        return view;
    }

    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.map_mark_infowindow, null);
        navigation = (TextView) view.findViewById(R.id.navigation_LL);
        nameTV = (TextView) view.findViewById(R.id.tv_name);
        btnBind = (TextView) view.findViewById(R.id.btn_bind);
        addrTV = (RxTextAutoZoom) view.findViewById(R.id.agent_addr);
        String[] split = agentName.split(",");
        title = split[0];
        id = split[1];
        nameTV.setText(title);
        addrTV.setText(snippet);
        Log.e("map", "initView:=============="+snippet);
        navigation.setOnClickListener(this);
        btnBind.setOnClickListener(this);
        return view;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
        snippet = marker.getSnippet();
        agentName = marker.getTitle();
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
