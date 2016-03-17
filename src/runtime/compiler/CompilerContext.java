package runtime.compiler;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import runtime.elements.ConditionMsg;
import runtime.compiler.FunctionMgr.Function;
import runtime.compiler.XmlFunctionMgr.XmlFunction;
import runtime.main.CompileWarning;
import runtime.main.IProblem;
import runtime.main.Log;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.ASTFunctionDef;
import runtime.parser.ASTGuidelineDef;
import runtime.parser.ASTLookupDef;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTConditionMsgDef;
import runtime.parser.ASTVarRef;
import runtime.parser.ASTXmlFuncDef;
import runtime.parser.Node;
import runtime.parser.SimpleNode;

public class CompilerContext implements IProgramContext, ILookups, IFunctionContext, IXmlFunctionContext {
	
	int 							tabCnt 		= 0;
	ASTCompilationUnit				rootNode	= null;
	
	HashMap<String,LookupData>		lookupData	= new HashMap<String,LookupData>();
	HashMap<String,ASTLookupDef>	lookups		= new HashMap<String,ASTLookupDef>();
	HashMap<String,IVariable> 		dpmVars 	= new HashMap<String,IVariable>();
	HashMap<String,IVariable> 		ppmVars 	= new HashMap<String,IVariable>();
	HashMap<String,ASTRuleDef>		rules 		= new HashMap<String,ASTRuleDef>();
	HashMap<String,IRuleset>		rulesets 	= new HashMap<String,IRuleset>();
//	HashMap<String,ASTGuidelineDef>	guidelines	= new HashMap<String,ASTGuidelineDef>();
	ASTGuidelineDef					guideline	= null;
	IFunctionContext				functions	= new FunctionMgr();
	IXmlFunctionContext				xmlFuncs	= new XmlFunctionMgr();
	HashMap<String,ConditionMsg>	conditions	= new HashMap<String,ConditionMsg>();
	ArrayList<IProblem> 			warnings	= new ArrayList<IProblem>();
	ArrayList<IProblem> 			errors		= new ArrayList<IProblem>();
	
	ArrayList<String>				includeDirs	= new ArrayList<String>();
	ArrayList<String>				configDirs	= new ArrayList<String>();
	
	int								ruleId		= 1;
	int								rulesetId 	= 1;
	int								lookupId	= 1;
	int								conditionId	= 1;
	HashMap<String, Integer> 		conditionCategories = new HashMap<String, Integer>();
	HashMap<String, Integer> 		conditionPriorTos	= new HashMap<String, Integer>();
	
	public int getTabCount() { return tabCnt; }
	public void incTabCount() { this.tabCnt++; }
	public void setTabCount(int cnt) {this.tabCnt = cnt;}
	
	int	getNextRuleId() 	{ int id = ruleId; ruleId += 1; return id; }
	int	getNextRulesetId() 	{ int id = rulesetId; rulesetId += 1; return id; }
	int	getNextLookupId() 	{ int id = lookupId; lookupId += 1; return id; }
	public int getNextConditionId(){ int id = conditionId; conditionId += 1; return id;}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#setRootNode(runtime.parser.ASTCompilationUnit)
	 */
	public void setRootNode(ASTCompilationUnit cuRoot) {
		this.rootNode = cuRoot;
	}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getRootNode()
	 */
	public ASTCompilationUnit getRootNode() {return this.rootNode;}
	
	public void addVar(VarPpm var){ ppmVars.put(var.getName(), var); }

	public VarPpm getVar(VarPpm var){ return (VarPpm)ppmVars.get(var.getName());	}

	public boolean containsVar(VarPpm var){ return ppmVars.containsKey(var.getName()); }

	public IVariable getVarPpm(String alias) {
		if(ppmVars.isEmpty()){
			return null;
		}
		for (IVariable ppm : ppmVars.values()) {
			if(ppm.getAlias().equalsIgnoreCase(alias)){
				return ppm;
			}
		}
		return null;
	}

	public void addVar(VarDpm var){ dpmVars.put(var.getName(), var); }

	public VarDpm getVar(VarDpm var){ return (VarDpm)dpmVars.get(var.getName());	}

	public boolean containsVar(VarDpm var){ return dpmVars.containsKey(var.getName()); }

	public IVariable getVarDpm(String alias) {
		if(dpmVars.isEmpty()){
			return null;
		}
		for (IVariable dpm : dpmVars.values()) {
			if(dpm.getAlias().equalsIgnoreCase(alias)){
				return dpm;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addRule(java.lang.String, runtime.parser.ASTRuleDef)
	 */
	public void addRule(String key, ASTRuleDef rule){
		String ruleId = "";
		if(this.containsRule(key)){
			this.addWarning(new CompileWarning(CompileWarning.warnings.REDEFINITION,
							"A rule has been redefined 1 or more times. [ " + key + " ]"));
			ASTRuleDef existingRule = this.getRule(key);
			ruleId = existingRule.getData("Id");
		}
		else {
			if(rule.getData("Id") == "99999"){
				ruleId = Integer.toString(this.getNextRuleId());
			}
		}
		
		rule.data.put("Id", ruleId);
		rules.put(key, rule);
	}
	
/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getRule(java.lang.String)
	 */
	public ASTRuleDef getRule(String key){return rules.get(key);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsRule(java.lang.String)
	 */
	public boolean containsRule(String key) { return rules.containsKey(key);}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addRuleset(java.lang.String, runtime.parser.ASTRulesetDef)
	 */
	public void addRuleset(String key, IRuleset ruleset){
		String rulesetId = "";
		if(this.containsRuleset(key)){
			this.addWarning(new CompileWarning(CompileWarning.warnings.REDEFINITION,
							"A ruleset has been redefined 1 or more times. [ " + key + " ]"));
			IRuleset existingRuleset = this.getRuleset(key);
			rulesetId = existingRuleset.getId();
		}
		else {
			if(ruleset.getId() == "99999"){
				rulesetId = Integer.toString(this.getNextRulesetId());
			}
		}
		
		ruleset.setId(rulesetId);
	
		rulesets.put(key, ruleset);
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getRuleset(java.lang.String)
	 */
	public IRuleset getRuleset(String key){return rulesets.get(key);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsRuleset(java.lang.String)
	 */
	public boolean containsRuleset(String key) { return rulesets.containsKey(key);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addFunction(java.lang.String, runtime.parser.ASTFunctionDef)
	 */
	public void addFunction(String key, ASTFunctionDef rule){functions.addFunction(key, rule);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getFunction(java.lang.String)
	 */
	public Function getFunction(String key){return functions.getFunction(key);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsFunction(java.lang.String)
	 */
	public boolean containsFunction(String key) { return functions.containsFunction(key);}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addXmlFunction(java.lang.String, runtime.parser.ASTFunctionDef)
	 */
	public void addXmlFunction(String key, ASTXmlFuncDef rule){xmlFuncs.addXmlFunction(key, rule);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getXmlFunction(java.lang.String)
	 */
	public XmlFunction getXmlFunction(String key){return xmlFuncs.getXmlFunction(key);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsXmlFunction(java.lang.String)
	 */
	public boolean containsXmlFunction(String key) { return xmlFuncs.containsXmlFunction(key);}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addCondition(java.lang.String, runtime.parser.ASTConditionMsgDef)
	 */
	public void addCondition(String key, ASTConditionMsgDef condition){
//		this.conditions.addConditionsMsg(key, condition);
		ConditionMsg 	cond 	= null;
		
		if(this.containsCondition(key)){
			this.addWarning(new CompileWarning(CompileWarning.warnings.REDEFINITION,
							"A condition has been redefined 1 or more times. [ " + key + " ]"));
			cond = this.getCondition(key);
			cond.initialize(condition);
		}
		else {
			cond = new ConditionMsg(this, key);
			cond.initialize(condition);
			cond.setId(this.getNextConditionId());
		}
		
		conditions.put(key, cond);
		
	}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getCondition(java.lang.String)
	 */
	public ConditionMsg getCondition(String key){return this.conditions.get(key);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsCondition(java.lang.String)
	 */
	public boolean containsCondition(String key) { return this.conditions.containsKey(key);}
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getConditionFromAlias(java.lang.String)
	 */
	public ConditionMsg getConditionFromAlias(String alias) {
		if(conditions.isEmpty()){
			return null;
		}
		for (ConditionMsg cond : conditions.values()) {
			if(cond.getAlias().equalsIgnoreCase(alias)){
				return cond;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addGuideline(java.lang.String, runtime.parser.ASTGuidelineDef)
	 */
//	public void addGuideline(String key, ASTGuidelineDef gdl){guidelines.put(key, gdl);}
	public void setGuideline(ASTGuidelineDef gdl){guideline = gdl;}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getGuideline(java.lang.String)
	 */
//	public ASTGuidelineDef getGuideline(String key){return guidelines.get(key);}

	public ASTGuidelineDef getGuideline(){ return guideline; }

	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addWarning(runtime.main.IProblem)
	 */
	public void	addWarning(IProblem warn) { warnings.add(warn); }
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#hasWarnings()
	 */
	public boolean hasWarnings() { return (!warnings.isEmpty()); }
	
	public int getWarningCount(){ return (getUniqueWarnings().size()); }
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addError(runtime.main.IProblem)
	 */
	public void addError(IProblem err) { errors.add(err); }

	/* (non-Javadoc)
	 * @see runtime.compiler.IErrorContext#hasErrors()
	 */
	public boolean hasErrors() { return (!errors.isEmpty()); }
	

	public String getParentRuleIdentifier(SimpleNode node) {
		if (node.getClass().getSimpleName().equalsIgnoreCase("ASTRuleDef")) {
			return node.getName();
		}

		Node currNode = node.jjtGetParent();
		while (currNode != null) {
			if (currNode.getClass().getSimpleName().equalsIgnoreCase("ASTRuleDef")) {
				return ((SimpleNode)currNode).getName();
			}
			currNode = currNode.jjtGetParent();
		}

		return "Unknown";
	}


	public void dumpDpmVars() {
		Set<Map.Entry<String, IVariable>> set = dpmVars.entrySet();
		
		for (Map.Entry<String, IVariable> me : set) {
			Log.info(me.getKey() + ": " + me.getValue());
		}
	}

	public void dumpPpmVars() {
		Set<Map.Entry<String, IVariable>> set = ppmVars.entrySet();
		
		for (Map.Entry<String, IVariable> me : set) {
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
		Set<Map.Entry<String, IRuleset>> set = rulesets.entrySet();
		
		for (Map.Entry<String, IRuleset> me : set) {
			Log.info(me.getKey() + ": " + me.getValue());
			dumpRulesetRules((Ruleset)me.getValue());
		}
	}

	void dumpRulesetRules(Ruleset ruleset){
		ruleset.dumpRules();
	}
	
	protected Set<String> getUniqueWarnings(){
		Set<String> warningSet = new HashSet<String>();
		for(IProblem me : warnings) {
			warningSet.add("Warning (W" + me.getId() + "): " + me.getDesc() + ". " + me.getMsg());
		}
		
		return warningSet;
	}
	
	public void dumpWarnings() {
		if(hasWarnings()){
			Set<String> warningSet = getUniqueWarnings();
			Log.warning("Warnings: " + warningSet.size());
			
			for(String w : warningSet) {
				Log.warning(w);
			}
		} else {
			Log.warning("Warnings: 0");
		}
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IErrorContext#dumpErrors()
	 */
	public String dumpErrors() {
		StringBuffer errs = new StringBuffer();
		String newLine = new String("\n");
		
		errs.append("Errors: " + errors.size() + newLine);
		if(hasErrors()){
			for(IProblem me : errors) {
				errs.append("Error (E" + me.getId() + "): " + me.getDesc() + ". " + me.getMsg() + newLine);
			}
			errs.append(newLine);
		} 
		
		return errs.toString();
	}
	
	/**
	 * Add a list of dirs to search for configuration files
	 * @param cdirs
	 */
	public void setConfigDirs(ArrayList<String> cdirs) {
		for (String dir : cdirs) {
			configDirs.add(dir);
		}
	}

	/**
	 * Add a directory to be searched for included src files.
	 * All of the added directory's children (sub-dirs) will be added as well.
	 * @param directory path
	 */
	public void addIncludeDir(String iDir){
		addSubDirs(iDir);
		
	}

	/***
	 * Walk the directory's sub-dir tree and add each directory.
	 * @param iDir
	 */
	private void addSubDirs(String iDir){
	    File dir = new File(iDir);

//	  The list of files can also be retrieved as File objects
	    File[] files = null;
	    
	    // This filter only returns directories
	    FileFilter fileFilter = new FileFilter() {
	        public boolean accept(File file) {
	            return (file.isDirectory() && !file.getName().startsWith("."));
	        }
	    };
	    files = dir.listFiles(fileFilter);
	    if(dir.isDirectory() ){
	    	this.includeDirs.add(dir.getAbsolutePath());
	    }
	    if(null != files){					// If there are subdirectories...
		    for(File file : files){
		    	this.includeDirs.add(file.getAbsolutePath());
		    }
	    }
	}

	public String joinPath(String dir, String file){
		StringBuffer joined = new StringBuffer(dir);
		
		// Choose the file separator based on what is currently used in the dir.
		// Default to posix style.
		String separator = "/";
		if(dir.contains("\\")) {
			separator = "\\";
		}
		joined.append(separator).append(file);
		
		return joined.toString();
	}
	
	public String findIncludePath(String filePath){
		String incFile = new String("");
		for(String incPath : this.includeDirs){
			if(new File(joinPath(incPath,filePath)).isFile()){
				incFile = joinPath(incPath, filePath);
				break;
			}
		}
		
		return incFile;
	}
	
	public void addLookup(String key, ASTLookupDef lk){
		String lkupId = Integer.toString(this.getNextLookupId());

		lk.data.put("Id", lkupId);
		this.lookups.put(key, lk);
	}

	public ASTLookupDef getLookup(String key){
		ASTLookupDef lkup = null;
		if(this.lookups.containsKey(key)){
			lkup = lookups.get(key);
		}
		
		return lkup;
	}
	
	public boolean containsLookup(String key){
		return this.lookups.containsKey(key);
	}

	public boolean containsLookupData(String key){
		return lookupData.containsKey(key);
	}

	public void setLookupData(Map<String, LookupData> lkData){
		this.lookupData = (HashMap<String, LookupData>)lkData;
	}

	public HashMap<String,LookupData> getLookupData(){
		return this.lookupData;
	}

	public LookupData getLookupData(String name){
		return this.lookupData.get(name);
	}
	
	
	/**
	 * Return a variable's name based on its alias.
	 * @param alias variable alias
	 * @param hint DPM,PPM or "" to search both both
	 * @return variable name or null if no match
	 */
	public String getVarName(String alias, String hint){
		
		if(hint.isEmpty() || hint.toUpperCase().contains("DPM")){
			if(!dpmVars.isEmpty()){
				for (IVariable dpm : dpmVars.values()) {
					if(dpm.getAlias().equalsIgnoreCase(alias)){
						return dpm.getName();
					}
				}
			}

/*
 * 			VarDpm var = (VarDpm) getDpmVar(alias);
 * 
			if(null != var){
				return var.name;
			}
 * 
 */
		}
		
		if(hint.isEmpty() || hint.toUpperCase().contains("PPM")){
			if(!ppmVars.isEmpty()){
				for (IVariable ppm : ppmVars.values()) {
					if(ppm.getAlias().equalsIgnoreCase(alias)){
						return ppm.getName();
					}
				}
			}

/*			
			VarPpm var = (VarPpm) getPpmVar(alias);
			if(null != var){
				return var.name;
			}
*/
		}
		
		return null;
	}
	
	
	/**
	 * Return a variable's alias based on its name.
	 * @param name variable name (valid id)
	 * @param hint DPM,PPM or "" for both
	 * @return variable alias or null if no match
	 */
	public String getVarAlias(String name, String hint){
		
		if(hint.isEmpty() || hint.toUpperCase().contains("DPM")){
			if(!dpmVars.isEmpty()){
				for (IVariable dpm : dpmVars.values()) {
					if(dpm.getName().equalsIgnoreCase(name)){
						return dpm.getAlias();
					}
				}
			}
		}
		
		if(hint.isEmpty() || hint.toUpperCase().contains("PPM")){
			if(!ppmVars.isEmpty()){
				for (IVariable ppm : ppmVars.values()) {
					if(ppm.getName().equalsIgnoreCase(name)){
						return ppm.getAlias();
					}
				}
			}
		}
		
		return null;
	}
	@Override
	public ArrayList<String> findPropertyFilesNamed(String filename) {
		ArrayList<String> paths = new ArrayList<String>();
		
		for (String cpath : configDirs) {
			if(new File(joinPath(cpath,filename)).isFile()){
				paths.add(joinPath(cpath, filename));
			}
		}
		
		return paths;
	}
	
	@Override
	public void setConditionCategories(HashMap<String, Integer> condCategories) {
		for (String key : condCategories.keySet()) {
			this.conditionCategories.put(key, condCategories.get(key));
		}
	}
	
	/**
	 * Retrieve a Category type ID based on a key (text version of type).
	 * The ConditionCategoryConfigPlugin reads a properties file named category.properties
	 * to populate the values stored here.
	 * @param key human readable text version of type
	 * @return ID if found, -1 if the key is not found
	 */
	public int getConditionCategoryId(String key) {
		if(conditionCategories.containsKey(key)){
			return conditionCategories.get(key);
		}
		// -1 indicates the key can't be found.
		return -1;
	}
	
	@Override
	public void setConditionPriorTos(HashMap<String, Integer> condPriorTos) {
		for (String key : condPriorTos.keySet()) {
			this.conditionPriorTos.put(key, condPriorTos.get(key));
		}
	}
	
	/**
	 * Retrieve a PriorTo type ID based on a key (text version of type).
	 * The ConditionPriorToConfigPlugin reads a properties file named priorto.properties
	 * to populate the values stored here.
	 * @param key human readable text version of type
	 * @return ID if found, -1 if the key is not found
	 */
	public int getConditionPriorToId(String key) {
		if(conditionPriorTos.containsKey(key)){
			return conditionPriorTos.get(key);
		}
		// -1 indicates the key can't be found.
		return -1;
	}

}
	
