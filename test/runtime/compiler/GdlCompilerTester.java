/**
 * 
 */
package runtime.compiler;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import runtime.main.GdlMain;
import runtime.parser.ASTCompilationUnit;

/**
 * @author killer
 *
 */
public class GdlCompilerTester {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link runtime.compiler.GdlCompiler#GdlCompiler(runtime.parser.ASTCompilationUnit)}.
	 */
	@Test
	public void testGdlCompiler() {
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\compileTest.gdl"),};

		GdlMain gm = new GdlMain();
		ASTCompilationUnit parseTree = gm.parse(args);
		assertNotNull("Parse failed.",parseTree);

		GdlCompiler compiler = new GdlCompiler(parseTree);
		
		assertNotNull("Compiler instance not created.",compiler);
	}

	/**
	 * Test method for {@link runtime.compiler.GdlCompiler#compile(runtime.compiler.CompilerContext)}.
	 */
	@Test
	public void testCompile() {
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\parseTest.gdl"),};
		String badArgs[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\doesnotexist.gdl"),};

		GdlMain gm = new GdlMain();
		assertNull("Parse should have failed.",gm.parse(badArgs));

		assertNotNull("Parse failed.",gm.parse(args));

		GdlCompiler compiler = new GdlCompiler(gm.getParseTree());
		
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
