package runtime.compiler;

import runtime.parser.ASTLookupDef;

public interface ILookups {
	public abstract void 			addLookup(String key, ASTLookupDef lk);

	public abstract ASTLookupDef 	getLookup(String key);
	
	public abstract boolean 		containsLookup(String key);


}
