package runtime.compiler;

import runtime.parser.ASTGuidelineDef;

public interface IGuidelineContext {

//	public abstract void addGuideline(String key, ASTGuidelineDef gdl);
//
//	public abstract ASTGuidelineDef getGuideline(String key);

	public abstract void setGuideline(ASTGuidelineDef gdl);

	public abstract ASTGuidelineDef getGuideline();

}