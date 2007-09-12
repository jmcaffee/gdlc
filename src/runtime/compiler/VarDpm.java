/**
 * 
 */
package runtime.compiler;

import runtime.parser.ASTVarDef;

/**
 * @author killer
 *
 */
public class VarDpm extends VarBase implements IVariable {
	String order = new String();
	String productType = new String();
	
	public VarDpm(ASTVarDef node){
		super(node);
		
		this.order = node.getData("Order");
		this.productType = node.getData("ProductType");
		
		if(null == this.order) {
			this.order = new String("0");	// Order is never set so we need a default.
		}
	}
	
	public VarDpm(String name){
		super(name);
	}
	
	public String getOrder() {
		return this.order;
	}

	public String getProductType() {
		return this.productType;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setProductType(String type) {
		this.productType = type;
	}


}
