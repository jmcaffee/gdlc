/**
 * 
 */
package runtime.main;

import runtime.compiler.*;
import runtime.parser.*;


/**
 * @author killer
 *
 */
public class GdlMain {
	static ASTCompilationUnit 	parseTree;
	static CompilerContext		compilerContext;
	
	  public static void main(String args[]) {
		  if(null != (parseTree = parse(args)))
			  compilerContext = new CompilerContext();
			  compile(parseTree, compilerContext);
		  
	      Log.status("GDLC:  Finished.");
	  }

	  public static ASTCompilationUnit parse(String args[]) {
		  	GdlParser 			parser;
		    ASTCompilationUnit	tree;
		    
		    if (args.length == 0) {
		      Log.status("GDLC:  Reading from standard input . . .");
		      parser = new GdlParser(System.in);
		    } else if (args.length == 1) {
		      Log.status("GDLC:  Reading from file " + args[0] + " . . .");
		      try {
		        parser = new GdlParser(new java.io.FileInputStream(args[0]));
		      } catch (java.io.FileNotFoundException e) {
		        Log.error("GDLC:  File not found: " + args[0] + "");
		        return null;
		      }
		    } else {
		      System.out.println("GDLC:  Usage is one of:");
		      System.out.println("         java GdlCompiler < inputfile");
		      System.out.println("OR");
		      System.out.println("         java GdlCompiler inputfile");
		      return null;
		    }
		    try {
		    	tree = parser.CompilationUnit();
		    	Log.status("GDLC:  Guideline parsed successfully.");
		    	parser.dump();
		    } catch (ParseException e) {
		      Log.error("GDLC:  Encountered errors during parsing.");
		      Log.error(e.toString());
		      return null;
		    }
		    
		    GdlMain.parseTree = tree;		// Store the tree for later retrieval
		    
		    return tree;
	  }

	  public static ASTCompilationUnit getParseTree(){
		  return GdlMain.parseTree;
	  }
	  
	  
	  public static void compile(ASTCompilationUnit tree, CompilerContext ctx) {
	      Log.status("GDLC:  Compiling...");

	      if(tree == null){
		      Log.error("GDLC:  Compile failed: parse tree is empty.");
		      return;
	      }

	      GdlCompiler compiler = new GdlCompiler(tree);
	      
	      try {
	    	  compiler.compile(ctx);

	      } catch (CompileException e) {
			      Log.error("GDLC:  Encountered errors during compilation.");
			      Log.error(e.toString());
			      return;
	      }

//		dumpContextData(ctx);
//		
//		if(ctx.hasWarnings()){
//			writeWarnings(ctx);
//		}
//
//		if(ctx.hasErrors()){
//			writeErrors(ctx);
//		}

	  }

	  static void writeWarnings(CompilerContext ctx){
		  Log.status("GDLC has completed with warnings:");
		  ctx.dumpWarnings();
	  }
	  
	  static void writeErrors(CompilerContext ctx){
		  Log.status("GDLC has completed with errors:");
		  ctx.dumpErrors();
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
