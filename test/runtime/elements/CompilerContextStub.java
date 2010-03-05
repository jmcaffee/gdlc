package runtime.elements;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import runtime.compiler.IProgramContext;
import runtime.compiler.IRuleset;
import runtime.compiler.VarDpm;
import runtime.compiler.VarPpm;
import runtime.compiler.FunctionMgr.Function;
import runtime.main.IProblem;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.ASTConditionMsgDef;
import runtime.parser.ASTFunctionDef;
import runtime.parser.ASTGuidelineDef;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTVarDef;

public class CompilerContextStub implements IProgramContext {
	ASTCompilationUnit	rootNode;

	HashMap<String,ASTVarDef> 		dpmVars 	= new HashMap<String,ASTVarDef>();
	HashMap<String,ASTVarDef> 		ppmVars 	= new HashMap<String,ASTVarDef>();
	HashMap<String,ASTRuleDef>		rules 		= new HashMap<String,ASTRuleDef>();
	HashMap<String,IRuleset>		rulesets 	= new HashMap<String,IRuleset>();
	HashMap<String,ASTConditionMsgDef>		conditions 	= new HashMap<String,ASTConditionMsgDef>();
	
	ArrayList<IProblem> 				warnings	= new ArrayList<IProblem>();
	ArrayList<IProblem> 				errors		= new ArrayList<IProblem>();

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#setRootNode(runtime.parser.ASTCompilationUnit)
	 */
	public void setRootNode(ASTCompilationUnit cuRoot) {
		this.rootNode = cuRoot;
	}
	
	public void addIncludeDir(String iDir){
	}

	public String findIncludePath(String filePath){
		return new String("");
	}
	

	public String joinPath(String dir, String file){
		StringBuffer joined = new StringBuffer(dir);
		joined.append(File.separatorChar).append(file);
		
		return joined.toString();
	}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getRootNode()
	 */
	public ASTCompilationUnit getRootNode() {return this.rootNode;}
	
	public ASTVarDef getAstDpmVar(String key) { return dpmVars.get(key);}

	public ASTVarDef getAstPpmVar(String key) { return ppmVars.get(key);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addRule(java.lang.String, runtime.parser.ASTRuleDef)
	 */
	public void addRule(String key, ASTRuleDef rule){rules.put(key, rule);}

	public ASTRuleDef getRule(String key){return null;}
	public ASTRuleDef getAstRule(String key){return rules.get(key);}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsRule(java.lang.String)
	 */
	public boolean containsRule(String key) { return rules.containsKey(key);}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addRuleset(java.lang.String, runtime.parser.ASTRulesetDef)
	 */
	public void addRuleset(String key, IRuleset ruleset){rulesets.put(key, ruleset);}

	public IRuleset getRuleset(String key){return null;}
	public IRuleset getAstRuleset(String key){return rulesets.get(key);}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsRuleset(java.lang.String)
	 */
	public boolean containsRuleset(String key) { return rulesets.containsKey(key);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addCondition(java.lang.String, runtime.parser.ASTConditionMsgDef)
	 */
	public void addCondition(String key, ASTConditionMsgDef condition){conditions.put(key, condition);}

	public ConditionMsg getCondition(String key){return null;}
	public ASTConditionMsgDef getAstCondition(String key){return conditions.get(key);}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsCondition(java.lang.String)
	 */
	public boolean containsCondition(String key) { return conditions.containsKey(key);}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addFunction(java.lang.String, runtime.parser.ASTFunctionDef)
	 */
	public void addFunction(String key, ASTFunctionDef rule){;}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getFunction(java.lang.String)
	 */
	public Function getFunction(String key){return null;}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsFunction(java.lang.String)
	 */
	public boolean containsFunction(String key) { return false;}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#setGuideline(ASTGuidelineDef)
	 */
	public void setGuideline(ASTGuidelineDef gdl){return;}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getGuideline()
	 */
	public ASTGuidelineDef getGuideline(){return null;}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addWarning(runtime.main.IProblem)
	 */
	public void	addWarning(IProblem warn) { warnings.add(warn); }
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#hasWarnings()
	 */
	public boolean hasWarnings() { return (!warnings.isEmpty()); }
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addError(runtime.main.IProblem)
	 */
	public void addError(IProblem err) { errors.add(err); }

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#dumpErrors()
	 */
	public String dumpErrors() { return null; }

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#hasErrors()
	 */
	public boolean hasErrors() { return (!errors.isEmpty()); }
	
	public void addVar(VarPpm var){ /*ppmVars.put(var.getName(), var);*/ }

	public VarPpm getVar(VarPpm var){ return null; /*return ppmVars.get(var.getName());*/	}

	public boolean containsVar(VarPpm var){ return false; /*return dpmVars.containsKey(var.getName());*/ }

	public void addVar(VarDpm var){ /*dpmVars.put(var.getName(), var);*/ }

	public VarDpm getVar(VarDpm var){ return null; /*return dpmVars.get(var.getName());*/	}

	public boolean containsVar(VarDpm var){ return false; /*return dpmVars.containsKey(var.getName());*/ }

}
