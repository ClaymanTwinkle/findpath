package com.lvetianzhiyi.route.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

import com.lvetianzhiyi.route.MainActivity;
import com.lvetianzhiyi.route.rotateanimation.ArrowRotatedAnimation;
import com.lvetianzhiyi.route.view.IndicateArrow;
import com.lvetianzhiyi.route.view.LveTianMapView;

/**
 * 类 SensorListener 传感器监听类
 * 
 * @author xinyi
 * 
 * @since 1.0.0
 */
public class SensorListener
{
	// 地图图层的视图
	private LveTianMapView mLveTianMapView;
	// 用户位置坐标的视图
	private IndicateArrow mIndicateArrow;
	
	/**
	 * 类 SensorListener 传感器监听类
	 * 
	 * @author xinyi
	 * 
	 * @since 1.0.0
	 */
	public SensorListener( LveTianMapView pLveTianMapView,
			IndicateArrow pIndicateArrow )
	{
		mLveTianMapView = pLveTianMapView;
		mIndicateArrow = pIndicateArrow;
	}

	public SensorEventListener getListener()
	{
		return listener;
	}

	private SensorEventListener listener = new SensorEventListener()
	{
		float[] accelerometerValues = new float[3];

		float[] magneticValues = new float[3];

		private float lastRotateDegree;

		@ Override
		public void onSensorChanged( SensorEvent event )
		{
			// 判断当前是加速度传感器还是地磁传感器
			if ( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER )
			{
				// 调用clone()方法获取
				accelerometerValues = event.values.clone();
			}
			else if ( event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD )
			{
				// 调用clone()方法获取
				magneticValues = event.values.clone();
			}
			float[] values = new float[3];
			float[] R = new float[9];
			SensorManager.getRotationMatrix( R, null, accelerometerValues,
					magneticValues );
			SensorManager.getOrientation( R, values );
			// 获得旋转角度
			float rotateDegree = -(float) Math.toDegrees( values[0] );
			// 若新旋转的角度与上次之差大于  2  度则进行旋转动画
			if ( Math.abs( rotateDegree - lastRotateDegree ) > 2.0f )
			{
				float centerX = ( mIndicateArrow.getImageRightPos() - mIndicateArrow
						.getImageLeftPos() )
						/ 2
						+ mIndicateArrow.getImageLeftPos();
				float centerY = ( mIndicateArrow.getImageBottomPos() - mIndicateArrow
						.getImageTopPos() )
						/ 2
						+ mIndicateArrow.getImageTopPos();

				ArrowRotatedAnimation my3dAnimation = new ArrowRotatedAnimation(
						lastRotateDegree, rotateDegree, centerX, centerY );
				my3dAnimation.setFillAfter( true );
				
				// 若当前地图层和人物所在层不在同一层,则隐藏用户位置光标
				if ( MainActivity.mArrowLayer == mLveTianMapView
						.getmCurrentFloor() )
				{
					mIndicateArrow.startAnimation( my3dAnimation );
				}
				else
				{
					mIndicateArrow.clearAnimation();
					mIndicateArrow.setVisibility( View.INVISIBLE );
				}

				lastRotateDegree = rotateDegree;
			}
		}

		@ Override
		public void onAccuracyChanged( Sensor sensor, int accuracy )
		{
		}
	};
};