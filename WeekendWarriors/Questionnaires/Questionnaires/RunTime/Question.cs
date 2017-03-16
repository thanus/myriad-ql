﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Questionnaires.RunTime
{
    public class Question
    {
        public Questionnaires.Renderer.Widgets.QuestionWidget Widget { get; set; }
        private QL.AST.Question QuestionASTNode; // TODO: come up with a better name

        public Question(QL.AST.Question questionASTNode)
        {
            QuestionASTNode = questionASTNode;
            SetWidget(questionASTNode.Type.GetWidget());
            Type = QuestionASTNode.Type;
        }

        public string Identifier
        {
            get { return QuestionASTNode.Identifier; }
        }

        public string Body
        {
            get { return QuestionASTNode.Body; }
        }

        public void SetWidget(QLS.AST.Widgets.Widget widget)
        {
            SetWidget(widget.CreateWidget((dynamic)QuestionASTNode.Type));
        }

        public Types.IType Type // TODO: we need to get rid of this but for now we need it for the typeof we do in the QLS processor. On second thought, maybe not. But it shoul be referred to as value rather than type.
        {
            get;
            set;
        }

        public event EventHandler ValueChanged;
        protected virtual void OnValueChanged(EventArgs e)
        {
            if (ValueChanged != null)
                ValueChanged(this, e);
        }

        private void SetWidget(Renderer.Widgets.QuestionWidget widget)
        {
            Widget = widget;
            Widget.SetLabel(QuestionASTNode.Body);
            Widget.InputChanged += (sender, value) => SetValue(value); 
        }

        public void SetValue(Types.IType value)
        {
            bool valueChanged = Type.InequalTo(value).GetValue();
            Type = value;
            if (valueChanged)
            {
                Widget.SetQuestionValue(Type);
                OnValueChanged(new EventArgs());
            }
        }

        public void SetVisibility(bool visible)
        {
            Widget.SetVisibility(visible);
        }
    }
}