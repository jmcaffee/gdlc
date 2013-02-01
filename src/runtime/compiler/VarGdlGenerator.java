package runtime.compiler;

public class VarGdlGenerator {
	public CompilerContext ctx;

	public VarGdlGenerator(CompilerContext ctx) {
		this.ctx = ctx;
	}
	
	/**
	 * Lookup a variable id and return it as a cast statement:
	 * 	DPM(varName)
	 * If varType not DPM or PPM (case insensitive), null will be returned.
	 * 
	 * @param nameOrAlias name or alias of variable to lookup
	 * @param varType DPM or PPM. Variable type to lookup
	 * @return cast statement or null if varType not valid
	 */
	public String lookupAndCast(String nameOrAlias, String varType){
		if(!varType.equalsIgnoreCase("DPM") && !varType.equalsIgnoreCase("PPM") ){
			return null;
		}
		
		String varName = lookupVar(nameOrAlias, varType);
		if(null == varName){
			varName = nameOrAlias;		// Variable not found in context.
		}
		
		String output = null;
		if(varType.equalsIgnoreCase("PPM")){
			output = new String("PPM(" + varName + ")");
		}

		if(varType.equalsIgnoreCase("DPM")){
			output = new String("DPM(" + varName + ")");
		}

		return output;
		
	}
	
	
	public String lookupVar(String nameOrAlias, String varType){
		if(varType.equalsIgnoreCase("PPM")){
			VarPpm ppm = (VarPpm) ctx.getVarPpm(nameOrAlias);
			if(null != ppm){
				return ppm.name;
			}
		}
		
		VarDpm dpm = (VarDpm) ctx.getVarDpm(nameOrAlias);
		if(null != dpm){
			return dpm.name;
		}
		
		return null;
		
	}
	
	
	
}