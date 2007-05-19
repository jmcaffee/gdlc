package runtime.compiler;

import java.util.ArrayList;
import java.util.List;

import runtime.parser.ASTCompilationUnit;

public class IncludeContext {
	List<ASTCompilationUnit> includes = new ArrayList<ASTCompilationUnit>();
	
	public void addInclude(ASTCompilationUnit cu){
		includes.add(cu);
	}
	
	public boolean isEmpty() {return includes.isEmpty();}
	
	public ASTCompilationUnit removeInclude() {
		ASTCompilationUnit cu = includes.remove(0);
		return cu;
	}
}
