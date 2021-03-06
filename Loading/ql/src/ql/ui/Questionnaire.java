package ql.ui;

import ql.ast.Form;
import ql.message.Message;
import ql.ui.message.MessageDialog;
import ql.value.Value;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Questionnaire extends Application implements Notifier {

	private static Form form;
	private static Environment environment;
	private static GridPane grid;
	private static List<Message> messages;
	
    public void main(Form form, Environment env, List<Message> messages) {
    	
    	init(form, env, messages);
        launch();
    }
    
    protected void main(Form form, Environment env, List<Message> messages, Stage primaryStage) {
    	
    	init(form, env, messages);
    	run(primaryStage);
    }
    
    private void init(Form f, Environment env, List<Message> msgs) {   	
    	
    	form = f;
    	environment = env;
    	environment.addDefaults();
    	messages = msgs;   	
    }
    
    @Override
    public void start(Stage primaryStage) {

        CollectFields collectFields = new CollectFields(this, environment);
        collectFields.visit(form);
        
    	showMessages(messages);
        if (hasFatalMessage(messages)) {
            return;
        }
    	
    	run(primaryStage);
    }
   
    
    private void run(Stage primaryStage) {

        primaryStage.setTitle(form.getId());

        initGrid();
        Scene scene = new Scene(grid, 700, 350);
        primaryStage.setScene(scene);

        renderQuestionnaire();
        
        primaryStage.show();
    }

    protected void showMessages(List<Message> messages) {
        if (messages.isEmpty()) {
            return;
        }

        new MessageDialog(messages);
    }

    protected boolean hasFatalMessage(List<Message> messages) {
        boolean isFatalMessage = false;
        for (Message msg : messages) {
            isFatalMessage = isFatalMessage || msg.isFatal();
        }

        return isFatalMessage;
    }

    private void initGrid() {
    	
        grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
    }

    private void renderQuestionnaire() {
    	
        renderTitle(form.getId());
        
        List<Row> visibleRows = createQuestions();
        
    	renderQuestions(visibleRows);
    	
        Button btn = renderButton(visibleRows.size() + 2);
              
        submit(btn, visibleRows);
    }
    
    private void submit(Button btn, List<Row> visibleRows) {
    	
        Text actiontarget = new Text();
        grid.add(actiontarget, 1, visibleRows.size() + 2);
    	
        btn.setOnAction(e -> complete(visibleRows, actiontarget));
    }
    
    private void complete(List<Row> visibleRows, Text actiontarget) {
        
    	actiontarget.setFill(Color.GREEN);
        actiontarget.setText("Thank you for filling in the questionnaire");

        export(visibleRows);
    }
    
    private void renderTitle(String title) {
        Text scenetitle = new Text(title);
        
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
    }
    
    private List<Row> createQuestions() {
        
    	QEvaluator qVisitor = new QEvaluator(environment);
    	qVisitor.visit(form);
    	return qVisitor.getVisibleRows();
    }
    
    private void renderQuestions(List<Row> visibleRows) {

    	int rowIndex = 1;
        for (Row row : visibleRows) {
            
        	environment.applyStyle(row.getName(), row.getLabel());
        	row.draw(grid, rowIndex);
            
            ++rowIndex;
        }
    }
    
    private Button renderButton(int rowIndex) {
    	 
        Button btn = new Button("Submit");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 3, rowIndex);
        
        return btn;
    }
    
	public void updateQuestionnaire(String name, Value newValue) {
		
		if (!environment.isAnswered(name) || !(environment.getAnswer(name).equals(newValue))) {

			environment.addAnswer(name, newValue);
	    	grid.getChildren().clear();
	        renderQuestionnaire();
	        grid.lookup("#" + name).requestFocus();
		}
	}

	private void export(List<Row> visibleRows) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage qStage = new Stage();

        File file = fileChooser.showSaveDialog(qStage);
        
        if (file == null) {
        	return;
        }

        try {
            JsonObjectBuilder questionnaire = Json.createObjectBuilder();

            for (Row question : visibleRows) {
                questionnaire.add(question.getName(), question.getAnswer());
            }

            OutputStream os = new FileOutputStream(file);
            JsonWriter jsonWriter = Json.createWriter(os);
            jsonWriter.writeObject(questionnaire.build());
            jsonWriter.close();
        } 
        catch (IOException ex) {
            throw new RuntimeException(
                    "This should never happen, this file exists", ex);
        }
	}
}
