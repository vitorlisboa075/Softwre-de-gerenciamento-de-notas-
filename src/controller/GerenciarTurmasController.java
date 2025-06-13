package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import model.Turma;
import model.TurmaWrapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GerenciarTurmasController {

    @FXML private TextField pesquisaField;
    @FXML private TableView<TurmaWrapper> tabelaTurmas;
    @FXML private TableColumn<TurmaWrapper, Boolean> colSelecao;
    @FXML private TableColumn<TurmaWrapper, String> colNome;
    @FXML private TableColumn<TurmaWrapper, String> colPeriodo;
    @FXML private Button btnVoltar;

    private ObservableList<TurmaWrapper> masterTurmaList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarDadosSimulados();

        // Configura as colunas
        colSelecao.setCellValueFactory(cellData -> cellData.getValue().selecionadoProperty());
        colSelecao.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecao));
        tabelaTurmas.setEditable(true);

        colNome.setCellValueFactory(cellData -> cellData.getValue().getTurma().nomeProperty());
        colPeriodo.setCellValueFactory(cellData -> cellData.getValue().getTurma().periodoProperty());
        
        // Configura a busca
        FilteredList<TurmaWrapper> filteredData = new FilteredList<>(masterTurmaList, p -> true);
        pesquisaField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(turmaWrapper -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return turmaWrapper.getTurma().getNome().toLowerCase().contains(lowerCaseFilter);
            });
        });

        tabelaTurmas.setItems(filteredData);
    }

    private void carregarDadosSimulados() {
        // Criando turmas de exemplo
        Turma t1 = new Turma(1, "1º Ano A", "Matutino");
        Turma t2 = new Turma(2, "2º Ano B", "Vespertino");
        Turma t3 = new Turma(3, "3º Ano C", "Noturno");

        masterTurmaList.addAll(
            new TurmaWrapper(t1),
            new TurmaWrapper(t2),
            new TurmaWrapper(t3)
        );
    }
    
    @FXML
    private void handleNovaTurma() {
        showAlert(Alert.AlertType.INFORMATION, "Ação", "Navegando para a tela de Cadastro de Turma...");
    }

    @FXML
    private void handleEditarTurma() {
        List<TurmaWrapper> selecionados = getTurmasSelecionadas();
        if (selecionados.size() != 1) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Por favor, selecione exatamente UMA turma para editar.");
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Ação", "Abrindo formulário para editar: " + selecionados.get(0).getTurma().getNome());
    }

    @FXML
    private void handleExcluirTurmas() {
        List<TurmaWrapper> selecionados = getTurmasSelecionadas();
        if (selecionados.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Selecione pelo menos uma turma para excluir.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir " + selecionados.size() + " turma(s)?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmação de Exclusão");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            masterTurmaList.removeAll(selecionados);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Turma(s) excluída(s) com sucesso.");
        }
    }

    private List<TurmaWrapper> getTurmasSelecionadas() {
        return masterTurmaList.stream()
                .filter(TurmaWrapper::isSelecionado)
                .collect(Collectors.toList());
    }

    @FXML private void voltar(ActionEvent event) { /* ... */ }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}