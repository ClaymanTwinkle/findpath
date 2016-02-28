package com.lvetianzhiyi.route.thread;

import android.app.Activity;

import com.lvetianzhiyi.route.utils.SoundSpeaker;
/**
 * 
 * 
 * Sound
 * 用来说话的线程
 * kesar
 * kesar
 * 2015-7-23 下午11:47:04
 * 
 * @version 1.0.0
 *
 */
public class Sound implements Runnable
{
	private String mText;
	private SoundSpeaker mSoundSpeaker;
	
	public Sound(Activity pActivity,String mText)
	{
		mSoundSpeaker=new SoundSpeaker(pActivity);
		this.mText = mText;
	}
	
	public Sound(SoundSpeaker pSoundSpeaker)
	{
		this.mSoundSpeaker=pSoundSpeaker;
	}
	
	public void setmText(String mText)
	{
		this.mText = mText;
	}


	@Override
	public void run()
	{
		if(mSoundSpeaker!=null)
		{
			mSoundSpeaker.SpeakString(mText);
		}
	}

}
