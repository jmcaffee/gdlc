/**
 * This exception is thrown when parse errors are encountered.
 * You can explicitly create objects of this exception type by
 * calling the method generateParseException in the generated
 * parser.
 *
 * You can modify this class to customize your error reporting
 * mechanisms so long as you retain the public fields.
 */
package runtime.compiler;

import runtime.main.GdlcException;

public class CompileException extends GdlcException {
      public CompileException(String message) {
        super(message);
      }

      /**
       * The end of line string for this machine.
       */
      protected String eol = System.getProperty("line.separator", "\n");

      static final long serialVersionUID = 10001;
      /**
       * Used to convert raw characters to their escaped version
       * when these raw version cannot be used as part of an ASCII
       * string literal.
       */
      protected String add_escapes(String str) {
          StringBuffer retval = new StringBuffer();
          char ch;
          for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i))
            {
               case 0 :
                  continue;
               case '\b':
                  retval.append("\\b");
                  continue;
               case '\t':
                  retval.append("\\t");
                  continue;
               case '\n':
                  retval.append("\\n");
                  continue;
               case '\f':
                  retval.append("\\f");
                  continue;
               case '\r':
                  retval.append("\\r");
                  continue;
               case '\"':
                  retval.append("\\\"");
                  continue;
               case '\'':
                  retval.append("\\\'");
                  continue;
               case '\\':
                  retval.append("\\\\");
                  continue;
               default:
                  if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e) {
                     String s = "0000" + Integer.toString(ch, 16);
                     retval.append("\\u" + s.substring(s.length() - 4, s.length()));
                  } else {
                     retval.append(ch);
                  }
                  continue;
            }
          }
          return retval.toString();
       }

}
