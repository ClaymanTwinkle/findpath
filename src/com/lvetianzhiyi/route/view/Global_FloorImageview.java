package com.lvetianzhiyi.route.view;

import com.lvetianzhiyi.route.utils.Screen_info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 
 * 类 Global_FloorImageview 全局视图
 * 
 * @author xinyi
 * 
 * @继承 ImageView
 * 
 * @since 1.0.0
 */
public class Global_FloorImageview extends ImageView
{
	// 变换矩阵
	private Matrix matrix;
	// 视图图片
	private Bitmap mBitmap;

	/**
	 * Global_FloorImageview 全局视图构造函数
	 * 
	 * @param Context
	 *            context 当前上下文
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	public Global_FloorImageview( Context context )
	{
		this( context, null, 0 );
	}

	/**
	 * Global_FloorImageview 全局视图构造函数
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
	public Global_FloorImageview( Context context, AttributeSet attrs )
	{
		this( context, attrs, 0 );
	}

	/**
	 * Global_FloorImageview 全局视图构造函数
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
	public Global_FloorImageview( Context context, AttributeSet attrs,
			int defStyle )
	{
		super( context, attrs, defStyle );
		setScaleType( ScaleType.MATRIX );// 设置矩阵变换模式
	}

	@ Override
	public void setImageBitmap( Bitmap bm )
	{
		super.setImageBitmap( bm );
		mBitmap = bm;
		FitToCenter();
	}
	
	/**
	 * FitToCenter 将图片适应屏幕至中间
	 * 
	 * @param void
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	private void FitToCenter()
	{
		int width = Screen_info.getScreen_Width();
		int dw = mBitmap.getWidth();
		int dx = width / 2 - dw / 2;

		matrix = getImageMatrix();
		matrix.postTranslate( dx, 0 );
		setImageMatrix( matrix );
	}

}
