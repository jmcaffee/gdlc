/**
 * 
 */
package runtime.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.PatternSyntaxException;

import runtime.main.CompileError;
import runtime.parser.ASTFunctionDef;
import runtime.parser.ASTFunctionDefArg;
import runtime.parser.ASTFunctionDefArgList;
import runtime.parser.GdlParserTreeConstants;
import runtime.parser.Node;
import runtime.parser.SimpleNode;

/**
 * @author killer
 *
 */
public class FunctionMgr implements IFunctionContext {
	HashMap<String,Function>	functions 	= new HashMap<String,Function>();

	public class Function {
		ArrayList<String>	args 		= new ArrayList<String>();
		String				name		= new String();
		ASTFunctionDef		node		= null;
		StringBuffer		codeBlock	=	new StringBuffer();
		
		public Function(String name){ this.name = name; }
		
		public void initialize(ASTFunctionDef node){
			this.node = node;
			this.codeBlock.append(node.getData("codeBlock"));
			initializeArgs(node);
		}
		
		public void initializeArgs(ASTFunctionDef node){
			SimpleNode sn = (SimpleNode)node.jjtGetChild(0);
			int nodeId = sn.id();
			if(nodeId != GdlParserTreeConstants.JJTFUNCTIONDEFARGLIST){
				return;
			}
			ASTFunctionDefArgList argList = (ASTFunctionDefArgList)(node.jjtGetChild(0));
			ASTFunctionDefArg arg = null;
			
			for(int i = 0; i < argList.jjtGetNumChildren(); i++){
				arg = (ASTFunctionDefArg)argList.jjtGetChild(i);
				if(null == arg) break;
				
				this.args.add(arg.getData("value"));
			}
		}
		
		public String resolveFunction(CompilerContext ctx, ArrayList<String> inputArgs){
			String output = new String();
			String arg = new String();
			
			// Check to see that the number of args match.
			if(args.size() != inputArgs.size()){
				ctx.addError(new CompileError(CompileError.errors.UNEXPECTEDARG,
								new String("["+this.name+"] An unexpected argument has been encountered while referencing a function.")));
			}
			
			output = this.codeBlock.toString();
			
			try{
				for(int i = 0; i < this.args.size(); i++){
					arg = "\\" + this.args.get(i);		// Args start with $. Escape it for the replacement. $ is a RegEx special char.
					output = output.replaceAll(arg, inputArgs.get(i));
				}
				
			} 
			catch(PatternSyntaxException e){
				ctx.addError(new CompileError(CompileError.errors.UNEXPECTEDARG,
						new String("["+this.name+"] An unexpected argument has been encountered while referencing a function."),
						e.getDescription()));
				output = new String("");
			}

			output.trim();
			return output;
		}
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IFunctionContext#addFunction(java.lang.String, runtime.parser.ASTFunctionDef)
	 */
	public void addFunction(String name, ASTFunctionDef node){
		Function func = new Function(name);
		func.initialize(node);
		functions.put(name, func);
		
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IFunctionContext#getFunction(java.lang.String)
	 */
	public Function getFunction(String key){return functions.get(key);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IFunctionContext#containsFunction(java.lang.String)
	 */
	public boolean containsFunction(String key) { return functions.containsKey(key);}
	

}
