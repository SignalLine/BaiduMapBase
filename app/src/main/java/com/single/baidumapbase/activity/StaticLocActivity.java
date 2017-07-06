package com.single.baidumapbase.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.single.baidumapbase.R;
import com.single.baidumapbase.app.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * 静态画运动轨迹，展示如何使用自定义图标绘制并响应点击事件
 */
public class StaticLocActivity extends AppCompatActivity {

    BitmapDescriptor mCurrentMarker;
    MapView mMapView;
    BaiduMap mBaiduMap;
    Polyline mPolyline;
    LatLng target;
    MapStatus.Builder builder;
    List<LatLng> latlngs = new ArrayList<>();

    BitmapDescriptor startBD = BitmapDescriptorFactory
            .fromResource(R.drawable.ic_me_history_startpoint);
    BitmapDescriptor finishBD = BitmapDescriptorFactory
            .fromResource(R.drawable.ic_me_history_finishpoint);

    private Marker mMarkerA;
    private Marker mMarkerB;
    private InfoWindow mInfoWindow;//点击展示的弹出框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_loc);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        coordinateConvert();

        builder = new MapStatus.Builder();
        builder.target(target).zoom(18f);//缩放比例
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        MarkerOptions oStart = new MarkerOptions();//地图标记覆盖物参数配置类
        oStart.position(latlngs.get(0));//覆盖物位置 第一个点位起始点
        oStart.icon(startBD);//设置覆盖物图
        oStart.zIndex(1);//设置覆盖物index
        mMarkerA = (Marker) mBaiduMap.addOverlay(oStart);//在地图上添加此图层

        //添加终点图层
        MarkerOptions oFinish = new MarkerOptions().position(latlngs.get(latlngs.size()-1)).icon(finishBD).zIndex(2);
        mMarkerB = (Marker) (mBaiduMap.addOverlay(oFinish));

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(marker.getZIndex() == mMarkerA.getZIndex()){
                    TextView textView = new TextView(getApplicationContext());
                    textView.setText("起点");
                    textView.setTextColor(Color.BLACK);
                    textView.setGravity(Gravity.CENTER);
                    textView.setBackgroundResource(R.drawable.popup);

                    //设置信息窗口点击回调
                    InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick() {
                            Toast.makeText(StaticLocActivity.this, "这是起点", Toast.LENGTH_SHORT).show();
                            mBaiduMap.hideInfoWindow();//隐藏信息窗口
                        }
                    };
                    /**
                     * 通过传入的 bitmap descriptor 构造一个 InfoWindow
                     * bd - 展示的bitmap
                     position - InfoWindow显示的位置点
                     yOffset - 信息窗口会与图层图标重叠，设置Y轴偏移量可以解决
                     listener - 点击监听者
                     */
                    LatLng latLng = marker.getPosition();//信息窗口显示的位置点
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(textView),latLng,-47,listener);
                    mBaiduMap.showInfoWindow(mInfoWindow);//显示信息窗口
                }else if(marker.getZIndex() == mMarkerB.getZIndex()){
                    Button button = new Button(getApplicationContext());
                    button.setText("终点");
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),"这里是终点", Toast.LENGTH_SHORT).show();
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    LatLng latLng = marker.getPosition();
                    /**
                     * 通过传入的 view 构造一个 InfoWindow, 此时只是利用该view生成一个Bitmap绘制在地图中，监听事件由自己实现。
                     view - 展示的 view
                     position - 显示的地理位置
                     yOffset - Y轴偏移量
                     */
                    mInfoWindow = new InfoWindow(button, latLng, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }

                return true;
            }
        });

        mBaiduMap.setOnPolylineClickListener(new BaiduMap.OnPolylineClickListener() {
            @Override
            public boolean onPolylineClick(Polyline polyline) {
                if(polyline.getZIndex() == mPolyline.getZIndex()){
                    Toast.makeText(StaticLocActivity.this, "点数:" + polyline.getPoints().size() + ",width:" + polyline.getWidth(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(latlngs);
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        mPolyline.setZIndex(3);
    }

    /**
     * 将google地图的wgs84坐标转化为百度地图坐标
     */
    private void coordinateConvert(){
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        double lanSum = 0;
        double lonSum = 0;
        for (int i = 0; i < Const.googleWGS84.length; i++) {
            String[] ll = Const.googleWGS84[i].split(",");
            LatLng sourceLatLng = new LatLng(Double.valueOf(ll[0]),Double.valueOf(ll[1]));
            converter.coord(sourceLatLng);
            LatLng desLatLng = converter.convert();
            latlngs.add(desLatLng);

            lanSum += desLatLng.latitude;
            lonSum += desLatLng.longitude;
        }

        target = new LatLng(lanSum/latlngs.size(),lonSum/latlngs.size());
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        // 为系统的方向传感器注册监听器
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.getMap().clear();
        mMapView.onDestroy();
        mMapView = null;
        startBD.recycle();
        finishBD.recycle();
        super.onDestroy();
    }
}
