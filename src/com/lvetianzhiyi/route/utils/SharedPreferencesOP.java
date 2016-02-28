package com.lvetianzhiyi.route.utils;

import android.app.Activity;
import android.content.SharedPreferences;

public class SharedPreferencesOP
{
	private Activity mActivity;
	private SharedPreferences mySharedPreferences;
	private SharedPreferences.Editor mEditor;

	public SharedPreferencesOP( Activity pActivity )
	{
		mActivity = pActivity;
		mySharedPreferences = mActivity.getSharedPreferences( "test",
				Activity.MODE_PRIVATE );
		mEditor = mySharedPreferences.edit();
	}

	public void WriteSP( String keyString, String valueString )
	{
		mEditor.putString( keyString, valueString );
		mEditor.commit();
	}

	public String ReadSP( String ketsString )
	{
		return mySharedPreferences.getString( ketsString, "" );
	}
};