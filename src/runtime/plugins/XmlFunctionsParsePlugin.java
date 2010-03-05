/**
 * 
 */
package runtime.plugins;

import runtime.compiler.IProgramContext;
import runtime.parser.ASTCompilationUnit;
import runtime.visitors.XmlFunctionVisitor;

/**
 * @author killer
 *
 */
public class XmlFunctionsParsePlugin implements IGdlcPlugin {

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#getName()
	 */
	public String getName(){ return new String("XmlFunctionDefs"); }

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#execute(runtime.compiler.IProgramContext, runtime.parser.ASTCompilationUnit)
	 */
	public void execute(IProgramContext ctx, ASTCompilationUnit rootNode) {
		XmlFunctionVisitor visitor = new XmlFunctionVisitor();
		
		// Parse all XmlFuncDef nodes to collect definitions.
		rootNode.childrenAccept(visitor, ctx);
		
		// Parse all XmlFuncRef nodes and validate arg counts.
		visitor.parseDefs(false);
		visitor.parseRefs(true);
		rootNode.childrenAccept(visitor, ctx);

	}

}
