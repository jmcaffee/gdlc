package runtime.elements;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BaseElem {
	public HashMap 		attribs 	= new HashMap();
	protected ArrayList children = new ArrayList();

	public String getAttribute(String key) {
		String theAttrib = null;
		
		if(this.attribs.containsKey(key)) {
			theAttrib = (String)this.attribs.get(key);
		}
		
		return theAttrib;
	}
	
	public void putAttribute(String key, String value) {
		if(this.attribs.containsKey(key)) {
			// return a warning
		}
		
		this.attribs.put(key, value);
	}
	
	abstract public String getTag();

	public void addChild(BaseElem elem) {
		children.add(elem);
	}

	public BaseElem getChild(int index) {
		return (BaseElem)children.get(index);
	}

	
}
