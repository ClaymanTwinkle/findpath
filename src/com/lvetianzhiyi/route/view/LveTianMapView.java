package com.lvetianzhiyi.route.view;

import com.lvetianzhiyi.route.algorithm.PixelPoint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * 类 LveTianMapView 地图图层视图
 * 
 * @author xinyi
 * 
 * @继承 ImageView
 * @接口 OnGlobalLayoutListener
 * @接口 OnScaleGestureListener
 * @接口 OnTouchListener
 * 
 * @since 1.0.0
 */
public class LveTianMapView extends ImageView implements
		OnGlobalLayoutListener, OnScaleGestureListener, OnTouchListener
{
	/*
	 * /////////////////////////////////// 成员变量定义
	 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	 */
	// 当前层
	private int mCurrentFloor;
	// 初始化执行一次的标识
	private boolean mOnce;
	// 初始化时缩放的值
	private float mInitScale;
	// 双击放大的值
	private float mMidScale;
	// 放大的最大值
	private float mMaxScale;
	// 变换矩阵
	private Matrix mScaleMatrix;
	// 手势检测
	private ScaleGestureDetector mScaleGestureDetector;
	// 上一次触碰时的手指个数
	private int mLastPointerCount;
	// 上一次点击的 X，Y
	private float mLastX, mLastY;
	// 是否可拖拉状态
	private int mTouchSlop;
	// 是否可拖拉的标识
	private boolean isCanDrag;
	// 是否检测过左右边界
	private boolean isCheckLeftAndRight;
	// 是否检测过上下边界
	private boolean isCheckTopAndBottom;
	// 手势控制
	private GestureDetector mGestureDetector;
	// 是否自动放缩
	private boolean isAutoScale;
	// 运行的 Activity
	private Activity mActivity;
	// 点击的点
	private PixelPoint mPic_PixelPoint;
	// 编辑框
	static private EditText mEditText;
	// 路径规划的标识
	private boolean ifStartRoutePlan;
	// 路线视图层
	private SurfaceViewDraw mCurrentSurfaceView;
	// 用户光标
	private IndicateArrow mIndicateArrow;

	/*
	 * /////////////////////////////////// 方法定义
	 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	 */
	/**
	 * LveTianMapView 图层视图构造函数
	 * 
	 * @param Context
	 *            context 当前上下文
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	public LveTianMapView( Context context )
	{
		this( context, null );
	}

	/**
	 * LveTianMapView 图层视图构造函数
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
	public LveTianMapView( Context context, AttributeSet attrs )
	{
		this( context, attrs, 0 );
	}

	/**
	 * LveTianMapView 图层视图构造函数
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
	@ SuppressLint( "ClickableViewAccessibility" )
	public LveTianMapView( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
		mOnce = false;// 设置初始化执行一次
		mCurrentFloor = 1;// 当前层为1
		ifStartRoutePlan = false;
		// temp_IfDoubleClick = new ifDoubleClick( false );
		mScaleMatrix = new Matrix();
		mCurrentSurfaceView = null;
		mScaleGestureDetector = new ScaleGestureDetector( context, this );
		setOnTouchListener( this );
		setScaleType( ScaleType.MATRIX );// 设置矩阵变换
		mTouchSlop = ViewConfiguration.get( context ).getScaledTouchSlop();
		mPic_PixelPoint = new PixelPoint();
		// 设置手势监听器
		mGestureDetector = new GestureDetector( context,
				new GestureDetector.SimpleOnGestureListener()
				{
					@ Override
					public boolean onDoubleTap( MotionEvent e )
					{
						if ( isAutoScale )
						{
							return true;
						}

						float x = e.getX();
						float y = e.getY();

						if ( getScale() < mMidScale )
						{
							// 执行双击放大
							postDelayed( new AutoScaleRunnable( mMidScale, x,
									y, true ), 16 );
							isAutoScale = true;
						}
						else
						{
							// 执行双击缩小
							postDelayed( new AutoScaleRunnable( mInitScale, x,
									y, true ), 16 );
							isAutoScale = true;
						}
						return true;
					}
				} );
	}

	/**
	 * getmCurrentFloor 获取当前层
	 * 
	 * @param void
	 * @return int 层数( 取值范围:0,1,2 )
	 * @since 1.0.0
	 */
	public int getmCurrentFloor()
	{
		return mCurrentFloor;
	}

	/**
	 * setmCurrentFloor 设置当前层
	 * 
	 * @param int 层数( 取值范围:0,1,2 )
	 * @return void
	 * @since 1.0.0
	 */
	public void setmCurrentFloor( int mCurrentFloor )
	{
		this.mCurrentFloor = mCurrentFloor;
	}

	public boolean getIfStartRoutePlan()
	{
		return ifStartRoutePlan;
	}

	public SurfaceViewDraw getmCurrentSurfaceView()
	{
		return mCurrentSurfaceView;
	}

	public void setmIndicateArrow( IndicateArrow mIndicateArrow )
	{
		this.mIndicateArrow = mIndicateArrow;
	}

	/**
	 * setSurfaceView 设置当前的SurfaceView跟随LveTianMapView变化
	 * 
	 * @param pSurfaceViewDrawRoute
	 *            当前的SurfaceView
	 * @return void
	 * @since 1.0.0
	 */
	public void setSurfaceView( SurfaceViewDraw pSurfaceViewDrawRoute )
	{
		this.mCurrentSurfaceView = pSurfaceViewDrawRoute;
	}

	@ Override
	public void setImageBitmap( Bitmap pBitmap )
	{
		super.setImageBitmap( pBitmap );
		CheckBorderAndCenterWhenScale();
		setImageMatrix( mScaleMatrix );
	}

	/**
	 * setEdittext 设置当前的Edittext
	 * 
	 * @param Edittext
	 *            输入框
	 * @return void
	 * @since 1.0.0
	 */
	public void setEdittext( EditText pEditText )
	{
		LveTianMapView.mEditText = pEditText;
	}

	/**
	 * setCurrentActivity 设置当前的Activity
	 * 
	 * @param Activity
	 *            当前Activity
	 * @return void
	 * @since 1.0.0
	 */
	public void setCurrentActivity( Activity pActivity )
	{
		this.mActivity = pActivity;
	}

	/**
	 * setEnlarge 设置动作为放大或缩小
	 * 
	 * @param boolean 放大标识 true 放大 false 缩小
	 * @return void
	 * @since 1.0.0
	 */
	public void setEnlarge( boolean pFlag )
	{
		float x = getWidth() / 2;
		float y = getHeight() / 2;
		if ( pFlag )
		{
			if ( getScale() < mMaxScale )
			{
				postDelayed( new AutoScaleRunnable( mMaxScale, x, y, false ), 0 );
			}
			else
			{
				Toast.makeText( mActivity, "已放大至最高级别", Toast.LENGTH_SHORT )
						.show();
			}
		}
		else
		{
			if ( getScale() <= mInitScale )
			{
				Toast.makeText( mActivity, "已缩小至最低级别", Toast.LENGTH_SHORT )
						.show();
			}
			else
			{
				postDelayed( new AutoScaleRunnable( mInitScale, x, y, false ),
						0 );
			}
		}
	}

	/**
	 * 
	 * 内部类 AutoScaleRunnable 自动放缩类
	 * 
	 * @author xinyi
	 * 
	 * @接口 Runnable
	 * 
	 * @since 1.0.0
	 */
	public class AutoScaleRunnable implements Runnable
	{
		// 缩放目标倍数
		private float mTargetScale;
		// 变化 x, y
		private float x, y;
		// 临时放缩倍数
		private float tmpScale;
		// 是否继续放缩标识
		private boolean ifContinue;
		// 放缩标识
		private boolean mFlag = true;
		// 放大默认倍数
		final static private float BIGGER = 1.07f;
		// 缩小默认倍数
		final static private float SMALL = 0.93f;

		/**
		 * AutoScaleRunnable 自动放缩构造函数
		 * 
		 * @param float pTargetScale 目标倍数
		 * @param float x 变化 x
		 * @param float y 变化 y
		 * @param boolean pIfContinue 是否继续标识
		 * @return void
		 * @since 1.0.0
		 */
		public AutoScaleRunnable( float pTargetScale, float x, float y,
				boolean pIfContinue )
		{
			this.mTargetScale = pTargetScale;
			this.x = x;
			this.y = y;
			this.ifContinue = pIfContinue;

			if ( getScale() < mTargetScale )
			{
				tmpScale = BIGGER;
			}
			else if ( getScale() >= mTargetScale )
			{
				tmpScale = SMALL;
			}
		}

		/**
		 * run 线程执行内容
		 * 
		 * @param float void
		 * @return void
		 * @since 1.0.0
		 */
		@ Override
		public void run()
		{
			if ( mFlag )
			{
				// 平移矩阵
				mScaleMatrix.postScale( tmpScale, tmpScale, x, y );
				mIndicateArrow.getImage_Matrix();
				mIndicateArrow.POST_SCALE( tmpScale, tmpScale, x, y );
				// 路线同时平移
				mCurrentSurfaceView.getImage_Matrix();
				mCurrentSurfaceView.POST_scale( tmpScale, tmpScale, x, y );
				CheckBorderAndCenterWhenScale();
				// 设置操作后的矩阵
				setImageMatrix( mScaleMatrix );

				float currrentScale = getScale();

				if ( ( tmpScale > 1.0f && currrentScale < mTargetScale )
						|| ( tmpScale < 1.0f && currrentScale > mTargetScale ) )
				{
					postDelayed( this, 0 );
				}
				else
				{
					float scale = mTargetScale / currrentScale;
					mScaleMatrix.postScale( scale, scale, x, y );
					mIndicateArrow.getImage_Matrix();
					mIndicateArrow.POST_SCALE( scale, scale, x, y );
					mCurrentSurfaceView.getImage_Matrix();
					mCurrentSurfaceView.POST_scale( scale, scale, x, y );
					CheckBorderAndCenterWhenScale();
					setImageMatrix( mScaleMatrix );
					if ( ifContinue )
						isAutoScale = false;
				}
				if ( !ifContinue )
					mFlag = false;
			}
		}
	}

	/**
	 * onGlobalLayout 初始化地图视图完成后执行一次，将图标放置在中间
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	@ Override
	public void onGlobalLayout()
	{
		if ( !mOnce )
		{
			int width = getWidth();// 获取视图宽
			int height = getHeight();// 获取视图高

			Drawable drawable = getDrawable();
			if ( null == drawable )
			{
				return;
			}

			int dw = drawable.getIntrinsicWidth();// 获取图片宽
			int dh = drawable.getIntrinsicHeight();// 获取图片高

			float scale = 1.0f;

			if ( dw > width && dh < height )
			{
				scale = width * 1.0f / dw;
			}
			else if ( dw < width && dh > height )
			{
				scale = height * 1.0f / dh;
			}
			else if ( ( dw < width && dh < height )
					|| ( dw > width && dh > height ) )
			{
				scale = Math.min( width * 1.0f / dw, height * 1.0f / dh );
			}

			mInitScale = scale;
			mMaxScale = mInitScale * 4;
			mMidScale = mInitScale * 2;

			int dx = ( getWidth() / 2 - dw / 2 );
			int dy = ( getHeight() / 2 - dh / 2 );

			mScaleMatrix.postTranslate( dx, dy );
			mScaleMatrix.postScale( mInitScale, mInitScale, width / 2,
					height / 2 );
			setImageMatrix( mScaleMatrix );

			mOnce = true;
		}
	}

	@ Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener( this );
	}

	@ SuppressWarnings( "deprecation" )
	@ Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
		getViewTreeObserver().removeGlobalOnLayoutListener( this );
	}

	/**
	 * getScale 获取当前地图的放大倍数
	 * 
	 * @param void
	 * @return float 倍数
	 * @since 1.0.0
	 */
	public float getScale()
	{
		float values[] = new float[9];
		mScaleMatrix.getValues( values );
		return values[Matrix.MSCALE_X];
	}

	/**
	 * onScale 放大情况
	 * 
	 * @param ScaleGestureDetector
	 *            detector 手势检测器
	 * @return boolean 是否正在执行
	 * @since 1.0.0
	 */
	@ Override
	public boolean onScale( ScaleGestureDetector detector )
	{
		float scale = getScale();
		float scaleFactor = detector.getScaleFactor();

		if ( null == getDrawable() )
		{
			return true;
		}

		if ( ( scale < mMaxScale && scaleFactor > 1.0f )
				|| ( scale > mInitScale && scaleFactor < 1.0f ) )
		{
			if ( scale * scaleFactor < mInitScale )
			{
				scaleFactor = mInitScale / scale;
			}
			else if ( scale * scaleFactor < mMaxScale )
			{
				scale = mMaxScale / scale;
			}
			mScaleMatrix.postScale( scaleFactor, scaleFactor,
					detector.getFocusX(), detector.getFocusY() );
			mIndicateArrow.getImage_Matrix();
			mIndicateArrow.POST_SCALE( scaleFactor, scaleFactor,
					detector.getFocusX(), detector.getFocusY() );
			mCurrentSurfaceView.getImage_Matrix();
			mCurrentSurfaceView.POST_scale( scaleFactor, scaleFactor,
					detector.getFocusX(), detector.getFocusY() );

			CheckBorderAndCenterWhenScale();
			setImageMatrix( mScaleMatrix );
		}
		return true;
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
		Matrix matrix = mScaleMatrix;
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

	/**
	 * CheckBorderAndCenterWhenScale 检测是否超边界
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	private void CheckBorderAndCenterWhenScale()
	{
		RectF rect = getMatrixRectF();

		float deltaX = 0.0f;
		float deltaY = 0.0f;

		int width = getWidth();
		int height = getHeight();

		if ( rect.width() >= width )
		{
			if ( rect.left > 0 )
			{
				deltaX = -rect.left;
			}
			if ( rect.right < width )
			{
				deltaX = width - rect.right;
			}
		}
		if ( rect.height() >= height )
		{
			if ( rect.top > 0 )
			{
				deltaY = -rect.top;
			}
			if ( rect.bottom < height )
			{
				deltaY = height - rect.bottom;
			}
		}
		if ( rect.width() < width )
		{
			deltaX = width / 2f - rect.right + rect.width() / 2f;
		}
		if ( rect.height() < height )
		{
			deltaY = height / 2f - rect.bottom + rect.height() / 2f;
		}

		mScaleMatrix.postTranslate( deltaX, deltaY );
		mIndicateArrow.getImage_Matrix();
		mIndicateArrow.POST_Translate( deltaX, deltaY );
		mCurrentSurfaceView.getImage_Matrix();
		mCurrentSurfaceView.POST_Translate( deltaX, deltaY );
	}

	@ Override
	public boolean onScaleBegin( ScaleGestureDetector detector )
	{
		return true;
	}

	@ Override
	public void onScaleEnd( ScaleGestureDetector detector )
	{
		ifStartRoutePlan = false;
	}

	/**
	 * onTouch 触碰屏幕事件
	 * 
	 * @param View
	 *            v 触摸视图
	 * @return MontionEvent event 动作行为
	 * @since 1.0.0
	 */
	@ SuppressLint( "ClickableViewAccessibility" )
	@ Override
	public boolean onTouch( View v, MotionEvent event )
	{
		super.onTouchEvent( event );

		if ( mGestureDetector.onTouchEvent( event ) )
			return true;

		mScaleGestureDetector.onTouchEvent( event );

		float x = 0;
		float y = 0;

		// 获取触碰点的数量
		int pointercount = event.getPointerCount();
		for ( int i = 0; i < pointercount; i++ )
		{
			x += event.getX( i );
			y += event.getY( i );
		}
		// 计算平均改变量
		x /= pointercount;
		y /= pointercount;
		// 判断是否和上次一样
		if ( mLastPointerCount != pointercount )
		{
			isCanDrag = false;
			mLastX = x;
			mLastY = y;
		}

		mLastPointerCount = pointercount;
		RectF rectF = getMatrixRectF();

		switch ( event.getAction() )
		{
		// 按下事件
			case MotionEvent.ACTION_DOWN :
				if ( rectF.width() > getWidth() || rectF.height() > getHeight() )
				{
					getParent().requestDisallowInterceptTouchEvent( true );
				}
				mEditText.clearFocus();
				( (InputMethodManager) mActivity
						.getSystemService( Context.INPUT_METHOD_SERVICE ) )
						.hideSoftInputFromWindow( mEditText.getWindowToken(), 0 );

				float left = -rectF.left;
				@ SuppressWarnings( "unused" )
				float top = -rectF.top;

				// System.out.println( "lvetianmap里面点击了屏幕的x:" + event.getX()
				// + ",里面点击了屏幕的:" + event.getY() );
				if ( 1 == mLastPointerCount )
				{
					ifStartRoutePlan = true;
					mPic_PixelPoint.setX( (int) ( event.getX() + left ) );
					mPic_PixelPoint.setY( (int) ( event.getY() ) );
					PixelPoint.SCALE=getScale();
				}
				break;
			// 移动事件
			case MotionEvent.ACTION_MOVE :
				if ( rectF.width() > getWidth() || rectF.height() > getHeight() )
				{
					getParent().requestDisallowInterceptTouchEvent( true );
				}
				float dx = x - mLastX;
				float dy = y - mLastY;
				if ( !isCanDrag )
				{
					isCanDrag = isMoveAction( dx, dy );
				}
				if ( isCanDrag )
				{
					if ( null != getDrawable() )
					{
						isCheckLeftAndRight = isCheckTopAndBottom = true;
						if ( rectF.width() < getWidth() )
						{
							isCheckLeftAndRight = false;
							dx = 0;
						}
						if ( rectF.height() < getHeight() )
						{
							isCheckTopAndBottom = false;
							dy = 0;
						}
						mScaleMatrix.postTranslate( dx, dy );
						mIndicateArrow.getImage_Matrix();
						mIndicateArrow.POST_Translate( dx, dy );
						mCurrentSurfaceView.getImage_Matrix();
						mCurrentSurfaceView.POST_Translate( dx, dy );
						CheckBorderWhenTranslate();
						setImageMatrix( mScaleMatrix );
						ifStartRoutePlan = false;
					}
				}
				mLastX = x;
				mLastY = y;

				break;
			// 抬起事件
			case MotionEvent.ACTION_UP :
				mLastPointerCount = 0;
				break;
			// 取消事件
			case MotionEvent.ACTION_CANCEL :
				mLastPointerCount = 0;
				break;

			default :
				break;
		}
		return true;
	}

	/**
	 * CheckBorderWhenTranslate 检测边界是否越界
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	private void CheckBorderWhenTranslate()
	{
		RectF rectF = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;

		int width = getWidth();
		int height = getHeight();

		if ( rectF.left > 0 && isCheckLeftAndRight )
		{
			deltaX = -rectF.left;
		}

		if ( rectF.right < width && isCheckLeftAndRight )
		{
			deltaX = width - rectF.right;
		}

		if ( rectF.top > 0 && isCheckTopAndBottom )
		{
			deltaY = -rectF.top;
		}

		if ( rectF.bottom < height && isCheckTopAndBottom )
		{
			deltaY = height - rectF.bottom;
		}

		mScaleMatrix.postTranslate( deltaX, deltaY );
		mIndicateArrow.getImage_Matrix();
		mIndicateArrow.POST_Translate( deltaX, deltaY );
		mCurrentSurfaceView.getImage_Matrix();
		mCurrentSurfaceView.POST_Translate( deltaX, deltaY );
	}

	/**
	 * getPixelPoint 获取点击的屏幕点
	 * 
	 * @param void
	 * @return PixelPoint 屏幕坐标点
	 * @since 1.0.0
	 */
	public PixelPoint getPixelPoint()
	{
		return mPic_PixelPoint;
	}

	private boolean isMoveAction( float dx, float dy )
	{
		return Math.sqrt( dx * dx + dy * dy ) >= mTouchSlop;
	}

};