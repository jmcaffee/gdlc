package runtime.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import runtime.compiler.IProgramContext;
import runtime.main.Log;
import runtime.parser.ASTCompilationUnit;

public class ConditionCategoryConfigPlugin extends PropertiesPlugin implements IGdlcPlugin {

	@Override
	public String getName(){ return new String("ConditionCategoryConfig"); }

	@Override
	public void execute(IProgramContext ctx, ASTCompilationUnit rootNode) {
		HashMap<String, Integer> categories = new HashMap<>();
		Log.info("Loading condition category properties");
	
		// Set some basic default categories.
		setDefaultCategories(categories);
		
		ArrayList<String> propertyFiles = ctx.findPropertyFilesNamed("category.properties");
		
		loadProperties(categories, propertyFiles);
		
		ctx.setConditionCategories(categories);
	}

	private void setDefaultCategories(HashMap<String, Integer> categories) {
		categories.put("asset", 1);
		categories.put("credit", 2);
		categories.put("income", 3);
		categories.put("property", 4);
		categories.put("purchase", 5);
		categories.put("title", 6);
	}

}
