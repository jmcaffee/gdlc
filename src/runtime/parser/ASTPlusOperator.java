/* Generated By:JJTree: Do not edit this line. ASTPlusOperator.java */

package runtime.parser;

public class ASTPlusOperator extends SimpleNode {
  public ASTPlusOperator(int id) {
    super(id);
  }

  public ASTPlusOperator(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
