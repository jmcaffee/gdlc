/* Generated By:JJTree: Do not edit this line. ASTExecuteType.java */

package runtime.parser;

public class ASTExecuteType extends SimpleNode {
  public ASTExecuteType(int id) {
    super(id);
  }

  public ASTExecuteType(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
