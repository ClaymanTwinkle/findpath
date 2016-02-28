package com.lvetianzhiyi.route.activity;

import com.lvetianzhiyi.route.MainActivity;
import com.lvetianzhiyi.route.R;
import com.lvetianzhiyi.route.utils.FixImageToFitScreen;
import com.lvetianzhiyi.route.utils.Screen_info;
import com.lvetianzhiyi.route.utils.SendRouteImage;
import com.lvetianzhiyi.route.view.Global_FloorImageview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * 类 Global_Activity 全局视图
 * 
 * @author xinyi
 * 
 * @继承 Activity
 * 
 * @since 1.0.0
 */
public class Global_Activity extends Activity
{
	// 返回按钮
	private Button button;
	// 各层视图
	private Global_FloorImageview imageview[];

	@ Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.global_view );

		// 初始化控件
		Init();
	}

	private void Init()
	{
		// 获取各层地图图片
		SendRouteImage sendRouteImage = MainActivity.mSendRouteImage;
		Bitmap bitmap_Route[] = sendRouteImage.getBitmaps();
		Bitmap Bitmap_Map[] = new Bitmap[3];
		float times = Screen_info.CaluculateInitInch();
		TextView textView = (TextView) findViewById( R.id.tvTitle );
		textView.setText( "全局" );

		// 进行构图
		for ( int i = 0; i < Bitmap_Map.length; i++ )
		{
			int width = (int) ( MainActivity.getmBitmap_Map( i ).getWidth() * times );
			int height = (int) ( MainActivity.getmBitmap_Map( i ).getHeight() * times );
			Bitmap_Map[i] = Bitmap.createBitmap( width, height,
					Config.ARGB_8888 );
			Canvas canvas = new Canvas( Bitmap_Map[i] );
			RectF rectF = new RectF( 0, 0, width, height );
			canvas.drawBitmap( MainActivity.getmBitmap_Map( i ), null, rectF,
					null );
			if ( i == 0 )
				canvas.drawBitmap(
						bitmap_Route[i],
						0,
						-Screen_info.getInitTop2() + 240
								* Screen_info.CaluculateInitInch() / 0.72f,
						null );
			else
				canvas.drawBitmap(
						bitmap_Route[i],
						0,
						-Screen_info.getInitTop() + 18
								* Screen_info.CaluculateInitInch() / 0.72f,
						null );
			canvas.save( Canvas.ALL_SAVE_FLAG );
			bitmap_Route[i].recycle();
		}

		button = (Button) findViewById( R.id.back );
		imageview = new Global_FloorImageview[3];
		imageview[0] = (Global_FloorImageview) findViewById( R.id.imageview_underground );
		imageview[1] = (Global_FloorImageview) findViewById( R.id.imageview_floor1 );
		imageview[2] = (Global_FloorImageview) findViewById( R.id.imageview_floor2 );

		// 设置全局视图
		for ( int i = 0; i < imageview.length; i++ )
		{
			FixImageToFitScreen fitScreen = new FixImageToFitScreen(
					Bitmap_Map[i] );
			imageview[i].setImageBitmap( fitScreen.getmBitmap() );
		}

		button.setOnClickListener( new OnClickListener()
		{
			@ Override
			public void onClick( View v )
			{
				finish();
				overridePendingTransition( R.anim.zoom_enter_global,
						R.anim.zoom_exit_global );
			}
		} );
	}

	// 按 Back 键返回
	@ Override
	public void onBackPressed()
	{
		super.onBackPressed();

		finish();
		overridePendingTransition( R.anim.zoom_enter_global,
				R.anim.zoom_exit_global );
	}

};