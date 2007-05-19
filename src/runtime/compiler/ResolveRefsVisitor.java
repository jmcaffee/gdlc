package runtime.compiler;

import runtime.main.CompileError;
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

public class ResolveRefsVisitor extends DepthFirstVisitor {

	public Object visit(SimpleNode node, Object data) {
		return super.visit(node, data);
	}

	public Object visit(ASTCompilationUnit node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTVarDef node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTAlias node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTAliasType node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTAliasOf node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTImport node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTInclude node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTGuidelineDef node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTRulesetDef node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTRulesetRef node, Object data) {
		CompilerContext ctx = (CompilerContext)super.visit(node, data);
		String name = (String)node.data.get("Name");
		
		if(!ctx.containsRuleset(name)) {
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
					new String("Ruleset [" + name + "] definition missing.")));
		}
		
		return ctx;
	}

	public Object visit(ASTRuleDef node, Object data) {
		return super.visit(node, data);//node.childrenAccept(this, data);
		
	}

	public Object visit(ASTRuleRef node, Object data) {
		CompilerContext ctx = (CompilerContext)super.visit(node, data);
		String name = (String)node.data.get("Name");
		
		if(!ctx.containsRule(name)) {
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
					new String("Rule [" + name + "] definition missing.")));
		}
		
		return ctx;
	}

	public Object visit(ASTCondition node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTExpression node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTLogicalCompute node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTLogicalOperator node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTEqualityCompute node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTEqualityOperator node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTBrace node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTConstant node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTMathExpression node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTMathCompute node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTPlusOperator node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTMinusOperator node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTMultOperator node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTDivOperator node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTRaiseToOperator node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTMathConstant node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTIfActions node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTElseActions node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTAssign node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTAssignTo node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTAssignValue node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTMessage node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTExptn node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTExptnType node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTMessageType node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTVarRef node, Object data) {
		CompilerContext ctx = (CompilerContext)super.visit(node, data);
		String name = (String)node.data.get("Name");
		
		if(!ctx.containsDpmVar(name) &&
			!ctx.containsPpmVar(name)) {
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
					new String("Variable [" + name + "] definition missing.")));
		}
		
		return ctx;
	}

	public Object visit(ASTVarCast node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTVariableType node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTAndOperator node, Object data) {
		return super.visit(node, data);
		
	}

	public Object visit(ASTOrOperator node, Object data) {
		return super.visit(node, data);
		
	}

}
