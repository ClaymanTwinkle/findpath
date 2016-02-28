package com.lvetianzhiyi.route.algorithm;

import java.util.List;

public class BuildTemp implements BuildObject
{
	private Building mBuilding;
	
	public BuildTemp(byte pZ)
	{
		mBuilding=new Building();
		mBuilding.setZ(pZ);
	}
	
	/**
	 * 
	 * addPoint(添加坐标)
	 * 
	 * @param pPoint 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public void addPoint(PixelPoint pPoint)
	{
		mBuilding.addPoint(pPoint);
	}
	
	public void setName(String mName)
	{
		mBuilding.setName(mName);
		
		checkType();
	}
	
	public void setLocation(List<PixelPoint> mLocation)
	{
		mBuilding.setLocation(mLocation);
	}
	
	public void setType(String mType)
	{
		mBuilding.setType(mType);
	}
	
	public String getName()
	{
		return mBuilding.getName();
	}

	public List<PixelPoint> getLocation()
	{
		return mBuilding.getLocation();
	}
	
	public String getType()
	{
		return mBuilding.getType();
	}

	private void checkType()
	{
		if(getName().startsWith("WT"))
		{
			setType(WT);
		}
		else if(getName().startsWith("ES"))
		{
			setType(ES);
		}
		else if(getName().startsWith("EL"))
		{
			setType(EL);
		}
		else if(getName().startsWith("SC"))
		{
			setType(SC);
		}
		else if(getName().startsWith("C"))
		{
			setType(C);
		}
		else if(getName().startsWith("B"))
		{
			setType(B);
		}
		else if(getName().startsWith("W"))
		{
			setType(W);
		}
		else if(getName().startsWith("E"))
		{
			setType(E);
		}
		else if(getName().startsWith("S"))
		{
			setType(S);
		}
		else if(getName().startsWith("D"))
		{
			setType(D);
		}
	}
	
	public Building getmBuilding()
	{
		return mBuilding;
	}

	public void setmBuilding(Building mBuilding)
	{
		this.mBuilding = mBuilding;
	}

	@Override
	public String toString()
	{
		return mBuilding.toString();
	}
	
	
}
