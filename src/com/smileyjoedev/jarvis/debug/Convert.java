package com.smileyjoedev.jarvis.debug;

import java.util.ArrayList;
import java.util.Arrays;


public class Convert {


	public static String convert(Object obj){
		String message = "";

		// TODO: HashMap //

		try {
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
				try {
					message = obj.toString();
				} catch (Exception e) {
					message = "UNKNOWN TYPE";
				}
			}
		} catch (NullPointerException e) {
			message = "--NULL--";
		}

		if (message.trim().equals("")) {
			message = "--BLANK--";
		}

		return message;
	}
	
	public static String fromNull(Object obj){
		return "--NULL--";
	}
	
	public static String fromString(Object obj){
		return (String) obj;
	}
	
	public static String fromInteger(Object obj){
		return Integer.toString((Integer) obj);
	}
	
	public static String fromBoolean(Object obj){
		if ((Boolean) obj) {
			return "TRUE";
		} else {
			return "FALSE";
		}
	}
	
	public static String fromLong(Object obj){
		return Long.toString((Long) obj);
	}
	
	public static String fromArrayList(Object obj){
		String message = "";
		
		for (int i = 0; i < ((ArrayList) obj).size(); i++) {
			message += Debug.LINE_BREAK + "[" + i + "]: " + convert(((ArrayList) obj).get(i));
		}

		message += Debug.LINE_BREAK;
		
		return message;
	}
	
	public static String fromArray(Object obj){
		String message = "";
		boolean first = true;
		
		for (int i = 0; i < ((Object[]) obj).length; i++) {
			if (first) {
				first = false;
				message += Debug.LINE_BREAK;
			} else {
				message += "--";
			}
			message += "[" + i + "]: " + convert(((Object[]) obj)[i]);
		}

		message += Debug.LINE_BREAK;
		
		return message;
	}
	
	public static String fromClassArray(Object obj){
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
