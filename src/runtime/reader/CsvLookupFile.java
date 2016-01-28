package runtime.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import runtime.compiler.CompileException;
import runtime.compiler.LookupData;

import com.Ostermiller.util.CSVParser;

public class CsvLookupFile {
    String [][] data = null;

    static int STATE_NONE                           = 0;
    static int STATE_KEYWORD_LOOKUP                 = 1;
    static int STATE_NAME                           = 2;
    static int STATE_KEYWORD_PARAMS                 = 3;
    static int STATE_PARAMS                         = 4;
    static int STATE_KEYWORD_PARAM_TYPES            = 5;
    static int STATE_Y_MINS                         = 6;
    static int STATE_Y_MAXS                         = 7;
    static int STATE_DATA                           = 8;

    ArrayList<String> errors = new ArrayList<>();
    String lookupFile;

/**
 * parse
 * @param filepath
 * @throws FileNotFoundException
 * @throws IOException
 */     
        public void parse(String filepath) throws FileNotFoundException, IOException {
                BufferedReader in = new BufferedReader(new FileReader(filepath));
                data = CSVParser.parse(in);
        lookupFile = filepath;
        }

    void addError(String errMsg) {
        errors.add("["+lookupFile+"] " + errMsg);
    }

    String getErrors() {
        StringBuilder errs = new StringBuilder();
        for (String err : errors) {
           errs.append(err + "\n");
        }
        return errs.toString();
    }

/**
 * extractLookupsTo process the data and extract the lookup info
 * @param map
 */     
        public Map extractLookupsTo(Map map) throws CompileException {
                LookupData lkData = null;
        int currentState = STATE_NONE;
        int rowIndex = 0;
                boolean colMinsRead = false;
                boolean colMaxsRead = false;
                
                for(String[] row : data){
            rowIndex++;

                        if(isLookupTag(row)){                   // It is a new lookup.
                currentState = STATE_KEYWORD_LOOKUP;

                                if(null != lkData){                     // If lkData already points to something, 
                                        map.put(lkData.name, lkData);   // store the previous object
                                        lkData = null;                  // and blow away the previous object.
                                        colMinsRead = false;    // Reset state flags.
                                        colMaxsRead = false;
                                }
                                
                                lkData = new LookupData();      // Create a new lookup data object.
                currentState = STATE_NAME;
                                continue;
                        }
                        
                        if(currentState == STATE_NAME) {
                if((lkData.name.length() == 0) &&
                                (isLookupName(row))){        // If it is the lookup name row,
                    lkData.name = row[1];        // store the name;
                    currentState = STATE_KEYWORD_PARAMS;
                    continue;
                }
                else {
                    addError("Missing expected Lookup name. [" + rowIndex + "]");
                }
                        }

            if (currentState == STATE_KEYWORD_PARAMS) {
                if (isKeywordParams(row)) {
                    currentState = STATE_PARAMS;
                    continue;
                } else {
                    addError("Missing Xparameter/Yparameter keyword(s). [" + rowIndex + "]");
                }
            }

            if (currentState == STATE_PARAMS) {
                if (isParamNames(row)) {
                    currentState = STATE_KEYWORD_PARAM_TYPES;
                    lkData.xParameter = xParameterFromRow(row);
                    lkData.yParameter = yParameterFromRow(row);
                    continue;
                } else {
                    addError("Missing Xparameter/Yparameter name(s). [" + rowIndex + "]");
                }
            }

            if (currentState == STATE_KEYWORD_PARAM_TYPES) {
                if (isParamTypes(row)) {
                    currentState = STATE_Y_MINS;
                    lkData.xParameterType = xParameterTypeFromRow(row);
                    lkData.yParameterType = yParameterTypeFromRow(row);
                    continue;
                } else {
                    addError("Missing Xparameter/Yparameter type(s). [" + rowIndex + "]");
                }
            }

            if (currentState == STATE_Y_MINS) {
                addColumnMins(lkData, row);     // read it.
                currentState = STATE_Y_MAXS;
                continue;
            }

            if (currentState == STATE_Y_MAXS) {
                addColumnMaxs(lkData, row);     // read it.
                currentState = STATE_DATA;
                continue;
            }

                        if (currentState == STATE_DATA) {                               // If we are here and the data object exists,
                                addRowValues(lkData, row);      // read in the data (that's all that is left).
                                continue;
                        }

                        throw new CompileException("Invalid lookup format.\n" + getErrors());
                }
                
                if(null != lkData){
                        map.put(lkData.name, lkData);   // Store the data in the supplied Map.
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
                
                if(row[0].equalsIgnoreCase("LOOKUP")){
                        result = true;
                }
                
                return result;
        }

    /**
     * isKeywordParams
     * @param row
     * @return
     */
    protected boolean isKeywordParams(String[] row){
        boolean result = false;

        if(row[2].equalsIgnoreCase("Xparameter")){
            if (row[3].equalsIgnoreCase("Yparameter")) {
                result = true;
            }
        }

        return result;
    }

    /**
     * isParamNames
     * @param row
     * @return
     */
    protected boolean isParamNames(String[] row){
        boolean result = false;

        if(!row[2].isEmpty()){
            if (!row[3].isEmpty()) {
                result = true;
            }
        }

        return result;
    }

    /**
     * xParameterFromRow
     * @param row
     * @return
     */
    protected String xParameterFromRow(String[] row){
        return row[2];
    }

    /**
     * yParameterFromRow
     * @param row
     * @return
     */
    protected String yParameterFromRow(String[] row){
        return row[3];
    }

    /**
     * isParamTypes
     * @param row
     * @return
     */
    protected boolean isParamTypes(String[] row){
        boolean result = false;

        if(row[2].equalsIgnoreCase("DPM") || row[2].equalsIgnoreCase("PPM")){
            if (row[3].equalsIgnoreCase("DPM") || row[3].equalsIgnoreCase("PPM")) {
                result = true;
            }
        }

        return result;
    }

    /**
     * xParameterTypeFromRow
     * @param row
     * @return
     */
    protected String xParameterTypeFromRow(String[] row){
        return row[2];
    }

    /**
     * yParameterTypeFromRow
     * @param row
     * @return
     */
    protected String yParameterTypeFromRow(String[] row){
        return row[3];
    }

/**
 * isLookupName
 * @param row
 * @return
 */
        protected boolean isLookupName(String[] row){
                boolean result = false;
                int cnt = 0;
                
                for(String col : row){                  // Count how many values exist in the row.
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
                
                for(String col : row){                  // Count how many values exist in the row.
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
                int startIndex = 6;                             // Data should start at index 6.
        for (int i = 0; i < startIndex; i++) {
            if (row[i].length() > 0) {
                addError("Columns 0 - 5 should be blank.");
                throw new CompileException("Invalid column format [yParameter mins]. " + getErrors());
            }
        }

                int index = 0;
                for(String min : row){
                        if(index++ < startIndex){
                                continue;                               // Data should not start yet.
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
        int startIndex = 6;                             // Data should start at index 6.
        for (int i = 0; i < startIndex; i++) {
            if (row[i].length() > 0) {
                addError("Columns 0 - 5 should be blank.");
                throw new CompileException("Invalid column format [yParameter maxs]. " + getErrors());
            }
        }

                int index = 0;
                int minmaxIndex = 0;
                for(String max : row){
                        if(index++ < startIndex){
                                continue;                                       // Data should not start yet.
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
        int startIndex = 4;                             // Data should start at index 4.
        for (int i = 0; i < startIndex; i++) {
            if (row[i].length() > 0) {
                addError("Columns 0 - 3 should be blank.");
                throw new CompileException("Invalid row format [values]. " + getErrors());
            }
        }

                int index = 0;
                String tmpVal;
                for(String value : row){
                        if(index++ < startIndex){
                                continue;                                                       // Data should not start yet.
                        }
                        if(index == (startIndex + 1)){                          // Index 2 = row min.
                                lkData.rowMinMaxs.add(lkData.new MinMax(value, ""));
                                continue;
                        }
                        if(index == (startIndex + 2)){                  // Index 3 = row max.
                                lkData.rowMinMaxs.get(lkData.rowMinMaxs.size()-1).setMax(value);
                                continue;
                        }
                        tmpVal = value.trim();
                        if(tmpVal.length() > 0){                // Don't add the value if it is empty.
                                lkData.values.add(tmpVal);
                        }
                }
                
        }
}
