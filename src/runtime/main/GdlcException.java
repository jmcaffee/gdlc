/**
 * Base class for all GDLC Exceptions
 */

package runtime.main;

import java.lang.Exception;

public class GdlcException extends Exception {
    public GdlcException(String message) {
        super(message);
    }
}

