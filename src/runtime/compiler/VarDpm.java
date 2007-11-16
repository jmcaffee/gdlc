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

	public String getDataType() {
		if(null != this.dataType){
			return this.dataType;
		} else
		{
			return this.getDataTypeFromProductType();
		}
	}

	protected String getDataTypeFromProductType(){
		if(this.productType.equals("1")){
			return new String("Boolean");
		}

		if(this.productType.equals("2")){
			return new String("Date");
		}
		if(this.productType.equals("3")){
			return new String("Money");
		}
		if(this.productType.equals("4")){
			return new String("Numeric");
		}
		if(this.productType.equals("5")){
			return new String("Percentage");
		}

		return new String("Text");
	}


}
