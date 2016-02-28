package com.lvetianzhiyi.route.utils;

import android.graphics.Bitmap;

/**
 * 
 * 类 SendRouteImage 传送图片文件类
 * 
 * @author xinyi
 * 
 * @since 1.0.0
 */
public class SendRouteImage
{
	private Bitmap mBitmaps[];

	/**
	 * SendRouteImage 传送路径图片
	 * 
	 * @param Bitmap
	 *            [] pBitmaps 传送的图片数组
	 * @return void
	 * @since: V1.0
	 */
	public SendRouteImage( Bitmap[] pBitmaps )
	{
		mBitmaps = pBitmaps;
	}

	public Bitmap[] getBitmaps()
	{
		return mBitmaps;
	}

	public void setBitmaps( Bitmap[] mBitmaps )
	{
		this.mBitmaps = mBitmaps;
	}

};