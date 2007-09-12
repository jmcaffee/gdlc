/**
 * 
 */
package runtime.visitors;

import runtime.compiler.IGuidelineContext;
import runtime.parser.ASTGuidelineDef;

/**
 * @author killer
 *
 */
public class GuidelineVisitor extends DepthFirstVisitor {
	
	/* (non-Javadoc)
	 * @see runtime.parser.DepthFirstVisitor#visit(runtime.parser.ASTGuidelineDef, java.lang.Object)
	 */
	public Object visit(ASTGuidelineDef node, Object data) {
		IGuidelineContext ctx = (IGuidelineContext)data;
		
		ctx.setGuideline(node);
		return ctx;
	}

	
}
