/**
 * 
 */
package runtime.compiler;

import java.util.ArrayList;

import runtime.main.Log;
import runtime.parser.ASTRulesetDef;

/**
 * @author killer
 *
 */
public class Ruleset implements IRuleset {
	ArrayList<String> 	rules 		= new ArrayList<String>();
	
	String 	id			= new String("99999");
	String	name 		= new String("");
	String 	executeType	= new String("");
	String	type		= new String("");
	
	ASTRulesetDef	rsNode	=	null;
	
	public Ruleset(ASTRulesetDef node){
		this.id 			= node.getData("Id");
		this.name 			= node.getName();
		this.executeType	= node.getData("ExecuteType");
		this.type			= node.getData("Type");
		
		this.rsNode = node;		// Store the node for reference purposes.
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#addRuleName(java.lang.String)
	 */
	public void addRuleName(String ruleName){
		this.rules.add(ruleName);
	}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#getRuleNames()
	 */
	public ArrayList<String> getRuleNames(){
		return this.rules;
	}
	
	public void dumpRules(){
		for(int i = 0; i < this.rules.size(); i++){
			Log.info("> " + this.rules.get(i));
		}
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#getExecuteType()
	 */
	public String getExecuteType() {
		return this.executeType;
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#setExecuteType(java.lang.String)
	 */
	public void setExecuteType(String executeType) {
		this.executeType = executeType;
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#getId()
	 */
	public String getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#getName()
	 */
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#getAlias()
	 */
	public String getAlias() {
		return this.rsNode.getAlias();
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#getType()
	 */
	public String getType() {
		return this.type;
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#setType(java.lang.String)
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IRuleset#getNode()
	 */
	public ASTRulesetDef getNode(){
		return this.rsNode;
	}

}
