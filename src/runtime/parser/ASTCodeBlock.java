/* Generated By:JJTree: Do not edit this line. ASTCodeBlock.java */

package runtime.parser;

public class ASTCodeBlock extends SimpleNode {
  public ASTCodeBlock(int id) {
    super(id);
  }

  public ASTCodeBlock(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}