package runtime.visitors;

import runtime.compiler.IRuleset;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTRuleRef;

public class RuleCollectorVisitor extends DepthFirstVisitor {
	IRuleset ruleset = null;
	
	public RuleCollectorVisitor(IRuleset ruleset){
		this.ruleset = ruleset;
	}
	
	public Object visit(ASTRuleDef node, Object data) {
		// Add the rule name to the ruleset's list.
		this.ruleset.addRuleName(node.getName());
		return data;
	}

	public Object visit(ASTRuleRef node, Object data) {
		// Add the rule name to the ruleset's list.
		this.ruleset.addRuleName(node.getName());
		return data;
	}


}
