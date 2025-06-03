package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import util.Sessao;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private void fazerLogin(ActionEvent event) {
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (email.equals("secretaria@if.com") && senha.equals("123")) {
            Sessao.setTipoUsuario("secretaria");
            Sessao.setEmail(email);
            abrirMenu(event);
        } else if (email.equals("professor@if.com") && senha.equals("123")) {
            Sessao.setTipoUsuario("professor");
            Sessao.setEmail(email);
            abrirMenu(event);
        } else if (email.equals("admin@if.com") && senha.equals("123")) {
            Sessao.setTipoUsuario("admin");
            Sessao.setEmail(email);
            abrirMenu(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Login");
            alert.setHeaderText(null);
            alert.setContentText("Email ou senha incorretos.");
            alert.showAndWait();
        }
    }

    private void abrirMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MenuPrincipal.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Principal");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
