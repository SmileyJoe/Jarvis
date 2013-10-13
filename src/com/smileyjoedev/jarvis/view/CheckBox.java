package com.smileyjoedev.jarvis.view;

import com.smileyjoedev.jarvis.statics.Font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * The default checkbox that will be used by Jarvis
 * @author Cody
 *
 */
public class CheckBox extends android.widget.CheckBox {

	public CheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public CheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public CheckBox(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		// Set the font to Roboto Thin //
		Font.set(this, Font.ROBOTO_THIN);
	}
	
}
