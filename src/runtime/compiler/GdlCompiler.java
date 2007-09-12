/**
 * 
 */
package runtime.compiler;

import runtime.main.Log;
import runtime.parser.*;
import runtime.visitors.AliasVisitor;
import runtime.visitors.GuidelineVisitor;
import runtime.visitors.ImportPowerLookupVisitor;
import runtime.visitors.ImportVisitor;
import runtime.visitors.IncludeVisitor;
import runtime.visitors.LookupVisitor;
import runtime.visitors.ResolveRefsVisitor;
import runtime.visitors.RuleVisitor;
import runtime.visitors.RulesetVisitor;
import runtime.visitors.VariableVisitor;

/**
 * @author killer
 *
 */
public class GdlCompiler {
	ASTCompilationUnit parseTree;

/**
 * Constructor
 * @param parseTree
 */
	public GdlCompiler( ASTCompilationUnit parseTree ) {
		this.parseTree = parseTree;
	}
	
/**
 * compile
 * @param ctx
 * @return
 * @throws CompileException
 */	
	public ASTCompilationUnit compile(IProgramContext ctx) throws CompileException {
		Log.status("Start compile");
		Log.info("Root node has " + parseTree.jjtGetNumChildren() + " children.");

		compileIncludes(ctx);
		compileVariables(ctx);
		compileLookupImports(ctx);
		compileLookups(ctx);
		compilePowerLookupImports(ctx);
		compileRuleDefs(ctx);
		compileRulesetDefs(ctx);
		
		resolveRefs(ctx);

		compileGuidelines(ctx);
		
		compileAliases((CompilerContext)ctx);

		return null;
	}
	
/**
 * compileIncludes
 * @param ctx
 */
	void compileIncludes(IProgramContext ctx) {
		Log.status("Compiling Includes");
		IncludeVisitor visitor = new IncludeVisitor();
		IncludeContext incCtx = new IncludeContext(ctx);
		
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

/**
 * compileLookupImports
 * @param ctx
 */	
	void compileLookupImports(IProgramContext ctx) {
		Log.status("Compiling Lookup Imports");
		ImportVisitor visitor = new ImportVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
		
	}

/**
 * compilePowerLookupImports
 * @param ctx
 */	
	void compilePowerLookupImports(IProgramContext ctx) {
		Log.status("Compiling PowerLookup Imports");
		ImportPowerLookupVisitor visitor = new ImportPowerLookupVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
		
	}

/**
 * compileVariables
 * @param ctx
 */	
	void compileVariables(IProgramContext ctx) {
		Log.status("Compiling Variables");
		VariableVisitor visitor = new VariableVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
		
	}

/**
 * compileLookups
 * @param ctx
 */	
	void compileLookups(IProgramContext ctx) {
		Log.status("Compiling Lookups");
		LookupVisitor visitor = new LookupVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
		
	}

/**
 * compileRuleDefs
 * @param ctx
 */	
	void compileRuleDefs(IProgramContext ctx) {
		Log.status("Compiling Rule definitions");
		RuleVisitor visitor = new RuleVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
		
	}

/**
 * compileRulesetDefs
 * @param ctx
 */	
	void compileRulesetDefs(IProgramContext ctx) {
		Log.status("Compiling Ruleset definitions");
		RulesetVisitor visitor = new RulesetVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
		
	}

/**
 * resolveRefs
 * @param ctx
 */	
	void resolveRefs(IProgramContext ctx){
		Log.status("Resolving references");
		ResolveRefsVisitor visitor = new ResolveRefsVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
	}

/**
 * compileGuidelines
 * @param ctx
 */	
	void compileGuidelines(IGuidelineContext ctx){
		Log.status("Compiling Guidelines");
		GuidelineVisitor visitor = new GuidelineVisitor();
		
		parseTree.childrenAccept(visitor, ctx);
	}

	/**
	 * compileAliases
	 * @param ctx
	 */	
		void compileAliases(CompilerContext ctx){
			Log.status("Compiling Aliases");
			AliasVisitor visitor = new AliasVisitor();
			
			parseTree.childrenAccept(visitor, ctx);
		}

}
