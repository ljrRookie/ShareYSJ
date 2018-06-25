package com.gxysj.shareysj.utils.callback;

import android.telecom.Call;

import java.util.WeakHashMap;

/**
 * Created by Administrator on 2018/2/1.
 */

public class CallbackManager {
    private static final WeakHashMap<Object, IGlobalCallback> CALLBACK_WEAK_HASH_MAP = new WeakHashMap<>();
    private static class Holder{
        private static final CallbackManager INSTANCE = new CallbackManager();
    }
    public static CallbackManager getIntance(){
        return Holder.INSTANCE;
    }
    public CallbackManager addCallback(Object tag,IGlobalCallback callback){
        CALLBACK_WEAK_HASH_MAP.put(tag, callback);
        return this;
    }
    public IGlobalCallback getCallback(Object tag){
        return CALLBACK_WEAK_HASH_MAP.get(tag);
    }
}
