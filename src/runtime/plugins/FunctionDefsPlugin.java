/**
 * 
 */
package runtime.plugins;

import runtime.compiler.IProgramContext;
import runtime.parser.ASTCompilationUnit;
import runtime.visitors.FunctionDefVisitor;
import runtime.visitors.ResolveFunctionRefsVisitor;

/**
 * @author killer
 *
 */
public class FunctionDefsPlugin implements IGdlcPlugin {

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#getName()
	 */
	public String getName(){ return new String("FunctionDefs"); }

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#execute(runtime.compiler.IProgramContext, runtime.parser.ASTCompilationUnit)
	 */
	public void execute(IProgramContext ctx, ASTCompilationUnit rootNode) {
		FunctionDefVisitor defVisitor = new FunctionDefVisitor();
		rootNode.childrenAccept(defVisitor, ctx);

		ResolveFunctionRefsVisitor refVisitor = new ResolveFunctionRefsVisitor();
		rootNode.childrenAccept(refVisitor, ctx);

	}

}
