package com.lvetianzhiyi.route.activity;

import com.lvetianzhiyi.route.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutUs_Activity extends Activity
{
	private Button mBtnBack;
	private TextView mTvTitle;

	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		requestWindowFeature( Window.FEATURE_NO_TITLE );// 去掉标题栏
		setContentView( R.layout.activity_about_us );

		mTvTitle = (TextView) findViewById( R.id.tvTitle );
		mTvTitle.setText( "关于我们" );

		mBtnBack = (Button) findViewById( R.id.back );
		mBtnBack.setText( "设置" );
		mBtnBack.setOnClickListener( new OnClickListener()
		{
			@ Override
			public void onClick( View v )
			{
				finish();
			}
		} );
	}
	
	@ Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
	}	
	
}
