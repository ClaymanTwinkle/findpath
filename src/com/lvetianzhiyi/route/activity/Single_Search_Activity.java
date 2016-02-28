package com.lvetianzhiyi.route.activity;

import java.util.Locale;

import com.lvetianzhiyi.route.MainActivity;
import com.lvetianzhiyi.route.R;
import com.lvetianzhiyi.route.adapter.SingleAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class Single_Search_Activity extends Activity implements OnClickListener
{
	private Button fastButton[];
	private ImageButton back;
	private ListView mListView;
	private SingleAdapter adapter;
	private AutoCompleteTextView mAutoEdittext;

	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		requestWindowFeature( Window.FEATURE_NO_TITLE );// 去掉标题栏
		setContentView( R.layout.single_search );

		Init();
		AddListener();
	}

	private void Init()
	{
		back = (ImageButton) findViewById( R.id.back );
		mListView = (ListView) findViewById( R.id.listview );
		mAutoEdittext = (AutoCompleteTextView) findViewById( R.id.sousuo_Edittext );

		fastButton = new Button[8];
		fastButton[0] = (Button) findViewById( R.id.fast_1 );
		fastButton[1] = (Button) findViewById( R.id.fast_2 );
		fastButton[2] = (Button) findViewById( R.id.fast_3 );
		fastButton[3] = (Button) findViewById( R.id.fast_4 );
		fastButton[4] = (Button) findViewById( R.id.fast_5 );
		fastButton[5] = (Button) findViewById( R.id.fast_6 );
		fastButton[6] = (Button) findViewById( R.id.fast_7 );
		fastButton[7] = (Button) findViewById( R.id.fast_8 );

		adapter = new SingleAdapter( this );
		mListView.setAdapter( adapter );
	}

	private void AddListener()
	{
		back.setOnClickListener( this );
		for ( int i = 0; i < fastButton.length; i++ )
		{
			fastButton[i].setOnClickListener( this );
		}
		mListView.setOnItemClickListener( new OnItemClickListener()
		{
			@ Override
			public void onItemClick( AdapterView< ? > adapterView, View view,
					int pos, long id )
			{
				doExcute( Search_Activity.placeArray[pos] );
			}
		} );
	}

	@ Override
	public void onClick( View v )
	{
		String userSelectedPos = null;
		switch ( v.getId() )
		{
			case R.id.back :
				finish();
				overridePendingTransition( R.anim.slide_bottom_in,
						R.anim.slide_bottom_out );
				break;

			case R.id.fast_1 :
				userSelectedPos = fastButton[0].getText().toString()
						.toUpperCase( Locale.CHINA );
				mAutoEdittext.setText( userSelectedPos );
				if ( userSelectedPos != null && !userSelectedPos.equals( "" ) )
					doExcute( userSelectedPos );
				break;
			case R.id.fast_2 :
				userSelectedPos = fastButton[1].getText().toString()
						.toUpperCase( Locale.CHINA );
				mAutoEdittext.setText( userSelectedPos );
				if ( userSelectedPos != null && !userSelectedPos.equals( "" ) )
					doExcute( userSelectedPos );
				break;
			case R.id.fast_3 :
				userSelectedPos = fastButton[2].getText().toString()
						.toUpperCase( Locale.CHINA );
				mAutoEdittext.setText( userSelectedPos );
				if ( userSelectedPos != null && !userSelectedPos.equals( "" ) )
					doExcute( userSelectedPos );
				break;
			case R.id.fast_4 :
				userSelectedPos = fastButton[3].getText().toString()
						.toUpperCase( Locale.CHINA );
				mAutoEdittext.setText( userSelectedPos );
				if ( userSelectedPos != null && !userSelectedPos.equals( "" ) )
					doExcute( userSelectedPos );
				break;
			case R.id.fast_5 :
				userSelectedPos = fastButton[4].getText().toString()
						.toUpperCase( Locale.CHINA );
				mAutoEdittext.setText( userSelectedPos );
				if ( userSelectedPos != null && !userSelectedPos.equals( "" ) )
					doExcute( userSelectedPos );
				break;
			case R.id.fast_6 :
				userSelectedPos = fastButton[5].getText().toString()
						.toUpperCase( Locale.CHINA );
				mAutoEdittext.setText( userSelectedPos );
				if ( userSelectedPos != null && !userSelectedPos.equals( "" ) )
					doExcute( userSelectedPos );
				break;
			case R.id.fast_7 :
				userSelectedPos = fastButton[6].getText().toString()
						.toUpperCase( Locale.CHINA );
				mAutoEdittext.setText( userSelectedPos );
				if ( userSelectedPos != null && !userSelectedPos.equals( "" ) )
					doExcute( userSelectedPos );
				break;
			case R.id.fast_8 :
				userSelectedPos = fastButton[7].getText().toString()
						.toUpperCase( Locale.CHINA );
				mAutoEdittext.setText( userSelectedPos );
				if ( userSelectedPos != null && !userSelectedPos.equals( "" ) )
					doExcute( userSelectedPos );
				break;

			default :
				Toast.makeText( this, "您输入的格式不正确或地点不存在", Toast.LENGTH_SHORT )
						.show();
				return;
		}
	}

	private void doExcute( String selectedPos )
	{
		if ( !selectedPos.startsWith( "B1" ) && !selectedPos.startsWith( "F1" )
				&& !selectedPos.startsWith( "F2" ) )
		{
			Toast.makeText( this, "请填写层数", Toast.LENGTH_SHORT ).show();
			return;
		}
		Intent intent = new Intent();
		intent.putExtra( "UserSelectedPos", selectedPos );
		setResult( MainActivity.CONST_SINGLE_RESULT, intent );
		finish();
		overridePendingTransition( R.anim.slide_bottom_in,
				R.anim.slide_bottom_out );
	}

	@ Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
		overridePendingTransition( R.anim.slide_bottom_in,
				R.anim.slide_bottom_out );
	}

};