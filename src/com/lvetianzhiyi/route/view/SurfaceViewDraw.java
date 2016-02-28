package com.lvetianzhiyi.route.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lvetianzhiyi.route.R;
import com.lvetianzhiyi.route.algorithm.PixelPoint;
import com.lvetianzhiyi.route.utils.Screen_info;

/**
 * 
 * 类 SurfaceViewDraw 画路线视图
 * 
 * @author xinyi
 * 
 * @继承 ImageView
 * 
 * @since 1.0.0
 */
public class SurfaceViewDraw extends ImageView
{
	/*
	 * /////////////////////////////////// 成员变量定义  \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	 */
	// 变换矩阵
	private Matrix mMatrix;
	// 是否绘制标识
	private boolean ifDraw;
	// 当前像素点
	private PixelPoint mPixelPoint;
	// 当前图片
	private Bitmap mCurrentBitmap;
	// 上一张图片
	private Bitmap mLastBitmap;
	// 标注图片
	private Bitmap Biao;
	// 是否已存在标注标识
	private boolean isHasBiao;

	/*
	 * /////////////////////////////////// 方法定义
	 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	 */
	/**
	 * SurfaceViewDraw 画路线视图
	 * 
	 * @param Context
	 *            context 当前上下文
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	public SurfaceViewDraw( Context context )
	{
		this( context, null );
	}

	/**
	 * LveTianMapView 画路线视图
	 * 
	 * @param Context
	 *            context 当前上下文
	 * 
	 * @param AttributeSet
	 *            attrs 属性
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	public SurfaceViewDraw( Context context, AttributeSet attrs )
	{
		this( context, attrs, 0 );
	}

	/**
	 * LveTianMapView 画路线视图
	 * 
	 * @param Context
	 *            context 当前上下文
	 * 
	 * @param AttributeSet
	 *            attrs 属性
	 * 
	 * @param int defStyle 默认风格
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	@ SuppressLint( "ClickableViewAccessibility" )
	public SurfaceViewDraw( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
		ifDraw = false;
		isHasBiao = false;
		mMatrix = new Matrix();
		setScaleType( ScaleType.MATRIX );
		Biao = BitmapFactory.decodeResource( getResources(), R.drawable.go_here );
	}

	@ Override
	public void setImageBitmap( Bitmap bm )
	{
		super.setImageBitmap( bm.copy( Config.ARGB_8888, true ) );
		setImageMatrix( mMatrix );
		mCurrentBitmap = bm.copy( Config.ARGB_8888, true );
	}

	/**
	 * My_onDraw 绘制标注点
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	public void My_onDraw()
	{
		if ( ifDraw )
		{
			if ( isHasBiao )
			{
				mCurrentBitmap = mLastBitmap.copy( Config.ARGB_8888, true );
				Canvas canvas = new Canvas( mCurrentBitmap );
				if ( mPixelPoint.getZ() + 1 == 0 )
				{
					canvas.drawBitmap(
							Biao,
							mPixelPoint.getX() - Biao.getWidth() / 2.0f,
							mPixelPoint.getY() - Biao.getHeight()
									- Screen_info.getInitTop() + 50
									* Screen_info.CaluculateInitInch() / 0.72f,
							null );
				}
				else
				{
					canvas.drawBitmap( Biao,
							mPixelPoint.getX() - Biao.getWidth() / 2.0f,
							mPixelPoint.getY() - Biao.getHeight(), null );
				}
				canvas.save( Canvas.ALL_SAVE_FLAG );// 保存
			}
			else
			{
				mLastBitmap = mCurrentBitmap.copy( Config.ARGB_8888, true );
				Canvas canvas = new Canvas( mCurrentBitmap );
				if ( mPixelPoint.getZ() + 1 == 0 )
				{
					canvas.drawBitmap(
							Biao,
							mPixelPoint.getX() - Biao.getWidth() / 2.0f,
							mPixelPoint.getY() - Biao.getHeight()
									- Screen_info.getInitTop() + 50
									* Screen_info.CaluculateInitInch() / 0.72f,
							null );
				}
				else
				{
					canvas.drawBitmap( Biao,
							mPixelPoint.getX() - Biao.getWidth() / 2.0f,
							mPixelPoint.getY() - Biao.getHeight(), null );
				}
				canvas.save( Canvas.ALL_SAVE_FLAG );// 保存
				isHasBiao = true;
			}

			invalidate();
			setImageBitmap( mCurrentBitmap );
			ifDraw = false;
		}
	}

	/**
	 * setTouchPos
	 * 
	 * @param
	 * @return 规划后的路径 Path
	 * @exception
	 * @since 1.0.0
	 */
	public void setTouchPos( PixelPoint pixelPoint )
	{
		mPixelPoint = pixelPoint;
	}

	public void clearRoute()
	{
		setImageBitmap( Bitmap.createBitmap( Screen_info.getScreen_Width(),
				Screen_info.getScreen_Height(), Config.ARGB_8888 ) );
		isHasBiao = false;
	}

	/**
	 * setIfDraw 设置是否绘制自定义点的标识
	 * 
	 * @param boolean 是否绘制自定义点
	 * @return 规划后的路径 Path
	 * @since 1.0.0
	 */
	public void setIfDraw( boolean ifDraw )
	{
		this.ifDraw = ifDraw;
	}

	public void setHasBiao( boolean isHasBiao )
	{
		this.isHasBiao = isHasBiao;
	}

	public void getImage_Matrix()
	{
		mMatrix.set( getImageMatrix() );
	}

	public void POST_scale( float sx, float sy, float px, float py )
	{
		mMatrix.postScale( sx, sy, px, py );
		setImageMatrix( mMatrix );
	}

	public void POST_Translate( float dx, float dy )
	{
		mMatrix.postTranslate( dx, dy );
		setImageMatrix( mMatrix );
	}

};