/**
 * 
 */
package runtime.main;


import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import runtime.compiler.CompilerContext;
import runtime.compiler.IErrorContext;
import runtime.compiler.IProgramContext;
import runtime.compiler.VarDpm;
import runtime.helpers.TestHelper;


/**
 * @author killer
 *
 */
public class CompileMgrTester extends TestHelper {
	CompilerParameters 	cp 			= null;
	String				TESTDIR		= "r:/sandbox/net.bd.gdlc/gdl/tests";
	String 				OUTPUTDIR	= "r:/sandbox/net.bd.gdlc/gdl/tests/output";
	String 				EXPECTED	= "r:/sandbox/net.bd.gdlc/gdl/tests/expected";
	String 				LOOKUPS		= "r:/sandbox/net.bd.gdlc/gdl/tests/lookups";

	/**
	 * writeXmlToFile - helper function - writes xml output file to disk
	 * @param mgr CompileMgr object
	 * @param filepath path of file to write
	 * @return
	 */
	protected boolean writeXmlToFile(CompileMgr mgr, String filepath){
		BufferedWriter out = null;
		try{
			out = new BufferedWriter(new FileWriter(filepath));
			mgr.writeXml(out);
		}
		catch(IOException e){
//			fail("IOException thrown while creating output file [" + outFile + "]");
			return false;
		}
		
		return true;
	}
	
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
	public void testCompile() {
		String args[] = {new String(TESTDIR + "/compileTest.gdl"),
						new String("-v"),
						new String("-vp"),
						new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

	}

	@Test
	public void testCompileVarDef() {
		String args[] = {new String(TESTDIR + "/varDefTest.gdl"),
						new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 
		IProgramContext ctx = mgr.getContext();
		
		assertTrue("testDpm1 variable was not defined.",ctx.containsVar(new VarDpm("testDpm1")));
		assertTrue("testDpm2 variable was not defined.",ctx.containsVar(new VarDpm("testDpm2")));
		assertFalse("testDpm3 variable is defined.",ctx.containsVar(new VarDpm("testDpm3")));

	}

	@Test
	public void testCompileMissingVarDef() {
		String args[] = {new String(TESTDIR + "/missingVarDefTest.gdl"),
						new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		IErrorContext ctx = mgr.getContext();
		
		assertTrue("CContext should have errors.",ctx.hasErrors());
		
	}

	@Test
	public void testCompileMissingRuleDef() {
		String args[] = {new String(TESTDIR + "/missingRuleDefTest.gdl"),
				new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		IErrorContext ctx = mgr.getContext();
		
		assertTrue("CContext should have errors.",ctx.hasErrors());
		
	}

	@Test
	public void testCompileMissingRulesetDef() {
		String args[] = {new String(TESTDIR + "/missingRulesetDefTest.gdl"),
				new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		IErrorContext ctx = mgr.getContext();
		
		assertTrue("CContext should have errors.",ctx.hasErrors());
		
	}

	@Test
	public void testCompileDefdRule() {
		String args[] = {new String(TESTDIR + "/ruleDefTest.gdl"),
				new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 
	}

	@Test
	public void testCompileDefdRuleset() {
		String args[] = {new String(TESTDIR + "/rulesetDefTest.gdl"),
				new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

	}

	@Test
	public void testCompileMissingLookupData() {
		String args[] = {new String(TESTDIR + "/missingLookupTest.gdl"),
						new String("/I" + TESTDIR),
						new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextErrorNotExist(ctx, "TestLookup LK1");

		assertTrue("Context should have missing data errors.",ctx.hasErrors());
		assertFalse("'TestLookup LK1' lookup should not be defined.",ctx.containsLookup("TestLookup LK1"));

	}

	@Test
	public void testCompileLookupDef() {
		String args[] = {new String(TESTDIR + "/LookupTest.gdl"),
						new String("/I" + TESTDIR),
						new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
		
		assertTrue("'TestLookup LK1' lookup was not defined.",ctx.containsLookup("TestLookup LK1"));
		assertTrue("'TestLookup LK2' lookup was not defined.",ctx.containsLookup("TestLookup LK2"));
		assertTrue("'TestLookup LK3' lookup was not defined.",ctx.containsLookup("TestLookup LK3"));
		assertFalse("'NonExistingTestLookup LK2' lookup should not be defined.",ctx.containsLookup("NonExistingTestLookup LK2"));

	}

	@Test
	public void testLookupXmlOutput() {
		String args[] = {new String(TESTDIR + "/LookupTest4.gdl"),
						new String("/I" + TESTDIR),
						new String("/I" + LOOKUPS),
						new String("-Vod"),
						new String("-Vop"),
						};
		this.cp.process(args);

		String outFile = OUTPUTDIR + "/testLookupXmlOutput.xml";
		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
		
		writeXmlToFile(mgr, outFile);

		// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file [testLookupXmlOutput.xml] not created", f.exists());

	}

	@Test
	public void testMoreThan2MathTermsXmlOutput() {
		String args[] = {new String(TESTDIR + "/mathTermTest.gdl"),
						new String("/I" + TESTDIR),
						new String("/I" + LOOKUPS),
						};
		this.cp.process(args);

		String outFile = OUTPUTDIR + "/mathTermXmlOutput.xml";
		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
		
		writeXmlToFile(mgr, outFile);

		// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file [mathTermXmlOutput.xml] not created", f.exists());

	}

	@Test
	public void testCompilePowerLookupDef() {
		String args[] = {new String(TESTDIR + "/powerLookupTest.gdl"),
						new String("/I" + TESTDIR),
						new String("-nooutput"),};
		this.cp.process(args);

		String outFile = OUTPUTDIR + "/testCompilePowerLookupDef.xml";
		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
		
		assertTrue("'TestPowerLookup1' lookup was not defined.",ctx.containsRuleset("TestPowerLookup1"));
		assertTrue("'TestPowerLookup2' lookup was not defined.",ctx.containsRuleset("TestPowerLookup2"));
		assertTrue("'TestPowerLookup3' lookup was not defined.",ctx.containsRuleset("TestPowerLookup3"));

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file [testCompilePowerLookupDef] not created", f.exists());

	}

	@Test
	public void testCompilePowerLookupWithLookupAction() {
		String args[] = {new String(TESTDIR + "/powerLookupTest2.gdl"),
						new String("/I" + TESTDIR),
						new String("-v"),
						new String("-vp"),
						new String("-nooutput"),};
		this.cp.process(args);

		String outFile = OUTPUTDIR + "/testCompilePowerLookupWithLookupAction.xml";
		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
		
		assertTrue("'TestPL1' lookup was not defined.",ctx.containsRuleset("TestPL1"));
		assertTrue("'TestPL2' lookup was not defined.",ctx.containsRuleset("TestPL2"));
		assertTrue("'TestPL3' lookup was not defined.",ctx.containsRuleset("TestPL3"));

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file [testCompilePowerLookupWithLookupAction] not created", f.exists());

	}

	@Test
	public void testCompilePowerLookupWithManyComparisons() {
		String args[] = {new String(TESTDIR + "/powerLookupTest3.gdl"),
						new String("/I" + TESTDIR),
						new String("-v"),
//						new String("-dd"),
						new String("-dp"),
						new String("-vp"),
						new String("-nooutput"),};
		this.cp.process(args);

		String outFile = OUTPUTDIR + "/testCompilePowerLookupWithManyComparisons.xml";
		String expected = new String(EXPECTED + "/testCompilePowerLookupWithManyComparisons.xml");

		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
		
		assertTrue("'LookupTest3' lookup was not defined.",ctx.containsRuleset("LookupTest3"));

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file [testCompilePowerLookupWithManyComparisons] not created", f.exists());

		assertTrue("File contents do not match", fileContentsAreIdentical(outFile, expected));

	}

	@Test
	public void testCompileManyANDComparisons() {
		String args[] = {new String(TESTDIR + "/LookupTest3-1.gdl"),
						new String("/I" + TESTDIR),
						new String("-v"),
						new String("-vp"),
						new String("-nooutput"),};
		this.cp.process(args);

		String outFile = OUTPUTDIR + "/testCompileManyANDComparisons.xml";
		String expected = new String(EXPECTED + "/testCompileManyANDComparisons.xml");

		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
		
		assertTrue("'LookupTest3-1' lookup was not defined.",ctx.containsRule("LookupTest3-1"));

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file [testCompilePowerLookupWithManyComparisons] not created", f.exists());

		assertTrue("File contents do not match", fileContentsAreIdentical(outFile, expected));

	}

	@Test
	public void testRuleXmlOutput() {
		String args[] = {new String(TESTDIR + "/ruleXmlTest.gdl"),
				new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 
		
		String xml = mgr.getRuleXml("G-SetPICalcType");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/G-SetPICalcType.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
	}


	@Test
	public void verifyFileCompare() {
		String outFile = new String(TESTDIR + "/verifyFC1.txt");
		String expected = new String(TESTDIR + "/verifyFC2.txt");
		
		assertTrue("File contents do not match", fileContentsAreIdentical(outFile, expected));
		
	}


	@Test
	public void testDeepComputeOutput() {
		String args[] = {new String(TESTDIR + "/deepComputeTest.gdl"),
				new String(TESTDIR + "/output/deepComputeTest.xml"),
				new String("/I" + TESTDIR),
//				new String("-nooutput"),
				new String("-v"),
				new String("-vp")};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 
		
		String xml = mgr.getRuleXml("DeepComputeRule");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String outFile = new String(TESTDIR + "/output/deepComputeTest.xml");
		String expected = new String(EXPECTED + "/expectedDeepComputeTest.xml");
		
		assertTrue("File contents do not match", fileContentsAreIdentical(outFile, expected));
		
	}


	@Test
	public void testElseActionRuleXmlOutput() {
		String args[] = {new String(TESTDIR + "/ruleXmlTest.gdl"),
				new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

		String xml = mgr.getRuleXml("R-ElseAction");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/R-ElseAction.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
	}


	@Test
	public void testMsgRuleXmlOutput() {
		String args[] = {new String(TESTDIR + "/ruleXmlTest.gdl"),
				new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

		String xml = mgr.getRuleXml("R-IfMessageRule");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/R-IfMessageRule.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
	}


	@Test
	public void testElseMsgRuleXmlOutput() {
		String args[] = {new String(TESTDIR + "/ruleXmlTest.gdl"),
				new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

		String xml = mgr.getRuleXml("R-ElseMessageRule");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/R-ElseMessageRule.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
	}


	@Test
	public void testRulesetXmlOutput() {
		String args[] = {new String(TESTDIR + "/rulesetXmlTest.gdl"),
				new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

		String xml = mgr.getRulesetXml("TestRuleset");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/TestRuleset.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
	}


	@Test
	public void testGuidelineXmlOutput() {
		String args[] = {new String(TESTDIR + "/gdlXmlTest.gdl"),
						 new String("/I" + TESTDIR),
							new String("-v"),
							new String("-vp"),
							new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

		String xml = mgr.getGuidelineXml();
		assertTrue("XML string is empty", (xml.length() > 0));

		SimpleDateFormat dtFormat = new SimpleDateFormat("MMM dd yyyy");
		String gdlAttribs = new String("GuidelineID=\"1\" Name=\"TestGuideline\" Version=\"1\" StartDate=\"" + dtFormat.format(new Date()) + " 12:00AM\"");

//		String validXml = new String("<Guideline " + gdlAttribs + "><Rule Id=\"99999\" Name=\"SimpleRule1\"><IfMessages /><ElseMessages /><Condition><Expression><Compute><Operator>==</Operator><LeftOperand><DPM Name=\"dpmVar\" Type=\"DPM\" Order=\"0\" ProductType=\"4\" /></LeftOperand><RightOperand><Constant>1</Constant></RightOperand></Compute></Expression></Condition></Rule></Guideline>");
		String expectedResult = EXPECTED + "/SimpleRule1.xml";
		String simpleRule = getFileContents(expectedResult);
		String validXml = new String("<Guideline " + gdlAttribs + ">" + simpleRule + "</Guideline>");
		assertEquals("XML string is not valid", validXml, xml);
	}

	@Test
	public void testConditionMsgXmlOutput() {
		String args[] = {new String(TESTDIR + "/conditionTest.gdl"),
				new String("-nooutput"),};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

		String xml = mgr.getRuleXml("SimpleConditionRule1");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/SimpleConditionRule1.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
	}

	@Test
	public void testGuidelineXmlOutputToFile() {
		String args[] = {new String(TESTDIR + "/LookupTest.gdl"),
						 new String("/I" + TESTDIR),
						 new String("-nooutput"),};
		String outFile = OUTPUTDIR + "/testGuidelineXmlOutputToFile.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}

	@Test
	public void testVarAlias() {
		String args[] = {new String(TESTDIR + "/aliasTest.gdl"),
				 new String("/I" + TESTDIR),
				 new String("-nooutput"),};
		String outFile = OUTPUTDIR + "/testVarAlias.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());
		
		assertIfContextHasError(mgr.getContext()); 

		assertTrue("IOException thrown while creating output file [" + outFile + "]", writeXmlToFile(mgr, outFile));
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}




	@Test
	public void testLookupRedefWarning() {
		String args[] = {new String(TESTDIR + "/lookupRedef.gdl"),
				 new String("/I" + TESTDIR),
				 new String("-nooutput"),};
		String outFile = OUTPUTDIR + "/testLookupRedefWarning.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());
		
		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 

		if(ctx.hasWarnings()){
			ctx.dumpWarnings();
		}
		assertTrue("CContext should have a lookup redefinition warning.",ctx.hasWarnings());
		assertEquals("CContext should only have 1 warning.", 1, ctx.getWarningCount());
		
		assertTrue("IOException thrown while creating output file [" + outFile + "]", writeXmlToFile(mgr, outFile));
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}

	@Test
	public void testFunctionDef() {
		String args[] = {new String(TESTDIR + "/functionTest.gdl"),
						 new String("/I" + TESTDIR),
						 new String("-nooutput"),};
		String outFile = OUTPUTDIR + "/testFunctionDef.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}


	@Test
	public void testAndOrOperator() {
		String args[] = {new String(TESTDIR + "/operatorTest.gdl"),
						 new String("/I" + TESTDIR),
						 new String("-nooutput"),};
		String outFile = OUTPUTDIR + "/testAndOrOperator.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}


	@Test
	public void testIncludeFileError() {
		String args[] = {new String(TESTDIR + "/includeFileErrorTest.gdl"),
						 new String("/I" + TESTDIR),
						 new String("-nooutput"),};
		String outFile = OUTPUTDIR + "/includeFileErrorTest.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextErrorNotExist(mgr.getContext(),"r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\badIncludeFile.gdl"); 

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}


	@Test
	public void testInsertPricingRule() {
		String args[] = {new String(TESTDIR + "/insertPricingGuideline.gdl"),
						 new String("/I" + TESTDIR),
						 new String("-nooutput"),};
		String outFile = OUTPUTDIR + "/insertPricingGuideline.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}


	@Test
	public void testFunctionDefinition() {
		String args[] = {new String(TESTDIR + "/functionDefTest.gdl"),
						 new String("/I" + TESTDIR),
						 new String("-v"),
						 new String("-vp"),
						 new String("-nooutput"),};
		String outFile = OUTPUTDIR + "/functionDefTest.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}


	@Test
	public void testMissingFunctionDefinition() {
		String args[] = {new String(TESTDIR + "/missingFunctionDefTest.gdl"),
						 new String("/I" + TESTDIR),
						 new String("-v"),
						 new String("-vp"),
						 new String("-nooutput"),};

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextErrorNotExist(mgr.getContext(), "Function [missingFunction] definition missing."); 

	}

	@Test
	public void testFhaGuidelineOutput() {
//		String FHAROOT = "r:/sandbox/fha/src";
		
//		String args[] = {new String(FHAROOT + "/FHA-Pricing1st.gdl"),
//				 		 new String("/I" + FHAROOT),
//						 new String("/I" + FHAROOT + "/lookups"),
//						 new String("/I" + FHAROOT + "/lookups/pl"),
//						 new String("-v"),
////						 new String("-vp"),
//						 new String("-nooutput"),};
//		String outFile = FHAROOT + "/FHA-Pricing1st.xml";
		String args[] = {new String(TESTDIR + "/corruptCsvTest.gdl"),
		 		 new String("/I" + TESTDIR),
				 new String("/I" + TESTDIR + "/lookups"),
				 new String("/I" + TESTDIR + "/lookups/pl"),
				 new String("-v"),
//				 new String("-vp"),
				 new String("-nooutput"),};
		String outFile = OUTPUTDIR + "/corruptCsvTest.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());


		writeXmlToFile(mgr, outFile);
		
		assertIfContextErrorNotExist(mgr.getContext(), "FHA-Rate1st-Fxd30-15Day RTN LK1"); 

			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}


	@Test
	public void testFhaCsvFileImport() {
		String args[] = {new String(TESTDIR + "/corruptCsvTest2.gdl"),
		 		 new String("/I" + TESTDIR),
				 new String("/I" + TESTDIR + "/lookups"),
				 new String("/I" + TESTDIR + "/lookups/pl"),
				 new String("-v"),
//				 new String("-vp"),
				 new String("-nooutput"),};
		String outFile = OUTPUTDIR + "/corruptCsvTest2.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());


		writeXmlToFile(mgr, outFile);
		
		assertIfContextErrorNotExist(mgr.getContext(), "FHA-Rate1st-Fxd30-15Day RTN LK1"); 

			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}


}
