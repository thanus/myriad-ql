/*
 * Software Construction - University of Amsterdam
 *
 * ./src/qls/astnodes/widgets/QLSUndefinedWidget.java.
 *
 * Gerben van der Huizen    -   10460748
 * Vincent Erich            -   10384081
 *
 * March, 2017
 */
package qls.astnodes.widgets;

import ql.astnodes.LineNumber;
import ql.astnodes.types.Type;
import qls.visitorinterfaces.WidgetVisitor;

import java.util.ArrayList;
import java.util.List;


public class QLSUndefinedWidget extends QLSWidget {

    public QLSUndefinedWidget(LineNumber lineNumber) {
        super(lineNumber);
    }

    @Override
    public List<Type> getSupportedQuestionTypes() {
        return new ArrayList<>();
    }

    @Override
    public boolean isUndefined() {
        return true;
    }

    public <T> T accept(WidgetVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
