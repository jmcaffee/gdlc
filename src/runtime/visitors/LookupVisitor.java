package runtime.visitors;

import runtime.compiler.CompilerContext;
import runtime.main.CompileError;
import runtime.parser.ASTLookupDef;

public class LookupVisitor extends DepthFirstVisitor {

	public Object visit(ASTLookupDef node, Object data) {
		// Handle Lookup definition nodes
		CompilerContext ctx = (CompilerContext)data;
		String name 		= node.getName();

											// Throw an error if the lookup hasn't been
											// imported yet.
		if(!ctx.containsLookupData(name)) {
			ctx.addError(new CompileError(CompileError.errors.DATAMISSING,
					new String("Lookup [" + name + "] data is missing. Verify that it is being imported.")));
		}
		else{								// Lookup has been imported, add the definition.
			ctx.addLookup(name, node);
		}
		
		return ctx;
	}
	

}
