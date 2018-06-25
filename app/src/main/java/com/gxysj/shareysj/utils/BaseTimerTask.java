package com.gxysj.shareysj.utils;


import com.gxysj.shareysj.listener.ITimerListener;

import java.util.TimerTask;

/**
 * Created by 傅令杰 on 2017/4/22
 */

public class BaseTimerTask extends TimerTask {

    private ITimerListener mITimerListener = null;

    public BaseTimerTask(ITimerListener timerListener) {
        this.mITimerListener = timerListener;
    }

    @Override
    public void run() {
        if (mITimerListener != null) {
            mITimerListener.onTimer();
        }
    }
}
