package runtime.visitors;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import runtime.compiler.CompilerContext;
import runtime.helpers.TestHelper;

public class ImportVisitorTester extends TestHelper  {
	String				TESTDIR		= "r:/sandbox/net.bd.gdlc/gdl/tests";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
//	public void testImportLookups() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testImportPowerLookups() {
		ImportPowerLookupVisitor visitor = new ImportPowerLookupVisitor();
		CompilerContext ctx = new CompilerContext();
		ctx.addIncludeDir(TESTDIR);
		String filename = "TestPowerLookups.csv";
		
		visitor.importPowerLookups(ctx, filename);
//		assertIfContextHasError(ctx);
//
//		assertTrue("Ruleset not created [TestPowerLookup1].", ctx.containsRuleset("TestPowerLookup1"));
//		assertTrue("Ruleset not created [TestPowerLookup2].", ctx.containsRuleset("TestPowerLookup2"));
//		assertTrue("Ruleset not created [TestPowerLookup3].", ctx.containsRuleset("TestPowerLookup3"));
	}

//	@Test
//	public void testBuildPowerLookups() {
//		fail("Not yet implemented");
//	}

}
