package runtime.visitors;

import java.util.HashMap;

import runtime.compiler.CompilerContext;
import runtime.compiler.IncludeContext;
import runtime.main.CompileError;
import runtime.main.Log;
import runtime.main.ParseMgr;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.ASTInclude;
import runtime.parser.GdlParser;
import runtime.parser.ParseException;

public class IncludeVisitor extends DepthFirstVisitor {
	static HashMap<String,String> includeFilesParsed = new HashMap<String, String>();
	
	static void 	trackIncludeFile(String incFile){
		// FIXME: should include files be tracked in the context?
		IncludeVisitor.includeFilesParsed.put(incFile, "");
	}
	static boolean	isFileTracked(String incFile){
		return (null != IncludeVisitor.includeFilesParsed.get(incFile)? true : false);
	}
	
	public static void		resetTracker(){
		IncludeVisitor.includeFilesParsed.clear();
	}
	
	public Object visit(ASTInclude node, Object data) {
		// Handle an include node
		IncludeContext ctx = (IncludeContext)data;

		String filePath = node.getData("filename");
		
		String incFilePath = ctx.getMaster().findIncludePath(filePath);
		
		if(incFilePath.length() < 1){
			ctx.getMaster().addError(new CompileError(CompileError.errors.INCLUDEDIRPATH,
														new String("File not found on include path [ " + filePath + " ].")));
			return ctx;						// File not found. Exit.
		}
		
		if(isFileTracked(incFilePath)){		// We've already looked at this file.
			return ctx;						// We are done.
		}else{
			trackIncludeFile(incFilePath);
		}

		ParseMgr	incParser = new ParseMgr();
		incParser.configureDefaultPlugins();
		
    	Log.status("Including file: " + incFilePath + ".");
    	incParser.execute(incFilePath,(CompilerContext)ctx.getMaster());
    	ASTCompilationUnit includeUnit = incParser.getParseTree();
    	
    	if(null != includeUnit){
	    	includeUnit.jjtSetParent(node);		// Set this include node to be the parent.
	    	node.jjtAddChild(includeUnit, 0); 	// Add the included nodes to the tree.
	
		    node.childrenAccept(this, ctx);
    	}
	    return ctx;
	}


}
