
package runtime.parser;
public class Compute extends SimpleNode {

  public Compute(int id) {
    super(id);
  }

  public Compute(GdlParser p, int id) {
    super(p, id);
  }

	public Node getLhs() {
		return jjtGetChild(0);
	}

	public Node getOperator() {
		return jjtGetChild(1);
	}

	public Node getRhs() {
		return jjtGetChild(2);
	}

  //public String toString() { return GdlParserTreeConstants.jjtNodeName[id] + ( null != getLhs()? " [LHS: " + lhs.toString()+"]":"") + ( null != getRhs()? " [RHS: " + rhs.toString()+"]":""); }
  public String toString() { return GdlParserTreeConstants.jjtNodeName[id] + ( null != getLhs()? " [LHS: " + getLhs().toString()+"]":"") + ( null != getRhs()? " [RHS: " + getRhs().toString()+"]":""); }

}
