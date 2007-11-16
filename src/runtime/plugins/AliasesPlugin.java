/**
 * 
 */
package runtime.plugins;

import runtime.compiler.IProgramContext;
import runtime.parser.ASTCompilationUnit;
import runtime.visitors.AliasVisitor;

/**
 * @author killer
 *
 */
public class AliasesPlugin implements IGdlcPlugin {

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#getName()
	 */
	public String getName(){ return new String("Aliases"); }

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#execute(runtime.compiler.IProgramContext, runtime.parser.ASTCompilationUnit)
	 */
	public void execute(IProgramContext ctx, ASTCompilationUnit rootNode) {
		AliasVisitor visitor = new AliasVisitor();
		rootNode.childrenAccept(visitor, ctx);
	}

}
