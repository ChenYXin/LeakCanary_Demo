package com.donkor.demo.leakcanary;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * handler造成的内存泄露
 * Created by donkor
 */
public class HandlerActivity extends Activity {

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            // do something you want
//        }
//    };

    /**
     * 正确的写法
     * 原因：
     * 在finish()的时候，该Message还没有被处理，Message持有Handler,
     * Handler持有Activity,这样会导致该Activity不会被回收，就发生了内存泄露.
     * 解决方法：
     *使用显形的引用，1.静态内部类。 2. 外部类
     *使用弱引用 2. WeakReference
     * 最后在Activity调用onDestroy()的时候要取消掉该Handler对象的Message和Runnable
     */
   private static class MyHandler extends Handler {
        private final  WeakReference<HandlerActivity> mActivityReference;

        public MyHandler(HandlerActivity activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HandlerActivity handlerAct = mActivityReference.get();
            if (handlerAct == null) {
                return;
            }
            // Do something  you want
        }
    }

    private MyHandler mHandler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler=new MyHandler(HandlerActivity.this);
        //just finish this activity
        finish();
    }

    @Override
    protected void onDestroy() {
        //  Remove all Runnable and Message.
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}


