/**
 * 
 */
package runtime.compiler;

import runtime.parser.ASTVarDef;

/**
 * @author killer
 *
 */
public class VarPpm extends VarBase implements IVariable {
	
	public VarPpm(ASTVarDef node){
		super(node);
	}
	
	public VarPpm(String name){
		super(name);
	}
	
}
