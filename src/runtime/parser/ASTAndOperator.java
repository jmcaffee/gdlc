/* Generated By:JJTree: Do not edit this line. ASTAndOperator.java */

package runtime.parser;

public class ASTAndOperator extends SimpleNode {
  public ASTAndOperator(int id) {
    super(id);
  }

  public ASTAndOperator(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
