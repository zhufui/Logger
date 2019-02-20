package com.example.logger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lib_logger.CsvFormatStrategy;
import com.lib_logger.DiskLogAdapter;
import com.lib_logger.Logger;

import cn.com.zf.library.PermissionCallback;
import cn.com.zf.library.PermissionGroup;
import cn.com.zf.library.ZPermissions;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, PermissionCallback {
    Button bt1;
    Button bt2;
    Button bt3;
    Button bt4;
    Button bt5;
    Button bt6;

    private Button mBtMap;
    private Button mBtSet;
    private Button mBtList;
    private Button mBtArray;
    private Button mBtJson;
    private Button mBtXml;
    private Button mBtDisk;
    private Button mBtOpenSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPer();
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        bt5 = findViewById(R.id.bt5);
        bt6 = findViewById(R.id.bt6);

        mBtMap = findViewById(R.id.btMap);
        mBtSet = findViewById(R.id.btSet);
        mBtList = findViewById(R.id.btList);
        mBtArray = findViewById(R.id.btArray);
        mBtJson = findViewById(R.id.btJson);
        mBtXml = findViewById(R.id.btXml);
        mBtDisk = findViewById(R.id.btDisk);
        mBtOpenSetting = findViewById(R.id.btOpenSetting);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
        mBtMap.setOnClickListener(this);
        mBtSet.setOnClickListener(this);
        mBtList.setOnClickListener(this);
        mBtArray.setOnClickListener(this);
        mBtJson.setOnClickListener(this);
        mBtXml.setOnClickListener(this);
        mBtDisk.setOnClickListener(this);
        mBtOpenSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt1:
                Logger.v("打印VERBOSE日志");
                break;
            case R.id.bt2:
                Logger.d("打印DEBUG日志");
                break;
            case R.id.bt3:
                Logger.d("打印INFO日志");
                break;
            case R.id.bt4:
                Logger.d("打印WARN日志");
                break;
            case R.id.bt5:
                Logger.d("打印ERROR日志");
                break;
            case R.id.bt6:
                Logger.d("打印ASSERT日志");
                break;
            case R.id.btMap:
                Logger.d(DataSupport.getMap());
                break;
            case R.id.btSet:
                Logger.d(DataSupport.getSet());
                break;
            case R.id.btList:
                Logger.d(DataSupport.getList());
                break;
            case R.id.btArray:
                Logger.d(DataSupport.getArray());
                break;
            case R.id.btJson:
                Logger.d(DataSupport.getJson());
                break;
            case R.id.btXml:
                Logger.d(DataSupport.getXml());
                break;
            case R.id.btDisk:
                CsvFormatStrategy fs = CsvFormatStrategy.newBuilder()
                        .tag("DisLogTag")
                        .build();
                Logger.addLogAdapter(new DiskLogAdapter(fs));
                break;
            case R.id.btOpenSetting:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.settingAct");
                startActivity(intent);
                break;
        }
    }

    private void requestPer() {
        ZPermissions.requestPermissions(this, 0,
                this, PermissionGroup.build(
                        PermissionGroup.SD()
                ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZPermissions.recycleCallback();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ZPermissions.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void permissionGrant(int requestCode) {

    }

    @Override
    public void permissionDenied(int requestCode) {

    }
}
