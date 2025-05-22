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

public class CadastroDisciplinaController {

    @FXML private TextField nomeField;
    @FXML private TextField descricaoField;
    @FXML private TextField cargaHorariaField;
    @FXML private TextField codigoField;
    @FXML private TextArea ementaField;
    @FXML private TextField semestreField;
    @FXML private ComboBox<String> cursoCombo;

    @FXML
    private void cadastrarDisciplina() {
        String nome = nomeField.getText();
        String descricao = descricaoField.getText();
        String carga = cargaHorariaField.getText();
        String codigo = codigoField.getText();
        String ementa = ementaField.getText();
        String semestre = semestreField.getText();

        if (nome.isEmpty() || carga.isEmpty() || codigo.isEmpty()) {
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
        alert.setContentText("Disciplina cadastrada com sucesso!");
        alert.showAndWait();

        limparCampos();
    }

    @FXML
    private void limparCampos() {
        nomeField.clear();
        descricaoField.clear();
        cargaHorariaField.clear();
        codigoField.clear();
        ementaField.clear();
        semestreField.clear();
        cursoCombo.getSelectionModel().clearSelection();
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
