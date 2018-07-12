package com.laifeng.sopcastdemo;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.laifeng.sopcastsdk.configuration.AudioConfiguration;
import com.laifeng.sopcastsdk.configuration.VideoConfiguration;
import com.laifeng.sopcastsdk.screen.ScreenRecordActivity;
import com.laifeng.sopcastsdk.stream.packer.rtmp.RtmpPacker;
import com.laifeng.sopcastsdk.stream.sender.rtmp.RtmpSender;
import com.laifeng.sopcastsdk.utils.SopCastLog;

import static com.laifeng.sopcastsdk.constant.SopCastConstant.TAG;

public class ScreenActivity extends ScreenRecordActivity {
    private Button mStartBtn;
    private Button mStopBtn;
    private RtmpSender mRtmpSender;
    private VideoConfiguration mVideoConfiguration;
    private int mCurrentBps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        initSender();
        initViews();
    }

    private void initViews() {
        mStartBtn = (Button) findViewById(R.id.startRecord);
        mStopBtn = (Button) findViewById(R.id.stopRecord);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uploadUrl="rtmp://10.0.2.2:1935/live/mylive";
                mRtmpSender.setAddress(uploadUrl);
                requestRecording();
            }
        });
        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });
    }

    private void initSender() {
        mRtmpSender = new RtmpSender();
        mRtmpSender.setVideoParams(640, 360);
        mRtmpSender.setAudioParams(AudioConfiguration.DEFAULT_FREQUENCY, 16, false);
        mRtmpSender.setSenderListener(mSenderListener);
    }

    @Override
    protected void requestRecordSuccess() {
        super.requestRecordSuccess();
        initRecorder();
        connectServer();
    }

    private void initRecorder() {
        RtmpPacker packer = new RtmpPacker();
        packer.initAudioParams(AudioConfiguration.DEFAULT_FREQUENCY, 16, false);
        mVideoConfiguration = new VideoConfiguration.Builder().setSize(640, 360).build();
        setVideoConfiguration(mVideoConfiguration);
        setRecordPacker(packer);
        setRecordSender(mRtmpSender);
    }

    private void connectServer() {
        Toast.makeText(ScreenActivity.this, "start to connect server", Toast.LENGTH_SHORT).show();
        mRtmpSender.connect();
    }

    private RtmpSender.OnSenderListener mSenderListener = new RtmpSender.OnSenderListener() {
        @Override
        public void onConnecting() {

        }

        @Override
        public void onConnected() {
            Toast.makeText(ScreenActivity.this, "start to upload data", Toast.LENGTH_SHORT).show();
            startRecording();
            mCurrentBps = mVideoConfiguration.maxBps;
        }

        @Override
        public void onDisConnected() {
            Toast.makeText(ScreenActivity.this, "Disconnect", Toast.LENGTH_SHORT).show();
            stopRecording();
        }

        @Override
        public void onPublishFail() {
            Toast.makeText(ScreenActivity.this, "Fail to publish the stream", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNetGood() {
            if (mCurrentBps + 50 <= mVideoConfiguration.maxBps){
                SopCastLog.d(TAG, "BPS_CHANGE good up 50");
                int bps = mCurrentBps + 50;
                boolean result = setRecordBps(bps);
                if(result) {
                    mCurrentBps = bps;
                }
            } else {
                SopCastLog.d(TAG, "BPS_CHANGE good good good");
            }
            SopCastLog.d(TAG, "Current Bps: " + mCurrentBps);
        }

        @Override
        public void onNetBad() {
            if (mCurrentBps - 100 >= mVideoConfiguration.minBps){
                SopCastLog.d(TAG, "BPS_CHANGE bad down 100");
                int bps = mCurrentBps - 100;
                boolean result = setRecordBps(bps);
                if(result) {
                    mCurrentBps = bps;
                }
            } else {
                SopCastLog.d(TAG, "BPS_CHANGE bad down 100");
            }
            SopCastLog.d(TAG, "Current Bps: " + mCurrentBps);
        }
    };
}
