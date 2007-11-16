/**
 * 
 */
package runtime.plugins;

import runtime.compiler.IProgramContext;
import runtime.parser.ASTCompilationUnit;

/**
 * @author killer
 *
 */
public interface IGdlcPlugin {

	public abstract String getName();

	public abstract void execute(IProgramContext ctx, ASTCompilationUnit rootNode);
}
