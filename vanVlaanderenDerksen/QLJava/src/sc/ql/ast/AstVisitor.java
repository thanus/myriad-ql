package sc.ql.ast;

import java.util.ArrayList;
import java.util.List;

import sc.ql.antlr.QLBaseVisitor;
import sc.ql.antlr.QLParser;
import sc.ql.model.*;
import sc.ql.model.atoms.*;
import sc.ql.model.expressions.*;
import sc.ql.model.form_elements.*;

public class AstVisitor extends QLBaseVisitor<Node> {
	@Override 
	public Node visitForm(QLParser.FormContext ctx) {
		List<Node> form_elements = getFormElements(ctx.form_element());
		
		return new Form(form_elements);
	}

	@Override 
	public Node visitQuestion(QLParser.QuestionContext ctx) {
		String question 		= ctx.STR().toString();
		question 				= question.substring(1, question.length()-1); // Remove the surrounding quotes
		Question.Type type 		= Question.Type.valueOf(ctx.TYPE().toString());		
		Node expression			= ctx.expression() != null ? visit(ctx.expression()) : null;
		Integer line_number 	= ctx.getStart().getLine();
		AtomId id 				= new AtomId(ctx.ID().toString(), line_number);
		
		return new Question(question, id, type, expression, line_number);
	}

	@Override 
	public Node visitIf_statement(QLParser.If_statementContext ctx) {
		List<ConditionalBlock> conditional_blocks = new ArrayList<ConditionalBlock>();
		List<Node> form_elements 		  = getFormElements(ctx.form_element());
		
		for(QLParser.Conditional_blockContext conditional_block : ctx.conditional_block()) {
			conditional_blocks.add((ConditionalBlock) visit(conditional_block));
		}
		
		return new IfStatement(conditional_blocks, form_elements);
	}

	@Override 
	public Node visitConditional_block(QLParser.Conditional_blockContext ctx) {
		Node expression = visit(ctx.expression());
		List<Node> form_elements = getFormElements(ctx.form_element());
		Integer line_number = ctx.getStart().getLine();
		
		return new ConditionalBlock(expression, form_elements, line_number);
	}
	
	@Override 
	public Node visitParenExpr(QLParser.ParenExprContext ctx) {
		return visit(ctx.expression());
	}	
	
	@Override 
	public Node visitNotExpr(QLParser.NotExprContext ctx) {
		Node expression = visit(ctx.expression());
		Integer line_number = ctx.getStart().getLine();
		
		return new NotExpression(expression, line_number);
	}
	
	@Override 
	public Node visitOpExpr(QLParser.OpExprContext ctx) {
		Node left  = visit(ctx.left);	
		Node right = visit(ctx.right);
		String operator  = ctx.op.getText();
		Integer line_number = ctx.getStart().getLine();
		
		if (operator.matches("\\+|-|\\*|/")) {
			return new CalcExpression(left, right, operator, line_number);
		}
		else {
			return new OpExpression(left, right, operator, line_number);
		}
	}

	@Override 
	public Node visitIdAtom(QLParser.IdAtomContext ctx) {
		return new AtomId(ctx.atom.getText(), ctx.getStart().getLine());
	}

	@Override 
	public Node visitIntAtom(QLParser.IntAtomContext ctx) {
		return new AtomInteger(Integer.parseInt(ctx.atom.getText()), ctx.getStart().getLine());
	}

	@Override 
	public Node visitStrAtom(QLParser.StrAtomContext ctx) {
		return new AtomString(ctx.atom.getText(), ctx.getStart().getLine());
	}

	@Override 
	public Node visitBoolAtom(QLParser.BoolAtomContext ctx) {
		return new AtomBoolean(Boolean.valueOf(ctx.atom.getText()), ctx.getStart().getLine());
	}
	
	/*
	 * Helper function
	 */
	private List<Node> getFormElements(List<QLParser.Form_elementContext> elements) {
		List<Node> form_elements = new ArrayList<Node>();
		for(QLParser.Form_elementContext form_element : elements) {
			form_elements.add((Node) visit(form_element));
		}
		
		return form_elements;
	}
}