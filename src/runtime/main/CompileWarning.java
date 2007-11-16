/**
 * 
 */
package runtime.main;

/**
 * @author killer
 *
 */
public class CompileWarning implements IProblem {
		int		id;
		String	desc;
		String 	msg;
		
		static public class warnings {
			static public int UNKNOWN = 0;
			static public int REDEFINITION = 1;
			static public int CSVLINELENGTH = 2;
			static public int LASTWARNING = 2;
						
			static String[] desc = {
				"Unknown error has occurred",
				"An object has been redefined",
				"CSV file contains inconsistent line lengths",
			};

			static String getWarningDesc(int id){
				if(id <= CompileWarning.warnings.LASTWARNING){
					return CompileWarning.warnings.desc[id];
				}
				
				return CompileWarning.warnings.desc[CompileWarning.warnings.UNKNOWN];
			}
			
			  
		}

		/**
		 * Constructor
		 * @param id problem ID
		 * @param desc description of problem
		 * @param msg problem message
		 */
		public CompileWarning(int id, String desc, String msg) {
			this.id = id;
			this.desc = desc;
			this.msg = msg;
		}

		/**
		 * Constructor
		 * @param id problem ID
		 * @param msg problem message
		 */
		public CompileWarning(int id, String msg) {
			this.id = id;
			this.desc = CompileWarning.warnings.getWarningDesc(id);
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
