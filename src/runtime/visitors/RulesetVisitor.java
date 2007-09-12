package runtime.visitors;

import runtime.compiler.IProgramContext;
import runtime.compiler.IRuleset;
import runtime.compiler.Ruleset;
import runtime.main.CompileError;
import runtime.parser.ASTRulesetDef;
import runtime.parser.GdlParserTreeConstants;
import runtime.parser.SimpleNode;

public class RulesetVisitor extends DepthFirstVisitor {

	public Object visit(ASTRulesetDef node, Object data) {
		// Handle Ruleset definition nodes
		IProgramContext ctx = (IProgramContext)data;
		String name 		= node.getName();
		IRuleset rs = new Ruleset(node);
		RuleCollectorVisitor ruleCollector = new RuleCollectorVisitor(rs);
		
//		resolveRefs(node, ctx);
		
		node.childrenAccept(ruleCollector, null);
		
		ctx.addRuleset(name, rs);
		return ctx;
	}

	void resolveRefs(ASTRulesetDef node, IProgramContext ctx){
		int cnt = node.jjtGetNumChildren();
		String ruleName;
		
		for(int i = 0; i < cnt; i++){
			if(((SimpleNode)(node.jjtGetChild(i))).id() == GdlParserTreeConstants.JJTRULEREF) {
				ruleName = (String)((SimpleNode)node.jjtGetChild(i)).data.get("Name");
				if(!ctx.containsRule(ruleName)){
					ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
							new String("Rule [" + ruleName + "] definition missing. Referenced from ruleset [" + node.getName() + "].")));
				}
			}
		}
	}
}
