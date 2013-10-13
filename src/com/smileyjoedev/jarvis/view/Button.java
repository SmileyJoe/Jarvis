package com.smileyjoedev.jarvis.view;

import com.smileyjoedev.jarvis.statics.Font;

import android.content.Context;
import android.util.AttributeSet;

/**
 * The default button that will be used for Jarvis
 * 
 * @author Cody
 *
 */
public class Button extends android.widget.Button{

	public Button(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public Button(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public Button(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		// Set the font to Roboto thin //
		Font.set(this, Font.ROBOTO_THIN);
	}

}
