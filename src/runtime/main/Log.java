package runtime.main;

public class Log {
	static boolean verbose = true;
	
	public static void setVerbose(boolean vFlag) {Log.verbose = vFlag;}
	
	public static void info(String msg) {
		if(Log.verbose){
			Log.out(msg);
		}
	}
	
	public static void error(String msg) {
		Log.out(msg);						// Error messages are always written.
	}
	
	public static void status(String msg) {
		if(Log.verbose){
			Log.out(msg);
		}
	}
	
	public static void out(String output){
		System.out.println(output);
	}
}
