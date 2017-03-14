package com.matthewchapman.ql.ast.expression.binary;

import com.matthewchapman.ql.ast.Expression;
import com.matthewchapman.ql.ast.QLVisitable;
import com.matthewchapman.ql.validation.QLVisitor;

/**
 * Created by matt on 24/02/2017.
 * <p>
 * Integer & Boolean equality class
 */
public class Equal extends BinaryOperation implements QLVisitable {

    public Equal(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public <T> T accept(QLVisitor<T> visitor, String context) {
        return visitor.visit(this, context);
    }
}