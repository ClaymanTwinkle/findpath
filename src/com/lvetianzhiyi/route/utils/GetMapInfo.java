package com.lvetianzhiyi.route.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;

public class GetMapInfo
{
	private String mMap_Path;
	private int mRow;
	private int mCol;
	private MyPoint mData[][];
	private Activity mActivity;

	public GetMapInfo( String pMap_Path, Activity pActivity )
	{
		mMap_Path = pMap_Path;
		mRow = 0;
		mCol = 0;
		mActivity = pActivity;
		Init();
	}

	private void Init()
	{
		InputStream is;
		try
		{
			is = mActivity.getAssets().open( mMap_Path );
			InputStreamReader reader = new InputStreamReader( is );
			BufferedReader bufferedReader = new BufferedReader( reader );
			String lineTxt = null;
			while ( ( lineTxt = bufferedReader.readLine() ) != null
					&& !lineTxt.equals( "" ) )
			{
				if ( mCol == 0 )
					mCol = lineTxt.length();
				mRow++;
			}
			mCol = ( mCol + 1 ) / 2;
			mData = new MyPoint[mRow][mCol];
			bufferedReader.close();
			is = mActivity.getAssets().open( mMap_Path );
			reader = new InputStreamReader( is );
			bufferedReader = new BufferedReader( reader );
			int k = 0;
			while ( ( lineTxt = bufferedReader.readLine() ) != null
					&& !lineTxt.equals( "" ) )
			{
				int size = lineTxt.length();
				for ( int i = 0, j = 0; i < size; i++ )
				{
					if ( lineTxt.charAt( i ) == '0' )
					{
						mData[k][j] = new MyPoint( 50 * k, 50 * j, k, j, '0' );
						j++;
					}
					else if ( lineTxt.charAt( i ) == '1' )
					{
						mData[k][j] = new MyPoint( 50 * k, 50 * j, k, j, '1' );
						j++;
					}
				}
				k++;
			}
			bufferedReader.close();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public String getmMap_Path()
	{
		return mMap_Path;
	}

	public MyPoint[][] getmData()
	{
		return mData;
	}
};