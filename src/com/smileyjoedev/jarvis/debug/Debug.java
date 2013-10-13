package com.smileyjoedev.jarvis.debug;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.smileyjoedev.jarvis.HUD;

/**
 * Handle Debugging to LogCat using {@link Log} Used to make the logging process
 * easier by including methods that accept an {@link Integer},
 * {@link ArrayList}, {@link String}, {@link Boolean}, {@link Long}, array, object
 * as a message and convert it to a string for logging.
 * 
 * Will also keep an {@link ArrayList} of {@link DebugItem} of everything
 * that is logged so that it can be recalled if needed.
 * 
 * <p>
 * d - debug e - error i - info v - verbose w - warn
 * </p>
 * 
 * @author Cody
 * 
 */
public class Debug {

	// TODO: Temp tag //
	// TODO: Something for views, get the text of a text view, editText etc //
	// TODO: Dialog Fragment:
	// - When initiated, hold down menu key to launch
	// - Stack Trace
	// - Button to email the Log/StackTrace
	// - Button to save to sdCard as text file //
	// TODO: Remove monitor class and have it as part of this, so Debug debug = new Debug(), debug.log(), debug.end() //

	private static String LOG_NAME = "smileyJoeDev";
	public static final String LINE_BREAK = "\n";
	private static boolean IS_ENABLED = false;
	private static Monitor MONITOR;
	private static ArrayList<Integer> TYPES = new ArrayList<Integer>();
	private static ArrayList<DebugItem> LOG = new ArrayList<DebugItem>();

	// Types //
	public static final int DEBUG = 1;
	public static final int ERROR = 2;
	public static final int INFO = 3;
	public static final int VERBOSE = 4;
	public static final int WARNING = 5;
	public static final int ALL = 6;
	public static final int NONE = 7;

	/****************************************************
	 * Setters
	 ***************************************************/

	public static void setLogName(String logName) {
		LOG_NAME = logName;
	}

	public static void isEnabled(boolean isEnabled) {
		IS_ENABLED = isEnabled;
	}

	public static void setLogType(int... params) {
		TYPES = new ArrayList<Integer>();

		for (int i = 0; i < params.length; i++) {
			TYPES.add(params[i]);
		}
	}
	
	/****************************************************
	 * Getters
	 ***************************************************/

	public static int getLogSize(){
		return Debug.LOG.size();
	}
	
	public static DebugItem getLogItem(int position){
		return Debug.LOG.get(position);
	}
	
	public static void clearLog(){
		Debug.LOG.clear();
	}
	
	/****************************************************
	 * Jarvis
	 ***************************************************/
	
	private static class ShowJarvisOnClickListener implements OnClickListener{

		private Activity mActivity;
		
		public ShowJarvisOnClickListener(Activity activity) {
			mActivity = activity;
		}
		
		@Override
		public void onClick(View v) {
			Debug.showJarvis(mActivity);
		}
		
	}
	
	public static void initJarvis(Activity activity, int resId){
		activity.findViewById(resId).setOnClickListener(new ShowJarvisOnClickListener(activity));
	}
	
	public static void showJarvis(Activity activity){
		HUD hud = new HUD();
		
		hud.show(activity.getFragmentManager(), null);
	}
	
	/****************************************************
	 * Base Logs
	 ***************************************************/

	public static void d(Object... params) {
		if (Debug.isLogging(DEBUG)) {
			String message = Debug.handleMessage(params);
			Log.d(Debug.LOG_NAME, message);
			LOG.add(new DebugItem(message, Debug.LOG_NAME, Debug.DEBUG));
		}
	}

	public static void e(Object... params) {
		if (Debug.isLogging(ERROR)) {
			String message = Debug.handleMessage(params);
			Log.e(Debug.LOG_NAME, message);
			LOG.add(new DebugItem(message, Debug.LOG_NAME, Debug.ERROR));
		}
	}

	public static void i(Object... params) {
		if (Debug.isLogging(INFO)) {
			String message = Debug.handleMessage(params);
			Log.i(Debug.LOG_NAME, message);
			LOG.add(new DebugItem(message, Debug.LOG_NAME, Debug.INFO));
		}
	}

	public static void v(Object... params) {
		if (Debug.isLogging(VERBOSE)) {
			String message = Debug.handleMessage(params);
			Log.v(Debug.LOG_NAME, message);
			LOG.add(new DebugItem(message, Debug.LOG_NAME, Debug.VERBOSE));
		}
	}

	public static void w(Object... params) {
		if (Debug.isLogging(WARNING)) {
			String message = Debug.handleMessage(params);
			Log.w(Debug.LOG_NAME, message);
			LOG.add(new DebugItem(message, Debug.LOG_NAME, Debug.WARNING));
		}
	}

	/*******************************************************
	 * Extra Logs
	 ******************************************************/

	public static void trace(Context context) {
		trace(context, Debug.DEBUG);
	}

	public static void trace(Context context, int type) {
		boolean first = true;
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		String packageName = context.getPackageName();
		String message = "";
		int starLength = 0;

		for (int i = 0; i < trace.length; i++) {
			StackTraceElement elm = trace[i];

			if (elm.getClassName().contains(packageName)) {
				if (first) {
					first = false;
					message += "************ TRACE - " + elm.getMethodName() + " ************" + Debug.LINE_BREAK;
					starLength = message.length() - 1;
				}

				message += elm.getClassName().replace(packageName + ".", "") + " : " + elm.getMethodName() + " (" + elm.getLineNumber() + ")" + Debug.LINE_BREAK;

			}
		}

		for (int i = 0; i < starLength; i++) {
			message += "*";
		}

		switch (type) {
			case Debug.DEBUG:
				d(message);
				break;
			case Debug.ERROR:
				e(message);
				break;
			case Debug.INFO:
				i(message);
				break;
			case Debug.VERBOSE:
				v(message);
				break;
			case Debug.WARNING:
				w(message);
				break;
			default:
				d(message);
				break;
		}
	}

	/**********************************************************
	 * Monitor
	 *********************************************************/

	public static void initMonitor(Context context) {
		Debug.initMonitor(context, null);
	}

	public static void initMonitor(Context context, String method) {
		MONITOR = new Monitor(context);

		MONITOR.start(method);
	}

	public static void endMonitor() {
		MONITOR.end();
	}

	public static void monitor() {
		Debug.monitor(null);
	}

	public static void monitor(String message) {
		MONITOR.log(message);
	}

	/*******************************************************
	 * Internal
	 ******************************************************/

	private static String handleMessage(Object... params) {
		String message = "";
		boolean first = true;

		if (params.length == 0) {
			message = "*******************";
			return message;
		}

		for (int i = 0; i < params.length; i++) {
			Object param = params[i];

			if (param instanceof ArrayList || param instanceof Object[]) {
				first = true;
			}

			if (first) {
				first = false;
			} else {
				message += ": ";
			}

			if (param instanceof ArrayList || param instanceof Object[]) {
				first = true;
			}

			message += Convert.convert(param);
		}

		return message;
	}

	private static boolean isLogging(int type) {
		boolean isLogging = false;

		if (IS_ENABLED) {
			if (TYPES.size() == 0) {
				TYPES.add(ALL);
			}
			if (TYPES.contains(type)) {
				isLogging = true;
			} else if (TYPES.contains(ALL)) {
				isLogging = true;
			}
		}

		return isLogging;
	}

	/***********************************************
	 * Internal Class
	 * 
	 * @author Cody
	 * 
	 **********************************************/

	private static class Monitor {

		private int mStartLineLength = 0;
		private Context mContext;
		private String mMessage;

		public Monitor(Context context) {
			mContext = context;
			mMessage = "";
		}

		public void start(String method) {

			if (method == null) {
				mMessage += "************************";
			} else {
				mMessage += "************ " + method + " ************";
			}

			mMessage += Debug.LINE_BREAK;

			mStartLineLength = mMessage.length();

		}

		public void log(String message) {
			mMessage += Integer.toString(this.getLineNumber());

			if (message != null) {
				mMessage += " - " + message;
			} else {

			}

			mMessage += Debug.LINE_BREAK;

		}

		public void end() {
			for (int i = 0; i < mStartLineLength; i++) {
				mMessage += "*";
			}

			mMessage += Debug.LINE_BREAK;

			Debug.d(mMessage);

			mMessage = "";
		}

		private int getLineNumber() {
			int lineNumber = -1;

			StackTraceElement[] trace = Thread.currentThread().getStackTrace();
			String packageName = mContext.getPackageName();

			for (int i = 0; i < trace.length; i++) {
				StackTraceElement elm = trace[i];

				if (elm.getClassName().contains(packageName)) {
					lineNumber = elm.getLineNumber();
					break;
				}
			}

			return lineNumber;
		}

	}
}
