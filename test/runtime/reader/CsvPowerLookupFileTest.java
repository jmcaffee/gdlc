package runtime.reader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import runtime.compiler.CompileException;
import runtime.compiler.PowerLookupData;
import runtime.helpers.TestHelper;

import static org.junit.Assert.*;

/**
 * Created by jeff on 1/27/16.
 */
public class CsvPowerLookupFileTest extends TestHelper {
    String		TESTDIR				= "gdl/tests/csv_powerlookup_file";
    String		TEST_PLK_FILE 	    = TESTDIR+"/test_plk.csv";
    String		TEST_TRUE_ACTION_PLK_FILE = TESTDIR+"/plk_with_true_action_keywords.csv";
    String		TEST_EMPTY_PLK_FILE = TESTDIR+"/test_empty_plk.csv";

    CsvPowerLookupFile subject = null;

    @Before
    public void setUp() throws Exception {
        subject = new CsvPowerLookupFile();
    }

    @After
    public void tearDown() throws Exception {
        subject = null;
    }

    @Test
    public void testParse() throws Exception {
        subject.parse(TEST_PLK_FILE);
    }

    @Test
    public void testParseThrowsFileNotFoundException() throws Exception {
        try {
            subject.parse("NON_EXISTING_FILE_PATH");
        } catch (FileNotFoundException e) {
            return;
        }

        fail("Expecting FileNotFoundException but no exception thrown");
    }

    @Test
    public void testExtractLookupsTo() throws Exception {
        subject.parse(TEST_PLK_FILE);

        HashMap<String,PowerLookupData> lkupData = new HashMap<>();
        subject.extractLookupsTo(lkupData);

        assert(!lkupData.isEmpty());
    }

    @Test
    public void testExtractLookupsToWithTrueActionAssigns() throws Exception {
        subject.parse(TEST_TRUE_ACTION_PLK_FILE);

        HashMap<String,PowerLookupData> lkupData = new HashMap<>();
        subject.extractLookupsTo(lkupData);

        assert(!lkupData.isEmpty());
    }

    @Test
    public void testNoNPEWhenParsingEmptyFile() throws Exception {
        try {
            subject.parse(TEST_EMPTY_PLK_FILE);

            HashMap<String,PowerLookupData> lkupData = new HashMap<>();
            subject.extractLookupsTo(lkupData);
        } catch (CompileException e) {
            return;
        }

        fail( "Expecting CompileException but no exception thrown");
    }
}