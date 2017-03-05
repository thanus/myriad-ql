package ast.type;

import ast.Node;
import ast.TypeVisitor;
import ui.field.Field;
import value.Value;

public abstract class Type extends Node {
	
	final private String keyWord;
	
	public Type(String keyWord, int line) {
		super(line);
		this.keyWord = keyWord;
	}
	
	public String getKeyWord() {
		return keyWord;
	}
	
	public abstract Field getField(String name);

	public abstract void accept(TypeVisitor v);

}