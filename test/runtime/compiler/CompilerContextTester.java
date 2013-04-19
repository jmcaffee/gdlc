/**
 * 
 */
package runtime.compiler;


import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import runtime.main.CompileMgr;
import runtime.main.CompilerParameters;

/**
 * @author killer
 *
 */
public class CompilerContextTester {
	CompilerParameters cp = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.cp = new CompilerParameters();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.cp = null;
	}

	@Test
	public void testIncludeDirArgs() {
		String incDir1 = "/Ic:/Users/Jeff/projects/java/gdlc/gdl/tests";
		String args[] = {new String("c:\\Users\\Jeff\\projects\\java\\gdlc\\gdl\\tests\\compileTest.gdl"),
						incDir1,};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Context is null.",mgr.getContext());
		CompilerContext ctx = (CompilerContext)mgr.getContext();
		
		int dirCount = 0;
		for(String path : ctx.includeDirs){
			if(path.equals(incDir1.substring(2).replace('/', '\\')))
				dirCount++;
		}
		
		assertEquals("Expected dirs not found", 1, dirCount);
	}

	@Test
	public void testConfigDirArgs() {
		String incDir1 = "/Ic:/Users/Jeff/projects/java/gdlc/gdl";
		String configDir1 = "/Cc:/Users/Jeff/projects/java/gdlc/gdl/tests";
		String configDir2 = "/Cc:/Users/Jeff/projects/java/gdlc/gdl/tests/expected";
		String args[] = {new String("c:\\Users\\Jeff\\projects\\java\\gdlc\\gdl\\tests\\compileTest.gdl"),
						incDir1, 
						configDir1, 
						configDir2,};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Context is null.",mgr.getContext());
		CompilerContext ctx = (CompilerContext)mgr.getContext();
		
		int dirCount = 0;
		for(String path : ctx.configDirs){
			if(path.equals(configDir1.substring(2)))
				dirCount++;
			if(path.equals(configDir2.substring(2)))
				dirCount++;
		}
		
		assertEquals("Expected config dirs not found", 2, dirCount);
		assertEquals("More config dirs than expected found", 2, ctx.configDirs.size());
		
	}


	@Test
	public void testFindPropertyFilesNamed() {
		String incDir1 = "/Ic:/Users/Jeff/projects/java/gdlc/gdl";
		String configDir1 = "/Cc:/Users/Jeff/projects/java/gdlc/gdl/tests";
		String configDir2 = "/Cc:/Users/Jeff/projects/java/gdlc/gdl/tests/expected";
		String args[] = {new String("c:/Users/Jeff/projects/java/gdlc/gdl/tests/compileTest.gdl"),
						new String("-nooutput"),
						incDir1, 
						configDir1, 
						configDir2,};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Context is null.",mgr.getContext());
		CompilerContext ctx = (CompilerContext)mgr.getContext();
	
		ArrayList<String> filesFound = ctx.findPropertyFilesNamed("test.properties");
		assertEquals("Expected to find 2 test.properties files", 2, filesFound.size());
		
		for (String path : filesFound) {
			assertTrue(path.endsWith("gdl/tests/test.properties") || path.endsWith("gdl/tests/expected/test.properties"));
		}

	}

	@Test
	public void testFindPropertyFilesNamedWithRelativeDirs() {
		String incDir1 = "/Igdl";
		String configDir1 = "/Cgdl/tests";
		String configDir2 = "/Cgdl/tests/expected";
		String args[] = {new String("gdl/tests/compileTest.gdl"),
						new String("-nooutput"),
						incDir1, 
						configDir1, 
						configDir2,};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Context is null.",mgr.getContext());
		CompilerContext ctx = (CompilerContext)mgr.getContext();
	
		ArrayList<String> filesFound = ctx.findPropertyFilesNamed("test.properties");
		assertEquals("Expected to find 2 test.properties files", 2, filesFound.size());
		
		for (String path : filesFound) {
			assertTrue(path.endsWith("gdl/tests/test.properties") || path.endsWith("gdl/tests/expected/test.properties"));
		}

	}

	@Test
	public void testJoinPath() {
		String posixPath 	= "/this/is/a/posix/path";
		String winPath 		= "\\this\\is\\a\\windows\\path";
		String filename 	= "testfile.txt";
		
		CompilerContext ctx = new CompilerContext();
		
		assertEquals("joinPath should handle posix path separators", "/this/is/a/posix/path/testfile.txt", ctx.joinPath(posixPath, filename));
		
		assertEquals("joinPath should handle windows path separators", "\\this\\is\\a\\windows\\path\\testfile.txt", ctx.joinPath(winPath, filename));
	
	}

	@Test
	public void testRetrievingConditionCategoryIdWhenNoneExist() {
		CompilerContext ctx = new CompilerContext();
		
		assertEquals("CompilerContext should return -1 when condition category key not found", -1, ctx.getConditionCategoryId("NotExist"));
	}

	@Test
	public void testRetrievingConditionPriorToIdWhenNoneExist() {
		CompilerContext ctx = new CompilerContext();
		
		assertEquals("CompilerContext should return -1 when condition priorto key not found", -1, ctx.getConditionPriorToId("NotExist"));
	}

}
