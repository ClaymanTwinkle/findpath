package com.lvetianzhiyi.route.view;

import java.util.ArrayList;

import com.lvetianzhiyi.route.R;
import com.lvetianzhiyi.route.utils.ActionItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author xinyi 功能描述：标题按钮上的弹窗（继承自PopupWindow）
 */
public class TitlePopup extends PopupWindow
{
	private TextView B1;
	private TextView F1;
	private TextView F2;

	private Context mContext;

	// 列表弹窗的间隔
	protected final int LIST_PADDING = 10;

	// 坐标的位置（x、y）
	private final int[] mLocation = new int[2];

	// 屏幕的宽度和高度
	// private int mScreenWidth, mScreenHeight;

	// 判断是否需要添加或更新列表子类项
	private boolean mIsDirty;

	// 位置不在中心
	// private int popupGravity = Gravity.NO_GRAVITY;

	// 弹窗子类项选中时的监听
	private OnItemOnClickListener mItemOnClickListener;

	// 定义弹窗子类项列表
	private ArrayList< ActionItem > mActionItems = new ArrayList< ActionItem >();

	public TitlePopup( Context context )
	{
		// 设置布局的参数
		this( context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
	}

	@ SuppressLint( "InflateParams" )
	public TitlePopup( Context context, int width, int height )
	{
		this.mContext = context;

		// 设置可以获得焦点
		setFocusable( false );
		// 设置弹窗内可点击
		setTouchable( true );
		// 设置弹窗外可点击
		setOutsideTouchable( true );

		// 获得屏幕的宽度和高度
		// mScreenWidth = Util.getScreenWidth(mContext);
		// mScreenHeight = Util.getScreenHeight(mContext);

		// 设置弹窗的宽度和高度
		setWidth( width );
		setHeight( height );

		// setBackgroundDrawable( new BitmapDrawable() );

		// 设置弹窗的布局界面
		View view = LayoutInflater.from( mContext ).inflate(
				R.layout.popupwindow_content, null );
		setContentView( view );
		B1 = (TextView) view.findViewById( R.id.pop_parking );
		F1 = (TextView) view.findViewById( R.id.pop_f1 );
		F2 = (TextView) view.findViewById( R.id.pop_f2 );
		B1.setOnClickListener( onclick );
		F1.setOnClickListener( onclick );
		F2.setOnClickListener( onclick );
	}

	/**
	 * 显示弹窗列表界面
	 */
	public void show( final View view )
	{
		// 获得点击屏幕的位置坐标
		view.getLocationOnScreen( mLocation );
		// F2.setText( mActionItems.get( 0 ).mTitle );
		// 判断是否需要添加或更新列表子类项
		if ( mIsDirty )
		{
			// populateActions();
		}

		// 显示弹窗的位置
		// showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING
		// - (getWidth() / 2), mRect.bottom);
		// showAtLocation( c, Gravity.NO_GRAVITY, mLocation[0] - this.getWidth()
		// - 10, mLocation[1]
		// - ( ( this.getHeight() - c.getHeight() ) / 2 ) );
		showAtLocation( view, Gravity.NO_GRAVITY,
				mLocation[0] + view.getWidth() + 5, mLocation[1] - 10 );
	}

	OnClickListener onclick = new OnClickListener()
	{
		@ Override
		public void onClick( View v )
		{
			dismiss();
			switch ( v.getId() )
			{
				case R.id.pop_parking :
					mItemOnClickListener.onItemClick( mActionItems.get( 0 ), 0 );
					break;
				case R.id.pop_f1 :
					mItemOnClickListener.onItemClick( mActionItems.get( 1 ), 1 );
					break;
				case R.id.pop_f2 :
					mItemOnClickListener.onItemClick( mActionItems.get( 2 ), 2 );
					break;
			}
		}

	};

	/**
	 * 添加子类项
	 */
	public void addAction( ActionItem action )
	{
		if ( action != null )
		{
			mActionItems.add( action );
			mIsDirty = true;
		}
	}

	/**
	 * 清除子类项
	 */
	public void cleanAction()
	{
		if ( mActionItems.isEmpty() )
		{
			mActionItems.clear();
			mIsDirty = true;
		}
	}

	/**
	 * 根据位置得到子类项
	 */
	public ActionItem getAction( int position )
	{
		if ( position < 0 || position > mActionItems.size() )
			return null;
		return mActionItems.get( position );
	}

	/**
	 * 设置监听事件
	 */
	public void setItemOnClickListener(
			OnItemOnClickListener onItemOnClickListener )
	{
		this.mItemOnClickListener = onItemOnClickListener;
	}

	/**
	 * @author yangyu 功能描述：弹窗子类项按钮监听事件
	 */
	public static interface OnItemOnClickListener
	{
		public void onItemClick( ActionItem item, int position );
	}
}