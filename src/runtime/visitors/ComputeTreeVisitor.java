/**
 * 
 */
package runtime.visitors;

import java.util.LinkedList;

import runtime.parser.ASTBrace;
import runtime.parser.ASTEqualityCompute;
import runtime.parser.ASTLogicalCompute;
import runtime.parser.ASTMathCompute;
import runtime.parser.GdlParserTreeConstants;
import runtime.parser.SimpleNode;

/**
 * @author killer
 *
 */
public class ComputeTreeVisitor extends DepthFirstVisitor {

	/* (non-Javadoc)
	 * @see runtime.parser.DepthFirsVisitor#visit(runtime.parser.ASTLogicalCompute, java.lang.Object)
	 */
	public Object visit(ASTLogicalCompute node, Object data) {
		int cnt = node.jjtGetNumChildren();
		ASTLogicalCompute newNode = null;
		
		LinkedList<SimpleNode> opQueue 	= new LinkedList<SimpleNode>();
		LinkedList<SimpleNode> termQueue = new LinkedList<SimpleNode>();
		LinkedList<SimpleNode> resQueue 	= new LinkedList<SimpleNode>();
		
		for(int i = 0; i < cnt; i++){
			SimpleNode child = (SimpleNode)node.jjtRemoveChild(0);
			switch(child.id()){
			case GdlParserTreeConstants.JJTLOGICALOPERATOR:
				opQueue.add(child);
				break;
			default:
				termQueue.add(child);
			}
		}

		
		while(termQueue.size() > 0){
			newNode = new ASTLogicalCompute(GdlParserTreeConstants.JJTLOGICALCOMPUTE);
			if(null != resQueue.peek()){
				this.addChild(newNode, resQueue.poll());
			}else{
				this.addChild(newNode, termQueue.poll());	
			}
			
			this.addChild(newNode, opQueue.poll());
			this.addChild(newNode, termQueue.poll());
			
			resQueue.add(newNode);
		}
		
//		while(!opQueue.isEmpty() && !resQueue.isEmpty()){
//			newNode = new ASTLogicalCompute(GdlParserTreeConstants.JJTLOGICALCOMPUTE);
//			this.addChild(newNode, resQueue.poll());
//			this.addChild(newNode, opQueue.poll());
//			this.addChild(newNode, resQueue.poll());
//			
//			resQueue.add(newNode);
//		}
		
		newNode = (ASTLogicalCompute)resQueue.poll();
		
		for(int i = 0; i < 3; i++){
			this.addChild((SimpleNode)node, (SimpleNode)newNode.jjtRemoveChild(0));
		}
		
		data = node.childrenAccept(this, data);
		return data;
	}

	/* (non-Javadoc)
	 * @see runtime.parser.DepthFirstVisitor#visit(runtime.parser.ASTEqualityCompute, java.lang.Object)
	 */
	public Object visit(ASTEqualityCompute node, Object data) {
		SimpleNode parent = (SimpleNode)node.jjtGetParent();
		String order = node.getData("Order");
		
		if(parent.id() != GdlParserTreeConstants.JJTBRACE){
			ASTBrace brace = new ASTBrace(GdlParserTreeConstants.JJTBRACE);
			this.addChild(brace, node);
			this.replaceChild(parent,node,brace);
		}
		
		data = node.childrenAccept(this, data);
		return data;
	}


	/* (non-Javadoc)
	 * @see runtime.parser.DepthFirsVisitor#visit(runtime.parser.ASTMathCompute, java.lang.Object)
	 */
	public Object visit(ASTMathCompute node, Object data) {
		int cnt = node.jjtGetNumChildren();
		ASTMathCompute newNode = null;
		
		LinkedList<SimpleNode> opQueue 	= new LinkedList<SimpleNode>();
		LinkedList<SimpleNode> termQueue = new LinkedList<SimpleNode>();
		LinkedList<SimpleNode> resQueue 	= new LinkedList<SimpleNode>();
		
		for(int i = 0; i < cnt; i++){
			SimpleNode child = (SimpleNode)node.jjtRemoveChild(0);
			switch(child.id()){
			case GdlParserTreeConstants.JJTDIVOPERATOR:
			case GdlParserTreeConstants.JJTMINUSOPERATOR:
			case GdlParserTreeConstants.JJTMULTOPERATOR:
			case GdlParserTreeConstants.JJTRAISETOOPERATOR:
			case GdlParserTreeConstants.JJTPLUSOPERATOR:
				opQueue.add(child);
				break;
			default:
				termQueue.add(child);
			}
		}

		
		while(termQueue.size() > 0){
			newNode = new ASTMathCompute(GdlParserTreeConstants.JJTMATHCOMPUTE);
			if(null != resQueue.peek()){
				this.addChild(newNode, resQueue.poll());
			}else{
				this.addChild(newNode, termQueue.poll());	
			}
			
			this.addChild(newNode, opQueue.poll());
			this.addChild(newNode, termQueue.poll());
			
			resQueue.add(newNode);
		}
		
//		while(!opQueue.isEmpty() && !resQueue.isEmpty()){
//			newNode = new ASTLogicalCompute(GdlParserTreeConstants.JJTLOGICALCOMPUTE);
//			this.addChild(newNode, resQueue.poll());
//			this.addChild(newNode, opQueue.poll());
//			this.addChild(newNode, resQueue.poll());
//			
//			resQueue.add(newNode);
//		}
		
		newNode = (ASTMathCompute)resQueue.poll();
		
		for(int i = 0; i < 3; i++){
			this.addChild((SimpleNode)node, (SimpleNode)newNode.jjtRemoveChild(0));
		}
		
		data = node.childrenAccept(this, data);
		return data;
	}

	protected int addChild(SimpleNode parent, SimpleNode child){
		child.jjtSetParent(parent);
		
		parent.jjtAddChild(child, parent.jjtGetNumChildren());
		
		return parent.jjtGetNumChildren();
	}

	/**
	 * replaceChild replaces a child node within a parent node
	 * @param parent node - has children
	 * @param oNode node to replace
	 * @param nNode node to insert
	 * @return number of children in parent
	 */
	protected int replaceChild(SimpleNode parent, SimpleNode oNode, SimpleNode nNode){
		for(int i = 0; i < parent.jjtGetNumChildren(); i++){
			if(parent.jjtGetChild(i) == oNode){
				nNode.jjtSetParent(parent);
				parent.jjtAddChild(nNode, i);
				break;
			}
		}
		
		return parent.jjtGetNumChildren();
	}

}
