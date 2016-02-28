package com.lvetianzhiyi.route.welcome;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * A TitleFlowIndicator is a FlowIndicator which displays the title of left view
 * (if exist), the title of the current select view (centered) and the title of
 * the right view (if exist). When the user scrolls the ViewFlow then titles are
 * also scrolled.
 * 
 */
public class TitleFlowIndicator extends TextView implements FlowIndicator
{

	private static final float TITLE_PADDING = 10.0f;
	//private static final float CLIP_PADDING = 0.0f;
	private static final int SELECTED_COLOR = 0xFFFFC445;
	private static final boolean SELECTED_BOLD = false;
	private static final int TEXT_COLOR = 0xFFAAAAAA;
	private static final int TEXT_SIZE = 15;
	private static final float FOOTER_LINE_HEIGHT = 4.0f;
	private static final int FOOTER_COLOR = 0xFFFFC445;
	//private static final float FOOTER_TRIANGLE_HEIGHT = 10;
	private ViewFlow viewFlow;
	private int currentScroll = 0;
	private TitleProvider titleProvider = null;
	private int currentPosition = 0;
	private Paint paintText;
	private Paint paintSelected;
	private Path path;
	private Paint paintFooterLine;
	private Paint paintFooterTriangle;
	private float footerTriangleHeight;
	private float titlePadding;
	/**
	 * Left and right side padding for not active view titles.
	 */
	private float clipPadding;
	private float footerLineHeight;

	/* These are hardcoded just like in TextView */
	private static final int SANS = 1;
	private static final int SERIF = 2;
	private static final int MONOSPACE = 3;

	private Typeface typeface;

	/**
	 * Default constructor
	 */
	public TitleFlowIndicator( Context context )
	{
		super( context );
		initDraw( TEXT_COLOR, TEXT_SIZE, SELECTED_COLOR, SELECTED_BOLD,
				TEXT_SIZE, FOOTER_LINE_HEIGHT, FOOTER_COLOR );
	}

	/**
	 * The contructor used with an inflater
	 * 
	 * @param context
	 * @param attrs
	 */
	public TitleFlowIndicator( Context context, AttributeSet attrs )
	{
		super( context, attrs );
	}

	/**
	 * Initialize draw objects
	 */
	private void initDraw( int textColor, float textSize, int selectedColor,
			boolean selectedBold, float selectedSize, float footerLineHeight,
			int footerColor )
	{
		paintText = new Paint();
		paintText.setColor( textColor );
		paintText.setTextSize( textSize );
		paintText.setAntiAlias( true );
		paintSelected = new Paint();
		paintSelected.setColor( selectedColor );
		paintSelected.setTextSize( selectedSize );
		paintSelected.setFakeBoldText( selectedBold );
		paintSelected.setAntiAlias( true );
		paintFooterLine = new Paint();
		paintFooterLine.setStyle( Paint.Style.FILL_AND_STROKE );
		paintFooterLine.setStrokeWidth( footerLineHeight );
		paintFooterLine.setColor( footerColor );
		paintFooterTriangle = new Paint();
		paintFooterTriangle.setStyle( Paint.Style.FILL_AND_STROKE );
		paintFooterTriangle.setColor( footerColor );
	}
	
	
	@ SuppressLint( "DrawAllocation" )
	@ Override
	protected void onDraw( Canvas canvas )
	{
		super.onDraw( canvas );

		// Calculate views bounds
		ArrayList< Rect > bounds = calculateAllBounds( paintText );

		// If no value then add a fake one
		int count = ( viewFlow != null && viewFlow.getAdapter() != null ) ? viewFlow
				.getAdapter().getCount() : 1;

		// Verify if the current view must be clipped to the screen
		Rect curViewBound = bounds.get( currentPosition );
		int curViewWidth = curViewBound.right - curViewBound.left;
		if ( curViewBound.left < 0 )
		{
			// Try to clip to the screen (left side)
			clipViewOnTheLeft( curViewBound, curViewWidth );
		}
		if ( curViewBound.right > getLeft() + getWidth() )
		{
			// Try to clip to the screen (right side)
			clipViewOnTheRight( curViewBound, curViewWidth );
		}

		// Left views starting from the current position
		if ( currentPosition > 0 )
		{
			for ( int iLoop = currentPosition - 1; iLoop >= 0; iLoop-- )
			{
				Rect bound = bounds.get( iLoop );
				int w = bound.right - bound.left;
				// Si left side is outside the screen
				if ( bound.left < 0 )
				{
					// Try to clip to the screen (left side)
					clipViewOnTheLeft( bound, w );
					// Except if there's an intersection with the right view
					if ( iLoop < count - 1 && currentPosition != iLoop )
					{
						Rect rightBound = bounds.get( iLoop + 1 );
						// Intersection
						if ( bound.right + TITLE_PADDING > rightBound.left )
						{
							bound.left = rightBound.left
									- ( w + (int) titlePadding );
						}
					}
				}
			}
		}
		// Right views starting from the current position
		if ( currentPosition < count - 1 )
		{
			for ( int iLoop = currentPosition + 1; iLoop < count; iLoop++ )
			{
				Rect bound = bounds.get( iLoop );
				int w = bound.right - bound.left;
				// If right side is outside the screen
				if ( bound.right > getLeft() + getWidth() )
				{
					// Try to clip to the screen (right side)
					clipViewOnTheRight( bound, w );
					// Except if there's an intersection with the left view
					if ( iLoop > 0 && currentPosition != iLoop )
					{
						Rect leftBound = bounds.get( iLoop - 1 );
						// Intersection
						if ( bound.left - TITLE_PADDING < leftBound.right )
						{
							bound.left = leftBound.right + (int) titlePadding;
						}
					}
				}
			}
		}

		// Now draw views
		for ( int iLoop = 0; iLoop < count; iLoop++ )
		{
			// Get the title
			String title = getTitle( iLoop );
			Rect bound = bounds.get( iLoop );
			// Only if one side is visible
			if ( ( bound.left > getLeft() && bound.left < getLeft()
					+ getWidth() )
					|| ( bound.right > getLeft() && bound.right < getLeft()
							+ getWidth() ) )
			{
				Paint paint = paintText;
				// Change the color is the title is closed to the center
				int middle = ( bound.left + bound.right ) / 2;
				if ( Math.abs( middle - ( getWidth() / 2 ) ) < 20 )
				{
					paint = paintSelected;
				}
				paint.setTypeface( typeface );
				canvas.drawText( title, bound.left, bound.bottom, paint );
			}
		}

		// Draw the footer line
		path = new Path();
		int coordY = getHeight() - 1;
		coordY -= ( footerLineHeight % 2 == 1 ) ? footerLineHeight / 2
				: footerLineHeight / 2 - 1;
		path.moveTo( 0, coordY );
		path.lineTo( getWidth(), coordY );
		path.close();
		canvas.drawPath( path, paintFooterLine );
		// Draw the footer triangle
		path = new Path();
		path.moveTo( getWidth() / 2, getHeight() - footerLineHeight
				- footerTriangleHeight );
		path.lineTo( getWidth() / 2 + footerTriangleHeight, getHeight()
				- footerLineHeight );
		path.lineTo( getWidth() / 2 - footerTriangleHeight, getHeight()
				- footerLineHeight );
		path.close();
		canvas.drawPath( path, paintFooterTriangle );

	}

	/**
	 * Set bounds for the right textView including clip padding.
	 * 
	 * @param curViewBound
	 *            current bounds.
	 * @param curViewWidth
	 *            width of the view.
	 */
	private void clipViewOnTheRight( Rect curViewBound, int curViewWidth )
	{
		curViewBound.right = getLeft() + getWidth() - (int) clipPadding;
		curViewBound.left = curViewBound.right - curViewWidth;
	}

	/**
	 * Set bounds for the left textView including clip padding.
	 * 
	 * @param curViewBound
	 *            current bounds.
	 * @param curViewWidth
	 *            width of the view.
	 */
	private void clipViewOnTheLeft( Rect curViewBound, int curViewWidth )
	{
		curViewBound.left = 0 + (int) clipPadding;
		curViewBound.right = curViewWidth;
	}

	/**
	 * Calculate views bounds and scroll them according to the current index
	 * 
	 * @param paint
	 * @param currentIndex
	 * @return
	 */
	private ArrayList< Rect > calculateAllBounds( Paint paint )
	{
		ArrayList< Rect > list = new ArrayList< Rect >();
		// For each views (If no values then add a fake one)
		int count = ( viewFlow != null && viewFlow.getAdapter() != null ) ? viewFlow
				.getAdapter().getCount() : 1;
		for ( int iLoop = 0; iLoop < count; iLoop++ )
		{
			Rect bounds = calcBounds( iLoop, paint );
			int w = ( bounds.right - bounds.left );
			int h = ( bounds.bottom - bounds.top );
			bounds.left = ( getWidth() / 2 ) - ( w / 2 ) - currentScroll
					+ ( iLoop * getWidth() );
			bounds.right = bounds.left + w;
			bounds.top = 0;
			bounds.bottom = h;
			list.add( bounds );
		}

		return list;
	}

	/**
	 * Calculate the bounds for a view's title
	 * 
	 * @param index
	 * @param paint
	 * @return
	 */
	private Rect calcBounds( int index, Paint paint )
	{
		// Get the title
		String title = getTitle( index );
		// Calculate the text bounds
		Rect bounds = new Rect();
		bounds.right = (int) paint.measureText( title );
		bounds.bottom = (int) ( paint.descent() - paint.ascent() );
		return bounds;
	}

	/**
	 * Returns the title
	 * 
	 * @param pos
	 * @return
	 */
	private String getTitle( int pos )
	{
		// Set the default title
		String title = "title " + pos;
		// If the TitleProvider exist
		if ( titleProvider != null )
		{
			title = titleProvider.getTitle( pos );
		}
		return title;
	}
	
	@ Override
	public void onScrolled( int h, int v, int oldh, int oldv )
	{
		currentScroll = h;
		invalidate();
	}
	
	@ Override
	public void onSwitched( View view, int position )
	{
		currentPosition = position;
		invalidate();
	}
	
	@ Override
	public void setViewFlow( ViewFlow view )
	{
		viewFlow = view;
		currentPosition = view.getSelectedItemPosition();
		invalidate();
	}

	/**
	 * Set the title provider
	 * 
	 * @param provider
	 */
	public void setTitleProvider( TitleProvider provider )
	{
		titleProvider = provider;
	}
	
	@ Override
	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
	{
		setMeasuredDimension( measureWidth( widthMeasureSpec ),
				measureHeight( heightMeasureSpec ) );
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth( int measureSpec )
	{
		int result = 0;
		int specMode = MeasureSpec.getMode( measureSpec );
		int specSize = MeasureSpec.getSize( measureSpec );

		if ( specMode != MeasureSpec.EXACTLY )
		{
			throw new IllegalStateException(
					"ViewFlow can only be used in EXACTLY mode." );
		}
		result = specSize;
		return result;
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight( int measureSpec )
	{
		int result = 0;
		int specMode = MeasureSpec.getMode( measureSpec );
		int specSize = MeasureSpec.getSize( measureSpec );

		// We were told how big to be
		if ( specMode == MeasureSpec.EXACTLY )
		{
			result = specSize;
		}
		// Measure the height
		else
		{
			// Calculate the text bounds
			Rect bounds = new Rect();
			bounds.bottom = (int) ( paintText.descent() - paintText.ascent() );
			result = bounds.bottom - bounds.top + (int) footerTriangleHeight
					+ (int) footerLineHeight + 10;
			return result;
		}
		return result;
	}

	@ SuppressWarnings( "unused" )
	private Typeface getTypefaceByIndex( int typefaceIndex )
	{
		switch ( typefaceIndex )
		{
			case SANS :
				return Typeface.SANS_SERIF;

			case SERIF :
				return Typeface.SERIF;

			case MONOSPACE :
				return Typeface.MONOSPACE;
			default :
				return Typeface.DEFAULT;
		}
	}
}
