package com.lvetianzhiyi.route.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CheckIfFirst
{
	private Activity mActivity;
	private SharedPreferences sp;

	public CheckIfFirst( Activity pActivity )
	{
		mActivity = pActivity;
		sp = mActivity.getSharedPreferences( "config",
				android.content.Context.MODE_PRIVATE );
	}

	public boolean isFirst()
	{
		Editor editor = sp.edit();
		if ( sp.getString( "first", "" ).equals( "" ) )
		{
			editor.putString( "first", "yes" );
			editor.commit();
			return true;
		}
		return false;
	}
};