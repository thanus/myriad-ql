package ql.semantic;

import ql.ast.*;
import ql.ast.atom.BoolAtom;
import ql.ast.atom.IntegerAtom;
import ql.ast.atom.StringAtom;
import ql.ast.expression.*;
import ql.message.Error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** CheckCyclicDependencies checks for
 * <li> cyclic dependencies between the questions
 */
public class CheckCyclicDependencies implements FormVisitor, ql.ast.ExpressionVisitor<Void> {

	private final Environment environment;
    private Question current;

    private final Map<String, List<String>> dependencies ;

    CheckCyclicDependencies(Environment environment) {
        this.environment = environment;
        dependencies = new HashMap<>();
    }

    @Override
        public void visit(Form form) {
        form.getBlock().accept(this);
    }

    @Override
    public void visit(Block block) {
        for (BlockItem blockItem : block) {
            blockItem.accept(this);
        }
    }

    @Override
    public void visit(BlockItem blockItem) {
        blockItem.accept(this);
    }

    @Override
    public void visit(Question question) {
    }

    @Override
    public void visit(ComputedQuestion question) {
        current = question;
        question.getComputedQuestion().accept(this);
    }

    @Override
    public void visit(Statement statement) {
    }

    @Override
    public void visit(IfElseStatement statement) {
    }

    @Override
    public Void visit(AddExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(AndExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(DivExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(EqExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(GEqExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(GExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(IdExpr id) {

        addReference(current.getName(), id.getName());
        check();

        return null;
    }

    @Override
    public Void visit(LEqExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(LExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(MinusExpr expr) {
        expr.getLhs().accept(this);

        return null;
    }


    @Override
    public Void visit(MulExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(NEqExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(NotExpr expr) {
        expr.getLhs().accept(this);

        return null;
    }

    @Override
    public Void visit(OrExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(PlusExpr expr) {
        expr.getLhs().accept(this);

        return null;
    }

    @Override
    public Void visit(SubExpr expr) {
        expr.getLhs().accept(this);
        expr.getRhs().accept(this);

        return null;
    }

    @Override
    public Void visit(BoolAtom expr) {
        return null;
    }

    @Override
    public Void visit(IntegerAtom expr) {
        return null;
    }

    @Override
    public Void visit(StringAtom expr) {
        return null;
    }

    private void check() {
        List<String> references = getReferences(current.getName());

        for (String reference : references) {
            List<String> cycleReferences = getReferences(reference);
            if (!cycleReferences.isEmpty() && cycleReferences.contains(current.getName())) {
            	environment.addMessage(new Error("[ql] There is a cyclic dependency in "
            			+ "the computed questions " + current.getName() + " and " + reference,
            			current.getLine()));
            }
        }
    }

    private void addReference(String name, String reference){
        List<String> references;

        if (dependencies.containsKey(name)) {
            references = dependencies.get(name);
        } else {
            references = new ArrayList<>();
        }

        references.add(reference);
        dependencies.put(name, references);
    }

    private List<String> getReferences(String name){
        if (dependencies.containsKey(name)) {
            return dependencies.get(name);
        } else {
            return new ArrayList<>();
        }
    }

}
