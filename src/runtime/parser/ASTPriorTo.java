/* Generated By:JJTree: Do not edit this line. ASTPriorTo.java */

package runtime.parser;

public class ASTPriorTo extends SimpleNode {
  public ASTPriorTo(int id) {
    super(id);
  }

  public ASTPriorTo(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
