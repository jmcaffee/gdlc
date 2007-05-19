package runtime.main;

public class Log {

	public static void info(String msg) {
		Log.out(msg);
	}
	
	public static void error(String msg) {
		Log.out(msg);
	}
	
	public static void status(String msg) {
		Log.out(msg);
	}
	
	public static void out(String output){
		System.out.println(output);
	}
}
