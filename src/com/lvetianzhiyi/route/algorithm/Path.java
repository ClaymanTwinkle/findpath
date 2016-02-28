package com.lvetianzhiyi.route.algorithm;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 
 * Path 用来表示小路径的类 kesar kesar 2015年4月26日 下午10:25:38
 * 
 * @version 1.0.0
 *
 */
public class Path
{
	private PixelPoint mStartPoint; // 路径起点坐标
	private PixelPoint mGoalPoint; // 路径终点坐标
	private String mGoalBuildingName; // 终点的建筑物名
	private int mCost; // 路径总代价
	private List< PixelPoint > mPathList; // 路径链表
	private int mZ_Index; // 代表路径所在的楼层(-1:地下层，0:第一层，1第二层)
	private DirectedPath mDirectedPath;	// 短路径

	/**
	 * 
	 * 创建一个新的实例 Path.
	 *
	 */
	public Path()
	{
		mPathList = new LinkedList< PixelPoint >();
	}

	/**
	 * 
	 * addPoint(向路径中添加坐标点)
	 * 
	 * @param pPoint
	 *            要添加的坐标 void
	 * @exception
	 * @since 1.0.0
	 */
	public void addPoint( PixelPoint pPoint )
	{
		mPathList.add( pPoint );
	}

	/**
	 * 
	 * compareWith(比较与其他路径代价大小)
	 * 
	 * @param pPath
	 * @return true 表示当前的路径代价大，false 表示当前的路径代价小 boolean
	 * @exception
	 * @since 1.0.0
	 */
	public boolean compareWith( Path pPath )
	{
		if ( pPath == null )
		{
			return false;
		}
		return this.getCost() > pPath.getCost() ? true : false;
	}

	/**
	 * 
	 * setStartPoint(设置起始点坐标)
	 * 
	 * @param mStartPoint
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setStartPoint( PixelPoint mStartPoint )
	{
		this.mStartPoint = mStartPoint;
	}

	/**
	 * 
	 * setGoalPoint(设置终点坐标)
	 * 
	 * @param mGoalPoint
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setGoalPoint( PixelPoint mGoalPoint )
	{
		this.mGoalPoint = mGoalPoint;
	}

	/**
	 * 
	 * setCost(设置代价)
	 *
	 * @param mCost
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setCost( int mCost )
	{
		this.mCost = mCost;
	}

	/**
	 * 
	 * setPathList(设置出代表路径的坐标点的链表)
	 * 
	 * @param mPathList
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setPathList( List< PixelPoint > mPathList )
	{
		this.mPathList = mPathList;
	}

	/**
	 * 
	 * getStartPoint(返回起始点坐标)
	 * 
	 * @return PixelPoint
	 * @exception
	 * @since 1.0.0
	 */
	public PixelPoint getStartPoint()
	{
		return mStartPoint;
	}

	/**
	 * 
	 * getGoalPoint(返回终点坐标)
	 * 
	 * @return PixelPoint
	 * @exception
	 * @since 1.0.0
	 */
	public PixelPoint getGoalPoint()
	{
		return mGoalPoint;
	}

	/**
	 * 
	 * getCost(返回路径总代价)
	 *
	 * @return int
	 * @exception
	 * @since 1.0.0
	 */
	public int getCost()
	{
		return mCost;
	}

	/**
	 * 
	 * getPathList(返回包含路径所有坐标的链表)
	 * 
	 * @return List<PixelPoint>
	 * @exception
	 * @since 1.0.0
	 */
	public List< PixelPoint > getPathList()
	{
		return mPathList;
	}

	/**
	 * 
	 * getZ_Index(取得路径中坐标的z值)
	 * 
	 * @return z值 int
	 * @exception
	 * @since 1.0.0
	 */
	public int getZ_Index()
	{
		return mZ_Index;
	}

	/**
	 * 
	 * setZ_Index(设置路径中坐标的z值)
	 * 
	 * @param mZ_Index
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setZ_Index( int mZ_Index )
	{
		this.mZ_Index = mZ_Index;
	}

	/**
	 * 
	 * getGoalBuildingName(得到目标建筑物的名字)
	 * 
	 * @return String
	 * @exception
	 * @since 1.0.0
	 */
	public String getGoalBuildingName()
	{
		return mGoalBuildingName;
	}

	/**
	 * 
	 * setGoalBuildingName(设置目标建筑物的名字)
	 * 
	 * @param mGoalBuildingName
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setGoalBuildingName( String mGoalBuildingName )
	{
		this.mGoalBuildingName = mGoalBuildingName;
	}

	public DirectedPath getmDirectedPath()
	{
		return mDirectedPath;
	}

	public void setmDirectedPath(DirectedPath mDirectedPath)
	{
		this.mDirectedPath = mDirectedPath;
	}

	@ Override
	public String toString()
	{
		return "Path [mStartPoint=" + mStartPoint + ", mGoalPoint="
				+ mGoalPoint + ", mCost=" + mCost + ", mPathList=" + mPathList
				+ "]";
	}
}
