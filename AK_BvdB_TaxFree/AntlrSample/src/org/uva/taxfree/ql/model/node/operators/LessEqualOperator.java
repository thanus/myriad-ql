package org.uva.taxfree.ql.model.node.operators;

import org.uva.taxfree.ql.model.node.expression.ExpressionNode;

public class LessEqualOperator extends CompareOperator {
    @Override
    public String evaluate(ExpressionNode left, ExpressionNode right) {
        return new Boolean(left.asInteger() <= right.asInteger()).toString();
    }
}