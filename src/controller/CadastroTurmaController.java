package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import javafx.scene.layout.BorderPane;

public class CadastroTurmaController {

    @FXML private TextField nomeField;
    @FXML private TextField descricaoField;
    @FXML private TextField semestreField;
    @FXML private TextField salaField;
    @FXML private TextField horarioField;
    @FXML private ComboBox<String> cursoCombo;
    @FXML private ComboBox<String> disciplinaCombo;
    @FXML private Button btnVoltar;

    @FXML
    private void cadastrarTurma() {
        String nome = nomeField.getText();
        String descricao = descricaoField.getText();
        String semestre = semestreField.getText();
        String sala = salaField.getText();
        String horario = horarioField.getText();

        if (nome.isEmpty() || semestre.isEmpty() || sala.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Preencha os campos obrigatórios.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cadastro Realizado");
        alert.setHeaderText(null);
        alert.setContentText("Turma cadastrada com sucesso!");
        alert.showAndWait();

        limparCampos();
    }

    @FXML
    private void limparCampos() {
        nomeField.clear();
        descricaoField.clear();
        semestreField.clear();
        salaField.clear();
        horarioField.clear();
        cursoCombo.getSelectionModel().clearSelection();
        disciplinaCombo.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void voltar(ActionEvent event) {
        MenuPrincipalController menuController = buscarMenuController();
        if (menuController != null) {
            menuController.restaurarEstado();
            menuController.limparConteudo();
        }
    }

    

    private MenuPrincipalController buscarMenuController() {
        Parent root = btnVoltar.getScene().getRoot();
        if (root instanceof BorderPane borderPane) {
            Object controller = borderPane.getUserData();
            if (controller instanceof MenuPrincipalController menuController) {
                return menuController;
            }
        }
        return null;
    }



}
