package runtime.main;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


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
