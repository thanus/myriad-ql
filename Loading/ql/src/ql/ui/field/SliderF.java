package ql.ui.field;

import ql.ui.Notifier;
import ql.value.IntegerValue;
import ql.value.Value;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;

public class SliderF implements Field {
	
	private final Slider field;
	
    public SliderF(String name, Notifier notifier, IntegerValue value) {
		
		this.field = new Slider(0, 5, value.getValue());
		field.setId(name);
        
        field.setShowTickLabels(true);
        field.setMajorTickUnit(1);
        field.setBlockIncrement(1);
		
        field.valueProperty().addListener((obs, oldValue, newValue) -> { 
        	field.setValue(newValue.intValue()); 
        	notifier.updateQuestionnaire(name, new IntegerValue(newValue.intValue())); 
        });
	}
	
	@Override
	public Value getAnswer() {
		return new IntegerValue((int) field.getValue());
	}

	@Override
	public void setValue(Value value) {
		field.setValue(((IntegerValue) value).getValue()); 
		
	}
	
	@Override
	public void draw(GridPane grid, int index) {
		grid.add(field, 1, index);	
	}
}
