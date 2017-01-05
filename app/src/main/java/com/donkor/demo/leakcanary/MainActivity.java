package com.donkor.demo.leakcanary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 *  @author donkor
 * blog：http://blog.csdn.net/donkor_
 * 个人微信公众号：donkor
 * 有问题可直接在公众号，博客上留言
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnInstance,btnHandler,btnThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnThread = (Button) findViewById(R.id.btnThread);
        btnInstance = (Button) findViewById(R.id.btnInstance);
        btnHandler = (Button) findViewById(R.id.btnHandler);
        btnInstance.setOnClickListener(this);
        btnHandler.setOnClickListener(this);
        btnThread.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnInstance:
                intent = new Intent(MainActivity.this, InstanceActivity.class);
                startActivity(intent);
                break;
            case  R.id.btnHandler:
                intent = new Intent(MainActivity.this, HandlerActivity.class);
                startActivity(intent);
                break;
            case R.id.btnThread:
                intent = new Intent(MainActivity.this, ThreadActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
