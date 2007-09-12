package runtime.compiler;

import java.util.ArrayList;

import runtime.compiler.PowerLookupData.VarOp;
import runtime.main.CompileError;
import runtime.parser.ASTLookupDef;
import runtime.parser.SimpleNode;

public class PowerLookupRulesetBuilder {

	CompilerContext ctx = null;
	
	public PowerLookupRulesetBuilder(CompilerContext ctx){this.ctx = ctx;}
	
	public StringBuffer build(PowerLookupData plData){
		String 			plName 		= plData.name;
		StringBuffer 	ruleset 	= new StringBuffer();
		int				ruleIndex 	= 1;
		
		// Normalize name (remove spaces)
		
		// Add rule aliases
		for(int i = 0; i < plData.values.size(); i++){
			ruleset.append(buildRuleAlias(plName, ruleIndex++));
		}

		ruleIndex = 1;		// Reset the index.
		
		ruleset.append("ruleset ").append(plName).append("(true, PL)");
		
			for(ArrayList<String> values : plData.values){
				ruleset.append(buildRule(plName, ruleIndex++, plData.operations, values));
			}
			
		ruleset.append("end ");
		
		return ruleset;
	}
	
	protected String generateRuleName(String name, int num){ return new String(name+"-"+num);}
	protected String generateRuleAlias(String name, int num){ return new String(name+"."+num);}
	
	//alias(rule, SimpleAliasRule1, "Alias Rule 1");

	protected StringBuffer buildRuleAlias(String name, int ruleNum){
		StringBuffer alias = new StringBuffer("alias(rule, ");
		alias.append( generateRuleName(name, ruleNum) ).append(", ");
		alias.append("\"" + generateRuleAlias(name, ruleNum) ).append("\");");
		
		return alias;
	}
	
	protected StringBuffer buildRule(String name, int ruleNum, ArrayList<VarOp> ops, ArrayList<String> values){
		StringBuffer rule = new StringBuffer();
		
		rule.append("rule ").append("" + generateRuleName(name, ruleNum) ).append("() ");
			rule.append("if(");
			
			boolean first 		= true;
			int		valueIndex 	= 0;
			
			for(VarOp op : ops){
				if(isActionOp(op)){
					break;
				}

				if(first){
					first = false;
					rule.append(op.getName() +" "+ op.getOp());
				}else{
					rule.append(" && "+ op.getName() +" "+ op.getOp());
				}
				rule.append(" \""+ values.get(valueIndex++) +"\" ");
			}

			rule.append(") then ");

			for(VarOp op : ops){
				if(!isActionOp(op)){
					continue;
				}

				addAction(values.get(valueIndex++), rule, op);
			}
			
			rule.append("end ");

		rule.append("end ");
		return rule;
	}

	protected void addAction(String value, StringBuffer rule, VarOp op) {
		rule.append(""+ op.name +" = ");
		if(op.op.equals("Lookup")){
			addLookupAction(value, rule, op);
		}else {
			rule.append("\""+ value +"\";");
		}
	}
	
	protected void addLookupAction(String value, StringBuffer rule, VarOp op) {
		ASTLookupDef lkup = ctx.getLookup(value);
		if(null == lkup){
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
				new String("Lookup [" + value + "] definition is missing. Verify that the lookup is defined before using it in a PowerLookup.")));
			return;
		}
		String xParam = ((SimpleNode)lkup.jjtGetChild(0)).getName();
		String yParam = ((SimpleNode)lkup.jjtGetChild(1)).getName();
		rule.append("lookup(\""+ value +"\", " + xParam + ", " + yParam + ");");
	}
	
	protected boolean isActionOp(VarOp op){
		if(op.getOp().equals("Assign") ||
				op.getOp().equals("Lookup")){
			return true;
		}
		return false;
	}
}

/*
ruleset PLNAME(true, PL)
	rule PLNAME.n()
		if(VARNAME1 OP1 VALUE1 && VARNAME2 OP2 VALUE2)
		then
			VARNAME3 OP3 VALUE3;
		end
	end
end
*/