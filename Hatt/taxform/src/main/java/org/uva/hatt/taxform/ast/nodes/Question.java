package org.uva.hatt.taxform.ast.nodes;

public class Question extends ASTNode{

    private String question;
    private String value;
    private ValueType type;

    public Question(int lineNumber) {
        super(lineNumber);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
