package runtime.plugins;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import runtime.compiler.CompilerContext;

public class ConditionCategoryConfigPluginTest extends
		ConditionCategoryConfigPlugin {

	private CompilerContext ctx;

	@Before
	public void setUp() throws Exception {
		ctx = new CompilerContext();
		// Add a configuration dir to search.
		ArrayList<String> cdirs = new ArrayList<String>();
		cdirs.add("gdl/tests");
		ctx.setConfigDirs(cdirs );
	}

	@After
	public void tearDown() throws Exception {
		ctx = null;
	}

	@Test
	public void testGetName() {
		assertTrue("Name should be ConditionCategoryConfig", "ConditionCategoryConfig".equals(this.getName()));
	}

	@Test
	public void testExecute() {
		this.execute(ctx, null);
		assertTrue("testCat1 ID should be 101", 101 == ctx.getConditionCategoryId("testCat1"));
		assertTrue("testCat2 ID should be 2222", 2222 == ctx.getConditionCategoryId("testCat2"));
		assertTrue("testCat3 ID should be 33", 33 == ctx.getConditionCategoryId("testCat3"));
	}
	
	@Test
	public void testDefaultCategories() {
		this.execute(ctx, null);
		assertTrue("asset ID should be 1", 		1 	== ctx.getConditionCategoryId("asset"));
		assertTrue("credit ID should be 2", 	2 	== ctx.getConditionCategoryId("credit"));
		assertTrue("income ID should be 3", 	3	== ctx.getConditionCategoryId("income"));
		assertTrue("property ID should be 4", 	4 	== ctx.getConditionCategoryId("property"));
		assertTrue("purchase ID should be 5", 	5 	== ctx.getConditionCategoryId("purchase"));
		assertTrue("title ID should be 6", 		6	== ctx.getConditionCategoryId("title"));
	}
	

}
