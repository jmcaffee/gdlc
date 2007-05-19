package runtime.compiler;

import runtime.nodes.NodeBuilder;
import runtime.nodes.Rule;
import runtime.parser.ASTAlias;
import runtime.parser.ASTAliasOf;
import runtime.parser.ASTAliasType;
import runtime.parser.ASTAndOperator;
import runtime.parser.ASTAssign;
import runtime.parser.ASTAssignTo;
import runtime.parser.ASTAssignValue;
import runtime.parser.ASTBrace;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.ASTCondition;
import runtime.parser.ASTConstant;
import runtime.parser.ASTDivOperator;
import runtime.parser.ASTElseActions;
import runtime.parser.ASTEqualityCompute;
import runtime.parser.ASTEqualityOperator;
import runtime.parser.ASTExpression;
import runtime.parser.ASTExptn;
import runtime.parser.ASTExptnType;
import runtime.parser.ASTGuidelineDef;
import runtime.parser.ASTIfActions;
import runtime.parser.ASTImport;
import runtime.parser.ASTInclude;
import runtime.parser.ASTLogicalCompute;
import runtime.parser.ASTLogicalOperator;
import runtime.parser.ASTMathCompute;
import runtime.parser.ASTMathConstant;
import runtime.parser.ASTMathExpression;
import runtime.parser.ASTMessage;
import runtime.parser.ASTMessageType;
import runtime.parser.ASTMinusOperator;
import runtime.parser.ASTMultOperator;
import runtime.parser.ASTOrOperator;
import runtime.parser.ASTPlusOperator;
import runtime.parser.ASTRaiseToOperator;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTRuleRef;
import runtime.parser.ASTRulesetDef;
import runtime.parser.ASTRulesetRef;
import runtime.parser.ASTVarCast;
import runtime.parser.ASTVarDef;
import runtime.parser.ASTVarRef;
import runtime.parser.ASTVariableType;
import runtime.parser.GdlParserVisitor;
import runtime.parser.SimpleNode;

public class RuleVisitor implements GdlParserVisitor {

	public Object visit(SimpleNode node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTCompilationUnit node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTVarDef node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTAlias node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTAliasType node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTAliasOf node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTImport node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTInclude node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTGuidelineDef node, Object data) {
		// Be sure to parse through all rules within guidelines
		node.childrenAccept(this, data);
		return null;
	}

	public Object visit(ASTRulesetDef node, Object data) {
		// Be sure to parse through all rulesets
		node.childrenAccept(this, data);
		return null;
	}

	public Object visit(ASTRulesetRef node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTRuleDef node, Object data) {
		// Handle Rule definition nodes
		CompilerContext ctx = (CompilerContext)data;
		String name 		= node.getData("Name");
		//Rule rule = NodeBuilder.buildRule(node);
		ctx.addRule(name, node);
		return null;
	}
	
	public Object visit(ASTRuleRef node, Object data) {
		// Handle rule reference nodes
		return null;
	}

	public Object visit(ASTCondition node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTExpression node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTLogicalCompute node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTLogicalOperator node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTEqualityCompute node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTEqualityOperator node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTBrace node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTConstant node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTMathExpression node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTMathCompute node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTPlusOperator node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTMinusOperator node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTMultOperator node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTDivOperator node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTRaiseToOperator node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTMathConstant node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTIfActions node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTElseActions node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTAssign node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTAssignTo node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTAssignValue node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTMessage node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTExptn node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTExptnType node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTMessageType node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTVarRef node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTVarCast node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTVariableType node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTAndOperator node, Object data) {
		// Not used
		return null;
	}

	public Object visit(ASTOrOperator node, Object data) {
		// Not used
		return null;
	}

}
