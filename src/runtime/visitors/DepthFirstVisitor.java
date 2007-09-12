package runtime.visitors;

import runtime.parser.ASTAlias;
import runtime.parser.ASTAndOperator;
import runtime.parser.ASTAssign;
import runtime.parser.ASTAssignTo;
import runtime.parser.ASTAssignValue;
import runtime.parser.ASTBrace;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.ASTCondType;
import runtime.parser.ASTCondition;
import runtime.parser.ASTConditionMsg;
import runtime.parser.ASTConstant;
import runtime.parser.ASTDivOperator;
import runtime.parser.ASTElseActions;
import runtime.parser.ASTEqualityCompute;
import runtime.parser.ASTEqualityOperator;
import runtime.parser.ASTExpression;
import runtime.parser.ASTExptn;
import runtime.parser.ASTExptnType;
import runtime.parser.ASTFuncRefParam;
import runtime.parser.ASTFunctionRef;
import runtime.parser.ASTGuidelineDef;
import runtime.parser.ASTIfActions;
import runtime.parser.ASTImport;
import runtime.parser.ASTInclude;
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
import runtime.parser.GdlParserVisitor;
import runtime.parser.SimpleNode;

/**
 * 
 */

/**
 * @author killer
 *
 */
public class DepthFirstVisitor implements GdlParserVisitor {

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.SimpleNode, java.lang.Object)
	 */
	public Object visit(SimpleNode node, Object data) {
		data = node.childrenAccept(this, data);
		
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTCompilationUnit, java.lang.Object)
	 */
	public Object visit(ASTCompilationUnit node, Object data) {
		data = node.childrenAccept(this, data);
		
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTVarDef, java.lang.Object)
	 */
	public Object visit(ASTVarDef node, Object data) {
		data = node.childrenAccept(this, data);
		
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTAlias, java.lang.Object)
	 */
	public Object visit(ASTAlias node, Object data) {
		data = node.childrenAccept(this, data);
		
		return data;
	}


	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTImport, java.lang.Object)
	 */
	public Object visit(ASTImport node, Object data) {
		data = node.childrenAccept(this, data);
		
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTInclude, java.lang.Object)
	 */
	public Object visit(ASTInclude node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTGuidelineDef, java.lang.Object)
	 */
	public Object visit(ASTGuidelineDef node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTRulesetDef, java.lang.Object)
	 */
	public Object visit(ASTRulesetDef node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTRulesetRef, java.lang.Object)
	 */
	public Object visit(ASTRulesetRef node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTRuleDef, java.lang.Object)
	 */
	public Object visit(ASTRuleDef node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTRuleRef, java.lang.Object)
	 */
	public Object visit(ASTRuleRef node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTCondition, java.lang.Object)
	 */
	public Object visit(ASTCondition node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTExpression, java.lang.Object)
	 */
	public Object visit(ASTExpression node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTLogicalCompute, java.lang.Object)
	 */
	public Object visit(ASTLogicalCompute node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTLogicalOperator, java.lang.Object)
	 */
	public Object visit(ASTLogicalOperator node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTLookupDef, java.lang.Object)
	 */
	public Object visit(ASTLookupDef node, Object data){
		data = node.childrenAccept(this, data);
		return data;
	}
	
	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTEqualityCompute, java.lang.Object)
	 */
	public Object visit(ASTEqualityCompute node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTEqualityOperator, java.lang.Object)
	 */
	public Object visit(ASTEqualityOperator node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTBrace, java.lang.Object)
	 */
	public Object visit(ASTBrace node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTConstant, java.lang.Object)
	 */
	public Object visit(ASTConstant node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTMathExpression, java.lang.Object)
	 */
	public Object visit(ASTMathExpression node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTMathCompute, java.lang.Object)
	 */
	public Object visit(ASTMathCompute node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTPlusOperator, java.lang.Object)
	 */
	public Object visit(ASTPlusOperator node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTMinusOperator, java.lang.Object)
	 */
	public Object visit(ASTMinusOperator node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTMultOperator, java.lang.Object)
	 */
	public Object visit(ASTMultOperator node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTDivOperator, java.lang.Object)
	 */
	public Object visit(ASTDivOperator node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTRaiseToOperator, java.lang.Object)
	 */
	public Object visit(ASTRaiseToOperator node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTMathConstant, java.lang.Object)
	 */
	public Object visit(ASTMathConstant node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTIfActions, java.lang.Object)
	 */
	public Object visit(ASTIfActions node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTElseActions, java.lang.Object)
	 */
	public Object visit(ASTElseActions node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTAssign, java.lang.Object)
	 */
	public Object visit(ASTAssign node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTAssignTo, java.lang.Object)
	 */
	public Object visit(ASTAssignTo node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTAssignValue, java.lang.Object)
	 */
	public Object visit(ASTAssignValue node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTMessage, java.lang.Object)
	 */
	public Object visit(ASTMessage node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTExptn, java.lang.Object)
	 */
	public Object visit(ASTExptn node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTExptnType, java.lang.Object)
	 */
	public Object visit(ASTExptnType node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTMessageType, java.lang.Object)
	 */
	public Object visit(ASTMessageType node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTVarRef, java.lang.Object)
	 */
	public Object visit(ASTVarRef node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTVarCast, java.lang.Object)
	 */
	public Object visit(ASTVarCast node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTVariableType, java.lang.Object)
	 */
	public Object visit(ASTVariableType node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTAndOperator, java.lang.Object)
	 */
	public Object visit(ASTAndOperator node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTOrOperator, java.lang.Object)
	 */
	public Object visit(ASTOrOperator node, Object data) {
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTConditionMsg, java.lang.Object)
	 */
	public Object visit(ASTConditionMsg node, Object data){
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTCondType, java.lang.Object)
	 */
	public Object visit(ASTCondType node, Object data){
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTPriorTo, java.lang.Object)
	 */
	public Object visit(ASTPriorTo node, Object data){
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTFunctionRef, java.lang.Object)
	 */
	public Object visit(ASTFunctionRef node, Object data){
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTFuncRefParam, java.lang.Object)
	 */
	public Object visit(ASTFuncRefParam node, Object data){
		data = node.childrenAccept(this, data);
		return data;
	}

}
