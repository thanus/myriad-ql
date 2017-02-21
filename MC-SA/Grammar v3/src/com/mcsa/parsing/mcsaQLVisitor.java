package com.mcsa.parsing;

import com.mcsa.QL.*;
import com.mcsa.antlr.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ErrorNode;

import java.util.ArrayList;


/**
 * Created by matt on 20/02/2017.
 */
public class mcsaQLVisitor extends AbstractParseTreeVisitor implements QLVisitor {

    @Override
    public Node visitFormDeclaration(QLParser.FormDeclarationContext ctx) {

        //get the name of the form
        String formName = ctx.ID().getText();

        //instantiate structures to contain the questions and if statement roots
        ArrayList<Question> questions = new ArrayList<>();
        ArrayList<IfStatement> ifStatements = new ArrayList<>();

        //new form to pass back
        Form form = new Form(formName);

        //iterate through each "statement" in the form
        for (QLParser.StatementContext statementContext : ctx.statement()) {

            //if we hit a question
            if (statementContext.question() != null) {
                //visit the question to get a Question object back
                Question result = (Question) visit(statementContext);
                questions.add(result);
            }
            //if we hit an if statement
            else if (statementContext.ifStatement() != null) {
                //visit the if statement to get an IfStatement object back
                IfStatement result = (IfStatement) visit(statementContext);
                ifStatements.add(result);
            }
        }

        //add the structures to the form
        form.replaceQuestions(questions);
        form.replaceIfStatements(ifStatements);

        return form;
    }

    @Override
    public Node visitStatement(QLParser.StatementContext ctx) {

        //visit the correct element of the StatementContext
        if (ctx.question() != null) {
            return (Question) visit(ctx.question());
        }
        else if (ctx.ifStatement() != null){
            return (IfStatement) visit(ctx.ifStatement());
        }

        //TODO: handle this return properly, null is not good
        return null;
    }

    @Override
    public Node visitIfStatement(QLParser.IfStatementContext ctx) {

        //visit the if case
        visit(ctx.ifCase());

        //TODO make visitIfStatement return an IfStatement object
        return null;
    }

    @Override
    public Node visitQuestion(QLParser.QuestionContext ctx) {

        //grab the question metadata and return the object
        String name = ctx.questionParameters().ID().getText();
        String type = ctx.questionParameters().type().getText();
        String text = ctx.STRING().getText();
        return new Question(name, text, new Type(type));
    }

    @Override
    public Node visitIfCase(QLParser.IfCaseContext ctx) {

        //new ifCase to contain the result
        IfCase ifCaseCheck = new IfCase();

        //if there is a token, we grab it and know to check for the elements either side
        if (ctx.TOKEN() != null) {
            ifCaseCheck.addToken(ctx.TOKEN().getText());

            //TODO visit the elements either side of the token to get IfCaseArgs
            if(ctx.ifCase().size() == 2)
            {
                //then we know there are two terms to deal with
            }
        }

        //TODO make visitIfCase return an IfCase object
        return null;
    }

    @Override
    public Object visitIfCaseArgs(QLParser.IfCaseArgsContext ctx) {

        //get the ID or number depending on what exists
        if (ctx.ID() != null)
            return ctx.ID().getText();
        else
            return ctx.NUMBER().getText();

    }

    @Override
    public Object visitQuestionParameters(QLParser.QuestionParametersContext ctx) {
        return null;
    }

    @Override
    public Object visitType(QLParser.TypeContext ctx) {
        return null;
    }

    @Override
    public Object visitMathaction(QLParser.MathactionContext ctx) {
        return null;
    }

    @Override
    public Object visitErrorNode(ErrorNode node) {
        return null;
    }
}
