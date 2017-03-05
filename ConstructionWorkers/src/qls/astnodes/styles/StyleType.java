package qls.astnodes.styles;

import ql.astnodes.LineNumber;
import ql.astnodes.Node;
import qls.astnodes.visitors.StyleSheetVisitor;

import javax.sound.sampled.Line;

/**
 * Created by LGGX on 03-Mar-17.
 */
public abstract class StyleType extends Node{

    private final String name;
    private final String value;

    public StyleType(String name, String value, LineNumber lineNumber) {
        super(lineNumber);
        this.name = name;
        this.value = value;

    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public abstract <T> T accept(StyleSheetVisitor<T> visitor);

    @Override
    public boolean equals(Object o){
        if (o == null) {
            return false;
        }

        if (!(o instanceof StyleType)) {
            return false;
        }

        StyleType other = (StyleType) o;
        return this.name == other.name;
    }



}