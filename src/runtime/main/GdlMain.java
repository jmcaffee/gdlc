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
	static ASTCompilationUnit parseTree;
	
	  public static void main(String args[]) {
		  parse(args);
		  compile();
		  
	      System.out.println("Guideline Compiler :  Finished.");
	  }

	  public static void parse(String args[]) {
		    GdlParser parser;
		    if (args.length == 0) {
		      System.out.println("Guideline Compiler :  Reading from standard input . . .");
		      parser = new GdlParser(System.in);
		    } else if (args.length == 1) {
		      System.out.println("Guideline Compiler :  Reading from file " + args[0] + " . . .");
		      try {
		        parser = new GdlParser(new java.io.FileInputStream(args[0]));
		      } catch (java.io.FileNotFoundException e) {
		        System.out.println("Guideline Compiler :  File " + args[0] + " not found.");
		        return;
		      }
		    } else {
		      System.out.println("Guideline Compiler :  Usage is one of:");
		      System.out.println("         java GdlCompiler < inputfile");
		      System.out.println("OR");
		      System.out.println("         java GdlCompiler inputfile");
		      return;
		    }
		    try {
		      parseTree = parser.CompilationUnit();
		      System.out.println("Guideline Compiler :  Guideline parsed successfully.");
		      parser.dump();
		    } catch (ParseException e) {
		      System.out.println("Guideline Compiler :  Encountered errors during parsing.");
		      System.out.println(e.toString());
		      return;
		    }
	  }

	  public static void compile() {
	      System.out.println("Guideline Compiler :  Compiling...");

	      if(parseTree == null){
		      System.out.println("Guideline Compiler :  Compile failed: parse tree is empty.");
		      return;
	      }

	      GdlCompiler compiler = new GdlCompiler(parseTree);
	      
	      try {
	    	  compiler.compile();

	      } catch (CompileException e) {
			      System.out.println("Guideline Compiler :  Encountered errors during compilation.");
			      System.out.println(e.toString());
			      return;
	      }

	  }


}
