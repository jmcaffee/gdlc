/* Generated By:JJTree: Do not edit this line. ASTLogicalOperator.java */

package runtime.parser;

public class ASTLogicalOperator extends SimpleNode {
  public ASTLogicalOperator(int id) {
    super(id);
  }

  public ASTLogicalOperator(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
