/* Generated By:JJTree: Do not edit this line. ASTAssignValue.java */

package runtime.parser;

public class ASTAssignValue extends SimpleNode {
  public ASTAssignValue(int id) {
    super(id);
  }

  public ASTAssignValue(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
