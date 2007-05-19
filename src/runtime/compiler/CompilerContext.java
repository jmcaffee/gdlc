package runtime.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import runtime.main.IProblem;
import runtime.main.Log;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTRulesetDef;
import runtime.parser.ASTVarDef;
import runtime.parser.BaseNode;

public class CompilerContext {
	int 				tabCnt 		= 0;
	ASTCompilationUnit	rootNode;
//	HashMap<String, IVarNode> 		dpmVars2 	= new HashMap<String,IVarNode>();
//	HashMap<String,IVarNode> 		ppmVars2 	= new HashMap<String,IVarNode>();
	HashMap<String, ASTVarDef> 		dpmVars 	= new HashMap<String,ASTVarDef>();
	HashMap<String,ASTVarDef> 		ppmVars 	= new HashMap<String,ASTVarDef>();
	HashMap<String,ASTRuleDef>		rules 		= new HashMap<String,ASTRuleDef>();
	HashMap<String,ASTRulesetDef>	rulesets 	= new HashMap<String,ASTRulesetDef>();
	
	ArrayList<IProblem> 				warnings	= new ArrayList<IProblem>();
	ArrayList<IProblem> 				errors		= new ArrayList<IProblem>();
	
	public int getTabCount() { return tabCnt; }
	public void incTabCount() { this.tabCnt++; }
	public void setTabCount(int cnt) {this.tabCnt = cnt;}
	
	public void setRootNode(ASTCompilationUnit cuRoot) {
		this.rootNode = cuRoot;
	}
	
	public ASTCompilationUnit getRootNode() {return this.rootNode;}
	
	public void addDpmVar(String key, ASTVarDef var){dpmVars.put(key, var);}
	public boolean containsDpmVar(String key) { return dpmVars.containsKey(key);}

	public void addPpmVar(String key, ASTVarDef var){ppmVars.put(key, var);}
	public boolean containsPpmVar(String key) { return ppmVars.containsKey(key);}
	
	public void addRule(String key, ASTRuleDef rule){rules.put(key, rule);}
	public boolean containsRule(String key) { return rules.containsKey(key);}
	
	public void addRuleset(String key, ASTRulesetDef rule){rulesets.put(key, rule);}
	public boolean containsRuleset(String key) { return rulesets.containsKey(key);}

	public void	addWarning(IProblem warn) { warnings.add(warn); }
	public boolean hasWarnings() { return (!warnings.isEmpty()); }
	
	public void addError(IProblem err) { errors.add(err); }
	public boolean hasErrors() { return (!errors.isEmpty()); }
	
	public void dumpDpmVars() {
		Set<Map.Entry<String, ASTVarDef>> set = dpmVars.entrySet();
		
		for (Map.Entry<String, ASTVarDef> me : set) {
			Log.info(me.getKey() + ": " + me.getValue());
		}
	}

	public void dumpPpmVars() {
		Set<Map.Entry<String, ASTVarDef>> set = ppmVars.entrySet();
		
		for (Map.Entry<String, ASTVarDef> me : set) {
			Log.info(me.getKey() + ": " + me.getValue());
		}
	}

	public void dumpRules() {
		Set<Map.Entry<String, ASTRuleDef>> set = rules.entrySet();
		
		for (Map.Entry<String, ASTRuleDef> me : set) {
			Log.info(me.getKey() + ": " + me.getValue());
		}
	}


	public void dumpRulesets() {
		Set<Map.Entry<String, ASTRulesetDef>> set = rulesets.entrySet();
		
		for (Map.Entry<String, ASTRulesetDef> me : set) {
			Log.info(me.getKey() + ": " + me.getValue());
			dumpRulesetRules(me.getValue());
		}
	}

	void dumpRulesetRules(ASTRulesetDef ruleset){
		for(int i = 0; i < ruleset.jjtGetNumChildren(); i++){
			Log.info("> " + ((BaseNode)(ruleset.jjtGetChild(i))).data.get("Name"));
		}
	}
	
	public void dumpWarnings() {
		Log.info("Warnings: " + errors.size());
		if(hasWarnings()){
			for(IProblem me : warnings) {
				Log.info("Warning " + me.getId() + ": " + me.getDesc() + ". " + me.getMsg());
			}
		} 
	}

	public void dumpErrors() {
		Log.info("Errors: " + errors.size());
		if(hasErrors()){
			for(IProblem me : errors) {
				Log.info("E" + me.getId() + ": " + me.getDesc() + ". ");
				Log.info(me.getMsg());
			}
		} 
	}
}
	
