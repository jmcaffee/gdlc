package runtime.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import runtime.compiler.CompileException;
import runtime.compiler.PowerLookupData;
import runtime.main.Log;

import com.Ostermiller.util.CSVParser;

public class CsvPowerLookupFile {

	String [][] data = null;
	
/**
 * parse
 * @param filepath
 * @throws FileNotFoundException
 * @throws IOException
 */	
	public void parse(String filepath) throws FileNotFoundException, IOException {
		BufferedReader in = new BufferedReader(new FileReader(filepath));
		data = CSVParser.parse(in);
	}
	
/**
 * extractLookupsTo process the data and extract the lookup info
 * @param map
 */	
	public Map extractLookupsTo(Map map) throws CompileException {
		PowerLookupData lkData 	= null;
		boolean varIdsRead 		= false;
		boolean varTypesRead 	= false;
		boolean	actionsRead		= false;
		
		for(String[] row : data){			
			if(isLookupTag(row)){			// It is a new lookup.
				if(null != lkData){			// If lkData already points to something, 
					map.put(lkData.name, lkData);	// store the previous object
					lkData = null;			// and blow away the previous object.
					varIdsRead = false;	// Reset state flags.
					varTypesRead = false;
					actionsRead = false;
				}
				
				lkData = new PowerLookupData();	// Create a new lookup data object.
				continue;
			}
			
			if((null != lkData) && 
				(lkData.name.length() == 0) &&
				(isLookupName(row))){		// If it is the lookup name row, 
				lkData.name = row[1];		// store the name;
				continue;
			}
			
//	 This will never fire. Blank lines are not returned as part of the data.
//	 This funcionality has been moved to the first check (above).			
//				if((null != lkData) && isBlankRow(row)){
//					map.put(lkData.name, lkData);
//					lkData = null;
//					varIdsRead = false;
//					varTypesRead = false;
//					continue;
//				}
											// If the variable IDs row hasn't been read,
			if((null != lkData) && (false == varIdsRead)){
				addVarIds(lkData, row);	// read the ID row.
				varIdsRead = true;
				continue;
			}
			
											// If the variable types row hasn't been read,
			if((null != lkData) && 
					(false == varTypesRead) && 
					(true == varIdsRead)){
				addVarTypes(lkData, row);	// read the var type row.
				varTypesRead = true;
				continue;
			}

											// If the action row hasn't been read,
			if((null != lkData) && 
					(false == actionsRead) && 
					(true == varTypesRead) && 
					(true == varIdsRead)){
				addActions(lkData, row);	// read the action row.
				actionsRead = true;
				continue;
			}

			if(null != lkData){				// If we are here and the data object exists,
				addRuleValues(lkData, row);	// read in the data (that's all that is left).
				continue;
			}

			throw new CompileException("Invalid lookup format.");
		}
		
		if(null != lkData){					// If there is an object from the last read,
			map.put(lkData.name, lkData);	// store the data in the supplied Map.
		}
		
		return map;
	}

/**
 * isLookupTag
 * @param row
 * @return
 */	
	protected boolean isLookupTag(String[] row){
		if(row[0].equals("PowerLookup")){
			return true;
		}
		
		return false;
	}
	
/**
 * isLookupName
 * @param row
 * @return
 */
	protected boolean isLookupName(String[] row){
		int cnt = 0;
		
		for(String col : row){			// Count how many values exist in the row.
			if(col.length() > 0){
				cnt++;
			}
		}
										// If this is a 'name' row, only 1 value will exist
										// and it will be index 1.
		if((1 == cnt) && (row[1].length() > 0)){
			return true;				
		}
		
		return false;
	}
	
/**
 * isBlankRow
 * @param row
 * @return
 */
	protected boolean isBlankRow(String[] row){
		boolean result = false;
		int cnt = 0;
		
		for(String col : row){			// Count how many values exist in the row.
			if(col.length() > 0){
				cnt++;
			}
		}
										// If this is a blank row, no values will exist.
		if(0 == cnt){
			result = true;				
		}
		
		return result;
	}
	
/**
 * isDataRow returns true if the first 2 columns are blank and the third one has data.
 * @param row
 * @return true/false
 */
	protected boolean isDataRow(String[] row){
		if(row.length < 3){					// Need at least 3 columns to have one data item.
			return false;
		}
											// First 2 columns must be blank.
		if((row[0].length() > 0) || (row[1].length() > 0)){
			return false;
		}
											// Third column must have data in it.
		if(row[2].length() < 1){
			return false;
		}

		return true;
	}
	
/**
 * dumpData
 *
 */
	public void dumpData(){
		if(null == data){
			Log.info("CsvPowerLookupFile: No data to dump.");
			return;
		}
		
		StringBuffer rowInfo = new StringBuffer();
		for(String[] row : data){
			for(String col : row){
				rowInfo.append(col + "\t");
			}
			Log.info(rowInfo.toString());
			rowInfo.setLength(0);
			//Log.info("");
		}
		
		Log.info("<<<EOD>>>");

	}
	
/**
 * addVarIds
 * @param lkData
 * @param row
 */	
	public void addVarIds(PowerLookupData lkData, String[] row) throws CompileException{
		if(!isDataRow(row)){
			throw new CompileException("Invalid column format [Variable IDs].");
		}
		
		int startIndex = 2;				// Data should start at index 2.
		int index = 0;
		for(String name : row){
			if(index++ < startIndex){
				continue;				// Data should not start yet.
			}
			lkData.operations.add(lkData.new VarOp(name));
		}
	}

/**
 * addVarTypes
 * @param lkData
 * @param row
 */	
	public void addVarTypes(PowerLookupData lkData, String[] row) throws CompileException {
		if(!isDataRow(row)){
			throw new CompileException("Invalid column format [Variable Types].");
		}
		
		int startIndex = 2;					// Data should start at index 2.
		int colIndex = 0;
		int index = 0;
		for(String typ : row){
			if(colIndex++ < startIndex){
				continue;					// Data should not start yet.
			}
			lkData.operations.get(index++).setType(typ);
		}
	}

/**
 * addActions
 * @param lkData
 * @param row
 */	
	public void addActions(PowerLookupData lkData, String[] row) throws CompileException {
		if(!isDataRow(row)){
			throw new CompileException("Invalid column format [Operations].");
		}
		
		int startIndex = 2;					// Data should start at index 2.
		int colIndex = 0;
		int index = 0;
		for(String op : row){
			if(colIndex++ < startIndex){
				continue;					// Data should not start yet.
			}
			lkData.operations.get(index++).setOp(op);
		}
	}

/**
 * addRuleValues
 * @param lkData
 * @param row
 */	
	public void addRuleValues(PowerLookupData lkData, String[] row)throws CompileException {
		if(!isDataRow(row)){
			throw new CompileException("Invalid column format [Rule values].");
		}
		
		int startIndex = 2;							// Data should start at index 2.
		int colIndex = 0;
		ArrayList<String> data = new ArrayList<String>();
		
		for(String value : row){
			if(colIndex++ < startIndex){
				continue;							// Data should not start yet.
			}
			data.add(value.trim());
		}
		lkData.values.add(data);
	}

	

}
