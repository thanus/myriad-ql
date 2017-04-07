package sc.ql.model.types;

import javax.swing.JPanel;

import sc.ql.gui.widgets.IntegerWidget;
import sc.ql.gui.widgets.Widget;

public class IntegerType extends Type {

	@Override
	public String toString() {
		return "Integer";
	}
	
	@Override
	public Boolean isNumericType() {
		return true;
	}
	
	@Override
	public Boolean isCompatibleWith(Type type) {
		return type.isCompatibleWith(this);
	}
	
	@Override
	public Boolean isCompatibleWith(IntegerType type) {
		return true;
	}
	
	@Override
	public Widget getWidget(JPanel panel) {
		return new IntegerWidget(panel);
	}
	
}
