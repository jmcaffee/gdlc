package runtime.visitors;

import java.text.SimpleDateFormat;
import java.util.Date;

import runtime.compiler.CompilerContext;
import runtime.compiler.IRuleset;
import runtime.compiler.VarDpm;
import runtime.compiler.VarPpm;
import runtime.elements.ConditionMsg;
import runtime.elements.XmlElem;
import runtime.main.Log;
import runtime.parser.ASTAndOperator;
import runtime.parser.ASTAssign;
import runtime.parser.ASTAssignTo;
import runtime.parser.ASTAssignValue;
import runtime.parser.ASTBrace;
import runtime.parser.ASTCondition;
import runtime.parser.ASTConditionMsgDef;
import runtime.parser.ASTConditionMsgRef;
import runtime.parser.ASTConstant;
import runtime.parser.ASTDivOperator;
import runtime.parser.ASTElseActions;
import runtime.parser.ASTEqualityCompute;
import runtime.parser.ASTEqualityOperator;
import runtime.parser.ASTExpression;
import runtime.parser.ASTGuidelineDef;
import runtime.parser.ASTIfActions;
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
import runtime.parser.ASTRaiseToOperator;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTRuleRef;
import runtime.parser.ASTRulesetDef;
import runtime.parser.ASTRulesetRef;
import runtime.parser.ASTVarRef;
import runtime.parser.ASTXmlFuncRef;
import runtime.parser.ASTXmlFuncRefArg;
import runtime.parser.ASTXmlFuncRefArgList;
import runtime.parser.SimpleNode;

public class XmlVisitor extends DepthFirstVisitor {
	CompilerContext ctx 			= null;
	int				orderIndex	= 1;
	boolean 		parseMsgs 		= false;// Set this to true if Message element XML should
											// be returned as well.
	
	public XmlVisitor(CompilerContext ctx){ this.ctx = ctx;}
	
			int	getNextOrderIndex(){ int cnt = this.orderIndex; this.orderIndex += 1; return cnt; }
			void resetOrderIndex() { this.orderIndex = 1; }
			
	public void parseMessages(boolean parseMsgs){ this.parseMsgs = parseMsgs;}
	
	public Object visit(ASTGuidelineDef node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Guideline");
		me.putAttribute("GuidelineID", "1");
		me.putAttribute("Name", node.getName());
		me.putAttribute("Version", "1");
		
		SimpleDateFormat dtFormat = new SimpleDateFormat("MMM dd yyyy");
		me.putAttribute("StartDate", dtFormat.format(new Date()) + " 12:00AM");
		
		String[] attOrder = {"GuidelineID", "Name", "Version", "StartDate"};
		me.setAttributeOrder(attOrder);

		node.childrenAccept(this, me);

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTRulesetDef node, Object data) {
		XmlElem elem = (XmlElem)data;

		IRuleset rs = null;
		XmlElem me = null;
		String setName = node.getName();
		
		if(this.ctx.containsRuleset(setName)){
			rs = this.ctx.getRuleset(setName);
			me = new XmlElem("Ruleset");

			me.putAttribute("Id", rs.getId());
			me.putAttribute("Name", rs.getAlias());
			me.putAttribute("ExecuteType", rs.getExecuteType());
			me.putAttribute("Type", rs.getType());
			
			String[] attOrder = {"Id", "Name", "ExecuteType", "Type"};
			me.setAttributeOrder(attOrder);
			
			ASTRuleDef rule = null;
			for(String name:rs.getRuleNames()){
				rule = this.ctx.getRule(name);
				rule.jjtAccept(this, me);
			}
		} 
		
		if(null == me){
			Log.error("Missing ruleset.");
			return elem;
		}
		
		elem.appendXml(me.toXml());

		return elem;

		
	}

	public Object visit(ASTRulesetRef node, Object data) {
		// Attempt to retrieve the ruleset def node and parse it.
		ASTRulesetDef actualNode = (this.ctx.getRuleset(node.getName())).getNode();

		if(null != actualNode){
			return this.visit(actualNode, data);
		}
		
		return data;
	}

	public Object visit(ASTRuleDef node, Object data) {
		XmlElem elem = (XmlElem)data;
		String[] attribs = {"Id", "Name"};
		XmlElem me = new XmlElem("Rule");
		me.setAttributeOrder(attribs);
		
		me.putAttribute("Id", node.getData("Id"));
		me.putAttribute("Name", node.getAlias());
		
		XmlElem ifMsgs = new XmlElem("IfMessages");
		XmlElem elseMsgs = new XmlElem("ElseMessages");
		
		ifMsgs.setIsShortTag(true);
		elseMsgs.setIsShortTag(true);
		
		MsgCollectorVisitor msgColl = new MsgCollectorVisitor();
		XmlVisitor msgXmlVisit = new XmlVisitor(this.ctx);
		msgXmlVisit.parseMessages(true);	// Turn on message parsing.
											// Collect all of the messages in the IfActions.
		node.jjtGetChild(1).jjtAccept(msgColl, null);
											// Build the msgs XML.
		for(SimpleNode msgNode: msgColl.getMsgs()){
			msgNode.jjtAccept(msgXmlVisit, ifMsgs);
		}
		
		msgColl.reset();
		if(node.jjtGetNumChildren() > 2){
			node.jjtGetChild(2).jjtAccept(msgColl, null);

			msgXmlVisit.resetOrderIndex();	// Reset the order counter (used to set order attrib).
			for(SimpleNode msgNode: msgColl.getMsgs()){
				msgNode.jjtAccept(msgXmlVisit, elseMsgs);
			}
		}
		
		
		me.appendXml(ifMsgs.toXml());
		me.appendXml(elseMsgs.toXml());
		
		node.childrenAccept(this, me);
		
		elem.appendXml(me.toXml());
		
		return elem;
	}

	public Object visit(ASTRuleRef node, Object data) {
			// Attempt to retrieve the rule def node and parse it.
		ASTRuleDef actualNode = this.ctx.getRule(node.getName());

		if(null != actualNode){
			return this.visit(actualNode, data);
		}
		
		return data;
	}

	public Object visit(ASTCondition node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Condition");
		
		node.childrenAccept(this, me);

		elem.appendXml(me.toXml());
		
		return elem;
	}

	public Object visit(ASTExpression node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Expression");
		
		node.childrenAccept(this, me);

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTLogicalCompute node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Compute");
		XmlElem lhs = new XmlElem("LeftOperand");
		XmlElem rhs = new XmlElem("RightOperand");
		
//		node.childrenAccept(this, me);
		node.getLhs().jjtAccept(this, lhs);
		node.getRhs().jjtAccept(this, rhs);
		node.getOperator().jjtAccept(this, me);
		me.appendXml(lhs.toXml());
		me.appendXml(rhs.toXml());
		
		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTMathCompute node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Compute");
		XmlElem lhs = new XmlElem("LeftOperand");
		XmlElem rhs = new XmlElem("RightOperand");
		
//		node.childrenAccept(this, me);
		node.getLhs().jjtAccept(this, lhs);
		node.getRhs().jjtAccept(this, rhs);
		node.getOperator().jjtAccept(this, me);
		me.appendXml(lhs.toXml());
		me.appendXml(rhs.toXml());
		
		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTLogicalOperator node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Operator");

		me.appendXml(node.getData("value"));

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTEqualityCompute node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Compute");
		XmlElem lhs = new XmlElem("LeftOperand");
		XmlElem rhs = new XmlElem("RightOperand");
		
//		node.childrenAccept(this, me);
		node.getLhs().jjtAccept(this, lhs);
		node.getRhs().jjtAccept(this, rhs);
		node.getOperator().jjtAccept(this, me);
		me.appendXml(lhs.toXml());
		me.appendXml(rhs.toXml());
		
		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTEqualityOperator node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Operator");

		me.appendXml(node.getData("value"));

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTBrace node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Brace");

		node.childrenAccept(this, me);

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTConstant node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Constant");

		me.appendXml(node.getData("value"));

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTMathExpression node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Expression");

		node.childrenAccept(this, me);

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTPlusOperator node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Operator");

		me.appendXml(node.getData("value"));

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTMinusOperator node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Operator");

		me.appendXml(node.getData("value"));

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTMultOperator node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Operator");

		me.appendXml(node.getData("value"));

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTDivOperator node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Operator");

		me.appendXml(node.getData("value"));

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTRaiseToOperator node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Operator");

		me.appendXml(node.getData("value"));

		elem.appendXml(me.toXml());
		return elem;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.DepthFirstVisitor#visit(runtime.parser.ASTLookupDef, java.lang.Object)
	 */
	public Object visit(ASTLookupDef node, Object data){
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Lookup");
		me.setIsShortTag(true);
		
		String[] attribs = {"ID", "Name"};
		me.setAttributeOrder(attribs);

		me.putAttribute("ID", node.getData("Id"));
		me.putAttribute("Name", node.getAlias());
		
		elem.appendXml(me.toXml());
		return elem;
	}
	
	public Object visit(ASTMathConstant node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Constant");

		me.appendXml(node.getData("value"));

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTIfActions node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("IfActions");

		node.childrenAccept(this, me);

		if(me.getContent().length() > 0){	// There should be no IfAction element if there are
			elem.appendXml(me.toXml());		// no child nodes.
		}
		return elem;
	}

	public Object visit(ASTElseActions node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("ElseActions");

		node.childrenAccept(this, me);

		if(me.getContent().length() > 0){	// There should be no ElseAction element if there are
			elem.appendXml(me.toXml());		// no child nodes.
		}
		return elem;
	}

	public Object visit(ASTAssign node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Assign");

		node.childrenAccept(this, me);

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTAssignTo node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("AssignTo");

		node.childrenAccept(this, me);

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTAssignValue node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("AssignValue");

		node.childrenAccept(this, me);

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTMessage node, Object data) {
		if(false == this.parseMsgs){return data;}	// Skip if message parsing is not on.

		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Message");

		String[] attribs = {"Type", "ExceptionType"};
		me.setAttributeOrder(attribs);

		node.childrenAccept(this, me);

		StringBuffer msgText = new StringBuffer();
		StringBuffer msg = new StringBuffer(node.getData("value"));
		// TODO: Parse message and replace var names (<DPM></DPM>) with aliases.
		
		msgText.append("<![CDATA[").append(msg).append("]]>");
		me.appendXml(msgText.toString());

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTMessageType node, Object data) {
		XmlElem elem = (XmlElem)data;
		String value = node.getData("value");
		
		if(value.equalsIgnoreCase("exception")) {
			value = new String("Exceptions");
		}
		else if(value.equalsIgnoreCase("findings")) {
			value = new String("Findings");
		}
		else if(value.equalsIgnoreCase("observation")) {
			value = new String("Observation");
		}
		else if(value.equalsIgnoreCase("credit")) {
			value = new String("Credit");
		}
									// Set attributes for the parent Message element.
		elem.putAttribute("Type", value);
		return elem;
	}

	public Object visit(ASTConditionMsgRef node, Object data) {
		if(false == this.parseMsgs){return data;}	// Skip if message parsing is not on.
		// Attempt to retrieve the condition msg def node and parse it.
		ConditionMsg actualNode = this.ctx.getCondition(node.getData("Identifier"));
	
		XmlElem elem = (XmlElem)data;
		XmlElem me = (XmlElem)actualNode.buildXmlRefElement(this.getNextOrderIndex());
		elem.appendXml(me.toXml());
		return elem;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTConditionMsgDef, java.lang.Object)
	 */
	public Object visit(ASTConditionMsgDef node, Object data){
		if(false == this.parseMsgs){return data;}	// Skip if message parsing is not on.
		// Attempt to retrieve the condition msg def node and parse it.
		ConditionMsg actualNode = this.ctx.getCondition(node.getData("Identifier"));
		
		XmlElem elem = (XmlElem)data;
		XmlElem me = (XmlElem)actualNode.buildXmlRefElement(this.getNextOrderIndex());
		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTVarRef node, Object data) {
		XmlElem elem = (XmlElem)data;

		XmlElem me = null;
		String varName = node.getName();
		
		if(this.ctx.containsVar(new VarDpm(varName))){
			VarDpm dpm = (VarDpm)this.ctx.getVar(new VarDpm(varName));
			me = new XmlElem(dpm.getVarType());
			me.setIsShortTag(true);

			// DPMs only output their DataType in the DERIVEDPARAMETERS element (v2.x).
			// See CompileMgr.java#buildDpm()			
			me.putAttribute("Name", dpm.getAlias());
			
			String[] attOrder = {"Name"};
			me.setAttributeOrder(attOrder);
		} 
		else {
			VarPpm ppm = (VarPpm)this.ctx.getVar(new VarPpm(varName));
			me = new XmlElem(ppm.getVarType());
			me.setIsShortTag(true);

			// PPMs always output their DataType (v2.x).			
			me.putAttribute("Name", ppm.getAlias());
			me.putAttribute("DataType", ppm.getDataType());
			me.putAttribute("Type", ppm.getType());
			
			String[] attOrder = {"Name", "DataType", "Type"};
			me.setAttributeOrder(attOrder);
		}
		
		if(null == me){
			Log.error("Missing variable.");
			return elem;
		}
		
		elem.appendXml(me.toXml());

		return elem;
	}

	public Object visit(ASTAndOperator node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Operator");

		me.appendXml(node.getData("value"));

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTOrOperator node, Object data) {
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Operator");

		me.appendXml(node.getData("value"));

		elem.appendXml(me.toXml());
		return elem;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTInsertPricing, java.lang.Object)
	 */
	public Object visit(ASTInsertPricing node, Object data){
		XmlElem elem = (XmlElem)data;
		String[] attribs = {"Id", "Name"};
		XmlElem me = new XmlElem("Rule");
		me.setAttributeOrder(attribs);
		
		me.putAttribute("Id", "0");
		me.putAttribute("Name", "Insert Pricing Guideline Rule");
		
		me.setIsShortTag(false);
		node.childrenAccept(this, me);
		
		elem.appendXml(me.toXml());
		
		return elem;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTXmlFuncRef, java.lang.Object)
	 */
	public Object visit(ASTXmlFuncRef node, Object data){
		XmlElem elem = (XmlElem)data;

		XmlElem expr = new XmlElem("Expression");
		
		String[] attribs = {"Name"};
		XmlElem me = new XmlElem("Function");
		me.setAttributeOrder(attribs);
		
		me.putAttribute("Name", node.getData("Identifier"));
		
		me.setIsShortTag(false);
		node.childrenAccept(this, me);
		
		expr.appendXml(me.toXml());
		elem.appendXml(expr.toXml());
		
		return elem;
		
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTXmlFuncRefArgList, java.lang.Object)
	 */
	public Object visit(ASTXmlFuncRefArgList node, Object data){
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Args");
		
		me.setIsShortTag(false);
		node.childrenAccept(this, me);
		
		elem.appendXml(me.toXml());
		
		return elem;
		
	}


	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTXmlFuncRefArg, java.lang.Object)
	 */
	public Object visit(ASTXmlFuncRefArg node, Object data){
		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Arg");
		
		me.setIsShortTag(false);
		node.childrenAccept(this, me);
		
		elem.appendXml(me.toXml());
		
		return elem;
		
	}

}
