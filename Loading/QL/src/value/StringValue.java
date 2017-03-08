package value;

import value.Value;
import value.BoolValue;

public class StringValue extends Value {

	private final String value;

    public StringValue(String value) {
        this.value = value;
    }
    
    public StringValue() {
    	this.value = null;
    }
    
    @Override
    public boolean isSet() {
    	return value != null;
    }

    @Override
	public BoolValue eq(Value other) {
    	
    	if (!isSet() || !other.isSet()) {
    		return new BoolValue();
    	}
    	System.out.println("TODO does this work?");
    	// return new BoolValue (this.equals(other));
    	
		return new BoolValue(value.equals(((StringValue) other).getValue()) );
	}

	@Override
	public BoolValue notEq(Value other) {
		
		
    	if (!isSet() || !other.isSet()) {
    		return new BoolValue();
    	}
    	
    	System.out.println("TODO does this work?");
    	// return new BoolValue (!this.equals(other));
		
		return new BoolValue(!value.equals(((StringValue) other).getValue()) );
	}
		
	public String getValue() {
		return this.value;
	}

	public String convertToString() {
    	return this.value;
    }
}