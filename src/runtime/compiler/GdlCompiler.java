/**
 * 
 */
package runtime.compiler;

import runtime.main.Log;
import runtime.parser.*;

/**
 * @author killer
 *
 */
public class GdlCompiler {
	ASTCompilationUnit parseTree;

	public GdlCompiler( ASTCompilationUnit parseTree ) {
		this.parseTree = parseTree;
	}
	
	public ASTCompilationUnit compile(CompilerContext ctx) throws CompileException {
		Log.info("Root node has " + parseTree.jjtGetNumChildren() + " children.");

		compileIncludes(ctx);
		compileVariables(ctx);
		compileRuleDefs(ctx);
		compileRulesetDefs(ctx);
		
		resolveRefs(ctx);
		
//		IncludeVisitor visitor = new IncludeVisitor();
//		IncludeContext incCtx = new IncludeContext();
//		
//		parseTree.childrenAccept(visitor, incCtx);
//		parseTree.jjtAccept(visitor, ctx);
//		for(int i = 0; i < parseTree.jjtGetNumChildren(); i++){
//			System.out.println(((SimpleNode)parseTree.jjtGetChild(i)).packData());
//	
//		}

//		throw new CompileException("Compiler not implemented.");
		
		return null;
	}
	
	void compileIncludes(CompilerContext ctx) {
		IncludeVisitor visitor = new IncludeVisitor();
		IncludeContext incCtx = new IncludeContext();
		
		parseTree.childrenAccept(visitor, incCtx);
		
		ASTCompilationUnit cu;
		GdlCompiler incCompiler;
		
		while(!incCtx.isEmpty()){
			cu = incCtx.removeInclude();
			
			incCompiler = new GdlCompiler(cu);
			try {
				incCompiler.compile(ctx);
			} catch (CompileException e) {
				Log.error("GDLC:  Encountered errors during compilation.");
				Log.error(e.toString());
				return;
			}
		}
	}

	void compileVariables(CompilerContext ctx) {
		VariableVisitor visitor = new VariableVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
		
	}

	void compileRuleDefs(CompilerContext ctx) {
		RuleVisitor visitor = new RuleVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
		
	}

	void compileRulesetDefs(CompilerContext ctx) {
		RulesetVisitor visitor = new RulesetVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
		
	}

	void resolveRefs(CompilerContext ctx){
		ResolveRefsVisitor visitor = new ResolveRefsVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
	}

}
