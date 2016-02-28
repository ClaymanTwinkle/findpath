package com.lvetianzhiyi.route.algorithm;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 
 * Building 用于标识地点的类 kesar kesar 2015年4月25日 下午10:25:47
 * 
 * @version 1.0.0
 *
 */
public class Building implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String mName; // 地点名称
	private List< PixelPoint > mLocation; // 地点可达位置
	private String mType; // 地点类型
	private byte mZ; // 层序数(-1：地下层，0：第一层，1：第二层)

	public Building()
	{
		mLocation = new LinkedList< PixelPoint >();
	}

	/**
	 * 
	 * addPoint(添加坐标)
	 * 
	 * @param pPoint
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void addPoint( PixelPoint pPoint )
	{
		mLocation.add( pPoint );
	}

	/**
	 * 
	 * setName(设置地点名称)
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
	 * setLocation(设置地点可达坐标)
	 * 
	 * @param mLocation
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setLocation( List< PixelPoint > mLocation )
	{
		this.mLocation = mLocation;
	}

	/**
	 * 
	 * setType(设置地点所属类型)
	 * 
	 * @param mType
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setType( String mType )
	{
		this.mType = mType;
	}

	/**
	 * 
	 * setZ(设置建筑物的层序数)
	 * 
	 * @param mZ
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setZ( byte mZ )
	{
		this.mZ = mZ;
	}

	/**
	 * 
	 * getName(获取地点名称)
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
	 * getLocation(获取地点可达坐标的集合)
	 * 
	 * @return List<Point>
	 * @exception
	 * @since 1.0.0
	 */
	public List< PixelPoint > getLocation()
	{
		return mLocation;
	}

	/**
	 * 
	 * getType(获取地点类型)
	 * 
	 * @return String
	 * @exception
	 * @since 1.0.0
	 */
	public String getType()
	{
		return mType;
	}

	/**
	 * 
	 * getZ(获取建筑物的层序数)
	 * 
	 * @return byte
	 * @exception
	 * @since 1.0.0
	 */
	public byte getZ()
	{
		return mZ;
	}

	/**
	 * 输出该实例的地点信息
	 */
	public String toString()
	{
		return "Building [mName=" + mName + ", mLocation=" + mLocation
				+ ", mType=" + mType + "]";
	}

}
