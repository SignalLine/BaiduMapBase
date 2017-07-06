package com.single.baidumapbase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.single.baidumapbase.activity.DynamicLocActivity;
import com.single.baidumapbase.activity.MapLocActivity;
import com.single.baidumapbase.activity.MapSdkActivity;
import com.single.baidumapbase.activity.StaticLocActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_go1).setOnClickListener(this);
        findViewById(R.id.btn_go2).setOnClickListener(this);
        findViewById(R.id.btn_go3).setOnClickListener(this);
        findViewById(R.id.btn_go4).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go1://基础定位
                startActivity(new Intent(MainActivity.this,MapSdkActivity.class));
                break;
            case R.id.btn_go2://地图定位
                startActivity(new Intent(MainActivity.this,MapLocActivity.class));
                break;
            case R.id.btn_go3://整个运动轨迹
                startActivity(new Intent(MainActivity.this,StaticLocActivity.class));
                break;
            case R.id.btn_go4://实时运动轨迹
                startActivity(new Intent(MainActivity.this,DynamicLocActivity.class));
                break;
        }
    }
}
