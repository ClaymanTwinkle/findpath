package com.lvetianzhiyi.route.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lvetianzhiyi.route.MainActivity;
import com.lvetianzhiyi.route.R;
import com.lvetianzhiyi.route.adapter.AutoCompleteAdapter;

/**
 * 类 Search_Activity 搜索Activity
 * 
 * @author xinyi
 * @继承 Activity
 * @接口 OnClickListener
 * @接口 OnTouchListener
 * @since 1.0.0
 */
public class Search_Activity extends Activity implements OnClickListener,
		OnTouchListener
{
	// 用户所在层
	private int mLayer;
	// 智能排序
	private CheckBox mSmart_CheckBox;
	// 智能排序标识
	private boolean ifSmart;
	// 返回按钮
	private Button mSearchBack_button;
	// 开始规划按钮
	private Button mSearch_OK_button;
	// 快捷目标地点按钮
	private Button mFast[];
	// 自动联想搜索框适配器
	private AutoCompleteAdapter< String > adapter;
	// 自动联想搜索框
	private AutoCompleteTextView aGoalTextView[];
	// 第一搜索框的扩展按钮
	private ImageButton search_rt1_jia;
	// 第二搜索框的父容器
	private RelativeLayout relativeLayout2;
	private ImageButton search_rt2_jia;// 第二搜索框的扩展按钮
	private ImageButton search_rt2_jian;// 第二搜索框的删减按钮
	// 第三搜索框相关控件
	private RelativeLayout relativeLayout3;
	private ImageButton search_rt3_jia;
	private ImageButton search_rt3_jian;
	// 第四搜索框相关控件
	private RelativeLayout relativeLayout4;
	private ImageButton search_rt4_jia;
	private ImageButton search_rt4_jian;
	// 第五搜索框相关控件
	private RelativeLayout relativeLayout5;
	private ImageButton search_rt5_jian;

	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		requestWindowFeature( Window.FEATURE_NO_TITLE );// 去掉标题栏
		setContentView( R.layout.activity_search );

		Intent intent = getIntent();
		mLayer = intent.getIntExtra( "user_layer", 1 );

		Init();// 初始化控件
		AddListener();// 添加监听事件
		InitOperate();// 初始化操作
	}

	private void Init()
	{

		ifSmart = true;
		mSmart_CheckBox = (CheckBox) findViewById( R.id.check_smartOrder );
		mSearchBack_button = (Button) findViewById( R.id.back );
		mSearch_OK_button = (Button) findViewById( R.id.search_ok_button );
		TextView textView = (TextView) findViewById( R.id.tvTitle );
		textView.setText( "多地点寻路" );
		mFast = new Button[8];
		adapter = new AutoCompleteAdapter< String >( this,
				R.layout.search_edittext_dropdown, ArrayToList() );
		relativeLayout2 = (RelativeLayout) findViewById( R.id.search_relativelayout2 );
		relativeLayout3 = (RelativeLayout) findViewById( R.id.search_relativelayout3 );
		relativeLayout4 = (RelativeLayout) findViewById( R.id.search_relativelayout4 );
		relativeLayout5 = (RelativeLayout) findViewById( R.id.search_relativelayout5 );
		aGoalTextView = new AutoCompleteTextView[5];
		aGoalTextView[0] = (AutoCompleteTextView) findViewById( R.id.search_Edittext1 );
		aGoalTextView[1] = (AutoCompleteTextView) findViewById( R.id.search_Edittext2 );
		aGoalTextView[2] = (AutoCompleteTextView) findViewById( R.id.search_Edittext3 );
		aGoalTextView[3] = (AutoCompleteTextView) findViewById( R.id.search_Edittext4 );
		aGoalTextView[4] = (AutoCompleteTextView) findViewById( R.id.search_Edittext5 );
		mFast[0] = (Button) findViewById( R.id.fast_1 );
		mFast[1] = (Button) findViewById( R.id.fast_2 );
		mFast[2] = (Button) findViewById( R.id.fast_3 );
		mFast[3] = (Button) findViewById( R.id.fast_4 );
		mFast[4] = (Button) findViewById( R.id.fast_5 );
		mFast[5] = (Button) findViewById( R.id.fast_6 );
		mFast[6] = (Button) findViewById( R.id.fast_7 );
		mFast[7] = (Button) findViewById( R.id.fast_8 );

		for ( int i = 0; i < aGoalTextView.length; i++ )
		{
			aGoalTextView[i].setAdapter( adapter );
			aGoalTextView[i].setThreshold( 1 );
		}
		for ( int i = 0; i < mFast.length; i++ )
		{
			mFast[i].setOnTouchListener( this );
		}
		search_rt1_jia = (ImageButton) findViewById( R.id.search_rl1_jia1 );
		search_rt2_jia = (ImageButton) findViewById( R.id.search_rl1_jia2 );
		search_rt2_jian = (ImageButton) findViewById( R.id.search_rl1_jian2 );
		search_rt3_jia = (ImageButton) findViewById( R.id.search_rl1_jia3 );
		search_rt3_jian = (ImageButton) findViewById( R.id.search_rl1_jian3 );
		search_rt4_jia = (ImageButton) findViewById( R.id.search_rl1_jia4 );
		search_rt4_jian = (ImageButton) findViewById( R.id.search_rl1_jian4 );
		search_rt5_jian = (ImageButton) findViewById( R.id.search_rl1_jian5 );
	}

	private void AddListener()
	{
		mSmart_CheckBox
				.setOnCheckedChangeListener( new OnCheckedChangeListener()
				{
					@ Override
					public void onCheckedChanged( CompoundButton buttonView,
							boolean isChecked )
					{
						if ( isChecked )
						{
							ifSmart = true;
						}
						else
						{
							ifSmart = false;
						}
					}
				} );
		mSearchBack_button.setOnClickListener( this );
		mSearch_OK_button.setOnTouchListener( this );
		search_rt1_jia.setOnClickListener( this );
		search_rt2_jia.setOnClickListener( this );
		search_rt2_jian.setOnClickListener( this );
		search_rt3_jia.setOnClickListener( this );
		search_rt3_jian.setOnClickListener( this );
		search_rt4_jia.setOnClickListener( this );
		search_rt4_jian.setOnClickListener( this );
		search_rt5_jian.setOnClickListener( this );
	}

	private List< String > ArrayToList()
	{
		List< String > placeList = new ArrayList< String >();

		for ( String place : placeArray )
		{
			placeList.add( place );
		}

		return placeList;
	}

	@ Override
	public void onClick( View v )
	{
		int tag = v.getId();
		switch ( tag )
		{
			case R.id.back :
				finish();
				overridePendingTransition( R.anim.slide_bottom_in,
						R.anim.slide_bottom_out );
				break;
			case R.id.search_rl1_jia1 :
				// 设置第一搜索框的扩展按钮动画
				relativeLayout2.setAnimation( CreateAnimation( 1 ) );
				// 点击后第二搜索框控件出现
				relativeLayout2.setVisibility( View.VISIBLE );
				// 设置第二搜索框的焦点
				aGoalTextView[1].requestFocus();
				break;
			case R.id.search_rl1_jia2 :
				relativeLayout3.setAnimation( CreateAnimation( 1 ) );
				relativeLayout3.setVisibility( View.VISIBLE );
				aGoalTextView[2].requestFocus();
				break;
			case R.id.search_rl1_jian2 :
				// 清除第二搜索框的文字信息
				aGoalTextView[1].setText( null );
				// 设置第二搜索框的扩展按钮动画
				relativeLayout2.setAnimation( CreateAnimation( 2 ) );
				// 设置第二搜索框控件不可见
				relativeLayout2.setVisibility( View.GONE );
				// 设置第一搜索框的焦点
				aGoalTextView[0].requestFocus();
				break;
			case R.id.search_rl1_jia3 :
				relativeLayout4.setAnimation( CreateAnimation( 1 ) );
				relativeLayout4.setVisibility( View.VISIBLE );
				aGoalTextView[3].requestFocus();
				break;
			case R.id.search_rl1_jian3 :
				aGoalTextView[2].setText( null );
				relativeLayout3.setAnimation( CreateAnimation( 2 ) );
				relativeLayout3.setVisibility( View.GONE );
				aGoalTextView[1].requestFocus();
				break;
			case R.id.search_rl1_jia4 :
				relativeLayout5.setAnimation( CreateAnimation( 1 ) );
				relativeLayout5.setVisibility( View.VISIBLE );
				aGoalTextView[4].requestFocus();
				break;
			case R.id.search_rl1_jian4 :
				aGoalTextView[3].setText( null );
				relativeLayout4.setAnimation( CreateAnimation( 2 ) );
				relativeLayout4.setVisibility( View.GONE );
				aGoalTextView[2].requestFocus();
				break;
			case R.id.search_rl1_jian5 :
				aGoalTextView[4].setText( null );
				relativeLayout5.setAnimation( CreateAnimation( 2 ) );
				relativeLayout5.setVisibility( View.GONE );
				aGoalTextView[3].requestFocus();
				break;

			default :
				break;
		}
	}

	@ SuppressLint( "ClickableViewAccessibility" )
	@ Override
	public boolean onTouch( View v, MotionEvent event )
	{
		int tag = v.getId();
		switch ( tag )
		{
			case R.id.search_ok_button :
				switch ( event.getAction() )
				{
					case MotionEvent.ACTION_DOWN :
						for ( int i = 0; i < aGoalTextView.length; i++ )
						{
							// 隐藏输入法框
							( (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE ) )
									.hideSoftInputFromWindow(
											aGoalTextView[i].getWindowToken(),
											0 );
						}
						mSearch_OK_button.setTextColor( Color.GRAY );
						sendUserInput();// 发送用户输入的目的地点信息
						break;

					case MotionEvent.ACTION_UP :
						mSearch_OK_button.setTextColor( Color.WHITE );
						break;

					default :
						break;
				}
				break;

			case R.id.fast_1 :
				switch ( event.getAction() )
				{
					case MotionEvent.ACTION_DOWN :
						mFast[0].setBackgroundColor( Color.rgb( 235, 235, 235 ) );
						aGoalTextView[FindWhichEdittextIsFoucus()]
								.setText( mFast[0].getText() );
						break;

					case MotionEvent.ACTION_UP :
						mFast[0].setBackgroundColor( Color.rgb( 255, 255, 255 ) );
						break;
				}
				break;

			case R.id.fast_2 :
				switch ( event.getAction() )
				{
					case MotionEvent.ACTION_DOWN :
						mFast[1].setBackgroundColor( Color.rgb( 235, 235, 235 ) );
						aGoalTextView[FindWhichEdittextIsFoucus()]
								.setText( mFast[1].getText() );
						break;

					case MotionEvent.ACTION_UP :
						mFast[1].setBackgroundColor( Color.rgb( 255, 255, 255 ) );
						break;
				}
				break;
			case R.id.fast_3 :
				switch ( event.getAction() )
				{
					case MotionEvent.ACTION_DOWN :
						mFast[2].setBackgroundColor( Color.rgb( 235, 235, 235 ) );
						aGoalTextView[FindWhichEdittextIsFoucus()]
								.setText( mFast[2].getText() );
						break;

					case MotionEvent.ACTION_UP :
						mFast[2].setBackgroundColor( Color.rgb( 255, 255, 255 ) );
						break;
				}
				break;
			case R.id.fast_4 :
				switch ( event.getAction() )
				{
					case MotionEvent.ACTION_DOWN :
						mFast[3].setBackgroundColor( Color.rgb( 235, 235, 235 ) );
						aGoalTextView[FindWhichEdittextIsFoucus()]
								.setText( mFast[3].getText() );
						break;

					case MotionEvent.ACTION_UP :
						mFast[3].setBackgroundColor( Color.rgb( 255, 255, 255 ) );
						break;
				}
				break;
			case R.id.fast_5 :
				switch ( event.getAction() )
				{
					case MotionEvent.ACTION_DOWN :
						mFast[4].setBackgroundColor( Color.rgb( 235, 235, 235 ) );
						aGoalTextView[FindWhichEdittextIsFoucus()]
								.setText( mFast[4].getText() );
						break;

					case MotionEvent.ACTION_UP :
						mFast[4].setBackgroundColor( Color.rgb( 255, 255, 255 ) );
						break;
				}
				break;
			case R.id.fast_6 :
				switch ( event.getAction() )
				{
					case MotionEvent.ACTION_DOWN :
						mFast[5].setBackgroundColor( Color.rgb( 235, 235, 235 ) );
						aGoalTextView[FindWhichEdittextIsFoucus()]
								.setText( mFast[5].getText() );
						break;

					case MotionEvent.ACTION_UP :
						mFast[5].setBackgroundColor( Color.rgb( 255, 255, 255 ) );
						break;
				}
				break;
			case R.id.fast_7 :
				switch ( event.getAction() )
				{
					case MotionEvent.ACTION_DOWN :
						mFast[6].setBackgroundColor( Color.rgb( 235, 235, 235 ) );
						aGoalTextView[FindWhichEdittextIsFoucus()]
								.setText( mFast[6].getText() );
						break;

					case MotionEvent.ACTION_UP :
						mFast[6].setBackgroundColor( Color.rgb( 255, 255, 255 ) );
						break;
				}
				break;
			case R.id.fast_8 :
				switch ( event.getAction() )
				{
					case MotionEvent.ACTION_DOWN :
						mFast[7].setBackgroundColor( Color.rgb( 235, 235, 235 ) );
						aGoalTextView[FindWhichEdittextIsFoucus()]
								.setText( mFast[7].getText() );
						break;

					case MotionEvent.ACTION_UP :
						mFast[7].setBackgroundColor( Color.rgb( 255, 255, 255 ) );
						break;
				}
				break;

			default :
				break;
		}
		return false;
	}

	private void InitOperate()
	{
		relativeLayout2.setVisibility( View.GONE );
		relativeLayout3.setVisibility( View.GONE );
		relativeLayout4.setVisibility( View.GONE );
		relativeLayout5.setVisibility( View.GONE );
	}

	private int FindWhichEdittextIsFoucus()
	{
		for ( int i = 0; i < aGoalTextView.length; i++ )
		{
			if ( aGoalTextView[i].hasFocus() )
				return i;
		}
		return 0;
	}

	// 发送用户输入的目的点
	@ SuppressLint( "UseSparseArrays" )
	private void sendUserInput()
	{
		int ifAllEmpty = 0;
		for ( int i = 0; i < aGoalTextView.length; i++ )
		{
			if ( aGoalTextView[i].getText().toString().trim() == null
					|| aGoalTextView[i].getText().toString().trim().equals( "" ) )
				ifAllEmpty++;
		}
		if ( 5 == ifAllEmpty )
		{
			Toast.makeText( this, "请输入目的地点", Toast.LENGTH_SHORT ).show();
			return;
		}
		Intent intent = new Intent();
		ArrayList< String > temp = new ArrayList< String >();
		Set< String > set = new HashSet< String >();
		// 将输入框中的信息检索、记录并出去空格及去重
		for ( int i = 0; i < aGoalTextView.length; i++ )
		{
			if ( aGoalTextView[i].getText().toString().trim() != null
					&& !aGoalTextView[i].getText().toString().trim()
							.equals( "" ) )
			{
				String info = aGoalTextView[i].getText().toString().trim()
						.toUpperCase( Locale.CHINA );
				if ( set.add( info ) )
					temp.add( info );
			}
		}

		// 将目的地点放入对象流传递至 MainActivity
		if ( ifSmart )
		{
			ArrayList< String > userInput = new ArrayList< String >();
			userInput = makeOrder( mLayer, temp );
			intent.putStringArrayListExtra( "UserInput", userInput );
		}
		else
		{
			intent.putStringArrayListExtra( "UserInput", temp );
		}

		setResult( MainActivity.CONST_SEARCH_ACTIVITY_RESULT, intent );
		finish();
		overridePendingTransition( R.anim.slide_bottom_in,
				R.anim.slide_bottom_out );
	}

	private ArrayList< String > makeOrder( int flag, ArrayList< String > temp )
	{
		ArrayList< String > userInput = new ArrayList< String >();
		String compareString1 = null, compareString2 = null;
		switch ( flag )
		{
			case 0 :
				compareString1 = "F2";
				compareString2 = "F1";
				break;

			case 1 :
				compareString1 = "F2";
				compareString2 = "B1";
				break;

			case 2 :
				compareString1 = "B1";
				compareString2 = "F1";
				break;

			default :
				break;
		}
		for ( int i = 0; i < temp.size(); i++ )
		{
			String info = temp.get( i );
			if ( info.startsWith( compareString1 ) )
			{
				temp.remove( i );
				userInput.add( info );
				i = -1;
			}
		}
		for ( int i = 0; i < temp.size(); i++ )
		{
			String info = temp.get( i );
			if ( info.startsWith( compareString2 ) )
			{
				temp.remove( i );
				userInput.add( info );
				i = -1;
			}
		}
		for ( int i = 0; i < temp.size(); i++ )
		{
			userInput.add( temp.get( i ) );
		}
		return userInput;
	}

	@ Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
		overridePendingTransition( R.anim.slide_bottom_in,
				R.anim.slide_bottom_out );
	}

	// 扩展、删减控件时的动画
	public static TranslateAnimation CreateAnimation( int flag )
	{
		if ( 1 == flag )
		{
			TranslateAnimation mShowAction = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f );
			mShowAction.setDuration( 500 );
			return mShowAction;
		}
		else
		{
			TranslateAnimation mHiddenAction = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f );
			mHiddenAction.setDuration( 500 );
			return mHiddenAction;
		}
	}

	final static public String[] placeArray = { "B1层 ES1", "B1层 ES2", "B1层 ES3",
			"B1层 ES4", "B1层 EL1", "B1层 EL2", "B1层 EL3", "B1层 EL4", "B1层 D1",
			"B1层 D2", "B1层 D3", "F1层 E1", "F1层 E2", "F1层 E3", "F1层 E4",
			"F1层 E5", "F1层 E6", "F1层 E7", "F1层 E8", "F1层 E9", "F1层 E10",
			"F1层 E11", "F1层 E12", "F1层 E13", "F1层 E14", "F1层 E15", "F1层 E16",
			"F1层 E17", "F1层 E18", "F1层 W1", "F1层 W2", "F1层 W3", "F1层 W4",
			"F1层 W5", "F1层 W6", "F1层 W7", "F1层 W8", "F1层 WT1", "F1层 WT2",
			"F1层 WT3", "F1层 WT4", "F1层 WT5", "F1层 WT6", "F1层 WT7", "F1层 WT8",
			"F1层 WT9", "F1层 WT10", "F1层 WT11", "F1层 WT12", "F1层 D1", "F1层 D2",
			"F1层 D3", "F1层 S1", "F1层 S2", "F1层 S3", "F1层 S4", "F1层 S5",
			"F1层 S6", "F1层 S7", "F1层 S8", "F1层 S9", "F1层 S10", "F1层 S11",
			"F1层 S12", "F1层 S13", "F1层 S14", "F1层 S15", "F1层 S16", "F1层 S17",
			"F1层 S18", "F1层 S19", "F1层 S20", "F1层 S21", "F1层 S22", "F1层 S23",
			"F1层 S24", "F1层 S25", "F1层 S26", "F1层 S27", "F1层 S28", "F1层 S29",
			"F1层 S30", "F1层 S31", "F1层 S32", "F1层 S33", "F1层 S34", "F1层 S35",
			"F1层 S36", "F1层 S37", "F1层 S38", "F1层 S39", "F1层 S40", "F1层 S41",
			"F1层 S42", "F1层 ES1", "F1层 ES2", "F1层 ES3", "F1层 ES4", "F1层 ES5",
			"F1层 ES6", "F1层 ES7", "F1层 ES8", "F1层 EL1", "F1层 EL2", "F1层 EL3",
			"F1层 EL4", "F1层 EL5", "F1层 EL6", "F1层 EL7", "F1层 EL8", "F1层 Q1",
			"F1层 Q2", "F1层 B1", "F1层 B2", "F1层 B3", "F1层 B4", "F1层 B5",
			"F1层 B6", "F2层 E1", "F2层 E2", "F2层 E3", "F2层 E4", "F2层 E5",
			"F2层 E6", "F2层 E7", "F2层 E8", "F2层 E9", "E10", "F2层 E11",
			"F2层 E12", "F2层 E13", "F2层 E14", "F2层 E15", "F2层 E16", "F2层 E17",
			"F2层 E18", "F2层 W1", "F2层 W2", "F2层 W3", "F2层 W4", "F2层 W5",
			"F2层 W6", "F2层 W7", "F2层 W8", "F2层 WT1", "F2层 WT2", "F2层 WT3",
			"F2层 WT4", "F2层 WT5", "F2层 WT6", "F2层 WT7", "F2层 WT8", "F2层 WT9",
			"F2层 WT10", "F2层 WT11", "F2层 WT12", "F2层 D1", "F2层 D2", "F2层 D3",
			"F2层 S1", "F2层 S2", "F2层 S3", "F2层 S4", "F2层 S5", "F2层 S6",
			"F2层 S7", "F2层 S8", "F2层 S9", "F2层 S10", "F2层 S11", "F2层 S12",
			"F2层 S13", "F2层 S14", "F2层 S15", "F2层 S16", "F2层 S17", "F2层 S18",
			"F2层 S19", "F2层 S20", "F2层 S21", "F2层 S22", "F2层 S23", "F2层 S24",
			"F2层 S25", "F2层 S26", "F2层 S27", "F2层 S28", "F2层 S29", "F2层 S30",
			"F2层 S31", "F2层 S32", "F2层 S33", "F2层 S34", "F2层 ES1", "F2层 ES2",
			"F2层 ES3", "F2层 ES4", "F2层 ES5", "F2层 ES6", "F2层 ES7", "F2层 ES8",
			"F2层 EL1", "F2层 EL2", "F2层 EL3", "F2层 EL4", "F2层 EL5", "F2层 EL6",
			"F2层 EL7", "F2层 EL8", "F2层 Q1", "F2层 Q2", "F2层 C1", "F2层 C2",
			"F2层 C3", "F2层 C4", "F2层 C5", "F2层 C6", "F2层 SC1", "F2层 SC2",
			"F2层 SC3" };

};