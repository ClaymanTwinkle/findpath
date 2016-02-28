package com.lvetianzhiyi.route.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

/**
 * 
 * 
 * IOTest 将数组通过对象流写入到文件中 kesar kesar 2015年4月24日 下午10:44:43
 * 
 * @version 1.0.0
 *
 */
public class IOTest
{
	public static void main( String[] args ) throws Exception
	{
		//System.out.println( "请选择地图：1.二楼，2.一楼，3.地下城" );
		Scanner in = new Scanner( System.in );
		int i = in.nextInt();
		String fileName = null;
		switch ( i )
		{
			case 1 :
				fileName = "Our_Map_Floor2.txt";
				break;
			case 2 :
				fileName = "Our_Map_Floor1.txt";
				break;
			case 3 :
				fileName = "Our_Map_Underground.txt";
				break;
			default :
				break;
		}

		File file = new File( fileName );
		FileReader reader = new FileReader( file );
		BufferedReader bufferedReader = new BufferedReader( reader );

		String str = null;
		byte[][] map = new byte[19][50];
		i = 0;

		while ( ( str = bufferedReader.readLine() ) != null )
		{
			String[] s = str.split( "	" );
			for ( int j = 0; j < s.length; j++ )
			{
				map[i][j] = Byte.parseByte( s[j] );
			}
			i++;
		}

		DataIOTool.output( map, fileName.replaceAll( ".txt", "" ) );

		for ( int y = 0; y < map.length; y++ )
		{
			for ( int x = 0; x < map[y].length; x++ )
			{
				//System.out.print( map[y][x] );
			}
			//System.out.println();
		}

		bufferedReader.close();
		reader.close();
		in.close();
	}
}
