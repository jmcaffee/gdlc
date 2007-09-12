package runtime.visitors;

import runtime.compiler.IProgramContext;
import runtime.compiler.VarDpm;
import runtime.compiler.VarPpm;
import runtime.main.CompileError;
import runtime.parser.ASTRuleRef;
import runtime.parser.ASTRulesetRef;
import runtime.parser.ASTVarRef;

public class ResolveRefsVisitor extends DepthFirstVisitor {

	public Object visit(ASTRulesetRef node, Object data) {
		IProgramContext ctx = (IProgramContext)super.visit(node, data);
		String name = (String)node.data.get("Name");
		
		if(!ctx.containsRuleset(name)) {
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
					new String("Ruleset [" + name + "] definition missing.")));
		}
		
		return ctx;
	}

	public Object visit(ASTRuleRef node, Object data) {
		IProgramContext ctx = (IProgramContext)super.visit(node, data);
		String name = (String)node.data.get("Name");
		
		if(!ctx.containsRule(name)) {
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
					new String("Rule [" + name + "] definition missing.")));
		}
		
		return ctx;
	}

	public Object visit(ASTVarRef node, Object data) {
		IProgramContext ctx = (IProgramContext)super.visit(node, data);
		String name = (String)node.data.get("Name");
		
		if(!ctx.containsVar(new VarDpm(name)) &&
			!ctx.containsVar(new VarPpm(name))) {
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
					new String("Variable [" + name + "] definition missing.")));
		}
		
		return ctx;
	}


}
