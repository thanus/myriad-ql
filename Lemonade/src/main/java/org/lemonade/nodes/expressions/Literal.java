package org.lemonade.nodes.expressions;

import org.lemonade.nodes.types.QLType;

public abstract class Literal<T> extends Expression {
    private QLType type;
    private T value;

    public Literal(QLType type, T value) {
        super();
        this.type = type;
        this.value = value;
    }

    public QLType getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }
}
