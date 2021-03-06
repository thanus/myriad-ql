package ql.ui.field;

import ql.ui.Notifier;
import ql.value.BoolValue;
import ql.value.Value;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

public class CheckBoxF implements Field {

	private final CheckBox field;
	
	public CheckBoxF(String name, Notifier notifier, BoolValue value) {
		
		this.field = new CheckBox();
		field.setId(name);
		
		field.setSelected(value.getValue());
		
		field.selectedProperty().addListener(
				(observable, oldValue, newValue) -> notifier.updateQuestionnaire(name, new BoolValue(newValue)));
	}
	
	@Override
	public Value getAnswer() {
		return new BoolValue(field.isSelected());
	}

	@Override
	public void setValue(Value value) {
		field.setSelected(((BoolValue) value).getValue()); 
		
	}

	@Override
	public void draw(GridPane grid, int index) {
		grid.add(field, 1, index);	
	}

}
