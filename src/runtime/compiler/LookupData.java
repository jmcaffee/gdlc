package runtime.compiler;

import java.util.ArrayList;

public class LookupData {
	public class MinMax {
		String[] minmax = new String[2];
		public MinMax(String min, String max){ minmax[0] = min; minmax[1] = max; }
		
		public void setMin(String min){minmax[0] = min; }
		public void setMax(String max){minmax[1] = max; }
		public String getMin(){return minmax[0];}
		public String getMax(){return minmax[1];}
	}

	public String name = new String();
	
	public ArrayList<MinMax> colMinMaxs = new ArrayList<MinMax>();
	public ArrayList<MinMax> rowMinMaxs = new ArrayList<MinMax>();

	public ArrayList<String> values = new ArrayList<String>();
	
}
