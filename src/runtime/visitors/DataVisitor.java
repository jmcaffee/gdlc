package runtime.visitors;

import runtime.compiler.CompilerContext;
import runtime.main.Log;
import runtime.parser.ASTAlias;
import runtime.parser.ASTAndOperator;
import runtime.parser.ASTAssign;
import runtime.parser.ASTAssignTo;
import runtime.parser.ASTAssignValue;
import runtime.parser.ASTBrace;
import runtime.parser.ASTCodeBlock;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.ASTCondType;
import runtime.parser.ASTCondition;
import runtime.parser.ASTConditionMsgDef;
import runtime.parser.ASTConditionMsgRef;
import runtime.parser.ASTConstant;
import runtime.parser.ASTDivOperator;
import runtime.parser.ASTElseActions;
import runtime.parser.ASTEqualityCompute;
import runtime.parser.ASTEqualityOperator;
import runtime.parser.ASTExpression;
import runtime.parser.ASTFunctionDef;
import runtime.parser.ASTFunctionDefArg;
import runtime.parser.ASTFunctionDefArgList;
import runtime.parser.ASTFunctionRef;
import runtime.parser.ASTFunctionRefArg;
import runtime.parser.ASTFunctionRefArgList;
import runtime.parser.ASTGuidelineDef;
import runtime.parser.ASTIfActions;
import runtime.parser.ASTImport;
import runtime.parser.ASTInclude;
import runtime.parser.ASTInsertPricing;
import runtime.parser.ASTLogicalCompute;
import runtime.parser.ASTLogicalOperator;
import runtime.parser.ASTLookupDef;
import runtime.parser.ASTMathCompute;
import runtime.parser.ASTMathConstant;
import runtime.parser.ASTMathExpression;
import runtime.parser.ASTMessage;
import runtime.parser.ASTMessageType;
import runtime.parser.ASTMinusOperator;
import runtime.parser.ASTMultOperator;
import runtime.parser.ASTOrOperator;
import runtime.parser.ASTPlusOperator;
import runtime.parser.ASTPriorTo;
import runtime.parser.ASTRaiseToOperator;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTRuleRef;
import runtime.parser.ASTRulesetDef;
import runtime.parser.ASTRulesetRef;
import runtime.parser.ASTVarCast;
import runtime.parser.ASTVarDef;
import runtime.parser.ASTVarRef;
import runtime.parser.ASTVariableType;
import runtime.parser.ASTXmlFuncDef;
import runtime.parser.ASTXmlFuncDefArg;
import runtime.parser.ASTXmlFuncDefArgList;
import runtime.parser.ASTXmlFuncRef;
import runtime.parser.ASTXmlFuncRefArg;
import runtime.parser.ASTXmlFuncRefArgList;
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
	
	public Object dumpNodeInfo(SimpleNode node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
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

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTLookupDef, java.lang.Object)
	 */
	public Object visit(ASTLookupDef node, Object data){
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

//	public Object visit(ASTLogicalBrace node, Object data) {
//		// Dump node info
//		CompilerContext ctx = (CompilerContext)data;
//		int cnt = ctx.getTabCount();
//
//		Log.info(tabify(cnt,node.toString()));
//		
//		ctx.setTabCount(cnt+1);
//		node.childrenAccept(this, data);
//		ctx.setTabCount(cnt);
//		return ctx;
//	}

//	public Object visit(ASTEqualityBrace node, Object data) {
//		// Dump node info
//		CompilerContext ctx = (CompilerContext)data;
//		int cnt = ctx.getTabCount();
//
//		Log.info(tabify(cnt,node.toString()));
//		
//		ctx.setTabCount(cnt+1);
//		node.childrenAccept(this, data);
//		ctx.setTabCount(cnt);
//		return ctx;
//	}

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

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTConditionMsgDef, java.lang.Object)
	 */
	public Object visit(ASTConditionMsgDef node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTConditionMsgRef, java.lang.Object)
	 */
	public Object visit(ASTConditionMsgRef node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTCondType, java.lang.Object)
	 */
	public Object visit(ASTCondType node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTPriorTo, java.lang.Object)
	 */
	public Object visit(ASTPriorTo node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTFunctionRef, java.lang.Object)
	 */
	public Object visit(ASTFunctionRef node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTFuncRefParam, java.lang.Object)
	 */
	public Object visit(ASTFunctionRefArg node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTInsertPricing, java.lang.Object)
	 */
	public Object visit(ASTInsertPricing node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTCodeBlock, java.lang.Object)
	 */
	public Object visit(ASTCodeBlock node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTFunctionDef, java.lang.Object)
	 */
	public Object visit(ASTFunctionDef node, Object data){
		return dumpNodeInfo(node, data);
	}
	
	
	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTFunctionDefArgList, java.lang.Object)
	 */
	public Object visit(ASTFunctionDefArgList node, Object data){
		return dumpNodeInfo(node, data);
	}
	
	
	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTFunctionDefArg, java.lang.Object)
	 */
	public Object visit(ASTFunctionDefArg node, Object data){
		return dumpNodeInfo(node, data);
	}


	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTFunctionRefArgList, java.lang.Object)
	 */
	public Object visit(ASTFunctionRefArgList node, Object data){
		return dumpNodeInfo(node, data);
	}


	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTXmlFuncDef, java.lang.Object)
	 */
	public Object visit(ASTXmlFuncDef node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}
	
	
	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTXmlFuncDefArgList, java.lang.Object)
	 */
	public Object visit(ASTXmlFuncDefArgList node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}
	
	
	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTXmlFuncDefArg, java.lang.Object)
	 */
	public Object visit(ASTXmlFuncDefArg node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTXmlFuncRef, java.lang.Object)
	 */
	public Object visit(ASTXmlFuncRef node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTXmlFuncRefArgList, java.lang.Object)
	 */
	public Object visit(ASTXmlFuncRefArgList node, Object data){
		// Dump node info
		CompilerContext ctx = (CompilerContext)data;
		int cnt = ctx.getTabCount();

		Log.info(tabify(cnt,node.toString()));
		
		ctx.setTabCount(cnt+1);
		node.childrenAccept(this, data);
		ctx.setTabCount(cnt);
		return ctx;
	}


	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTXmlFuncRefArg, java.lang.Object)
	 */
	public Object visit(ASTXmlFuncRefArg node, Object data){
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
