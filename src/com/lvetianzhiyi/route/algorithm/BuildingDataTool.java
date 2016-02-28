package com.lvetianzhiyi.route.algorithm;

import java.util.List;

import com.lvetianzhiyi.route.data.OnlineData;

import android.app.Activity;

/**
 * 
 * 
 * BuildingDataTool 用于找到地点坐标的工具类 kesar kesar 2015年4月25日 下午10:32:02
 * 
 * @version 1.0.0
 *
 */
public class BuildingDataTool
{
	private int mBuildingIndex;
	private static Building[][] mBuildings_all = new Building[3][];

	public BuildingDataTool()
	{
	}

	public static void LoadDataInit( Activity pActivity )
	{
		for ( int i = 0; i < OnlineData.mMapFile.length; i++ )
		{
			mBuildings_all[i] = (Building[]) DataIOTool.input(
					OnlineData.mMapFile[i] + "_Buildings", pActivity );
		}
	}

	public BuildingDataTool( String pMapDataName )
	{
		init( pMapDataName );
	}

	/**
	 * 
	 * init(初始化)
	 * 
	 * @param pMapDataName
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void init( String pMapDataName )
	{
		for ( int i = 0; i < OnlineData.mMapFile.length; i++ )
		{
			if ( OnlineData.mMapFile[i].equals( pMapDataName ) )
			{
				mBuildingIndex = i;
			}
		}

	}

	/**
	 * 
	 * getBuildingsData(获取地点数据的集合)
	 * 
	 * @return Building[]
	 * @exception
	 * @since 1.0.0
	 */
	public Building[] getBuildingsData()
	{
		return mBuildings_all[mBuildingIndex];
	}

	/**
	 * 
	 * findBuildingByName(通过目标名找到目标点可达坐标)
	 * 
	 * @param pBuildingName
	 *            目标地点名
	 * @return 可达地点的坐标集合 List<Point>
	 * @exception
	 * @since 1.0.0
	 */
	public List< PixelPoint > findBuildingByName( String pBuildingName )
	{
		Building[] mBuildings = getBuildingsData();
		List< PixelPoint > points = null;

		for ( Building building : mBuildings )
		{
			if ( building.getName().equals( pBuildingName ) )
			{
				points = building.getLocation();
			}
		}
		return points;
	}

}
