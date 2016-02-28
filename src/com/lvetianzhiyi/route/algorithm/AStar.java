package com.lvetianzhiyi.route.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.lvetianzhiyi.route.data.OnlineData;
import com.lvetianzhiyi.route.utils.DealData;

public class AStar
{
	// 起始节点
	Node start;
	// 终止节点
	Node goal;

	// 开启队列，用于存放待处理的节点
	Queue< Node > openQueue = null;
	// 关闭队列，用于存放已经处理过的节点
	Queue< Node > closedQueue = null;

	// 起始节点到某个节点的距离
	int[][] FList = null;
	// 某个节点到目的节点的距离
	int[][] GList = null;
	// 起始节点经过某个节点到目的节点的距离
	int[][] HList = null;

	// 经过的总路程
	private static int Total_Distance;
	// 地图的行
	private int mRow;
	// 地图的列
	private int mCol;
	// 地图的二值化数据
	private Node mData[][];
	public byte[][][] mMapArray; // 地图二值化数组
	final static private int CONST_Horizontal = 50;
	final static private int CONST_Oblique = 71;
	public static final int RATIO = 20; // 数据和地图的比例
	private int mNo = 0; // 选择的地图号
	private boolean isOK = true;

	/**
	 * 打印行走路径
	 * 
	 * 经过的点用'#'表示, 未经过的点用'0'表示， 起始节点用'q'表示, 目的节点用'z'
	 */
	public void printPath()
	{
		Node father_point = null, temp_point;
		char[][] result = new char[mRow][mCol];
		for ( int i = 0; i < mRow; i++ )
		{
			for ( int j = 0; j < mCol; j++ )
			{
				result[i][j] = mData[i][j].getValue();
			}
		}

		@ SuppressWarnings( "unused" )
		int step = 0;
		father_point = mData[goal.getRowPos()][goal.getColPos()];
		temp_point = father_point;
		while ( father_point != null )
		{
			if ( ( Math.abs( father_point.getRowPos() - temp_point.getRowPos() ) == 1 )
					&& ( Math.abs( father_point.getColPos()
							- temp_point.getColPos() ) == 1 ) )
			{
				Total_Distance += 71;
			}
			else if ( father_point.getRowPos() == temp_point.getRowPos()
					&& father_point.getColPos() == temp_point.getColPos() )
			{
			}
			else
			{
				Total_Distance += 50;
			}
			if ( father_point.equals( start ) )
				result[father_point.getRowPos()][father_point.getColPos()] = 'q';
			else if ( father_point.equals( goal ) )
			{
				result[father_point.getRowPos()][father_point.getColPos()] = 'z';
				step++;
			}
			else
			{
				result[father_point.getRowPos()][father_point.getColPos()] = '#';
				step++;
			}
			temp_point = father_point;
			father_point = father_point.getFather();
		}

		for ( int i = 0; i < mRow; i++ )
		{
			for ( int j = 0; j < mCol; j++ )
			{
				////System.out.print( result[i][j] );
			}
			////System.out.println();
		}
		// 打印行走步数
		////System.out.println( "步数: " + step );
		////System.out.println( "总路程: " + Total_Distance );
	}

	public Path getResultPath()
	{
		if ( isOK )
		{
			Path resultPath = new Path();
			List< PixelPoint > points = new ArrayList< PixelPoint >();

			Node father_point = null, temp_point;
			
			@ SuppressWarnings( "unused" )
			int step = 0;
			father_point = mData[goal.getRowPos()][goal.getColPos()];
			temp_point = father_point;
			while ( father_point != null )
			{
				if ( ( Math.abs( father_point.getRowPos()
						- temp_point.getRowPos() ) == 1 )
						&& ( Math.abs( father_point.getColPos()
								- temp_point.getColPos() ) == 1 ) )
				{
					Total_Distance += 71;
				}
				else if ( father_point.getRowPos() == temp_point.getRowPos()
						&& father_point.getColPos() == temp_point.getColPos() )
				{
				}
				else
				{
					Total_Distance += 50;
				}
				PixelPoint pixelPoint = father_point.getPixelPoint();
				pixelPoint.setZ( mNo - 1 );
				if ( father_point.equals( start ) )
				{
					resultPath.setStartPoint( pixelPoint );
				}
				else if ( father_point.equals( goal ) )
				{
					resultPath.setGoalPoint( pixelPoint );
					step++;
				}
				else
				{
					step++;
				}
				points.add( pixelPoint );
				temp_point = father_point;
				father_point = father_point.getFather();
			}

			// 打印行走步数
			////System.out.println( "步数: " + step );
			////System.out.println( "总路程: " + Total_Distance );
			
			resultPath.setPathList( points );
			resultPath.setZ_Index( mNo - 1 );
			resultPath.setCost( Total_Distance );
			//printPath();

			if ( resultPath.getStartPoint() != null )
			{
				if ( resultPath.getGoalPoint() == null )
				{
					resultPath.setGoalPoint( resultPath.getStartPoint() );
				}
				resultPath.setmDirectedPath(DealData.CalcDirectedPath(points));
				return resultPath;
			}
		}
		return null;
	}

	/**
	 * 构造函数
	 * 
	 */
	public AStar()
	{
		mMapArray=OnlineData.mMapArray;
	}

	/**
	 * 
	 * init(初始化A星算法中的成员变量)
	 * 
	 * @param pMap
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void changeMap( int pNo )
	{
		mNo = pNo;
		Total_Distance = 0;
		GetMapInfo MapInfo = new GetMapInfo( mMapArray[pNo] );
		this.mData = MapInfo.getmData();
		this.mRow = ( mData != null ? mData.length : 0 );
		this.mCol = ( mData != null ? mData[0].length : 0 );

		// // 测试用的起点
		// this.start = mData[2][1];
		// //this.start = mData[41][3];
		// this.goal = mData[42][46];

		openQueue = new LinkedList< Node >();
		closedQueue = new LinkedList< Node >();

		FList = new int[mRow][mCol];
		GList = new int[mRow][mCol];
		HList = new int[mRow][mCol];

		for ( int i = 0; i < mData.length; i++ )
		{
			for ( int j = 0; j < mData[0].length; j++ )
			{
				FList[i][j] = Integer.MAX_VALUE;
				GList[i][j] = Integer.MAX_VALUE;
				HList[i][j] = Integer.MAX_VALUE;
			}
		}

	}

	/*
	 * 初始化
	 * 
	 * 将起始节点添加至开启列表，初始化： 1) 起始节点到当前节点（起始节点）的距离 2) 当前节点（起始节点）到目的节点的距离 3)
	 * 起始节点经过当前节点（起始节点）到目的节点的距离
	 */
	private void init()
	{
		openQueue.offer( start );
		int start_x = start.getX();
		int start_y = start.getY();
		int goal_x = goal.getX();
		int goal_y = goal.getY();

		int start_row = start.getRowPos();
		int start_col = start.getColPos();

		// 起始节点到当前节点的距离
		GList[start_row][start_col] = 0;
		// 当前节点到目的节点的距离
		HList[start_row][start_col] = getDistance( start_x, start_y, goal_x,
				goal_y );
		// f(x) = g(x) + h(x)
		FList[start_row][start_col] = GList[start_row][start_col]
				+ HList[start_row][start_col];
	}

	/*
	 * 启动搜索迷宫过程主入口
	 * 
	 * 从开启列表中搜索F值最小（即：起始节点 经过某一节点 到目的节点 距离最短），
	 * 将选取的节点作为当前节点，并更新当前节点的邻居节点信息（G、H、F值）以及 开启列表与关闭列表的成员。
	 */
	public void start( PixelPoint pStartPoint, PixelPoint pGoalPoint )
	{
		isOK = true;
		if ( !isExistInMap( pStartPoint ) || !isExistInMap( pGoalPoint ) )
		{
			isOK = false;
			return;
		}

		this.start = mData[pStartPoint.getIndex_X()][pStartPoint.getIndex_Y()];
		this.goal = mData[pGoalPoint.getIndex_X()][pGoalPoint.getIndex_Y()];

		init();

		Node currentPoint;

		while ( ( currentPoint = findShortestFPoint() ) != null )
		{
			if ( currentPoint.getX() == goal.getX() && currentPoint.getY() == goal.getY() )
			{
				return;
			}

			updateNeighborPoints( currentPoint );
		}
	}

	/*
	 * 检查位置是否有效
	 * 
	 * 如果当前位置存在、不是墙，且不在关闭列表中，则返回"true"，表示为有效位置； 否则，返回"false"。
	 * 
	 * 输入： 待检查位置的横坐标值 待检查位置的纵坐标值
	 * 
	 * 输出： 是否有效
	 */
	private boolean checkPosValid( int x, int y )
	{
		// 检查x,y是否越界， 并且当前节点不是墙
		if ( ( x >= 0 && x < mData.length ) && ( y >= 0 && y < mData[0].length )
				&& ( mData[x][y].getValue() != '1' ) )
		{
			// 检查当前节点是否已在关闭队列中，若存在，则返回 "false"
			for(Node point:closedQueue)
			{
				if ( point.getRowPos() == x && point.getColPos() == y )
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/*
	 * 获取当前位置到目的位置的距离
	 * 
	 * 距离衡量规则： 横向移动一格或纵向移动一格的距离为1.
	 * 
	 * 输入： 当前位置的横坐标值 当前位置的纵坐标值 目的位置的横坐标值 目的位置的纵坐标值
	 * 
	 * 输出： 当前位置到目的位置的距离
	 */
	private int getDistance( int current_x, int current_y, int goal_x,
			int goal_y )
	{
		return Math.abs( current_x - goal.getX() )
				+ Math.abs( current_y - goal.getY() );
	}

	/*
	 * 找寻最短路径所经过的节点
	 * 
	 * 从开启列表中找寻F值最小的节点，将其从开启列表中移除，并置入关闭列表。
	 * 
	 * 输出：最短路径所经过的节点
	 */
	private Node findShortestFPoint()
	{
		Node shortestFPoint = null;
		int shortestFValue = Integer.MAX_VALUE;

		for(Node currentPoint:openQueue)
		{
			if ( FList[currentPoint.getRowPos()][currentPoint.getColPos()] <= shortestFValue )
			{
				shortestFPoint = currentPoint;
				shortestFValue = FList[currentPoint.getRowPos()][currentPoint
						.getColPos()];
			}
		}

		if ( shortestFValue != Integer.MAX_VALUE )
		{
			closedQueue.offer( shortestFPoint );
			openQueue.remove( shortestFPoint );
		}

		return shortestFPoint;
	}

	/*
	 * 更新邻居节点
	 * 
	 * 依次判断上、下、左、右方向的邻居节点，如果邻居节点有效，则更新距离矢量表。
	 * 
	 * 输入： 当前节点
	 */
	private void updateNeighborPoints( Node currentPoint )
	{
		int current_row = currentPoint.getRowPos();
		int current_col = currentPoint.getColPos();

		// 上
		if ( checkPosValid( current_row - 1, current_col ) )
		{
			updatePoint( mData[current_row][current_col],
					mData[current_row - 1][current_col], CONST_Horizontal );
		}
		// 下
		if ( checkPosValid( current_row + 1, current_col ) )
		{
			updatePoint( mData[current_row][current_col],
					mData[current_row + 1][current_col], CONST_Horizontal );
		}
		// 左
		if ( checkPosValid( current_row, current_col - 1 ) )
		{
			updatePoint( mData[current_row][current_col],
					mData[current_row][current_col - 1], CONST_Horizontal );
		}
		// 右
		if ( checkPosValid( current_row, current_col + 1 ) )
		{
			updatePoint( mData[current_row][current_col],
					mData[current_row][current_col + 1], CONST_Horizontal );
		}
		// 左上
		if ( checkPosValid( current_row - 1, current_col - 1 )
				&& mData[current_row - 1][current_col].getValue() != '1'
				&& mData[current_row][current_col - 1].getValue() != '1' )
		{
			updatePoint( mData[current_row][current_col],
					mData[current_row - 1][current_col - 1], CONST_Oblique );
		}
		// 右上
		if ( checkPosValid( current_row - 1, current_col + 1 )
				&& mData[current_row - 1][current_col].getValue() != '1'
				&& mData[current_row][current_col + 1].getValue() != '1' )
		{
			updatePoint( mData[current_row][current_col],
					mData[current_row - 1][current_col + 1], CONST_Oblique );
		}
		// 左下
		if ( checkPosValid( current_row + 1, current_col - 1 )
				&& mData[current_row + 1][current_col].getValue() != '1'
				&& mData[current_row][current_col - 1].getValue() != '1' )
		{
			updatePoint( mData[current_row][current_col],
					mData[current_row + 1][current_col - 1], CONST_Oblique );
		}
		// 右下
		if ( checkPosValid( current_row + 1, current_col + 1 )
				&& mData[current_row + 1][current_col].getValue() != '1'
				&& mData[current_row][current_col + 1].getValue() != '1' )
		{
			updatePoint( mData[current_row][current_col],
					mData[current_row + 1][current_col + 1], CONST_Oblique );
		}
	}

	/*
	 * 更新节点
	 * 
	 * 依次计算：1) 起始节点到当前节点的距离; 2) 当前节点到目的位置的距离; 3) 起始节点经过当前节点到目的位置的距离
	 * 如果当前节点在开启列表中不存在，则：置入开启列表，并且“设置”1)/2)/3)值； 否则，判断 从起始节点、经过上一节点到当前节点、至目的地的距离
	 * < 上一次记录的从起始节点、到当前节点、至目的地的距离， 如果有更短路径，则更新1)/2)/3)值
	 * 
	 * 输入： 上一跳节点（又：父节点） 当前节点
	 */
	private void updatePoint( Node lastPoint, Node currentPoint,
			int MoveDistance )
	{
		int last_row = lastPoint.getRowPos();
		int last_col = lastPoint.getColPos();
		int current_row = currentPoint.getRowPos();
		int current_col = currentPoint.getColPos();

		// 起始节点到当前节点的距离
		int temp_g = GList[last_row][last_col] + MoveDistance;
		// 当前节点到目的位置的距离
		int temp_h = getDistance( currentPoint.getX(), currentPoint.getY(),
				goal.getX(), goal.getY() );
		// f(x) = g(x) + h(x)
		int temp_f = temp_g + temp_h;

		// 如果当前节点在开启列表中不存在，则：置入开启列表，并且“设置”
		// 1) 起始节点到当前节点距离
		// 2) 当前节点到目的节点的距离
		// 3) 起始节点到目的节点距离
		if ( !openQueue.contains( currentPoint ) )
		{
			openQueue.offer( currentPoint );
			currentPoint.setFather( lastPoint );

			// 起始节点到当前节点的距离
			GList[current_row][current_col] = temp_g;
			// 当前节点到目的节点的距离
			HList[current_row][current_col] = temp_h;
			// f(x) = g(x) + h(x)
			FList[current_row][current_col] = temp_f;
		}
		else
		{
			// 如果当前节点在开启列表中存在，并且，
			// 从起始节点、经过上一节点到当前节点、至目的地的距离 < 上一次记录的从起始节点、到当前节点、至目的地的距离，
			// 则：“更新”
			// 1) 起始节点到当前节点距离
			// 2) 当前节点到目的节点的距离
			// 3) 起始节点到目的节点距离
			if ( temp_f < FList[current_row][current_col] )
			{
				// 起始节点到当前节点的距离
				GList[current_row][current_col] = temp_g;
				// 当前节点到目的位置的距离
				HList[current_row][current_col] = temp_h;
				// f(x) = g(x) + h(x)
				FList[current_row][current_col] = temp_f;
				// 更新当前节点的父节点
				currentPoint.setFather( lastPoint );
			}
		}
	}

	/**
	 * @return total_Distance
	 */
	public static int getTotal_Distance()
	{
		return Total_Distance;
	}
	
	public static void initTotal_Distance()
	{
		Total_Distance = 0;
	}

	/**
	 * @return mRow
	 */
	public int getmRow()
	{
		return mRow;
	}

	/**
	 * @return mCol
	 */
	public int getmCol()
	{
		return mCol;
	}

	/**
	 * @return mData
	 */
	public Node[][] getmData()
	{
		return mData;
	}

	/**
	 * 
	 * isExistInMap(判断坐标是否在地图中)
	 * 
	 * @param pPoint
	 * @return true 表示坐标在地图中，false表示坐标不在地图中 boolean
	 * @exception
	 * @since 1.0.0
	 */
	private boolean isExistInMap( PixelPoint pPoint )
	{
		if ( pPoint == null )
		{
			return false;
		}
		if ( pPoint.getIndex_X() >= mMapArray[mNo].length
				|| mData[pPoint.getIndex_X()][pPoint.getIndex_Y()].getValue() == '1' )
		{
			return false;
		}
		return true;
	}

	/**
	 * 
	 * getMapArray(得到地图)
	 * 
	 * @return byte[][]
	 * @exception
	 * @since 1.0.0
	 */
	public byte[][] getMapArray()
	{
		return mMapArray[mNo];
	}
}