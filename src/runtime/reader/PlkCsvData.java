package runtime.reader;

import java.util.ArrayList;
//import java.util.Iterator;

//import runtime.compiler.CompilerContext;
import runtime.compiler.PowerLookupData;
//import runtime.compiler.VarDpm;
//import runtime.compiler.VarPpm;
import runtime.compiler.PowerLookupData.VarOp;
import runtime.main.Log;

class PlkCsvData {

	ArrayList<String[]>	data		= new ArrayList<String[]>();
	ArrayList<String>	errors		= new ArrayList<String>();
	
	//String [][] data 		= null;
	boolean		isDirty 	= false;
	boolean		isComplete	= false;
	PowerLookupData	plk		= null;
	
	// Matrix layout values:
	int	plkNameRow		= 0;		// PowerLookup row is skipped.
	int	varNameRow		= plkNameRow + 1;
	int varTypeRow		= varNameRow + 1;
	int varOpRow		= varTypeRow + 1;
	int firstDataRow	= varOpRow + 1;
	
	int dataStartIdx	= -1;
	int dataEndIdx		= -1;


	/**
	 * @param
	 */
	public PlkCsvData() {
	}


	/**
	 * Reset the data and settings.
	 */
	public void reset(){
		this.data 			= new ArrayList<String[]>();
		this.errors			= new ArrayList<String>();
		this.isDirty 		= false;
		this.isComplete 	= false;
		this.plk			= null;
		this.dataStartIdx	= -1;
		this.dataEndIdx		= -1;

	}
	
	
	/**
	 * Add one row of plk data.
	 * @param row
	 */
	public void addRow(String[] row){
		if((isBlank(row) || isPowerLookup(row)) && !isDirty){	// If it is a blank row but we haven't started
			isDirty = true;				// parsing yet, skip it and start parsing.
			return;
		}
		
		if((isBlank(row) || isPowerLookup(row))	&& isDirty){	// If it is a blank row and we're parsing,
			buildPlkData();				// build the plk with the data we have and
			isComplete = true;			// indicate that we are done.
			return;
		}
		
		if(isComplete){
			addError("Attempted to add a row to already completed PLK.");
			return;
		}
		
		isDirty = true;
		data.add(row);
	}
	
	
	/**
	 * Let object know that the end of data has been reached.
	 * This is used when a blank line is *not* sent as the last line of PLK data.
	 * 
	 */
	public void endOfData(){
		if(!isComplete){
			buildPlkData();
			isComplete = true;
		}
	}
	
	
	/**
	 * Return the PowerLookupData object.
	 * This may be undefined if errors exist (+hasErrors+), or parsing is not
	 * completed (+lookupComplete+).
	 * @return PowerLookupData
	 */
	public PowerLookupData getData(){
		return this.plk;
	}
	
	
	/**
	 * Parse the added row data and build the PowerLookupData object.
	 */
	void buildPlkData(){
		if(this.plk != null){
			addError("Overwrite of PLK data requested. Call reset() before adding data.");
			return;
		}
		
		this.plk = new PowerLookupData();
		plk.name 		= getName();
    Log.out("Importing PLK "+plk.name);
		plk.exeType 	= getExeType();
		plk.operations 	= getOperations();
		storeValues(plk);
	}
	
	
	/**
	 * Return the plk name.
	 * @return empty string if name not found
	 */
	public String getName(){
		int col = 2;
		int idx = col - 1;
		if(data.isEmpty() || data.get(plkNameRow).length < col || data.get(plkNameRow)[idx].isEmpty()){
			addError("Name not found.");
			return new String("UnknownPLK");
		}
		return data.get(plkNameRow)[idx];
	}
	

	/**
	 * Return the plk execution type (true, continue).
	 * @return default is 'true' if not specified
	 */
	String getExeType(){
		int col = 4;
		int idx = col - 1;
		if(data.get(plkNameRow).length < col || data.get(plkNameRow)[idx].isEmpty()){
			return new String("true");
		}
		
		if(data.get(plkNameRow)[idx].equalsIgnoreCase("Continue")){
			return new String("continue");
		}
		
		if(data.get(plkNameRow)[idx].equalsIgnoreCase("Stop If True")){
			return new String("true");
		}
		
		addError("Unrecognized execution type: " + data.get(plkNameRow)[idx]);
		return new String("true");
	}
	
	
	/**
	 * Return the *index* (not column) of the first element that is not empty.
	 * @param row
	 * @return -1 if all elements are empty
	 */
	int	getStartIndex(String[] row){
		int idx =  -1;
		
		for (int i = 0; i < row.length; i++) {
			if(row[i].isEmpty()){
				continue;
			}
			idx = i;
			break;
		}
		
		return idx;
	}
	

	/**
	 * Return the *index* (not column) of the last element that is not empty.
	 * @param row
	 * @return -1 if all elements are empty
	 */
	int	getEndIndex(String[] row){
		int idx =  -1;
		
		for (int i = row.length - 1; i > 0; i--) {
			if(row[i].isEmpty()){
				continue;
			}
			idx = i;
			break;
		}
		
		return idx;
	}
	
	
	/**
	 * Return the plk operations.
	 * @return default is 'true' if not specified
	 */
	ArrayList<VarOp> getOperations(){
		int col = 3;
		int idx = col - 1;
		this.dataStartIdx = getStartIndex(data.get(varNameRow));
		this.dataEndIdx   = getEndIndex(data.get(varNameRow));
		int rowWidth = dataEndIdx + 1;
		
		if(rowWidth < col || data.get(varNameRow)[idx].isEmpty() ){
			addError("Invalid variable name row. Must contain at least one variable name.");
		}
		
		if((getEndIndex(data.get(varTypeRow)) + 1) < col){
			addError("Invalid variable type row. Must contain at least one variable type.");
		}
		
		if((getEndIndex(data.get(varOpRow)) + 1) < rowWidth){
			addError("Invalid variable operation row. Must contain an operation for each item in the variable name row.");
		}

		ArrayList<VarOp> vars = new ArrayList<VarOp>();
		for (int i = dataStartIdx; i < rowWidth; i++) {
			VarOp var = this.plk.new VarOp("Unknown");
			var.setName(data.get(varNameRow)[i]);
			var.setType(data.get(varTypeRow)[i]);
			var.setOp( performOpConversions(data.get(varOpRow)[i]) );
			vars.add(var);
		}
		
		return vars;
	}
	
	
	/**
	 * Convert Excel specific operation symbols into the correct GDL equivalent.
	 * Example: (Excel) IN => ??
	 * @param op
	 * @return converted GDL operation symbol
	 */
	String performOpConversions(String op){
		String finalOp = op;
		finalOp = finalOp.replace("IN", "??");
		finalOp = finalOp.replace("Findings", "findings");
		finalOp = finalOp.replace("Exceptions", "exception");
		return new String(finalOp);
		
	}
	
	
	/**
	 * Convert Excel specific value symbols into the correct GDL equivalent.
	 * Example: (pipe) | => (comma),
	 * @param op
	 * @return converted GDL operation symbol
	 */
	String performValueConversions(String val){
		String finalVal = val;
		finalVal = finalVal.replace("|", ",");
		return new String(finalVal);
		
	}
	
	
	/**
	 * Store the rule data in a PowerLookupData object.
	 * @param plkdata
	 * @return true on success
	 */
	
	boolean storeValues(PowerLookupData plk){
		if(dataStartIdx < 0 || dataEndIdx < 0){
			addError("Start OR end index of variable name row not set.");
			return false;
		}
		
		for (int r = firstDataRow; r < data.size(); r++) {
			ArrayList<String> values = new ArrayList<String>();
			for (int i = dataStartIdx; i <= dataEndIdx; i++) {
				values.add( performValueConversions(data.get(r)[i]) );
			}
			plk.values.add(values);
		}
		
		return true;
	}
	
	
	/**
	 * Returns true if all columns are empty.
	 * @return false
	 */
	boolean isBlank(String[] row){
		for (String item : row) {
			if(!item.isEmpty()){
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Returns true if column 1 == 'PowerLookup'
	 * @return false
	 */
	boolean isPowerLookup(String[] row){
		if(!row[0].isEmpty() && row[0].equalsIgnoreCase("POWERLOOKUP")){
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Indicate if a complete lookup is contained within this object.
	 * @return
	 */
	public boolean isLookupComplete(){
		return isComplete;
	}
	
	
	/**
	 * Add an error string to the errors list.
	 * @param errText
	 */
	void addError(String errText){
		errors.add(errText);
	}
	
	
	/**
	 * Returns true if there are any errors pending.
	 * @return false
	 */
	public boolean hasErrors(){
		return (this.errors.size() > 0);
	}
	
	
	/**
	 * Return an array of error strings.
	 * @return
	 */
	public String[] getErrorList(){
		String[] errs = new String[this.errors.size()];
		// Get the length of the error array *before* calling get name 'cause
		// it could add more errors (name doesn't exist). We want to exclude
		// any additional errors.
		int	len = this.errors.size();		
		String plkName = getName();	 
		for (int i = 0; i < len; i++) {
			errs[i] = plkName + ": " + this.errors.get(i);
		}
		return errs;
	}

	
	/**
	 * Return a string containing all errors.
	 * @return
	 */
	public String getErrors(){
		String[] errs = getErrorList();
		
		String err = new String();
		
		for (String e : errs) {
			err += e + " ";
		}
		
		return err;
	}

	
}
