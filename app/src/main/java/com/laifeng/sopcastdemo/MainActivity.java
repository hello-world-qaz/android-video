package com.laifeng.sopcastdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.laifeng.sopcastdemo.permission.FloatWindowManager;


public class MainActivity extends AppCompatActivity {
    private static volatile FloatWindowManager instance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent it=new Intent(this, VideoService.class);
        startService(it);
    }
}
