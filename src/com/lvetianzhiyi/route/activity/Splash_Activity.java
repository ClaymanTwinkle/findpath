package com.lvetianzhiyi.route.activity;

import com.lvetianzhiyi.route.R;
import com.lvetianzhiyi.route.WelcomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash_Activity extends Activity
{
	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_splash );

		new Thread( new Runnable()
		{			
			@ Override
			public void run()
			{
//				try
//				{
//					Thread.sleep( 300 );
//				}
//				catch ( InterruptedException e )
//				{
//				}
				runOnUiThread( new Runnable()
				{
					@ Override
					public void run()
					{
						startActivity( new Intent( Splash_Activity.this,
								WelcomeActivity.class ) );
						finish();
					}
				} );
			}
		} ).start();

	}
}
