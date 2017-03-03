﻿namespace OffByOne.Ql.Visitors
{
    using MoreDotNet.Extensions.Collections;
    using MoreDotNet.Extensions.Common;

    using OffByOne.Ql.Ast;
    using OffByOne.Ql.Ast.Expressions;
    using OffByOne.Ql.Ast.Expressions.Binary;
    using OffByOne.Ql.Ast.Expressions.Binary.Base;
    using OffByOne.Ql.Ast.Expressions.Unary;
    using OffByOne.Ql.Ast.Expressions.Unary.Base;
    using OffByOne.Ql.Ast.Statements;
    using OffByOne.Ql.Ast.Statements.Branch;
    using OffByOne.Ql.Ast.ValueTypes;
    using OffByOne.Ql.Ast.ValueTypes.Base;
    using OffByOne.Ql.Checker;
    using OffByOne.Ql.Checker.Messages;
    using OffByOne.Ql.Checker.Models;
    using OffByOne.Ql.Visitors.Contracts;

    public class TypeVisitor
        : BaseTypeCheckerVisitor,
        IExpressionVisitor<ValueType, VisitorTypeEnv>,
        IStatementVisitor<ValueType, VisitorTypeEnv>
    {
        public TypeVisitor()
            : this(new CheckerReport())
        {
        }

        public TypeVisitor(CheckerReport report)
        {
            this.Report = report;
        }

        public CheckerReport Report { get; }

        public ValueType Visit(AddExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryMatematicalExpression(expression, context);
        }

        public ValueType Visit(SubtractExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryMatematicalExpression(expression, context);
        }

        public ValueType Visit(MultiplyExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryMatematicalExpression(expression, context);
        }

        public ValueType Visit(DivideExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryMatematicalExpression(expression, context);
        }

        public ValueType Visit(AndExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryBooleanLogicExpression(expression, context);
        }

        public ValueType Visit(OrExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryBooleanLogicExpression(expression, context);
        }

        public ValueType Visit(EqualExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryComparisonExpression(expression, context);
        }

        public ValueType Visit(NotEqualExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryComparisonExpression(expression, context);
        }

        public ValueType Visit(GreaterThanExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryComparisonExpression(expression, context);
        }

        public ValueType Visit(GreaterThanOrEqualExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryComparisonExpression(expression, context);
        }

        public ValueType Visit(LessThanExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryComparisonExpression(expression, context);
        }

        public ValueType Visit(LessThanOrEqualExpression expression, VisitorTypeEnv context)
        {
            return this.CheckBinaryComparisonExpression(expression, context);
        }

        public ValueType Visit(NotExpression expression, VisitorTypeEnv context)
        {
            return this.CheckUnaryBooleanLogicExpression(expression, context);
        }

        public ValueType Visit(NegativeExpression expression, VisitorTypeEnv context)
        {
            return this.CheckUnaryMatematicalExpression(expression, context);
        }

        public ValueType Visit(PositiveExpression expression, VisitorTypeEnv context)
        {
            return this.CheckUnaryMatematicalExpression(expression, context);
        }

        public ValueType Visit(VariableExpression expression, VisitorTypeEnv context)
        {
            var quetionType = context.GetTypeOf(expression.Identifier);
            if (quetionType == null)
            {
                this.Report.Add(new UndeclaredVariableMessage(expression));
                return TypeConstants.VoidType;
            }

            return quetionType;
        }

        public ValueType Visit(BracketExpression expression, VisitorTypeEnv context)
        {
            return expression.Expression.Accept(this, context);
        }

        public ValueType Visit(QuestionStatement expression, VisitorTypeEnv context)
        {
            context.AddSymbol(expression.Identifier, expression.Type);
            return TypeConstants.VoidType;
        }

        public ValueType Visit(IfStatement expression, VisitorTypeEnv context)
        {
            var result = this.CheckIfStatement(expression, context);
            expression.Statements.ForEach(x => x.Accept(this, context));
            expression.ElseStatements.ForEach(x => x.Accept(this, context));

            return result;
        }

        public ValueType Visit(FormStatement expression, VisitorTypeEnv context)
        {
            expression.Statements.ForEach(x => x.Accept(this, context));

            return TypeConstants.VoidType;
        }

        private ValueType CheckBinaryMatematicalExpression(BinaryExpression expression, VisitorTypeEnv context)
        {
            var leftExpressionType = expression.LeftExpression.Accept(this, context);
            var rightEpressionType = expression.RightExpression.Accept(this, context);

            if (leftExpressionType.IsNot<NumericalValueType>())
            {
                this.Report.Add(new InvaildTypeMessage(
                    expression.LeftExpression,
                    TypeConstants.NumericTypes,
                    leftExpressionType));

                return TypeConstants.VoidType;
            }

            if (rightEpressionType.IsNot<NumericalValueType>())
            {
                this.Report.Add(new InvaildTypeMessage(
                    expression.RightExpression,
                    TypeConstants.NumericTypes,
                    rightEpressionType));
            }

            if (leftExpressionType.Is<IntegerValueType>() && rightEpressionType.Is<IntegerValueType>())
            {
                return TypeConstants.IntegerType;
            }

            if (leftExpressionType.Is<DecimalValueType>() || rightEpressionType.Is<DecimalValueType>())
            {
                return TypeConstants.DecimalType;
            }

            if (leftExpressionType.Is<MoneyValueType>() || rightEpressionType.Is<MoneyValueType>())
            {
                return TypeConstants.MoneyType;
            }

            return TypeConstants.VoidType;
        }

        private ValueType CheckUnaryMatematicalExpression(UnaryExpression expression, VisitorTypeEnv context)
        {
            var subExpressionType = expression.Expression.Accept(this, context);
            if (subExpressionType.IsNot<NumericalValueType>())
            {
                this.Report.Add(new InvaildTypeMessage(
                    expression,
                    TypeConstants.NumericTypes,
                    subExpressionType));
            }

            return subExpressionType;
        }

        private ValueType CheckBinaryComparisonExpression(BinaryExpression expression, VisitorTypeEnv context)
        {
            var leftExpressionType = expression.LeftExpression.Accept(this, context);
            var rightEpressionType = expression.RightExpression.Accept(this, context);

            if (leftExpressionType != rightEpressionType)
            {
                this.Report.Add(new InvaildTypeMessage(expression, leftExpressionType, rightEpressionType, LogLevel.Error));
            }

            return TypeConstants.BooleanType;
        }

        private ValueType CheckBinaryBooleanLogicExpression(BinaryExpression expression, VisitorTypeEnv context)
        {
            var leftExpressionType = expression.LeftExpression.Accept(this, context);
            var rightEpressionType = expression.RightExpression.Accept(this, context);

            if (leftExpressionType.IsNot<BooleanValueType>())
            {
                this.Report.Add(new InvaildTypeMessage(
                    expression.LeftExpression,
                    TypeConstants.BooleanType,
                    leftExpressionType));

                return leftExpressionType;
            }

            if (rightEpressionType.IsNot<BooleanValueType>())
            {
                this.Report.Add(new InvaildTypeMessage(
                    expression.RightExpression,
                    TypeConstants.BooleanType,
                    rightEpressionType));

                return rightEpressionType;
            }

            return TypeConstants.BooleanType;
        }

        private ValueType CheckUnaryBooleanLogicExpression(UnaryExpression expression, VisitorTypeEnv context)
        {
            var subExpressionType = expression.Expression.Accept(this, context);

            if (subExpressionType.IsNot<BooleanValueType>())
            {
                this.Report.Add(new InvaildTypeMessage(
                    expression,
                    TypeConstants.BooleanType,
                    subExpressionType));

                return subExpressionType;
            }

            return TypeConstants.BooleanType;
        }

        private ValueType CheckIfStatement(IfStatement statement, VisitorTypeEnv context)
        {
            var conditionType = statement.Condition.Accept(this, context);
            if (conditionType.IsNot<BooleanValueType>())
            {
                this.Report.Add(new InvaildTypeMessage(statement, TypeConstants.BooleanType, conditionType));
            }

            return TypeConstants.VoidType;
        }
    }
}