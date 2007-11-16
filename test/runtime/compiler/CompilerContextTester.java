/**
 * 
 */
package runtime.compiler;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import runtime.main.CompileMgr;
import runtime.main.CompilerParameters;

/**
 * @author killer
 *
 */
public class CompilerContextTester {
	CompilerParameters cp = null;

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
	public void testCompileArgs() {
		String incDir1 = "/Ir:/sandbox/net.bd.gdlc/gdl";
		String incDir2 = "/Ir:/sandbox/net.bd.gdlc/gdl/tests";
		String args[] = {new String("r:\\sandbox\\net.bd.gdlc\\gdl\\tests\\compileTest.gdl"),
						incDir1,};
		this.cp.process(args);

		CompileMgr mgr = new CompileMgr();
		mgr.execute(this.cp);
		
		assertNotNull("Context is null.",mgr.getContext());
		CompilerContext ctx = (CompilerContext)mgr.getContext();
		
		int dirCount = 0;
		for(String path : ctx.includeDirs){
			if(path.equals(incDir1.substring(2).replace('/', '\\')))
				dirCount++;
			if(path.equals(incDir2.substring(2).replace('/', '\\')))
				dirCount++;
		}
		
		assertEquals("Expected dirs not found", 2, dirCount);
		assertEquals("More dirs than expected found", 2, ctx.includeDirs.size());
		
	}


}
