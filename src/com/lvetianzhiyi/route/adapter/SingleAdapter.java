package com.lvetianzhiyi.route.adapter;

import com.lvetianzhiyi.route.R;
import com.lvetianzhiyi.route.activity.Search_Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SingleAdapter extends BaseAdapter
{
	private Activity mActivity;

	public SingleAdapter( Activity activity )
	{
		super();
		mActivity = activity;
	}

	@ Override
	public int getCount()
	{
		return Search_Activity.placeArray.length;
	}

	@ Override
	public Object getItem( int pos )
	{
		return Search_Activity.placeArray[pos];
	}

	@ Override
	public long getItemId( int pos )
	{
		return pos;
	}

	@ SuppressLint( { "ViewHolder", "InflateParams" } )
	@ Override
	public View getView( int pos, View convertView, ViewGroup parent )
	{
		View myview = null;
		TextView textView = null;
		LayoutInflater inflater = mActivity.getLayoutInflater();
		if ( pos == 0 )
		{
			myview = inflater.inflate( R.layout.listview_item_first, null );
			textView = (TextView) myview
					.findViewById( R.id.listview_item_textview1 );
		}
		else if ( pos == Search_Activity.placeArray.length - 1 )
		{
			myview = inflater.inflate( R.layout.listview_item_last, null );
			textView = (TextView) myview
					.findViewById( R.id.listview_item_textview3 );
		}
		else
		{
			myview = inflater.inflate( R.layout.listview_item_middle, null );
			textView = (TextView) myview
					.findViewById( R.id.listview_item_textview2 );
		}

		textView.setText( Search_Activity.placeArray[pos] );

		return myview;
	}
	
	

}
