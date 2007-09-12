/**
 * 
 */
package runtime.compiler;

import runtime.parser.ASTRuleDef;

/**
 * @author killer
 *
 */
public class RuleContext implements IContext {
	ASTRuleDef rule = null;
	
	public RuleContext(ASTRuleDef rule){
		this.rule = rule;
	}
}
