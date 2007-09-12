package runtime.compiler;

import java.util.ArrayList;

import runtime.parser.ASTRulesetDef;

public interface IRuleset {

	public abstract void addRuleName(String ruleName);

	public abstract ArrayList<String> getRuleNames();

	public abstract String getExecuteType();

	public abstract void setExecuteType(String executeType);

	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getName();

	public abstract String getAlias();

	public abstract void setName(String name);

	public abstract String getType();

	public abstract void setType(String type);
	
	public abstract ASTRulesetDef	getNode();

}