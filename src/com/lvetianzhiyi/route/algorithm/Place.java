package com.lvetianzhiyi.route.algorithm;

import android.annotation.SuppressLint;

/**
 * 
 * 
 * Place 用于临时表示地点的名称和坐在层序数的类 kesar kesar 2015年5月15日 下午8:04:56
 * 
 * @version 1.0.0
 *
 */

public class Place
{
	private String mName; // 地点名
	private int mLayerNo; // 当前所在层（0表示地下层，1表示第一层，2表示第二层
	private String mBuildingInfo;

	@ SuppressLint( "DefaultLocale" )
	public Place( String pBuildingInfo )
	{
		if ( pBuildingInfo.equals( "自定义地点" ) )
		{
			mLayerNo = -1;
			mName = pBuildingInfo;
		}
		else
		{
			mBuildingInfo = pBuildingInfo.toUpperCase();
			if ( mBuildingInfo.startsWith( "B1层" ) )
			{
				mLayerNo = 0;

			}
			else if ( mBuildingInfo.startsWith( "F1层" ) )
			{
				mLayerNo = 1;
			}
			else if ( mBuildingInfo.startsWith( "F2层" ) )
			{
				mLayerNo = 2;
			}
			mName = mBuildingInfo.split( " " )[1];
		}
	}

	/**
	 * 
	 * getName(得到地点名)
	 * 
	 * @return String
	 * @exception
	 * @since 1.0.0
	 */
	public String getName()
	{
		return mName;
	}

	/**
	 * 
	 * setName(设置地点名)
	 * 
	 * @param mName
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setName( String mName )
	{
		this.mName = mName;
	}

	/**
	 * 
	 * getLayerNo(得到地点是在第几层)
	 * 
	 * @return 当前所在层（0表示地下层，1表示第一层，2表示第二层） int
	 * @exception
	 * @since 1.0.0
	 */
	public int getLayerNo()
	{
		return mLayerNo;
	}

	/**
	 * 
	 * setLayerNo(设置地点所在层)
	 * 
	 * @param mLayerNo
	 *            当前所在层（0表示地下层，1表示第一层，2表示第二层） void
	 * @exception
	 * @since 1.0.0
	 */
	public void setLayerNo( int mLayerNo )
	{
		this.mLayerNo = mLayerNo;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.lang.Object#toString()
	 */
	@ Override
	public String toString()
	{
		return "Place [mName=" + mName + ", mLayerNo=" + mLayerNo
				+ ", mBuildingInfo=" + mBuildingInfo + "]";
	}

}