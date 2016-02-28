package com.lvetianzhiyi.route.activity;

import com.lvetianzhiyi.route.MainActivity;
import com.lvetianzhiyi.route.R;
import com.lvetianzhiyi.route.utils.ExitApplication;
import com.lvetianzhiyi.route.utils.SharedPreferencesOP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Setting_Activity extends Activity implements OnClickListener,
		OnCheckedChangeListener
{
	private boolean Light;
	private Button mBtnBack;
	private Button mBtnExit;
	private RelativeLayout rl_about_us;
	private RelativeLayout rl_refresh_ver;
	private LinearLayout ll_manwoman;
	private CheckBox mCheckBox;
	private RadioButton mRaBtn_man, mRaBtn_woman;
	private ImageButton mBtnAlwaysLight;
	private SharedPreferencesOP mSFOP;

	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		requestWindowFeature( Window.FEATURE_NO_TITLE );// 去掉标题栏
		setContentView( R.layout.activity_setting );
		ExitApplication.getInstance().addActivity( this );

		Init();// 初始化控件
		AddListener();// 添加监听事件
		InitOperate();// 初始化操作
	}

	private void Init()
	{
		TextView textView = (TextView) findViewById( R.id.tvTitle );
		textView.setText( "设置" );
		mBtnBack = (Button) findViewById( R.id.back );
		mBtnExit = (Button) findViewById( R.id.exit_btn );
		mBtnAlwaysLight = (ImageButton) findViewById( R.id.alwaysLight );
		mSFOP = new SharedPreferencesOP( this );
		rl_about_us = (RelativeLayout) findViewById( R.id.rl_about_us );
		rl_refresh_ver = (RelativeLayout) findViewById( R.id.refresh_version );
		ll_manwoman = (LinearLayout) findViewById( R.id.ll_manwoman );
		mCheckBox = (CheckBox) findViewById( R.id.check_yuyin );
		mRaBtn_man = (RadioButton) findViewById( R.id.radio_man );
		mRaBtn_woman = (RadioButton) findViewById( R.id.radio_woman );
	}

	private void AddListener()
	{
		mBtnBack.setOnClickListener( this );
		mBtnAlwaysLight.setOnClickListener( this );
		rl_about_us.setOnClickListener( this );
		rl_refresh_ver.setOnClickListener( this );
		mCheckBox.setOnCheckedChangeListener( this );
		mRaBtn_man.setOnClickListener( this );
		mRaBtn_woman.setOnClickListener( this );
		mBtnExit.setOnTouchListener( new OnTouchListener()
		{
			@ SuppressLint( "ClickableViewAccessibility" )
			@ Override
			public boolean onTouch( View v, MotionEvent event )
			{
				switch ( event.getAction() )
				{
					case MotionEvent.ACTION_DOWN :
						mBtnExit.setTextColor( Color.GRAY );
						dialog();
						break;

					case MotionEvent.ACTION_UP :
						mBtnExit.setTextColor( Color.WHITE );
						break;

					default :
						break;
				}
				return false;
			}
		} );
	}

	private void InitOperate()
	{
		String flagString = mSFOP.ReadSP( "alwaysLight" );
		if ( flagString.equals( "" ) )
		{
			mSFOP.WriteSP( "alwaysLight", "closed" );
			mBtnAlwaysLight.setImageDrawable( getResources().getDrawable(
					R.drawable.toggle_close ) );
			Light = false;
		}
		else
		{
			if ( flagString.equals( "opened" ) )
			{
				mBtnAlwaysLight.setImageDrawable( getResources().getDrawable(
						R.drawable.toggle_on ) );
				Light = true;
			}
			else
			{
				mBtnAlwaysLight.setImageDrawable( getResources().getDrawable(
						R.drawable.toggle_close ) );
				Light = false;
			}
		}

		flagString = mSFOP.ReadSP( "useVoice" );
		String flagString2 = mSFOP.ReadSP( "voiceSex" );
		if ( flagString.equals( "" ) )
		{
			mCheckBox.setChecked( true );
			ll_manwoman.setVisibility( View.VISIBLE );
			mRaBtn_man.setChecked( false );
			mRaBtn_woman.setChecked( true );
			mSFOP.WriteSP( "useVoice", "yes" );
			mSFOP.WriteSP( "voiceSex", "woman" );
		}
		else
		{
			if ( flagString.equals( "yes" ) )
			{
				mCheckBox.setChecked( true );
				ll_manwoman.setVisibility( View.VISIBLE );

				if ( flagString2.equals( "" ) )
				{
					mRaBtn_man.setChecked( false );
					mRaBtn_woman.setChecked( true );
					mSFOP.WriteSP( "voiceSex", "woman" );
				}
				else
				{
					if ( flagString2.equals( "man" ) )
					{
						mRaBtn_man.setChecked( true );
						mRaBtn_woman.setChecked( false );
					}
					else
					{
						mRaBtn_man.setChecked( false );
						mRaBtn_woman.setChecked( true );
					}
				}
			}
			else
			{
				mCheckBox.setChecked( false );
				ll_manwoman.setVisibility( View.GONE );
				mRaBtn_man.setChecked( false );
				mRaBtn_woman.setChecked( false );
			}
		}
	}

	@ Override
	public void onClick( View v )
	{
		int which = v.getId();
		switch ( which )
		{
			case R.id.back :
				Intent intent = new Intent();
				intent.putExtra( "alwaysLight", Light );
				setResult( MainActivity.CONST_SETTINGS_RESULT, intent );
				finish();
				overridePendingTransition( R.anim.zoom_enter_setting,
						R.anim.zoom_exit_setting );
				break;

			case R.id.alwaysLight :
				if ( Light )
				{
					mBtnAlwaysLight.setImageDrawable( getResources()
							.getDrawable( R.drawable.toggle_close ) );
					Light = false;
					mSFOP.WriteSP( "alwaysLight", "closed" );
				}
				else
				{
					mBtnAlwaysLight.setImageDrawable( getResources()
							.getDrawable( R.drawable.toggle_on ) );
					Light = true;
					mSFOP.WriteSP( "alwaysLight", "opened" );
				}
				break;

			case R.id.rl_about_us :
				Intent intent2 = new Intent( Setting_Activity.this,
						AboutUs_Activity.class );
				startActivity( intent2 );
				break;

			case R.id.refresh_version :
				new Builder( this )
						.setMessage( "\n正式发布后将添加更新功能\n" )
						.setTitle( "版本更新" )
						.setPositiveButton( "确认",
								new DialogInterface.OnClickListener()
								{
									@ Override
									public void onClick(
											DialogInterface dialog, int which )
									{
										dialog.dismiss();
									}
								} ).create().show();
				break;

			case R.id.radio_man :
				mRaBtn_man.setChecked( true );
				mRaBtn_woman.setChecked( false );
				mSFOP.WriteSP( "voiceSex", "man" );
				break;

			case R.id.radio_woman :
				mRaBtn_man.setChecked( false );
				mRaBtn_woman.setChecked( true );
				mSFOP.WriteSP( "voiceSex", "woman" );
				break;

			default :
		}
	}

	@ Override
	public void onCheckedChanged( CompoundButton buttonView, boolean isChecked )
	{
		switch ( buttonView.getId() )
		{
			case R.id.check_yuyin :
				if ( isChecked )
				{
					ll_manwoman.setAnimation( Search_Activity
							.CreateAnimation( 1 ) );
					ll_manwoman.setVisibility( View.VISIBLE );
					mRaBtn_man.setChecked( false );
					mRaBtn_woman.setChecked( true );
					mSFOP.WriteSP( "useVoice", "yes" );
					mSFOP.WriteSP( "voiceSex", "woman" );
				}
				else
				{
					ll_manwoman.setVisibility( View.GONE );
					mRaBtn_man.setChecked( false );
					mRaBtn_woman.setChecked( false );
					mSFOP.WriteSP( "useVoice", "no" );
				}
				break;

			default :
				break;
		}
	}

	private void dialog()
	{
		AlertDialog.Builder builder = new Builder( this );
		builder.setMessage( "\n确定退出应用吗？\n" );

		builder.setTitle( "温馨提示" );

		builder.setNegativeButton( "取消", new DialogInterface.OnClickListener()
		{
			@ Override
			public void onClick( DialogInterface dialog, int which )
			{
				mBtnExit.setTextColor( Color.WHITE );
				dialog.dismiss();
			}
		} );

		builder.setPositiveButton( "确认", new DialogInterface.OnClickListener()
		{
			@ Override
			public void onClick( DialogInterface dialog, int which )
			{
				dialog.dismiss();
				ExitApplication.getInstance().exit();
				android.os.Process.killProcess( android.os.Process.myPid() );
				System.exit( 0 );
			}
		} );

		builder.create().show();
	}

	@ Override
	public void onBackPressed()
	{
		super.onBackPressed();
		Intent intent = new Intent();
		intent.putExtra( "alwaysLight", Light );
		setResult( MainActivity.CONST_SETTINGS_RESULT, intent );
		finish();
		overridePendingTransition( R.anim.zoom_enter_setting,
				R.anim.zoom_exit_setting );
	}

};