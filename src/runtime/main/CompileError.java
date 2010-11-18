package runtime.main;

/**
 * @author killer
 *
 */
public class CompileError implements IProblem {
	int		id;
	String	desc;
	String 	msg;
	
	static public class errors {
		static public int UNKNOWN = 0;
		static public int DEFMISSING = 1;
		static public int DATAMISSING = 2;
		static public int FILENOTFOUND = 3;
		static public int UNSUPPORTEDOPERATION = 4;
		static public int IMPORTERROR = 5;
		static public int PARSEERROR = 6;
		static public int UNEXPECTEDARG = 7;
		static public int MISSINGARG = 8;
		static public int INCLUDEDIRPATH = 9;
		static public int OUTPUTERROR = 10;
		static public int BADCAST = 11;
		static public int LASTERR = 11;
		
		static String[] desc = {
			"Unknown error has occurred",
			"A reference to an undefined object has occurred",
			"Data does not exist for this object",
			"File not found",
			"Unsupported operation",
			"Import operation error",
			"An error has occurred during parsing",
			"Unexpected argument found",
			"Argument missing",
			"Check INCLUDE dir paths",
			"An output error has occurred",
			"Bad cast",
		};

		static String getErrorDesc(int id){
			if(id <= CompileError.errors.LASTERR){
				return CompileError.errors.desc[id];
			}
			
			return CompileError.errors.desc[CompileError.errors.UNKNOWN];
		}
		
		  
	}

	/**
	 * Constructor
	 * @param id problem ID
	 * @param desc description of problem
	 * @param msg problem message
	 */
	public CompileError(int id, String desc, String msg) {
		this.id = id;
		this.desc = desc;
		this.msg = msg;
	}

	/**
	 * Constructor
	 * @param id problem ID
	 * @param msg problem message
	 */
	public CompileError(int id, String msg) {
		this.id = id;
		this.desc = CompileError.errors.getErrorDesc(id);
		this.msg = msg;
	}

	/* (non-Javadoc)
	 * @see runtime.main.IProblem#getDesc()
	 */
	public String getDesc() {
		return this.desc;
	}

	/* (non-Javadoc)
	 * @see runtime.main.IProblem#setDesc(java.lang.String)
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/* (non-Javadoc)
	 * @see runtime.main.IProblem#getId()
	 */
	public int getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see runtime.main.IProblem#setId(int)
	 */
	public void setId(int id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see runtime.main.IProblem#getMsg()
	 */
	public String getMsg() {
		return this.msg;
	}

	/* (non-Javadoc)
	 * @see runtime.main.IProblem#setMsg(java.lang.String)
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
