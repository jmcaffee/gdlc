package runtime.compiler;

import runtime.parser.ASTVarDef;

public class VarBase {

	protected String varType = new String();
	protected String name = new String();
	protected String alias = new String();
	protected String type = new String();
	protected String dataType = new String();

	public VarBase(ASTVarDef node) {
		super();

		this.varType = node.getData("varType");
		this.name = node.getName();
		this.type = node.getData("Type");

		this.alias = node.getAlias();
		
		this.dataType = node.getData("DataType");
}

	public VarBase(String varName) {
		super();

		this.name = varName;
	}

	public String getVarType() {
		return this.varType;
	}

	public void setVarType(String type) {
		this.varType = type;
	}

	public String getName() {
		return this.name;
	}

	public String getAlias() {
		return this.alias;
	}

	public String getType() {
		return this.type;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String type) {
		this.dataType = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

}