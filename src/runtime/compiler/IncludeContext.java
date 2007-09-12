package runtime.compiler;

import java.util.ArrayList;
import java.util.List;

import runtime.parser.ASTCompilationUnit;

public class IncludeContext {
	IProgramContext	master = null;
	List<ASTCompilationUnit> includes = new ArrayList<ASTCompilationUnit>();
	
	public IncludeContext(IProgramContext ctx){
		this.master = ctx;
	}
	public void addInclude(ASTCompilationUnit cu){
		includes.add(cu);
	}
	
	public boolean isEmpty() {return includes.isEmpty();}
	
	public ASTCompilationUnit removeInclude() {
		ASTCompilationUnit cu = includes.remove(0);
		return cu;
	}
	
	public IProgramContext getMaster(){ return this.master; }
}
