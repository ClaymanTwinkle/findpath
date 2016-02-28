package com.lvetianzhiyi.route.utils;

import java.util.List;

import android.graphics.Matrix;

import com.lvetianzhiyi.route.algorithm.DirectedPath;
import com.lvetianzhiyi.route.algorithm.PixelPoint;
import com.lvetianzhiyi.route.type.Direction;
import com.lvetianzhiyi.route.view.IndicateArrow;
import com.lvetianzhiyi.route.view.LveTianMapView;

public class DealData
{
	public static float[] get3data( String point )
	{
		float data[] = new float[3];
		// 获取 x, y 坐标及层数
		data[0] = ( Float.parseFloat( point.split( "," )[0].substring( 1 ) ) - 0.02f ) / 0.96f;

		String tempLayerString = point.split( "," )[2];
		data[2] = Float.parseFloat( tempLayerString.substring( 0,
				tempLayerString.length() - 1 ) );
		int temp = (int) data[2];
		if ( temp == 0 )
		{
			data[1] = ( Float.parseFloat( point.split( "," )[1] ) - 0.02f ) / 0.36f * 0.375f;
		}
		else
		{
			data[1] = ( Float.parseFloat( point.split( "," )[1] ) - 0.02f ) / 0.96f;
		}

		return data;
	}

	/**
	 * 
	 * moveIndicator(移动光标)
	 *
	 * @param lveTianMapView
	 * @param indicateArrow
	 * @param ox
	 * @param oy
	 * @param layer
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public static void moveIndicator( LveTianMapView lveTianMapView,
			IndicateArrow indicateArrow, float ox, float oy, int layer )
	{
		float scale = lveTianMapView.getScale();
		float left = lveTianMapView.getMatrixRectF().left;
		float top = lveTianMapView.getMatrixRectF().top;
		float x = ox * 1000 * scale + left;
		float y = 0;
		if ( layer == 0 )
		{
			y = ( 0.375f - oy ) * 1000 * scale + top;
		}
		else
		{
			y = ( 1 - oy ) * 1000 * scale + top;
		}
		Matrix matrix = indicateArrow.getImageMatrix();
		float Indicate_x = ( indicateArrow.getMatrixRectF().right - indicateArrow
				.getMatrixRectF().left )
				/ 2.0f
				+ indicateArrow.getMatrixRectF().left;
		float Indicate_y = indicateArrow.getMatrixRectF().bottom;
		matrix.postTranslate( x - Indicate_x, y - Indicate_y );
		indicateArrow.setImageMatrix( matrix );
		indicateArrow.invalidate();
	}

	/**
	 * 
	 * CalcDirectedPath(计算出有向路径)
	 * 
	 * @param pPoints
	 * @return Queue<DirectedPath>
	 * @exception
	 * @since 1.0.0
	 */
	public static DirectedPath CalcDirectedPath( List< PixelPoint > pPoints )
	{
		DirectedPath first = null;
		// 判空操作
		if ( pPoints != null && !pPoints.isEmpty() )
		{
			int size = pPoints.size();
			DirectedPath present = new DirectedPath();
			DirectedPath father = null;
			first = present;
			for ( int i = size - 1; i > -1; i-- )
			{
				PixelPoint p = pPoints.get( i );
				if ( present.isEmpty() || present.size() == 1
						|| isInLine( present, p ) )
				{
					present.AddPoint( p );
					if ( i == 0 )
					{
						// 计算代价
						present.CalcCost();
						// 添加到对列中
						if ( father != null )
						{
							father.setmNextDirectedPath( present );
						}
						father = present;
					}
				}
				else
				{
					// 计算代价
					present.CalcCost();
					// 添加到对列中
					if ( father != null )
					{
						father.setmNextDirectedPath( present );
					}
					father = present;
					if ( i != 0 )
					{
						present = new DirectedPath();
					}
				}
			}

		}
		return first;
	}

	/**
	 * 
	 * isInLine(判断是否在同一直线上)
	 * 
	 * @param pGroup
	 * @param pPoint
	 * @return boolean
	 * @exception
	 * @since 1.0.0
	 */
	private static boolean isInLine( DirectedPath pGroup, PixelPoint pPoint )
	{
		if ( pGroup.size() == 1 || pGroup.getmDirection() == null )
		{
			pGroup.setmDirection( CalcDirection( pGroup.getmStartPoint(),
					pPoint ) );
			return true;
		}
		if ( pGroup.size() > 1 )
		{
			if ( pGroup.getmDirection() != CalcDirection(
					pGroup.getmStartPoint(), pPoint ) )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * TOEL(做电梯？)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	public static void TOEL( DirectedPath pGroup1, DirectedPath pGroup2 )
	{
		if ( pGroup1.getmLayerNo() != pGroup2.getmLayerNo() )
		{
			switch ( pGroup2.getmLayerNo() )
			{
				case 0 :
					pGroup1.setmPathEL( Direction.TO0 );

					break;
				case 1 :
					pGroup1.setmPathEL( Direction.TO1 );
					break;
				case 2 :
					pGroup1.setmPathEL( Direction.TO2 );
					break;
			}
		}
	}

	/**
	 * 
	 * CalcDirection(计算两点方向)
	 * 
	 * @param pSPoint
	 * @param pEPoint
	 * @return Direction
	 * @exception
	 * @since 1.0.0
	 */
	private static Direction CalcDirection( PixelPoint pSPoint,
			PixelPoint pEPoint )
	{
		// 判断垂直方向
		if ( pSPoint.getIndex_X() == pEPoint.getIndex_X() )
		{// 等
			// 判断水平方向
			if ( pSPoint.getIndex_Y() == pEPoint.getIndex_Y() )
			{// 同位置
				return null;
			}
			else if ( pSPoint.getIndex_Y() < pEPoint.getIndex_Y() )
			{// 右
				return Direction.RIGHT;
			}
			else
			{// 左
				return Direction.LEFT;
			}
		}
		else if ( pSPoint.getIndex_X() > pEPoint.getIndex_X() )
		{// 上
			// 判断水平方向
			if ( pSPoint.getIndex_Y() == pEPoint.getIndex_Y() )
			{// 同位置
				return Direction.UP;
			}
			else if ( pSPoint.getIndex_Y() < pEPoint.getIndex_Y() )
			{// 右
				return Direction.RIGHTUP;
			}
			else
			{// 左
				return Direction.LEFTUP;
			}
		}
		else
		{// 下
			// 判断水平方向
			if ( pSPoint.getIndex_Y() == pEPoint.getIndex_Y() )
			{// 同位置
				return Direction.DOWN;
			}
			else if ( pSPoint.getIndex_Y() < pEPoint.getIndex_Y() )
			{// 右
				return Direction.RIGHTDOWN;
			}
			else
			{// 左
				return Direction.LEFTDOWN;
			}
		}
	}

	/**
	 * 
	 * SpeakDirection(说话时的方向指示)
	 * 
	 * @param d
	 * @return String
	 * @exception
	 * @since 1.0.0
	 */
	public static String SpeakDirection( DirectedPath dp )
	{
		String result = "";
		switch ( dp.getmDirection() )
		{
			case LEFT :
				result += "向左走";
				break;
			case RIGHT :
				result += "向右走";
				break;
			case UP :
				result += "向上走";
				break;
			case DOWN :
				result += "向下走";
				break;
			case LEFTUP :
				result += "向左上走";
				break;
			case RIGHTUP :
				result += "向右上走";
				break;
			case LEFTDOWN :
				result += "向左下走";
				break;
			case RIGHTDOWN :
				result += "向右下走";
				break;
			default :
				break;
		}
		if ( dp.getmPathEL() != null )
		{
			switch ( dp.getmDirection() )
			{
				case TO0 :
					result += ",然后做电梯到地下层";
					break;
				case TO1 :
					result += ",然后做电梯到第一层";
					break;
				case TO2 :
					result += ",然后做电梯到第二层";
					break;
				default :
					break;
			}
		}
		return result;
	}
};