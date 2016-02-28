package com.lvetianzhiyi.route.rotateanimation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 旋转类 ArrowRotatedAnimation
 * 
 * @author xinyi
 * @继承 Animation
 * @version: V1.0
 */
public class ArrowRotatedAnimation extends Animation
{
	private float mFromDegrees;
	private float mToDegrees;
	private float mCenterX;
	private float mCenterY;
	private Camera mCamera;

	/**
	 * ArrowRotatedAnimation 旋转类构造函数
	 * 
	 * @param float fromDegrees 初始角度
	 * @param float toDegrees 结束角度
	 * @param float centerX 旋转中心横坐标
	 * @param float centerY 旋转中心纵坐标
	 * @since: V1.0
	 */
	public ArrowRotatedAnimation( float fromDegrees, float toDegrees,
			float centerX, float centerY )
	{
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
		mCenterX = centerX;
		mCenterY = centerY;
	}

	@ Override
	public void initialize( int width, int height, int parentWidth,
			int parentHeight )
	{
		super.initialize( width, height, parentWidth, parentHeight );
		mCamera = new Camera();
	}

	/**
	 * @author xinyi
	 * @param float fromDegrees 初始角度
	 * @param float toDegrees 结束角度
	 * @param float centerX 旋转中心横坐标
	 * @param float centerY 旋转中心纵坐标
	 * @version: V1.0
	 */
	@ Override
	protected void applyTransformation( float interpolatedTime, Transformation t )
	{
		Camera camera = mCamera;
		Matrix matrix = t.getMatrix();
		float RotateDegree = mFromDegrees + ( mToDegrees - mFromDegrees )
				* interpolatedTime;
		camera.save();
		camera.rotateZ( RotateDegree );
		camera.getMatrix( matrix );
		camera.restore();
		matrix.preTranslate( -mCenterX, -mCenterY );
		matrix.postTranslate( mCenterX, mCenterY );
	}
}