package com.lvetianzhiyi.route.thread;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;

import com.lvetianzhiyi.route.MainActivity;
import com.lvetianzhiyi.route.utils.SoundSpeaker;

public class GuideSpeaker implements Runnable
{
	private static boolean mFlag;
	private static int routeLength = 0;
	private SoundSpeaker mSoundSpeaker;
	private Handler mHandler;
	private static ArrayList< String > mGoalName;

	public GuideSpeaker( SoundSpeaker soundSpeaker, Handler handler )
	{
		mSoundSpeaker = soundSpeaker;
		mHandler = handler;
	}

	@ Override
	public void run()
	{
		if ( mFlag )
		{
			String talk = "导航开始，您要经过";
			if ( mSoundSpeaker != null && !mGoalName.isEmpty() )
			{
				for ( String word : mGoalName )
				{
					talk += ( word + "," );
				}
				talk = talk + "请转至图中的路线方向，大约步行" + routeLength + "即可";
				mSoundSpeaker.SpeakString( talk );
				Message msg = new Message();
				msg.what = MainActivity.CONST_REFRESH_MARQUEE;
				msg.obj = talk;
				mHandler.sendMessage( msg );
				// mMyMarqueeTextView.setText( talk );
			}
		}
		else
		{
			if ( mSoundSpeaker != null )
			{
				mSoundSpeaker.SpeakString( "从当前位置转至图中的路线方向，大约步行" + routeLength
						+ "即可到达您的目的地" );
				Message msg = new Message();
				msg.what = MainActivity.CONST_REFRESH_MARQUEE;
				msg.obj = "从当前位置转至图中的路线方向，大约步行" + routeLength + "即可到达您的目的地";
				mHandler.sendMessage( msg );
				// mMyMarqueeTextView.setText( "从当前位置转至图中的路线方向，大约步行" +
				// routeLength
				// + "即可到达您的目的地" );
			}
		}
		routeLength = 0;
	}

	public static void setGoalPoint( ArrayList< String > goal )
	{
		clearGoal();
		mGoalName = goal;
		mFlag = true;
	}

	public static void setRouteLength( int routeLength )
	{
		GuideSpeaker.routeLength += routeLength;
	}

	public static void setSelfChoosePoint()
	{
		clearGoal();
		if ( mGoalName == null )
			mGoalName = new ArrayList< String >();
		mGoalName.add( "目的地" );
		mFlag = false;
	}

	public static void clearGoal()
	{
		if ( mGoalName != null )
			mGoalName.clear();
	}
};