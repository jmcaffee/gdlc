package runtime.main;

import java.util.ArrayList;

public class CommandLineParameters {
	ArrayList<String>	switches	= new ArrayList<String>();
	ArrayList<String>	parameters 	= new ArrayList<String>();
	ArrayList<String>	args 		= new ArrayList<String>();
	
	public CommandLineParameters() {
		super();
	}

	public void process(String args[]) {
		for(String arg : args){
			if(arg.startsWith("/")){
				parameters.add(arg.substring(1));
				continue;
			}

			if(arg.startsWith("-")){
				switches.add(arg.substring(1));
				continue;
			}
			
			this.args.add(arg);
		}
		
		this.processSwitches();				// Call overridden methods.
		this.processParameters();
		this.processArgs();
		this.validate();
	}
		
//	public String[] parseArgs(String compileArgs[]) {
//		int count = 0;
//		for(String arg : compileArgs){
//			if(arg.startsWith("/I")){
//				compilerContext.addIncludeDir(arg.substring(2));
//				count++;
//			}
//		}
//		
//		int totalArgs = compileArgs.length - count;
//		count = 0;
//		
//		String parsedArgs[] = new String[totalArgs];
//		for(String arg : compileArgs){
//			if(!arg.startsWith("/I")){
//				parsedArgs[count++] = arg;
//			}
//		}
//		
//		return parsedArgs;
//	}

	public void usage(){
		
	}
	
	protected void processSwitches(){
		
	}
	
	protected void processParameters(){
		
	}
	
	protected void processArgs(){
		
	}
	
	protected void validate(){
		
	}
	
}