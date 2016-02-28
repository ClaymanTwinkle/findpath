package com.lvetianzhiyi.route;

import java.util.Timer;
import java.util.TimerTask;

import com.lvetianzhiyi.route.algorithm.BuildingDataTool;
import com.lvetianzhiyi.route.data.OnlineData;
import com.lvetianzhiyi.route.utils.GetIP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * 类 WelcomeActivity(程序欢迎页)
 * 
 * @author xinyi
 * 
 * @继承 Activity
 * 
 * @since 1.0.0
 */
public class WelcomeActivity extends Activity
{
	// 欢迎页显示的图片
	private ImageView welcomeImage;
	// 欢迎页显示的标题名
	private TextView welcomeTextView;
	// 所属商标
	private TextView shangBiao;
	// 相对布局
	private RelativeLayout relativeLayout;
	// 任务执行器
	private Timer timer;
	// 动画类
	private Animation animation;
	// 当前 Activity
	private Activity mActivity;
	// 旋转时间
	final static int FanRotateTime = 2000;
	// 动画持续时间
	final static int AnimLastTime = 4100;
	// 淡入时间
	final static int ShowYing = 1500;
	// 本地 IP 地址
	private static String IP = "";

	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.welcome_page );

		IP = GetIP.GetLocalIP( this );

		// 初始化控件
		mActivity = this;
		timer = new Timer();
		relativeLayout = (RelativeLayout) findViewById( R.id.welcome_layout );
		welcomeImage = (ImageView) findViewById( R.id.welcome_image );
		welcomeTextView = (TextView) findViewById( R.id.welcome_text );
		shangBiao = (TextView) findViewById( R.id.shangbiao );

		// 设置执行的任务及动画
		timer.schedule( Task_ShowWelcome, FanRotateTime );
		timer.schedule( Task_JumpToMainAct, AnimLastTime );
		timer.schedule( Task_LoadBuidingData, 0 );
		animation = AnimationUtils.loadAnimation( this, R.anim.danru );
		welcomeImage.startAnimation( animation );

		// 绑定监听事件
		relativeLayout.setOnClickListener( new OnClickListener()
		{
			@ Override
			public void onClick( View view )
			{
				Task_ShowWelcome.cancel();
				Task_JumpToMainAct.cancel();

				ExcuteActivity();
			}
		} );
	}

	public static String GetIP()
	{
		return IP;
	}

	@ SuppressLint( "HandlerLeak" )
	Handler handler = new Handler()
	{
		public void handleMessage( Message msg )
		{
			switch ( msg.what )
			{
				case 1 :
					ExcuteActivity();
					break;
				case 2 :
					try
					{
						Thread.sleep( 300 );
					}
					catch ( InterruptedException e )
					{
						e.printStackTrace();
					}
					welcomeImage.setImageDrawable( null );
					welcomeImage.setBackgroundResource( R.drawable.ying );
					AlphaAnimation animation = new AlphaAnimation( 0, 1 );
					animation.setFillAfter( true );
					animation.setDuration( ShowYing );
					welcomeImage.startAnimation( animation );
					welcomeTextView.setVisibility( View.VISIBLE );
					shangBiao.setVisibility( View.VISIBLE );
					break;
				case 3 :
					initAllData();
					break;
			}
			super.handleMessage( msg );
		}

	};
	TimerTask Task_JumpToMainAct = new TimerTask()
	{
		public void run()
		{
			Message message = new Message();
			message.what = 1;
			handler.sendMessage( message );
		}

	};
	TimerTask Task_ShowWelcome = new TimerTask()
	{
		public void run()
		{
			Message message = new Message();
			message.what = 2;
			handler.sendMessage( message );
		}

	};
	TimerTask Task_LoadBuidingData = new TimerTask()
	{
		@ Override
		public void run()
		{
			Message message = new Message();
			message.what = 3;
			handler.sendMessage( message );
		}
	};

	/**
	 * onBackPressed 按 Back 键直接进入主画面
	 * 
	 * @param void
	 * 
	 * @return void
	 * 
	 * @since 1.0.0
	 */
	@ Override
	public void onBackPressed()
	{
		Task_ShowWelcome.cancel();
		Task_JumpToMainAct.cancel();

		ExcuteActivity();
	}

	private void ExcuteActivity()
	{
		Intent intent = new Intent( WelcomeActivity.this, MainActivity.class );
		startActivity( intent );
		mActivity.finish();
	}
	
	/**
	 * 
	 * initAllData(初始化所有数据)
	 * 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	private void initAllData()
	{
		OnlineData.LoadDataInit(mActivity);
		BuildingDataTool.LoadDataInit( mActivity );
	}

}
