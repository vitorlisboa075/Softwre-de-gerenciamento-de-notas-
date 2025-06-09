package controller;

import model.MockDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Curso;
import model.Disciplina;
import model.Professor;

public class DisciplinaController {

    @FXML
    private TableView<Disciplina> tabelaDisciplinas;
    @FXML
    private TableColumn<Disciplina, Integer> colId;
    @FXML
    private TableColumn<Disciplina, String> colNome;
    @FXML
    private TableColumn<Disciplina, String> colCodigo;
    @FXML
    private TableColumn<Disciplina, String> colCurso;
    @FXML
    private TableColumn<Disciplina, String> colProfessor;
    @FXML
    private TableColumn<Disciplina, Integer> colCargaHoraria;
    @FXML
    private TableColumn<Disciplina, Integer> colSemestre;

    @FXML
    private Button btnNova;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnExcluir;

    private ObservableList<Disciplina> listaDisciplinas;

    public void initialize() {
        // Configurar colunas da tabela
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCurso.setCellValueFactory(cell -> {
            Curso c = cell.getValue().getCurso();
            return c != null ? new javafx.beans.property.SimpleStringProperty(c.getNome()) :
                    new javafx.beans.property.SimpleStringProperty("");
        });
        colProfessor.setCellValueFactory(cell -> {
            Professor p = cell.getValue().getProfessor();
            return p != null ? new javafx.beans.property.SimpleStringProperty(p.getNome()) :
                    new javafx.beans.property.SimpleStringProperty("");
        });
        colCargaHoraria.setCellValueFactory(new PropertyValueFactory<>("cargaHoraria"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));

        // Carregar dados da "base"
        listaDisciplinas = FXCollections.observableArrayList(MockDB.getDisciplinas());
        tabelaDisciplinas.setItems(listaDisciplinas);

        // Configurar botões
        btnNova.setOnAction(e -> abrirFormulario(null));
        btnEditar.setOnAction(e -> {
            Disciplina selecionada = tabelaDisciplinas.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                abrirFormulario(selecionada);
            } else {
                alert("Selecione uma disciplina para editar.");
            }
        });
        btnExcluir.setOnAction(e -> {
            Disciplina selecionada = tabelaDisciplinas.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                // Aqui você deve validar se a disciplina tem turmas vinculadas antes de remover
                boolean podeExcluir = verificarTurmasVinculadas(selecionada);
                if (podeExcluir) {
                    listaDisciplinas.remove(selecionada);
                    alert("Disciplina excluída com sucesso.");
                } else {
                    alert("Não é possível excluir esta disciplina pois há turmas vinculadas.");
                }
            } else {
                alert("Selecione uma disciplina para excluir.");
            }
        });
    }

    private void abrirFormulario(Disciplina disciplina) {
        // Implementação de abertura do form de cadastro/edição de disciplina
        // Pode ser um diálogo, outro Stage, etc.
        // Passar a disciplina para edição ou null para novo cadastro
        System.out.println("Abrir formulário para: " + (disciplina == null ? "Nova disciplina" : disciplina.getNome()));
        // TODO: implementar diálogo com formulário
    }

    private boolean verificarTurmasVinculadas(Disciplina disciplina) {
        // Aqui você faria a verificação real na base de turmas para saber se alguma usa essa disciplina
        // Por ora, só retornamos true para simular que pode excluir
        return true;
    }

    private void alert(String msg) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}
