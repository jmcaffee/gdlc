/**
 * 
 */
package runtime.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

import runtime.compiler.*;
import runtime.elements.XmlElem;
import runtime.parser.*;
import runtime.visitors.CollectReferencedLookupsVisitor;
import runtime.visitors.LookupNodeVisitor;
import runtime.visitors.XmlVisitor;


/**
 * @author killer
 *
 */
public class CompileMgr {
	ASTCompilationUnit 	parseTree;
	CompilerContext		compilerContext;
	CompilerParameters	params = null;
	
	public CompileMgr() {
		parseTree = null;
		compilerContext = new CompilerContext();
	}
	
	public void compile(CompilerParameters cp) {
		params = cp;
		String srcFile = params.inFile;
											// Store include dirs
		for(String inc : params.incDirs){
			compilerContext.addIncludeDir(inc);
		}
		
		Log.setVerbose(params.verbose);
		
		if(null == (parseTree = parse(srcFile))) {
			Log.error("Parsing failed");
			return;
		}
	    Log.status("GDLC:  Parsing completed.");
		  
		compileParseTree(parseTree, compilerContext);
		 
		if(compilerContext.hasErrors()){
			Log.error("*** GDLC: Compile aborted with errors:");
			Log.error(compilerContext.dumpErrors());
		}
		
		if(compilerContext.hasWarnings()){
			Log.error("*** GDLC: Compile completed with warnings:");
			compilerContext.dumpWarnings();
		}

		if(!compilerContext.hasErrors() && !compilerContext.hasWarnings()){
			Log.error("GDLC:  Compile completed.");
		}
		
		if(compilerContext.hasErrors()){
			return;
		}
		
		if(this.params.generateOutput){
			generateOutput();
		}else{
			Log.error("No output generated (-nooutput switch)");
		}
		
	}

	protected void generateOutput(){
											// Determine name of output file
		String output = new String();
		if(this.params.outFile.length() < 1){
			ASTGuidelineDef gdl = compilerContext.getGuideline();
			if(null == gdl){
				Log.error("No guideline definitions exist to write. Use '-raw' flag to force output.");
				return;
			}
			
			output = gdl.getName();
		}
		else{
			output = this.params.outFile;
		}
		
		if(output.length() < 1){
			Log.error("Unable to resolve an output filename.");
			return;
		}
											// Output file should not have an extension.
		output = output.concat(".xml");		// Add XML extension to filename.
		
											// Write file to destination.
		
		if(this.writeXmlToFile(output)){
			Log.error("XML written to file [" + output + "].");
		}
		else{
			Log.error("Errors occurred while writing XML [" + output + "].");
		}

	}
	  private ASTCompilationUnit parse(String srcFile) {
		  	GdlParser 			parser;
		    ASTCompilationUnit	tree;
		    
		    Log.status("GDLC:  Reading from file " + srcFile + " . . .");
		    try {
		        parser = new GdlParser(new java.io.FileInputStream(srcFile));
		    } 
		    catch (java.io.FileNotFoundException e) {
			        Log.error("GDLC:  File not found: " + srcFile + "");
			        return null;
			}

//	        if (args.length == 0) {
//		      Log.status("GDLC:  Reading from standard input . . .");
//		      parser = new GdlParser(System.in);
//		    } else if (args.length == 1) {
//		      Log.status("GDLC:  Reading from file " + args[0] + " . . .");
//		      try {
//		        parser = new GdlParser(new java.io.FileInputStream(args[0]));
//		      } catch (java.io.FileNotFoundException e) {
//		        Log.error("GDLC:  File not found: " + args[0] + "");
//		        return null;
//		      }
//		    } else {
//		      System.out.println("GDLC:  Usage is one of:");
//		      System.out.println("         java GdlCompiler < inputfile");
//		      System.out.println("OR");
//		      System.out.println("         java GdlCompiler inputfile");
//		      return null;
//		    }
		    try {
		    	tree = parser.CompilationUnit();
		    	Log.status("GDLC:  Guideline parsed successfully.");
		    	if(params.verboseParse){
		    		parser.dump();
		    	}
		    } catch (ParseException e) {
		      Log.error("GDLC:  Encountered errors during parsing.");
		      Log.error(e.toString());
		      return null;
		    }
		    
		    parseTree = tree;		// Store the tree for later retrieval
		    
		    return tree;
	  }

	  public ASTCompilationUnit getParseTree(){
		  return parseTree;
	  }
	  
	  
	  public IProgramContext getContext(){
		  return compilerContext;
	  }
	  
	  
	  private void compileParseTree(ASTCompilationUnit tree, IProgramContext ctx) {
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
	      
	  }

		public String getRuleXml(String key){
			ASTRuleDef rule = compilerContext.getRule(key);
			if(null == rule){ return new String(""); }
			
			// Create XmlVisitor
			XmlVisitor xmlVisitor = new XmlVisitor(this.getContext());
			XmlElem	result = new XmlElem("Result");
			
			// Run the visitor against the rule.
			rule.jjtAccept(xmlVisitor, result);

			return result.getContent();
		}
		

		public String getRulesetXml(String key){
			IRuleset rs = compilerContext.getRuleset(key);
			if(null == rs){ return new String(""); }
			
			// Create XmlVisitor
			XmlVisitor xmlVisitor = new XmlVisitor(this.getContext());
			XmlElem	result = new XmlElem("Result");
			
			// Run the visitor against the rule.
			rs.getNode().jjtAccept(xmlVisitor, result);

			return result.getContent();
		}
		
		public String getGuidelineXml(){
			ASTGuidelineDef gdl = compilerContext.getGuideline();
			if(null == gdl){ return new String(""); }
			
			// Create XmlVisitor
			XmlVisitor xmlVisitor = new XmlVisitor(this.getContext());
			XmlElem	result = new XmlElem("Result");
			
			// Run the visitor against the rule.
			gdl.jjtAccept(xmlVisitor, result);

			return result.getContent();
		}
		
		public void writeXml(Writer out) throws IOException {
			ASTGuidelineDef gdl = compilerContext.getGuideline();
			if(null == gdl){ 
				Log.error("No guideline has been defined.");
				return; 
			}
			
			// Create XmlVisitor
			XmlVisitor xmlVisitor = new XmlVisitor(this.getContext());
			XmlElem	gdlRoot = new XmlElem("GuidelineRoot");
			
			// Run the visitor against the guideline node.
			gdl.jjtAccept(xmlVisitor, gdlRoot);
			
			// Build a list of lookup names referenced in the guideline definition.
			CollectReferencedLookupsVisitor lookupList = new CollectReferencedLookupsVisitor(compilerContext);
			gdl.jjtAccept(lookupList, null);
			
			XmlElem lookupData = buildLookupsElement(lookupList.lookups);
			gdlRoot.appendXml(lookupData.toXml());
			
			String content = gdlRoot.toXml();
			out.write(content);
			out.close();
		}
		
		protected XmlElem buildLookupsElement(HashMap<String,String> list){
			XmlElem lookupData = new XmlElem("LOOKUPS");
			
			
			for(String lkName : list.keySet()){
				buildLookup(lookupData, lkName);
			}
			
			return lookupData;
		}
		
		protected void buildLookup(XmlElem parent, String lkName){
			ASTLookupDef node = compilerContext.getLookup(lkName);
			LookupNodeVisitor lkupVisitor = new LookupNodeVisitor(compilerContext);
			
			node.jjtAccept(lkupVisitor, parent);
			
		}
	
		protected boolean writeXmlToFile(String filepath){
			BufferedWriter out = null;
			try{
				out = new BufferedWriter(new FileWriter(filepath));
				this.writeXml(out);
			}
			catch(IOException e){
//				fail("IOException thrown while creating output file [" + outFile + "]");
				return false;
			}
			
			return true;
		}
		


}

