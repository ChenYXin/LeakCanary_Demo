package com.donkor.demo.leakcanary;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

import java.lang.ref.WeakReference;

/**
 * Thread造成的内存泄露
 * Created by donkor
 */
public class ThreadActivity extends Activity {

    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        //两种常见线程写法造成的内存泄露
//        new MyAsyncTask().execute();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SystemClock.sleep(10000);
//            }
//        }).start();
        //正确的写法
        myAsyncTask = new MyAsyncTask(ThreadActivity.this);
        myAsyncTask.execute();
        new Thread(new MyRunnable()).start();
    }

    //造成内存溢出的写法
//    private class MyAsyncTask extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            SystemClock.sleep(10000);
//            return null;
//        }
//    }

    /**
     * 以上的异步任务和Runnable都是一个匿名内部类，因此它们对当前Activity都有一个隐式引用。
     * 如果Activity在销毁之前，任务还未完成， 那么将导致Activity的内存资源无法回收，造成内存泄漏。
     * 正确的做法还是使用静态内部类的方式
     * 最后在Activity销毁的时候，相对应的取消异步任务
     */
    private static class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Context> weakReference;

        public MyAsyncTask(Context context) {
            weakReference = new WeakReference<>(context);
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Void doInBackground(Void... params) {
            SystemClock.sleep(10000);
            return null;
        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity activity = (MainActivity) weakReference.get();
            if (activity != null) {
                //...
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            SystemClock.sleep(10000);
        }
    }

    @Override
    protected void onDestroy() {
        //判断异步任务是否存在
        if (myAsyncTask != null && myAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            myAsyncTask.cancel(true);
        }
        super.onDestroy();
    }
}