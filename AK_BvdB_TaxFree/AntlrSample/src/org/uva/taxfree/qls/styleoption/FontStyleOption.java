package org.uva.taxfree.qls.styleoption;

import org.uva.taxfree.ql.gui.widgets.Widget;
import org.uva.taxfree.ql.model.SourceInfo;

public class FontStyleOption extends StyleOption {
    private final String mFontName;

    public FontStyleOption(String fontName, SourceInfo sourceInfo) {
        super(sourceInfo);
        mFontName = fontName;
    }

    public void applyStyle(Widget widget) {
        widget.setFontName(mFontName);
    }
}
