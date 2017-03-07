from QL import AST

# TODO: Double dispatch in AST --> make it work here.
# TODO: Remove context, remove clear errors, clear env <-- dangerous.
# TODO: Add a pass to collect variables --> out of order questions.


class TypeChecker(object):
    def __init__(self, ast, env, error_handler):
        """
        :type ast: AST.FormNode
        :type env: Environment.Environment
        :type error_handler: ErrorHandler.ErrorHandler
        """
        self.ast = ast
        self.env = env
        self.labels = []

        self.handler = error_handler

    def start_traversal(self):
        self.ast.accept(self)

    @staticmethod
    def highest_number_type(left, right):
        """ :type left: AST.TypeNode """
        if left.is_decimal() or right.is_decimal():
            return AST.DecimalTypeNode()
        elif left.is_money() or right.is_money():
            return AST.MoneyTypeNode()
        elif left.is_integer() and right.is_integer():
            return AST.IntTypeNode()
        else:
            assert False, "Not both operands are of number type!"

    def get_monop_type(self, node):
        return node.expression.accept(self)

    def get_binop_types(self, node):
        return node.left.accept(self), node.right.accept(self)

    def question_node(self, question_node):
        """ :type question_node: AST.QuestionNode """
        pass

    def comp_question_node(self, comp_question_node):
        """ :type comp_question_node: AST.CompQuestionNode """
        comp_question_node.expression.accept(self)

    def if_node(self, if_node):
        """ :type if_node: AST.IfNode """
        expr_type = if_node.condition.accept(self)

        if not expr_type.is_boolean():
            self.handler.add_if_cond_error(if_node)
        if_node.if_block.accept(self)

    def if_else_node(self, if_else_node):
        """ :type if_else_node: AST.IfElseNode """
        expr_type = if_else_node.condition.accept(self)

        if not expr_type.is_boolean():
            self.handler.add_if_cond_error(if_else_node)
        if_else_node.if_block.accept(self)
        if_else_node.else_block.accept(self)

    def mon_op_node(self, mon_op_node):
        """ Default behavior for monOps. """
        var_type = self.get_monop_type(mon_op_node)
        if not var_type.is_numeric():
            self.handler.add_monop_error(mon_op_node, var_type)
            return AST.DecimalTypeNode()
        return var_type

    def neg_node(self, neg_node):
        """ '!' has different behavior than the other monOps."""
        var_type = self.get_monop_type(neg_node)
        if not var_type.is_boolean():
            self.handler.add_monop_error(neg_node, var_type)
        return AST.BoolTypeNode()

    def min_node(self, min_node):
        return self.mon_op_node(min_node)

    def plus_node(self, plus_node):
        return self.mon_op_node(plus_node)

    def arithmetic_expr_node(self, expr_node):
        """ Default behavior for arithmetic binOps. """
        left, right = self.get_binop_types(expr_node)
        if not (left.is_numeric() and right.is_numeric()):
            self.handler.add_binop_error(expr_node, left, right)
            return AST.DecimalTypeNode()
        return self.highest_number_type(left, right)

    def add_node(self, add_node):
        """ '+' has different behavior than the other arithmetic binOps. """
        left, right = self.get_binop_types(add_node)
        valid_types = left.is_alphanumeric() and right.is_alphanumeric()
        different_types = left.is_numeric() ^ right.is_numeric()

        if not valid_types or different_types:
            self.handler.add_binop_error(add_node, left, right)
            return AST.DecimalTypeNode()
        if left.is_numeric() and right.is_numeric():
            return self.highest_number_type(left, right)
        return left

    def sub_node(self, sub_node):
        return self.arithmetic_expr_node(sub_node)

    def mul_node(self, mul_node):
        return self.arithmetic_expr_node(mul_node)

    def div_node(self, div_node):
        return self.arithmetic_expr_node(div_node)

    def comparison_expr_node(self, expr_node):
        """ Default behavior for comparison binOps. """
        left, right = self.get_binop_types(expr_node)
        valid_types = not (left.is_boolean() or right.is_boolean())
        same_types = left == right or left.is_numeric() and right.is_numeric()

        if not valid_types or not same_types:
            self.handler.add_binop_error(expr_node, left, right)
        return AST.BoolTypeNode()

    def lt_node(self, lt_node):
        return self.comparison_expr_node(lt_node)

    def lte_node(self, lte_node):
        return self.comparison_expr_node(lte_node)

    def gt_node(self, gt_node):
        return self.comparison_expr_node(gt_node)

    def gte_node(self, gte_node):
        return self.comparison_expr_node(gte_node)

    def eq_node(self, eq_node):
        return self.comparison_expr_node(eq_node)

    def neq_node(self, neq_node):
        return self.comparison_expr_node(neq_node)

    def logical_expr_node(self, expr_node):
        left, right = self.get_binop_types(expr_node)
        if not (left.is_boolean() and right.is_boolean()):
            self.handler.add_binop_error(expr_node, left, right)
        return AST.BoolTypeNode()

    def and_node(self, and_node):
        return self.logical_expr_node(and_node)

    def or_node(self, or_node):
        return self.logical_expr_node(or_node)

    @staticmethod
    def string_node(_):
        return AST.StringTypeNode()

    @staticmethod
    def int_node(_):
        return AST.IntTypeNode()

    @staticmethod
    def bool_node(_):
        return AST.BoolTypeNode()

    def var_node(self, var_node):
        return self.env.get_var_type(var_node)

    @staticmethod
    def decimal_node(_):
        return AST.DecimalTypeNode()

    @staticmethod
    def date_node(_):
        return AST.DateTypeNode()
