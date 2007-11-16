package runtime.main;

import java.text.DateFormat;

public class Log {
	static boolean 	verbose 		= true;
	static String	statusPrefix 	= "";
	static String	errorPrefix 	= "";
	static String	warningPrefix 	= "";
	static DateFormat df 			= DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
	
	public static void setVerbose(boolean vFlag) {Log.verbose = vFlag;}
	
	public static void setStatusPrefix(String prefix){Log.statusPrefix = prefix;}
	
	public static void setErrorPrefix(String prefix){Log.errorPrefix = prefix;}

	public static void setWarningPrefix(String prefix){Log.warningPrefix = prefix;}

	public static String getCurrentDT(){
		return df.format(System.currentTimeMillis());
	}
	
	public static void info(String msg) {
		if(Log.verbose){
			Log.out(msg);
		}
	}
	
	public static void error(String msg) {
		Log.out(Log.errorPrefix + msg);			// Error messages are always written.
	}
	
	public static void warning(String msg) {
		Log.out(Log.warningPrefix + msg);		// Warning messages are always written.
	}
	
	public static void status(String msg) {
		if(Log.verbose){
			Log.out("["+ Log.getCurrentDT() +"] "+ Log.statusPrefix + msg);
		}
	}
	
	public static void out(String output){
		System.out.println(output);
	}
}
