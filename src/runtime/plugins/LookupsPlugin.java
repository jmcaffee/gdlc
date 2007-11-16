/**
 * 
 */
package runtime.plugins;

import runtime.compiler.IProgramContext;
import runtime.parser.ASTCompilationUnit;
import runtime.visitors.LookupVisitor;

/**
 * @author killer
 *
 */
public class LookupsPlugin implements IGdlcPlugin {

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#getName()
	 */
	public String getName(){ return new String("Lookups"); }

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#execute(runtime.compiler.IProgramContext, runtime.parser.ASTCompilationUnit)
	 */
	public void execute(IProgramContext ctx, ASTCompilationUnit rootNode) {
		LookupVisitor visitor = new LookupVisitor();
		rootNode.childrenAccept(visitor, ctx);
	}

}
