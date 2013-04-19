package runtime.plugins;

import java.util.ArrayList;
import java.util.HashMap;

import runtime.compiler.IProgramContext;
import runtime.main.Log;
import runtime.parser.ASTCompilationUnit;

public class ConditionPriorToConfigPlugin extends PropertiesPlugin implements
		IGdlcPlugin {

	@Override
	public String getName(){ return new String("ConditionPriorToConfig"); }

	@Override
	public void execute(IProgramContext ctx, ASTCompilationUnit rootNode) {
		HashMap<String, Integer> priorToTypes = new HashMap<>();
		Log.info("Loading condition PriorTo properties");
		
		// Set some basic default prior to types.
		setDefaultPriorToTypes(priorToTypes);
		
		ArrayList<String> propFiles = ctx.findPropertyFilesNamed("priorto.properties");
		
		loadProperties(priorToTypes, propFiles);
		
		ctx.setConditionPriorTos(priorToTypes);
	}

	private void setDefaultPriorToTypes(HashMap<String, Integer> priorToTypes) {
		priorToTypes.put("docs", 1);
		priorToTypes.put("funding", 2);
		priorToTypes.put("approval", 3);
	}

}
