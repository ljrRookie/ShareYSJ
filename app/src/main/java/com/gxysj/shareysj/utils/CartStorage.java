package com.gxysj.shareysj.utils;

import android.content.Context;
import android.util.SparseArray;

import com.gxysj.shareysj.bean.CartBean;
import com.gxysj.shareysj.bean.MachineGoodBean;
import com.gxysj.shareysj.config.ShareYSJ;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/29.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class CartStorage {
    private Context mContext;
    private SparseArray<MachineGoodBean.DataBean> mCartBeanSparseArray;
    private static CartStorage sCartStorage;

    public CartStorage(Context context) {
        mContext = context;
        mCartBeanSparseArray = new SparseArray<>(100);
    }
    public static CartStorage getInstance(){
        if(sCartStorage == null){
            sCartStorage = new CartStorage(ShareYSJ.getApplicationContext());
        }
        return sCartStorage;
    }
    /**
     * 添加数据
     */
    public void addData(MachineGoodBean.DataBean goodsBean) {
        //1.添加到内存中SparesAray
        //如果添加的某条数据已存在则number+1
        MachineGoodBean.DataBean tempData = mCartBeanSparseArray.get(Integer.parseInt(goodsBean.getId()));
        if (tempData != null) {
            //内存中存在这条数据
            tempData.setCount(tempData.getCount() + 1);
        } else {
            tempData = goodsBean;
            tempData.setCount(1);
        }
        //同步到内存中
        mCartBeanSparseArray.put(Integer.parseInt(goodsBean.getId()), tempData);

    }
    /**
     * 删除数据
     *
     * @param goodsBean
     */
    public void deleteData(MachineGoodBean.DataBean goodsBean) {
        MachineGoodBean.DataBean dataBean = mCartBeanSparseArray.get(Integer.parseInt(goodsBean.getId()));
        int count = dataBean.getCount();
            dataBean.setCount(count-1);
            if(count - 1 ==0){
                mCartBeanSparseArray.delete(Integer.parseInt(goodsBean.getId()));
            }else{
                mCartBeanSparseArray.put(Integer.parseInt(goodsBean.getId()), dataBean);
            }

        //1.内存中删除

    }
    public void clearCartData(){
        mCartBeanSparseArray.clear();
    }
    public List< MachineGoodBean.DataBean> cartSparseToList() {
        List< MachineGoodBean.DataBean> goodsBeanList = new ArrayList<>();
        for (int i = 0; i < mCartBeanSparseArray.size(); i++) {
            MachineGoodBean.DataBean goodsBean = mCartBeanSparseArray.valueAt(i);
            goodsBeanList.add(goodsBean);
        }
        return goodsBeanList;
    }

}
