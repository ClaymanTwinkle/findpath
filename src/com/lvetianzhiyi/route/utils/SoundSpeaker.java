package com.lvetianzhiyi.route.utils;

import android.app.Activity;
import android.media.AudioManager;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;

public class SoundSpeaker implements SpeechSynthesizerListener
{
	private Activity mActivity;
	private SpeechSynthesizer speechSynthesizer;

	public SoundSpeaker( Activity pActivity )
	{
		mActivity = pActivity;
		LoadSoLib();
		Init();
	}

	private void LoadSoLib()
	{
		// 加载播音库
		System.loadLibrary( "gnustl_shared" );
		// 部分版本不需要BDSpeechDecoder_V1
		try
		{
			System.loadLibrary( "BDSpeechDecoder_V1" );
		}
		catch ( UnsatisfiedLinkError e )
		{
			// SpeechLogger.logD( "load BDSpeechDecoder_V1 failed, ignore" );
		}
		System.loadLibrary( "bd_etts" );
		System.loadLibrary( "bds" );
	}

	private void Init()
	{
		/*
		 * 创建新的合成器实例，SYNTHESIZER_RESERVE为保留字段，目前作用跟SYNTHESIZER_AUTO一致
		 * SYNTHESIZER_AUTO目前仅支持一个实例，因此每次调用将返回同一个实例
		 */
		speechSynthesizer = SpeechSynthesizer.newInstance(
				SpeechSynthesizer.SYNTHESIZER_AUTO,
				mActivity.getApplicationContext(), "holder", this );
		// 语音开发者平台注册应用得到的apikey和secretkey (在线授权)
		speechSynthesizer.setApiKey( "0Ta27g2qb3glv8jZG5YXQKdI",
				"8a888dccce522f1ff730fec95103449f" );
		speechSynthesizer.setAppId( "6423280" );
		speechSynthesizer.initEngine();
		mActivity.setVolumeControlStream( AudioManager.STREAM_MUSIC );
		speechSynthesizer.setParam( SpeechSynthesizer.PARAM_SPEED, "6" );
	}

	public boolean SpeakString( String keyWords )
	{
		if ( speechSynthesizer != null )
		{
			speechSynthesizer.speak( keyWords );
		}
		else
		{
			return false;
		}
		return true;
	}

	// sex 为 true 时为男声，false 为女声
	public void setVoiceSex( boolean sex )
	{
		if ( sex )
		{
			speechSynthesizer.setParam( SpeechSynthesizer.PARAM_SPEAKER,
					SpeechSynthesizer.SPEAKER_MALE );
		}
		else
		{
			speechSynthesizer.setParam( SpeechSynthesizer.PARAM_SPEAKER,
					SpeechSynthesizer.SPEAKER_FEMALE );
		}
	}

	@ Override
	public void onBufferProgressChanged( SpeechSynthesizer arg0, int arg1 )
	{
	}

	@ Override
	public void onCancel( SpeechSynthesizer arg0 )
	{

	}

	@ Override
	public void onError( SpeechSynthesizer arg0, SpeechError arg1 )
	{

	}

	@ Override
	public void onNewDataArrive( SpeechSynthesizer arg0, byte[] arg1,
			boolean arg2 )
	{

	}

	@ Override
	public void onSpeechFinish( SpeechSynthesizer arg0 )
	{

	}

	@ Override
	public void onSpeechPause( SpeechSynthesizer arg0 )
	{

	}

	@ Override
	public void onSpeechProgressChanged( SpeechSynthesizer arg0, int arg1 )
	{

	}

	@ Override
	public void onSpeechResume( SpeechSynthesizer arg0 )
	{

	}

	@ Override
	public void onSpeechStart( SpeechSynthesizer arg0 )
	{

	}

	@ Override
	public void onStartWorking( SpeechSynthesizer arg0 )
	{

	}

	@ Override
	public void onSynthesizeFinish( SpeechSynthesizer arg0 )
	{

	}

};