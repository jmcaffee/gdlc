/* Generated By:JJTree: Do not edit this line. ASTAliasOf.java */

package runtime.parser;

public class ASTAliasOf extends SimpleNode {
  public ASTAliasOf(int id) {
    super(id);
  }

  public ASTAliasOf(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
