package ql.ui.field;

import ql.ui.Notifier;
import ql.value.IntegerValue;
import ql.value.Value;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class NumberF implements Field {
	
	private final TextField field;
	
	public NumberF(String name, Notifier notifier, IntegerValue value) {
		this.field = new TextField();
		
		field.setId(name);

		String valueText = Integer.toString(value.getValue());
		field.setText(valueText);
		field.positionCaret(valueText.length());
		
    	field.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.matches("-?\\d*")) {
                field.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (!newValue.isEmpty()) {
                notifier.updateQuestionnaire(name, new IntegerValue(Integer.parseInt(newValue)));
            } else {
                notifier.updateQuestionnaire(name, new IntegerValue());
            }
        });
	}

	@Override
	public Value getAnswer() {
		String str = field.getText();
		if (str.isEmpty()) {
			return new IntegerValue();
		}
		return new IntegerValue(Integer.valueOf(str));
	}

	@Override
	public void setValue(Value value) {
		field.setText(Integer.toString(((IntegerValue) value).getValue()));	
	}
	
	@Override
	public void draw(GridPane grid, int index) {
		grid.add(field, 1, index);	
	}
	
}
