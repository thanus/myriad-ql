package test.org.uva.taxfree.qls;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.uva.taxfree.ql.ast.QlAstBuilder;
import org.uva.taxfree.ql.gui.MessageList;
import org.uva.taxfree.ql.gui.QlsForm;
import org.uva.taxfree.ql.gui.QuestionForm;
import org.uva.taxfree.ql.model.environment.SymbolTable;
import org.uva.taxfree.ql.model.node.blocks.FormNode;
import org.uva.taxfree.ql.util.FileUtility;
import org.uva.taxfree.qls.QlsStyle;
import org.uva.taxfree.qls.QlsStyleBuilder;
import test.org.uva.taxfree.ql.SemanticsTester;

import java.io.File;
import java.io.IOException;

public class QLSFormTest extends SemanticsTester {
    QlsStyleBuilder mStyleBuilder;
    QlsStyle mQlsStyle;

    public static void main(String args[]) {
        QLSFormTest main = new QLSFormTest();
        try {
            main.executeMain();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeMain() throws IOException {
        // Generate AST
        QlAstBuilder builder = new QlAstBuilder(testFile("SimpleForm.txfrm"));
        MessageList messageList = new MessageList();
        FormNode ast = builder.generateAst(messageList);
        SymbolTable symbolTable = new SymbolTable();
        ast.fillSymbolTable(symbolTable);
        createQls("SimpleForm.qls", messageList);
        QuestionForm taxForm = new QlsForm(ast.toString(), symbolTable, mQlsStyle);
        ast.fillQuestionForm(taxForm);
        taxForm.show();
    }

    @Test
    public void testSemantics() throws Exception {
        assertSemantics("SimpleForm.txfrm", 0, "Qls should not contain errors");
    }

    private void createQls(String taxFile, MessageList messageList) {
        String styleFile = FileUtility.replaceExtension(taxFile, ".qls");
        File style = testFile(styleFile);
        Assert.assertTrue(style.exists(), "File not found: " + styleFile);
        mStyleBuilder = new QlsStyleBuilder(style);
        mQlsStyle = mStyleBuilder.generateStyle(messageList);
    }

    protected void assertSemantics(String fileName, int expectedErrorAmount, String description) throws IOException {
        super.assertSemantics(fileName, 0, "only works for valid tax forms");
        MessageList messages = new MessageList();
        createQls(fileName, messages);
        mQlsStyle.checkSemantics(getSymbolTable(), messages);
        System.out.println(messages);
        Assert.assertEquals(expectedErrorAmount, messages.messageAmount(), "invalid error amount detected: " + description);
    }

    @Override
    protected File testFile(String fileName) {
        return new File("src\\test\\org\\uva\\taxfree\\qls\\" + fileDirectory() + "\\" + fileName);
    }

    @Override
    protected String fileDirectory() {
        return "testFiles";
    }
}
