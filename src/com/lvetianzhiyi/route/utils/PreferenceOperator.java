package com.lvetianzhiyi.route.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 用于记录用户数据到本地
 * 
 * @author Liang
 * 
 */
public class PreferenceOperator
{

	/**
	 * 存入自定义的标识的数据，可以近似的看作网络下载数据的缓存
	 * 
	 * 单条方式存入
	 * 
	 * @param context
	 *            使用的上下文
	 * @param tag
	 *            存入内容的标记，约定俗成的tag用当前的类名命名来区分不同的
	 * @param content
	 *            存入的内容
	 */
	public static void putContent( Context context, String tag, String content )
	{
		SharedPreferences sp = context.getSharedPreferences( tag,
				Context.MODE_PRIVATE );
		Editor editor = sp.edit();
		editor.putString( tag, content );
		editor.commit();
	}

	/**
	 * 获取以tag命名的存储内容
	 * 
	 * @param context
	 *            当前调用的上下文
	 * @param tag
	 *            命名的tag
	 * @return 返回以tag区分的内容，默认为空值
	 */
	public static String getContent( Context context, String tag )
	{
		SharedPreferences sp = context.getSharedPreferences( tag,
				Context.MODE_PRIVATE );
		String myF = sp.getString( tag, "" );
		return myF;
	}

	private final static String JSON_CACHE = "json_cache";

	/**
	 * 存放JSON缓存数据
	 * 
	 * @param context
	 * @param tag
	 * @param content
	 * @return
	 */
	public static void putJSONCache( Context context, String key, String content )
	{
		SharedPreferences sp = context.getSharedPreferences( JSON_CACHE,
				Context.MODE_PRIVATE );
		Editor editor = sp.edit();
		editor.putString( key, content );
		editor.commit();

	}

	/**
	 * 读取JSON缓存数据
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String readJSONCache( Context context, String key )
	{
		SharedPreferences sp = context.getSharedPreferences( JSON_CACHE,
				Context.MODE_PRIVATE );
		String jsoncache = sp.getString( key, null );
		return jsoncache;
	}
}
