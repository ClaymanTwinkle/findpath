package com.lvetianzhiyi.route.utils;

public class MyPoint
{
	// 节点横坐标
	private int x;
	// 节点纵坐标
	private int y;
	// 节点所处行
	private int RowPos;
	// 节点所处列
	private int ColPos;

	// 节点值
	private char value;
	// 父节点
	private MyPoint father;

	/**
	 * 构造函数
	 * 
	 * @param x
	 *            节点横坐标
	 * @param y
	 *            节点纵坐标
	 * @param value
	 *            节点值
	 */
	public MyPoint( int x, int y, int Row, int Col, char value )
	{
		this.x = x;
		this.y = y;
		this.RowPos = Row;
		this.ColPos = Col;
		this.value = value;
	}

	public MyPoint getFather()
	{
		return father;
	}

	public void setFather( MyPoint father )
	{
		this.father = father;
	}

	public char getValue()
	{
		return value;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getRowPos()
	{
		return RowPos;
	}

	public int getColPos()
	{
		return ColPos;
	}
};