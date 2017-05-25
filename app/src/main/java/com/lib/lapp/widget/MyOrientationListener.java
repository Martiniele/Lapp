package com.lib.lapp.widget;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 *  @author wxx
 *  @Date 2017/4/19.
 *  @Description 终端方向与定位图标方向匹配操作。基于加速度传感器和磁场传感器计算出方向的改变值，设计回调接口为定位导航提供支持
 */
public class MyOrientationListener implements SensorEventListener{

    private OnOrientationListener mOnOrientationListener;
    private SensorManager mSensorManager;                //传感器管理器
    private Context context;                             //系统上下文
    private Sensor accelerometer;                        //加速度传感器
    private Sensor magnetic;                             //地磁传感器
    private float[] accelerometerValues = new float[3];  //存储获取的加速度传感器的值
    private float[] magneticFieldValues = new float[3];  //存储获取的地磁传感器的值
    private float[] orientationValues = new float[3];    //存储计算出来的方向值
    private float[] R = new float[9];                    //旋转矩阵，用于计算方向的算法，存储磁场和加速度的数据，用于计算方向

    private float lastX;                                 //最终返回值 用于确定定位图标的方向的值

    public MyOrientationListener(Context context){
        this.context = context;
    }

    public void setmOnOrientationListener(OnOrientationListener mOnOrientationListener) {
        this.mOnOrientationListener = mOnOrientationListener;
    }

    /**
     * 开启监听
     */
    public void start(){
        //获得系统服务
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager != null){
            //获得加速度传感器
            accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            //获得磁场传感器
            magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
        if(accelerometer != null){
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI); //注册并设置获取传感器获取数据的频率
        }
        if(magnetic != null){
            mSensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_UI); //注册并设置磁场传感器获取数据的频率
        }
    }

    /**
     * 结束监听
     */
    public void stop(){
        mSensorManager.unregisterListener(this);    //停止定位
    }

    /**
     * 计算方向向量的值
     */
    public void calculateOrientation(){
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, orientationValues);
    }

    /**
     * 纬度发生改变时
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerometerValues = event.values;
        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magneticFieldValues = event.values;
        }
        calculateOrientation();         //计算方向向量的值，计算结果保存在orientationValues数组中
        float x = orientationValues[0];
        //X方向变化大于1度时执行
        if(Math.abs(x - lastX) > 1.0){
            //通知主界面进行回调
            if(mOnOrientationListener != null){
                mOnOrientationListener.onOrientationChanged(x);
            }
        }
        lastX = x;
    }

    /**
     * 经度发生改变时
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 回调接口
     */
    public interface OnOrientationListener{
        void onOrientationChanged(float x);
    }
}
