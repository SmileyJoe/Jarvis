package com.smileyjoedev.jarvis.debug;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Handles the conversion of any variable type to a String
 * 
 * Will cycle through ararys, lists, objects and build a string to represent it.
 * Will call the default toString of objects.
 * 
 * @author Cody
 * 
 */
public class Convert {

	// TODO: HashMap //

	/**
	 * Will take the object and try to convert it into a String
	 * 
	 * @param obj	the object to convert
	 */
	public static String convert(Object obj) {
		String message = "";

		try {
			// try find out what the object is, and convert it appropriately //
			if (obj instanceof String) {
				message = fromString(obj);
			} else if (obj instanceof Boolean) {
				message = fromBoolean(obj);
			} else if (obj instanceof Integer) {
				message = fromInteger(obj);
			} else if (obj instanceof Long) {
				message = fromLong(obj);
			} else if (obj instanceof ArrayList) {
				message = fromArrayList(obj);
			} else if (obj instanceof Object[]) {
				message = fromArray(obj);
			} else if (obj.getClass().isArray()) {
				message = fromClassArray(obj);
			} else if (obj == null) {
				message = fromNull(obj);
			} else {
				// if it is not found yet, call the default toString //
				try {
					message = obj.toString();
				} catch (Exception e) {
					// if anything goes wrong we dont know what it is //
					message = "UNKNOWN TYPE";
				}
			}
		// catch a null and make a message //
		} catch (NullPointerException e) {
			message = fromNull(obj);
		}

		// if the message is blank we want to say so //
		if (message.trim().equals("")) {
			message = "--BLANK--";
		}

		return message;
	}

	/**
	 * Get a default null message
	 * 
	 * @param obj	the object
	 * @return
	 */
	public static String fromNull(Object obj) {
		return "--NULL--";
	}

	/**
	 * From a string
	 * 
	 * @param obj	the object
	 * @return
	 */
	public static String fromString(Object obj) {
		return (String) obj;
	}

	/**
	 * Convert an integer to a string
	 * 
	 * @param obj	the object
	 * @return
	 */
	public static String fromInteger(Object obj) {
		return Integer.toString((Integer) obj);
	}

	/**
	 * Converts a bool to a String message
	 * 
	 * @param obj	the object
	 * @return
	 */
	public static String fromBoolean(Object obj) {
		if ((Boolean) obj) {
			return "TRUE";
		} else {
			return "FALSE";
		}
	}

	/**
	 * Converts a long to a String
	 * 
	 * @param obj	the object
	 * @return
	 */
	public static String fromLong(Object obj) {
		return Long.toString((Long) obj);
	}

	/**
	 * Converts an array list of any object type to a String
	 * message
	 * 
	 * @param obj	the object
	 * @return
	 */
	public static String fromArrayList(Object obj) {
		String message = "";

		// cycle throug the arrayList //
		for (int i = 0; i < ((ArrayList) obj).size(); i++) {
			// add to the message, recall convert so that any type can be converted correctly //
			message += Debug.LINE_BREAK + "[" + i + "]: " + convert(((ArrayList) obj).get(i));
		}

		message += Debug.LINE_BREAK;

		return message;
	}

	/**
	 * Converts an array to a String
	 * 
	 * @param obj	the object to convery
	 * @return
	 */
	public static String fromArray(Object obj) {
		String message = "";
		boolean first = true;

		// cycle through the array //
		for (int i = 0; i < ((Object[]) obj).length; i++) {
			if (first) {
				first = false;
				message += Debug.LINE_BREAK;
			} else {
				message += "--";
			}
			// add the position to the message, send the object to be converted //
			message += "[" + i + "]: " + convert(((Object[]) obj)[i]);
		}

		message += Debug.LINE_BREAK;

		return message;
	}

	public static String fromClassArray(Object obj) {
		String message = "";

		try {
			// TODO: Find a way to hadle arrays of primitive type //
			message = Debug.LINE_BREAK + Arrays.asList(obj).toString() + Debug.LINE_BREAK;
		} catch (Exception e) {
			message = "UNKNOWN TYPE";
		}

		return message;
	}

}
