package com.smileyjoedev.jarvis.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.smileyjoedev.jarvis.debug.Debug;
import com.smileyjoedev.jarvis.statics.Font;

/**
 * The default textView that will be used for Jarvis
 * @author Cody
 *
 */
public class TextView extends android.widget.TextView {

	public TextView(Context context) {
		super(context);
		init();
	}

	public TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		// Set the font to Roboto //
		Font.set(this, Font.ROBOTO_THIN);
	}
	
}
