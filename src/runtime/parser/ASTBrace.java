/* Generated By:JJTree: Do not edit this line. ASTBrace.java */

package runtime.parser;

public class ASTBrace extends SimpleNode {
  public ASTBrace(int id) {
    super(id);
  }

  public ASTBrace(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
