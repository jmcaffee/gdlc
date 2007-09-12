package runtime.visitors;

import runtime.compiler.IProgramContext;
import runtime.compiler.VarDpm;
import runtime.compiler.VarPpm;
import runtime.parser.ASTVarDef;

public class VariableVisitor extends DepthFirstVisitor {


	public Object visit(ASTVarDef node, Object data) {
		IProgramContext ctx = (IProgramContext)data;
		String varType		= node.getData("varType");
		
		if(varType.equals("DPM")) {
			ctx.addVar(new VarDpm(node));
		} else if(varType.equals("PPM")) {
			ctx.addVar(new VarPpm(node));
		}

		return ctx;
	}


}
