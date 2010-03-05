package runtime.visitors;

import runtime.compiler.IProgramContext;
import runtime.compiler.XmlFunctionMgr.XmlFunction;
import runtime.main.CompileError;
import runtime.main.CompileWarning;
import runtime.parser.ASTXmlFuncDef;
import runtime.parser.ASTXmlFuncRef;
import runtime.parser.ASTXmlFuncRefArg;
import runtime.parser.ASTXmlFuncRefArgList;

public class XmlFunctionVisitor extends DepthFirstVisitor {
	
	boolean		parseDefs	= true;
	boolean		parseRefs	= false;
	int			argCount	= 0;
	
	public void parseDefs(boolean trueOrFalse) { this.parseDefs = trueOrFalse; }
	public void parseRefs(boolean trueOrFalse) { this.parseRefs = trueOrFalse; }
	
	public Object visit(ASTXmlFuncDef node, Object data) {
		// See if we are supposed to be parsing Defs at this time.
		if( !this.parseDefs ){ return data; }
		
		// Handle XML Function definition nodes
		IProgramContext ctx = (IProgramContext)data;
		String id 		= node.getData("Identifier");
		
		if(ctx.containsXmlFunction(id)){
			ctx.addWarning(new CompileError(CompileWarning.warnings.REDEFINITION,
					new String("XmlFunction [" + id + "] has been redefined.")));
		}
		
		ctx.addXmlFunction(id, node);
		return ctx;
	}

	public Object visit(ASTXmlFuncRef node, Object data) {
		// See if we are supposed to be parsing Refs at this time.
		if( !this.parseRefs ){ return data; }
		
		// Handle XML Function definition nodes
		IProgramContext ctx = (IProgramContext)data;
		String id 		= node.getData("Identifier");
		
		if(!ctx.containsXmlFunction(id)){
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
					new String("XmlFunction [" + id + "] has not been defined.")));
			
			return ctx;
		}
		
		XmlFunction def = ctx.getXmlFunction(id);
		this.argCount = 0;					// Reset the arg count before walking children.
		
		node.childrenAccept(this, data);

		int defArgCount = def.getArgCount();
		
		if(this.argCount < defArgCount){
			int countDiff = defArgCount - this.argCount;
			ctx.addError(new CompileError(CompileError.errors.MISSINGARG,
						new String("XmlFunction reference [" + id + "] is missing " + Integer.toString(countDiff) + " argument(s). Expecting " + Integer.toString(defArgCount) + ", found " + Integer.toString(this.argCount) + ".")));
		}
		
		if(this.argCount > defArgCount){
			ctx.addError(new CompileError(CompileError.errors.UNEXPECTEDARG,
						new String("XmlFunction reference [" + id + "] has too many arguments. Expecting " + Integer.toString(defArgCount) + ", found " + Integer.toString(this.argCount) + ".")));
		}
		
		return ctx;
	}

	public Object visit(ASTXmlFuncRefArg node, Object data) {
		// See if we are supposed to be parsing Refs at this time.
		if( !this.parseRefs ){ return data; }
		
		// We're counting arg nodes... increment the counter and leave.
		this.argCount++;
		
		return data;
	}

}
