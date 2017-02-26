/**
 * InvalidTypeError.java.
 */

package semanticChecker.messageHandling.errors;

import ASTnodes.LineNumber;
import ASTnodes.types.Type;

public class InvalidTypeError extends ErrorHandler {

    private final Type validType;

    public InvalidTypeError(LineNumber location, Type validType) {
        super(location);
        this.validType = validType;
    }

    public Type getValidType() {
        return validType;
    }

    public String getMessage() {
        return "ERROR: Invalid type at line " + getLocation().getStartingLine() + ". Type should be " +
                validType.toString() + ".";
    }
}
