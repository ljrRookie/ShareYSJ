package com.gxysj.shareysj.delegate.home;


import com.gxysj.shareysj.R;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.utils.SPUtil;
import com.vondear.rxtools.view.RxToast;

import me.yokeyword.fragmentation.anim.DefaultNoAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


/**
 * Created by 傅令杰
 */

public abstract class BottomItemDelegate extends YSJDelegate {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            SPUtil.remove(ShareYSJ.getApplicationContext(),"MACHINEID");
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            RxToast.normal("双击退出" + ShareYSJ.getApplicationContext().getString(R.string.app_name));
        }
        return true;
    }


}
