package runtime.visitors;

import runtime.compiler.IProgramContext;
import runtime.parser.ASTConditionMsgDef;
import runtime.parser.ASTConditionMsgRef;

public class ConditionMsgVisitor extends DepthFirstVisitor {

	public Object visit(ASTConditionMsgDef node, Object data) {
		// Handle Rule definition nodes
		IProgramContext ctx = (IProgramContext)data;
		String identifier 	= node.getData("Identifier");

		ctx.addCondition(identifier, node);
		return ctx;
	}
	
	public Object visit(ASTConditionMsgRef node, Object data) {
		// Handle condition msg reference nodes
		return null;
	}

}
