package ql.semanticchecker.messagehandling.errors;

import ql.astnodes.LineNumber;
import ql.astnodes.expressions.literals.Identifier;

/**
 * Created by LGGX on 22-Feb-17.
 */
public class IfExpressionUndefinedError extends Error {

    private final Identifier identifier;

    public IfExpressionUndefinedError(LineNumber location, Identifier identifier) {
        super(location);
        this.identifier = identifier;
    }

    public String getMessage() {
        return "ERROR: Identifier " + identifier.getName() + " at line " + getLocation().getStartingLine() +
                " in IF statement condition is undefined.";
    }
}