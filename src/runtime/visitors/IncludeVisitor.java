package runtime.visitors;

import runtime.compiler.IncludeContext;
import runtime.main.CompileError;
import runtime.main.Log;
import runtime.parser.ASTInclude;
import runtime.parser.GdlParser;
import runtime.parser.ParseException;

public class IncludeVisitor extends DepthFirstVisitor {

	public Object visit(ASTInclude node, Object data) {
		// Handle an include node
		IncludeContext ctx = (IncludeContext)data;

		String filePath = node.getData("filename");
		filePath = filePath.replace("\"", "");		// Remove quotes
		GdlParser incParser;
		
		String incFilePath = ctx.getMaster().findIncludePath(filePath);
        try {
        	Log.status("GDLC:  Including file: " + incFilePath + ".");
        	incParser = new GdlParser(new java.io.FileInputStream(incFilePath));
	    } catch (java.io.FileNotFoundException e) {
	        ctx.getMaster().addError(new CompileError(CompileError.errors.FILENOTFOUND, "[" + incFilePath + "] " + e.toString()));
			return null;
	    }

	    try {
	    	ctx.addInclude(incParser.CompilationUnit());
	    } catch (ParseException e) {
		      Log.error("GDLC:  Encountered errors during parsing of include.");
		      ctx.getMaster().addError(new CompileError(CompileError.errors.PARSEERROR, "[" + incFilePath + "] " + e.toString()));
		      return null;
		}
	    
	    return null;
	}


}
