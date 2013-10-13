package com.smileyjoedev.jarvis.statics;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Class used for handling fonts
 * 
 * @author Cody
 *
 */
public class Font {

	// ID for the Roboto thin font //
	public static final int ROBOTO_THIN = 1;
	
	/**
	 * Sets the font to a supplied view
	 * 
	 * Takes an id {@link Font.ROBOTO_THIN}
	 * 
	 * @param view		the view to apply the font
	 * @param fontId	the id of the font
	 */
	public static void set(TextView view, int fontId){
		try{
			Typeface tf = Typeface.createFromAsset(view.getContext().getAssets(), getName(fontId));
			view.setTypeface(tf);
		} catch (RuntimeException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the name (full path) of the font based on
	 * the font id.
	 * 
	 * {@link Font.ROBOTO_THIN}
	 * @param fontId	the id of the font
	 * @return			the name (full path) of the font
	 */
	private static final String getName(int fontId){
		String font = "";
		
		switch(fontId){
			case ROBOTO_THIN:
				font = "fonts/Roboto-Thin.ttf";
				break;
		}
		
		return font;
	}
	
}
