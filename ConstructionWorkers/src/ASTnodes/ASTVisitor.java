package ASTnodes; /**
 * ASTnodes.ASTVisitor.java.
 */

import ASTnodes.expressions.Expression;
import ASTnodes.expressions.binaries.equality.*;
import ASTnodes.expressions.binaries.logic.AND;
import ASTnodes.expressions.binaries.logic.Logic;
import ASTnodes.expressions.binaries.logic.OR;
import ASTnodes.expressions.binaries.numerical.*;
import ASTnodes.expressions.literals.*;
import ASTnodes.expressions.literals.MyInteger;
import ASTnodes.expressions.unaries.*;
import ASTnodes.statements.ComputedQuestion;
import ASTnodes.statements.IfStatement;
import ASTnodes.statements.SimpleQuestion;
import ASTnodes.statements.Statement;
import ASTnodes.types.*;
import antlr.QLBaseVisitor;
import antlr.QLParser;
import antlr.QLVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ASTVisitor extends QLBaseVisitor<Node> implements QLVisitor<Node> {

    private final Form abstractSyntaxTree;

    private static final String GRAMMAR_ERROR = "Why u no work!";

    public ASTVisitor(ParseTree parseTree) {
        abstractSyntaxTree = (Form) parseTree.accept(this);
    }

    public Form getAbstractSyntaxTree() {
        return abstractSyntaxTree;
    }

    public String getGrammarErrorMessage(String expression) {
        return MessageFormat.format(ASTVisitor.GRAMMAR_ERROR, expression);
    }

    @Override
    public Form visitForm(QLParser.FormContext ctx) {
        Identifier identifier = new Identifier(ctx.IDENTIFIER().getText(), getLineNumber(ctx));
        List<Statement> statements = new ArrayList<>();

        for (QLParser.StatementContext currentStatement : ctx.statement()) {
            statements.add((Statement) currentStatement.accept(this));
        }

        return new Form(identifier, statements, getLineNumber(ctx));
    }

    @Override
    public SimpleQuestion visitSimpleQuestion(QLParser.SimpleQuestionContext ctx) {
        Type type = (Type) ctx.type().accept(this);
        Identifier identifier = new Identifier(ctx.IDENTIFIER().getText(), getLineNumber(ctx));
        String label = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);

        return new SimpleQuestion(identifier, label, type, getLineNumber(ctx));
    }

    @Override
    public ComputedQuestion visitComputedQuestion(QLParser.ComputedQuestionContext ctx) {
        Type type = (Type) ctx.type().accept(this);
        Identifier identifier = new Identifier(ctx.IDENTIFIER().getText(), getLineNumber(ctx));
        String label = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);
        Expression expression = (Expression) ctx.expression().accept(this);

        return new ComputedQuestion(identifier, label, type, expression, getLineNumber(ctx));
    }

    @Override
    public IfStatement visitIfStatement(QLParser.IfStatementContext ctx) {
        Expression expression = (Expression) ctx.expression().accept(this);

        List<Statement> statements = new ArrayList<>();

        for (QLParser.StatementContext section : ctx.statement()) {
            Statement stat = (Statement) section.accept(this);
            statements.add(stat);
        }

        return new IfStatement(expression, statements, getLineNumber(ctx));
    }

    @Override
    public IntegerType visitIntType(QLParser.IntTypeContext ctx) {
        return new IntegerType(getLineNumber(ctx));
    }

    @Override
    public StringType visitStringType(QLParser.StringTypeContext ctx) {
        return new StringType(getLineNumber(ctx));
    }

    @Override
    public BooleanType visitBoolType(QLParser.BoolTypeContext ctx) {
        return new BooleanType(getLineNumber(ctx));
    }

    @Override
    public MoneyType visitMoneyType(QLParser.MoneyTypeContext ctx) {
        return new MoneyType(getLineNumber(ctx));
    }

    @Override
    public Equality visitComparisonExpression(QLParser.ComparisonExpressionContext ctx) {
        Expression left = (Expression) ctx.expression().get(0).accept(this);
        Expression right = (Expression) ctx.expression().get(1).accept(this);

        switch (ctx.op.getText()) {
            case "==":
                return new EQ(left, right, getLineNumber(ctx));
            case "!=":
                return new NEQ(left, right, getLineNumber(ctx));
            case ">":
                return new GT(left, right, getLineNumber(ctx));
            case "<":
                return new LT(left, right, getLineNumber(ctx));
            case ">=":
                return new GTEQ(left, right, getLineNumber(ctx));
            case "<=":
                return new LTEQ(left, right, getLineNumber(ctx));
            default:
                throw new AssertionError(getGrammarErrorMessage("EqualityExpressions"));
        }
    }

    @Override
    public Numerical visitMultDivExpression(QLParser.MultDivExpressionContext ctx) {
        Expression left = (Expression) ctx.expression(0).accept(this);
        Expression right = (Expression) ctx.expression(1).accept(this);

        switch (ctx.op.getText()) {
            case "*":
                return new Multiplication(left, right, getLineNumber(ctx));
            case "/":
                return new Division(left, right, getLineNumber(ctx));
            default:
                throw new AssertionError(getGrammarErrorMessage("MultDivExpressions"));
        }
    }

    @Override
    public Numerical visitAddSubExpression(QLParser.AddSubExpressionContext ctx) {
        Expression left = (Expression) ctx.expression(0).accept(this);
        Expression right = (Expression) ctx.expression(1).accept(this);

        switch (ctx.op.getText()) {
            case "+":
                return new Addition(left, right, getLineNumber(ctx));
            case "-":
                return new Subtraction(left, right, getLineNumber(ctx));
            default:
                throw new AssertionError(getGrammarErrorMessage("AddSubExpressions"));
        }
    }

    @Override
    public Parenthesis visitParenthesesExpression(QLParser.ParenthesesExpressionContext ctx) {
        Expression expression = (Expression) ctx.expression().accept(this);

        return new Parenthesis(expression, getLineNumber(ctx));
    }

    @Override
    public Unary visitUnaryExpression(QLParser.UnaryExpressionContext ctx) {
        Expression expression = (Expression) ctx.expression().accept(this);

        switch (ctx.op.getText()) {
            case "!":
                return new Negation(expression, getLineNumber(ctx));
            case "+":
                return new Positive(expression, getLineNumber(ctx));
            case "-":
                return new Negative(expression, getLineNumber(ctx));
            default:
                throw new AssertionError(getGrammarErrorMessage("UnaryExpressions"));
        }
    }

    @Override
    public Logic visitAndExpression(QLParser.AndExpressionContext ctx) {
        Expression left = (Expression) ctx.expression(0).accept(this);
        Expression right = (Expression) ctx.expression(1).accept(this);

        return new AND(left, right, getLineNumber(ctx));
    }

    @Override
    public Logic visitOrExpression(QLParser.OrExpressionContext ctx) {
        Expression left = (Expression) ctx.expression(0).accept(this);
        Expression right = (Expression) ctx.expression(1).accept(this);

        return new OR(left, right, getLineNumber(ctx));
    }

    @Override
    public MyInteger visitIntExpression(QLParser.IntExpressionContext ctx) {
        return new MyInteger(java.lang.Integer.parseInt(ctx.getText()), getLineNumber(ctx));
    }

    @Override
    public Money visitMoneyExpression(QLParser.MoneyExpressionContext ctx) {
        return new Money(BigDecimal.valueOf(Double.parseDouble(ctx.getText())), getLineNumber(ctx));
    }

    @Override
    public Identifier visitIdentifierExpression(QLParser.IdentifierExpressionContext ctx) {
        return new Identifier(ctx.IDENTIFIER().getText(), getLineNumber(ctx));
    }

    @Override
    public MyBoolean visitBoolExpression(QLParser.BoolExpressionContext ctx) {
        return new MyBoolean(Boolean.parseBoolean(ctx.getText()), getLineNumber(ctx));
    }

    @Override
    public MyString visitStringExpression(QLParser.StringExpressionContext ctx) {
        return new MyString(ctx.getText().substring(1, ctx.getText().length() - 1), getLineNumber(ctx));
    }

    private LineNumber getLineNumber(ParserRuleContext ctx) {
        return new LineNumber(ctx.getStart().getLine());
    }
}