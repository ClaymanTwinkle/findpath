package com.lvetianzhiyi.route.utils;

import android.content.Context;

/**
 * 展开项目工具Popup_Util
 * 
 * @author xinyi
 * @version: V1.0
 */
public class Popup_Util
{
	/**
	 * getScreenDensity 获取屏幕信息
	 * 
	 * @param Context
	 *            context 当前上下文
	 * @return float 尺寸信息
	 * @since: V1.0
	 */
	public static float getScreenDensity( Context context )
	{
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * dip2px 获取像素位置
	 * 
	 * @param Context
	 *            context 当前上下文
	 * @param float px 点击像素位置
	 * @return int 尺寸信息
	 * @since: V1.0
	 */
	public static int dip2px( Context context, float px )
	{
		final float scale = getScreenDensity( context );
		int value = (int) ( px * scale + 0.5 );
		return value;
	}
};