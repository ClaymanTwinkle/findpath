package com.lvetianzhiyi.route.utils;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * 类 FixImageToFitScreen 修正图像至屏幕中央
 * 
 * @author xinyi
 * 
 * @since 1.0.0
 */
public class FixImageToFitScreen
{
	// 图片文件
	private Bitmap mBitmap;

	/**
	 * 类 FixImageToFitScreen 修正图像至屏幕中央
	 * 
	 * @author xinyi
	 * 
	 * @param Bitmap
	 *            pBitmap 图片文件
	 */
	public FixImageToFitScreen( Bitmap pBitmap )
	{
		mBitmap = pBitmap;
		FitImageToFitScreen();
	}

	/**
	 * getBitmap 获取图片
	 * 
	 * @author xinyi
	 * @param void
	 * @return Bitmap 图片文件
	 * @version: V1.0
	 */
	public Bitmap getmBitmap()
	{
		return mBitmap;
	}

	/**
	 * setmBitmap 设置图片
	 * 
	 * @author xinyi
	 * @param Bitmap
	 *            图片文件
	 * @return void
	 * @version: V1.0
	 */
	public void setmBitmap( Bitmap pBitmap )
	{
		this.mBitmap = pBitmap;
		FitImageToFitScreen();
	}

	/**
	 * FitImageToFitScreen 令图片适应屏幕
	 * 
	 * @author xinyi
	 * @param void
	 * @return void
	 * @version: V1.0
	 */
	private void FitImageToFitScreen()
	{
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();

		float scale = 1.0f;

		if ( width > Screen_info.getScreen_Width()
				&& height < Screen_info.getScreen_Height() )
		{
			scale = Screen_info.getScreen_Width() * 1.0f / width;
		}
		else if ( width < Screen_info.getScreen_Width()
				&& height > Screen_info.getScreen_Height() )
		{
			scale = Screen_info.getScreen_Height() * 1.0f / height;
		}
		else if ( ( width < Screen_info.getScreen_Width() && height < Screen_info
				.getScreen_Height() )
				|| ( width > Screen_info.getScreen_Width() && height > Screen_info
						.getScreen_Height() ) )
		{
			scale = Math.min( Screen_info.getScreen_Width() * 1.0f / width,
					Screen_info.getScreen_Height() * 1.0f / height );
		}

		Matrix matrix = new Matrix();
		matrix.postScale( scale * 2 / 3, scale * 2 / 3 );
		Bitmap bmp = Bitmap.createBitmap( mBitmap, 0, 0, width, height, matrix,
				true );
		mBitmap = skewImage( bmp );
		bmp.recycle();
	}

	/**
	 * skewImage 图片错切变换
	 * 
	 * @author xinyi
	 * @param Bitmap
	 *            paramBitmap 待错切变换的图片
	 * @return Bitmap 错切变换后的图片
	 * @version: V1.0
	 */
	public Bitmap skewImage( Bitmap paramBitmap )
	{
		Bitmap localBitmap1 = paramBitmap;
		Camera localCamera = new Camera();
		localCamera.save();
		Matrix localMatrix = new Matrix();
		localCamera.rotateX( 50.0f );
		localCamera.rotateY( 10.0f );
		localCamera.rotateZ( -30.0f );
		localCamera.getMatrix( localMatrix );
		localCamera.restore();
		localMatrix.preTranslate( -localBitmap1.getWidth() >> 1,
				-localBitmap1.getHeight() >> 1 );
		Bitmap localBitmap2 = Bitmap.createBitmap( localBitmap1, 0, 0,
				localBitmap1.getWidth(), localBitmap1.getHeight(), localMatrix,
				true );
		localBitmap1.recycle();
		Bitmap localBitmap3 = Bitmap.createBitmap( localBitmap2.getWidth(),
				localBitmap2.getHeight(), Bitmap.Config.ARGB_8888 );
		Canvas localCanvas = new Canvas( localBitmap3 );
		Paint localPaint = new Paint();
		localPaint.setAntiAlias( true );
		localPaint.setFilterBitmap( true );
		localCanvas.drawBitmap( localBitmap2, 0.0F, 0.0F, localPaint );
		localBitmap2.recycle();
		return localBitmap3;
	}
};