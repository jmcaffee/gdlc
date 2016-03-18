package runtime.visitors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import runtime.compiler.IErrorContext;
import runtime.helpers.TestHelper;
import runtime.main.CompileMgr;
import runtime.main.CompilerParameters;
import runtime.main.GdlcException;

import static org.junit.Assert.*;

public class ImportPowerLookupVisitorTest extends TestHelper {
    CompilerParameters cp 			= null;
    //	String				TESTDIR		= rootDir + "/gdl/tests";
    // Using relative directories for easier paths, now that GDLC supports them.
    String				INCDIR		= "gdl/tests";
    String				TESTDIR		= "gdl/tests/visitors/import_power_lookup_visitor";
    String 				OUTPUTDIR	= "tmp/tests/visitors/import_power_lookup_visitor";

    @Before
    public void setUp() throws Exception {
        mkCleanDirs(OUTPUTDIR);
        this.cp = new CompilerParameters();
    }

    @After
    public void tearDown() throws Exception {
        this.cp = null;
    }

    @Test
    public void testImportOfMissingPLKGeneratesHelpfulErrorMessage() throws GdlcException {
        String args[] = {TESTDIR + "/import_missing_plk.gdl",
                "--I" + TESTDIR,
                "-nooutput",
        };
        String outFile = OUTPUTDIR + "/import_missing_plk.xml";
        deleteFileIfExists(outFile);

        this.cp.process(args);

        CompileMgr mgr = new CompileMgr();
        try {
            mgr.execute(this.cp);
        } catch (GdlcException e) {
            // Expected error... do nothing.
        }

        IErrorContext ctx = mgr.getContext();
        assertIfContextErrorNotExist(ctx,"File Not Found. PowerLookup file [does_not_exist.csv] not found.");
    }
}