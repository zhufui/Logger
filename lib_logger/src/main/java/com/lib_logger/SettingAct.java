package com.lib_logger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * 日志设置页面
 */
public class SettingAct extends AppCompatActivity implements View.OnClickListener {
    private Button mBtOpen;
    private Button mBtClose;
    private Button mBtOpenDisk;
    private Button mBtCloseDisk;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting);
        mContext = this;
        mBtOpen = findViewById(R.id.btOpen);
        mBtClose = findViewById(R.id.btClose);
        mBtOpenDisk = findViewById(R.id.btOpenDisk);
        mBtCloseDisk = findViewById(R.id.btCloseDisk);
        mBtOpen.setOnClickListener(this);
        mBtClose.setOnClickListener(this);
        mBtOpenDisk.setOnClickListener(this);
        mBtCloseDisk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btOpen) {
            Logger.addLogAdapter(new AndroidLogAdapter());
            LogSwitch.setClog(mContext, true);
            return;
        }

        if (v.getId() == R.id.btClose) {
            Logger.removeLogAdapter(AndroidLogAdapter.class.getSimpleName());
            LogSwitch.setClog(mContext, false);
            return;
        }

        if (v.getId() == R.id.btOpenDisk) {
            Logger.addLogAdapter(new DiskLogAdapter());
            LogSwitch.setDlog(mContext, true);
            return;
        }

        if (v.getId() == R.id.btCloseDisk) {
            Logger.removeLogAdapter(DiskLogAdapter.class.getSimpleName());
            LogSwitch.setDlog(mContext, false);
        }
    }

}
