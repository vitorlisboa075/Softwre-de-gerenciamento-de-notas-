// Local: src/controller/CadastroTurmaController.java
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import model.*;

import java.util.List;
import java.util.stream.Collectors;

public class CadastroTurmaController {

    // --- Campos da Turma ---
    @FXML private TextField nomeTurmaField;
    @FXML private ComboBox<String> periodoCombo;
    
    // --- Seção de Disciplinas ---
    @FXML private TextField pesquisaDisciplinaField;
    @FXML private TableView<DisciplinaWrapper> tabelaDisciplinas;
    @FXML private TableColumn<DisciplinaWrapper, Boolean> colSelecaoDisciplina;
    @FXML private TableColumn<DisciplinaWrapper, String> colNomeDisciplina;
    
    // --- Seção de Alunos ---
    @FXML private TextField pesquisaAlunoField;
    @FXML private TableView<UsuarioWrapper> tabelaAlunos;
    @FXML private TableColumn<UsuarioWrapper, Boolean> colSelecaoAluno;
    @FXML private TableColumn<UsuarioWrapper, String> colNomeAluno;
    @FXML private TableColumn<UsuarioWrapper, String> colCpfAluno;
    
    @FXML private Button btnVoltar;

    // --- Listas de Dados ---
    private ObservableList<DisciplinaWrapper> masterDisciplinaList = FXCollections.observableArrayList();
    private ObservableList<UsuarioWrapper> masterUsuarioList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        periodoCombo.getItems().addAll("Matutino", "Vespertino", "Noturno", "Integral");

        carregarDadosSimulados();
        configurarTabelaDisciplinas();
        configurarTabelaAlunos();
    }

    private void configurarTabelaDisciplinas() {
        // Configura colunas
        colSelecaoDisciplina.setCellValueFactory(cellData -> cellData.getValue().selecionadoProperty());
        colSelecaoDisciplina.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecaoDisciplina));
        colNomeDisciplina.setCellValueFactory(cellData -> cellData.getValue().getDisciplina().nomeProperty());
        tabelaDisciplinas.setEditable(true);

        // Configura filtro
        FilteredList<DisciplinaWrapper> filteredData = new FilteredList<>(masterDisciplinaList, p -> true);
        pesquisaDisciplinaField.textProperty().addListener((obs, oldV, newV) -> {
            filteredData.setPredicate(dw -> newV == null || newV.isEmpty() || dw.getDisciplina().getNome().toLowerCase().contains(newV.toLowerCase()));
        });
        tabelaDisciplinas.setItems(filteredData);
    }

    private void configurarTabelaAlunos() {
        // Filtra a lista mestra de usuários para pegar apenas alunos
        FilteredList<UsuarioWrapper> listaDeAlunos = new FilteredList<>(
            masterUsuarioList,
            uw -> "Aluno".equalsIgnoreCase(uw.getUsuario().getTipoUsuario())
        );

        // Configura colunas
        colSelecaoAluno.setCellValueFactory(cellData -> cellData.getValue().selecionadoProperty());
        colSelecaoAluno.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecaoAluno));
        colNomeAluno.setCellValueFactory(cellData -> cellData.getValue().getUsuario().nomeProperty());
        colCpfAluno.setCellValueFactory(cellData -> cellData.getValue().getUsuario().cpfProperty());
        tabelaAlunos.setEditable(true);

        // Configura filtro da busca
        FilteredList<UsuarioWrapper> filteredSearchData = new FilteredList<>(listaDeAlunos, p -> true);
        pesquisaAlunoField.textProperty().addListener((obs, oldV, newV) -> {
            filteredSearchData.setPredicate(uw -> {
                if (newV == null || newV.isEmpty()) return true;
                String filter = newV.toLowerCase();
                return uw.getUsuario().getNome().toLowerCase().contains(filter) || uw.getUsuario().getCpf().contains(filter);
            });
        });
        tabelaAlunos.setItems(filteredSearchData);
    }

    @FXML
    void salvarTurma() {
        String nome = nomeTurmaField.getText();
        String periodo = periodoCombo.getValue();
        if (nome.trim().isEmpty() || periodo == null) {
            showAlert(Alert.AlertType.WARNING, "Campos Incompletos", "Preencha o nome e o período da turma.");
            return;
        }

        List<Disciplina> disciplinasSelecionadas = masterDisciplinaList.stream()
                .filter(DisciplinaWrapper::isSelecionado).map(DisciplinaWrapper::getDisciplina).collect(Collectors.toList());

        List<Usuario> alunosSelecionados = masterUsuarioList.stream()
                .filter(uw -> uw.getUsuario().isAluno() && uw.isSelecionado()) // Garante que só pegamos alunos selecionados
                .map(UsuarioWrapper::getUsuario).collect(Collectors.toList());
        
        if (disciplinasSelecionadas.isEmpty() || alunosSelecionados.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Seleção Necessária", "Selecione ao menos uma disciplina e um aluno.");
            return;
        }

        Turma novaTurma = new Turma(0, nome, periodo); // ID seria gerado pelo banco
        novaTurma.setDisciplinas(disciplinasSelecionadas);
        novaTurma.setAlunos(alunosSelecionados);

        // Log para verificação
        System.out.println("--- Turma Salva ---");
        System.out.println("Nome: " + novaTurma.getNome() + " | Período: " + novaTurma.getPeriodo());
        System.out.println("Disciplinas: " + novaTurma.getDisciplinas().stream().map(Disciplina::getNome).collect(Collectors.joining(", ")));
        System.out.println("Alunos: " + novaTurma.getAlunos().stream().map(Usuario::getNome).collect(Collectors.joining(", ")));

        showAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Turma cadastrada com sucesso.");
        limparCampos();
    }

    @FXML
    void limparCampos() {
        nomeTurmaField.clear();
        periodoCombo.getSelectionModel().clearSelection();
        pesquisaDisciplinaField.clear();
        pesquisaAlunoField.clear();
        masterDisciplinaList.forEach(dw -> dw.selecionadoProperty().set(false));
        masterUsuarioList.forEach(uw -> uw.selecionadoProperty().set(false));
    }
    
    private void carregarDadosSimulados() {
        // Disciplinas
        Usuario prof1 = new Usuario(); prof1.setNome("Mariana Costa");
        Disciplina d1 = new Disciplina(1, "Matemática", prof1);
        Disciplina d2 = new Disciplina(2, "Português", prof1);
        masterDisciplinaList.addAll(new DisciplinaWrapper(d1), new DisciplinaWrapper(d2));

        // Usuários de todos os tipos
        Usuario a1 = new Usuario(); a1.setNome("Carlos Souza"); a1.setCpf("11122233344"); a1.setTipoUsuario("Aluno");
        Usuario a2 = new Usuario(); a2.setNome("Beatriz Lima"); a2.setCpf("33344455566"); a2.setTipoUsuario("Aluno");
        masterUsuarioList.addAll(new UsuarioWrapper(a1), new UsuarioWrapper(a2), new UsuarioWrapper(prof1));
    }

    @FXML private void voltar(ActionEvent event) { /* ... Lógica para voltar ... */ }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}