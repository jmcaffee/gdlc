package runtime.elements;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConditionMsgTest extends ConditionMsg {

	public ConditionMsgTest() {
	//public ConditionMsgTest(CompilerContext ctx, String identifier) {
	//	super(ctx, identifier);
		super(null, null);
		// TODO Auto-generated constructor stub
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildXmlRefElement() {
		this.id = 1;
		this.identifier = new String("TestCondition");
		this.category = "Category";
		this.priorTo = "PriorTo";
		this.visibility = "External";
		this.msg = new StringBuffer("Test Condition");
		
		XmlElem xml = (XmlElem) this.buildXmlRefElement(1);
		
		String expected = "<Message Type=\"Condition\" Id=\"1\" Order=\"1\"/>";
		assertEquals(expected, xml.toXml());
	}

}
