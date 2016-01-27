package runtime.compiler;

import java.util.ArrayList;


public class PowerLookupData {
	public class VarOp {
		String	name 	= null;
		String	typ		= null;
		String	op		= null;
		String	cast	= new String();
		
		public VarOp(String name, String typ, String op, String cast){ this.name = name; this.typ = typ; this.op = op; this.cast = cast; }
		public VarOp(String name){ this.name = name;}
		
		public void setName(String name){this.name = name; }
		public void setType(String typ){this.typ = typ; }
		public void setOp(String op){this.op = op; }
		public void setCast(String cast) {this.cast = cast;	}

		public String getName(){return this.name;}
		public String getType(){return this.typ;}
		public String getOp(){ return stripPrefix(this.op);}
		public String getCast() {return cast;}

		String stripPrefix(String val) {
			if (val.startsWith("True Action:")) {
				// Don't strip the True Action: prefix if the op is 'True Action:Condition'
				if (!val.endsWith("Condition")) {
					return val.substring(12);
				}
			}

			return val;
		}
	}

	public String name 		= new String();
	public String exeType 	= new String();		// Execution type (true, continue)
												// Operations
	public ArrayList<VarOp> operations = new ArrayList<VarOp>();
	
	public ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
}
