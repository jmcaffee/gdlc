package runtime.visitors;

import java.util.HashMap;

import runtime.compiler.CompilerContext;
import runtime.parser.ASTConditionMsgRef;
import runtime.parser.ASTConditionMsgDef;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTRuleRef;
import runtime.parser.ASTRulesetDef;
import runtime.parser.ASTRulesetRef;

public class CollectReferencedConditionsVisitor extends DepthFirstVisitor {

	public HashMap<String, String>	conditions = new HashMap<String, String>();
	CompilerContext				ctx		= null;
	
	Boolean					insideRule = false;
	
	public CollectReferencedConditionsVisitor(CompilerContext ctx){this.ctx = ctx;}
	
	/**
	 * visit ConditionMsgRef node - store referenced conditions in a list.
	 */
	public Object visit(ASTConditionMsgRef node, Object data) {
		String condName = node.getData("Identifier");
											
		if(this.ctx.containsCondition(condName)){
			conditions.put(condName, condName);
		} 
		return data;
		
	}
	
	/**
	 * visit ConditionMsgDef node - store referenced conditions in a list.
	 */
	public Object visit(ASTConditionMsgDef node, Object data) {
		String condName = node.getData("Identifier");
		
		// Because conditions can be defined within rules,
		// we want to collect condDefs only if the def is within a rule.
		
//		if(insideRule && this.ctx.containsCondition(condName)){
		if(this.ctx.containsCondition(condName)){
			conditions.put(condName, condName);
		} 
		return data;
		
	}
	
	/* (non-Javadoc)
	 * @see runtime.parser.DepthFirstVisitor#visit(runtime.parser.ASTRuleRef, java.lang.Object)
	 */
	public Object visit(ASTRuleRef node, Object data) {
											// Retrieve actual rule definition
		ASTRuleDef rule = ctx.getRule(node.getName());	
		
		insideRule = true;					// Flag that we are inside a rule.
											// Because conditions can be defined within rules,
											// we want to collect condDefs as well as condRefs,
											// but only if the def is within a rule.
											// Since refs can only happen within a rule, collect
											// all refs.
		data = rule.childrenAccept(this, data);
		
		insideRule = false;
		return data;
	}


	/* (non-Javadoc)
	 * @see runtime.parser.DepthFirstVisitor#visit(runtime.parser.ASTRulesetRef, java.lang.Object)
	 */
	public Object visit(ASTRulesetRef node, Object data) {
											// Retrieve actual ruleset definition
		ASTRulesetDef ruleset = ctx.getRuleset(node.getName()).getNode();	
		
		data = ruleset.childrenAccept(this, data);
		return data;
	}



}
