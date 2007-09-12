/**
 * 
 */
package runtime.visitors;

import runtime.compiler.CompilerContext;
import runtime.compiler.Ruleset;
import runtime.main.CompileError;
import runtime.main.CompileWarning;
import runtime.parser.ASTAlias;
import runtime.parser.ASTRuleDef;
import runtime.parser.ASTRulesetDef;

/**
 * @author killer
 *
 */
public class AliasVisitor extends DepthFirstVisitor {

	/* (non-Javadoc)
	 * @see runtime.parser.DepthFirstVisitorr#visit(runtime.parser.ASTAlias, java.lang.Object)
	 */
	public Object visit(ASTAlias node, Object data) {
		// Handle Ruleset definition nodes
		CompilerContext ctx = (CompilerContext)data;
		String aliasType = node.getData("AliasType");
		String name = node.getName();
		String alias = node.getAlias();
		
		if(aliasType.equals("rule")){
			setRuleAlias(name, alias, ctx);
		}
		else if(aliasType.equals("ruleset")){
			setRulesetAlias(name, alias, ctx);
		}
		else{
			ctx.addError(new CompileError(CompileError.errors.UNSUPPORTEDOPERATION, "Unknown alias type [" + aliasType + "]"));
		}
		
		return ctx;
	}

	protected void setRuleAlias(String name, String alias, CompilerContext ctx){
		if(!ctx.containsRule(name)){
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING, "Attempting to set an alias for an undefined rule [" + name + "]"));
			return;
		}
		
		ASTRuleDef rule = ctx.getRule(name);
		if(!rule.getAlias().equals(alias)){
			if(!rule.getAlias().equals(rule.getName())){
				ctx.addWarning(new CompileWarning(CompileWarning.warnings.REDEFINITION, "Rule alias is being redefined [" + name + ":" + alias + "]"));
			}
		}
		rule.data.put("Alias", alias);
	}


	protected void setRulesetAlias(String name, String alias, CompilerContext ctx){
		if(!ctx.containsRuleset(name)){
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING, "Attempting to set an alias for an undefined ruleset [" + name + "]"));
			return;
		}
		
		Ruleset rs = (Ruleset)ctx.getRuleset(name);
		ASTRulesetDef ruleset = rs.getNode();
		if(!ruleset.getAlias().equals(alias)){
			if(!ruleset.getAlias().equals(ruleset.getName())){
				ctx.addWarning(new CompileWarning(CompileWarning.warnings.REDEFINITION, "Ruleset alias is being redefined [" + name + ":" + alias + "]"));
			}
		}
		ruleset.data.put("Alias", alias);
	}


}
