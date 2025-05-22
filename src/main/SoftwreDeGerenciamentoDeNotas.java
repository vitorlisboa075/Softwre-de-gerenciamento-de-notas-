package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SoftwreDeGerenciamentoDeNotas extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        primaryStage.setTitle("Sistema de Gerenciamento de Notas");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false); // impede redimensionar
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
