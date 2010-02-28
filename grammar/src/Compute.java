
package runtime.parser;
public class Compute extends SimpleNode {

  public Compute(int id) {
    super(id);
  }

  public Compute(GdlParser p, int id) {
    super(p, id);
  }

	public Node getLhs() {
		if(this.jjtGetNumChildren() > 0)
			return jjtGetChild(0);
		
		return null;
	}

	public Node getOperator() {
		if(this.jjtGetNumChildren() > 1)
			return jjtGetChild(1);
		
		return null;
	}

	public Node getRhs() {
		if(this.jjtGetNumChildren() > 2)
		return jjtGetChild(2);
		
		return null;
	}

  //public String toString() { return GdlParserTreeConstants.jjtNodeName[id] + ( null != getLhs()? " [LHS: " + lhs.toString()+"]":"") + ( null != getRhs()? " [RHS: " + rhs.toString()+"]":""); }
  public String toString() { return GdlParserTreeConstants.jjtNodeName[id] + ( null != getLhs()? " [LHS: " + getLhs().toString()+"]":"") + ( null != getRhs()? " [RHS: " + getRhs().toString()+"]":""); }

}
