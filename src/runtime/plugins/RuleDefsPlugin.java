/**
 * 
 */
package runtime.plugins;

import runtime.compiler.IProgramContext;
import runtime.parser.ASTCompilationUnit;
import runtime.visitors.RuleVisitor;

/**
 * @author killer
 *
 */
public class RuleDefsPlugin implements IGdlcPlugin {

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#getName()
	 */
	public String getName(){ return new String("RuleDefs"); }

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#execute(runtime.compiler.IProgramContext, runtime.parser.ASTCompilationUnit)
	 */
	public void execute(IProgramContext ctx, ASTCompilationUnit rootNode) {
		RuleVisitor visitor = new RuleVisitor();
		rootNode.childrenAccept(visitor, ctx);
	}

}
