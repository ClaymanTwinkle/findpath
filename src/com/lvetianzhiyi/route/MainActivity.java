package com.lvetianzhiyi.route;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lvetianzhiyi.route.activity.FirstTime_Activity;
import com.lvetianzhiyi.route.activity.Global_Activity;
import com.lvetianzhiyi.route.activity.Search_Activity;
import com.lvetianzhiyi.route.activity.Setting_Activity;
import com.lvetianzhiyi.route.activity.Single_Search_Activity;
import com.lvetianzhiyi.route.algorithm.AStar;
import com.lvetianzhiyi.route.algorithm.BuildingDataTool;
import com.lvetianzhiyi.route.algorithm.DrawImage;
import com.lvetianzhiyi.route.algorithm.Guider;
import com.lvetianzhiyi.route.algorithm.Path;
import com.lvetianzhiyi.route.algorithm.PixelPoint;
import com.lvetianzhiyi.route.algorithm.Place;
import com.lvetianzhiyi.route.algorithm.Route;
import com.lvetianzhiyi.route.data.OnlineData;
import com.lvetianzhiyi.route.listener.SensorListener;
import com.lvetianzhiyi.route.thread.GuideSpeaker;
import com.lvetianzhiyi.route.thread.GuiderRunRunnable;
import com.lvetianzhiyi.route.thread.Sound;
import com.lvetianzhiyi.route.type.Model;
import com.lvetianzhiyi.route.utils.ActionItem;
import com.lvetianzhiyi.route.utils.CheckIfFirst;
import com.lvetianzhiyi.route.utils.DealData;
import com.lvetianzhiyi.route.utils.ExitApplication;
import com.lvetianzhiyi.route.utils.JsonManger;
import com.lvetianzhiyi.route.utils.LoadImageFromAssets;
import com.lvetianzhiyi.route.utils.Popup_Util;
import com.lvetianzhiyi.route.utils.Screen_info;
import com.lvetianzhiyi.route.utils.SendRouteImage;
import com.lvetianzhiyi.route.utils.SharedPreferencesOP;
import com.lvetianzhiyi.route.utils.SoundSpeaker;
import com.lvetianzhiyi.route.view.IndicateArrow;
import com.lvetianzhiyi.route.view.LveTianMapView;
import com.lvetianzhiyi.route.view.MyMarqueeTextView;
import com.lvetianzhiyi.route.view.MyProgressBar;
import com.lvetianzhiyi.route.view.SurfaceViewDraw;
import com.lvetianzhiyi.route.view.TitlePopup;
import com.lvetianzhiyi.route.view.TitlePopup.OnItemOnClickListener;

/**
 * 
 * 类 MainActivity(进入程序后与用户交互的主界面)
 * 
 * @author xinyi
 * 
 * @接口 OnClickListener
 * 
 * @since 1.0.0
 */
public class MainActivity extends Activity implements OnClickListener
{
	// 通信标识
	public final static int CONST_SEARCH_ACTIVITY_RESULT = 1;
	final static private int DRAW_PROGRESS = 2;
	final static public int GETJSONPOINT = 3;
	public final static int CONST_SETTINGS_RESULT = 4;
	public final static int CONST_REFRESH_MARQUEE = 5;
	public final static int RUNDEVIATION = 6;
	public final static int SHOWTOAST = 7;
	public final static int REARCHGOAL = 8;
	public final static int CONST_SINGLE_RESULT = 9;
	private final static int TIPS = 10;
	public final static int CONST_UPDATE_PROGRESS=11;

	// IP 地址
	private String contectUrl = null;
	// 当前层号
	static public int mArrowLayer;
	// 自定义传送路径图片工具
	static public SendRouteImage mSendRouteImage;
	// 与服务器交互的循环线程
	private Thread CycleThread;
	// 加载显示的三层地图片
	static private Bitmap mBitmap_Map[];
	// 加载显示的路线规划图片
	static private Bitmap mBitmap_Route[];
	// 主界面的“+”号按钮
	private ImageButton imageButton_add;
	// 主界面的“-”号按钮
	private ImageButton imageButton_minus;
	// 主界面的指南针按钮
	private ImageButton imageButton_zhinanzhen;
	// 展开选层按钮
	private ImageButton mPopupWindow;
	// 全局视图按钮
	private Button mButton_GlobalPreview;
	// /////////////////////////////////////////////// 路线按钮
	private Button mButton_Luxian;
	// “到这去”按钮
	private Button mButton_GoHere;
	// 设置按钮
	private Button mSettings;
	// 选层选项
	private TitlePopup titlePopup;
	// 选择地点编辑框
	private EditText mEditText;
	// 跑马灯路径提示
	private MyMarqueeTextView mMarqueeText;
	// 地图视图图层
	private LveTianMapView mLveTianMapView;
	// 路径视图图层
	private SurfaceViewDraw mSurfaceView;
	// 用户当前位置光标
	private IndicateArrow mIndicateArrow;
	// 安卓感应器
	private SensorManager sensorManager;
	// 感应器监听器
	private SensorListener mSensorListener;
	// 引导线程
	private GuiderRunRunnable guiderRouteRunnable;
	// 提示框
	private Dialog mDialog;
	// 自定义圆形进度条
	private MyProgressBar myProgressBar;
	// 按下 Back 的退出时间
	private long mExitTime;
	// 规划执行时的地点数量
	private int mExexute_cishu;
	// 当前执行规划的地点号
	private int mNow_Execute;
	// 画图时用于纠正路线偏差的偏移量
	private float moveInch;
	// 目的地的集合
	private List< Place > mGoalPlaceList;
	// 规划后路径的集合
	private List< Path > mResultPaths;
	// 用于规划路径的实例
	private Guider mGuider;
	// 起始点
	private PixelPoint mStartPos;
	// 缩小后的起点坐标
	private PixelPoint mNewStartPos = null;
	// 用户当前选择的目标位置
	private PixelPoint mTouchGoalPos;
	// 所有路径
	private Queue< Route > mRoutes = null;
	// 画路线图的工具类
	private DrawImage mDrawImage;
	// 当前主 Activity
	private Activity mActivity;
	// 语音播放类
	private SoundSpeaker mSoundSpeaker = null;
	// 语音播放线程
	private Sound mSound = null;
	// 判断登录次数
	private CheckIfFirst mCheck;
	// 当前模式
	public static Model mLocationMode;
	// 当前的path
	private Path mLocationPath;
	// 提示线程
	private Thread mTipsThread;

	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		requestWindowFeature( Window.FEATURE_NO_TITLE );// 设置无标题栏风格
		setContentView( R.layout.activity_main );
		Init();// 初始化控件
		PrepareThread();
	}

	/**
	 * Init
	 * 
	 * 组件初始化
	 * 
	 * @param void
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	private void Init()
	{
		// 开始显示的东西
		FirstShow();
		// 预处理ip
		PrepareIp();

		mActivity = this;
		mArrowLayer = 1;// 如果无网络，默认为第一层
		// 默认当前模式为普通模式
		mLocationMode = Model.COMMON_MODE;

		// 获取用户屏幕信息并保存
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics( dm );
		new Screen_info( dm.widthPixels, dm.heightPixels );

		mDrawImage = new DrawImage( this );
		mRoutes = new LinkedList< Route >();
		mLocationPath = null;

		// 地图图片初始化
		mBitmap_Map = new Bitmap[3];
		mNow_Execute = 0;
		mBitmap_Map[0] = new LoadImageFromAssets( this, "underground.png" )
				.getBitmap();
		mBitmap_Map[1] = new LoadImageFromAssets( this, "floor1.png" )
				.getBitmap();
		mBitmap_Map[2] = new LoadImageFromAssets( this, "floor2.png" )
				.getBitmap();

		// 路线图层初始化
		mBitmap_Route = new Bitmap[3];
		for ( int i = 0; i < mBitmap_Route.length; i++ )
		{
			mBitmap_Route[i] = Bitmap.createBitmap( dm.widthPixels,
					dm.heightPixels, Config.ARGB_8888 );
		}
		mGuider = new Guider( this );
		mGuider.setmRoutes( mRoutes );
		mGoalPlaceList = new LinkedList< Place >();

		 mDialog = new Dialog(this);
		 mDialog.setTitle("温馨提示");
		 mDialog.setCancelable(false);
		 mDialog.setCanceledOnTouchOutside(false);
		 mDialog.setContentView(R.layout.circle_progress_bar);

		// 绑定控件
		FindViewsById();
		myProgressBar.setProgress(0);

		// 初始化选层选项卡
		CreateTitlePopup();

		// View 图层初始化设置
		mSurfaceView.setImageBitmap( Bitmap.createBitmap( dm.widthPixels,
				dm.heightPixels, Config.ARGB_8888 ) );
		mIndicateArrow.setImageBitmap( new LoadImageFromAssets( this,
				"icon_arrow.png" ).getBitmap() );
		mIndicateArrow.CalculateScreen_move();
		mIndicateArrow.MoveToCenter();
		mLveTianMapView.setSurfaceView( mSurfaceView );
		mLveTianMapView.setmIndicateArrow( mIndicateArrow );
		mLveTianMapView.setImageBitmap( mBitmap_Map[1] );
		mLveTianMapView.setmCurrentFloor( 1 );
		mLveTianMapView.setEdittext( mEditText );
		mLveTianMapView.setCurrentActivity( this );
		mSensorListener = new SensorListener( mLveTianMapView, mIndicateArrow );

		// 绑定事件监听器
		SetListeners();

		// 如果设置了屏幕常亮
		if ( new SharedPreferencesOP( this ).ReadSP( "alwaysLight" ).equals(
				"opened" ) )
		{
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
		}
		openVoice( 1 );
	}

	/**
	 * 
	 * SetListeners(给控件绑定事件监听器)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void SetListeners()
	{
		// click点击事件
		imageButton_add.setOnClickListener( this );
		imageButton_minus.setOnClickListener( this );
		imageButton_zhinanzhen.setOnClickListener( this );
		mPopupWindow.setOnClickListener( this );
		mLveTianMapView.setOnClickListener( this );
		mButton_GoHere.setOnClickListener( this );
		mButton_GlobalPreview.setOnClickListener( this );
		mButton_Luxian.setOnClickListener( this );
		mSettings.setOnClickListener( this );

		mEditText.setOnFocusChangeListener( new View.OnFocusChangeListener()
		{
			@ Override
			public void onFocusChange( View v, boolean hasFocus )
			{
				if ( hasFocus )
				{
					// 此处为得到焦点时的处理内容
					DismissThePopupwindow();
					InputMethodManager m = (InputMethodManager) mEditText
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE );
					m.hideSoftInputFromWindow( mEditText.getWindowToken(), 0 );

					// 跳转至搜索输入框的 Activity
					Intent intent = new Intent( MainActivity.this,
							Single_Search_Activity.class );

					startActivityForResult( intent, CONST_SINGLE_RESULT );
					overridePendingTransition( R.anim.slide_bottom_in,
							R.anim.slide_bottom_out );
					if ( mLveTianMapView.getScale()
							/ Screen_info.CaluculateInitInch() > 1.01f )
					{
						mLveTianMapView.postDelayed(
								mLveTianMapView.new AutoScaleRunnable(
										Screen_info.CaluculateInitInch(),
										Screen_info.getScreen_Width() / 2,
										Screen_info.getScreen_Height() / 2,
										true ), 0 );
					}

				}
				else
				{
					// 此处为失去焦点时的处理内容
				}

			}
		} );

	}

	/**
	 * onClick
	 * 
	 * 实现按钮事件
	 * 
	 * @param view
	 *            点击事件时的视图组件
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	@ Override
	public void onClick( View view )
	{
		switch ( view.getId() )
		{
			case R.id.imagebutton_add :
				DismissThePopupwindow();// 如果层选项卡未收起，则将选项卡收起
				mLveTianMapView.setEnlarge( true );
				break;

			case R.id.imagebutton_minus :
				DismissThePopupwindow();
				mLveTianMapView.setEnlarge( false );
				break;

			case R.id.zhinanzhen :
				DismissThePopupwindow();
				ChangeLayer( mArrowLayer );
				break;

			case R.id.button_select_floor :
				if ( titlePopup.isShowing() )
				{
					titlePopup.dismiss();
					return;
				}
				titlePopup.setAnimationStyle( R.style.cricleBottomAnimation );
				titlePopup.show( view );
				break;

			case R.id.LveTianMap :
				DismissThePopupwindow();
				float top = mLveTianMapView.getMatrixRectF().top;
				// 当前放大倍数小于1.01倍时，才能使用自定义定点规划
				if ( mLveTianMapView.getIfStartRoutePlan()
						&& mLveTianMapView.getScale()
								/ Screen_info.CaluculateInitInch() < 1.01f )
				{
					mTouchGoalPos = mLveTianMapView.getPixelPoint();

					if ( ( mTouchGoalPos.getY() - top >= 0 )
							&& ( mTouchGoalPos.getY() <= mLveTianMapView
									.getMatrixRectF().bottom ) )
					{
						mSurfaceView.setIfDraw( true );
						mTouchGoalPos
								.setZ( mLveTianMapView.getmCurrentFloor() - 1 );
						mSurfaceView.setTouchPos( mTouchGoalPos );
						try
						{
							mSurfaceView.My_onDraw();// 描绘标注
						}
						catch ( OutOfMemoryError e )
						{
							ShowToast( "点击有误，请重试" );
						}
					}
					else
					{
						ShowToast( "您点击的位置有误,请重新选择！" );
						DrawView();
					}
				}
				break;

			case R.id.button_Luxian :
				// 跳转至搜索输入框的 Activity
				Intent intent = new Intent( MainActivity.this,
						Search_Activity.class );
				startActivityForResult( intent, 1 );
				overridePendingTransition( R.anim.slide_bottom_in,
						R.anim.slide_bottom_out );
				break;

			case R.id.button_GoHere :
				DismissThePopupwindow();
				if ( mTouchGoalPos != null
						&& mLveTianMapView.getScale()
								/ Screen_info.CaluculateInitInch() < 1.01f )
				{
					if ( mStartPos == null )
					{
						PlaningInit( 0 );
					}
					if ( mStartPos != null )
					{
						float times = mLveTianMapView.getScale()
								/ Screen_info.CaluculateInitInch();
						mTouchGoalPos
								.setY( (int) ( mTouchGoalPos.getY() - mLveTianMapView
										.getMatrixRectF().top * times ) );
						mTouchGoalPos.CalculateIndexPos();
						if ( checkedPoint( mTouchGoalPos ) )
						{
							// PlaningInit(1);
							ArrayList< String > GoalPos = new ArrayList< String >();
							GoalPos.add( "自定义地点" );
							// 添加到集合中
							mGoalPlaceList.add( new Place( "自定义地点" ) );
							setRoutesArray();
							GuideSpeaker.setSelfChoosePoint();
							// 开始程序路径规划的逻辑
							logic( mStartPos, GoalPos );
						}
						else
						{
							ShowToast( "提示：选择的坐标错误！" );
						}
					}
					else
					{
						ShowToast( "提示：找不到用户位置！" );
						return;
					}

				}
				break;

			case R.id.button_GlobalPreview :
				DismissThePopupwindow();
				// 跳转至全局视图
				Intent intent2 = new Intent( MainActivity.this,
						Global_Activity.class );
				Bitmap SendImage[] = new Bitmap[3];
				for ( int i = 0; i < mBitmap_Map.length; i++ )
					SendImage[i] = mBitmap_Route[i].copy( Config.ARGB_8888,
							true );
				mSendRouteImage = new SendRouteImage( SendImage );
				startActivity( intent2 );
				overridePendingTransition( R.anim.zoom_enter_global,
						R.anim.zoom_exit_global );
				break;

			case R.id.Setting :
				DismissThePopupwindow();
				// 跳转至全局视图
				Intent intent3 = new Intent( MainActivity.this,
						Setting_Activity.class );
				startActivityForResult( intent3, 1 );
				overridePendingTransition( R.anim.zoom_enter_setting,
						R.anim.zoom_exit_setting );
				ExitApplication.getInstance().addActivity( this );
			default :
				DismissThePopupwindow();
				break;
		}
	}

	/**
	 * CreateTitlePopup 初始化选层选项卡
	 * 
	 * @param void
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	private void CreateTitlePopup()
	{
		//
		titlePopup = new TitlePopup( this, Popup_Util.dip2px( this, 165 ),
				Popup_Util.dip2px( this, 40 ) );
		titlePopup.addAction( new ActionItem( this, "F2",
				R.drawable.pop_list_2_unpressed ) );
		titlePopup.addAction( new ActionItem( this, "F1",
				R.drawable.pop_list_1_unpressed ) );
		titlePopup.addAction( new ActionItem( this, "B1",
				R.drawable.pop_list_0_unpressed ) );

		// 设置监听
		titlePopup.setItemOnClickListener( new OnItemOnClickListener()
		{
			@ Override
			public void onItemClick( ActionItem item, int position )
			{
				ChangeLayer( position );
			}
		} );
	}

	/**
	 * onActivityResult 接收Activity返回值
	 * 
	 * @param int 请求
	 * @param int 结果
	 * @param Intent
	 *            意图对象
	 * @return void
	 * @since 1.0.0
	 */
	@ Override
	protected void onActivityResult( int requestCode, int resultCode,
			Intent data )
	{
		super.onActivityResult( requestCode, resultCode, data );
		switch ( resultCode )
		{
			case CONST_SEARCH_ACTIVITY_RESULT :
				try
				{
					openVoice( 0 );
					// 获取用户输入的地点
					ArrayList< String > UserInput = data.getExtras()
							.getStringArrayList( "UserInput" );
					// PlaningInit( 0 );
					if ( mStartPos == null )
					{
						PlaningInit( 0 );
					}
					if ( mStartPos != null )
					{
						for ( int i = 0; i < UserInput.size(); i++ )
						{
							Place place = new Place( UserInput.get( i ) );
							// 添加到集合中
							mGoalPlaceList.add( place );

						}
						setRoutesArray();
						GuideSpeaker.setGoalPoint( UserInput );
						// 开始程序路径规划的逻辑
						logic( mStartPos, UserInput );
					}
					else
					{
						ShowToast( "提示：找不到用户位置！" );
						return;
					}
				}
				catch ( Exception e )
				{
					ShowToast( "搜索地点不存在或发生未知错误" );
				}
				break;

			case CONST_SETTINGS_RESULT :
				openVoice( 0 );
				if ( mSoundSpeaker != null )
					mSoundSpeaker.SpeakString( "设置完毕" );
				if ( data.getBooleanExtra( "alwaysLight", false ) )
				{
					getWindow().addFlags(
							WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
				}
				else
				{
					getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
				}
				break;

			case CONST_SINGLE_RESULT :
				openVoice( 0 );
				mSurfaceView.clearRoute();
				String userSelected = data.getStringExtra( "UserSelectedPos" );
				BuildingDataTool tempBDT = null;
				if ( userSelected.startsWith( "B1" ) )
				{
					tempBDT = new BuildingDataTool( OnlineData.mMapFile[0] );
					ChangeLayer( 0 );
				}
				else if ( userSelected.startsWith( "F1" ) )
				{
					tempBDT = new BuildingDataTool( OnlineData.mMapFile[1] );
					ChangeLayer( 1 );
				}
				else
				{
					tempBDT = new BuildingDataTool( OnlineData.mMapFile[2] );
					ChangeLayer( 2 );
				}
				List< PixelPoint > points = tempBDT
						.findBuildingByName( userSelected.split( " " )[1] );
				PixelPoint.SCALE = Screen_info.CaluculateInitInch();
				PixelPoint pixelPoint = points.get( 0 );
				pixelPoint.CalculatePixelPos();
				pixelPoint.set(
						pixelPoint.getX()
								+ (int) mLveTianMapView.getMatrixRectF().left,
						pixelPoint.getY()
								+ (int) mLveTianMapView.getMatrixRectF().top );
				pixelPoint.CalculateIndexPos();
				mSurfaceView.setIfDraw( true );
				pixelPoint.setZ( mLveTianMapView.getmCurrentFloor() - 1 );
				mSurfaceView.setTouchPos( pixelPoint );
				mTouchGoalPos = pixelPoint;
				try
				{
					mSurfaceView.My_onDraw();// 描绘标注
				}
				catch ( OutOfMemoryError e )
				{
					ShowToast( "网络繁忙" );
				}
				break;

			default :
				break;
		}
	}

	/**
	 * PlaningInit 路径规划前初始化(获取放大倍数及起点位置等)
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	private void PlaningInit( int flag )
	{
		// 纠偏需要用到的数据
		float RightPos = mIndicateArrow.getImageRightPos();
		float LeftPos = mIndicateArrow.getImageLeftPos();
		float BottomPos = mIndicateArrow.getImageBottomPos();
		float TopPos = mIndicateArrow.getImageTopPos();
		float MatrixRectFleft = mLveTianMapView.getMatrixRectF().left;
		float MatrixRectFtop = mLveTianMapView.getMatrixRectF().top;
		float Scale = mLveTianMapView.getScale();
		float currentPointX = ( RightPos - LeftPos ) / 2 + LeftPos;
		float currentPointY = ( BottomPos - TopPos ) / 2 + TopPos;
		float InitJiange = mLveTianMapView.getmCurrentFloor() == 0 ? Screen_info
				.getInitTop2() : Screen_info.getInitTop();

		// 这三个分别是像素点 x, y, 层数z
		float centerX = currentPointX - MatrixRectFleft;
		float centerY = currentPointY - MatrixRectFtop;
		int layerZ = mArrowLayer;// 无连网时默认在第一层

		// 根据放大倍数不同需要纠偏移动的距离
		moveInch = ( centerY - ( currentPointY - InitJiange ) )
				* ( Scale / Screen_info.CaluculateInitInch() );

		// System.out.println( "当前层：" + layerZ );
		mStartPos = new PixelPoint( (int) centerX, (int) centerY, layerZ - 1 );
		PixelPoint.SCALE = Scale;
		mStartPos.CalculateIndexPos();
		// 记录坐标
		mGuider.SetLocationPoint( mStartPos );
	}

	/**
	 * 
	 * logic(调用A星算法绘制图片路径)
	 * 
	 * @param pGoalPlaceName
	 *            目标地点名 void
	 * @since 1.0.0
	 */
	public void logic( PixelPoint CurrentLayer, ArrayList< String > GoalPos )
	{
		// 初始化地图
		mLveTianMapView.setImageBitmap( mBitmap_Map[CurrentLayer.getZ() + 1] );
		mLveTianMapView.setmCurrentFloor( CurrentLayer.getZ() + 1 );

		switch ( CurrentLayer.getZ() )
		{
			case -1 :
				mPopupWindow
						.setBackgroundResource( R.drawable.popupbutton_0_selector );
				break;
			case 0 :
				mIndicateArrow.setVisibility( View.VISIBLE );
				mPopupWindow
						.setBackgroundResource( R.drawable.popupbutton_1_selector );
				break;

			case 1 :
				mPopupWindow
						.setBackgroundResource( R.drawable.popupbutton_2_selector );
				break;
		}

		mSurfaceView.clearRoute();

		// 缩小坐标
		mNewStartPos = new PixelPoint( CurrentLayer.getX(),
				CurrentLayer.getY(), CurrentLayer.getZ() );
		PixelPoint.SCALE = mLveTianMapView.getScale();
		mNewStartPos.CalculateIndexPos();

		if ( checkedPoint( mNewStartPos ) )
		{
			setRoutesArray();
			mExexute_cishu = GoalPos.size();
			mDialog.show();
			mDrawImage.init( mLveTianMapView,mHandler);
			new Thread( new CalculatePath() ).start();
			
			// 修改模式
			if ( mExexute_cishu == 1 )
			{
				ChangeMode( Model.SINGLE_PLANNING_MODE );
			}
			else
			{
				ChangeMode( Model.MULTI_PLANNING_MODE );
			}
		}
		else
		{
			ShowToast( "当前人物位置出错！" );
		}
	}

	private Handler mHandler = new Handler()
	{
		@ Override
		public void handleMessage( Message msg )
		{
			switch ( msg.what )
			{
				case GETJSONPOINT :
					float data[] = (float[]) msg.obj;
					if ( mArrowLayer != (int) data[2] )
					{
						mArrowLayer = (int) data[2];// 根据服务器端的位置设定当前层用户所在当前层
						ChangeLayer( mArrowLayer );
					}
					DealData.moveIndicator( mLveTianMapView, mIndicateArrow,
							data[0], data[1], mArrowLayer );
					PlaningInit( 0 );
					if ( Model.COMMON_MODE != mLocationMode )
					{
						// 检测走偏
						new Thread( guiderRouteRunnable ).start();
					}
					break;

				case DRAW_PROGRESS :
					if ( mResultPaths != null )
					{
						if(mExexute_cishu>1)
						{
							// 画图
							DrawView(false);
						}
						else
						{
							// 画图
							DrawView(true);
						}
					}
					mDialog.dismiss();
					myProgressBar.setProgress(0);
					break;
				case CONST_REFRESH_MARQUEE :
					String talkWord = (String) msg.obj;
					mMarqueeText.setText( talkWord );
					break;
				// 跑偏处理
				case RUNDEVIATION :
					if ( mSoundSpeaker != null )
					{
						mSoundSpeaker.SpeakString( "你跑偏了，路径已重新规划！" );
					}
					//ShowToast( "你跑偏了，路径已重新规划！" );
					mRoutes = mGuider.getRoutes();
					Route  pRoute= mRoutes.element();
					getLocationPath(pRoute );
					mResultPaths = pRoute.getmResultPaths();
					mDialog.show();
					// 画图
					DrawView();
					mDialog.dismiss();
					myProgressBar.setProgress(0);
					break;
				// 到达中转站
				case REARCHGOAL :
					mRoutes = mGuider.getRoutes();
					if ( mRoutes.isEmpty() )
					{
						if ( mSoundSpeaker != null )
						{
							mSound.setmText( "抵达最后的地点" );
							new Thread( mSound ).start();
						}
						ShowToast( "抵达最后的地点" );
						ChangeMode( Model.COMMON_MODE );
						mResultPaths = null;
					}
					else
					{
						mSound.setmText( "抵达中转点" );
						new Thread( mSound ).start();
						ShowToast( "抵达中转点" );
						mResultPaths = mRoutes.element().getmResultPaths();
					}
					// 画图
					mDialog.show();
					// 画图
					DrawView();
					mDialog.dismiss();
					myProgressBar.setProgress(0);
					break;
				case SHOWTOAST :
					if ( mSoundSpeaker != null && msg.obj != null )
					{
						mSound.setmText( msg.obj.toString() );
						new Thread( mSound ).start();
						ShowToast( msg.obj.toString() );
					}
					break;

				case TIPS :
					if (mLocationMode != Model.COMMON_MODE&&!mRoutes.isEmpty())
					{
						mSound.setmText( msg.obj.toString() );
						new Thread( mSound ).start();
					}
					ShowToast( msg.obj.toString() );
					break;
				case CONST_UPDATE_PROGRESS:
					int increase=(Integer)msg.obj;
					myProgressBar.setProgress(myProgressBar.getProgress()+increase);
					break;
				default :
					break;
			}
			super.handleMessage( msg );
		}
	};

	class CalculatePath implements Runnable
	{
		public void run()
		{
			while ( mNow_Execute < mExexute_cishu )
			{
				planPath();
				if ( mNow_Execute == 0 )
				{
					if ( !mRoutes.isEmpty() )
					{
						if ( mRoutes.element() == null )
						{
							Message msg = Message.obtain();
							msg.what = SHOWTOAST;
							msg.obj = "查找不到地点";
							// 发送这个消息到消息队列中
							mHandler.sendMessage( msg );
						}
						else
						{
							// 获取一个Message对象，设置what为1
							Message msg = Message.obtain();
							msg.what = DRAW_PROGRESS;
							// 发送这个消息到消息队列中
							mHandler.sendMessage( msg );
							GuideSpeaker.setRouteLength( AStar
									.getTotal_Distance() );
							AStar.initTotal_Distance();
							new Thread( new GuideSpeaker( mSoundSpeaker,
									mHandler ) ).start();
						}
					}
				}
				mNow_Execute++;
			}
			mDialog.dismiss();
			myProgressBar.setProgress(0);
			mNow_Execute = 0;
		}
	};

	/**
	 * ChangeLayer 更改当前显示的视图为目标层数
	 * 
	 * @param int 目标层数(取值范围0,1,2)
	 * @return void
	 * @since 1.0.0
	 */
	public void ChangeLayer( int layer )
	{
		try
		{
			if ( layer != mLveTianMapView.getmCurrentFloor() )
			{
				mSurfaceView.setHasBiao( false );
			}
			mLveTianMapView.setImageBitmap( mBitmap_Map[layer] );
			mLveTianMapView.setmCurrentFloor( layer );
			if ( mBitmap_Route != null )
			{
				mSurfaceView.setImageBitmap( mBitmap_Route[layer] );
			}
			else
			{
				mSurfaceView.clearRoute();
			}
			switch ( layer )
			{
				case 0 :
					mPopupWindow
							.setBackgroundResource( R.drawable.popupbutton_0_selector );
					break;
				case 1 :
					mPopupWindow
							.setBackgroundResource( R.drawable.popupbutton_1_selector );
					break;
				case 2 :
					mPopupWindow
							.setBackgroundResource( R.drawable.popupbutton_2_selector );
					break;

			}

			mIndicateArrow.setVisibility( layer == mArrowLayer ? View.VISIBLE
					: View.INVISIBLE );

		}
		catch ( Exception e )
		{
			ShowToast( "服务器繁忙，请重试！" );
		}
	}

	/**
	 * 
	 * planPath(用于规划路径线程的方法)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void planPath()
	{
		if ( !mGoalPlaceList.isEmpty() )
		{
			if ( mStartPos != null )
			{
				// 规划地图，取得规划后的路径集合
				Place place = mGoalPlaceList.remove( 0 );
				List< Path > mtempPaths = null;
				try
				{
					if ( place.getName().equals( "自定义地点" ) )
					{
						if ( mTouchGoalPos.getZ() == -1 )
						{
							mTouchGoalPos.setY( (int) ( mTouchGoalPos.getY() ) );
						}
						mtempPaths = mGuider.routeSearch( mNewStartPos,
								mTouchGoalPos.getZ() + 1, mTouchGoalPos,
								place.getName() );
					}
					else
					{
						mtempPaths = mGuider.routeSearch( mNewStartPos,
								place.getLayerNo(), null, place.getName() );
					}
				}
				catch ( NullPointerException e )
				{
					ShowToast( "服务器繁忙，请稍后再试" );
				}
				if ( mtempPaths != null )
				{
					mNewStartPos = mtempPaths.get( mtempPaths.size() - 1 )
							.getGoalPoint();
					mRoutes.add( new Route( mtempPaths ) );
					setResultPaths( mtempPaths );
				}
			}
		}
	}

	/**
	 * json网络线程
	 */
	private Runnable jsonRunnable = new Runnable()
	{
		@ Override
		public void run()
		{
			try
			{
				JsonManger.init( contectUrl );
				while ( true )
				{
					String temp = JsonManger.readParse();
					if ( temp != null )
					{
						Message message = new Message();
						message.what = MainActivity.GETJSONPOINT;
						message.obj = DealData.get3data( new String( temp ) );
						mHandler.sendMessage( message ); // 向Handler发送消息,更新UI
					}

					if ( mLocationMode != Model.COMMON_MODE )
					{
						if ( !mRoutes.isEmpty() )
						{
							Route pRoute = mRoutes.element();
							if ( mLocationPath == null )
							{
								getLocationPath( pRoute );
							}
							if ( mLocationPath != null
									&& mLocationPath.getmDirectedPath() != null )
							{
								TipsRun( pRoute );
							}
						}
					}
				}

			}
			catch ( Exception e )
			{
				CycleThread = null;
				CycleThread = new Thread( jsonRunnable );
				CycleThread.start();
			}

		}
	};

	/**
	 * 引导提示的线程
	 */
	private Runnable TipsRunnable = new Runnable()
	{
		@ Override
		public void run()
		{
			try
			{
				while ( true )
				{
					if ( mLocationMode != Model.COMMON_MODE )
					{
						if ( !mRoutes.isEmpty() )
						{
							Route pRoute = mRoutes.element();
							if ( mLocationPath == null )
							{
								getLocationPath( pRoute );
							}
							if ( mLocationPath != null
									&& mLocationPath.getmDirectedPath() != null )
							{
								Message message = new Message();
								message.what = TIPS;
								message.obj = DealData
										.SpeakDirection( mLocationPath
												.getmDirectedPath());
								mHandler.sendMessage( message );
							}
						}
					}
					Thread.sleep( 8000 );
				}
			}
			catch ( Exception e )
			{
				mTipsThread = null;
				mTipsThread = new Thread( TipsRunnable );
				mTipsThread.start();
			}
		}

	};

	public synchronized void setResultPaths( List< Path > mResultPaths )
	{
		this.mResultPaths = mResultPaths;
	}

	/**
	 * DismissThePopupwindow 如果层选项卡未收起，则将选项卡收起
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	private void DismissThePopupwindow()
	{
		if ( titlePopup != null && titlePopup.isShowing() )
		{
			titlePopup.dismiss();
		}
	}

	/**
	 * RegistListener 注册传感器监听
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	private void RegistListener()
	{
		sensorManager = (SensorManager) getSystemService( Context.SENSOR_SERVICE );
		Sensor magneticSensor = sensorManager
				.getDefaultSensor( Sensor.TYPE_MAGNETIC_FIELD );
		Sensor accelerometerSensor = sensorManager
				.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
		sensorManager.registerListener( mSensorListener.getListener(),
				magneticSensor, SensorManager.SENSOR_DELAY_GAME );
		sensorManager.registerListener( mSensorListener.getListener(),
				accelerometerSensor, SensorManager.SENSOR_DELAY_GAME );
	}

	private void openVoice( int flag )
	{
		String temp = new SharedPreferencesOP( this ).ReadSP( "useVoice" );
		// 如果启用语音，则播报语音
		if ( temp.equals( "yes" ) || temp.equals( "" ) )
		{
			// 初始化播音类
			mSoundSpeaker = new SoundSpeaker( this );
			mSound = new Sound( mSoundSpeaker );
			if ( new SharedPreferencesOP( this ).ReadSP( "voiceSex" ).equals(
					"woman" ) )
			{
				mSoundSpeaker.setVoiceSex( false );
			}
			else if ( new SharedPreferencesOP( this ).ReadSP( "voiceSex" )
					.equals( "man" ) )
			{
				mSoundSpeaker.setVoiceSex( true );
			}

			if ( flag == 1 )
			{
				mSoundSpeaker.SpeakString( "欢迎使用掠天之翼" );
			}
		}
	}

	/**
	 * onResume Activity生命周期中的恢复方法
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	@ Override
	protected void onResume()
	{
		super.onResume();
		mEditText.clearFocus();
		if ( titlePopup == null )
		{
			CreateTitlePopup();
		}
		RegistListener();// 注册传感器监听
	}

	/**
	 * onPause Activity生命周期中的暂停方法
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	@ Override
	protected void onPause()
	{
		super.onPause();
		if ( sensorManager != null )
		{
			sensorManager.unregisterListener( mSensorListener.getListener() );
		}
		if ( mSoundSpeaker != null )
		{
			mSoundSpeaker = null;
		}
	}

	/**
	 * onDestroy Activity生命周期中的销毁方法
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	@ Override
	protected void onDestroy()
	{
		super.onDestroy();
		if ( sensorManager != null )
		{
			sensorManager.unregisterListener( mSensorListener.getListener() );
		}
	}

	/**
	 * getmBitmap_Map 获取当前地图图层图片
	 * 
	 * @param int 层数
	 * @return Bitmap 当前图层的图片
	 * @since 1.0.0
	 */
	public static Bitmap getmBitmap_Map( int pos )
	{
		return mBitmap_Map[pos];
	}

	/**
	 * onBackPressed 按两下 Back 键退出
	 * 
	 * @param void
	 * @return void
	 * @since 1.0.0
	 */
	@ Override
	public void onBackPressed()
	{
		if ( ( System.currentTimeMillis() - mExitTime ) > 2000 )
		{
			ShowToast( "再按一次退出程序" );
			mExitTime = System.currentTimeMillis();
		}
		else
		{
			finish();
		}
	}

	/**
	 * 
	 * PrepareThread(线程准备)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void PrepareThread()
	{
		CycleThread = new Thread( jsonRunnable );
		CycleThread.start();
		guiderRouteRunnable = new GuiderRunRunnable( mHandler, mGuider );

		mTipsThread = new Thread( TipsRunnable );
		mTipsThread.start();
	}

	/**
	 * 
	 * FindViewById(通过id得到控件的实例)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void FindViewsById()
	{
		// 绑定控件
		imageButton_zhinanzhen = (ImageButton) findViewById( R.id.zhinanzhen );
		mPopupWindow = (ImageButton) findViewById( R.id.button_select_floor );
		mEditText = (EditText) findViewById( R.id.Main_Edittext );
		mMarqueeText = (MyMarqueeTextView) findViewById( R.id.marquee_hint );
		mLveTianMapView = (LveTianMapView) findViewById( R.id.LveTianMap );
		mButton_GoHere = (Button) findViewById( R.id.button_GoHere );
		mButton_GlobalPreview = (Button) findViewById( R.id.button_GlobalPreview );
		mButton_Luxian = (Button) findViewById( R.id.button_Luxian );
		mSettings = (Button) findViewById( R.id.Setting );
		imageButton_add = (ImageButton) findViewById( R.id.imagebutton_add );
		imageButton_minus = (ImageButton) findViewById( R.id.imagebutton_minus );
		myProgressBar = (MyProgressBar) mDialog
		 .findViewById(R.id.roundProgressBar);
		// View 图层初始化设置
		mSurfaceView = (SurfaceViewDraw) findViewById( R.id.SurfaveView );
		mIndicateArrow = (IndicateArrow) findViewById( R.id.IndicateArrow );
	}

	/**
	 * 
	 * FirstShow(第一次打开应用)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void FirstShow()
	{
		mCheck = new CheckIfFirst( this );
		if ( mCheck.isFirst() )
		{
			startActivity( new Intent( this, FirstTime_Activity.class ) );
		}
		mCheck = null;
	}

	/**
	 * 
	 * PrepareIp(预处理ip地址)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void PrepareIp()
	{
		contectUrl = WelcomeActivity.GetIP();// 获取本地 WiFi IP 地址
		contectUrl = "http://" + contectUrl + ":8001/loc";
	}

	/**
	 * 
	 * setRoutesArray(用于走偏，所有路径的数组)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void setRoutesArray()
	{
		mRoutes.clear();
		mLocationPath=null;
	}

	/**
	 * 
	 * Printer(显示文本)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void ShowToast( String text )
	{
		if ( text != null )
		{
			Toast.makeText( this, text, Toast.LENGTH_SHORT ).show();
		}
	}

	/**
	 * 
	 * DrawView(画图)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void DrawView(boolean isEnd)
	{	
		mDrawImage.init( mLveTianMapView ,mHandler);
		mDrawImage.drawLineOnImage( mActivity, mResultPaths, moveInch,isEnd);
		mBitmap_Route = mDrawImage.getmBitmap();
		mSurfaceView.setImageBitmap( mBitmap_Route[mLveTianMapView
					.getmCurrentFloor()] );
	}
	
	/**
	 * 
	 * DrawView(画图)
	 * 
	 * void
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private void DrawView()
	{
		boolean isEnd=false;
		if(!mRoutes.isEmpty())
		{
			int size = mRoutes.size();
			if(size==1)
			{
				isEnd=true;
			}
		}
		
		mDrawImage.init( mLveTianMapView ,mHandler);
		mDrawImage.drawLineOnImage( mActivity, mResultPaths, moveInch,isEnd);
		mBitmap_Route = mDrawImage.getmBitmap();
		mSurfaceView.setImageBitmap( mBitmap_Route[mLveTianMapView
					.getmCurrentFloor()] );
	}

	/**
	 * 
	 * changeMode(修改模式)
	 * 
	 * @param mode
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	private void ChangeMode( Model mode )
	{
		mLocationMode = mode;
	}

	/**
	 * 
	 * checkedPoint(检查坐标点是否符合要求)
	 * 
	 * @param pPoint
	 * @return boolean
	 * @exception
	 * @since 1.0.0
	 */
	private boolean checkedPoint( PixelPoint pPoint )
	{
		if ( pPoint.getIndex_X() < 0 || pPoint.getIndex_Y() > 50 )
		{
			return false;
		}
		return true;
	}

	/**
	 * 
	 * TipsRun(方向导航提示)
	 * 
	 * @param pRoute
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void TipsRun( Route pRoute )
	{

		PixelPoint point = mLocationPath.getmDirectedPath().getmEndPoint();
		if ( point != null )
		{
			if ( point.getInstance( mStartPos ) < 2 )
			{
				mLocationPath.setmDirectedPath( mLocationPath
						.getmDirectedPath().getmNextDirectedPath() );
				if ( mLocationPath.getmDirectedPath() != null )
				{
					Message message = new Message();
					message.what = TIPS;
					message.obj = DealData.SpeakDirection( mLocationPath
							.getmDirectedPath());
					mHandler.sendMessage( message );
				}
			}
		}

	}

	/**
	 * 
	 * getLocationPath(获取当前的路径)
	 * 
	 * @param pRoute
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	private void getLocationPath( Route pRoute )
	{
		for ( int i = 0; i < pRoute.getmSize(); i++ )
		{
			if ( i == 0 )
			{
				if ( pRoute.getmStartPath().getmDirectedPath() != null )
				{
					mLocationPath = pRoute.getmStartPath();
					break;
				}
			}

			if ( i == pRoute.getmSize() - 1 )
			{
				if ( pRoute.getmEndPath().getmDirectedPath() != null )
				{
					mLocationPath = pRoute.getmEndPath();
					break;
				}
			}
			mLocationPath = pRoute.getmResultPaths().get( i );
			if ( mLocationPath.getmDirectedPath() != null )
			{
				break;
			}
		}
		//System.out.println( mLocationPath.getmDirectedPath() );
	}
}