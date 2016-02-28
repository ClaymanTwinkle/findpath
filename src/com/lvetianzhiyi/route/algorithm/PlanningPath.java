package com.lvetianzhiyi.route.algorithm;

import java.util.LinkedList;
import java.util.List;

import com.lvetianzhiyi.route.data.OnlineData;

import android.app.Activity;

/**
 * 
 * 
 * PlanningPath
 * 
 * kesar kesar 2015-7-20 下午10:28:32
 * 
 * @version 1.0.0
 * 
 */
public class PlanningPath
{
	private AStar mAStar; // A星算法实例对象
	private BuildingDataTool mBuildingDataTool; // 获取建筑物数据工具的实例(用于通过建筑物名得到建筑物坐标)
	private String[] mMapFile;// 地图文件
	private String[][] mELInMap;// 电梯名的集合

	/**
	 * 
	 * 创建一个新的实例 PainingPath.
	 *
	 */
	public PlanningPath( Activity pActivity )
	{
		// 实例化获取建筑物数据工具类
		mBuildingDataTool = new BuildingDataTool( );
		mMapFile=OnlineData.mMapFile;
		mELInMap=OnlineData.mELInMap;
	}

	/**
	 * 
	 * search(开始路径规划，包括非跨层和跨层的)
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
	public List< Path > search( PixelPoint pStartPos, int pGoalLayerNo, PixelPoint pGoalPos, String pGoalPlaceName )
	{
		// 实例化A星
		mAStar = new AStar();
		// 规划路径的结果集
		List< Path > resultPaths = null;
		// 目标地点的坐标集
		List< PixelPoint > points;

		mBuildingDataTool.init( mMapFile[pGoalLayerNo] );
		if ( pGoalPlaceName.equals( "自定义地点" ) )
		{
			points = new LinkedList< PixelPoint >();
			points.add( pGoalPos );
		}
		else
		{
			points = mBuildingDataTool.findBuildingByName( pGoalPlaceName );
		}

		// 判断是否存在这个地点
		if ( points != null )
		{
			if ( !isCrossLayer( pStartPos, points.get( 0 ) ) )
			{// 没有跨层

				Path[] paths = sameLayerSearch( pStartPos, points );
				if ( paths != null )
				{
					resultPaths = new LinkedList< Path >();
					for ( int i = 0, length = paths.length; i < length; i++ )
					{
						resultPaths.add( paths[i] );
					}
				}
			}
			else
			{
				long startTime = System.currentTimeMillis(); // 获取开始时间
				// 跨层
				resultPaths = crossLayerSearch( pStartPos, pGoalLayerNo, points );
				long endTime = System.currentTimeMillis();
				System.err.println( "程序运行时间： " + ( endTime - startTime )
						+ "ms" );
			}
		}

		if ( resultPaths != null )
		{
			resultPaths.get( resultPaths.size() - 1 ).setGoalBuildingName(pGoalPlaceName );
		}

		return resultPaths;
	}

	/**
	 * 
	 * sameLayerSearchWithoutPassBuilding(通过起点地名和目的地名，开始路径规划，返回最短路径)
	 * 
	 * @param pStartPlaceName
	 *            起始地点名
	 * @param pGoalPlaceName
	 *            目标地点名
	 * @return 最短路径 Path
	 * @exception
	 * @since 1.0.0
	 */
	private Path sameLayerSearchWithoutPassBuilding( String pStartPlaceName, String pGoalPlaceName )
	{
		// 结果集
		List< PixelPoint > startPoints = mBuildingDataTool.findBuildingByName( pStartPlaceName );
		List< PixelPoint > goalPoints = mBuildingDataTool.findBuildingByName( pGoalPlaceName );
		// 最终规划出的路径
		Path resultPath = null;

		// 判断是否存在这个地点
		resultPath = sameLayerSearchWithoutPassBuilding( startPoints, goalPoints );

		if ( resultPath != null )
		{
			resultPath.setGoalBuildingName( pGoalPlaceName );
		}

		return resultPath;
	}

	/**
	 * 
	 * sameLayerSearchWithoutPassBuilding(同一层的路径规划不穿越SC)
	 * 
	 * @param pStartPos
	 *            起始点坐标
	 * @param pGoalPoints
	 *            目标建筑可达坐标的集合
	 * @return 规划后的路径 Path
	 * @exception
	 * @since 1.0.0
	 */
	private Path sameLayerSearchWithoutPassBuilding( PixelPoint pStartPos,List< PixelPoint > pGoalPoints )
	{
		fixPoint( pStartPos );
		// 最终规划出的路径
		Path resultPath = null;
		// 楼层序数
		int pGoalLayerNo = pStartPos.getZ() + 1;
		// 选择地图的二值化数组
		mAStar.changeMap( pGoalLayerNo );

		if ( pGoalPoints != null )
		{
			// 找出所有可达路径，比较出最短代价路径，选择最优路径
			for (int i=0,length=pGoalPoints.size();i<length;i++ )
			{
				PixelPoint tempPoint=pGoalPoints.get(i);
				fixPoint( tempPoint );
				mAStar.start( pStartPos, tempPoint );
				Path tempPath = mAStar.getResultPath();
				if(tempPath==null)
				{
					continue;
				}
				if ( resultPath != null )
				{
					// 比较出最短的路径
					if ( resultPath.compareWith( tempPath ) )
					{
						resultPath = tempPath;
					}
				}
				else
				{
					// 第一个赋值的路径
					resultPath = tempPath;
				}
			}
		}

		return resultPath;
	}

	/**
	 * 
	 * sameLayerSearchWithoutPassBuilding(同一层的路径规划不穿越SC)
	 * 
	 * @param pStartBuildingName
	 *            起始建筑的名
	 * @param pGoalPoints
	 *            目标可达坐标的集合
	 * @return Path
	 * @exception
	 * @since 1.0.0
	 */
	private Path sameLayerSearchWithoutPassBuilding( String pStartBuildingName,List< PixelPoint > pGoalPoints )
	{
		List< PixelPoint > startPoints = mBuildingDataTool.findBuildingByName( pStartBuildingName );

		return sameLayerSearchWithoutPassBuilding( startPoints, pGoalPoints );
	}

	/**
	 * 
	 * sameLayerSearchWithoutPassBuilding(同一层的路径规划不穿越SC)
	 * 
	 * @param pStartPoints
	 *            起始点的坐标的集合
	 * @param pGoalPoints
	 *            目标的坐标的集合
	 * @return Path
	 * @exception
	 * @since 1.0.0
	 */
	private Path sameLayerSearchWithoutPassBuilding(List< PixelPoint > pStartPoints, List< PixelPoint > pGoalPoints )
	{
		// 最终规划出的路径
		Path resultPath = null;

		// 判断是否存在这个地点
		if ( pStartPoints != null && pGoalPoints != null )
		{
			for (int i=0,length=pStartPoints.size();i<length;i++ )
			{
				Path tempPath = sameLayerSearchWithoutPassBuilding(pStartPoints.get(i),pGoalPoints);
				// 判空
				if ( tempPath != null )
				{
					// 比较出最短的路径
					if ( resultPath == null || !( tempPath.compareWith( resultPath ) ) )
					{
						resultPath = tempPath;
					}
				}
			}
		}

		return resultPath;
	}

	/**
	 * 
	 * sameLayerSearchWithoutPassBuilding(同一层的路径规划不穿越SC)
	 * 
	 * @param pStartBuildingName
	 *            起始建筑物的名
	 * @param pELInMap
	 *            可坐电梯的名称集合
	 * @return Path
	 * @exception
	 * @since 1.0.0
	 */
	private Path sameLayerSearchWithoutPassBuilding( String pStartBuildingName, String[] pELInMap )
	{
		// 结果路径
		Path resultPath = null;

		// 遍历目标建筑所有可达的坐标
		for ( String EL : pELInMap )
		{
			Path tempPath = sameLayerSearchWithoutPassBuilding( pStartBuildingName, EL );

			if ( tempPath != null )
			{
				tempPath.setGoalBuildingName( EL );
				// 比较路径代价，取最小代价的路径作为结果路径
				if ( !tempPath.compareWith( resultPath ) )
				{
					resultPath = tempPath;
				}
			}
		}

		return resultPath;
	}

	/**
	 * 
	 * sameLayerSearch(同一层的路径规划)
	 * 
	 * @param pStartPos
	 *            起始点坐标
	 * @param pGoalPoints
	 *            目标建筑可达坐标的集合
	 * @return 规划后的路径 Path
	 * @exception
	 * @since 1.0.0
	 */
	private Path[] sameLayerSearch( PixelPoint pStartPos,List< PixelPoint > pGoalPoints )
	{
		// 最终规划出的路径
		Path[] resultPaths = null;
		Path resultPath = null;

		if ( pStartPos!= null && pGoalPoints != null )
		{
			PixelPoint goalPoint=pGoalPoints.get(0);
			boolean sb=isInWall( pStartPos );
			boolean sp=isInWall(goalPoint);
			// 判断是否在第二层
			if ((pStartPos.getZ() == 1)&&((sb&&!sp)||(!sb&&sp)))
			{
				resultPaths = passSC( pStartPos, pGoalPoints );
			}
			else
			{
				resultPath = sameLayerSearchWithoutPassBuilding( pStartPos, pGoalPoints );
				if(resultPath!=null)
				{
					resultPaths = new Path[1];
					resultPaths[0] = resultPath;
				}
			}
		}

		return resultPaths;
	}

	/**
	 * 
	 * sameLayerSearch(同一层的路径规划)
	 * 
	 * @param pStartPoints
	 *            起始建筑物的可达坐标的集合
	 * @param pGoalPoints
	 *            目标建筑物的可达坐标的集合
	 * @return 规划后的路径 Path
	 * @exception
	 * @since 1.0.0
	 */
	private Path[] sameLayerSearch( List< PixelPoint > pStartPoints, List< PixelPoint > pGoalPoints )
	{
		// 最终规划出的路径
		Path[] resultPaths = null;
		Path resultPath = null;

		if ( pStartPoints!= null && pGoalPoints != null )
		{
			PixelPoint startPoint=pStartPoints.get(0);
			PixelPoint goalPoint=pGoalPoints.get(0);
			boolean sb=isInWall( startPoint );
			boolean sp=isInWall(goalPoint);
			// 判断是否在第二层
			if ((startPoint.getZ() == 1)&&((sb&&!sp)||(!sb&&sp)))
			{
				resultPaths = passSC( pStartPoints, pGoalPoints );
			}
			else
			{
				resultPath = sameLayerSearchWithoutPassBuilding( pStartPoints, pGoalPoints );
				if(resultPath!=null)
				{
					resultPaths = new Path[1];
					resultPaths[0] = resultPath;
				}
			}
		}

		return resultPaths;
	}

	/**
	 * 
	 * passSC(跨过SC整个过程的路径)
	 * 
	 * @param pStartPoint
	 *            起始点坐标
	 * @param pGoalPoints
	 *            目标建筑物的可达坐标的集合
	 * @return 规划后的路径 Path
	 * @exception
	 * @since 1.0.0
	 */
	private Path[] passSC( PixelPoint pStartPoint,List< PixelPoint > pGoalPoints )
	{
		// 最终规划出的路径
		Path[] resultPath = null;
		// 通过SC之前的路径
		Path startPath = null;
		// 通过SC之后的路径
		Path goalPath = null;
		// 通过的SC坐标集合
		List< PixelPoint > SCPoint = null;

		// //
		// 1.通过SC之后的路径
		// //
		for ( String SC : mELInMap[3] )
		{
			// SC的可达坐标的集合
			List< PixelPoint > tempSCPoint = mBuildingDataTool.findBuildingByName( SC );
			Path tempStartPath = sameLayerSearchWithoutPassBuilding(pStartPoint, tempSCPoint);

			// 判空
			if ( tempStartPath != null )
			{
				// 比较路径代价大小，取最短代价的路径
				if ( startPath == null || ( !tempStartPath.compareWith( startPath ) ) )
				{
					startPath = tempStartPath;
					SCPoint = tempSCPoint;
					startPath.setGoalBuildingName( SC );
				}
			}
		}

		// //
		// 2.通过SC之前的路径
		// //

		// 得到路径
		goalPath = sameLayerSearchWithoutPassBuilding(SCPoint, pGoalPoints);

		// 判空
		if ( startPath != null && goalPath != null )
		{
			resultPath = new Path[2];
			// 路径添加到数组中
			resultPath[0] = startPath;
			resultPath[1] = goalPath;
		}
		return resultPath;
	}

	/**
	 * 
	 * passSC(跨过SC整个过程的路径)
	 * 
	 * @param pStartPoint
	 *            起始点坐标
	 * @param pGoalPoints
	 *            目标建筑物的可达坐标的集合
	 * @return 规划后的路径 Path
	 * @exception
	 * @since 1.0.0
	 */
	private Path[] passSC( List< PixelPoint > pStartPoints, List< PixelPoint > pGoalPoints )
	{
		// 最终规划出的路径
		Path[] resultPath = null;
		// 通过SC之前的路径
		Path startPath = null;
		// 通过SC之后的路径
		Path goalPath = null;
		// 通过的SC坐标集合
		List< PixelPoint > SCPoint = null;

		// //
		// 1.通过SC之后的路径
		// //
		for ( String SC : mELInMap[3] )
		{
			// SC的可达坐标的集合
			List< PixelPoint > tempSCPoint = mBuildingDataTool.findBuildingByName( SC );
			Path tempStartPath = sameLayerSearchWithoutPassBuilding(pStartPoints, tempSCPoint);

			// 判空
			if ( tempStartPath != null )
			{
				// 比较路径代价大小，取最短代价的路径
				if ( startPath == null || ( !tempStartPath.compareWith( startPath ) ) )
				{
					startPath = tempStartPath;
					SCPoint = tempSCPoint;
					startPath.setGoalBuildingName( SC );
				}
			}
		}

		// //
		// 2.通过SC之前的路径
		// //

		// 得到路径
		goalPath = sameLayerSearchWithoutPassBuilding(SCPoint, pGoalPoints);

		// 判空
		if ( startPath != null && goalPath != null )
		{
			resultPath = new Path[2];
			// 路径添加到数组中
			resultPath[0] = startPath;
			resultPath[1] = goalPath;
		}
		return resultPath;
	}

	/**
	 * 
	 * crossLayerSearch(跨层规划路径)
	 * 
	 * @param pStartPos
	 *            起点坐标
	 * @param pGoalPoints
	 *            目标建筑可达坐标的集合
	 * @return 规划路径集合 List<Path>
	 * @exception
	 * @since 1.0.0
	 */
	private List< Path > crossLayerSearch( PixelPoint pStartPos, int pGoalLayerNo, List< PixelPoint > pGoalPoints )
	{
		List< Path > resultPaths = null;
		Path startResultPath = null;
		Path[] goalResultPaths = null;

		if ( ( pStartPos.getZ() == 0 && pGoalLayerNo == 0 )|| ( pStartPos.getZ() == -1 && pGoalLayerNo > 0 ) )
		{// 从(0-1,0-2,1-0)只能坐EL1~EL4的电梯
			startResultPath = findELPath( pStartPos, mELInMap[0] );
			//System.out.println(startResultPath.getGoalBuildingName());
			goalResultPaths = findGoalByEL(
					startResultPath.getGoalBuildingName(), pGoalPoints,
					pGoalLayerNo );
		}
		else if ( pStartPos.getZ() == 1 && pGoalLayerNo == 0 )
		{// 从(2-0)
			// 判断是否在墙内
			if ( isInWall( pStartPos ) )
			{
				startResultPath = findELPath( pStartPos, mELInMap[0] );
				goalResultPaths = findGoalByEL(
						startResultPath.getGoalBuildingName(), pGoalPoints,
						pGoalLayerNo );
			}
			else
			{
				startResultPath = findELPath( pStartPos, mELInMap[2] );

				// 设定目标建筑所在层的地图为一楼
				mBuildingDataTool.init( mMapFile[pGoalLayerNo + 1] );
				// 得到路径
				Path tempResultPath = sameLayerSearchWithoutPassBuilding(startResultPath.getGoalBuildingName(), mELInMap[0] );
				// 设定目标建筑所在层的地图为地下层
				mBuildingDataTool.init( mMapFile[pGoalLayerNo] );
				Path goalResultPath = sameLayerSearchWithoutPassBuilding(tempResultPath.getGoalBuildingName(), pGoalPoints );

				// 如果路径存在
				if ( goalResultPath != null && tempResultPath != null )
				{
					// 添加路径到数组中
					goalResultPaths = new Path[2];
					goalResultPaths[0] = tempResultPath;
					goalResultPaths[1] = goalResultPath;
				}
			}
		}
		else
		{// EL1~EL8的电梯随便坐
			startResultPath = findELPath( pStartPos, mELInMap[1] );
			goalResultPaths = findGoalByEL(
					startResultPath.getGoalBuildingName(), pGoalPoints,
					pGoalLayerNo );

			// //System.out.println(goalResultPaths);
		}

		// 将路径添加到集合中
		if ( startResultPath != null && goalResultPaths != null )
		{
			resultPaths = new LinkedList< Path >();
			resultPaths.add( startResultPath );
			for ( int i = 0, length = goalResultPaths.length; i < length; i++ )
			{
				resultPaths.add( goalResultPaths[i] );
			}
		}

		// 返回路径集合
		return resultPaths;
	}

	/**
	 * 
	 * findELPath(起点所在层寻找要跨层的电梯及规划出到达电梯的路线)
	 * 
	 * @param pStartPos
	 *            起点
	 * @param pELInMap
	 *            可到的电梯
	 * @return Path
	 * @exception
	 * @since 1.0.0
	 */
	private Path findELPath( PixelPoint pStartPos, String[] pELInMap )
	{
		// 最终的跨层规划的路径集合
		Path resultPath = null;
		// 跨层用的电梯
		String crossELName = null;

		// 遍历所有可用电梯
		for ( String EL : pELInMap )
		{
			// 设定起点所在层的地图
			mBuildingDataTool.init( mMapFile[pStartPos.getZ() + 1] );
			// 取得电梯可达坐标的集合
			List< PixelPoint > ELPoints = mBuildingDataTool
					.findBuildingByName( EL );
			// 临时用的路径
			Path[] tempPath = sameLayerSearch( pStartPos, ELPoints );
			// 判空
			if ( tempPath != null )
			{
				if ( tempPath.length == 1 && tempPath[0] != null )
				{
					// 比较出集合中最短路径
					if ( resultPath == null|| ( !tempPath[0].compareWith( resultPath ) ) )
					{
						// 开始时，将第一路径假设为最短，之后再与其他路径进行比较
						resultPath = tempPath[0];
						// 跨层用的电梯
						crossELName = EL;
					}
				}
			}
		}

		if ( resultPath != null )
		{
			resultPath.setGoalBuildingName( crossELName );
		}

		return resultPath;
	}

	/**
	 * 
	 * findGoalByEL(出电梯后规划到达目标建筑物的路径)
	 * 
	 * @param pCrossELName
	 *            电梯名
	 * @param pGoalPoints
	 *            目标建筑物的可达坐标的集合
	 * @param pGoalLayerNo
	 *            目标所在层的序号
	 * @return 规划后的路径 Path
	 * @exception
	 * @since 1.0.0
	 */
	private Path[] findGoalByEL( String pCrossELName,
			List< PixelPoint > pGoalPoints, int pGoalLayerNo )
	{
		// 目标建筑所在层规划后的路径
		Path[] goalResultPaths = null;

		// 设定目标建筑所在层的地图
		mBuildingDataTool.init( mMapFile[pGoalLayerNo] );
		List< PixelPoint > ELPoints = mBuildingDataTool
				.findBuildingByName( pCrossELName );
		
		// 得到路径
		goalResultPaths = sameLayerSearch( ELPoints, pGoalPoints );


		return goalResultPaths;
	}

	/**
	 * 
	 * isCrossLayer(判断是否跨层)
	 * 
	 * @param pStartPoint
	 * @param pGoalPoint
	 * @return true表示跨层，false表示没有跨层 boolean
	 * @exception
	 * @since 1.0.0
	 */
	private boolean isCrossLayer( PixelPoint pStartPoint, PixelPoint pGoalPoint )
	{
		return pStartPoint.getZ() != pGoalPoint.getZ();
	}

	/**
	 * 
	 * isInWall(判断点是否在二楼围墙之内)
	 * 
	 * @param pPoint
	 *            要判断的点
	 * @return true表示在围墙内，false表示在围墙外 boolean
	 * @exception
	 * @since 1.0.0
	 */
	private boolean isInWall( PixelPoint pPoint )
	{
		if ( pPoint.getIndex_X() >= 11 && pPoint.getIndex_X() <= 41 && pPoint.getIndex_Y() > 10
				&& pPoint.getIndex_Y() < 41 )
		{
			return true;
		}
		else if ( pPoint.getIndex_X() == 42 && pPoint.getIndex_Y() > 9
				&& pPoint.getIndex_Y() < 42 )
		{
			return true;
		}
		else if ( pPoint.getIndex_X() == 43 && pPoint.getIndex_Y() > 6
				&& pPoint.getIndex_Y() < 45 )
		{
			return true;
		}
		else if ( pPoint.getIndex_X() == 44 && pPoint.getIndex_Y() > 4
				&& pPoint.getIndex_Y() < 47 )
		{
			return true;
		}
		else if ( pPoint.getIndex_X() > 45 )
		{
			return true;
		}

		return false;
	}

	/**
	 * 
	 * fixPoint(修复像素坐标和数组下标的问题)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void fixPoint( PixelPoint pPixelPoint )
	{
		if ( pPixelPoint.getIndex_X() == 0 && pPixelPoint.getIndex_Y() == 0 )
		{
			if ( pPixelPoint.getX() > 50 || pPixelPoint.getY() > 50 )
			{
				pPixelPoint.CalculateIndexPos();
			}
			else
			{
				pPixelPoint.setIndex( pPixelPoint.getY(), pPixelPoint.getX() );
			}
		}
		else
		{
			pPixelPoint.CalculatePixelPos();
		}
	}

}
