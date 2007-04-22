/**
 * 
 */
package runtime.compiler;

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
	
	public ASTCompilationUnit compile() throws CompileException {
		System.out.println("Root node has " + parseTree.jjtGetNumChildren() + " children.");
		
		for(int i = 0; i < parseTree.jjtGetNumChildren(); i++){
			System.out.println(((SimpleNode)parseTree.jjtGetChild(i)).getText());
	
		}

		throw new CompileException("Compiler not implemented.");
	}
}
