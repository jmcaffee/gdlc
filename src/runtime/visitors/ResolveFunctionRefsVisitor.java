package runtime.visitors;

import java.util.ArrayList;

import runtime.compiler.CompilerContext;
import runtime.compiler.FunctionMgr.Function;
import runtime.main.CompileError;
import runtime.main.ParseMgr;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.ASTFunctionRef;
import runtime.parser.ASTFunctionRefArg;
import runtime.parser.ASTFunctionRefArgList;
import runtime.parser.GdlParserTreeConstants;
import runtime.parser.Node;
import runtime.parser.SimpleNode;

public class ResolveFunctionRefsVisitor extends DepthFirstVisitor {

	public Object visit(ASTFunctionRef node, Object data){
		CompilerContext ctx = (CompilerContext)super.visit(node, data);
		String name = (String)node.data.get("Name");
		
		if(!ctx.containsFunction(name)) {
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
					new String("Function [" + name + "] definition missing.")));
			return ctx;
		}
		
		ArrayList<String> argList = new ArrayList<String>();
		getArgs(node, argList);
		
		Function func = ctx.getFunction(name);
		StringBuffer codeBlock = new StringBuffer(func.resolveFunction(ctx, argList));
		
		ParseMgr parser = new ParseMgr();
		parser.configureDefaultPlugins();
		ASTCompilationUnit funcCode = parser.parseBuffer(codeBlock, ctx);
		
		if(null != funcCode){
			while(node.jjtGetNumChildren() > 0){
				node.jjtRemoveChild(0);
			}
			
			funcCode.jjtSetParent(node);
			node.jjtAddChild(funcCode, 0);
		}
		
		return ctx;
	}

	private void getArgs(ASTFunctionRef node, ArrayList<String> argList){
		if(node.jjtGetNumChildren() < 1){
			return;
		}
		
//		SimpleNode sn = (SimpleNode)node.jjtGetChild(0);
//		int nodeId = sn.id();
//		if(nodeId != GdlParserTreeConstants.JJTFUNCTIONREFARGLIST){
//			return;
//		}
		ASTFunctionRefArgList refArgList = (ASTFunctionRefArgList)(node.jjtGetChild(0));
		ASTFunctionRefArg arg = null;
		argList.clear();
		
		for(int i = 0; i < refArgList.jjtGetNumChildren(); i++){
			arg = (ASTFunctionRefArg)refArgList.jjtGetChild(i);
			if(null == arg) break;
			
			argList.add(arg.getData("value"));
		}

	}

}
