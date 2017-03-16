﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Questionnaires.Types
{
    public interface IType
    {
        // Unary operations
        IType Positive();
        IType Negative();
        BooleanType Bang();

        // Binary operations
        IType Add(IType value);
        IType Subtract(IType value);
        IType Multiply(IType value);
        IType Divide(IType value);
        BooleanType And(IType value);
        BooleanType Or(IType value);
        BooleanType LessThan(IType value);
        BooleanType LessThanOrEqual(IType value);
        BooleanType GreaterThan(IType value);
        BooleanType GreaterThanOrEqual(IType value);
        BooleanType EqualTo(IType value);
        BooleanType InequalTo(IType value);

        Questionnaires.Renderer.Widgets.QuestionWidget GetWidget();
    }
}