/**
 * CyclicDependencyError.java.
 */

package semanticChecker.messageHandling.errors;

import ASTnodes.LineNumber;
import ASTnodes.expressions.literals.Identifier;

public class CyclicDependencyError extends ErrorHandler {

    private final Identifier end;
    private final Identifier start;

    public CyclicDependencyError(LineNumber location, Identifier end, Identifier start) {
        super(location);
        this.end = end;
        this.start = start;
    }

    public String getMessage() {
        return "ERROR: Cyclomatic dependency between " + end.getName() + " and " +  start.getName() +
                " at line " +  end.getLocation().getStartingLine() + ".";
    }
}
