package com.lvetianzhiyi.route.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lvetianzhiyi.route.utils.Screen_info;

/**
 * 
 * 类 IndicateArrow 用户位置坐标图标
 * 
 * @author xinyi
 * 
 * @继承 ImageView
 * 
 * @since 1.0.0
 */
public class IndicateArrow extends ImageView
{
	// 变换矩阵
	private Matrix mMatrix;
	// 图片文件
	private Bitmap mBitmap;
	// X 轴移动距离
	private float ScreenMove_X;
	// Y 轴移动距离
	private float ScreenMove_Y;

	/**
	 * IndicateArrow 用户位置坐标图标构造函数
	 * 
	 * @param Context
	 *            context 当前上下文
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	public IndicateArrow( Context context )
	{
		this( context, null );
	}

	/**
	 * IndicateArrow 用户位置坐标图标构造函数
	 * 
	 * @param Context
	 *            context 当前上下文
	 * 
	 * @param AttributeSet
	 *            attrs 属性
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	public IndicateArrow( Context context, AttributeSet attrs )
	{
		this( context, attrs, 0 );
	}

	/**
	 * IndicateArrow 用户位置坐标图标构造函数
	 * 
	 * @param Context
	 *            context 当前上下文
	 * 
	 * @param AttributeSet
	 *            attrs 属性
	 * 
	 * @param int defStyle 默认风格
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	public IndicateArrow( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
		setScaleType( ScaleType.MATRIX );
		mMatrix = new Matrix();
	}

	@ Override
	public void setImageBitmap( Bitmap bm )
	{
		super.setImageBitmap( bm );
		setImageMatrix( mMatrix );
		mBitmap = bm;
	}

	public Bitmap getImageBitmap()
	{
		return mBitmap;
	}

	@ Override
	public void setBackgroundResource( int resid )
	{
		super.setBackgroundResource( resid );
	}

	/**
	 * getMatrixRectF 获取当前图层的矩形区域
	 * 
	 * @param void
	 * @return 图层矩形
	 * @since 1.0.0
	 */
	public RectF getMatrixRectF()
	{
		Matrix matrix = mMatrix;
		RectF rectF = new RectF();

		Drawable drawable = getDrawable();
		if ( null != drawable )
		{
			rectF.set( 0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight() );
			matrix.mapRect( rectF );
		}
		return rectF;
	}

	public float getImageLeftPos()
	{
		return getMatrixRectF().left;
	}

	public float getImageRightPos()
	{
		return getMatrixRectF().right;
	}

	public float getImageTopPos()
	{
		return getMatrixRectF().top;
	}

	public float getImageBottomPos()
	{
		return getMatrixRectF().bottom;
	}

	/**
	 * CalculateScreen_move 计算光标移至屏幕中间的距离
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	public void CalculateScreen_move()
	{
		ScreenMove_X = ( Screen_info.getScreen_Width() - getDrawable()
				.getIntrinsicWidth() ) / 2;
		ScreenMove_Y = ( Screen_info.getScreen_Height() - getDrawable()
				.getIntrinsicHeight() ) / 2;
	}
	
	public void getImage_Matrix()
	{
		mMatrix.set( getImageMatrix() );
	}

	public void POST_SCALE( float sx, float sy, float px, float py )
	{
		mMatrix.postScale( sx, sy, px, py );
		setImageMatrix( mMatrix );
		invalidate();
	}
	
	public void POST_Translate( float dx, float dy )
	{
		mMatrix.postTranslate( dx, dy );
		setImageMatrix( mMatrix );
		invalidate();
	}

	public float getScreenMove_X()
	{
		return ScreenMove_X;
	}

	public float getScreenMove_Y()
	{
		return ScreenMove_Y;
	}

	/**
	 * CalculateScreen_move 光标初始化移动至屏幕中间
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	public void MoveToCenter()
	{
		mMatrix = getImageMatrix();
		// matrix.postRotate( 50 );
		// matrix.postTranslate( 200, 300 );
		mMatrix.postTranslate( ScreenMove_X, ScreenMove_Y );
		Bitmap bmp = Bitmap.createBitmap( mBitmap, 0, 0, mBitmap.getWidth(),
				mBitmap.getHeight(), mMatrix, true );
		setImageBitmap( bmp );
	}
};