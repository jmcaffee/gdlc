package runtime.visitors;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import runtime.compiler.CompilerContext;
import runtime.compiler.IncludeContext;
import runtime.compiler.PowerLookupData;
import runtime.helpers.TestHelper;
import runtime.parser.ASTInclude;

public class ImportVisitorTester extends TestHelper  {
	String		TESTDIR				= "r:/tools/java/com.ktechsystems.gdlc/gdl/tests";
	String		TEST_INCLUDE_FILE 	= TESTDIR+"/importTest-dpms.gdl";
	
	ImportPowerLookupVisitor 	visitor = null;
	CompilerContext 			ctx 	= null;

	@Before
	public void setUp() throws Exception {
		this.visitor 	= new ImportPowerLookupVisitor();
		this.ctx 		= new CompilerContext();
		this.ctx.addIncludeDir(TESTDIR);
	}

	@After
	public void tearDown() throws Exception {
		this.visitor 	= null;
		this.ctx 		= null;
	}

//	@Test
//	public void testImportLookups() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testImportPowerLookups() {
		String filename = "TestPowerLookups.csv";
		
		visitor.importPowerLookups(ctx, filename);
		
		// Context WILL have errors because DPMs and PPMs have not been parsed yet.
//		assertIfContextHasError(ctx);
	}


	@Test
	public void testImportPowerLookups20() {
		String filename = "testPLK-2.0.csv";
		String expected = "Step Mod New Maturity Date PLK";
		
		visitor.importPowerLookups(ctx, filename);
		
		assertTrue("expected plData to contain ruleset alias '"+expected+"'.", visitor.plData.containsKey(expected));

		// Context WILL have errors because DPMs and PPMs have not been parsed yet.
//		assertIfContextHasError(ctx);

	}

	@Test
	public void testImportPowerLookups20PpmAlias() {
		String filename = "testPLK-2.0.csv";
		String expected = "Step Mod New Maturity Date PLK";
		
		visitor.importPowerLookups(ctx, filename);
		assertTrue("expected plData to contain ruleset alias '"+expected+"'.", visitor.plData.containsKey(expected));

		PowerLookupData plk = visitor.plData.get(expected);
		assertNotNull("PowerLookupData is null", plk);
		
		ASTInclude node = new ASTInclude(1);
		node.data.put("filename", TEST_INCLUDE_FILE);
		IncludeVisitor incVisitor = new IncludeVisitor();
		IncludeContext incCtx		= new IncludeContext(ctx);
		incVisitor.visit(node, incCtx);
		
		String expectedVarName 	= "Program Name";
		String expectedVarOp 	= "??";
		String expectedVarType	= "DPM";
		// Get 1st var info:
			
		String varName = plk.operations.get(0).getName();
		String varOp   = plk.operations.get(0).getOp();
		String varType = plk.operations.get(0).getType();
		
		assertTrue("var name "+varName+" equals '"+expectedVarName+"'", varName.equals(expectedVarName));
		assertTrue("var op "  +varOp+" equals '"+expectedVarOp+"'", varOp.equals(expectedVarOp));
		assertTrue("var type "+varType+" equals '"+expectedVarType+"'", varType.equals(expectedVarType));
		
		
		// Context WILL have errors because DPMs and PPMs have not been parsed yet.
//		assertIfContextHasError(ctx);

	}

//	@Test
//	public void testBuildPowerLookups() {
//		fail("Not yet implemented");
//
//		assertIfContextHasError(ctx);
//
//		assertTrue("Ruleset not created [Step Mod New Maturity Date PLK].", ctx.containsRuleset("Step Mod New Maturity Date PLK"));
//		assertTrue("Ruleset not created [TestPowerLookup2].", ctx.containsRuleset("TestPowerLookup2"));
//		assertTrue("Ruleset not created [TestPowerLookup3].", ctx.containsRuleset("TestPowerLookup3"));
//
//	}

}
