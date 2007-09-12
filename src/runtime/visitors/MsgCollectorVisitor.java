/**
 * 
 */
package runtime.visitors;

import java.util.ArrayList;

import runtime.parser.ASTConditionMsg;
import runtime.parser.ASTMessage;
import runtime.parser.SimpleNode;


/**
 * @author killer
 *
 */
public class MsgCollectorVisitor extends DepthFirstVisitor {
	ArrayList<SimpleNode> messages = new ArrayList<SimpleNode>();
	
	public ArrayList<SimpleNode> getMsgs(){ return this.messages; }
	
	public Object visit(ASTMessage node, Object data) {
		messages.add(node);
		return data;
	}
	
	public Object visit(ASTConditionMsg node, Object data) {
		messages.add(node);
		return data;
	}
	
	public void reset(){
		this.messages.clear();
	}


}
