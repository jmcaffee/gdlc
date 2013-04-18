package runtime.compiler;

import runtime.elements.ConditionMsg;
import runtime.main.IProblem;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.ASTConditionMsgDef;
import runtime.parser.ASTRuleDef;
 
public interface IProgramContext extends IGuidelineContext, IErrorContext, IIncludeDirs, IFunctionContext, IXmlFunctionContext {

	public abstract void 				setRootNode(ASTCompilationUnit cuRoot);

	public abstract ASTCompilationUnit 	getRootNode();

	public abstract void 				addRule(String key, ASTRuleDef rule);

	public abstract ASTRuleDef			getRule(String key);

	public abstract boolean 			containsRule(String key);

	public abstract void 				addRuleset(String key, IRuleset ruleset);

	public abstract IRuleset			getRuleset(String key);

	public abstract boolean 			containsRuleset(String key);

	public abstract void 				addCondition(String key, ASTConditionMsgDef condition);

	public abstract ConditionMsg		getCondition(String key);

	public abstract boolean 			containsCondition(String key);

	public abstract ConditionMsg		getConditionFromAlias(String alias);
	
	public abstract void 				addWarning(IProblem warn);

	public abstract boolean 			hasWarnings();

	public abstract void 				addVar(VarPpm var);

	public abstract IVariable			getVar(VarPpm var);

	public abstract IVariable			getVarPpm(String alias);

	public abstract boolean 			containsVar(VarPpm var);

	public abstract void 				addVar(VarDpm var);

	public abstract IVariable			getVar(VarDpm var);

	public abstract IVariable			getVarDpm(String alias);

	public abstract boolean 			containsVar(VarDpm var);


}