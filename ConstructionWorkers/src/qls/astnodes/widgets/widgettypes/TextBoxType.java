package qls.astnodes.widgets.widgettypes;

/**
 * Created by LGGX on 03-Mar-17.
 */
public class TextBoxType implements WidgetType {

    public <T> T accept(WidgetTypeInterface<T> visitor) {
        return visitor.visit(this);
    }
}
