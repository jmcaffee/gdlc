package runtime.visitors;

import runtime.compiler.IProgramContext;
import runtime.main.CompileError;
import runtime.main.CompileWarning;
import runtime.parser.ASTFunctionDef;

public class FunctionDefVisitor extends DepthFirstVisitor {

	public Object visit(ASTFunctionDef node, Object data) {
		// Handle Function definition nodes
		IProgramContext ctx = (IProgramContext)data;
		String name 		= node.getName();
		
		if(ctx.containsFunction(name)){
			ctx.addWarning(new CompileError(CompileWarning.warnings.REDEFINITION,
					new String("Function [" + name + "] has been redefined.")));
		}
		
		ctx.addFunction(name, node);
		return ctx;
	}

}
