package org.uva.taxfree.model.types;

import org.uva.taxfree.gui.QuestionForm;
import org.uva.taxfree.model.node.operators.BooleanOperator;
import org.uva.taxfree.model.node.operators.CompareOperator;
import org.uva.taxfree.model.node.operators.NumericOperator;
import org.uva.taxfree.model.node.widgets.StringWidget;

public class StringType extends Type {
    @Override
    public boolean supports(NumericOperator numericOperator) {
        return false;
    }

    @Override
    public boolean supports(BooleanOperator booleanOperator) {
        return false;
    }

    @Override
    public boolean supports(CompareOperator compareOperator) {
        return false;
    }

    @Override
    public void generateWidget(String label, String id, QuestionForm frame) {
        frame.addWidget(new StringWidget(label, id));
    }
}
