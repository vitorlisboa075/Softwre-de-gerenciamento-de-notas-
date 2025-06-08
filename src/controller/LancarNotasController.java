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
import javafx.scene.layout.BorderPane;
import model.AlunoNota;

public class LancarNotasController {

    @FXML private ComboBox<String> cursoCombo;
    @FXML private ComboBox<String> turmaCombo;
    @FXML private ComboBox<String> disciplinaCombo;
    @FXML private Button btnVoltar;

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
