/**
 * 
 */
package runtime.main;

import runtime.compiler.*;


/**
 * @author killer
 *
 */
public class GdlMain {
	
	  public static void main(String args[]) {
		  CompilerParameters cp = new CompilerParameters();
		  
//		  if(args.length == 0){
//			  cp.usage();
//			  return;
//		  }
		  
		  cp.process(args);
		  if(cp.isValid){
			  CompileMgr mgr = new CompileMgr();
			  mgr.execute(cp);
		  }
	  }

//	  public static void usage(){
//		System.out.println();
//		System.out.println("======================================================================");
//		System.out.println("GDLC GuideLine Compiler");
//		System.out.println("Usage:  GDLC inFile [/I]*");
//		System.out.println();
//		System.out.println("	inFile    name of file to compile.");
//		System.out.println();
//		System.out.println("   --switches--");
//		System.out.println("	none");
//		System.out.println();
//		System.out.println("   --parameters--");
//		System.out.println("	/Ipath        path to include dir.");
//		System.out.println();
//		System.out.println("======================================================================");
//		System.out.println();
//
//	  }
	  
	  static void writeWarnings(CompilerContext ctx){
		  Log.status("GDLC has completed with warnings:");
		  ctx.dumpWarnings();
	  }
	  
	  static void writeErrors(CompilerContext ctx){
		  Log.status("GDLC has completed with errors:");
		  Log.error(ctx.dumpErrors());
	  }
	  
	  static void dumpContextData(CompilerContext ctx) {

//		Log.info("");
//		Log.info("---------- ERRORS ----------");
//		ctx.dumpErrors();
//		Log.info("");
//
//		Log.info("");
//		Log.info("---------- WARNINGS ----------");
//		ctx.dumpWarnings();
//		Log.info("");
//
		Log.info("");
		Log.info("---------- DPM variables ----------");
		ctx.dumpDpmVars();
		Log.info("");

		Log.info("");
		Log.info("---------- PPM variables ----------");
		ctx.dumpPpmVars();
		Log.info("");

		Log.info("");
		Log.info("---------- Rule Defs ----------");
		ctx.dumpRules();
		Log.info("");

		Log.info("");
		Log.info("---------- Ruleset Defs ----------");
		ctx.dumpRulesets();
		Log.info("");

		Log.info("");

	  }
}
