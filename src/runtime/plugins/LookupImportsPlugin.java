/**
 * 
 */
package runtime.plugins;

import runtime.compiler.IProgramContext;
import runtime.parser.ASTCompilationUnit;
import runtime.visitors.ImportVisitor;

/**
 * @author killer
 *
 */
public class LookupImportsPlugin implements IGdlcPlugin {

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#getName()
	 */
	public String getName(){ return new String("LookupImports"); }

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#execute(runtime.compiler.IProgramContext, runtime.parser.ASTCompilationUnit)
	 */
	public void execute(IProgramContext ctx, ASTCompilationUnit rootNode) {
		ImportVisitor visitor = new ImportVisitor();
		rootNode.childrenAccept(visitor, ctx);
	}

}
