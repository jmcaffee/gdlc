/* Generated By:JJTree: Do not edit this line. ASTName.java */

package runtime.parser;

public class ASTName extends SimpleNode {
  public ASTName(int id) {
    super(id);
  }

  public ASTName(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
