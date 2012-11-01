package runtime.visitors;

import runtime.compiler.IProgramContext;
import runtime.compiler.XmlFunctionMgr.XmlFunction;
import runtime.main.CompileError;
import runtime.main.CompileWarning;
import runtime.main.Log;
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
		int hash = 0;
		
		// We need to generate a hash of the function signature to see if it is
		// the same definition, just included multiple times. If this IS the case, don't
		// throw a warning.
		// In short: Only throw warning when the signature is actually being changed.
		
		// Step 1: if the context already has a function with this ID, store the hashCode.
		if(ctx.containsXmlFunction(id)){
			hash = ctx.getXmlFunction(id).hashCode();
		}
		
		// Step 2: go ahead and add the function.
		ctx.addXmlFunction(id, node);
		
		// Step 3: if the hash is not 0, we know there was an existing func def,
		// so grab the updated func's hashCode and compare them.
		if(hash != 0 && hash != ctx.getXmlFunction(id).hashCode()){
			ctx.addWarning(new CompileWarning(CompileWarning.warnings.REDEFINITION_XMLFUNC,
					new String("XmlFunction [" + id + "] has been redefined.")));
			//Log.warning("Context already contains xmlfunc: " + id + ". Redef warning generated.");
		}
		
		return ctx;
	}

	public Object visit(ASTXmlFuncRef node, Object data) {
		// See if we are supposed to be parsing Refs at this time.
		if( !this.parseRefs ){ return data; }
		
		// Handle XML Function definition nodes
		IProgramContext ctx = (IProgramContext)data;
		String id 		= node.getData("Identifier");
		
		if(!ctx.containsXmlFunction(id)){
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING_XMLFUNC,
					new String("XmlFunction [" + id + "] has not been defined.")));
			
			//Log.error("Xmlfunc definition missing from context: " + id + ". Def missing error generated.");
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
			Log.error("Xmlfunc ref is missing argument: " + id + ". Missing Arg error generated.");
		}
		
		if(this.argCount > defArgCount){
			ctx.addError(new CompileError(CompileError.errors.UNEXPECTEDARG,
						new String("XmlFunction reference [" + id + "] has too many arguments. Expecting " + Integer.toString(defArgCount) + ", found " + Integer.toString(this.argCount) + ".")));
			Log.error("Xmlfunc ref has extra arguments: " + id + ". Unexpected Arg error generated.");		
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
