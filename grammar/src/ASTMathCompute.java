/* Generated By:JJTree: Do not edit this line. ASTMathCompute.java */

package runtime.parser;

public class ASTMathCompute extends Compute {
  public ASTMathCompute(int id) {
    super(id);
  }

  public ASTMathCompute(GdlParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}