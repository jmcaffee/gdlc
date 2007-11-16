package runtime.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import runtime.compiler.CompileException;
import runtime.compiler.LookupData;

import com.Ostermiller.util.CSVParser;

public class CsvLookupFile {
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
		LookupData lkData = null;
		boolean colMinsRead = false;
		boolean colMaxsRead = false;
		
		for(String[] row : data){			
			if(isLookupTag(row)){			// It is a new lookup.
				if(null != lkData){			// If lkData already points to something, 
					map.put(lkData.name, lkData);	// store the previous object
					lkData = null;			// and blow away the previous object.
					colMinsRead = false;	// Reset state flags.
					colMaxsRead = false;
				}
				
				lkData = new LookupData();	// Create a new lookup data object.
				continue;
			}
			
			if((null != lkData) && 
				(lkData.name.length() == 0) &&
				(isLookupName(row))){		// If it is the lookup name row, 
				lkData.name = row[1];		// store the name;
				continue;
			}
			
// This will never fire. Blank lines are not returned as part of the data.
// This funcionality has been moved to the first check (above).			
//			if((null != lkData) && isBlankRow(row)){
//				map.put(lkData.name, lkData);
//				lkData = null;
//				colMinsRead = false;
//				colMaxsRead = false;
//				continue;
//			}
											// If the 'column mins' row hasn't been read,
			if((null != lkData) && (false == colMinsRead)){
				addColumnMins(lkData, row);	// read it.
				colMinsRead = true;
				continue;
			}
			
											// If the mins are read and the maxes are not,
			if((null != lkData) && (false == colMaxsRead) && (true == colMinsRead)){
				addColumnMaxs(lkData, row);	// read the maxes row.
				colMaxsRead = true;
				continue;
			}

			if(null != lkData){				// If we are here and the data object exists,
				addRowValues(lkData, row);	// read in the data (that's all that is left).
				continue;
			}

			throw new CompileException("Invalid lookup format.");
		}
		
		if(null != lkData){
			map.put(lkData.name, lkData);	// Store the data in the supplied Map.
		}
		
		return map;
	}

/**
 * isLookupTag
 * @param row
 * @return
 */	
	protected boolean isLookupTag(String[] row){
		boolean result = false;
		
		if(row[0].equals("LOOKUP")){
			result = true;
		}
		
		return result;
	}
	
/**
 * isLookupName
 * @param row
 * @return
 */
	protected boolean isLookupName(String[] row){
		boolean result = false;
		int cnt = 0;
		
		for(String col : row){			// Count how many values exist in the row.
			if(col.length() > 0){
				cnt++;
			}
		}
										// If this is a 'name' row, only 1 value will exist
										// and it will be index 1.
		if((1 == cnt) && (row[1].length() > 0)){
			result = true;				
		}
		
		return result;
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
 * dumpData
 *
 */
	public void dumpData(){
		if(null == data){
			System.out.println("CsvLookupFile: No data to dump.");
			return;
		}
			
		for(String[] row : data){
			for(String col : row){
				System.out.print(col + "\t");
			}
			System.out.println();
		}
		
		System.out.println("<<<EOD>>>");

	}
	
/**
 * addColumnMins
 * @param lkData
 * @param row
 */	
	public void addColumnMins(LookupData lkData, String[] row) throws CompileException{
		if((row[0].length() > 0) || (row[1].length() > 0)){
			throw new CompileException("Invalid column format [min].");
		}
		
		int startIndex = 4;				// Data should start at index 4.
		int index = 0;
		for(String min : row){
			if(index++ < startIndex){
				continue;				// Data should not start yet.
			}
			if(min.length() > 0){
				lkData.colMinMaxs.add(lkData.new MinMax(min, ""));
			}
		}
	}

/**
 * addColumnMaxs
 * @param lkData
 * @param row
 */	
	public void addColumnMaxs(LookupData lkData, String[] row) throws CompileException {
		if((row[0].length() > 0) || (row[1].length() > 0)){
			throw new CompileException("Invalid column format [max].");
		}
		
		int startIndex = 4;					// Data should start at index 4.
		int index = 0;
		int minmaxIndex = 0;
		for(String max : row){
			if(index++ < startIndex){
				continue;					// Data should not start yet.
			}
			if(max.length() > 0){
				lkData.colMinMaxs.get(minmaxIndex++).setMax(max);
			}
		}
	}

/**
 * addRowValues
 * @param lkData
 * @param row
 */	
	public void addRowValues(LookupData lkData, String[] row)throws CompileException {
		if((row[0].length() > 0) || (row[1].length() > 0)){
			throw new CompileException("Invalid row format [values].");
		}
		
		int startIndex = 2;							// Data should start at index 2.
		int index = 0;
		String tmpVal;
		for(String value : row){
			if(index++ < startIndex){
				continue;							// Data should not start yet.
			}
			if(index == (startIndex + 1)){				// Index 2 = row min.
				lkData.rowMinMaxs.add(lkData.new MinMax(value, ""));
				continue;
			}
			if(index == (startIndex + 2)){			// Index 3 = row max.
				lkData.rowMinMaxs.get(lkData.rowMinMaxs.size()-1).setMax(value);
				continue;
			}
			tmpVal = value.trim();
			if(tmpVal.length() > 0){		// Don't add the value if it is empty.
				lkData.values.add(tmpVal);
			}
		}
		
	}

	

}
