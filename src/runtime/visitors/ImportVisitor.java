package runtime.visitors;

import java.io.FileNotFoundException;
import java.io.IOException;

import runtime.compiler.CompileException;
import runtime.compiler.CompilerContext;
import runtime.main.CompileError;
import runtime.parser.ASTImport;
import runtime.reader.CsvLookupFile;

public class ImportVisitor extends DepthFirstVisitor {

	public Object visit(ASTImport node, Object data) {
		// Handle an import node
		CompilerContext ctx = (CompilerContext)data;
		String importType = node.getData("importType");
		String filepath = node.getData("filename");
		
		if(importType.equals("lookup")){
			importLookups(ctx, filepath);
		}
		else if(importType.equals("powerlookup")){
			// Skip this:
			//importPowerLookups(ctx, filepath);
		}
		else {
			ctx.addError(new CompileError(CompileError.errors.UNSUPPORTEDOPERATION,
					new String("Import type [" + importType + "] is not currently supported.")));
		}
		
		return ctx;
	}

	protected void importLookups(CompilerContext ctx, String filename){
		CsvLookupFile lk = new CsvLookupFile();
		
		String filepath = ctx.findIncludePath(filename);

		try{

			
			lk.parse(filepath);
			lk.extractLookupsTo(ctx.getLookupData());
		
		
		}
		catch(FileNotFoundException e){
			ctx.addError(new CompileError(CompileError.errors.IMPORTERROR,
					new String("An error occurred while importing [ " + filename + " ].")));
			ctx.addError(new CompileError(CompileError.errors.FILENOTFOUND,
					new String("File [" + filepath + "] not found.")));
			if(filepath.length() < 1){
				ctx.addError(new CompileError(CompileError.errors.INCLUDEDIRPATH,
						new String("[ " + filename + " ] was not found in any INCLUDE dir.")));
			}
			return;
		}
		catch(IOException e){
			ctx.addError(new CompileError(CompileError.errors.UNKNOWN,
					new String("There was an problem reading the lookup file [" + filepath + "].")));
			return;
		}
		catch(CompileException e){
			ctx.addError(new CompileError(CompileError.errors.IMPORTERROR,
					new String("[" + filepath +"] Errors have occured while importing lookups: " + e.getMessage())));
		}
		
//		try{
//			lk.extractLookupsTo(ctx.getLookupData());
//		}
//		catch(CompileException e){
//			ctx.addError(new CompileError(CompileError.errors.IMPORTERROR,
//					new String("[" + filepath +"] Errors have occured while importing lookups: " + e.getMessage())));
//		}
		// lk.dumpData();
		
	}
	

}
