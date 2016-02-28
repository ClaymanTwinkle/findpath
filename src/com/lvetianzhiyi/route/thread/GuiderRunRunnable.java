package com.lvetianzhiyi.route.thread;

import android.os.Handler;
import android.os.Message;

import com.lvetianzhiyi.route.MainActivity;
import com.lvetianzhiyi.route.algorithm.Guider;

/**
 * 
 * 
 * GuiderRouteRunnable
 * 引导规划线程
 * kesar
 * kesar
 * 2015-7-21 下午9:25:43
 * 
 * @version 1.0.0
 *
 */
public class GuiderRunRunnable implements Runnable
{
	private Guider mGuider;
	private Handler mHandler;
	
	public GuiderRunRunnable(Handler pHandler,Guider pGuider)
	{
		this.mGuider=pGuider;
		this.mHandler=pHandler;
	}
	
	@Override
	public void run()
	{
		Message message=new Message();
		switch (mGuider.HandleRunDeviation())
		{
		case Guider.RUN:
			message.what=MainActivity.RUNDEVIATION;
			mHandler.sendMessage( message );
			break;
//		case Guider.OUTMAP:
//			message.what=MainActivity.SHOWTOAST;
//			message.obj="所在位置超出地图了！";
//			mHandler.sendMessage( message );
//		case Guider.STRIKEWALL:
//			message.what=MainActivity.SHOWTOAST;
//			message.obj="所在位置撞墙了！";
//			mHandler.sendMessage( message );
		case Guider.REACHGOAL:
			message.what=MainActivity.REARCHGOAL;
			mHandler.sendMessage( message );
			break;
		default:
			break;
		}
	}

	public Guider getmGuider()
	{
		return mGuider;
	}

	public void setmGuider(Guider mGuider)
	{
		this.mGuider = mGuider;
	}
	
}
