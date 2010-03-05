package runtime.compiler;

import runtime.compiler.XmlFunctionMgr.XmlFunction;
import runtime.parser.ASTXmlFuncDef;

public interface IXmlFunctionContext {

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addXmlFunction(java.lang.String, runtime.parser.ASTXmlFuncDef)
	 */
	public abstract void addXmlFunction(String key, ASTXmlFuncDef rule);

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getXmlFunction(java.lang.String)
	 */
	public abstract XmlFunction getXmlFunction(String key);

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsXmlFunction(java.lang.String)
	 */
	public abstract boolean containsXmlFunction(String key);

}