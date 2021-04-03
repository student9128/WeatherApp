package com.kevin.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.kevin.playandroid.util.LogUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnStartLocation.setOnClickListener {
            var mLocationListener = object : AMapLocationListener {
                override fun onLocationChanged(location: AMapLocation?) {
                    val toString = location.toString()
                    LogUtils.printD("MainActivity", toString)
                    val address = location?.address
                    tvResult.text = address

                }

            }
            var mLocationClient = AMapLocationClient(applicationContext)
            mLocationClient.setLocationListener(mLocationListener)
            var mLocationOption = AMapLocationClientOption()
//        mLocationOption.setLocationPurpose()
//        选择定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
//        设置单次定位
            mLocationOption.setOnceLocation(true)
//获取最近3s内精度最高的一次定位结果：
            mLocationOption.setOnceLocationLatest(true)
            mLocationClient.setLocationOption(mLocationOption)
            mLocationClient.startLocation()
        }
    }
}