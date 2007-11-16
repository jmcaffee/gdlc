/**
 * 
 */
package runtime.plugins;

import runtime.compiler.IProgramContext;
import runtime.main.Log;
import runtime.parser.ASTCompilationUnit;
import runtime.visitors.ComputeTreeVisitor;

/**
 * @author killer
 *
 */
public class ComputePlugin implements IGdlcPlugin {

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#execute(runtime.compiler.IProgramContext, runtime.parser.ASTCompilationUnit)
	 */
	public void execute(IProgramContext ctx, ASTCompilationUnit rootNode) {
		Log.status("Plugin starting [" + this.getName() +"]");
		ComputeTreeVisitor visitor = new ComputeTreeVisitor();

		rootNode.childrenAccept(visitor, ctx);
		
		Log.status("Plugin terminating [" + this.getName() +"]");
	}

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#getName()
	 */
	public String getName() {
		
		return new String("Compute");
	}

}
