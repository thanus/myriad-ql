﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DSL.SemanticAnalysis.SemenaticAnalysisEvents;

namespace DSL.AST
{

    public class QLForm : INode
    {
        public QLForm(string identifier, List<INode> statements)
        {
            this.Identifier = identifier;
            Statements = statements;
        }   

        public List<INode> Statements
        {
            get;
        }

        public String Identifier
        {
            get;
        }

        public QLType? CheckTypes(List<QLType> parameters, QLContext context, List<ISemenaticAnalysisEvent> events)
        {
            // No type validation to do here.
            return null;
        }
    }
}
