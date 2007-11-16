package runtime.helpers;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import runtime.compiler.IErrorContext;

public class TestHelper {

	public TestHelper() {
		super();
	}

	/**
	 * deleteFileIfExists - helper function - delete a file if it exists
	 * @param filename path of file to delete
	 */
	protected void deleteFileIfExists(String filename) {
		File f=new File(filename);
		if(f.exists()){
			f.delete();
		}
	}

	/**
	 * getFileContents - returns the contents of a text file as a string
	 * @param filename path of file to read
	 */
	protected String getFileContents(String filename) {
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
	protected void assertIfContextHasError(IErrorContext ctx) {
		if(ctx.hasErrors()){
			System.out.print(ctx.dumpErrors());
		}
		assertFalse("Context should not have errors.",ctx.hasErrors());
	}

	/**
	 * assertIfContextErrorNotExist - helper function - throws assertion if context does not have specific error.
	 * @param ctx IErrorContext object
	 */
	protected void assertIfContextErrorNotExist(IErrorContext ctx, String errPart) {
		String errs = null;
		if(ctx.hasErrors()){
			errs = ctx.dumpErrors();
			assertTrue("'" + errPart + "' is not in returned errors. ", (-1 != errs.indexOf(errPart)));
		}
		assertTrue("Context SHOULD have errors.",ctx.hasErrors());
		
		
	}
	
	/**
	 * fileContentsAreIdentical compares two text files and returns false if any errors occur,
	 * either file is not found, or if any line does not match.
	 * @param fname1 filename/path of file to compare
	 * @param fname2 filename/path of 2nd file to compare with
	 * @return true/false
	 */
	protected boolean fileContentsAreIdentical(String fname1, String fname2){
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

}