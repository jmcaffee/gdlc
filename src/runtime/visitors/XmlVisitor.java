package runtime.visitors;

import java.text.SimpleDateFormat;
import java.util.Date;

import runtime.compiler.IProgramContext;
import runtime.compiler.IRuleset;
import runtime.compiler.VarDpm;
import runtime.compiler.VarPpm;
import runtime.elements.XmlElem;
import runtime.main.Log;
import runtime.parser.ASTAndOperator;
import runtime.parser.ASTAssign;
import runtime.parser.ASTAssignTo;
import runtime.parser.ASTAssignValue;
import runtime.parser.ASTBrace;
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
import runtime.parser.ASTGuidelineDef;
import runtime.parser.ASTIfActions;
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
import runtime.parser.ASTVarRef;
import runtime.parser.SimpleNode;

public class XmlVisitor extends DepthFirstVisitor {
	IProgramContext ctx = null;
	boolean 		parseMsgs = false;		// Set this to true if Message element XML should
											// be returned as well.
	
	public XmlVisitor(IProgramContext ctx){ this.ctx = ctx;}
	
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
		
		me.putAttribute("Id", "99999");
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

		me.putAttribute("ID", "99999");
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
		
		msgText.append("<![CDATA[").append(msg).append("]]>");
		me.appendXml(msgText.toString());

		elem.appendXml(me.toXml());
		return elem;
	}

	public Object visit(ASTExptn node, Object data) {
		XmlElem elem = (XmlElem)data;
		String value = new String("Exceptions");
									// Set attributes for the parent Message element.
		elem.putAttribute("Type", value);
		return elem;
	}

	public Object visit(ASTExptnType node, Object data) {
		XmlElem elem = (XmlElem)data;
		String value = node.getData("value");
		
		if(value.equalsIgnoreCase("exception")){
			value = new String("Exceptions");
		}
		else if(value.equalsIgnoreCase("assets")) {
			value = new String("Assets");
		}
									// Set attributes for the parent Message element.
		elem.putAttribute("ExceptionType", value);
		return elem;
	}

	public Object visit(ASTMessageType node, Object data) {
		XmlElem elem = (XmlElem)data;
		String value = node.getData("value");
		
		if(value.equalsIgnoreCase("findings")) {
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

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTConditionMsg, java.lang.Object)
	 */
	public Object visit(ASTConditionMsg node, Object data){
		if(false == this.parseMsgs){return data;}	// Skip if message parsing is not on.

		XmlElem elem = (XmlElem)data;
		XmlElem me = new XmlElem("Message");

		String[] attribs = {"Type", "Id", "PriorTo", "Category", "Responsible", "WhoCanClear", "Critical", "Visibility"};
		me.setAttributeOrder(attribs);
		
		me.putAttribute("Type", "Condition");
		me.putAttribute("Id", "99999");
		me.putAttribute("Responsible", "Broker");
		me.putAttribute("WhoCanClear", "Underwriter");
		me.putAttribute("Critical", "No");
		me.putAttribute("Visibility", "External");

		node.childrenAccept(this, me);

		StringBuffer msgText = new StringBuffer();
		StringBuffer msg = new StringBuffer(node.getData("value"));
		
		msgText.append("<![CDATA[").append(msg).append("]]>");
		me.appendXml(msgText.toString());

		elem.appendXml(me.toXml());
		return elem;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTCondType, java.lang.Object)
	 */
	public Object visit(ASTCondType node, Object data){
		XmlElem elem = (XmlElem)data;
		String value = node.getData("value");
		
		if(value.equalsIgnoreCase("asset")) {
			value = new String("1");
		}
		else if(value.equalsIgnoreCase("credit")) {
			value = new String("2");
		}
		else if(value.equalsIgnoreCase("income")) {
			value = new String("3");
		}
		else if(value.equalsIgnoreCase("property")) {
			value = new String("4");
		}
		else if(value.equalsIgnoreCase("purchase")) {
			value = new String("5");
		}
		else if(value.equalsIgnoreCase("title")) {
			value = new String("6");
		}
									// Set attributes for the parent Message element.
		elem.putAttribute("Category", value);
		return elem;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTPriorTo, java.lang.Object)
	 */
	public Object visit(ASTPriorTo node, Object data){
		XmlElem elem = (XmlElem)data;
		String value = node.getData("value");
		
		if(value.equalsIgnoreCase("docs")) {
			value = new String("1");
		}
		else if(value.equalsIgnoreCase("funding")) {
			value = new String("2");
		}
		else if(value.equalsIgnoreCase("approval")) {
			value = new String("3");
		}
									// Set attributes for the parent Message element.
		elem.putAttribute("PriorTo", value);
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

			me.putAttribute("Name", dpm.getAlias());
			me.putAttribute("Type", dpm.getType());
			me.putAttribute("Order", dpm.getOrder());
			me.putAttribute("ProductType", dpm.getProductType());
			
			String[] attOrder = {"Name", "Type", "Order", "ProductType"};
			me.setAttributeOrder(attOrder);
		} 
		else {
			VarPpm ppm = (VarPpm)this.ctx.getVar(new VarPpm(varName));
			me = new XmlElem(ppm.getVarType());
			me.setIsShortTag(true);

			me.putAttribute("Name", ppm.getAlias());
			me.putAttribute("Type", ppm.getType());
			
			String[] attOrder = {"Name", "Type"};
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

}
