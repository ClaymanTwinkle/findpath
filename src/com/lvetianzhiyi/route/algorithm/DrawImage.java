package com.lvetianzhiyi.route.algorithm;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;

import com.lvetianzhiyi.route.MainActivity;
import com.lvetianzhiyi.route.R;
import com.lvetianzhiyi.route.utils.Screen_info;
import com.lvetianzhiyi.route.view.LveTianMapView;

/**
 * 
 * 
 * DrawImage 用算法规划后的路径在图片上绘制出来的类
 * 
 * kesar kesar
 * 
 * 
 * @version 1.0.0
 * 
 */

public class DrawImage
{
	private Bitmap mBitmap[];
	private Paint mPaint_route;// 声明路线画笔
	private LveTianMapView mLveTianMapView;
	private float InitInch;
	private static Bitmap start;
	private static Bitmap end;
	private static Bitmap zhongtu;
	private Handler mHandler;

	/**
	 * 
	 * 创建一个新的实例 DrawImage.
	 * 
	 * 
	 */
	public DrawImage( Activity activity )
	{
		CreatePaint();
		mBitmap = new Bitmap[3];
		start = BitmapFactory.decodeResource( activity.getResources(),
				R.drawable.start );
		end = BitmapFactory.decodeResource( activity.getResources(),
				R.drawable.end );
		zhongtu = BitmapFactory.decodeResource( activity.getResources(),
				R.drawable.icon_through_node );
	}

	/**
	 * 
	 * init(初始化图)
	 * 
	 * @param pLveTianMapView
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void init( LveTianMapView pLveTianMapView, Handler handler )
	{
		InitInch = Screen_info.CaluculateInitInch();
		for ( int i = 0; i < mBitmap.length; i++ )
		{
			mBitmap[i] = Bitmap.createBitmap( Screen_info.getScreen_Width(),
					Screen_info.getScreen_Height(), Config.ARGB_8888 );
		}
		mLveTianMapView = pLveTianMapView;
		mHandler = handler;
	}

	private void CreatePaint()
	{
		float times = Screen_info.CaluculateInitInch() / 0.72f;
		if ( null != mPaint_route )
		{
			mPaint_route = null;
		}
		mPaint_route = new Paint( Paint.DITHER_FLAG );// 创建一个画笔
		mPaint_route.setTextSize( 35 * times );
		mPaint_route.setStyle( Paint.Style.STROKE );// 设置非填充
		mPaint_route.setStrokeWidth( 10 * times );// 笔宽5像素
		mPaint_route.setColor( Color.rgb( 34, 139, 34 ) );// 设置为绿色
		mPaint_route.setAntiAlias( true );// 锯齿不显示
		mPaint_route.setDither( true );// 设置图像抖动处理
		// mPaint_route.setAlpha( 255 );
		mPaint_route.setStrokeJoin( Paint.Join.ROUND );// 设置图像的结合方式
		mPaint_route.setStrokeCap( Paint.Cap.ROUND );// 设置画笔为圆形样式
	}

	/**
	 * 
	 * drawLineOnImage(在地图的图片上绘制规划后的路径)
	 * 
	 * @param pActivity
	 * @param pResultPaths
	 * @param moveInch
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void drawLineOnImage( Activity pActivity, List< Path > pResultPaths,
			float moveInch ,boolean isEnd)
	{
		// 判断结果路径集合是否为空
		if ( pResultPaths != null )
		{
			// 判断路径集是否为空
			if ( !pResultPaths.isEmpty() )
			{
				int all = 0;
				for ( Path resultPath : pResultPaths )
				{
					all += resultPath.getPathList().size();
				}
				int increase = 100 / all;
				for ( Path resultPath : pResultPaths )
				{
					Canvas canvas = new Canvas(
							mBitmap[resultPath.getZ_Index() + 1] );
					// 路径的坐标集合
					List< PixelPoint > resultList = resultPath.getPathList();
					// 遍历链表，绘制路径
					int length = resultList.size();
					for ( int i = 0; i < length; i++ )
					{
						float LveTianScale = mLveTianMapView.getScale();
						PixelPoint.SCALE = LveTianScale;
						PixelPoint StartPoint = resultList.get( i );
						StartPoint.CalculatePixelPos();
						DrawPoint s = Rectification( StartPoint, LveTianScale,
								moveInch );
						int limit = length - 1;
						if ( limit == 0 )
						{
							// 直接画终点
							if(isEnd)
							{
								canvas.drawBitmap( end,
										s.getX() - end.getWidth() / 2,
										s.getY() - end.getHeight(), null );
							}
							else
							{
								canvas.drawBitmap( zhongtu,
										s.getX() - zhongtu.getWidth() / 2,
										s.getY() - zhongtu.getHeight(), null );
							}
						}
						else
						{
							if ( i < limit )
							{
								PixelPoint EndPoint = resultList.get( i + 1 );
								EndPoint.CalculatePixelPos();
								// 以下开始繁琐的画线纠偏工作。。。
								DrawPoint e = Rectification( EndPoint,
										LveTianScale, moveInch );
								// 纠偏完毕！！！
								// 画线
								canvas.drawLine( s.getX(), s.getY(), e.getX(),
										e.getY(), mPaint_route );
								// 画终点
								if ( i == 0 )
								{
									if(isEnd)
									{
										canvas.drawBitmap( end,
												s.getX() - end.getWidth() / 2,
												s.getY() - end.getHeight(), null );
									}
									else
									{
										canvas.drawBitmap( zhongtu,
												s.getX() - zhongtu.getWidth() / 2,
												s.getY() - zhongtu.getHeight(), null );
									}
								}
							}
							else
							{
								canvas.drawBitmap( start,
										s.getX() - start.getWidth() / 2, s.getY()
												- start.getHeight(), null );
							}
						}
						Message msg = Message.obtain();
						msg.what = MainActivity.CONST_UPDATE_PROGRESS;
						msg.obj = increase;
						// 发送这个消息到消息队列中
						mHandler.sendMessage( msg );
					}
					canvas.save( Canvas.ALL_SAVE_FLAG );// 保存
				}
			}
			else
			{
				System.err.println( "链表为空" );
			}
		}
		else
		{
			System.err.println( "查不到路径或未知错误" );
		}
	}

//	/**
//	 * 
//	 * drawLineOnImage(在地图的图片上绘制规划后的路径)
//	 * 
//	 * @param mResultPaths
//	 *            路径 void
//	 * @exception
//	 * @since 1.0.0
//	 */
//	public void drawLineOnImage( Activity pActivity, Route[] routes,
//			float moveInch )
//	{
//		if ( routes != null )
//		{
//			for ( int i = 0; i < routes.length; i++ )
//			{
//				if ( routes[i] != null )
//				{
//					drawLineOnImage( pActivity, routes[i].getmResultPaths(),
//							moveInch );
//				}
//			}
//		}
//	}

	/**
	 * 
	 * Rectification(纠偏画线)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private DrawPoint Rectification( PixelPoint pPoint, float LveTianScale,
			float moveInch )
	{
		float MatrixRectFtop = mLveTianMapView.getMatrixRectF().top;
		float x = pPoint.getX();
		float y = pPoint.getY();
		float times = LveTianScale / InitInch;
		if ( mLveTianMapView.getmCurrentFloor() == 0 )
		{
			y = y - 312.5f * LveTianScale;
		}
		y = y + ( MatrixRectFtop - 15 ) * times + moveInch;
		x /= times;
		y /= times;

		return new DrawPoint( x, y );
	}

	/**
	 * @返回画好的透明路线图
	 */
	public Bitmap[] getmBitmap()
	{
		return mBitmap;
	}

}

/**
 * 
 * 
 * DrawPoint 封装x和y kesar kesar 2015-7-23 下午6:39:28
 * 
 * @version 1.0.0
 * 
 */
class DrawPoint
{
	private float mX;
	private float mY;

	public DrawPoint( float mX, float mY )
	{
		this.mX = mX;
		this.mY = mY;
	}

	public float getX()
	{
		return mX;
	}

	public void setX( float mX )
	{
		this.mX = mX;
	}

	public float getY()
	{
		return mY;
	}

	public void setY( float mY )
	{
		this.mY = mY;
	}

}
