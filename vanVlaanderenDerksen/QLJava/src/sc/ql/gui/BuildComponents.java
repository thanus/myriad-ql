package sc.ql.gui;

import java.awt.Font;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import sc.ql.gui.values.*;
import sc.ql.gui.widgets.Widget;
import sc.ql.model.Form;
import sc.ql.model.FormElement;
import sc.ql.model.expressions.*;
import sc.ql.model.expressions.arithmetical.*;
import sc.ql.model.expressions.literals.*;
import sc.ql.model.expressions.logical.*;
import sc.ql.model.form_elements.*;
import sc.ql.model.visitors.*;

public class BuildComponents implements FormVisitor<JPanel>, FormElementVisitor<JPanel>, ExpressionVisitor<Value> {
	private Form form;
	private Map<String, Value> questionValues;
	private JPanel panel;
	
	public BuildComponents(Form form, Map<String, Value> questionValues, JPanel panel) {
		this.form = form;
		this.questionValues = questionValues;
		this.panel = panel;
		
		visit(this.form);
	}
	
	public void updatePanel(String questionId, Value value) {		
		panel.removeAll();
		questionValues.put(questionId, value);
		
		visit(this.form);
		panel.validate();
		panel.repaint();
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
	
	@Override
	public JPanel visit(Form form) {
		for(FormElement formElement : form.getFormElements()) {
			panel.add(formElement.accept(this), "span");
		}
		
		return panel;
	}
	
	@Override
	public JPanel visit(IfThenStatement statement) {
		Value condition = statement.getCondition().accept(this);
		Boolean visible = condition.toBoolean();
		
		JPanel statementPanel = new JPanel();
		statementPanel.setLayout(new MigLayout());
		
		if (visible) {
			for(FormElement formElement : statement.getThenBody()) {
				statementPanel.add(formElement.accept(this), "span");
			}
		}
		
		return statementPanel;
	}
	
	@Override
	public JPanel visit(IfThenElseStatement statement) {
		Value condition = statement.getCondition().accept(this);
		Boolean visible = condition.toBoolean();
		
		JPanel statementPanel = new JPanel();
		statementPanel.setLayout(new MigLayout());
		
		List<FormElement> visibleBody = visible ? statement.getThenBody() : statement.getElseBody();
		
		for(FormElement formElement : visibleBody) {
			statementPanel.add(formElement.accept(this), "span");
		}
		
		return statementPanel;
	}
	
	@Override
	public JPanel visit(Question question) {	
		JPanel questionPanel = new JPanel();
		String questionId = question.getId();
		
		Value value = questionValues.containsKey(questionId) ? questionValues.get(questionId) : new EmptyValue();
		questionValues.put(questionId, value);
		
		JLabel label = new JLabel(question.getLabel());
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		questionPanel.add(label);
		
		Widget widgetComponent = question.getType().getWidget(this, questionId, value);
		questionPanel.add(widgetComponent.getComponent());

		return questionPanel;
	}
	
	@Override
	public JPanel visit(CalculatedQuestion question) {
		JPanel questionPanel = new JPanel();
		String questionId = question.getId();
		
		Value value = question.getExpression().accept(this);
		questionValues.put(question.getId(), value);
		
		JLabel label = new JLabel(question.getLabel());
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		questionPanel.add(label);
		
		Widget widgetComponent = question.getType().getWidget(this, questionId, value);
		questionPanel.add(widgetComponent.getComponent());
		
		return questionPanel;
	}

	@Override
	public Value visit(BinaryExpression expression) {
		return null;
	}

	@Override
	public Value visit(NotExpression expression) {
		Value value = expression.getExpression().accept(this);
		
		return value.negate();
	}

	@Override
	public Value visit(Add expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.add(value_right);
	}

	@Override
	public Value visit(Divide expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.divide(value_right);
	}

	@Override
	public Value visit(Multiply expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.multiply(value_right);
	}

	@Override
	public Value visit(Subtract expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.subtract(value_right);
	}

	@Override
	public Value visit(BooleanLiteral expression) {
		return new BooleanValue(expression.getValue());
	}

	@Override
	public Value visit(IdLiteral expression) {
		String questionId = expression.getValue();
		Value value = questionValues.containsKey(questionId) ? questionValues.get(questionId) : new EmptyValue();		
		return value;
	}

	@Override
	public Value visit(IntegerLiteral expression) {
		return new IntegerValue(expression.getValue());
	}

	@Override
	public Value visit(MoneyLiteral expression) {
		return new MoneyValue(expression.getValue());
	}

	@Override
	public Value visit(StringLiteral expression) {
		return new StringValue(expression.getValue());
	}

	@Override
	public Value visit(And expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.and(value_right);
	}
	
	@Override
	public Value visit(Or expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.or(value_right);
	}

	@Override
	public Value visit(Equals expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.equals(value_right);
	}

	@Override
	public Value visit(EqualsNot expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.equalsNot(value_right);
	}

	@Override
	public Value visit(GreaterThen expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.greaterThen(value_right);
	}

	@Override
	public Value visit(GreaterThenEqual expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.greaterThenEqual(value_right);
	}

	@Override
	public Value visit(LessThen expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.lessThen(value_right);
	}

	@Override
	public Value visit(LessThenEqual expression) {
		Value value_left = expression.getLeft().accept(this);
		Value value_right = expression.getRight().accept(this);
		
		return value_left.lessThenEqual(value_right);
	}
	
}
