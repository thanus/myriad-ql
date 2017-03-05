﻿namespace OffByOne.LanguageCore.Ast.Literals
{
    using System;
    using System.Globalization;

    using OffByOne.LanguageCore.Ast.Literals.Base;
    using OffByOne.LanguageCore.Visitors.Contracts;

    public class DateLiteral : Literal
    {
        public const string Format = "dd-MM-yyyy";

        public DateLiteral(DateTime value)
        {
            this.Value = value;
        }

        public DateLiteral(string dateString)
            : this(DateTime.ParseExact(
                dateString.Trim('\''),
                DateLiteral.Format,
                CultureInfo.InvariantCulture))
        {
        }

        public DateTime Value { get; private set; }

        public override TResult Accept<TResult, TContext>(
            ILiteralVisitor<TResult, TContext> visitor,
            TContext context)
        {
            return visitor.Visit(this, context);
        }
    }
}