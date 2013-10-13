package com.smileyjoedev.jarvis.debug;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.smileyjoedev.jarvis.Hud;

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

	/**
	 * Sets the types that will be logged
	 * 
	 * {@link Debug.DEBUG}
	 * {@link Debug.VERBISE}
	 * {@link Debug.WARNING}
	 * {@link Debug.ERROR}
	 * {@link Debug.INFORMATION}
	 * {@link Debug.ALL}
	 * {@link Debug.NONE}
	 * 
	 * @param params the types to log
	 */
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

	// OnClickListener that is used to show Jarvis HUD //
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
	
	/**
	 * Applies the onClick listener to a view
	 * 
	 * @param activity	the current activity
	 * @param resId		view to apply the listener to
	 */
	public static void initJarvis(Activity activity, int resId){
		activity.findViewById(resId).setOnClickListener(new ShowJarvisOnClickListener(activity));
	}
	
	/**
	 * Hows the Jsrvis HUD
	 * 
	 * @param activity	the current activity
	 */
	public static void showJarvis(Activity activity){
		Hud hud = new Hud();
		
		hud.show(activity.getFragmentManager(), null);
	}
	
	/****************************************************
	 * Base Logs
	 ***************************************************/

	/**
	 * Logs the given params as type Debug.
	 * 
	 * Will add an item to {@link Debug.LOG}
	 */
	public static void d(Object... params) {
		if (Debug.isLogging(DEBUG)) {
			String message = Debug.handleMessage(params);
			Log.d(Debug.LOG_NAME, message);
			LOG.add(new DebugItem(message, Debug.LOG_NAME, Debug.DEBUG));
		}
	}

	/**
	 * Logs the given params as type Error.
	 * 
	 * Will add an item to {@link Debug.LOG}
	 */
	public static void e(Object... params) {
		if (Debug.isLogging(ERROR)) {
			String message = Debug.handleMessage(params);
			Log.e(Debug.LOG_NAME, message);
			LOG.add(new DebugItem(message, Debug.LOG_NAME, Debug.ERROR));
		}
	}

	/**
	 * Logs the given params as type Information.
	 * 
	 * Will add an item to {@link Debug.LOG}
	 */
	public static void i(Object... params) {
		if (Debug.isLogging(INFO)) {
			String message = Debug.handleMessage(params);
			Log.i(Debug.LOG_NAME, message);
			LOG.add(new DebugItem(message, Debug.LOG_NAME, Debug.INFO));
		}
	}

	/**
	 * Logs the given params as type Verbose.
	 * 
	 * Will add an item to {@link Debug.LOG}
	 */
	public static void v(Object... params) {
		if (Debug.isLogging(VERBOSE)) {
			String message = Debug.handleMessage(params);
			Log.v(Debug.LOG_NAME, message);
			LOG.add(new DebugItem(message, Debug.LOG_NAME, Debug.VERBOSE));
		}
	}

	/**
	 * Logs the given params as type Warning.
	 * 
	 * Will add an item to {@link Debug.LOG}
	 */
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

	/**
	 * {@link trace} with a default type of Debug
	 * 
	 * @param context	the context of the application
	 */
	public static void trace(Context context) {
		trace(context, Debug.DEBUG);
	}

	/**
	 * Traces a method
	 * 
	 * Cycles through the stack trace and outputs the name
	 * off the method as well as the line number if it exists in
	 * the current contexts package
	 * 
	 * @param context	the current context
	 * @param type		the type of log
	 */
	public static void trace(Context context, int type) {
		// the first method ... the one that is being traced //
		boolean first = true;
		// the stack trace //
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		// the current package name //
		String packageName = context.getPackageName();
		// the message that will be logged //
		String message = "";
		// the amount of stars to add at the end to match the title //
		int starLength = 0;

		// cycle through the stack trace //
		for (int i = 0; i < trace.length; i++) {
			// get the element //
			StackTraceElement elm = trace[i];

			// check if it is in the current package //
			if (elm.getClassName().contains(packageName)) {
				// if it is the first one set the title as the method name as this is what we are tracing //
				if (first) {
					first = false;
					message += "************ TRACE - " + elm.getMethodName() + " ************" + Debug.LINE_BREAK;
					// get the length of the title so that we can set the correct amount of stars at the end //
					starLength = message.length() - 1;
				}

				// add the method name and line number to the message //
				message += elm.getClassName().replace(packageName + ".", "") + " : " + elm.getMethodName() + " (" + elm.getLineNumber() + ")" + Debug.LINE_BREAK;

			}
		}

		// add the final stars to close off the trace //
		for (int i = 0; i < starLength; i++) {
			message += "*";
		}

		// log the message as the selected type //
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

	/**
	 * Will start to monitor with a blank title
	 * 
	 * @param context	the current context
	 */
	public static void initMonitor(Context context) {
		Debug.initMonitor(context, null);
	}

	/**
	 * Will create an instance of the monitor class and
	 * prepare it to monitor {@link Monitor}.
	 * 
	 * @param context	the current context
	 * @param method	the method that is being monitored, used in the title
	 */
	public static void initMonitor(Context context, String method) {
		MONITOR = new Monitor(context);

		MONITOR.start(method);
	}

	/**
	 * Ends the monitor and logs the results.
	 */
	public static void endMonitor() {
		MONITOR.end();
	}

	/**
	 * Log the line number in the monitor instance
	 * 
	 * {@link Debug.initMonitor} needs to be called first
	 */
	public static void monitor() {
		Debug.monitor(null);
	}

	/**
	 * Log the line number and a message in the current monitor instance
	 * 
	 * {@link Debug.initMonitor} needs to be called first
	 * 
	 * @param message	the message to log
	 */
	public static void monitor(String message) {
		MONITOR.log(message);
	}

	/*******************************************************
	 * Internal
	 ******************************************************/

	/**
	 * Builds the message to be logged based on the params
	 * sent through.
	 * 
	 * Can accept almost any variable type and as many
	 * as required, they do not all need to be of the same
	 * type.
	 */
	private static String handleMessage(Object... params) {
		String message = "";
		boolean first = true;

		// if no params are sent through log a line of stars as a line breaker //
		if (params.length == 0) {
			message = "*******************";
			return message;
		}

		// cycle through the params //
		for (int i = 0; i < params.length; i++) {
			// get a param //
			Object param = params[i];

			// check if the param is an array //
			if (param instanceof ArrayList || param instanceof Object[]) {
				first = true;
			}

			if (first) {
				first = false;
			} else {
				message += ": ";
			}

			// convert the param to a string //
			message += Convert.convert(param);
		}

		return message;
	}

	/**
	 * Checks if the type has been selected to be logged
	 * 
	 * @param type	the type to be checked
	 * @return		whether the type is being logged
	 */
	private static boolean isLogging(int type) {
		boolean isLogging = false;

		// first check if logging is enabled at all //
		if (IS_ENABLED) {
			// if no types are selected default it to all //
			if (TYPES.size() == 0) {
				TYPES.add(ALL);
			}
			
			// check if the selected type needs to be logged //
			if (TYPES.contains(type)) {
				isLogging = true;
			// if not check if all types are being logged //
			} else if (TYPES.contains(ALL)) {
				isLogging = true;
			}
		}

		return isLogging;
	}

	/***********************************************
	 * Internal Class
	 * 
	 * This class is used to monitor the path of a code,
	 * use it to check what conditions are being met.
	 * 
	 * @author Cody
	 * 
	 **********************************************/

	private static class Monitor {

		// the number of stars needed to close the log //
		private int mStartLineLength = 0;
		// the current context //
		private Context mContext;
		// the message that will be logged //
		private String mMessage;

		public Monitor(Context context) {
			mContext = context;
			mMessage = "";
		}

		/**
		 * Start the monitor service
		 * 
		 * @param method	the name of the method that is being monitored
		 */
		public void start(String method) {

			if (method == null) {
				mMessage += "************************";
			} else {
				mMessage += "************ " + method + " ************";
			}

			mMessage += Debug.LINE_BREAK;

			mStartLineLength = mMessage.length();

		}

		/**
		 * Add the line number and the message to the main message
		 * 
		 * @param message	the message for this line
		 */
		public void log(String message) {
			mMessage += Integer.toString(this.getLineNumber());

			if (message != null) {
				mMessage += " - " + message;
			} else {

			}

			mMessage += Debug.LINE_BREAK;

		}

		/**
		 * Log the message as a whole
		 */
		public void end() {
			// add the correct amount of stars //
			for (int i = 0; i < mStartLineLength; i++) {
				mMessage += "*";
			}

			mMessage += Debug.LINE_BREAK;

			// log the message //
			Debug.d(mMessage);

			mMessage = "";
		}

		/**
		 * Gets the current line number by going through the stack trace
		 * and getting the line number of the first instance that
		 * is of the current contexts package
		 * 
		 * @return
		 */
		private int getLineNumber() {
			int lineNumber = -1;

			// get the stack trace //
			StackTraceElement[] trace = Thread.currentThread().getStackTrace();
			// get the package name //
			String packageName = mContext.getPackageName();

			// cycle through the stack trace //
			for (int i = 0; i < trace.length; i++) {
				// get the element //
				StackTraceElement elm = trace[i];

				// check if the element is in the package //
				if (elm.getClassName().contains(packageName)) {
					// get the line number //
					lineNumber = elm.getLineNumber();
					break;
				}
			}

			return lineNumber;
		}

	}
}
