package runtime.elements;

import runtime.compiler.CompilerContext;
import runtime.main.CompileError;
import runtime.parser.ASTCondType;
import runtime.parser.ASTConditionMsgDef;
import runtime.parser.ASTPriorTo;
import runtime.parser.GdlParserTreeConstants;
import runtime.parser.SimpleNode;

public class ConditionMsg {
	CompilerContext 	ctx				= null;
	String				identifier		= new String();
	String				name			= new String();
	int					id				= 99999;
	String				priorTo			= new String();
	String				category		= new String();
	String				imageDocType	= new String();
	String				visibility		= new String();
	StringBuffer		msg				= new StringBuffer();

	ASTConditionMsgDef	node			= null;
	
	public ConditionMsg(CompilerContext ctx, String identifier){ 
		this.ctx = ctx;
		this.identifier = identifier; 
	}
	
	// Return the GDLC identifier of the condition.
	public String getIdentifier() {
		return identifier;
	}

	// Return the name/alias of the condition.
	public String getName() {
		return name;
	}

	// Alias method to return the name/alias of the condition.
	public String getAlias() {
		return getName();
	}

	public int	getId() { return this.id; }
	
	public void	setId( int id ) { this.id = id; }
//	this.id = id; //Integer.toString(id);
	
	public void initialize(ASTConditionMsgDef node){
		this.node = node;
		initializeArgs(node);
	}
	
	public void initializeArgs(ASTConditionMsgDef node){
		for(int i = 0; i < node.jjtGetNumChildren(); i++){
			SimpleNode child = (SimpleNode)node.jjtGetChild(i);
			int nodeId = child.id();
			if(nodeId == GdlParserTreeConstants.JJTCONDTYPE){
				parseConditionCategory((ASTCondType)child);
				continue;
			}
			if(nodeId == GdlParserTreeConstants.JJTPRIORTO){
				parseConditionPriorTo((ASTPriorTo)child);
				continue;
			}
			
			this.ctx.addError(new CompileError(CompileError.errors.UNEXPECTEDARG,
					new String("["+this.identifier+"] An unexpected argument has been encountered while parsing a condition definition.")));
		}
		
		this.name = new String(node.getData("Name"));
		this.imageDocType = new String(node.getData("ImageDocType"));
		this.visibility = new String(node.getData("Visibility"));
		this.msg = new StringBuffer(node.getData("value"));

	}
	
	public String parseConditionCategory(ASTCondType node){
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
		this.category = value;
		return value;
	}
	
	public String parseConditionPriorTo(ASTPriorTo node){
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
		this.priorTo = value;
		return value;
	}
	
	public Object buildXmlRefElement(int order){
		XmlElem me = new XmlElem("Message");

		String[] attribs = {"Type", "Id", "Order"};
		me.setAttributeOrder(attribs);
		
		me.putAttribute("Type", "Condition");
		me.putAttribute("Id", Integer.toString(this.id));
		me.putAttribute("Order", Integer.toString(order));
		
		return me;
	}
	
	public Object buildXmlDefElement(){
		XmlElem me = new XmlElem("Message");

		String[] attribs = {"Type", "Id", "Name", "PriorTo", "Category", "ImageDocType", "Visibility"};
		me.setAttributeOrder(attribs);
		
		me.putAttribute("Type", "Condition");
		me.putAttribute("Id", Integer.toString(this.id));
		me.putAttribute("Name", this.name);
		me.putAttribute("PriorTo", this.priorTo);
		me.putAttribute("Category", this.category);
		me.putAttribute("ImageDocType", this.imageDocType);
		me.putAttribute("Visibility", this.visibility);

		StringBuffer msgText = new StringBuffer();
		StringBuffer msg = new StringBuffer(this.msg);
		
		// AMS outputs condition message with one leading space and 2 trailing spaces (inside CDATA element).
		msgText.append("<![CDATA[ ").append(msg).append("  ]]>");
		me.appendXml(msgText.toString());

		return me;
	}
	

}
