/**
 *
 */
package runtime.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import runtime.compiler.*;
import runtime.elements.ConditionMsg;
import runtime.elements.XmlElem;
import runtime.parser.*;
import runtime.visitors.CollectReferencedConditionsVisitor;
import runtime.visitors.CollectReferencedDpmsVisitor;
import runtime.visitors.CollectReferencedLookupsVisitor;
import runtime.visitors.LookupNodeVisitor;
import runtime.visitors.XmlVisitor;


/**
 * @author killer
 *
 */
public class CompileMgr {
    static CompilerParameters   config  = null;

    CompilerContext     compilerContext;
    CompilerParameters  params      = null;
    ParseMgr            parseMgr    = null;

    /**
     * Constructor
     *
     */
    public CompileMgr() {
        compilerContext = new CompilerContext();
        parseMgr = new ParseMgr();
    }

    /**
     * getConfig
     * Returns the configuration (command line parameters) that the compiler was called with.
     * @return CompilerParameters
     */
    static public CompilerParameters getConfig(){
        if(null == CompileMgr.config){
            CompileMgr.config = new CompilerParameters();
        }
        return CompileMgr.config;
    }

    /**
     * setConfig
     * Set the configuration object.
     * @param cfg parameters object. Must not be null.
     */
    static private void setConfig(CompilerParameters cfg){
        if(null != cfg){
            CompileMgr.config = cfg;
        }
    }

    /**
     * execute execute the parser/compiler
     * @param cp CompileParameters object containing configuration options for the compiler
     */
    public void execute(CompilerParameters cp) {
        // Set the global configuration
        CompileMgr.setConfig(cp);

        String              srcFile     = CompileMgr.config.inFile;
        ASTCompilationUnit  parseTree   = null;

        // Store include dirs
        for(String inc : CompileMgr.config.incDirs){
            compilerContext.addIncludeDir(inc);
        }

        compilerContext.setConfigDirs(cp.configDirs);

        Log.setVerbose(CompileMgr.config.verbose);
        Log.setStatusPrefix("GDLC: ");
        Log.setErrorPrefix("*** ");

        // Parse source code
        parseMgr.configureDefaultPlugins();
        parseMgr.execute(srcFile, compilerContext);

        if(null == (parseTree = parseMgr.getParseTree())) {
            Log.error("Aborting compile.");
            return;
        }

        if(CompileMgr.config.verboseParse){
            parseMgr.dumpParser();
        }

        // Store the abstract source tree...
        compilerContext.setRootNode(parseTree);
        // and compile the tree.
        compile(compilerContext, parseTree);

        // Dump error and warning info.
        if(compilerContext.hasErrors()){
            Log.error("Compile aborted with errors:");
            Log.out(compilerContext.dumpErrors());
        }

        if(compilerContext.hasWarnings()){
            Log.error("Compile completed with warnings:");
            compilerContext.dumpWarnings();
        }

        if(!compilerContext.hasErrors() && !compilerContext.hasWarnings()){
            Log.out("Compile completed.");
        }

        if(compilerContext.hasErrors()){
            return;
        }

        if(CompileMgr.config.generateOutput){
            generateOutput();
        }else{
            Log.out("No output generated (-nooutput switch)");
        }
    }

    /**
     * generateOutput generates compiled output data
     *
     */
    protected void generateOutput(){
        // Determine name of output file
        String output = new String();
        if(CompileMgr.config.outFile.length() < 1){
            ASTGuidelineDef gdl = compilerContext.getGuideline();
            if(null == gdl){
                Log.out("No guideline definitions exist to write. Use '-raw' flag to force output.");
                return;
            }

            output = gdl.getName();
        }
        else{
            output = CompileMgr.config.outFile;
        }

        if(output.length() < 1){
            Log.error("Unable to resolve an output filename.");
            return;
        }
        // Output file might not have an extension.
        // Add extension if it doesn't.
        if(!output.endsWith(".xml")){
            // Add XML extension to filename.
            output = output.concat(".xml");
        }

        // Write file to destination.

        Log.out("########################################");
        if(this.writeXmlToFile(output)){
            Log.out("XML written to file [" + output + "].");
        }
        else{
            Log.error("Errors occurred while writing XML [" + output + "].");
        }
        Log.out("########################################");
    }

    public ASTCompilationUnit getParseTree(){
        return compilerContext.getRootNode();
    }

    public CompilerContext getContext(){
        return compilerContext;
    }

    private void compile(IProgramContext ctx, ASTCompilationUnit tree) {
        Log.status("Compiling...");

        GdlCompiler compiler = new GdlCompiler();
        compiler.configureDefaultPlugins();

        try {
            compiler.compile(ctx, tree);

        } catch (CompileException e) {
                Log.error("Encountered errors during compilation.");
                Log.error(e.toString());
                return;
        }
    }

    public String getRuleXml(String key){
        ASTRuleDef rule = compilerContext.getRule(key);
        if(null == rule){ return new String(""); }

        // Create XmlVisitor
        XmlVisitor xmlVisitor = new XmlVisitor(this.getContext());
        XmlElem result = new XmlElem("Result");

        // Run the visitor against the rule.
        rule.jjtAccept(xmlVisitor, result);

        return result.getContent();
    }

    public String getRulesetXml(String key){
        IRuleset rs = compilerContext.getRuleset(key);
        if(null == rs){ return new String(""); }

        // Create XmlVisitor
        XmlVisitor xmlVisitor = new XmlVisitor(this.getContext());
        XmlElem result = new XmlElem("Result");

        // Run the visitor against the rule.
        rs.getNode().jjtAccept(xmlVisitor, result);

        return result.getContent();
    }

    public String getGuidelineXml(){
        ASTGuidelineDef gdl = compilerContext.getGuideline();
        if(null == gdl){ return new String(""); }

        // Create XmlVisitor
        XmlVisitor xmlVisitor = new XmlVisitor(this.getContext());
        XmlElem result = new XmlElem("Result");

        // Run the visitor against the rule.
        gdl.jjtAccept(xmlVisitor, result);

        return result.getContent();
    }

    public void writeXml(Writer out) throws IOException {
        ASTGuidelineDef gdl = compilerContext.getGuideline();
        if(null == gdl){
            Log.error("No guideline has been defined.");
            return;
        }

        // Create XmlVisitor
        XmlVisitor xmlVisitor = new XmlVisitor(this.getContext());

        XmlElem gdlRoot = new XmlElem("GuidelineRoot");

        // Run the visitor against the guideline node.
        gdl.jjtAccept(xmlVisitor, gdlRoot);

        // Build a list of lookup names referenced in the guideline definition.
        CollectReferencedLookupsVisitor lookupList = new CollectReferencedLookupsVisitor(compilerContext);
        gdl.jjtAccept(lookupList, null);

        XmlElem lookupData = buildLookupsElement(lookupList.lookups);
        gdlRoot.appendXml(lookupData.toXml());

        // Build a list of Condition names referenced in the guideline definition.
        CollectReferencedConditionsVisitor condList = new CollectReferencedConditionsVisitor(compilerContext);
        gdl.jjtAccept(condList, null);

        // Build the Conditions element.
        XmlElem condData = buildConditionsElement(condList.conditions);
        gdlRoot.appendXml(condData.toXml());

        // Build a list of DPM names referenced in the guideline definition.
        CollectReferencedDpmsVisitor dpmList = new CollectReferencedDpmsVisitor(compilerContext);
        gdl.jjtAccept(dpmList, null);

        // Build the DERIVEDPARAMETERS element.
        XmlElem dpmData = buildDerivedParametersElement(dpmList.dpms);
        gdlRoot.appendXml(dpmData.toXml());

        String content = gdlRoot.toXml();
        out.write(content);
        out.close();
    }

    protected XmlElem buildLookupsElement(HashMap<String,String> list){
        XmlElem lookupData = new XmlElem("LOOKUPS");


        for(String lkName : list.keySet()){
            buildLookup(lookupData, lkName);
        }

        return lookupData;
    }

    protected void buildLookup(XmlElem parent, String lkName){
        ASTLookupDef node = compilerContext.getLookup(lkName);
        LookupNodeVisitor lkupVisitor = new LookupNodeVisitor(compilerContext);

        node.jjtAccept(lkupVisitor, parent);
    }

    protected XmlElem buildConditionsElement(HashMap<String,String> list){
        XmlElem condData = new XmlElem("Conditions");

        for(String condName : list.keySet()){
            XmlElem condElem = null;
            ConditionMsg cond = compilerContext.getCondition(condName);
            condElem = (XmlElem)cond.buildXmlDefElement();
            condData.appendXml(condElem.toXml());
        }

        return condData;
    }

    protected XmlElem buildDerivedParametersElement(HashMap<String,String> list){
        XmlElem dpmData = new XmlElem("DERIVEDPARAMETERS");

        for(String dpmName : list.keySet()){
            buildDpm(dpmData, dpmName);
        }

        return dpmData;
    }

    protected void buildDpm(XmlElem parent, String dpmName){
        XmlElem me = null;

        if(compilerContext.containsVar(new VarDpm(dpmName))){
            VarDpm dpm = (VarDpm)compilerContext.getVar(new VarDpm(dpmName));
            me = new XmlElem(dpm.getVarType());
            me.setIsShortTag(true);

            me.putAttribute("Name", dpm.getAlias());
            me.putAttribute("Type", dpm.getType());
            me.putAttribute("Order", dpm.getOrder());
            String pt = dpm.getProductType();
            me.putAttribute("ProductType", pt);
            if(pt.equals("4")){
                me.putAttribute("Precision", dpm.getPrecision());
            }
            me.putAttribute("DataType", dpm.getDataType());

            String[] attOrderWithPrecision = {"Name", "Type", "Order", "ProductType", "Precision", "DataType"};
            String[] attOrder = {"Name", "Type", "Order", "ProductType", "DataType"};
            if(pt.equals("4") && !pt.isEmpty()){
                me.setAttributeOrder(attOrderWithPrecision);
            }
            else {
                me.setAttributeOrder(attOrder);
            }
        }

        if(null == me){
            Log.error("Missing variable.");
            return;
        }

        parent.appendXml(me.toXml());
    }

    protected boolean writeXmlToFile(String filepath){
        BufferedWriter out = null;
        try{
            out = new BufferedWriter(new FileWriter(filepath));
            this.writeXml(out);
        }
        catch(IOException e){
//              fail("IOException thrown while creating output file [" + outFile + "]");
            return false;
        }

        return true;
    }
}

