﻿namespace OffByOne.Ql.Checker.Analyzers.Environment
{
    using System;
    using System.Collections.Generic;

    using MoreDotNet.Wrappers;

    using OffByOne.Ql.Checker.Analyzers.Environment.Contracts;
    using OffByOne.Ql.Values;

    public class QuestionEnvironment : IQuestionEnvironment
    {
        private readonly ISet<string> questionNames;
        private readonly ISet<string> questionLables;

        public QuestionEnvironment()
        {
            this.questionLables = new HashSet<string>();
            this.questionNames = new HashSet<string>();
        }

        public void AddQuestionIdentifier(string name)
        {
            if (name.IsNullOrWhiteSpace())
            {
                throw new ArgumentException(
                    "Question identifier must not be empty or null.",
                    nameof(name));
            }

            this.questionNames.Add(name);
        }

        public void AddQuestionLabel(StringValue label)
        {
            if (label.Value.IsNullOrWhiteSpace())
            {
                throw new ArgumentException(
                    "Question label must not be empty or null.",
                    nameof(label));
            }

            this.questionLables.Add(label.Value);
        }

        public bool IsIdentifierDuplicate(string name)
        {
            if (name.IsNullOrWhiteSpace())
            {
                throw new ArgumentException(
                    "Question identifier must not be empty or null.",
                    nameof(name));
            }

            return this.questionNames.Contains(name);
        }

        public bool IsLabelDuplicate(StringValue label)
        {
            if (label.Value.IsNullOrWhiteSpace())
            {
                throw new ArgumentException(
                    "Question label must not be empty or null.",
                    nameof(label));
            }

            return this.questionLables.Contains(label.Value);
        }
    }
}
