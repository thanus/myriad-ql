package org.uva.taxfree.ql.gui.widgets;

import org.uva.taxfree.ql.gui.FormListener;
import org.uva.taxfree.ql.model.environment.SymbolTable;
import org.uva.taxfree.ql.model.values.Value;
import org.uva.taxfree.qls.QlsStyle;

import javax.swing.*;
import java.awt.*;

public abstract class Widget implements Resolvable {
    private final GuiComponent mGuiComponent;
    private Resolvable mResolver;

    public Widget(String label, String id) {
        mGuiComponent = new GuiComponent(label, id);
        mResolver = this;
    }

    public void registerToPanel(JPanel widgetPanel) {
        fillPanel(mGuiComponent);
        mGuiComponent.registerToPanel(widgetPanel);
    }

    protected abstract void fillPanel(GuiComponent widgetPanel);

    public Value resolveValue() {
        return mResolver.resolve();
    }

    public void setListener(FormListener listener) {
        mResolver.callOnUpdate(listener);
    }

    public void updateVisibility(SymbolTable symbolTable) {
        mGuiComponent.setVisible(symbolTable.isVisible(mGuiComponent.getId()));
    }

    public abstract void updateValues(SymbolTable symbolTable);

    protected void writeToTable(SymbolTable symbolTable) {
        symbolTable.updateValue(mGuiComponent.getId(), resolveValue());
    }

    protected String readFromTable(SymbolTable symbolTable) {
        return symbolTable.resolveValue(getId()).toString();
    }

    public void updateStyle(QlsStyle qlsStyle) {
        applyStyle(this, qlsStyle);
    }

    protected void applyStyle(Widget component, QlsStyle qlsStyle) {
        // Intentionally left blank
    }

    public String getId() {
        return mGuiComponent.getId();
    }

    public void setResolver(Resolvable externalResolver) {
        mResolver = externalResolver;
    }

    public void setBackgroundColor(Color backgroundColor) {
        mGuiComponent.setBackgroundColor(backgroundColor);
    }

    public void setFontName(String fontName) {
        mGuiComponent.setFontName(fontName);
    }

    public void setFontSize(int fontSize) {
        mGuiComponent.setFontSize(fontSize);
    }

    public void setForegroundColor(Color foregroundColor) {
        mGuiComponent.setForegroundColor(foregroundColor);
    }

    public void setValueCompoent(JComponent component) {
        mGuiComponent.setValueComponent(component);
    }
}
