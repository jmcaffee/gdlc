package runtime.visitors;

import java.util.ArrayList;

import runtime.compiler.IProgramContext;
import runtime.compiler.VarDpm;
import runtime.compiler.VarPpm;
import runtime.main.CompileError;
import runtime.parser.ASTMessage;
import runtime.parser.ASTRuleRef;
import runtime.parser.ASTRulesetRef;
import runtime.parser.ASTVarRef;

public class ResolveRefsVisitor extends DepthFirstVisitor {

	public Object visit(ASTRulesetRef node, Object data) {
		IProgramContext ctx = (IProgramContext)super.visit(node, data);
		String name = (String)node.data.get("Name");
		
		if(!ctx.containsRuleset(name)) {
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
					new String("Ruleset [" + name + "] definition missing.")));
		}
		
		return ctx;
	}

	public Object visit(ASTRuleRef node, Object data) {
		IProgramContext ctx = (IProgramContext)super.visit(node, data);
		String name = (String)node.data.get("Name");
		
		if(!ctx.containsRule(name)) {
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
					new String("Rule [" + name + "] definition missing.")));
		}
		
		return ctx;
	}

	public Object visit(ASTVarRef node, Object data) {
		IProgramContext ctx = (IProgramContext)super.visit(node, data);
		String name = (String)node.data.get("Name");
		String cast = (String)node.data.get("cast");
		if(null != cast){
			if(cast.equalsIgnoreCase("DPM")){
				if(!ctx.containsVar(new VarDpm(name))){
					// "Name" is not its ID. See if it's an alias.
					VarDpm dpm = (VarDpm)ctx.getVarDpm(name);
					if(null != dpm){
						// It is an alias, set the name and alias.
						node.data.put("Alias",name);
						node.data.put("Name", dpm.getName());
					} else{
						// It's not a valid name, it's not a valid alias.
						ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
								new String("Variable [" + name + "] definition missing.")));
					}
				}
			} else if(cast.equalsIgnoreCase("PPM")){
				if(!ctx.containsVar(new VarPpm(name))){
					// "Name" is not its ID. See if it's an alias.
					VarPpm ppm = (VarPpm)ctx.getVarPpm(name);
					if(null != ppm){
						// It is an alias, set the name and alias.
						node.data.put("Alias",name);
						node.data.put("Name", ppm.getName());
					} else{
						// It's not a valid name, it's not a valid alias.
						ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
								new String("Variable [" + name + "] definition missing.")));
					}
				}
			} else {
				// It's not a valid cast.
				ctx.addError(new CompileError(CompileError.errors.BADCAST,
						new String("Invalid cast operation: Variable [" + name + "].")));
			}
				
		} else if(!ctx.containsVar(new VarDpm(name)) &&
			!ctx.containsVar(new VarPpm(name))) {
			ctx.addError(new CompileError(CompileError.errors.DEFMISSING,
					new String("Variable [" + name + "] definition missing.")));
		}
		
		return ctx;
	}


	/* (non-Javadoc)
	 * @see runtime.parser.GdlParserVisitor#visit(runtime.parser.ASTMessage, java.lang.Object)
	 */
	public Object visit(ASTMessage node, Object data) {
		IProgramContext ctx = (IProgramContext)super.visit(node, data);
		String msg = (String)node.data.get("value");

		if(msg.contains("<DPM>")){
			ArrayList<String> 	nestedVars 	= new ArrayList<String>();
			ArrayList<Integer> 	startTags 	= new ArrayList<Integer>();
			ArrayList<Integer> 	endTags 	= new ArrayList<Integer>();
			
			int iStartTag = -1;
			while (-1 != (iStartTag = msg.indexOf("<DPM>", (iStartTag + 1)))) {
				startTags.add(iStartTag);
				iStartTag = msg.indexOf("</DPM>", (iStartTag + 1));
				endTags.add(iStartTag);
			}
			
			Integer start 	= -1;
			Integer end 	= -1;
			
			for (int i = 0; i < startTags.size(); i++) {
				start = startTags.get(i);
				end = endTags.get(i);
				
				if(start > -1){
					nestedVars.add(msg.substring(start, (end + 6)));
				}
			}
			
			for (int i = 0; i < nestedVars.size(); i++) {
				String varName = nestedVars.get(i).replace("<DPM>", "");
				varName = varName.replace("</DPM>", "");
				// Lookup variable alias
				if(ctx.containsVar(new VarDpm(varName))){
					// We have the ID, not the alias, so replace it with the alias.
					VarDpm dpm = (VarDpm)ctx.getVar(new VarDpm(varName));
					String alias = new String("<DPM>"+ dpm.getAlias() +"</DPM>");
					msg = msg.replace(nestedVars.get(i), alias);
				}
			}

			node.data.put("value", msg);
		}
		
		return data;
	}



}
