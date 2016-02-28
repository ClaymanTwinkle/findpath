package com.lvetianzhiyi.route.algorithm;

public class Node
{
	PixelPoint pixelPoint;
	
	// 节点值
	private char value;
	// 父节点
	private Node father;

	/**
	 * 
	 * 创建一个新的实例 Node.
	 *
	 * @param x
	 * @param y 像素y
	 * @param Row 行（数组下标x）
	 * @param Col 列（数组下标y）
	 * @param value
	 */
	public Node( int x, int y, int Row, int Col, char value )
	{
		pixelPoint=new PixelPoint(x, y);
		pixelPoint.setIndex(Row, Col);
		this.value = value;
	}

	public Node getFather()
	{
		return father;
	}

	public void setFather( Node father )
	{
		this.father = father;
	}

	public char getValue()
	{
		return value;
	}

	public int getRowPos()
	{
		return pixelPoint.getIndex_X();
	}

	public int getColPos()
	{
		return pixelPoint.getIndex_Y();
	}

	public int getX()
	{
		return pixelPoint.getX();
	}

	public void setX(int x)
	{
		pixelPoint.setX(x);
	}

	public int getY()
	{
		return pixelPoint.getY();
	}

	public void setY(int y)
	{
		pixelPoint.setY(y);
	}
	
	public PixelPoint getPixelPoint()
	{
		return pixelPoint;
	}

	@Override
	public String toString()
	{
		return "Node [pixelPoint=" + pixelPoint + ", value=" + value
				+ ", father=" + father + "]";
	}
	
}