/* Generated By:JJTree: Do not edit this line. SimpleNode.java */

package runtime.parser;

import runtime.parser.BaseNode;

public class SimpleNode extends BaseNode implements Node {
  protected Node parent;
  protected Node[] children;
  protected int id;
  protected GdlParser parser;
	String text;

  public SimpleNode(int i) {
    id = i;
  }

  public SimpleNode(GdlParser p, int i) {
    this(i);
    parser = p;
  }
  
  public int id() {return this.id;}
  
  public void jjtOpen() {
  }

  public void jjtClose() {
  }

	public void setText( String text ) {this.text = text;}
	public String getText() { return this.text; }
	public String getData(String key) {
		String theData = null;
		
		if(this.data.containsKey(key)) {
			theData = (String)this.data.get(key);
		}
		
		return theData;
	}
	
	public String packData() {
		return this.data.values().toString();
	}
	
  public void jjtSetParent(Node n) { parent = n; }
  public Node jjtGetParent() { return parent; }

  public void jjtAddChild(Node n, int i) {
    if (children == null) {
      children = new Node[i + 1];
    } else if (i >= children.length) {
      Node c[] = new Node[i + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = n;
  }

  public Node jjtGetChild(int i) {
    return children[i];
  }

  public int jjtGetNumChildren() {
    return (children == null) ? 0 : children.length;
  }

  /** Accept the visitor. **/
  public Object jjtAccept(GdlParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

  /** Accept the visitor. **/
  public Object childrenAccept(GdlParserVisitor visitor, Object data) {
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        children[i].jjtAccept(visitor, data);
      }
    }
    return data;
  }

  /* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

//  public String toString() { return GdlParserTreeConstants.jjtNodeName[id] + ( null != getText()? " [" +getText()+"]":""); }
  public String toString() { 
	  String attributes = new String();
	  attributes = GdlParserTreeConstants.jjtNodeName[id];
	  
	  if (null != getText()){
		attributes = attributes + " [" +getText()+"]";  
	  }
	  
	  attributes = attributes + ( 0 < data.size()? (" " + data.toString()) : "");

//	  {
//		for(int i = 0; i < data.size(); i++){
//			attributes = attributes + data.toString();
//		}
//	  }
	  return attributes;
  }
  
  public String toString(String prefix) { return prefix + toString(); }

  /* Override this method if you want to customize how the node dumps
     out its children. */

  public void dump(String prefix) {
    System.out.println(toString(prefix));
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
	SimpleNode n = (SimpleNode)children[i];
	if (n != null) {
	  n.dump(prefix + " ");
	}
      }
    }
  }
  
  public void replaceChild(int i, SimpleNode newNode){
	  if(children[i] != null){
		  children[i] = newNode;
	  }
  }
}

