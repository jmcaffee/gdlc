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
		
		// We use the value as a key to retrieve the ID from the context.
		// The ConditionCategoryConfigPlugin reads the values and IDs from
		// a properties file named category.properties.
		
		int categoryId = ctx.getConditionCategoryId(value);
		
		// An ID of -1 indicates we don't know what it is.
		// Tell the user the situation.
		if(categoryId == -1){
			ctx.addError(new CompileError(CompileError.errors.MISSING_CONFIG_VALUE,
							new String("[Missing Condition Category] An unknown condition category ("+value+") found in condition definition: "+this.identifier+"\n"+
									"    If this is not a typo, be sure to add the category to your 'category.properties' file.")));
		}
		
		// Convert to a string and set attributes for the parent Message element.
		value = Integer.toString(categoryId);
		this.category = value;
		return value;
	}
	
	public String parseConditionPriorTo(ASTPriorTo node){
		String value = node.getData("value");
		
		// We use the value as a key to retrieve the ID from the context.
		// The ConditionPriorToConfigPlugin reads the values and IDs from
		// a properties file named priorto.properties.
		
		int priorToId = ctx.getConditionPriorToId(value);
		
		// An ID of -1 indicates we don't know what it is.
		// Tell the user the situation.
		if(priorToId == -1){
			ctx.addError(new CompileError(CompileError.errors.MISSING_CONFIG_VALUE,
							new String("[Missing Condition PriorTo Type] An unknown condition PriorTo type ("+value+") found in condition definition: "+this.identifier+"\n"+
									"    If this is not a typo, be sure to add the PriorTo type to your 'priorto.properties' file.")));
		}
		
		// Convert to a string and set attributes for the parent Message element.
		value = Integer.toString(priorToId);
		this.priorTo = value;
		return value;
	}
	
	public Object buildXmlRefElement(int order){
		XmlElem me = new XmlElem("Message");
		// Condition reference elements must be short tags.
		me.setIsShortTag(true);

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
