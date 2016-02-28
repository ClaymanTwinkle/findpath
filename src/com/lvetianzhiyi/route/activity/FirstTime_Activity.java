package com.lvetianzhiyi.route.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.lvetianzhiyi.route.R;
import com.lvetianzhiyi.route.welcome.CircleFlowIndicator;
import com.lvetianzhiyi.route.welcome.ViewFlow;

public class FirstTime_Activity extends Activity
{
	/** Called when the activity is first created. */
	private ViewFlow viewFlow;

	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.activity_first_time_in );
		viewFlow = (ViewFlow) findViewById( R.id.viewflow );
		viewFlow.setAdapter( new DemoPic() );
		viewFlow.setmSideBuffer( ids.length ); // 实际图片张数，
		// 我的ImageAdapter实际图片张数为3

		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById( R.id.viewflowindic );
		viewFlow.setFlowIndicator( indic );
		viewFlow.setSelection( 0 ); // 设置初始位置
	}

	@ Override
	public boolean onKeyDown( int keyCode, KeyEvent event )
	{
		if ( keyCode == KeyEvent.KEYCODE_BACK )
		{
			setResult( RESULT_OK );
			finish();
		}
		return super.onKeyDown( keyCode, event );
	}

	public int[] ids = { R.drawable.first_time_post_1,
			R.drawable.first_time_post_2, R.drawable.first_time_post_3,
			R.drawable.first_time_post_4 };

	class DemoPic extends BaseAdapter
	{
		private LayoutInflater mInflater;

		public DemoPic()
		{
			mInflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		}

		@ Override
		public int getCount()
		{
			return ids.length;
		}

		@ Override
		public Object getItem( int position )
		{
			return position;
		}

		@ Override
		public long getItemId( int position )
		{
			return position;
		}

		@ SuppressLint( "InflateParams" )
		@ Override
		public View getView( final int position, View convertView,
				ViewGroup parent )
		{
			if ( convertView == null )
			{
				convertView = mInflater.inflate( R.layout.image_item, null );
			}

			( (ImageView) convertView.findViewById( R.id.imgView ) )
					.setImageResource( ids[position % ids.length] );
			if ( position == 3 )
			{
				convertView.setOnClickListener( new OnClickListener()
				{
					@ Override
					public void onClick( View v )
					{
						finish();
					}
				} );
			}
			return convertView;
		}
	}
}
