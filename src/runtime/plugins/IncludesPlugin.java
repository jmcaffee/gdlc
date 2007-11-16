/**
 * 
 */
package runtime.plugins;

import runtime.compiler.IProgramContext;
import runtime.compiler.IncludeContext;
import runtime.main.Log;
import runtime.parser.ASTCompilationUnit;
import runtime.visitors.IncludeVisitor;

/**
 * @author killer
 *
 */
public class IncludesPlugin implements IGdlcPlugin {

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#getName()
	 */
	public String getName(){ return new String("Includes"); }

	/* (non-Javadoc)
	 * @see runtime.plugins.IGdlcPlugin#execute(runtime.compiler.IProgramContext)
	 */
	public void execute(IProgramContext ctx, ASTCompilationUnit rootNode) {
		Log.status("Plugin starting [Includes]");
		IncludeVisitor.resetTracker();
		IncludeVisitor visitor = new IncludeVisitor();
		IncludeContext incCtx = new IncludeContext(ctx);
//		ASTCompilationUnit	parseTree = ctx.getRootNode();
		
//		parseTree.childrenAccept(visitor, incCtx);
		rootNode.childrenAccept(visitor, incCtx);
		
//		ASTCompilationUnit cu;
//		GdlCompiler incCompiler;
//		
//		while(!incCtx.isEmpty()){
//			cu = incCtx.removeInclude();
//			
//			incCompiler = new GdlCompiler(cu);
//			try {
//				incCompiler.compile(ctx);
//			} catch (CompileException e) {
//				Log.error("*** Errors encountered during compilation. ");
//				Log.error(e.toString());
//				return;
//			}
//		}
		Log.status("Plugin terminating [Includes]");
	}

}
