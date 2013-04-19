package runtime.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import runtime.main.Log;

public class PropertiesPlugin {

	public PropertiesPlugin() {
		super();
	}

	protected void loadProperties(HashMap<String, Integer> propertyList, ArrayList<String> fileList) {
		Properties properties = new Properties();
	    InputStream is = null;
	    File f = null;
		
		for (String path : fileList) {
		    
			Log.info("Loading properties from "+path);
			// Attempt to load the current file.
		    try {
		        f = new File(path);
		        is = new FileInputStream( f );
		        
		        // Try loading properties from the file (if found)
		        properties.load( is );
		        Enumeration<String> keys = (Enumeration<String>) properties.propertyNames();
		        while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					propertyList.put(key, new Integer(properties.getProperty(key)));
					// Example: Read a string
					// serverAddr = props.getProperty("ServerAddress", "192.168.0.1");
					// Example: Read an integer
					// serverPort = new Integer(props.getProperty("ServerPort", "8080"));
				}
		    }
		    catch ( Exception e ) {
		    	Log.error("Error while loading properties from "+path+"  Exception: "+e.getMessage());
		    }
		}
	}

}