package com.lvetianzhiyi.route.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class JsonManger
{
	private static byte[] data = null;
	private static URL url = null;
	private static ByteArrayOutputStream outStream = null;
	private static String jsonPointString = null;
	private static InputStream inStream = null;

	/**
	 * 
	 * initUrl(初始化url)
	 * 
	 * @param urlPath
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public static void init( String urlPath )
	{
		data = new byte[1024];
		outStream = new ByteArrayOutputStream();
		jsonPointString = "";
		try
		{
			url = new URL( urlPath );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * 从指定的URL中获取数组
	 * 
	 * @param urlPath
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public static String readParse() throws Exception
	{
		// 重置字节流
		outStream.reset();
		int len = 0;
		inStream = url.openStream();
		// ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ( ( len = inStream.read( data ) ) != -1 )
		{
			outStream.write( data, 0, len );
		}
		String result = new String( outStream.toByteArray() );// 通过out.Stream.toByteArray获取到写的数据
		inStream.close();

		inStream = null;

		if ( !jsonPointString.equals( result ) )
		{
			jsonPointString = result;

			return result;
		}

		return null;
	}

	public static float[] get3data( String point )
	{
		float data[] = new float[3];
		// 获取 x, y 坐标及层数
		data[0] = ( Float.parseFloat( point.split( "," )[0].substring( 1 ) ) - 0.02f ) / 0.96f;

		String tempLayerString = point.split( "," )[2];
		data[2] = Float.parseFloat( tempLayerString.substring( 0,
				tempLayerString.length() - 1 ) );
		int temp = (int) data[2];
		if ( temp == 0 )
		{
			data[1] = ( Float.parseFloat( point.split( "," )[1] ) - 0.02f ) / 0.36f * 0.375f;
		}
		else
		{
			data[1] = ( Float.parseFloat( point.split( "," )[1] ) - 0.02f ) / 0.96f;
		}

		return data;
	}
}
