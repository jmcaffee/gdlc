package runtime.compiler;

import java.util.ArrayList;

import runtime.compiler.PowerLookupData.VarOp;
import runtime.elements.ConditionMsg;
import runtime.main.CompileError;
import runtime.main.Log;
import runtime.parser.ASTLookupDef;
import runtime.parser.SimpleNode;

public class PowerLookupRulesetBuilder {

	CompilerContext ctx = null;
	VarGdlGenerator	genVar = null;
	
	public PowerLookupRulesetBuilder(CompilerContext ctx){this.ctx = ctx; genVar = new VarGdlGenerator(ctx);}
	
	public StringBuffer build(PowerLookupData plData){
		String 			plName 		= plData.name;
		StringBuffer 	ruleset 	= new StringBuffer();
		int				ruleIndex 	= 1;
		
		
		// Add rule aliases
		for(int i = 0; i < plData.values.size(); i++){
			ruleset.append(buildRuleAlias(plName, ruleIndex++));
		}

		ruleIndex = 1;		// Reset the index.
		
		ruleset.append( buildRulesetAlias(plName) );
		String exeType = filterExeType(plData.exeType);
		ruleset.append("ruleset ").append( cleanString(plName) ).append("(").append(exeType).append(", PL)");
		
			// values is an array of arrays (rows).
			for(ArrayList<String> values : plData.values){
				ruleset.append(buildRule(plName, ruleIndex++, plData.operations, values));
			}
			
		ruleset.append("end ");
		
		return ruleset;
	}
	
	private String filterExeType(String exeType) {
		if(exeType.equalsIgnoreCase("Continue")){
			return "continue";
		}

		// Currently, only continue and true are supported for PLKs.
		// True is the default so a blank = true OR Stop if True = true.
		// Since there are no other options here, return true.
		return "true";
	}


	protected String generateRuleName(String name, int num){ 
		String cleanStr = cleanString(name);
		return new String(cleanStr+"-"+num);
	}
	
	protected String generateRuleAlias(String name, int num){ return new String(name+"."+num);}
	
	protected String cleanString(String dirty){
		String cleanStr = dirty;
		cleanStr = cleanStr.replaceAll("[\\s/\\?*#+]", "");	// Removes whitespace, forward and backslashes, and ?*#+ chars.
		cleanStr = cleanStr.replace("[_]+", "_");	// Replace consecutive underscores with 1 underscore.
		cleanStr = cleanStr.replace("[\\.]", "-");	// Replace periods with dash.
		return new String(cleanStr);
	}
	//alias(rule, SimpleAliasRule1, "Alias Rule 1");

	protected StringBuffer buildRulesetAlias(String name){
		String id = cleanString(name);
		if(id.equals(name)){	// Don't create an alias if it is not needed.
			return new StringBuffer();
		}
		StringBuffer alias = new StringBuffer("alias(ruleset, ");
		alias.append( cleanString(name) ).append(", ");
		alias.append("\"" + name ).append("\");");
		
		Log.info(alias.toString());
		return alias;
	}
	
	protected StringBuffer buildRuleAlias(String name, int ruleNum){
		StringBuffer alias = new StringBuffer("alias(rule, ");
		alias.append( generateRuleName(name, ruleNum) ).append(", ");
		alias.append("\"" + generateRuleAlias(name, ruleNum) ).append("\");");
		
		Log.info(alias.toString());
		return alias;
	}
	
	protected StringBuffer buildRule(String name, int ruleNum, ArrayList<VarOp> ops, ArrayList<String> values){
		StringBuffer rule 		= new StringBuffer();
		StringBuffer ruleOps 	= new StringBuffer();
		
		rule.append("rule ").append("" + generateRuleName(name, ruleNum) ).append("() ");
			rule.append("if(");
			
			boolean first 		= true;
			int		valueIndex 	= 0;
			int		orderIndex  = 0;	// We have to tell AMS which column the data came from
										// so it can recreate the matrix correctly when viewed
										// within the PLK editor (admin module).
			
			// For each row of data, use the operation (LHOperand and Operator)
			// at the same index.
			for(VarOp op : ops){
				orderIndex += 1;		// The column always starts at one.
				if(isActionOp(op)){
					break;
				}

				// Skip adding the var, op, value set if the value is blank.
				if(values.get(valueIndex).isEmpty()){
					valueIndex++;
					continue;
				}
				
				if(first){
					first = false;
					ruleOps.append(metaOrder(orderIndex) + genVar.lookupAndCast(op.getName(), op.typ) +" "+ op.getOp());
				}else{
					ruleOps.append(" && "+ metaOrder(orderIndex) + genVar.lookupAndCast(op.getName(), op.typ) +" "+ op.getOp());
				}
				ruleOps.append(" "+ normalizeValue(values.get(valueIndex++)) +" ");
			}

			if(!(ruleOps.length() > 0)){
				return new StringBuffer();
			}
			rule.append(ruleOps);
			rule.append(") then ");

			// Reset the orderIndex so the column count is correct.
			orderIndex = 0;
			
			for(VarOp op : ops){
				orderIndex += 1;	// Column count always starts from 1.
				if(!isActionOp(op)){
					continue;
				}

				addAction(values.get(valueIndex++), rule, op, orderIndex);
			}
			
			rule.append("end ");

		rule.append("end ");
		
		Log.info(rule.toString());
		return rule;
	}

	private String metaOrder(int orderIndex) {
		return "["+Integer.toString(orderIndex) + "]";
	}

	protected boolean isMsgOp(VarOp op){
		if(op.getName().equalsIgnoreCase("True Message")){
			return true;
		}
		if(op.getName().equalsIgnoreCase("False Message")){
			return true;
		}
		return false;
	}
	
	protected boolean isConditionOp(VarOp op){
		if(op.getName().equalsIgnoreCase("Condition")){
			return true;
		}
		if(op.getName().equalsIgnoreCase("True Action:Condition")){
			return true;
		}
		if(op.getName().equalsIgnoreCase("False Action:Condition")){
			return true;
		}
		return false;
	}
	
	protected void addAction(String value, StringBuffer rule, VarOp op, int orderIndex) {
		if(value.isEmpty()){
			return;				// Don't add action if the value is blank. 
		}
		if(isMsgOp(op)){
			addMessageAction(value, rule, op, orderIndex);
			return;
		}
		if(isConditionOp(op)){
			addConditionAction(value, rule, op, orderIndex);
			return;
		}
		rule.append(metaOrder(orderIndex) + genVar.lookupAndCast(op.name, op.typ) +" = ");
		
		if(op.op.equals("Lookup")){	
			addLookupAction(value, rule, op);
		}else {
			rule.append(normalizeValue(value) +";");
		}
	}

	
	protected void addMessageAction(String value, StringBuffer rule, VarOp op, int orderIndex) {
		if(!value.startsWith("{") && !value.endsWith("}")){
			ctx.addError(new CompileError(CompileError.errors.IMPORTERROR,
				new String("Message [" + value + "] declaration is missing brace prefix or suffix. PowerLookup messages must be surrounded with braces: '{Message text here.}'.")));
			return;
		}
		
		// Ex:
		// [7]message(findings, "Recommendation By CV: New Balance of $<DPM>dpmCurrency</DPM>.");

		String msgTxt = value.substring(1, (value.length() - 1));		// Strip braces.
		StringBuffer msg = new StringBuffer(metaOrder(orderIndex) + "message("+ op.getOp() + ", \""+ msgTxt +"\");");  
		
		if(op.getName().equalsIgnoreCase("True Message")){
			rule.append(msg);
			return;
		}
		
		if(op.getName().equalsIgnoreCase("False Message")){
			rule.append(" else ");
			rule.append(msg);
			return;
		}
		
		ctx.addError(new CompileError(CompileError.errors.IMPORTERROR,
				new String("Message [" + value + "] unrecognized message type: ["+ op.getName() +"]. Valid PowerLookup message types are: 'True Message', 'False Message'.")));
		return;
		
	}
	
	
	protected void addConditionAction(String value, StringBuffer rule, VarOp op, int orderIndex) {
		ConditionMsg condition = ctx.getConditionFromAlias(value);
		if(null == condition){
			// Maybe we're actually using the GDLC ID?
			condition = ctx.getCondition(value);
		}
		
		// Still nothing? Throw an error.
		if(null == condition){
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
				new String("Condition [" + value + "] definition is missing. Verify that the condition is defined before using it in a PowerLookup.")));
			return;
		}
		
		// Ex output:
		// [7]condition ConditionId();

		// Condition references use the identifier.
		String condId = condition.getIdentifier();		
		StringBuffer cond = new StringBuffer(metaOrder(orderIndex) + "condition " + condId + "();");
		
		if(op.getOp().equalsIgnoreCase("True Action:Condition")){
			rule.append(cond);
			return;
		}
		
		if(op.getOp().equalsIgnoreCase("False Action:Condition")){
			rule.append(" else ");
			rule.append(cond);
			return;
		}
		
		// FIXME: Add the current filename to the error messages.
		ctx.addError(new CompileError(CompileError.errors.IMPORTERROR,
				new String("Condition [" + value + "] unrecognized condition operation type: ["+ op.getName() +"]. Valid PowerLookup condition operation types are: 'True Action:Condition', 'False Action:Condition'.")));
		return;
		
	}
	
	
	protected void addLookupAction(String value, StringBuffer rule, VarOp op) {
		ASTLookupDef lkup = ctx.getLookup(value);
		if(null == lkup){
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
				new String("Lookup [" + value + "] definition is missing. Verify that the lookup is defined before using it in a PowerLookup.")));
			return;
		}

		rule.append("lookup(\""+ value +"\");");
	}
	
	protected boolean isActionOp(VarOp op){
		if(	op.getOp().equals("Assign") ||
            op.getOp().equals("Lookup") ||
			op.getOp().equals("Condition") ||
			op.getOp().equals("True Action:Condition") ||
			op.getOp().equals("False Action:Condition") ||
			op.getName().equalsIgnoreCase("True Message") ||
			op.getName().equalsIgnoreCase("False Message") ||
			op.getName().equalsIgnoreCase("Condition") ||
			op.getType().equalsIgnoreCase("Findings") ||
			op.getType().equalsIgnoreCase("Exceptions") ||
			op.getType().equalsIgnoreCase("Condition") ||
			op.getType().equalsIgnoreCase("True Action:Condition") ||
			op.getType().equalsIgnoreCase("False Action:Condition") ){	
				return true;
		}
		return false;
	}

	
	protected String stripCast(String value){
		if((value.startsWith("DPM(") || value.startsWith("PPM(")) && 
				value.endsWith(")")){
			return value.substring(4, value.length() - 1);
		}
		
		return value;
		
	}
	
	protected String normalizeValue(String value){
		if(value.startsWith("DPM(") && value.endsWith(")")){
			return genVar.lookupAndCast(stripCast(value), "DPM");
		}
		
		if(value.startsWith("PPM(") && value.endsWith(")")){
			return genVar.lookupAndCast(stripCast(value), "PPM");
		}

		if(value.equalsIgnoreCase("NULL")){
			return "NULL";
		}
		
		if(value.contains("|")){
			value = value.replaceAll("|", ",");
		}
		
		if(value.length() > 0){
			value = new String("\"" + value + "\"");
		}

		return value;
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