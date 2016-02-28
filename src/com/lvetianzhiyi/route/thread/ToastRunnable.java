package com.lvetianzhiyi.route.thread;

import android.app.Activity;
import android.os.Looper;
import android.widget.Toast;

public class ToastRunnable implements Runnable
{
	public static Activity mActivity;
	private String msg;
	
	@Override
	public void run()
	{
        Looper.prepare(); 
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        Looper.loop(); 
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}
}
