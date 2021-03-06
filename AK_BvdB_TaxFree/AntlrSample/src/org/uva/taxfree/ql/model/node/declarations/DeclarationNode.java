package org.uva.taxfree.ql.model.node.declarations;

import org.uva.taxfree.ql.gui.MessageList;
import org.uva.taxfree.ql.gui.QuestionForm;
import org.uva.taxfree.ql.model.SourceInfo;
import org.uva.taxfree.ql.model.environment.SymbolTable;
import org.uva.taxfree.ql.model.node.Node;
import org.uva.taxfree.ql.model.types.Type;
import org.uva.taxfree.ql.model.values.Value;

import java.util.List;
import java.util.Set;

public class DeclarationNode extends Node {
    private final String mId;
    private final String mLabel;
    private final Type mType;

    public DeclarationNode(String label, String id, Type type, SourceInfo sourceInfo) {
        super(sourceInfo);
        mLabel = label;
        mId = id;
        mType = type;
    }

    public Type getType() {
        return mType;
    }

    @Override
    public void fillSymbolTable(SymbolTable symbolTable) {
        symbolTable.addDeclaration(this);
    }

    @Override
    public void fillQuestionForm(QuestionForm frame) {
        mType.generateWidget(mLabel, mId, frame);
    }

    public String getId() {
        return mId;
    }

    public String getLabel() {
        return mLabel;
    }

    @Override
    public void checkSemantics(SymbolTable symbolTable, MessageList semanticsMessages) {
        if (!symbolTable.contains(mId)) {
            semanticsMessages.addError("Declaration not present in symbolTable:" + mId);
        }
    }

    @Override
    public void generateVisibleIds(List<String> visibleIds) {
        visibleIds.add(mId);
    }

    public Value generateValue() {
        return mType.defaultValue();
    }

    @Override
    public void collectUsedVariables(Set<String> dependencies) {
        dependencies.add(mId);
    }
}
