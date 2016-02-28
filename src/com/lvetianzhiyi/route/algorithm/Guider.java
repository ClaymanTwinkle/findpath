package com.lvetianzhiyi.route.algorithm;

import java.util.List;
import java.util.Queue;

import android.app.Activity;

import com.lvetianzhiyi.route.data.OnlineData;

/**
 * 
 * 
 * Guider 路径引导类 kesar kesar 2015-7-21 下午7:37:51
 * 
 * @version 1.0.0
 * 
 */
public class Guider
{
	public static final int RUN = 1;
	public static final int REACHGOAL = 2;
	public static final int STRIKEWALL = 3;
	public static final int OUTMAP = 4;
	public static final int TIP=5;

	// 用于规划路径的实例
	private PlanningPath mPainingPath;
	// 当前点坐标
	private PixelPoint mPoint;
	// 所有路径
	private Queue<Route> mRoutes;
	
	public Guider(Activity pActivity)
	{
		mPainingPath = new PlanningPath(pActivity);
	}

	/**
	 * 
	 * routeSearch(路径规划)
	 * 
	 * @param pStartPos
	 *            起始点坐标
	 * @param pGoalLayerNo
	 *            目标层
	 * @param pGoalPlaceName
	 *            目标建筑
	 * @return if result is null,则找不到路径或者输入数据有误 ;else 返回规划后的路径的集合 List<Path>
	 * @exception
	 * @since 1.0.0
	 */
	public List<Path> routeSearch(PixelPoint pStartPos, int pGoalLayerNo,
			PixelPoint pGoalPos, String pGoalPlaceName)
	{
		try
		{
			return mPainingPath.search(pStartPos, pGoalLayerNo, pGoalPos,
					pGoalPlaceName);
		}
		catch (OutOfMemoryError e)
		{
			return mPainingPath.search(pStartPos, pGoalLayerNo, pGoalPos,
					pGoalPlaceName);
		}
	}

	/**
	 * 
	 * HandleRunDeviation(处理跑偏)
	 * 
	 * @param pDrawImage
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public int HandleRunDeviation()
	{
		if (!mRoutes.isEmpty())
		{
			Route route = mRoutes.element();
			try
			{
				int answer = checkLocation(route);
				if (answer != -1)
				{
					return answer;
				}
				if (isRunDeviation(route.getmResultPaths()))
				{
					System.err.println("RUN");
					RePlanPathInMap();
					return RUN;
				}
			}
			catch (Exception e)
			{
				System.err.println("出错！！！！");
			}
		}
		return 0;
		// 画人物DrawPosition();
	}

	/**
	 * 
	 * RePlanPathInMap(重新绘制地图)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void RePlanPathInMap()
	{
		if (!mRoutes.isEmpty())
		{
			List<Path> mResultPaths = mRoutes.element().getmResultPaths();
			Path goalPath = mResultPaths.get(mResultPaths.size() - 1);

			List<Path> mtempPaths = this.routeSearch(mPoint, goalPath
					.getGoalPoint().getZ() + 1, goalPath.getGoalPoint(),
					goalPath.getGoalBuildingName());

			if (mtempPaths != null)
			{
				mRoutes.element().setmResultPaths(mtempPaths);
			}
		}

	}


	/**
	 * 
	 * isRunDeviation(判断是否跑遍)
	 * 
	 * @return true表示跑偏，false表示不跑偏 boolean
	 * @exception
	 * @since 1.0.0
	 */
	public boolean isRunDeviation(List<Path> pResultPaths)
	{
		double cost = Double.MAX_VALUE;

		if (pResultPaths != null)
		{
			for (Path path : pResultPaths)
			{
				if (path.getZ_Index() == mPoint.getZ())
				{
					List<PixelPoint> lists = path.getPathList();
					for (PixelPoint point : lists)
					{
						double temp = point.getInstance(mPoint);
						cost = temp < cost ? temp : cost;
					}
				}
			}
		}

		if (cost < 5)
		{
			return false;
		}
		return true;
	}

	/**
	 * 
	 * setmRoutes(设置路线的数组)
	 * 
	 * @param mRoutes
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void setmRoutes(Queue<Route> mRoutes)
	{
		this.mRoutes = mRoutes;
	}

	/**
	 * 
	 * checkLocation(检查位置)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	public int checkLocation(Route pRoute)
	{
		try
		{
			if (OnlineData.mMapArray[mPoint.getZ() + 1][mPoint.getIndex_X()][mPoint.getIndex_Y()] == 0)
			{
				Path path = pRoute.getmEndPath();
				PixelPoint goalPoint = path.getGoalPoint();
				if (goalPoint.getInstance(mPoint) < 2)
				{
					//System.out.println("到了" + path.getGoalBuildingName());
					mRoutes.remove();
					RePlanPathInMap();
					return REACHGOAL;
				}
			}
			else
			{
				//System.out.println("检查位置：撞墙了");
				return STRIKEWALL;
			}
		}
		catch (Exception e)
		{
			//System.out.println("检查位置：超出地图了");
			return OUTMAP;
		}
		return -1;
	}
	

	/**
	 * 
	 * SetLocationPoint(设置本地坐标)
	 * 
	 * @param point
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void SetLocationPoint(PixelPoint point)
	{
		this.mPoint = point;
	}

	/**
	 * 
	 * getPoint(得到当前坐标)
	 * 
	 * @return PixelPoint
	 * @exception
	 * @since 1.0.0
	 */
	public PixelPoint getPoint()
	{
		return mPoint;
	}

	public Queue<Route> getRoutes()
	{
		return mRoutes;
	}
}
