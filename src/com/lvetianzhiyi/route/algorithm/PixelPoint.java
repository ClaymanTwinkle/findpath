package com.lvetianzhiyi.route.algorithm;

import java.io.Serializable;

/**
 * 
 * 
 * Point 用来表示坐标的类
 * 
 * kesar kesar
 * 
 * @version 1.0.0
 *
 */
public class PixelPoint implements Serializable
{
	public static float SCALE =0.0f;
	private static final long serialVersionUID = 458655026013795308L;
	// 坐标值
	private int mPixel_x;
	private int mPixel_y;
	private int mIndex_x;
	private int mIndex_y;
	private int mZ; // 取值为[-1,1]

	/**
	 * 
	 * 以默认的x和y值创建一个坐标实例
	 *
	 */
	public PixelPoint()
	{
		this( 0, 0 );
	}

	/**
	 * 
	 * 带参x,y创建一个坐标实例.
	 *
	 * @param pX
	 * @param pY
	 */
	public PixelPoint( int pX, int pY )
	{
		this.mPixel_x = pX;
		this.mPixel_y = pY;
		mIndex_x = 0;
		mIndex_y = 0;
	}

	/**
	 * 
	 * 带参x,y,z创建一个坐标实例.
	 *
	 * @param pX
	 * @param pY
	 * @param pZ
	 */
	public PixelPoint( int pX, int pY, int pZ )
	{
		this.mPixel_x = pX;
		this.mPixel_y = pY;
		this.mZ = pZ;
		mIndex_x = 0;
		mIndex_y = 0;
	}

	/**
	 * 
	 * set(同时修改x，y值)
	 * 
	 * @param x
	 * @param y
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void set( int x, int y )
	{
		this.mPixel_x = x;
		this.mPixel_y = y;
	}

	/**
	 * 
	 * setIndex(数组下标的横纵坐标)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	public void setIndex( int x, int y )
	{
		this.mIndex_x = x;
		this.mIndex_y = y;
	}

	public int getIndex_X()
	{
		return mIndex_x;
	}

	public int getIndex_Y()
	{
		return mIndex_y;
	}

	/**
	 * CalculateIndexPos 计算图片像素点对应的数组下标位置
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	public void CalculateIndexPos()
	{
		// 图片中的 X 对应地图数组中的下标 Y
		mIndex_x = (int) ( mPixel_y / SCALE / AStar.RATIO );
		// 图片中的 Y 对应地图数组中的下标 X
		mIndex_y = (int) ( mPixel_x / SCALE / AStar.RATIO );
	}

	/**
	 * CalculatePixelPos 计算图片数组下标对应在图片中的像素点位置
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	public void CalculatePixelPos()
	{
		mPixel_x = (int) ( mIndex_y * AStar.RATIO * SCALE );
		mPixel_y = (int) ( mIndex_x * AStar.RATIO * SCALE );
	}

	/**
	 * getInstance(返回两点间的距离)
	 * 
	 * @param pPoint
	 * @return double
	 * @exception
	 * @since 1.0.0
	 */
	public double getInstance( PixelPoint pPoint )
	{
		return Math.sqrt( ( 1.0 * mIndex_x - pPoint.getIndex_X() )
				* ( 1.0 * mIndex_x - pPoint.getIndex_X() )
				+ ( 1.0 * mIndex_y - pPoint.getIndex_Y() )
				* ( 1.0 * mIndex_y - pPoint.getIndex_Y() ) );
	}

	/**
	 * equals(比较两个坐标是否相同)
	 * 
	 * @param pPoint
	 * @return true表示两个坐标相同，false表示两个坐标不相同 boolean
	 * @exception
	 * @since 1.0.0
	 */
	public boolean equals( PixelPoint pPoint )
	{
		return pPoint.getIndex_X() == mIndex_x
				&& pPoint.getIndex_Y() == mIndex_y;
	}

	/**
	 * 输出坐标信息
	 */
	public String toString()
	{
		return "(Pixel_x=" + mPixel_x + ",Pixel_y=" + mPixel_y + ",Index_x="
				+ mIndex_x + ",Index_y=" + mIndex_y + ", z=" + mZ + ")";
	}

	/**
	 * 
	 * setX(这里用一句话描述这个方法的作用)
	 * 
	 * @param x
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setX( int x )
	{
		this.mPixel_x = x;
	}

	/**
	 * 
	 * setY(这里用一句话描述这个方法的作用)
	 *
	 * @param y
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setY( int y )
	{
		this.mPixel_y = y;
	}

	/**
	 * 
	 * setZ(设置z值)
	 * 
	 * @param mZ
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setZ( int mZ )
	{
		this.mZ = mZ;
	}

	/**
	 * 
	 * getX(这里用一句话描述这个方法的作用)
	 * 
	 * @return int
	 * @exception
	 * @since 1.0.0
	 */
	public int getX()
	{
		return mPixel_x;
	}

	/**
	 * 
	 * getY(这里用一句话描述这个方法的作用)
	 * 
	 * @return int
	 * @exception
	 * @since 1.0.0
	 */
	public int getY()
	{
		return mPixel_y;
	}

	/**
	 * 
	 * getZ(取得Z值)
	 * 
	 * @return int
	 * @exception
	 * @since 1.0.0
	 */
	public int getZ()
	{
		return mZ;
	}

}
