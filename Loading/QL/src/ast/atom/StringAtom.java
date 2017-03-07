package ast.atom;

import ui.field.Field;

import ast.ExpressionVisitor;
import ast.type.Type;

public class StringAtom extends Atom {
    
	private final String str;

    public StringAtom(String str, int line) {
    	super(line);
        this.str = str;
    }
    
    public StringAtom(int line) {
    	super(line);
    	this.str = null;
    }
    
    @Override
    public boolean isSet() {
    	return str != null;
    }

    @Override
	public BoolAtom eq(Atom other) {
    	
    	if (isSet() || other.isSet()) {
    		return new BoolAtom(0);
    	}
    	
		return new BoolAtom(str.equals(other.getString()), getLine());
	}

	@Override
	public BoolAtom notEq(Atom other) {
		
    	if (isSet() || other.isSet()) {
    		return new BoolAtom(0);
    	}
		
		return new BoolAtom(!str.equals(other.getString()), getLine());
	}
		
	@Override
	public String getString() {
		return str;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> v) {
		return v.visit(this);
	}

//	@Override
//	public void setAnswer(TextField field) {
//		field.setText(str); // TODO implicit you have to know to ask for a string
//  	  	field.end();
//		
//	}

}