package runtime.compiler;

import runtime.main.Log;
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

public class DataVisitor implements GdlParserVisitor {

	String tabify(int cnt, String text){
		StringBuffer buf = new StringBuffer("");
		
		for(int i = 0; i < cnt; i++){
			buf.append("  ");
		}
		
		buf.append(text);
		
		return buf.toString();
	}
	
	public Object visit(SimpleNode node, Object data) {
		// Dump node info
		Log.info("SimpleNode");
		return data;
	}

	public Object visit(ASTCompilationUnit node, Object data) {
		// Dump node info
		Log.info("ASTCompilationUnit");
		return data;
	}

	public Object visit(ASTVarDef node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTAlias node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTAliasType node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTAliasOf node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTImport node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTInclude node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTGuidelineDef node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTRulesetDef node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTRulesetRef node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTRuleDef node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTRuleRef node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTCondition node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTExpression node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTLogicalCompute node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTLogicalOperator node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTEqualityCompute node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTEqualityOperator node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTBrace node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTConstant node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTMathExpression node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTMathCompute node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTPlusOperator node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTMinusOperator node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTMultOperator node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTDivOperator node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTRaiseToOperator node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTMathConstant node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTIfActions node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTElseActions node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTAssign node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTAssignTo node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTAssignValue node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTMessage node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTExptn node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTExptnType node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTMessageType node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTVarRef node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTVarCast node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTVariableType node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTAndOperator node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	public Object visit(ASTOrOperator node, Object data) {
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

}
