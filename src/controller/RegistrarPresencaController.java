package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import model.AlunoPresenca;

public class RegistrarPresencaController {

    @FXML private ComboBox<String> cursoCombo;
    @FXML private ComboBox<String> turmaCombo;
    @FXML private ComboBox<String> disciplinaCombo;

    @FXML private TableView<AlunoPresenca> tabelaAlunos;
    @FXML private TableColumn<AlunoPresenca, String> colNome;
    @FXML private TableColumn<AlunoPresenca, String> colMatricula;
    @FXML private TableColumn<AlunoPresenca, Boolean> colPresenca;

    private ObservableList<AlunoPresenca> listaAlunos = FXCollections.observableArrayList();

    @FXML
    private void salvarPresenca() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Presença Salva");
        alert.setHeaderText(null);
        alert.setContentText("Presença salva com sucesso!");
        alert.showAndWait();
    }

    @FXML
    private void limparCampos() {
        cursoCombo.getSelectionModel().clearSelection();
        turmaCombo.getSelectionModel().clearSelection();
        disciplinaCombo.getSelectionModel().clearSelection();
        tabelaAlunos.getItems().clear();
    }

    @FXML
    private void voltar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MenuPrincipal.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
