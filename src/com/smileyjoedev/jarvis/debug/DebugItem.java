package com.smileyjoedev.jarvis.debug;

/**
 * This is a single item that is used by {@link Debug}
 * 
 * Everytime something is logged through {@link Debug} an 
 * instance of this is created and stored so that the log
 * can be recalled
 * 
 * @author Cody
 *
 */
public class DebugItem {

	// the message in the log //
	private String message;
	// the tag that is used for the log //
	private String tag;
	// the type of logging, Debug, Warning, Error etc //
	private int type;

	public DebugItem(String message, String tag, int type) {
		this.setMessage(message);
		this.setTag(tag);
		this.setType(type);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Sets the type of debug based on an ID
	 * 
	 * {@link Debug.DEBUG}
	 * {@link Debug.VERBISE}
	 * {@link Debug.WARNING}
	 * {@link Debug.ERROR}
	 * {@link Debug.INFORMATION}
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public String getTag() {
		return tag;
	}

	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		return "DebugItem [getMessage()=" + getMessage() + ", getTag()=" + getTag() + ", getType()=" + getType() + "]";
	}

}
