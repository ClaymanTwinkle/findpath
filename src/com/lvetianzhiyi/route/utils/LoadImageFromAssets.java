package com.lvetianzhiyi.route.utils;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 类 LoadImageFromAssets(从Assets包中读入地图图片文件)
 * 
 * @author xinyi
 * 
 * @since 1.0.0
 */
public class LoadImageFromAssets
{
	// 图片
	private Bitmap mBitmap;
	// 当前的 Activity
	private Activity mActivity;

	/**
	 * 类 LoadImageFromAssets构造函数(从Assets包中读入地图图片文件)
	 * 
	 * @author xinyi
	 * 
	 * @param Activity
	 *            pActivity 当前的Activity
	 * 
	 * @param String
	 *            pPicPath 图片名称
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	public LoadImageFromAssets( Activity pActivity, String pPicPath )
	{
		mActivity = pActivity;
		mBitmap = getImageFromAssetsFile( pPicPath );
	}

	/**
	 * getImageFromAssetsFile 从Assets包中读入地图图片文件
	 * 
	 * @param fileName
	 *            文件名
	 * 
	 * @return image 获取到的图片文件
	 */
	private Bitmap getImageFromAssetsFile( String fileName )
	{
		Bitmap image = null;
		AssetManager am = mActivity.getResources().getAssets();
		try
		{
			InputStream is = am.open( fileName );
			image = BitmapFactory.decodeStream( is );
			is.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * getBitmap 获取读入的地图图片文件
	 * 
	 * @param void
	 * 
	 * @return image 获取到的图片文件
	 */
	public Bitmap getBitmap()
	{
		return mBitmap == null ? null : mBitmap;
	}

	/**
	 * getImageWidth 获取地图图片文件的宽
	 * 
	 * @param void
	 * 
	 * @return int 宽
	 */
	public int getImageWidth()
	{
		return mBitmap == null ? 0 : mBitmap.getWidth();
	}

	/**
	 * getImageHeight 获取地图图片文件的高
	 * 
	 * @param void
	 * 
	 * @return int 高
	 */
	public int getImageHeight()
	{
		return mBitmap == null ? 0 : mBitmap.getHeight();
	}
};