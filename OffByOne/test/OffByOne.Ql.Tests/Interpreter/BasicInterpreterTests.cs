﻿namespace OffByOne.Ql.Tests.Interpreter
{
    using System.Collections.Generic;
    using System.Globalization;
    using System.Linq;
    using System.Threading.Tasks;
    using System.Windows;
    using System.Windows.Controls;

    using OffByOne.Ql.Tests.Common;
    using OffByOne.Ql.Tests.Interpreter.Base;
    using OffByOne.Ql.Values;

    using Xunit;

    public class BasicInterpreterTests : InterpreterTest
    {
        [Fact]
        public async Task Interpretation_ShouldReturnCorrectNumberOfControls()
        {
            await ThreadHelper.StartSTATask(() =>
            {
                var input = @"
                form taxOfficeExample { 
                    ""Did you sell a house in 2010?""
                        hasSoldHouse: boolean
                    ""Did you buy a house in 2010?""
                        hasBoughtHouse: boolean
                    ""Did you enter a loan?""
                        hasMaintLoan: boolean
    
                    if (hasSoldHouse) {
                        ""What was the selling price?""
                            sellingPrice: money
                        ""Private debts for the sold house:""
                            privateDebt: money
                        ""Value residue:""
                            valueResidue: money = 
                                (sellingPrice - privateDebt)
                        }
                    }
                ";
                var form = this.GetInterpretationFromInput(input);
                Assert.True(form.Controls.Count == 12);
            });
        }

        [Fact]
        public async Task Interpretation_ShouldHideAndShowControlsOnUpdate()
        {
            await ThreadHelper.StartSTATask(() =>
            {
                var input = @"
                form taxOfficeExample { 
                    ""Did you sell a house in 2010?""
                        hasSoldHouse: boolean
                    ""Did you buy a house in 2010?""
                        hasBoughtHouse: boolean
                    ""Did you enter a loan?""
                        hasMaintLoan: boolean
    
                    if (hasSoldHouse) {
                        ""What was the selling price?""
                            sellingPrice: money
                        ""Private debts for the sold house:""
                            privateDebt: money
                        ""Value residue:""
                            valueResidue: money = 
                                (sellingPrice - privateDebt)
                        }
                    }
                ";
                var form = this.GetInterpretationFromInput(input);
                Assert.False(form.Controls["sellingPrice"].Visibility == Visibility.Visible);
                Assert.False(form.Controls["privateDebt"].Visibility == Visibility.Visible);
                Assert.False(form.Controls["valueResidue"].Visibility == Visibility.Visible);

                form.Environment.UpdateValues("hasSoldHouse", new BooleanValue(true));
                Assert.True(form.Controls["sellingPrice"].Visibility == Visibility.Visible);
                Assert.True(form.Controls["privateDebt"].Visibility == Visibility.Visible);
                Assert.True(form.Controls["valueResidue"].Visibility == Visibility.Visible);

                form.Environment.UpdateValues("hasSoldHouse", new BooleanValue(false));
                Assert.False(form.Controls["sellingPrice"].Visibility == Visibility.Visible);
                Assert.False(form.Controls["privateDebt"].Visibility == Visibility.Visible);
                Assert.False(form.Controls["valueResidue"].Visibility == Visibility.Visible);
            });
        }

        [Fact]
        public async Task Interpretation_ShouldUpdateValuesInGui()
        {
            await ThreadHelper.StartSTATask(() =>
            {
                var input = @"
                form taxOfficeExample { 
                    ""Did you sell a house in 2010?""
                        hasSoldHouse: boolean
                    ""Did you buy a house in 2010?""
                        hasBoughtHouse: boolean
                    ""Did you enter a loan?""
                        hasMaintLoan: boolean
    
                    if (hasSoldHouse) {
                        ""What was the selling price?""
                            sellingPrice: money
                        ""Private debts for the sold house:""
                            privateDebt: money
                        ""Value residue:""
                            valueResidue: money = 
                                (sellingPrice - privateDebt)
                        }
                    }
                ";
                var selling = new decimal(110000.10);
                var debt = new decimal(100000.12);
                var updatedDebt = 120000;

                var expectedResidue = selling - debt;
                var expectedUpdatedResidue = selling - updatedDebt;

                var form = this.GetInterpretationFromInput(input);
                var residueControl = (TextBox)form.Controls["valueResidue"];

                form.Environment.UpdateValues("hasSoldHouse", new BooleanValue(true));
                form.Environment.UpdateValues("sellingPrice", new MoneyValue(selling));
                form.Environment.UpdateValues("privateDebt", new MoneyValue(debt));
                var controlValue = decimal.Parse(residueControl.Text, CultureInfo.InvariantCulture);
                Assert.Equal(expectedResidue, controlValue);

                form.Environment.UpdateValues("privateDebt", new MoneyValue(updatedDebt));
                controlValue = decimal.Parse(residueControl.Text, CultureInfo.InvariantCulture);
                Assert.Equal(expectedUpdatedResidue, controlValue);
            });
        }

        [Fact]
        public async Task Interpretation_ShouldMakeComputedControlsReadOnly()
        {
            await ThreadHelper.StartSTATask(() =>
            {
                var input = @"
                form taxOfficeExample { 
                    ""Did you sell a house in 2010?""
                        hasSoldHouse: boolean
                    ""Did you buy a house in 2010?""
                        hasBoughtHouse: boolean
                    ""Did you enter a loan?""
                        hasMaintLoan: boolean
    
                    if (hasSoldHouse) {
                        ""What was the selling price?""
                            sellingPrice: money
                        ""Private debts for the sold house:""
                            privateDebt: money
                        ""Value residue:""
                            valueResidue: money = 
                                (sellingPrice - privateDebt)
                        }
                    }
                ";
                var numberOfLabels = 6;
                var numberOfEnabledControls = 5;

                var form = this.GetInterpretationFromInput(input);
                Assert.False(form.Controls["valueResidue"].IsEnabled);
                var enabledControls = form.Controls.Where(x => x.Value.IsEnabled).ToList();
                Assert.Equal(enabledControls.Count, numberOfEnabledControls + numberOfLabels);
                Assert.Empty(enabledControls.Where(x => x.Key.Equals("valueResidue")).ToList());
            });
        }

        [Fact]
        public async Task Interpretation_ShouldPreserveQuestionOrder()
        {
            await ThreadHelper.StartSTATask(() =>
            {
                var input = @"
                form taxOfficeExample { 
                    ""Did you sell a house in 2010?""
                        hasSoldHouse: boolean
                    ""Did you buy a house in 2010?""
                        hasBoughtHouse: boolean
                    ""Did you enter a loan?""
                        hasMaintLoan: boolean
    
                    if (hasSoldHouse) {
                        ""What was the selling price?""
                            sellingPrice: money
                        ""Private debts for the sold house:""
                            privateDebt: money
                        ""Value residue:""
                            valueResidue: money = 
                                (sellingPrice - privateDebt)
                        }
                    }
                ";

                var questionIdentifiers = new List<string>
                {
                    "hasSoldHouse", "hasBoughtHouse", "hasMaintLoan",
                    "sellingPrice", "privateDebt", "valueResidue"
                };

                var form = this.GetInterpretationFromInput(input);
                var keys = form.Controls.Keys.ToList();
                var lastIndex = -1;
                foreach (var identifier in questionIdentifiers)
                {
                    var currentIndex = keys.IndexOf(identifier);
                    Assert.True(lastIndex < currentIndex);
                    lastIndex = currentIndex;
                }
            });
        }
    }
}
