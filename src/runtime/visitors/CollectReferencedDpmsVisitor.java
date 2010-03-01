package runtime.visitors;

import java.util.HashMap;

import runtime.compiler.CompilerContext;
import runtime.compiler.VarDpm;
import runtime.parser.ASTVarRef;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTRuleRef;
import runtime.parser.ASTRulesetDef;
import runtime.parser.ASTRulesetRef;

public class CollectReferencedDpmsVisitor extends DepthFirstVisitor {

	public HashMap<String, String>	dpms = new HashMap<String, String>();
	CompilerContext				ctx		= null;
	
	public CollectReferencedDpmsVisitor(CompilerContext ctx){this.ctx = ctx;}
	
	/**
	 * visit VarRef node - store referenced dpms in a list.
	 */
	public Object visit(ASTVarRef node, Object data) {
		String varName = node.getName();
											// Make sure it is a DPM and not a PPM.
		if(this.ctx.containsVar(new VarDpm(varName))){
			dpms.put(varName, varName);
		} 
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
