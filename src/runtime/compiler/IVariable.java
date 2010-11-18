/**
 * 
 */
package runtime.compiler;

/**
 * @author killer
 *
 */
public interface IVariable {

	abstract public String getVarType();

	abstract public void setVarType(String type);

	abstract public String getDataType();

	abstract public void setDataType(String type);

	abstract public String getName();
	
	abstract public void setName(String name);

	abstract public String getAlias();
	
	abstract public void setAlias(String alias);

	abstract public String getType();
	
	abstract public void setType(String type);
	
}
