package ql.ast.expressions.monop;

import ql.ast.Expr;
import ql.ast.expressions.ArithmeticOp;
import ql.ast.expressions.MonOp;
import ql.ast.visistor.ASTVisitor;

/**
 * Created by Erik on 7-2-2017.
 */
public class Pos extends MonOp implements ArithmeticOp {

    public Pos(Expr expr, int rowNumber) {
        super(expr, rowNumber);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
