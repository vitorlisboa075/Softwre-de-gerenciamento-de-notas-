package util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TrocaDeTela {

    public static void abrir(ActionEvent event, String caminhoFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(TrocaDeTela.class.getResource(caminhoFXML));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(TrocaDeTela.class.getResource("/view/styles.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
