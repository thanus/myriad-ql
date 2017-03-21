package UvA.Gamma.GUI;

import UvA.Gamma.AST.Form;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Created by Tjarco, 13-02-17.
 */
public class MainScreen {
    private Form form;

    public MainScreen(Form form) {
        this.form = form;
    }

    public void initUI(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Form.fxml"));
        Parent root = loader.load();
        FXMLController controller = loader.getController();
        controller.addFormItems(form);
        stage.setTitle(form.getId());
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.show();
    }
}

