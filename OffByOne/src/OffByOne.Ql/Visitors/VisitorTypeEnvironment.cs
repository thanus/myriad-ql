﻿namespace OffByOne.Ql.Visitors
{
    using System.Collections.Generic;

    using MoreDotNet.Extensions.Collections;

    using OffByOne.Ql.Ast.ValueTypes.Base;
    using OffByOne.Ql.Visitors.Contracts;

    public class VisitorContext : IVisitorContext
    {
        private readonly IDictionary<string, ValueType> symbolMappings;

        public VisitorContext()
        {
            this.symbolMappings = new Dictionary<string, ValueType>();
        }

        public void AddSymbol(string name, ValueType type)
        {
            // TODO: What about redeclaration?
            if (!this.symbolMappings.ContainsKey(name))
            {
                this.symbolMappings[name] = type;
            }
        }

        public ValueType GetTypeOf(string name)
        {
            return this.symbolMappings.GetOrDefault(name);
        }
    }
}