package runtime.visitors;

import runtime.compiler.IProgramContext;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTRuleRef;

public class RuleVisitor extends DepthFirstVisitor {

	public Object visit(ASTRuleDef node, Object data) {
		// Handle Rule definition nodes
		IProgramContext ctx = (IProgramContext)data;
		String name 		= node.getName();

		ctx.addRule(name, node);
		return ctx;
	}
	
	public Object visit(ASTRuleRef node, Object data) {
		// Handle rule reference nodes
		return null;
	}

}
