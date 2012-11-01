/**
 * 
 */
package runtime.compiler;

import java.util.HashMap;

import runtime.main.CompileError;
import runtime.parser.ASTXmlFuncDef;
import runtime.parser.ASTXmlFuncDefArg;
import runtime.parser.ASTXmlFuncDefArgList;
import runtime.parser.GdlParserTreeConstants;
import runtime.parser.Node;
import runtime.parser.SimpleNode;

/**
 * @author killer
 *
 */
public class XmlFunctionMgr implements IXmlFunctionContext {
	HashMap<String,XmlFunction>	functions 	= new HashMap<String,XmlFunction>();

	public class XmlFunction {
		String				identifier	= new String();
		ASTXmlFuncDef		node		= null;
		int					argCount	= 0;
		
		public XmlFunction(String identifier){ this.identifier = identifier; }
		
		public void initialize(ASTXmlFuncDef node){
			this.node = node;
			initializeArgs(node);
		}
		
		public int getArgCount() { return this.argCount; }
		
		public void initializeArgs(ASTXmlFuncDef node){
			SimpleNode sn = (SimpleNode)node.jjtGetChild(0);
			int nodeId = sn.id();
			if(nodeId != GdlParserTreeConstants.JJTXMLFUNCDEFARGLIST){
				return;
			}
			ASTXmlFuncDefArgList argList = (ASTXmlFuncDefArgList)(node.jjtGetChild(0));
			ASTXmlFuncDefArg arg = null;
			
			// For XmlFunctions, we just want a count of the args.
			// We'll use the count to verify that XmlFuncRefs are called correctly.
			for(int i = 0; i < argList.jjtGetNumChildren(); i++){
				arg = (ASTXmlFuncDefArg)argList.jjtGetChild(i);
				if(null == arg) break;
				
				this.argCount++;
			}
		}
		
		public int hashCode(){
			int code = 37;
			code += this.identifier.hashCode();
			
			ASTXmlFuncDefArgList argList = (ASTXmlFuncDefArgList)(node.jjtGetChild(0));
			ASTXmlFuncDefArg arg = null;
			
			// For XmlFunctions, we just want a count of the args.
			// We'll use the count to verify that XmlFuncRefs are called correctly.
			for(int i = 0; i < argList.jjtGetNumChildren(); i++){
				arg = (ASTXmlFuncDefArg)argList.jjtGetChild(i);
				if(null == arg) break;
				
				code += arg.toString().hashCode();
			}
			
			return code;
		}
	}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IXmlFunctionContext#addXmlFunction(java.lang.String, runtime.parser.ASTXmlFuncDef)
	 */
	public void addXmlFunction(String identifier, ASTXmlFuncDef node){
		XmlFunction func = new XmlFunction(identifier);
		func.initialize(node);
		functions.put(identifier, func);
		
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IFunctionContext#getXmlFunction(java.lang.String)
	 */
	public XmlFunction getXmlFunction(String key){return functions.get(key);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IFunctionContext#containsXmlFunction(java.lang.String)
	 */
	public boolean containsXmlFunction(String key) { return functions.containsKey(key);}
	

}
