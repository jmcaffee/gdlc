/**
 * 
 */
package runtime.main;


import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import runtime.compiler.*;
import runtime.parser.*;
import runtime.plugins.ComputePlugin;
import runtime.plugins.FunctionDefsPlugin;
import runtime.plugins.IGdlcPlugin;
import runtime.plugins.IncludesPlugin;


/**
 * @author killer
 *
 */
public class ParseMgr {
	HashMap<String, ArrayList<IGdlcPlugin>> plugins = new HashMap<String, ArrayList<IGdlcPlugin>>();
	ASTCompilationUnit 	parseTree	= null;
  	GdlParser 			parser		= null;

	
	/**
	 * Constructor
	 *
	 */
	public ParseMgr() {
	}
	
	/**
	 * configurePlugins configure a sections plugin list. Plugins are processed in order.
	 * @param section PREPARSE, PARSE, POSTPARSE. Case is irrelevent.
	 * @param plugins ArrayList of plugins for a section.
	 */
	public void configurePlugins(String section, ArrayList<IGdlcPlugin> plugins){
		this.plugins.put(section.toUpperCase(), plugins);
	}
	
	/**
	 * configureDefaultPlugins builds a default list of plugins for use by the compiler.
	 *
	 */
	public void configureDefaultPlugins(){
	      ArrayList<IGdlcPlugin> parsePlugins = new ArrayList<IGdlcPlugin>(); 
	      
	      parsePlugins.add(new IncludesPlugin());
	      parsePlugins.add(new ComputePlugin());
	      parsePlugins.add(new FunctionDefsPlugin());


	      this.configurePlugins("parse", parsePlugins);

	}
	
	/**
	 * execute execute the parser
	 * @param srcFile file to parse
	 * @param ctx context
	 */
	public void execute(String srcFile, CompilerContext ctx) {
											// Parse source code
		parseTree = parse(srcFile, ctx);
	}

	/**
	 * parse parse the source code.
	 * @param srcFile filename of source code to parse
	 * @param context program context
	 * @return null, abstract source tree node
	 */
	  private ASTCompilationUnit parse(String srcFile, CompilerContext context) {
		    ASTCompilationUnit	tree;
		    
		    Log.status("Reading from file " + srcFile + " . . .");
		    try {
		        parser = new GdlParser(new java.io.FileInputStream(srcFile));

		    	tree = parser.CompilationUnit();
				executePlugins(context, tree);
		    } 
		    catch (java.io.FileNotFoundException e) {
			    context.addError(new CompileError(CompileError.errors.FILENOTFOUND,
			    		new String("Parsing failed attempting to parse a file [ " + srcFile + " ]")));
		    	Log.error("[" + srcFile + "] File not found.");
			    Log.error(e.toString());
			    return null;
		    } 
		    catch (ParseException e) {
			    context.addError(new CompileError(CompileError.errors.PARSEERROR,
			    		new String("Parsing failed while parsing a file [ " + srcFile + " ]")));
		    	Log.error("[" + srcFile + "] Encountered errors during parsing.");
		    
			    Log.error(e.toString());
			    return null;
			}

	    	Log.status("[" + srcFile + "] Parse completed.");
		    
		    return tree;					// Return the abstract source tree
	  }

	  /**
	   * executePlugins execute all standard plugin sections: PREPARSE,PARSE,POSTPARSE
	   * @param ctx context
	   * @param tree to execute plugins against
	   */
	  private void executePlugins(CompilerContext ctx, ASTCompilationUnit tree){
			String[] sections = {	new String("PREPARSE"),
					new String("PARSE"),
					new String("POSTPARSE"),
			};

					// Execute all plugin sections
	    	for(String section : sections){
				executePluginSection(section, ctx, tree);
			}

	  }
	  
	  /**
		 * executePluginSection execute all plugins for a particular section.
		 * @param section PREPARSE, PARSE, POSTPARSE
		 * @param ctx Compiler context object
		 * @param tree tree to parse
		 */
		private void executePluginSection(String section, IProgramContext ctx, ASTCompilationUnit tree){
			ArrayList<IGdlcPlugin> plugins = this.plugins.get(section);
			
			if(null != plugins){
				for(IGdlcPlugin plugin : plugins){
					if(null != plugin){
						Log.status("Plugin starting ["+plugin.getName()+"]");
							plugin.execute(ctx, tree);
						Log.status("Plugin terminating ["+plugin.getName()+"]");
					}
				}
			}
		}
		
		

	  public ASTCompilationUnit getParseTree(){
		  return parseTree;
	  }
	  
	  
	  public void dumpParser(){
		  if(null != parser)
			  parser.dump();
	  }
	  
	/**
	 * inputStreamFromStringBuffer turn a StringBuffer into an InputStream
	 * @param sb StringBuffer to convert
	 * @return ByteArrayInputStream input stream
	 */
	protected ByteArrayInputStream inputStreamFromStringBuffer(StringBuffer sb){
	    try {
			return new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
	    } catch (UnsupportedEncodingException e) {
	    	return null;
	    }
	}

	/**
	 * parseBuffer parse a StringBuffer and return the parsed tree.
	 * @param input buffer to parse
	 * @param ctx context for storing errors
	 * @return
	 */
	public ASTCompilationUnit parseBuffer(StringBuffer input, CompilerContext ctx){
		GdlParser 			smallParse	= new GdlParser(inputStreamFromStringBuffer(input));
		ASTCompilationUnit 	tree 		= null;
		
	    try {
	    	tree = smallParse.CompilationUnit();
	    	executePlugins(ctx,tree);

	    } catch (ParseException e) {
			ctx.addError(new CompileError(CompileError.errors.PARSEERROR,
					new String("Parsing failed while parsing buffer: " + e.toString()),
					input.toString()));
			tree = null;
	    }
	    
	    return tree;
	}
}

