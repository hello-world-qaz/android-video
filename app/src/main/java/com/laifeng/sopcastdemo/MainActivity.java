package com.laifeng.sopcastdemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.laifeng.sopcastdemo.permission.FloatWindowManager;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private static volatile FloatWindowManager instance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //添加悬浮窗动态权限申请
        instance = FloatWindowManager.getInstance();
        if(!instance.checkPermission(this)){
            instance.applyPermission(this);
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent it=new Intent(MyApplication.getAppContext(), VideoService.class);
                startService(it);
            }},10000);//延时10s执行
    }
}
