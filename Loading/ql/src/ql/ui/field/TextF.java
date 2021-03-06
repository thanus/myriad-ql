package ql.ui.field;

import ql.ui.Notifier;
import ql.value.StringValue;
import ql.value.Value;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class TextF implements Field {
	
	private final TextField field;
	
	public TextF(String name, Notifier notifier, StringValue value) {
		this.field = new TextField();
		
		field.setId(name);
		field.setText(value.getValue());
		field.positionCaret(value.getValue().length());
		
		field.textProperty().addListener(
				(observable, oldValue, newValue) -> notifier.updateQuestionnaire(name, new StringValue(newValue)));
	}
	
	@Override
	public Value getAnswer() {
		if (field.getText().isEmpty()) {
			return new StringValue();
		}	
		return new StringValue(field.getText());
	}

	@Override
	public void setValue(Value value) {
		field.setText(((StringValue) value).getValue());
		
	}
	
	@Override
	public void draw(GridPane grid, int index) {
		grid.add(field, 1, index);	
	}
	

}
