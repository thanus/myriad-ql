package UvA.Gamma.AST.Values;

import UvA.Gamma.AST.ASTNode;

/**
 * Created by Tjarco, 14-02-17.
 */

public class Boolean implements ASTNode, Value {
    private boolean value;

    public Boolean(boolean value) {
        this.value = value;
    }

    public Boolean(String value) {
        setValue(value);
    }

    @Override
    public void setValue(String value) {
        this.value = java.lang.Boolean.valueOf(value);
    }

    @Override
    public boolean canAcceptValue(String value) {
        return Boolean.isBoolean(value);
    }

    public boolean getValue() {
        return value;
    }

    public boolean and(Boolean other) {
        return this.value && other.getValue();
    }

    public boolean or(Boolean other) {
        return this.value || other.getValue();
    }

    public boolean equals(Boolean other) {
        return this.value == other.getValue();
    }

    @Override
    public String computableString() {
        return toString();
    }

    @Override
    public String toString() {
        return "" + this.value;
    }

    public static boolean isBoolean(String value) {
        return value.toLowerCase().equals("true") || value.toLowerCase().equals("false");
    }
}