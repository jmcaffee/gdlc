package runtime.reader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;

import runtime.compiler.LookupData;
import runtime.helpers.TestHelper;

import static org.junit.Assert.*;

/**
 * Created by jeff on 1/26/16.
 */
public class CsvLookupFileTest extends TestHelper {
    String		TESTDIR				= "gdl/tests/csv_lookup_file";
    String		TEST_LKUP_FILE 	    = TESTDIR+"/test_lkup.csv";

    CsvLookupFile subject = null;

    @Before
    public void setUp() throws Exception {
        subject = new CsvLookupFile();
    }

    @After
    public void tearDown() throws Exception {
        subject = null;
    }

    @Test
    public void testParse() throws Exception {

        subject.parse(TEST_LKUP_FILE);
    }

    @Test
    public void testParseThrowsExceptionWhenNoFile() throws Exception {

        try {
            subject.parse("NON_EXISTING_FILE_PATH");
        } catch (FileNotFoundException e) {
            return;
        }

        fail( "Expecting FileNotFoundException but no exception thrown");
    }

    @Test
    public void testExtractLookupsTo() throws Exception {
        subject.parse(TEST_LKUP_FILE);

        HashMap<String,LookupData> lkupData = new HashMap<>();
        subject.extractLookupsTo(lkupData);
    }
}