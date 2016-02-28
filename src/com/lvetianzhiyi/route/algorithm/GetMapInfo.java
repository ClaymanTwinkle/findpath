package com.lvetianzhiyi.route.algorithm;

public class GetMapInfo
{
	private Node mData[][];
	private byte[][] mMapArray;

	public GetMapInfo( byte pMapArray[][] )
	{
		this.mMapArray = pMapArray;
		Init();
	}

	// private void Init()
	// {
	// File file = new File( mMap_Path );
	// if ( file.isFile() && file.exists() ) // 判断文件是否存在
	// {
	// InputStreamReader read;
	// try
	// {
	// read = new InputStreamReader( new FileInputStream( file ) );
	// BufferedReader bufferedReader = new BufferedReader( read );
	// String lineTxt = null;
	// while ( ( lineTxt = bufferedReader.readLine() ) != null
	// && !lineTxt.equals( "" ) )
	// {
	// if ( mCol == 0 )
	// mCol = lineTxt.length();
	// mRow++;
	// }
	// mCol = ( mCol + 1 ) / 2;
	// mData = new Node[mRow][mCol];
	// bufferedReader.close();
	//
	// read = new InputStreamReader( new FileInputStream( file ) );
	// bufferedReader = new BufferedReader( read );
	// int k = 0;
	// while ( ( lineTxt = bufferedReader.readLine() ) != null
	// && !lineTxt.equals( "" ) )
	// {
	// int size = lineTxt.length();
	// for ( int i = 0, j = 0; i < size; i++ )
	// {
	// if ( lineTxt.charAt( i ) == '0' )
	// {
	// mData[k][j] = new Node( 50 * k, 50 * j, k, j,
	// '0' );
	// j++;
	// }
	// else if ( lineTxt.charAt( i ) == '1' )
	// {
	// mData[k][j] = new Node( 50 * k, 50 * j, k, j,
	// '1' );
	// j++;
	// }
	// }
	// k++;
	// }
	// bufferedReader.close();
	// }
	// catch ( Exception e )
	// {
	// e.printStackTrace();
	// }
	// }
	// }

	private void Init()
	{
		mData = new Node[mMapArray.length][mMapArray[0].length];
		for ( int i = 0; i < mMapArray.length; i++ )
		{
			for ( int j = 0; j < mMapArray[i].length; j++ )
			{
				if ( mMapArray[i][j] == 0 )
				{
					mData[i][j] = new Node( AStar.RATIO * j, AStar.RATIO * i,
							i, j, '0' );
				}
				else if ( mMapArray[i][j] == 1 )
				{
					mData[i][j] = new Node( AStar.RATIO * j, AStar.RATIO * i,
							i, j, '1' );
				}
			}
		}
	}

	public Node[][] getmData()
	{
		return mData;
	}
};