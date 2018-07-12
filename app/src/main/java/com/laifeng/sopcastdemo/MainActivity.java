package com.laifeng.sopcastdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.laifeng.sopcastdemo.permission.FloatWindowManager;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private volatile FloatWindowManager instance;
    private Button mButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button)findViewById(R.id.btn_click);
        mButton.setOnClickListener(this);
        //添加悬浮窗动态权限申请
        instance = FloatWindowManager.getInstance();
        if(!instance.checkPermission(this)){
            instance.applyPermission(this);
        }


        //录屏
        /*
        Intent intent = new Intent(this, ScreenActivity.class);
        startActivity(intent);
        */

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent it=new Intent(MyApplication.getAppContext(), VideoService.class);
                startService(it);
            }},10000);//延时10s执行


    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, VideoService.class);
        intent.putExtra("command","switchcamera");
        startService(intent);
    }

}
