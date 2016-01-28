/**
 * 
 */
package runtime.compiler;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import runtime.helpers.TestHelper;
import runtime.main.CompileMgr;
import runtime.main.CompilerParameters;
import runtime.main.GdlcException;

/**
 * @author killer
 *
 */
public class GdlCompilerTester extends TestHelper {
	CompilerParameters cp = null;

	String				TESTDIR		= "gdl/tests/gdl_compiler";
    String				INCDIR		= "gdl/tests";
	String 				OUTPUTDIR	= "tmp/tests/gdl_compiler";
	String 				EXPECTED	= TESTDIR+"/expected";
	String 				LOOKUPS		= TESTDIR+"/lookups";
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.cp = new CompilerParameters();
        mkCleanDirs(OUTPUTDIR);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.cp = null;
	}

	/**
	 * Test method for {@link runtime.compiler.GdlCompiler#GdlCompiler(runtime.parser.ASTCompilationUnit)}.
	 */
	@Test
	public void testGdlCompiler() throws GdlcException {
		String args[] = {TESTDIR+"/compileTest.gdl",
                         OUTPUTDIR+"/compileTest.xml",
						 "--I"+"gdl",
		};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);

		assertNotNull("Parse failed.",mgr.getParseTree());

		GdlCompiler compiler = new GdlCompiler();
		
		assertNotNull("Compiler instance not created.",compiler);
	}

	/**
	 * Test method for {@link runtime.compiler.GdlCompiler#compile(runtime.compiler.CompilerContext)}.
	 */
	@Test
	public void testCompile() throws GdlcException {
        String args[] = {TESTDIR+"/parseTest.gdl",
                        OUTPUTDIR+"/parseTest.xml",
                        "--I"+"gdl",
                        "--I"+INCDIR,
        };
		String badArgs[] = {new String(TESTDIR+"/doesnotexist.gdl"),};

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
//		assertNull("Parse should have failed.",mgr.parse(badArgs));

//		assertNotNull("Parse failed.",mgr.parse(args));

		GdlCompiler compiler = new GdlCompiler();
		
		assertNotNull("Compiler instance not created.",compiler);
	}

	/**
	 * Test method for {@link runtime.compiler.GdlCompiler#compileIncludes(runtime.compiler.CompilerContext)}.
	 */
	@Test
	public void testCompileIncludes() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link runtime.compiler.GdlCompiler#compileVariables(runtime.compiler.CompilerContext)}.
	 */
	@Test
	public void testCompileVariables() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link runtime.compiler.GdlCompiler#compileRuleDefs(runtime.compiler.CompilerContext)}.
	 */
	@Test
	public void testCompileRuleDefs() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link runtime.compiler.GdlCompiler#compileRulesetDefs(runtime.compiler.CompilerContext)}.
	 */
	@Test
	public void testCompileRulesetDefs() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link runtime.compiler.GdlCompiler#resolveRefs(runtime.compiler.CompilerContext)}.
	 */
	@Test
	public void testResolveRefs() {
		fail("Not yet implemented");
	}

}
