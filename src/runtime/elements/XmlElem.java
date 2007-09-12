package runtime.elements;


/**
 * @author killer
 *
 */

public class XmlElem extends BaseElem {

	String			elemTag		= null;
	StringBuffer    content		= new StringBuffer();
	boolean			isShortTag	= false;
	String[]		attributeOrder = null;
	
	public XmlElem(String tag) {
		this.elemTag = tag;
	}
	
	/* (non-Javadoc)
	 * @see runtime.elements.BaseElem#getTag()
	 */
	@Override
	public String getTag() {
		return this.elemTag;
	}
	
	/**
	 * getContent - return the element's content
	 * @return - String containing element content.
	 */
	public String getContent() { return this.content.toString(); }
	
	/**
	 * clearContext - delete all element content.
	 * Note: Does not change attributes or tag name.
	 *
	 */
	public void clearContent(){ content.setLength(0);}

	public void setIsShortTag(boolean isShort) {this.isShortTag = isShort;}
	
	public void setAttributeOrder(String[] attOrder) {this.attributeOrder = attOrder;}
	
	/**
	 * getAttributesAsXml - build an XML string out of the attribute list.
	 * @param attOrder - String array of names of attributes to include, in order.
	 * @return String - xml version of all requested attributes with values.
	 */
	public String getAttributesAsXml(String[] attOrder) {
		StringBuffer attXml = new StringBuffer();
		String	sep = new String(" ");
		String value;
		StringBuffer attVal = new StringBuffer();
		
		for (String att : attOrder){
			value = this.getAttribute(att);
			if (null != value){
				attVal.append(sep).append(att).append("=\"").append(value).append("\"");
				attXml.append(attVal);
				attVal.setLength(0);			// Clear out the buffer.
			}
		}
		
		return attXml.toString();
	}

	/**
	 * appendXml - append 'content' xml to the current content.
	 * @param xml - String containing xml to append as content.
	 * @return - String containing complete xml content of this element (children).
	 */
	public String appendXml(String xml) {
		this.content.append(xml);
		return this.content.toString();
	}
	
	/**
	 * toXml - return the element data as XML
	 * @return - String containing element XML.
	 */
	public String toXml() {
		StringBuffer xml = new StringBuffer();
		xml.append("<").append(this.getTag());
		
		if(this.attributeOrder != null){
			xml.append(this.getAttributesAsXml(this.attributeOrder));
		}
		
		if(true == this.isShortTag && (this.content.length() < 1)){
			xml.append(" />");
		}
		else {
			xml.append(">");
		}
			
		xml.append(this.content);
		
		if(!this.isShortTag || this.content.length() > 0){
			xml.append("</").append(this.getTag()).append(">");
		}
		
		return xml.toString();
	}
}
