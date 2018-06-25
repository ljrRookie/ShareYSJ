package com.ljr.delegate_sdk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by 林佳荣 on 2018/2/8.
 * Github：https://github.com/ljrRookie
 * Function ：UniverImageLoader,加载图片工具
 */

public class ImageLoaderUtils {
    //线程数
    private static final int THREAD_COUNT = 3;
    //线程优先级
    private static final int PRIORITY = 2;
    //内存缓存大小
    private static final int MEMORY_CACHE_SIZE = 2 * 1024 * 1024;
    //硬盘缓存大小
    private static final int DISK_CACHE_SIZE = 20 * 1024 * 1024;
    //连接超时
    private static final int CONNECTION_TIME_OUT = 5 * 1000;
    //读取超时
    private static final int READ_TIME_OUT = 30 * 1000;

    private static ImageLoaderUtils mInstance = null;
    private static ImageLoader mLoader = null;
    private DisplayImageOptions mDefaultOptions;

    /**
     * 私有构造方法完成初始化工作
     *
     * @param context
     */
    public ImageLoaderUtils(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(THREAD_COUNT)//配置图片下载线程的最大数量
                .threadPriority(Thread.NORM_PRIORITY - PRIORITY)//线程优先级
                .denyCacheImageMultipleSizesInMemory()//防止缓存多套尺寸的图片到我们的内存中
                //   .memoryCache(new UsingFreqLimitedMemoryCache(MEMORY_CACHE_SIZE))
                .memoryCache(new WeakMemoryCache())//使用弱引用内存缓存
                .diskCacheSize(DISK_CACHE_SIZE)//分配硬盘缓存大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//使用MD5命名文件
                .tasksProcessingOrder(QueueProcessingType.FIFO)//图片下载顺序
                .defaultDisplayImageOptions(getDefaultOptions())//默认的图片加载Options
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT))//设置图片下载器
                .writeDebugLogs()//debug环境下输出日志
                .build();
        ImageLoader.getInstance().init(config);
        mLoader = ImageLoader.getInstance();
    }

    public static ImageLoaderUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ImageLoaderUtils.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtils(context);
                }
            }
        }
        return mInstance;
    }

    public DisplayImageOptions getDefaultOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//缓存到内存中
                .cacheOnDisk(true)//缓存到sd卡，硬盘
                .considerExifParams(true)//考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .build();
        return options;
    }

    public DisplayImageOptions getOptionsWithNoCache() {

        DisplayImageOptions options = new
                DisplayImageOptions.Builder()
                //.cacheInMemory(true)//设置下载的图片是否缓存在内存中, 重要，否则图片不会缓存到内存中
                //.cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中, 重要，否则图片不会缓存到硬盘中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new FadeInBitmapDisplayer(400))
                .build();
        return options;
    }

    /**
     * 加载图片
     */
    public void displayImage( String path,ImageView imageView,
                             ImageLoadingListener listener, DisplayImageOptions options) {
        if (mLoader != null) {
            mLoader.displayImage(path, imageView, options, listener);
        }
    }
    public void displayImage(ImageView imageView, String path, ImageLoadingListener listener) {

          displayImage(path, imageView,listener,null);
        }

    public void displayImage(ImageView imageView, String path) {
        displayImage(imageView, path, null);
    }
}
