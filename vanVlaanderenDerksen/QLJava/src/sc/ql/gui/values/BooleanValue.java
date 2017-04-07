package sc.ql.gui.values;

public class BooleanValue extends Value {
	private final Boolean value;
	
	public BooleanValue(Boolean value) {
		this.value = value;
	}
	
	public Boolean getValue() {
		return value;
	}
	
	@Override
    public BooleanValue negate() {
        return new BooleanValue(!this.value);
    }
	
	@Override
	public Value and(Value value) {
        return value.and(this);
    }
	
	@Override
	public BooleanValue and(BooleanValue value) {
        return new BooleanValue(this.value && value.getValue());
    }
	
	@Override
	public Value or(Value value) {
        return value.and(this);
    }
	
	@Override
	public BooleanValue or(BooleanValue value) {
        return new BooleanValue(this.value || value.getValue());
    }
	
}
