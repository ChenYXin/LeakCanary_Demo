package com.donkor.demo.leakcanary;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by donkor
 */
public class InstanceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance);

        InsUtil.getInsUtil(InstanceActivity.this);
    }
}
