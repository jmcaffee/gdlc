package runtime.visitors;

import java.util.HashMap;

import runtime.compiler.CompilerContext;
import runtime.parser.ASTLookupDef;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTRuleRef;
import runtime.parser.ASTRulesetDef;
import runtime.parser.ASTRulesetRef;

public class CollectReferencedLookupsVisitor extends DepthFirstVisitor {

	public HashMap<String, String>	lookups = new HashMap<String, String>();
	CompilerContext				ctx		= null;
	
	public CollectReferencedLookupsVisitor(CompilerContext ctx){this.ctx = ctx;}
	
	/**
	 * visit LookupDef node - store referenced lookups in a list.
	 */
	public Object visit(ASTLookupDef node, Object data) {
		lookups.put(node.getName(), node.getName());
		return data;
	}
	
	/* (non-Javadoc)
	 * @see runtime.parser.DepthFirstVisitor#visit(runtime.parser.ASTRuleRef, java.lang.Object)
	 */
	public Object visit(ASTRuleRef node, Object data) {
											// Retreive actual rule definition
		ASTRuleDef rule = ctx.getRule(node.getName());	
		
		data = rule.childrenAccept(this, data);
		return data;
	}


	/* (non-Javadoc)
	 * @see runtime.parser.DepthFirstVisitor#visit(runtime.parser.ASTRulesetRef, java.lang.Object)
	 */
	public Object visit(ASTRulesetRef node, Object data) {
											// Retreive actual ruleset definition
		ASTRulesetDef ruleset = ctx.getRuleset(node.getName()).getNode();	
		
		data = ruleset.childrenAccept(this, data);
		return data;
	}



}
