package runtime.compiler;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import runtime.main.CompileWarning;
import runtime.main.IProblem;
import runtime.main.Log;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.ASTGuidelineDef;
import runtime.parser.ASTLookupDef;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTVarRef;

public class CompilerContext implements IProgramContext, ILookups {
	int 							tabCnt 		= 0;
	ASTCompilationUnit				rootNode;
	
	HashMap<String,LookupData>		lookupData	= new HashMap<String,LookupData>();
	HashMap<String,ASTLookupDef>	lookups		= new HashMap<String,ASTLookupDef>();
	HashMap<String,IVariable> 		dpmVars 	= new HashMap<String,IVariable>();
	HashMap<String,IVariable> 		ppmVars 	= new HashMap<String,IVariable>();
	HashMap<String,ASTRuleDef>		rules 		= new HashMap<String,ASTRuleDef>();
	HashMap<String,IRuleset>		rulesets 	= new HashMap<String,IRuleset>();
//	HashMap<String,ASTGuidelineDef>	guidelines	= new HashMap<String,ASTGuidelineDef>();
	ASTGuidelineDef					guideline	= null;
	
	ArrayList<IProblem> 			warnings	= new ArrayList<IProblem>();
	ArrayList<IProblem> 			errors		= new ArrayList<IProblem>();
	
	ArrayList<String>				includeDirs	= new ArrayList<String>();
	
	public int getTabCount() { return tabCnt; }
	public void incTabCount() { this.tabCnt++; }
	public void setTabCount(int cnt) {this.tabCnt = cnt;}
	
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

	public void addVar(VarDpm var){ dpmVars.put(var.getName(), var); }

	public VarDpm getVar(VarDpm var){ return (VarDpm)dpmVars.get(var.getName());	}

	public boolean containsVar(VarDpm var){ return dpmVars.containsKey(var.getName()); }

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addRule(java.lang.String, runtime.parser.ASTRuleDef)
	 */
	public void addRule(String key, ASTRuleDef rule){rules.put(key, rule);}

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
	public void addRuleset(String key, IRuleset ruleset){rulesets.put(key, ruleset);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#getRuleset(java.lang.String)
	 */
	public IRuleset getRuleset(String key){return rulesets.get(key);}

	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#containsRuleset(java.lang.String)
	 */
	public boolean containsRuleset(String key) { return rulesets.containsKey(key);}

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
	
	public int getWarningCount(){ return warnings.size(); }
	
	/* (non-Javadoc)
	 * @see runtime.compiler.IProgramContext#addError(runtime.main.IProblem)
	 */
	public void addError(IProblem err) { errors.add(err); }

	/* (non-Javadoc)
	 * @see runtime.compiler.IErrorContext#hasErrors()
	 */
	public boolean hasErrors() { return (!errors.isEmpty()); }
	

	
	
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
	
	public void dumpWarnings() {
		Log.error("Warnings: " + warnings.size());
		if(hasWarnings()){
			for(IProblem me : warnings) {
				Log.error("Warning (W" + me.getId() + "): " + me.getDesc() + ". " + me.getMsg());
			}
		} 
	}

	/* (non-Javadoc)
	 * @see runtime.compiler.IErrorContext#dumpErrors()
	 */
	public String dumpErrors() {
		StringBuffer errs = new StringBuffer();
		String newLine = new String("\n");
		
		errs.append("Errors: " + errors.size() + newLine);
		//Log.error("Errors: " + errors.size());
		if(hasErrors()){
			for(IProblem me : errors) {
				errs.append("Error (E" + me.getId() + "): " + me.getDesc() + ". " + newLine);
				//Log.error("Error (E" + me.getId() + "): " + me.getDesc() + ". ");
				errs.append(me.getMsg()+ newLine + newLine);
				//Log.error(me.getMsg());
			}
		} 
		
		return errs.toString();
	}
	
	public void addIncludeDir(String iDir){
		addSubDirs(iDir);
		
	}

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
	    for(File file : files){
	    	this.includeDirs.add(file.getAbsolutePath());
	    }
	}

	public String joinPath(String dir, String file){
		StringBuffer joined = new StringBuffer(dir);
		joined.append(File.separatorChar).append(file);
		
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
		if(isRedefining(key, lk)){
				addWarning(new CompileWarning(CompileWarning.warnings.REDEFINITION,
					new String("Lookup [" + key + "] is being redefined. One or both delta variables are different.")));
		}
	
		this.lookups.put(key, lk);
	}

	protected boolean isRedefining(String key, ASTLookupDef lk){
		ASTLookupDef currentNode = getLookup(key);
		if(null == currentNode){
			return false; 
		}
		
		ASTVarRef curDelta = (ASTVarRef)currentNode.jjtGetChild(0);
		ASTVarRef newDelta = (ASTVarRef)lk.jjtGetChild(0);
		if(!curDelta.getName().equals(newDelta.getName())){
			return true;
		}
		
		curDelta = (ASTVarRef)currentNode.jjtGetChild(1);
		newDelta = (ASTVarRef)lk.jjtGetChild(1);
		if(!curDelta.getName().equals(newDelta.getName())){
			return true;
		}
		
			
		return false;
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

	public void setLookupData(Map lkData){
		this.lookupData = (HashMap<String, LookupData>)lkData;
	}

	public HashMap<String,LookupData> getLookupData(){
		return this.lookupData;
	}

	public LookupData getLookupData(String name){
		return this.lookupData.get(name);
	}


}
	
