package com.lvetianzhiyi.route.algorithm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;

/**
 * 
 * 
 * DataIOTool 地图数据输入输出流的工具 kesar kesar 2015年4月24日 下午10:43:49
 * 
 * @version 1.0.0
 *
 */
public class DataIOTool
{
	/**
	 * 
	 * input(使用对象输入流，得到文件中保存的对象)
	 * 
	 * @param fileName
	 * @return Object
	 * @exception
	 * @since 1.0.0
	 */
	public static Object input( String fileName, Activity pActivity )
	{
		Object object = null;
		ObjectInputStream objectInputStream = null;
		try
		{
			objectInputStream = new ObjectInputStream( pActivity.getResources()
					.getAssets().open( fileName ) );
			// 读取文件中保存的对象
			object = objectInputStream.readObject();
			// 关闭流
			objectInputStream.close();
		}
		catch ( FileNotFoundException e )
		{
			e.printStackTrace();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		catch ( ClassNotFoundException e )
		{
			e.printStackTrace();
		}

		return object;
	}

	/**
	 * 
	 * output(将对象保存到文件中)
	 * 
	 * @param object
	 *            要保存的文件 void
	 * @exception
	 * @since 1.0.0
	 */
	public static void output( Object object, String fileName )
	{
		if ( object != null )
		{
			try
			{
				FileOutputStream fileOutputStream = new FileOutputStream(
						fileName );
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(
						fileOutputStream );
				// 写入对象到文件中
				objectOutputStream.writeObject( object );
				// 关闭流
				objectOutputStream.close();
				fileOutputStream.close();

			}
			catch ( FileNotFoundException e )
			{
				e.printStackTrace();
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
		}

	}

	public static void printMap( byte[][] pMap )
	{
		for ( int i = 0; i < pMap.length; i++ )
		{
			for ( int j = 0; j < pMap[i].length; j++ )
			{
				//System.out.print( pMap[i][j] );
			}
			//System.out.println();
		}
	}
}
