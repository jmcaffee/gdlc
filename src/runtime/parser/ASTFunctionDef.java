/* Generated By:JJTree: Do not edit this line. ASTFunctionDef.java */

package runtime.parser;

public class ASTFunctionDef extends SimpleNode {
  public ASTFunctionDef(int id) {
    super(id);
  }

  public ASTFunctionDef(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}