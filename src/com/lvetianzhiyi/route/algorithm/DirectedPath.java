package com.lvetianzhiyi.route.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.lvetianzhiyi.route.type.Direction;


public class DirectedPath
{
	private List< PixelPoint > mPathList; // 路径链表
	private Direction mDirection;		  // 方向
	private int mCost;					  // 路径代价
	private int mSize;					  // 点的数量
	private PixelPoint mStartPoint;		  // 起点
	private PixelPoint mEndPoint;		  // 终点
	private DirectedPath mNextDirectedPath; // 下一个路
	private Direction mPathEL;				// 坐不坐电梯
	private int mLayerNo;					// 第几层
	
	public DirectedPath()
	{
		mPathList=new ArrayList<PixelPoint>();
		mDirection=null;
		mPathEL=null;
		mCost=0;
		mSize=0;
	}
	
	/**
	 * 
	 * AddPoint(添加坐标到集合中)
	 *
	 * @param pPoint 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public void AddPoint(PixelPoint pPoint)
	{
		if(mSize==0)
		{
			mStartPoint=pPoint;
			mLayerNo = mStartPoint.getZ()+1;
		}
		mPathList.add(pPoint);
		mEndPoint=pPoint;
		mSize++;
	}
	
	/**
	 * 
	 * isEmpty(判空)
	 * 
	 * @return 
	 * boolean
	 * @exception 
	 * @since  1.0.0
	 */
	public boolean isEmpty()
	{
		if(mSize==0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * CalcCost(计算总代价)
	 * 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public void CalcCost()
	{
		if(mDirection!=null)
		{
			if(mDirection==Direction.LEFT||mDirection==Direction.RIGHT||mDirection==Direction.UP||mDirection==Direction.DOWN)
			{
				mCost=mSize*50;
			}
			else
			{
				mCost=mSize*71;
			}
		}
	}
	
	/**
	 * 
	 * size(坐标的数量)
	 * 
	 * @return 
	 * int
	 * @exception 
	 * @since  1.0.0
	 */
	public int size()
	{
		return mSize;
	}

	public List<PixelPoint> getmPathList()
	{
		return mPathList;
	}

	public Direction getmDirection()
	{
		return mDirection;
	}

	public int getmCost()
	{
		return mCost;
	}

	public PixelPoint getmStartPoint()
	{
		return mStartPoint;
	}

	public PixelPoint getmEndPoint()
	{
		return mEndPoint;
	}

	public void setmPathList(List<PixelPoint> mPathList)
	{
		this.mPathList = mPathList;
	}

	public void setmDirection(Direction mDirection)
	{
		this.mDirection = mDirection;
	}

	public void setmCost(int mCost)
	{
		this.mCost = mCost;
	}

	public void setmStartPoint(PixelPoint mStartPoint)
	{
		this.mStartPoint = mStartPoint;
	}

	public void setmEndPoint(PixelPoint mEndPoint)
	{
		this.mEndPoint = mEndPoint;
	}
	
	public DirectedPath getmNextDirectedPath()
	{
		return mNextDirectedPath;
	}

	public void setmNextDirectedPath(DirectedPath mNextDirectedPath)
	{
		this.mNextDirectedPath = mNextDirectedPath;
	}
	
	public int getmLayerNo()
	{
		return mLayerNo;
	}

	public void setmLayerNo(int mLayerNo)
	{
		this.mLayerNo = mLayerNo;
	}

	public Direction getmPathEL()
	{
		return mPathEL;
	}

	public void setmPathEL(Direction mPathEL)
	{
		this.mPathEL = mPathEL;
	}

	@Override
	public String toString()
	{
		return "DirectedPath [mPathList=" + mPathList + ", mDirection="
				+ mDirection + ", mCost=" + mCost + ", mSize=" + mSize
				+ ", mStartPoint=" + mStartPoint + ", mEndPoint=" + mEndPoint
				+ ", mNextDirectedPath=" + mNextDirectedPath + "]";
	}
}
