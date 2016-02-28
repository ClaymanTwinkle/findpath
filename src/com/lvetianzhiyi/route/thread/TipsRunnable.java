package com.lvetianzhiyi.route.thread;

import android.app.Activity;

import com.lvetianzhiyi.route.algorithm.Path;
import com.lvetianzhiyi.route.algorithm.PixelPoint;
import com.lvetianzhiyi.route.algorithm.Route;
import com.lvetianzhiyi.route.utils.SoundSpeaker;

public class TipsRunnable implements Runnable
{
	
	private Sound mSound;
	private PixelPoint mPixelPoint;
	
	public TipsRunnable(Activity pActivity)
	{
		mSound=new Sound(new SoundSpeaker(pActivity));
	}
	
	public void setmPixelPoint(PixelPoint mPixelPoint)
	{
		this.mPixelPoint = mPixelPoint;
	}
	
	@Override
	public void run()
	{
		
	}

	/**
	 * 
	 *  TipsRun(方向导航提示)
	 * 
	 * @param pRoute 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public void TipsRun(Route pRoute)
	{
		PixelPoint point=null;
		Path p=null;
		for(int i=0;i<pRoute.getmSize();i++)
		{
			if(i==0)
			{
				if(pRoute.getmStartPath().getmDirectedPath()!=null)
				{
					p=pRoute.getmStartPath();
					point=p.getmDirectedPath().getmEndPoint();
					break;
				}
			}
			
			if(i==pRoute.getmSize()-1)
			{
				if(pRoute.getmEndPath().getmDirectedPath()!=null)
				{
					p=pRoute.getmEndPath();
					point=p.getmDirectedPath().getmEndPoint();
					break;
				}
			}
			p=pRoute.getmResultPaths().get(i);
			if(p.getmDirectedPath()!=null)
			{
				point=p.getmDirectedPath().getmEndPoint();
				break;
			}
		}
		if(p!=null&&point!=null)
		{
			if(point.getInstance(mPixelPoint) < 2.5)
			{
				mSound.setmText(p.getmDirectedPath().getmDirection().toString());
				new Thread(mSound).start();
				System.err.println(p.getmDirectedPath().getmDirection().toString());
				p.setmDirectedPath(p.getmDirectedPath().getmNextDirectedPath());
			}
		}
	}
}
