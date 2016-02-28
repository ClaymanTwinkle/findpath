package com.lvetianzhiyi.route.welcome;

import com.lvetianzhiyi.route.welcome.ViewFlow.ViewSwitchListener;

/**
 * An interface which defines the contract between a ViewFlow and a
 * FlowIndicator.<br/>
 * A FlowIndicator is responsible to show an visual indicator on the total views
 * number and the current visible view.<br/>
 * 
 */
public interface FlowIndicator extends ViewSwitchListener
{
	/**
	 * Set the current ViewFlow. This method is called by the ViewFlow when the
	 * FlowIndicator is attached to it.
	 * 
	 * @param view
	 */
	public void setViewFlow( ViewFlow view );

	/**
	 * The scroll position has been changed. A FlowIndicator may implement this
	 * method to reflect the current position
	 * 
	 * @param h
	 * @param v
	 * @param oldh
	 * @param oldv
	 */
	public void onScrolled( int h, int v, int oldh, int oldv );
}
