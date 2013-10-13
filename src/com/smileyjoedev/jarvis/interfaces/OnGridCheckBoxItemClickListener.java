package com.smileyjoedev.jarvis.interfaces;

/**
 * Listener to be used with {@link GridCheckBoxAdapter}
 * 
 * @author Cody
 *
 */
public interface OnGridCheckBoxItemClickListener {

	/**
	 * Called when an item is clicked
	 * 
	 * @param title		the text title of the item that was clicked
	 * @param id		the id of the item that was clicked
	 * @param isChecked	whether the item is now selected or not
	 */
	public void onItemClick(String title, int id, boolean isChecked);
	
}
