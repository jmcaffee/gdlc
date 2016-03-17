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
//	String				TESTDIR		= rootDir + "/gdl/tests";
	// Using relative directories for easier paths, now that GDLC supports them.
	String				INCDIR		= "gdl/tests";
	String				TESTDIR		= "gdl/tests/compile_mgr_tester";
	String 				OUTPUTDIR	= "tmp/tests/compile_mgr_tester";
	String 				EXPECTED	= TESTDIR+"/expected";
	String 				LOOKUPS		= TESTDIR+"/lookups";

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
        mkCleanDirs(OUTPUTDIR);
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
	public void testCompile() throws GdlcException {
		String args[] = {TESTDIR + "/compileTest.gdl",
						"--I"+INCDIR,
                        //"-v",
						//"-vp",
						"-nooutput",
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

	}

	@Test
	public void testCompileVarDef() throws GdlcException {
		String args[] = {TESTDIR + "/varDefTest.gdl",
						"-nooutput",
        };
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
	public void testCompileMissingVarDef() throws GdlcException {
		String args[] = {TESTDIR + "/missingVarDefTest.gdl",
						"-nooutput",
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
        try {
            mgr.execute(this.cp);
        } catch (GdlcException e) {
            // Expected error... do nothing.
        }

		assertNotNull("Parse failed.",mgr.getParseTree());

		IErrorContext ctx = mgr.getContext();
		
		assertTrue("CContext should have errors.",ctx.hasErrors());
		
	}

	@Test
	public void testCompileMissingRuleDef() throws GdlcException {
		String args[] = {TESTDIR + "/missingRuleDefTest.gdl",
				        "-nooutput",
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
        try {
            mgr.execute(this.cp);
        } catch (GdlcException e) {
            // Expected error... do nothing.
        }

		assertNotNull("Parse failed.",mgr.getParseTree());

		IErrorContext ctx = mgr.getContext();
		
		assertTrue("CContext should have errors.",ctx.hasErrors());
		
	}

	@Test
	public void testCompileMissingRulesetDef() throws GdlcException {
		String args[] = {TESTDIR + "/missingRulesetDefTest.gdl",
				        "-nooutput",
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
        try {
            mgr.execute(this.cp);
        } catch (GdlcException e) {
            // Expected error... do nothing.
        }

		assertNotNull("Parse failed.",mgr.getParseTree());

		IErrorContext ctx = mgr.getContext();
		
		assertTrue("CContext should have errors.",ctx.hasErrors());
		
	}

	@Test
	public void testCompileDefdRule() throws GdlcException {
		String args[] = {TESTDIR + "/ruleDefTest.gdl",
				        "-nooutput",
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 
	}

	@Test
	public void testCompileDefdRuleset() throws GdlcException {
		String args[] = {TESTDIR + "/rulesetDefTest.gdl",
				        "-nooutput"
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

	}

	@Test
	public void testCompileMissingLookupData() throws GdlcException {
		String args[] = {TESTDIR + "/missingLookupTest.gdl",
						"--I" + TESTDIR,
						"-nooutput"
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
        try {
            mgr.execute(this.cp);
        }
        catch (GdlcException e) {
            // Expected... do nothing.
        }

		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextErrorNotExist(ctx, "TestLookup LK1");

		assertTrue("Context should have missing data errors.",ctx.hasErrors());
		assertFalse("'TestLookup LK1' lookup should not be defined.",ctx.containsLookup("TestLookup LK1"));

	}

	@Test
	public void testCompileLookupDef() throws GdlcException {
		String args[] = {TESTDIR + "/LookupTest.gdl",
						"--I" + TESTDIR,
                        "--I" + LOOKUPS,
						"-nooutput"
        };
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
	public void testLookupXmlOutput() throws GdlcException {
		String outFile = OUTPUTDIR + "/testLookupXmlOutput.xml";
		String args[] = {TESTDIR + "/LookupTest4.gdl",
						 outFile,
						 "--I" + TESTDIR,
						 "--I" + LOOKUPS,
						 //"-Vod",
						 //"-Vop",
						};
		this.cp.process(args);

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
	public void testMoreThan2MathTermsXmlOutput() throws GdlcException {
		String outFile = OUTPUTDIR + "/mathTermXmlOutput.xml";
		String args[] = {TESTDIR + "/mathTermTest.gdl",
						 outFile,
						 "--I" + TESTDIR,
						 "--I" + LOOKUPS,
						};
		this.cp.process(args);

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
	public void testCompilePowerLookupDef() throws GdlcException {
		String outFile = OUTPUTDIR + "/testCompilePowerLookupDef.xml";
		String args[] = {TESTDIR + "/powerLookupTest.gdl",
						 outFile,
						 "--I" + TESTDIR,
						 "-nooutput",
        };
		this.cp.process(args);

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
	public void compilePLKWithLookupAction() throws GdlcException {
		String outFile = OUTPUTDIR + "/testCompilePowerLookupWithLookupAction.xml";
		String args[] = {TESTDIR + "/powerLookupTest2.gdl",
						 outFile,
						 "--I" + TESTDIR,
						 //"-v",
						 //"-vp",
						 "-nooutput",
        };
		this.cp.process(args);

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
	public void compilePLKWithManyComparisons() throws GdlcException {
		String outFile = OUTPUTDIR + "/plk_with_many_comparisons.xml";
		String args[] = {TESTDIR + "/powerLookupTest3.gdl",
						 outFile,
						 "--I" + TESTDIR,
						 "-v",
//						 "-dd",
						 "-dp",
						 "-vp",
						 "-nooutput",
        };
		this.cp.process(args);

		String expected = new String(EXPECTED + "/plk_with_many_comparisons.xml");

		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
		
		assertTrue("'TestPowerLookup3' lookup was not defined.",ctx.containsRuleset("TestPowerLookup3"));

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file ["+outFile+"] not created", f.exists());

		assertTrue("File contents do not match", fileContentsAreIdenticalWithStrippedDate(outFile, expected));

	}

	@Test
	public void testCompileManyANDComparisons() throws GdlcException {
		String outFile = OUTPUTDIR + "/testCompileManyANDComparisons.xml";
		String args[] = {TESTDIR + "/LookupTest3-1.gdl",
						 outFile,
						 "--I" + TESTDIR,
						 //"-v",
						 //"-vp",
						 "-nooutput",
        };
		this.cp.process(args);

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

		assertTrue("File contents do not match", fileContentsAreIdenticalWithStrippedDate(outFile, expected));

	}

	@Test
	public void testRuleXmlOutput() throws GdlcException {
		String args[] = {TESTDIR + "/ruleXmlTest.gdl",
				         "-nooutput",
        };
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
		String outFile = TESTDIR + "/aliasTest.gdl";
		String expected = TESTDIR + "/aliasTest.gdl";
		
		assertTrue("File contents do not match", fileContentsAreIdentical(outFile, expected));
		
	}


	@Test
	public void testDeepComputeOutput() throws GdlcException {
		mkdirs(OUTPUTDIR);
		String outFile = OUTPUTDIR + "/deepComputeTest.xml";
		String args[] = {TESTDIR + "/deepComputeTest.gdl",
						outFile,
						"--I" + TESTDIR,
		//				"-nooutput",
					//  "-v",
					//  "-vp"
		};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 
		
		String xml = mgr.getRuleXml("DeepComputeRule");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String expected = new String(EXPECTED + "/deepComputeTest.xml");
		
		assertTrue("File contents do not match", fileContentsAreIdenticalWithStrippedDate(outFile, expected));
		
	}


	@Test
	public void testElseActionRuleXmlOutput() throws GdlcException {
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
	public void testMsgRuleXmlOutput() throws GdlcException {
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
	public void testElseMsgRuleXmlOutput() throws GdlcException {
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
	public void testRulesetXmlOutput() throws GdlcException {
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
	public void testGuidelineXmlOutput() throws GdlcException {
		String args[] = {new String(TESTDIR + "/gdlXmlTest.gdl"),
						 new String("--I" + TESTDIR),
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

		// TODO: Turn this into a helper test function: wrap xml in currently dated Guideline tag.
		SimpleDateFormat dtFormat = new SimpleDateFormat("MMM dd yyyy");
		String gdlAttribs = new String("GuidelineID=\"1\" Name=\"TestGuideline\" Version=\"1\" StartDate=\"" + dtFormat.format(new Date()) + " 12:00AM\"");

//		String validXml = new String("<Guideline " + gdlAttribs + "><Rule Id=\"99999\" Name=\"SimpleRule1\"><IfMessages /><ElseMessages /><Condition><Expression><Compute><Operator>==</Operator><LeftOperand><DPM Name=\"dpmVar\" Type=\"DPM\" Order=\"0\" ProductType=\"4\" /></LeftOperand><RightOperand><Constant>1</Constant></RightOperand></Compute></Expression></Condition></Rule></Guideline>");
		String expectedResult = EXPECTED + "/SimpleRule1.xml";
		String simpleRule = getFileContents(expectedResult);
		String validXml = new String("<Guideline " + gdlAttribs + ">" + simpleRule + "</Guideline>");
		assertEquals("XML string is not valid", validXml, xml);
	}

	@Test
	public void testConfigurationDirectoryParameter() throws GdlcException {
		String args[] = {	new String(TESTDIR + "/conditionTest.gdl"),
							new String("--C" + TESTDIR),		// Configuration directory
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
	public void testConditionCategoryConfiguration() throws GdlcException {
		String args[] = {	new String(TESTDIR + "/conditionTest.gdl"),
							new String("--C" + TESTDIR),		// Configuration directory
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
	public void testConditionMsgXmlOutput() throws GdlcException {
		String args[] = {new String(TESTDIR + "/conditionTest.gdl"),
                         new String("--C"+TESTDIR),
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
	public void compileRuleWithMetaOrdering() throws GdlcException {
		String args[] = {TESTDIR + "/ruleWithMetaOrdering.gdl",
						 "--C" + TESTDIR,		// Configuration directory
						 "-nooutput",
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 
	}

	@Test
	public void ruleXmlContainsOrderedCompute() throws GdlcException {
		String outFile = OUTPUTDIR + "/ruleWithOrderedCompute.xml";
		String args[] = {TESTDIR + "/ruleWithOrderedCompute.gdl",
						outFile,
						"--I" + TESTDIR,
						"-nooutput",
        };
		this.cp.process(args);

		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
	
		String rulename = "TestRule";
		assertTrue("'"+rulename+"' rule was not defined.",ctx.containsRule(rulename));

		String xml = mgr.getRuleXml(rulename);
		assertTrue("XML string should not be empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/ruleWithOrderedCompute.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
	}

	@Test
	public void ruleXmlContainsOrderedAssignTo() throws GdlcException {
		String outFile = OUTPUTDIR + "/ruleWithOrderedAssignTo.xml";
		String args[] = {new String(TESTDIR + "/ruleWithOrderedAssignTo.gdl"),
						outFile,
						new String("--I" + TESTDIR),
						new String("-nooutput"),};
		this.cp.process(args);

		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
	
		String rulename = "TestRule";
		assertTrue("'"+rulename+"' rule was not defined.",ctx.containsRule(rulename));

		String xml = mgr.getRuleXml(rulename);
		assertTrue("XML string should not be empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/ruleWithOrderedAssignTo.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
	}

	@Test
	public void ruleXmlContainsOrderedMessage() throws GdlcException {
		String outFile = OUTPUTDIR + "/ruleWithOrderedMessage.xml";
		String args[] = {new String(TESTDIR + "/ruleWithOrderedMessage.gdl"),
						outFile,
						new String("--I" + TESTDIR),
						new String("-nooutput"),};
		this.cp.process(args);

		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
	
		String rulename = "TestRule";
		assertTrue("'"+rulename+"' rule was not defined.",ctx.containsRule(rulename));

		String xml = mgr.getRuleXml(rulename);
		assertTrue("XML string should not be empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/ruleWithOrderedMessage.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
	}

	@Test
	public void ruleXmlContainsOrderedElseMessage() throws GdlcException {
		String outFile = OUTPUTDIR + "/ruleWithOrderedElseMessage.xml";
		String args[] = {new String(TESTDIR + "/ruleWithOrderedElseMessage.gdl"),
						outFile,
						new String("--I" + TESTDIR),
						new String("-nooutput"),};
		this.cp.process(args);

		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
	
		String rulename = "TestRule";
		assertTrue("'"+rulename+"' rule was not defined.",ctx.containsRule(rulename));

		String xml = mgr.getRuleXml(rulename);
		assertTrue("XML string should not be empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/ruleWithOrderedElseMessage.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
	}

	@Test
	public void ruleXmlContainsOrderedCondition() throws GdlcException {
		String outFile = OUTPUTDIR + "/ruleWithOrderedCondition.xml";
		String args[] = {TESTDIR + "/ruleWithOrderedCondition.gdl",
						outFile,
						"--I" + TESTDIR,
						"--C" + TESTDIR,		// Configuration directory
						"-nooutput",
		};
		this.cp.process(args);

		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
	
		String rulename = "TestRule";
		assertTrue("'"+rulename+"' rule was not defined.",ctx.containsRule(rulename));

		String xml = mgr.getRuleXml(rulename);
		assertTrue("XML string should not be empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/ruleWithOrderedCondition.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
	}

	@Test
	public void ruleXmlContainsOrderedElseCondition() throws GdlcException {
		String outFile = OUTPUTDIR + "/ruleWithOrderedElseCondition.xml";
		String args[] = {TESTDIR + "/ruleWithOrderedElseCondition.gdl",
						outFile,
						"--I" + TESTDIR,
						"--C" + TESTDIR,		// Configuration directory
						"-nooutput",
		};
		this.cp.process(args);

		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
	
		String rulename = "TestRule";
		assertTrue("'"+rulename+"' rule was not defined.",ctx.containsRule(rulename));

		String xml = mgr.getRuleXml(rulename);
		assertTrue("XML string should not be empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/ruleWithOrderedElseCondition.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
	}

	@Test
	public void plkXmlOutputContainsOrderAttributes() throws GdlcException {
		String outFile = OUTPUTDIR + "/orderAttribPLKTest.xml";
		String args[] = {TESTDIR + "/orderAttribPLKTest.gdl",
						outFile,
						"--I" + TESTDIR,
						"-nooutput",
		};
		this.cp.process(args);

		deleteFileIfExists(outFile);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		CompilerContext ctx = (CompilerContext)mgr.getContext();
		assertIfContextHasError(mgr.getContext()); 
		
		assertTrue("'OrderAttribPLK' powerlookup was not defined.",ctx.containsRuleset("OrderAttribPLK"));

		String xml = mgr.getRuleXml("OrderAttribPLK-1");
		assertTrue("XML string should not be empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/orderAttribPLKRule1.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
		xml = mgr.getRuleXml("OrderAttribPLK-2");
		assertTrue("XML string should not be empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/orderAttribPLKRule2.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
	}

	@Test
	public void testConditionPLKMsgXmlOutput() throws GdlcException {
		String args[] = {TESTDIR + "/conditionPLKTest.gdl",
						 "--I" + TESTDIR,
						 "-nooutput",
		};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextHasError(mgr.getContext()); 

		String xml = mgr.getRuleXml("ConditionTestPLK-1");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/ConditionTestPLK-1.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
		xml = mgr.getRuleXml("ConditionTestPLK-2");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/ConditionTestPLK-2.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
		xml = mgr.getRuleXml("ConditionTestPLK-3");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/ConditionTestPLK-3.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
	}

	@Test
	public void testConditionsElementXmlOutput() throws GdlcException {
		String args[] = {TESTDIR + "/conditionTest.gdl",
                         "--C"+TESTDIR,
                         "-nooutput",
		};
		String outFile = OUTPUTDIR + "/testConditionsElementXmlOutputToFile.xml";
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
		
		String condDef1 = "<Message Type=\"Condition\" Id=\"1\" Name=\"Test Docs\" PriorTo=\"2222\" Category=\"101\" ImageDocType=\"\" Visibility=\"\"><![CDATA[ Test PriorTo docs condition message.  ]]></Message>";
		String condDef2 = "<Message Type=\"Condition\" Id=\"2\" Name=\"Test Funding\" PriorTo=\"33\" Category=\"2222\" ImageDocType=\"My Image Doc Type\" Visibility=\"Internal\"><![CDATA[ Test PriorTo funding condition message.  ]]></Message>";
		String condDef3 = "<Message Type=\"Condition\" Id=\"3\" Name=\"Test Approval\" PriorTo=\"101\" Category=\"33\" ImageDocType=\"\" Visibility=\"External\"><![CDATA[ Test PriorTo approval condition message.  ]]></Message>";
		
		assertTrue("File should contain condition XML definition", fileContains(outFile, condDef1));
		assertTrue("File should contain condition XML definition", fileContains(outFile, condDef2));
		assertTrue("File should contain condition XML definition", fileContains(outFile, condDef3));
		
	}

	@Test
	public void testGuidelineXmlOutputToFile() throws GdlcException {
		String args[] = {TESTDIR + "/LookupTest.gdl",
						 "--I" + TESTDIR,
						 "-nooutput",
		};
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
	public void testVarAlias() throws GdlcException {
		String args[] = {TESTDIR + "/aliasTest.gdl",
                         "--I" + TESTDIR,
                         "-nooutput",
        };
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
	public void testFunctionDef() throws GdlcException {
		String args[] = {TESTDIR + "/functionTest.gdl",
						 "--I" + TESTDIR,
						 "-nooutput",
        };
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
	public void testAndOrOperator() throws GdlcException {
		String args[] = {TESTDIR + "/operatorTest.gdl",
						 "--I" + TESTDIR,
						 "-nooutput",
        };
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
	public void testIncludeFileError() throws GdlcException {
		String args[] = {TESTDIR + "/includeFileErrorTest.gdl",
						 "--I" + TESTDIR,
						 "-nooutput",
        };
		String outFile = OUTPUTDIR + "/includeFileErrorTest.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
        try {
            mgr.execute(this.cp);
        }
        catch (GdlcException e) {
            // Expected exception... do nothing.
        }

		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextErrorNotExist(mgr.getContext(),"badIncludeFile.gdl"); 

		writeXmlToFile(mgr, outFile);
		
			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}


	@Test
	public void testInsertPricingRule() throws GdlcException {
		String args[] = {TESTDIR + "/insertPricingGuideline.gdl",
						 "--I" + TESTDIR,
						 "-nooutput",
        };
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
	public void compilesFunctionDefinition() throws GdlcException {
		fail("Embedded rule references are not yet supported.");

		String args[] = {TESTDIR + "/functionDefTest.gdl",
						 "--I" + TESTDIR,
						 //"-v",
						 //"-vp",
						 //"-nooutput",
        };
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
	public void testMissingFunctionDefinition() throws GdlcException {
		String args[] = {TESTDIR + "/missingFunctionDefTest.gdl",
						 "--I" + TESTDIR,
						 //"-v",
						 //"-vp",
						 "-nooutput",
        };

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
        try {
            mgr.execute(this.cp);
        }
        catch (GdlcException e) {
            // Expected exception... do nothing.
        }

		assertNotNull("Parse failed.",mgr.getParseTree());

		assertIfContextErrorNotExist(mgr.getContext(), "Function [missingFunction] definition missing."); 

	}

	@Test
	public void testFhaGuidelineOutput() throws GdlcException {
//		String FHAROOT = "r:/sandbox/fha/src";
		
//		String args[] = {new String(FHAROOT + "/FHA-Pricing1st.gdl"),
//				 		 new String("--I" + FHAROOT),
//						 new String("--I" + FHAROOT + "/lookups"),
//						 new String("--I" + FHAROOT + "/lookups/pl"),
//						 new String("-v"),
////						 new String("-vp"),
//						 new String("-nooutput"),};
//		String outFile = FHAROOT + "/FHA-Pricing1st.xml";
		String args[] = {TESTDIR + "/corruptCsvTest.gdl",
                         "--I" + TESTDIR,
                         "--I" + TESTDIR + "/lookups",
                         "--I" + TESTDIR + "/lookups/pl",
        //               "-v",
        //				 "-vp",
                         "-nooutput",
        };
		String outFile = OUTPUTDIR + "/corruptCsvTest.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());


		writeXmlToFile(mgr, outFile);
		
//		assertIfContextErrorNotExist(mgr.getContext(), "FHA-Rate1st-Fxd30-15Day RTN LK1"); 

			// Verify that the file was created.
		File f=new File(outFile);
		
		assertTrue("Output file not created", f.exists());
		
	}

	@Test
	public void testCompileUsingReservedConstants() throws GdlcException {
		String args[] = {TESTDIR + "/compileTest-reservedConstants.gdl",
						 //"-v",
						 //"-vp",
						 "-nooutput",
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());
		assertIfContextHasError(mgr.getContext()); 
		
		String xml = mgr.getRuleXml("Rule-TestNullConstant");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/Rule-TestNullConstant.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("NullConstant XML string is not valid", validXml, xml);
		
		xml = mgr.getRuleXml("Rule-TestTrueConstant");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/Rule-TestTrueConstant.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("TrueConstant XML string is not valid", validXml, xml);
		
		xml = mgr.getRuleXml("Rule-TestFalseConstant");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/Rule-TestFalseConstant.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("FalseConstant XML string is not valid", validXml, xml);
		

	}

	
	@Test
	public void testCompileUsingVarCasting() throws GdlcException {
		String args[] = {TESTDIR + "/compileTest-varCasting.gdl",
						 //"-v",
						 //"-vp",
						 "-nooutput",
        };
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());
		assertIfContextHasError(mgr.getContext()); 
		
		String xml = mgr.getRuleXml("Rule-TestDpmCast");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/Rule-TestDpmCast.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("DpmCast XML string is not valid", validXml, xml);
		
		
		xml = mgr.getRuleXml("Rule-TestDpmCastWithParens");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/Rule-TestDpmCastWithParens.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("DpmCast with parens in the alias failed", validXml, xml);
		
		
		xml = mgr.getRuleXml("Rule-TestPpmCast");
		assertTrue("PpmCast XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/Rule-TestPpmCast.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
		xml = mgr.getRuleXml("Rule-TestPpmCastWithParens");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/Rule-TestPpmCastWithParens.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("PpmCast with parens in the alias failed", validXml, xml);
		
		

	}

	
	@Test
	public void compilesPLK_CastingVars() throws GdlcException {
		String args[] = {TESTDIR + "/powerLookupTest-castingVars.gdl",
						 "--I" + TESTDIR,
						 //"-v",
						 //"-vp",
						 "-nooutput",
						};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());
		assertIfContextHasError(mgr.getContext()); 
		
		String xml = mgr.getRuleXml("CastVarsNumericPLK-1");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/CastVarsNumericPLK-1.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("DpmCast XML string is not valid", validXml, xml);
		
		
		xml = mgr.getRuleXml("CastVarsTextPLK-1");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/CastVarsTextPLK-1.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("DpmCast with parens in the alias failed", validXml, xml);
		
		
		xml = mgr.getRuleXml("CastVarsBooleanPLK-1");
		assertTrue("PpmCast XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/CastVarsBooleanPLK-1.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
		
		xml = mgr.getRuleXml("CastVarsBooleanPLK-2");
		assertTrue("PpmCast XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/CastVarsBooleanPLK-2.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
		
		xml = mgr.getRuleXml("CastVarsBooleanPLK-3");
		assertTrue("PpmCast XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/CastVarsBooleanPLK-3.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("XML string is not valid", validXml, xml);
		
		

	}

	
	@Test
	public void testCompilePowerLookups_exeType() throws GdlcException {
		String args[] = {TESTDIR + "/powerLookupTest-exeType.gdl",
						 "--I" + TESTDIR,
						 //"-v",
						 //"-vp",
						 "-nooutput",
						};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());
		assertIfContextHasError(mgr.getContext()); 
		
		String xml = mgr.getRulesetXml("TestTrueExeTypePLK");
		assertTrue("TestTrueExeTypePLK ruleset XML string is empty", (xml.length() > 0));
		
		String expectedResult = EXPECTED + "/TestTrueExeTypePLK.xml";
		String validXml = getFileContents(expectedResult);
		assertEquals("TestTrueExeTypePLK ruleset XML is not valid", validXml, xml);
		
		
		xml = mgr.getRulesetXml("TestContinueExeTypePLK");
		assertTrue("TestContinueExeTypePLK ruleset XML string is empty", (xml.length() > 0));
		
		expectedResult = EXPECTED + "/TestContinueExeTypePLK.xml";
		validXml = getFileContents(expectedResult);
		assertEquals("TestContinueExeTypePLK ruleset XML is not valid", validXml, xml);
		
		
	}

	
	@Test
	public void compilesPLKs_Msgs() throws GdlcException {
		String args[] = {TESTDIR + "/powerLookupTest-msgs.gdl",
						 "--I" + TESTDIR,
						 //"-v",
						 //"-vp",
						 "-nooutput",
						};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Parse failed.",mgr.getParseTree());
		assertIfContextHasError(mgr.getContext()); 
		
		String xml = mgr.getRuleXml("MsgsTruePLK-1");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		assertXmlContains("<IfMessages><Message Type=\"Findings\" Order=\"3\"><![CDATA[True Message 1.]]></Message></IfMessages>", xml);
		
		
		xml = mgr.getRuleXml("MsgsFalsePLK-1");
		assertTrue("XML string is empty", (xml.length() > 0));
		
		assertXmlContains("<ElseMessages><Message Type=\"Findings\" Order=\"4\"><![CDATA[False Message 1.]]></Message></ElseMessages>", xml);
		
		
		xml = mgr.getRuleXml("MsgsTrueWithDpmPLK-1");
		assertTrue("PpmCast XML string is empty", (xml.length() > 0));
		
		assertXmlContains("<IfMessages><Message Type=\"Exceptions\" Order=\"3\"><![CDATA[True Message 1: <DPM>DPM Text</DPM>.]]></Message></IfMessages>", xml);
	}


	@Test
	public void testXmlFunctionDef() throws GdlcException {
		String args[] = {TESTDIR + "/xml_function_def_test.gdl",
				"--I" + TESTDIR,
				"-nooutput",
		};
		String outFile = OUTPUTDIR + "/test_xml_function_def.xml";
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
	public void testXmlFunctionDefErrorMsgs() throws GdlcException {
		String args[] = {TESTDIR + "/xml_function_def_err_msg_test.gdl",
				"--I" + TESTDIR,
				"-nooutput",
		};
		String outFile = OUTPUTDIR + "/test_xml_function_def_err_msg.xml";
		deleteFileIfExists(outFile);

		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		try {
			mgr.execute(this.cp);
		} catch (GdlcException e) {
			// Expected error... do nothing.
		}

		IErrorContext ctx = mgr.getContext();
        assertIfContextErrorNotExist(ctx,"XmlFunction reference [noArgFunc] is expecting 0 argument(s). Received 1 instead.");
		assertIfContextErrorNotExist(ctx,"XmlFunction reference [round] is expecting 2 argument(s). Received 1 instead.");

		// Name of failing rule is included in messages
		assertIfContextErrorNotExist(ctx,"Argument Missing. @ rule TestXmlFunction-NoArgFunc:");
		assertIfContextErrorNotExist(ctx,"Argument Missing. @ rule TestXmlFunction-Round:");

	}
}
