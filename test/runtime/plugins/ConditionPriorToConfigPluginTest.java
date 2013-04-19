package runtime.plugins;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import runtime.compiler.CompilerContext;

public class ConditionPriorToConfigPluginTest extends
		ConditionPriorToConfigPlugin {

	private CompilerContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new CompilerContext();
		// Add a configuration dir to search.
		ArrayList<String> cdirs = new ArrayList<String>();
		cdirs.add("gdl/tests/condition_prior_to_config_plugin");
		ctx.setConfigDirs(cdirs );
	}

	@After
	public void tearDown() throws Exception {
		ctx = null;
	}

	@Test
	public void testGetName() {
		assertTrue("Name should be ConditionPriorToConfig", "ConditionPriorToConfig".equals(this.getName()));
	}

	@Test
	public void testExecute() {
		this.execute(ctx, null);
		assertTrue("testPriorTo1 ID should be 101", 	101 	== ctx.getConditionPriorToId("testPriorTo1"));
		assertTrue("testPriorTo2 ID should be 2222",	2222 	== ctx.getConditionPriorToId("testPriorTo2"));
		assertTrue("testPriorTo3 ID should be 33", 		33 		== ctx.getConditionPriorToId("testPriorTo3"));
	}
	
	@Test
	public void testDefaultPriorTos() {
		this.execute(ctx, null);
		assertTrue("docs ID should be 1", 		1 	== ctx.getConditionPriorToId("docs"));
		assertTrue("funding ID should be 2", 	2 	== ctx.getConditionPriorToId("funding"));
		assertTrue("approval ID should be 3", 	3	== ctx.getConditionPriorToId("approval"));
	}
	
}
