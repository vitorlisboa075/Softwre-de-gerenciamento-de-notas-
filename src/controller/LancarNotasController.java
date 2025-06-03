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
import model.AlunoNota;

public class LancarNotasController {

    @FXML private ComboBox<String> cursoCombo;
    @FXML private ComboBox<String> turmaCombo;
    @FXML private ComboBox<String> disciplinaCombo;

    @FXML private TableView<AlunoNota> tabelaAlunos;
    @FXML private TableColumn<AlunoNota, String> colNome;
    @FXML private TableColumn<AlunoNota, String> colMatricula;
    @FXML private TableColumn<AlunoNota, Double> colNota1;
    @FXML private TableColumn<AlunoNota, Double> colNota2;
    @FXML private TableColumn<AlunoNota, Double> colNota3;
    @FXML private TableColumn<AlunoNota, Double> colMedia;

    private ObservableList<AlunoNota> listaAlunos = FXCollections.observableArrayList();

    @FXML
    private void salvarNotas() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notas Salvas");
        alert.setHeaderText(null);
        alert.setContentText("Notas salvas com sucesso!");
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
