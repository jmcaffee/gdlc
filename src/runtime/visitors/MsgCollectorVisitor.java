/**
 * 
 */
package runtime.visitors;

import java.util.ArrayList;

import runtime.parser.ASTConditionMsgDef;
import runtime.parser.ASTConditionMsgRef;
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
	
	public Object visit(ASTConditionMsgRef node, Object data) {
		messages.add(node);
		return data;
	}
	
	public Object visit(ASTConditionMsgDef node, Object data) {
		messages.add(node);
		return data;
	}
	
	public void reset(){
		this.messages.clear();
	}


}
