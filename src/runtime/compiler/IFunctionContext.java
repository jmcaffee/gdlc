package runtime.compiler;

import runtime.compiler.FunctionMgr.Function;
import runtime.parser.ASTFunctionDef;

public interface IFunctionContext {

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addFunction(java.lang.String, runtime.parser.ASTFunctionDef)
	 */
	public abstract void addFunction(String key, ASTFunctionDef rule);

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getFunction(java.lang.String)
	 */
	public abstract Function getFunction(String key);

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsFunction(java.lang.String)
	 */
	public abstract boolean containsFunction(String key);

}