package com.lvetianzhiyi.route.utils;

/**
 * 
 * 类 Screen_info 用户的屏幕信息类
 * 
 * @author xinyi
 * 
 * @since 1.0.0
 */
public class Screen_info
{
	static private int Screen_Width;
	static private int Screen_Height;

	/**
	 * 
	 * 类 Screen_info 用户的屏幕信息类构造函数
	 * 
	 * @author xinyi
	 * 
	 * @param int screen_Width 屏幕宽
	 * @param int screen_Height 屏幕高
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	public Screen_info( int screen_Width, int screen_Height )
	{
		Screen_Width = screen_Width;
		Screen_Height = screen_Height;
	}

	/**
	 * 获取屏幕的宽
	 * 
	 * @param void
	 * @return int 屏幕宽
	 * @since 1.0.0
	 */
	public static int getScreen_Width()
	{
		return Screen_Width;
	}

	/**
	 * 获取屏幕的高
	 * 
	 * @param void
	 * @return int 屏幕高
	 * @since 1.0.0
	 */
	public static int getScreen_Height()
	{
		return Screen_Height;
	}

	/**
	 * 获取初始化时地图层距屏幕顶端的差
	 * 
	 * @param void
	 * @return float 初始时一、二层图层距屏幕顶端的差
	 * @since 1.0.0
	 */
	public static float getInitTop()
	{
		return ( Screen_Height - Screen_Width ) / 2.0f;
	}

	/**
	 * 获取初始化时地图层距屏幕顶端的差
	 * 
	 * @param void
	 * @return float 初始时地下层图层距屏幕顶端的差
	 * @since 1.0.0
	 */
	public static float getInitTop2()
	{
		return ( Screen_Height - 375.0f * CaluculateInitInch() ) / 2.0f;
	}
	
	/**
	 * 获取初始化时地图图层缩放的初始尺寸
	 * 
	 * @param void
	 * @return float 初始尺寸
	 * @since 1.0.0
	 */
	public static float CaluculateInitInch()
	{//0.72
		return ( Screen_Width > Screen_Height ? Screen_Height : Screen_Width ) / 1000.0f;
	}
};