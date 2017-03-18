package com.matthewchapman.ql.ast.expression;

import com.matthewchapman.ql.ast.Expression;
import com.matthewchapman.ql.ast.QLVisitable;
import com.matthewchapman.ql.validation.visitor.QLVisitor;

/**
 * Created by matt on 24/02/2017.
 * Parameter class, for inserting Question return values into Expressions
 */

public class Parameter extends Expression implements QLVisitable {

    private String id;

    public Parameter(String s, int line, int column) {
        this.id = s;
        super.setLine(line);
        super.setColumn(column);
    }

    public String getID() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public <T, C> T accept(QLVisitor<T, C> visitor, C context) {
        return visitor.visit(this, context);
    }
}