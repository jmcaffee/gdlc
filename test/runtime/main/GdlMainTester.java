package runtime.main;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import runtime.compiler.CompilerContext;
import runtime.parser.ASTCompilationUnit;
import runtime.parser.GdlParser;

public class GdlMainTester {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMain() {
		GdlMain gm = new GdlMain();
		assertNotNull("Construction failed.",gm);
	}

	@Test
	public void testParse() {
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\parseTest.gdl"),};
		String badArgs[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\doesnotexist.gdl"),};

		GdlMain gm = new GdlMain();
		assertNull("Parse should have failed.",gm.parse(badArgs));

		assertNotNull("Parse failed.",gm.parse(args));

	}

	@Test
	public void testCompile() {
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\compileTest.gdl"),};

		GdlMain gm = new GdlMain();
		ASTCompilationUnit tree = gm.parse(args);
		
		assertNotNull("Parse failed.",tree);

		CompilerContext ctx = new CompilerContext();
		gm.compile(tree, ctx);
		
		gm.writeErrors(ctx);
		assertFalse("CContext should not have errors.",ctx.hasErrors());
		

	}

	@Test
	public void testGetParseTree() {
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\compileTest.gdl"),};

		GdlMain gm = new GdlMain();
		ASTCompilationUnit tree = gm.parse(args);
		
		assertNotNull("Parse failed.",tree);
		ASTCompilationUnit storedParseTree = gm.getParseTree();
		
		assertNotNull("Stored parse tree is null", storedParseTree);
		assertEquals("Returned parseTree and stored parseTree do not match.",tree, storedParseTree);
		
	}

	@Test
	public void testCompileVarDef() {
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\varDefTest.gdl"),};

		GdlMain gm = new GdlMain();
		ASTCompilationUnit tree = gm.parse(args);
		
		assertNotNull("Parse failed.",tree);

		CompilerContext ctx = new CompilerContext();
		gm.compile(tree, ctx);
		
		gm.writeErrors(ctx);
		assertFalse("CContext should not have errors.",ctx.hasErrors());
		
		assertTrue("testDpm1 variable was not defined.",ctx.containsDpmVar("testDpm1"));
		assertTrue("testDpm2 variable was not defined.",ctx.containsDpmVar("testDpm2"));
		assertFalse("testDpm3 variable is defined.",ctx.containsDpmVar("testDpm3"));

	}

	@Test
	public void testCompileMissingVarDef() {
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\missingVarDefTest.gdl"),};

		GdlMain gm = new GdlMain();
		ASTCompilationUnit tree = gm.parse(args);
		
		assertNotNull("Parse failed.",tree);

		CompilerContext ctx = new CompilerContext();
		gm.compile(tree, ctx);
		
		assertTrue("CContext should have errors.",ctx.hasErrors());
		gm.writeErrors(ctx);
		
	}

	@Test
	public void testCompileMissingRuleDef() {
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\missingRuleDefTest.gdl"),};

		GdlMain gm = new GdlMain();
		ASTCompilationUnit tree = gm.parse(args);
		
		assertNotNull("Parse failed.",tree);

		CompilerContext ctx = new CompilerContext();
		gm.compile(tree, ctx);
		
		assertTrue("CContext should have errors.",ctx.hasErrors());
		gm.writeErrors(ctx);
		
	}

	@Test
	public void testCompileMissingRulesetDef() {
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\missingRulesetDefTest.gdl"),};

		GdlMain gm = new GdlMain();
		ASTCompilationUnit tree = gm.parse(args);
		
		assertNotNull("Parse failed.",tree);

		CompilerContext ctx = new CompilerContext();
		gm.compile(tree, ctx);
		
		assertTrue("CContext should have errors.",ctx.hasErrors());
		gm.writeErrors(ctx);
		
	}

	@Test
	public void testCompileDefdRule() {
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\ruleDefTest.gdl"),};

		GdlMain gm = new GdlMain();
		ASTCompilationUnit tree = gm.parse(args);
		
		assertNotNull("Parse failed.",tree);

		CompilerContext ctx = new CompilerContext();
		gm.compile(tree, ctx);
		
		assertFalse("CContext should not have errors.",ctx.hasErrors());
		gm.writeErrors(ctx);
		
	}

	@Test
	public void testCompileDefdRuleset() {
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\rulesetDefTest.gdl"),};

		GdlMain gm = new GdlMain();
		ASTCompilationUnit tree = gm.parse(args);
		
		assertNotNull("Parse failed.",tree);

		CompilerContext ctx = new CompilerContext();
		gm.compile(tree, ctx);
		
		assertFalse("CContext should not have errors.",ctx.hasErrors());
		gm.writeErrors(ctx);
		
	}

	//@Test
	public void testWriteWarnings() {
		fail("Not yet implemented");
	}

	//@Test
	public void testWriteErrors() {
		fail("Not yet implemented");
	}

	//@Test
	public void testDumpContextData() {
		fail("Not yet implemented");
	}

}
