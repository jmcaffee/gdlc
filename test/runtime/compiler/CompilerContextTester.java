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
import runtime.main.GdlcException;

/**
 * @author killer
 *
 */
public class CompilerContextTester {
	CompilerParameters cp = null;

	String				TESTDIR		= "gdl/tests/compiler_context";
    String				INCDIR		= "gdl/tests";
	String 				OUTPUTDIR	= TESTDIR+"/output";
	String 				EXPECTED	= TESTDIR+"/expected";
	String 				LOOKUPS		= TESTDIR+"/lookups";
	
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
	public void testIncludeDirArgs() throws GdlcException {
		String incDir1 = "--I"+TESTDIR;
		String args[] = {TESTDIR+"/compileTest.gdl",
						incDir1,
                        "--I"+INCDIR,
						"-nooutput",
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);

		assertNotNull("Context is null.",mgr.getContext());
		CompilerContext ctx = (CompilerContext)mgr.getContext();
		
		assertTrue("Context should contain at least 1 include dir", ctx.includeDirs.size() > 0 );
	}

	@Test
	public void testConfigDirArgs() throws GdlcException {
		String incDir1 = "--I"+TESTDIR;
		String configDir1 = "--C"+TESTDIR;
		String configDir2 = "--C"+EXPECTED;
		String args[] = {TESTDIR+"/compileTest.gdl",
						incDir1,
                        "--I"+INCDIR,
						configDir1, 
						configDir2,
						"-nooutput",
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Context is null.",mgr.getContext());
		CompilerContext ctx = (CompilerContext)mgr.getContext();
		
		int dirCount = 0;
		for(String path : ctx.configDirs){
			if(path.equals(configDir1.substring(3)))
				dirCount++;
			if(path.equals(configDir2.substring(3)))
				dirCount++;
		}
		
		assertEquals("Expected config dirs not found", 2, dirCount);
		assertEquals("More config dirs than expected found", 2, ctx.configDirs.size());
		
	}


	@Test
	public void testFindPropertyFilesNamed() throws GdlcException {
		String incDir1 = "--I"+TESTDIR;
		String configDir1 = "--C"+TESTDIR;
		String configDir2 = "--C"+EXPECTED;
		String args[] = {TESTDIR+"/compileTest.gdl",
						"-nooutput",
						incDir1,
                        "--I"+INCDIR,
						configDir1, 
						configDir2,
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Context is null.",mgr.getContext());
		CompilerContext ctx = (CompilerContext)mgr.getContext();
	
		ArrayList<String> filesFound = ctx.findPropertyFilesNamed("test.properties");
		assertEquals("Expected to find 2 test.properties files", 2, filesFound.size());
		
		for (String path : filesFound) {
			assertTrue(path.endsWith(TESTDIR+"/test.properties") || path.endsWith(EXPECTED+"/test.properties"));
		}

	}

	@Test
	public void testFindPropertyFilesNamedWithRelativeDirs() throws GdlcException {
		String incDir1 = "--I"+TESTDIR;
		String configDir1 = "--C"+TESTDIR;
		String configDir2 = "--C"+EXPECTED;
		String args[] = {TESTDIR+"/compileTest.gdl",
						"-nooutput",
						incDir1,
                        "--I"+INCDIR,
						configDir1, 
						configDir2,
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Context is null.",mgr.getContext());
		CompilerContext ctx = (CompilerContext)mgr.getContext();
	
		ArrayList<String> filesFound = ctx.findPropertyFilesNamed("test.properties");
		assertEquals("Expected to find 2 test.properties files", 2, filesFound.size());
		
		for (String path : filesFound) {
			assertTrue(path.endsWith(TESTDIR+"/test.properties") || path.endsWith(EXPECTED+"/test.properties"));
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
