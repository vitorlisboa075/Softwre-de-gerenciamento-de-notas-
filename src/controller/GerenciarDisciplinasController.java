package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import model.Disciplina;
import model.DisciplinaWrapper;
import model.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GerenciarDisciplinasController {

    @FXML private TextField pesquisaField;
    @FXML private TableView<DisciplinaWrapper> tabelaDisciplinas;
    @FXML private TableColumn<DisciplinaWrapper, Boolean> colSelecao;
    @FXML private TableColumn<DisciplinaWrapper, String> colNome;
    @FXML private TableColumn<DisciplinaWrapper, String> colProfessor;
    @FXML private Button btnVoltar;

    private ObservableList<DisciplinaWrapper> masterDisciplinaList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarDadosSimulados();

        // Configura as colunas
        colSelecao.setCellValueFactory(cellData -> cellData.getValue().selecionadoProperty());
        colSelecao.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecao));
        tabelaDisciplinas.setEditable(true);

        colNome.setCellValueFactory(cellData -> cellData.getValue().getDisciplina().nomeProperty());
        
        // Para pegar o nome do professor, que está dentro de outro objeto
        colProfessor.setCellValueFactory(cellData -> {
            Usuario professor = cellData.getValue().getDisciplina().getProfessor();
            return new SimpleStringProperty(professor != null ? professor.getNome() : "Não definido");
        });
        
        // Configura a busca
        FilteredList<DisciplinaWrapper> filteredData = new FilteredList<>(masterDisciplinaList, p -> true);
        pesquisaField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(disciplinaWrapper -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return disciplinaWrapper.getDisciplina().getNome().toLowerCase().contains(lowerCaseFilter);
            });
        });

        tabelaDisciplinas.setItems(filteredData);
    }

    private void carregarDadosSimulados() {
        // Criando professores e disciplinas de exemplo
        Usuario prof1 = new Usuario(); prof1.setNome("Mariana Costa");
        Usuario prof2 = new Usuario(); prof2.setNome("Ricardo Alves");
        
        Disciplina d1 = new Disciplina(1, "Matemática", prof1);
        Disciplina d2 = new Disciplina(2, "Português", prof2);
        Disciplina d3 = new Disciplina(3, "História", prof1);

        masterDisciplinaList.addAll(
            new DisciplinaWrapper(d1),
            new DisciplinaWrapper(d2),
            new DisciplinaWrapper(d3)
        );
    }
    
    @FXML
    private void handleNovaDisciplina() {
        showAlert(Alert.AlertType.INFORMATION, "Ação", "Navegando para a tela de Cadastro de Disciplina...");
    }

    @FXML
    private void handleEditarDisciplina() {
        List<DisciplinaWrapper> selecionados = getDisciplinasSelecionadas();
        if (selecionados.size() != 1) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Por favor, selecione exatamente UMA disciplina para editar.");
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Ação", "Abrindo formulário para editar: " + selecionados.get(0).getDisciplina().getNome());
    }

    @FXML
    private void handleExcluirDisciplinas() {
        List<DisciplinaWrapper> selecionados = getDisciplinasSelecionadas();
        if (selecionados.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Selecione pelo menos uma disciplina para excluir.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir " + selecionados.size() + " disciplina(s)?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmação de Exclusão");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            masterDisciplinaList.removeAll(selecionados);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Disciplina(s) excluída(s) com sucesso.");
        }
    }

    private List<DisciplinaWrapper> getDisciplinasSelecionadas() {
        return masterDisciplinaList.stream()
                .filter(DisciplinaWrapper::isSelecionado)
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