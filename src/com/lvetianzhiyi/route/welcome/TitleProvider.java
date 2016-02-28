package com.lvetianzhiyi.route.welcome;

/**
 * A TitleProvider provides the title to display according to a view.
 */
public interface TitleProvider
{
	/**
	 * Returns the title of the view at position
	 * 
	 * @param position
	 * @return
	 */
	public String getTitle( int position );

}
