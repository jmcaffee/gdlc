package runtime.visitors;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import runtime.compiler.CompileException;
import runtime.compiler.CompilerContext;
import runtime.compiler.GdlCompiler;
import runtime.compiler.IProgramContext;
import runtime.compiler.PowerLookupData;
import runtime.compiler.PowerLookupRulesetBuilder;
import runtime.main.CompileError;
import runtime.main.Log;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.ASTImport;
import runtime.parser.GdlParser;
import runtime.parser.ParseException;
import runtime.reader.CsvPowerLookupFile;

public class ImportPowerLookupVisitor extends DepthFirstVisitor {
	HashMap<String,PowerLookupData>	plData	= null;		// Used for storing power lookup data.

	public Object visit(ASTImport node, Object data) {
		// Handle an import node
		CompilerContext ctx = (CompilerContext)data;
		String importType = node.getData("importType");
		String filepath = node.getData("filename");
		
		if(importType.equals("lookup")){
			// Skip this:
			// importLookups(ctx, filepath);
		}
		else if(importType.equals("powerlookup")){
			importPowerLookups(ctx, filepath);
		}
		else {
			ctx.addError(new CompileError(CompileError.errors.UNSUPPORTEDOPERATION,
					new String("Import type [" + importType + "] is not currently supported.")));
		}
		
		return ctx;
	}

	protected void importPowerLookups(CompilerContext ctx, String filename){
		CsvPowerLookupFile lk = new CsvPowerLookupFile();
		
		String filepath = ctx.findIncludePath(filename);

		try{
			lk.parse(filepath);
		}
		catch(FileNotFoundException e){
			ctx.addError(new CompileError(CompileError.errors.FILENOTFOUND,
					new String("PowerLookup file [" + filepath + "] not found.")));
			return;
		}
		catch(IOException e){
			ctx.addError(new CompileError(CompileError.errors.UNKNOWN,
					new String("There was an problem reading the power lookup file [" + filepath + "].")));
			return;
		}
		
		this.plData	= new HashMap<String,PowerLookupData>();
		try{
			
			lk.extractLookupsTo(this.plData);
		}
		catch(CompileException e){
			ctx.addError(new CompileError(CompileError.errors.IMPORTERROR,
					new String("[" + filepath +"] Errors have occured while importing power lookups: " + e.getMessage())));
		}
		
		lk.dumpData();

		buildPowerLookups(ctx);
	}

	/**
	 * buildPowerLookups creates power lookup rulesets/rules and adds them to the context.
	 * @param ctx context to add rulesets/rules to.
	 */
	protected void buildPowerLookups(CompilerContext ctx){
		if(null == this.plData){
			ctx.addError(new CompileError(CompileError.errors.IMPORTERROR,
					new String("No power lookup data imported.")));
			return;
		}
		
		StringBuffer 				pl 			= new StringBuffer();
		PowerLookupRulesetBuilder 	plBuilder 	= new PowerLookupRulesetBuilder(ctx);
		ASTCompilationUnit 			tree 		= null;
		GdlParser 					parser 		= null;
		
		for(String plName : this.plData.keySet()){
			pl.setLength(0);
			pl.append(plBuilder.build(this.plData.get(plName)));
											// Parse pl buffer.
			parser = new GdlParser(inputStreamFromStringBuffer(pl));
		    try {
		    	tree = parser.CompilationUnit();
		    } catch (ParseException e) {
				ctx.addError(new CompileError(CompileError.errors.PARSEERROR,
						new String("Parsing failed while parsing powerlookup ruleset: " + e.toString())));
				continue;		// Try the next powerlookup.
		    }
		    
											// Compile generated, parsed source.
			compileParseTree(tree, ctx);
			tree = null;
		}
	}

	/**
	 * inputStreamFromStringBuffer turn a StringBuffer into an InputStream
	 * @param sb StringBuffer to convert
	 * @return ByteArrayInputStream input stream
	 */
	protected ByteArrayInputStream inputStreamFromStringBuffer(StringBuffer sb){
	    try {
			return new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
	    } catch (UnsupportedEncodingException e) {
	    	return null;
	    }
	}

	/**
	 * compileParseTree Compile a parse tree
	 * @param tree object to parse
	 * @param ctx CompilerContext
	 */
	protected void compileParseTree(ASTCompilationUnit tree, IProgramContext ctx) {
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


}
