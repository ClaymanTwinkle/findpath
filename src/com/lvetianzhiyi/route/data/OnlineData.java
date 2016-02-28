package com.lvetianzhiyi.route.data;

import android.app.Activity;

import com.lvetianzhiyi.route.algorithm.DataIOTool;

public class OnlineData
{
	public static byte[][][] mMapArray; // 地图二值化数组
	public static String[] mMapFile = { "Our_Map_Underground",
			"Our_Map_Floor1", "Our_Map_Floor2" };// 地图文件
	public static String[][] mELInMap = { // 电梯名的集合
	{ "EL1", "EL2", "EL3", "EL4" },
			{ "EL1", "EL2", "EL3", "EL4", "EL5", "EL6", "EL7", "EL8" },
			{ "EL7", "EL8" }, { "SC1", "SC2", "SC3" } };

	public static void LoadDataInit(Activity pActivity)
	{
		// 地图二值化的数组
		mMapArray = new byte[mMapFile.length][][];

		// 得到地图二值化的数据，用于参与A星算法的计算
		for (int i = 0; i < mMapFile.length; i++)
		{
			mMapArray[i] = (byte[][]) DataIOTool.input(mMapFile[i], pActivity);
		}
	}
}
