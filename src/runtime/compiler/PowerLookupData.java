package runtime.compiler;

import java.util.ArrayList;


public class PowerLookupData {
	public class VarOp {
		String	name 	= null;
		String	typ		= null;
		String	op		= null;
		
		public VarOp(String name, String typ, String op){ this.name = name; this.typ = typ; this.op = op; }
		public VarOp(String name){ this.name = name;}
		
		public void setName(String name){this.name = name; }
		public void setType(String typ){this.typ = typ; }
		public void setOp(String op){this.op = op; }
		public String getName(){return this.name;}
		public String getType(){return this.typ;}
		public String getOp(){return this.op;}
	}

	public String name = new String();
											// Operations
	public ArrayList<VarOp> operations = new ArrayList<VarOp>();
	
	public ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
	

}
