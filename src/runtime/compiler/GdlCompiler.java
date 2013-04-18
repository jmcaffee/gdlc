/**
 * 
 */
package runtime.compiler;

import java.util.ArrayList;
import java.util.HashMap;

import runtime.main.Log;
import runtime.parser.*;
import runtime.plugins.AliasesPlugin;
import runtime.plugins.ConditionDefsPlugin;
import runtime.plugins.FunctionDefsPlugin;
import runtime.plugins.GuidelinesPlugin;
import runtime.plugins.IGdlcPlugin;
import runtime.plugins.LookupImportsPlugin;
import runtime.plugins.LookupsPlugin;
import runtime.plugins.PowerLookupImportsPlugin;
import runtime.plugins.RefResolverPlugin;
import runtime.plugins.RuleDefsPlugin;
import runtime.plugins.RulesetDefsPlugin;
import runtime.plugins.VariablesPlugin;

/**
 * @author killer
 *
 */
public class GdlCompiler {
	HashMap<String, ArrayList<IGdlcPlugin>> compilePlugins = new HashMap<String, ArrayList<IGdlcPlugin>>();

	/**
	 * Constructor
	 * @param parseTree
	 */
	public GdlCompiler() {
	}

	/**
	 * configurePlugins configure a sections plugin list. Plugins are processed in order.
	 * @param section PRECOMPILE, COMPILE, POSTCOMPILE. Case is irrelevent.
	 * @param plugins ArrayList of plugins for a section.
	 */
	public void configurePlugins(String section, ArrayList<IGdlcPlugin> plugins){
		compilePlugins.put(section.toUpperCase(), plugins);
	}
	
	/**
	 * configureDefaultPlugins builds a default list of plugins for use by the compiler.
	 *
	 */
	public void configureDefaultPlugins(){
	      ArrayList<IGdlcPlugin> compilePlugins = new ArrayList<IGdlcPlugin>(); 
	      
	      compilePlugins.add(new VariablesPlugin());
	      compilePlugins.add(new LookupImportsPlugin());
	      compilePlugins.add(new LookupsPlugin());
	      compilePlugins.add(new ConditionDefsPlugin());		// Must compile conditions before PLKs so their aliases are available
	      compilePlugins.add(new PowerLookupImportsPlugin()); 	// when the PLKs are parsed.
	      compilePlugins.add(new RuleDefsPlugin());
	      compilePlugins.add(new RulesetDefsPlugin());
	      compilePlugins.add(new RefResolverPlugin());
	      compilePlugins.add(new GuidelinesPlugin());
	      compilePlugins.add(new AliasesPlugin());

	      this.configurePlugins("compile", compilePlugins);

	}
	
	/**
	 * compile compile the provided abstract source tree
	 * @param ctx compiler context object
	 * @return the resulting abstract source tree
	 * @throws CompileException
	 */	
	public ASTCompilationUnit compile(IProgramContext ctx, ASTCompilationUnit tree) throws CompileException {
		Log.status("Start compile");
		Log.info("Current compilation unit has " + tree.jjtGetNumChildren() + " children.");

		String[] sections = {	new String("PRECOMPILE"),
								new String("COMPILE"),
								new String("POSTCOMPILE"),
							};

		for(String section : sections){
			executePluginSection(section, ctx, tree);
		}

		return tree;
	}
	
	/**
	 * executePluginSection execute all plugins for a particular section.
	 * @param section PRECOMPILE, COMPILE, POSTCOMPILE
	 * @param ctx Compiler context object
	 * @param tree tree to parse
	 */
	private void executePluginSection(String section, IProgramContext ctx, ASTCompilationUnit tree){
		ArrayList<IGdlcPlugin> plugins = compilePlugins.get(section);
		
		if(null != plugins){
			for(IGdlcPlugin plugin : plugins){
				
				executePlugin(plugin, ctx, tree);
			}
		}
	}
	
	/**
	 * executePlugin execute a plugin against the abstract tree
	 * @param plugin plugin to execute
	 * @param ctx compiler context object
	 * @param tree tree to execute plugin against
	 */
	private void executePlugin(IGdlcPlugin plugin, IProgramContext ctx, ASTCompilationUnit tree){
		if(null != plugin){
			Log.status("Plugin starting ["+plugin.getName()+"]");
				plugin.execute(ctx, tree);
			Log.status("Plugin terminating ["+plugin.getName()+"]");
		}
	}
	

}	// GdlCompiler
