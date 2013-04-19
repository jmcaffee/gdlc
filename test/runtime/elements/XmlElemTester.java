package runtime.elements;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XmlElemTester {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTag() {
		XmlElem elem = new XmlElem("TestTag");
		
		assertEquals("getTagFailed", "TestTag", elem.getTag());
	}

	@Test
	public void testGetAttributesAsXml() {
		XmlElem elem = new XmlElem("TestTag");
		
		elem.putAttribute("Att1", "1");
		elem.putAttribute("Att2", "2");
		elem.putAttribute("Att3", "3");
		
		String[] atts = {"Att1", "Att2", "Att3"};
		
		elem.setAttributeOrder(atts);
		
		String result = elem.getAttributesAsXml(atts);
		String expected = new String(" Att1=\"1\" Att2=\"2\" Att3=\"3\"");
		
		assertEquals("attribute string is incorrect", expected, result);
	}

	@Test
	public void testAppendXml() {
		XmlElem elem = new XmlElem("TestTag");
		String content = new String("content");
		String expected = new String("<TestTag>content</TestTag>");
		
		elem.appendXml(content);
		String result = elem.toXml();
		
		assertEquals("Content not set correctly", expected, result);
	}

	@Test
	public void testToXml() {
		XmlElem elem = new XmlElem("TestTag");
		String content = new String("content");
		String expected = new String("<TestTag>content</TestTag>");
		
		elem.appendXml(content);
		String result = elem.toXml();
		
		assertEquals("Content not set correctly", expected, result);
	}

	@Test
	public void testToXmlWithAttributes() {
		XmlElem elem = new XmlElem("TestTag");
		String content = new String("content");
		String expected = new String("<TestTag Att1=\"1\" Att2=\"2\" Att3=\"3\">content</TestTag>");
		String[] atts = {"Att1", "Att2", "Att3"};
		
		elem.setAttributeOrder(atts);
		
		elem.putAttribute("Att1", "1");
		elem.putAttribute("Att2", "2");
		elem.putAttribute("Att3", "3");
		
		elem.appendXml(content);
		String result = elem.toXml();
		
		assertEquals("Content not set correctly", expected, result);
	}

	@Test
	public void testShortTag() {
		XmlElem elem = new XmlElem("TestTag");
		String content = new String("content");
		
		// To get '<TestTag />', -validxml switch must be supplied to compiler.
		// Default is '<TestTag/>' to match what AMS actually puts out.
		String expectedNoContent 	= new String("<TestTag/>");
		String expectedWithContent 	= new String("<TestTag>content</TestTag>");
		
		elem.setIsShortTag(true);		// This element should default to short style if
										// there is no content.
		
		String resultNoContent = elem.toXml();
		assertEquals("Short tag not closed correctly.", expectedNoContent, resultNoContent);
		
		elem.appendXml(content);
		String result = elem.toXml();
		
		assertEquals("Short tag not closed correctly when content is present.", expectedWithContent, result);
	}


}
