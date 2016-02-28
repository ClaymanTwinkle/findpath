package com.lvetianzhiyi.route.algorithm;

import java.util.Arrays;
import java.util.List;

import com.lvetianzhiyi.route.utils.DealData;

/**
 * 
 * 
 * Route
 * 一次规划的总路线(包含所有小路径)
 * kesar
 * kesar
 * 2015年5月18日 下午11:33:26
 * 
 * @version 1.0.0
 *
 */
final public class Route
{
	private String[] mTransitBuildingName;	// 中转的地点名的数组
	private List<Path> mResultPaths;		// 到达一个地点的所有路径
	private int mSize;						// 路径数量
	private Path mStartPath;				// 开始路径
	private Path mEndPath;					// 结束路径
	
	public Route(List<Path> resultPathList)
	{
		mSize = resultPathList.size();
		mTransitBuildingName=new String[mSize];
		mResultPaths=resultPathList;
		// 遍历
		for(int i=0;i<mSize;i++)
		{
			Path p =resultPathList.get(i);
			mTransitBuildingName[i]=p.getGoalBuildingName();
			if(i==0)
			{
				mStartPath=p;
			}
			if(i==mSize-1)
			{
				mEndPath=p;
			}
			if(i+1<mSize)
			{
			    Path p2 = resultPathList.get(i+1);
				DealData.TOEL(p.getmDirectedPath(), p2.getmDirectedPath());
			}
		}
	}

	/**
	 * 
	 * getmTransitBuildingName(得到地点名的所有集合)
	 *
	 * @return 
	 * String[]
	 * @exception 
	 * @since  1.0.0
	 */
	public String[] getmTransitBuildingName()
	{
		return mTransitBuildingName;
	}

	/**
	 * 
	 * getmResultPaths(得到路径数组)
	 * 
	 * @return 
	 * Path[]
	 * @exception 
	 * @since  1.0.0
	 */
	public List<Path> getmResultPaths()
	{
		return mResultPaths;
	}

	/**
	 * 
	 * getmSize(得到路径的数量)
	 *
	 * @return 
	 * int
	 * @exception 
	 * @since  1.0.0
	 */
	public int getmSize()
	{
		return mSize;
	}

	/**
	 * 
	 * setmTransitBuildingName(设置所有地点名的集合)
	 * 
	 * @param mTransitBuildingName 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public void setmTransitBuildingName(String[] mTransitBuildingName)
	{
		this.mTransitBuildingName = mTransitBuildingName;
	}

	/**
	 * 
	 * setmResultPaths(设置路径数组)
	 * 
	 * @param mResultPaths 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public void setmResultPaths(List<Path> mResultPaths)
	{
		this.mResultPaths = mResultPaths;
		mSize = this.mResultPaths.size();
		mTransitBuildingName=new String[mSize];
		// 遍历
		for(int i=0;i<mSize;i++)
		{
			Path p =this.mResultPaths.get(i);
			mTransitBuildingName[i]=p.getGoalBuildingName();
			if(i==0)
			{
				mStartPath=p;
			}
			if(i==mSize-1)
			{
				mEndPath=p;
			}
			if(i+1<mSize)
			{
			    Path p2 = this.mResultPaths.get(i+1);
				DealData.TOEL(p.getmDirectedPath(), p2.getmDirectedPath());
			}
		}
		 
	}

	/**
	 * 
	 * setmSize(设置路径数量)
	 *
	 * @param mSize 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public void setmSize(int mSize)
	{
		this.mSize = mSize;
	}

	public Path getmStartPath()
	{
		return mStartPath;
	}

	public Path getmEndPath()
	{
		return mEndPath;
	}

	public void setmStartPath(Path mStartPath)
	{
		this.mStartPath = mStartPath;
	}

	public void setmEndPath(Path mEndPath)
	{
		this.mEndPath = mEndPath;
	}

	@Override
	public String toString()
	{
		return "Route [mTransitBuildingName="
				+ Arrays.toString(mTransitBuildingName) + ", mResultPaths="
				+ mResultPaths + ", mSize=" + mSize + ", mStartPath="
				+ mStartPath + ", mEndPath=" + mEndPath + "]";
	}
	
	
}
