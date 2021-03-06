package org.ql.typechecker;

import org.ql.ast.form.Form;
import org.ql.typechecker.issues.*;
import org.ql.typechecker.visitor.CircularDependencyVisitor;
import org.ql.typechecker.visitor.QuestionsVisitor;
import org.ql.typechecker.visitor.TypeMismatchVisitor;

public class QLTypeChecker {

    private final QuestionsVisitor questionsVisitor;
    private final TypeMismatchVisitor typeMismatchVisitor;
    private final CircularDependencyVisitor circularDependenciesVisitor;

    private final IssuesStorage issuesStorage;

    public QLTypeChecker() {
        this.issuesStorage = new IssuesStorage();

        questionsVisitor = new QuestionsVisitor(issuesStorage);
        typeMismatchVisitor = new TypeMismatchVisitor(issuesStorage);
        circularDependenciesVisitor = new CircularDependencyVisitor(issuesStorage);
    }

    public IssuesStorage checkForm(Form form, SymbolTable symbolTable) {
        questionsVisitor.visitForm(form, symbolTable);
        typeMismatchVisitor.visitForm(form, symbolTable);
        circularDependenciesVisitor.visitForm(form, null);

        return issuesStorage;
    }
}
