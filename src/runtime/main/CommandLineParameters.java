package runtime.main;

import java.util.ArrayList;

public class CommandLineParameters {
    ArrayList<String>   switches    = new ArrayList<String>();
    ArrayList<String>   parameters  = new ArrayList<String>();
    ArrayList<String>   args        = new ArrayList<String>();

    public CommandLineParameters() {
        super();
    }

    public void process(String args[]) {
        for(String arg : args){
            if( arg.startsWith("--") ){
                parameters.add(arg.substring(2));
                continue;
            }

            if(arg.startsWith("-")){
                switches.add(arg.substring(1));
                continue;
            }

            this.args.add(arg);
        }

        this.processSwitches();             // Call overridden methods.
        this.processParameters();
        this.processArgs();
        this.validate();
    }

    public void usage(){

    }

    public void version(){

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
