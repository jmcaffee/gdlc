package runtime.helpers;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import runtime.compiler.IErrorContext;

public class TestHelper {

	public String rootDir;
	
	public TestHelper() {
		super();
		rootDir = "c:/Users/Jeff/projects/java/gdlc";
	}

	/**
	 * deleteFileIfExists - helper function - delete a file if it exists
	 * @param filename path of file to delete
	 */
	public static void deleteFileIfExists(String filename) {
		File f=new File(filename);
		if(f.exists()){
			f.delete();
		}
	}

	/**
	 * getFileContents - returns the contents of a text file as a string
	 * @param filename path of file to read
	 */
	public static String getFileContents(String filename) {
		BufferedReader f1 = null;
		String line;
		StringBuffer contents = new StringBuffer();
		
		try{
			f1 = new BufferedReader(new FileReader(filename));

			while(null != (line = f1.readLine())){
				contents.append(line);
			}
		}
		catch(FileNotFoundException e){
			fail("[getFileContents] File not found: "+e.getMessage()+".");
			return null;
		}
		catch(IOException e){
			fail("[getFileContents] Read exception: "+e.getMessage()+".");
			return null;
		}
		
		return contents.toString();
	}

	/**
	 * assertIfContextHasError - helper function - throws assertion if context has errors.
	 * @param ctx IErrorContext object
	 */
	public static void assertIfContextHasError(IErrorContext ctx) {
		if(ctx.hasErrors()){
			System.out.print(ctx.dumpErrors());
		}
		assertFalse("Context should not have errors.",ctx.hasErrors());
	}

	/**
	 * assertIfContextErrorNotExist - helper function - throws assertion if context does not have specific error.
	 * @param ctx IErrorContext object
	 * @param errPart error text to search context for 
	 */
	public static void assertIfContextErrorNotExist(IErrorContext ctx, String errPart) {
		String errs = null;
		if(ctx.hasErrors()){
			errs = ctx.dumpErrors();
			assertTrue("'" + errPart + "' is not in returned errors. ", (-1 != errs.indexOf(errPart)));
		}
		assertTrue("Context SHOULD have errors.",ctx.hasErrors());
	}
	

	/**
	 * Assert that a section of XML contains an XML partial
	 * @param needle partial to search for
	 * @param haystack XML to search for partial
	 */
	public static void assertXmlContains(String needle, String haystack) {
		if (!haystack.contains(needle)) {
			System.out.print(""+getFileLineLink()+" "+needle+" not found in:\n"+haystack);
			fail();
		}
	}
	
	/**
	 * Generate a link to the file and line number.
	 * This link can be clicked to take the user to the location the method was called from.
	 * 			
	 * @return String in format: (filename.java:linenum)
	 */
	public static String getFileLineLink() {
	    return new String("("+Thread.currentThread().getStackTrace()[3].getFileName()+":"+Integer.toString(Thread.currentThread().getStackTrace()[3].getLineNumber())+")");
	}
	
	/**
	 * fileContains reads a file and searches for a given string, returning true if string is found
	 * or false otherwise.
	 * @param fname1 filename/path of file to search
	 * @param searchFor string to search for
	 * @return true/false
	 */
	public static boolean fileContains(String fname1, String searchFor){
		boolean result = false;
		
		BufferedReader f1 = null;
		String line;
		
		try{
			f1 = new BufferedReader(new FileReader(fname1));

			while(null != (line = f1.readLine())){
				if(line.contains(searchFor)){
					result = true;
					f1.close();
					break;
				}
			}
		}
		catch(FileNotFoundException e){
			System.out.println("[FILECONTAINS = false] File not found: "+e.getMessage()+".");
			return false;
		}
		catch(IOException e){
			System.out.println("[FILECONTAINS = false] Read exception: "+e.getMessage()+".");
			return false;
		}
		
		return result;
	}

	
	/**
	 * fileContentsAreIdentical compares two text files and returns false if any errors occur,
	 * either file is not found, or if any line does not match.
	 * @param fname1 filename/path of file to compare
	 * @param fname2 filename/path of 2nd file to compare with
	 * @return true/false
	 */
	public static boolean fileContentsAreIdentical(String fname1, String fname2){
		boolean result = true;
		
		BufferedReader f1 = null;
		BufferedReader f2 = null;
		String line;
		
		try{
			f1 = new BufferedReader(new FileReader(fname1));
			f2 = new BufferedReader(new FileReader(fname2));

			while(null != (line = f1.readLine())){
				if(!line.equals(f2.readLine())){
					System.out.println("[FILECOMPARE = false] contents do not match.");
					result = false;
					f1.close();
					f2.close();
					break;
				}
				
			}
		}
		catch(FileNotFoundException e){
			System.out.println("[FILECOMPARE = false] File not found: "+e.getMessage()+".");
			return false;
		}
		catch(IOException e){
			System.out.println("[FILECOMPARE = false] Read exception: "+e.getMessage()+".");
			return false;
		}
		
		return result;
	}

	
	/**
	 * fileContentsAreIdenticalWithStrippedDate compares two text 
	 * files and returns false if any errors occur,
	 * either file is not found, or if any line does not match.
	 * Before comparison, the guideline StartDate Attribute is 
	 * stripped from both file's data.
	 * @param fname1 filename/path of file to compare
	 * @param fname2 filename/path of 2nd file to compare with
	 * @return true/false
	 */
	public static boolean fileContentsAreIdenticalWithStrippedDate(String fname1, String fname2){
		boolean result = true;
		
		BufferedReader f1 = null;
		BufferedReader f2 = null;
		String line;
		String otherLine;
		
		try{
			f1 = new BufferedReader(new FileReader(fname1));
			f2 = new BufferedReader(new FileReader(fname2));

			while(null != (line = f1.readLine())){
				otherLine = f2.readLine();
				if(!stripDate(line).equals(stripDate(otherLine))){
					System.out.println("[FILECOMPARE = false] contents do not match.");
					result = false;
					f1.close();
					f2.close();
					break;
				}
				
			}
		}
		catch(FileNotFoundException e){
			System.out.println("[FILECOMPARE = false] File not found: "+e.getMessage()+".");
			return false;
		}
		catch(IOException e){
			System.out.println("[FILECOMPARE = false] Read exception: "+e.getMessage()+".");
			return false;
		}
		
		return result;
	}
	
	
	public static String stripDate(String line){
		String clean = line.replaceFirst("StartDate=\"[a-zA-Z]++\\s\\d{1,2}+\\s\\d{4}+\\s\\d{2}+:\\d{2}+(AM|PM)\"", "StartDate=\"\"");
		return clean;
	}
}