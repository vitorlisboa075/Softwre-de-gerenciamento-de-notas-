package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import model.NotaAluno;
import model.Disciplina;
import model.Turma;
import model.Usuario;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LancarNotasController {

    @FXML private ComboBox<Turma> turmaCombo;
    @FXML private ComboBox<Disciplina> disciplinaCombo;
    @FXML private Button btnCarregarAlunos;
    @FXML private TableView<NotaAluno> tabelaNotas;
    @FXML private TableColumn<NotaAluno, String> colNome;
    @FXML private TableColumn<NotaAluno, String> colMatricula;
    @FXML private TableColumn<NotaAluno, Double> colNota1;
    @FXML private TableColumn<NotaAluno, Double> colNota2;
    @FXML private TableColumn<NotaAluno, Double> colNota3;
    @FXML private TableColumn<NotaAluno, Double> colMedia;
    @FXML private Button btnVoltar;

    private ObservableList<Turma> turmasDoProfessor = FXCollections.observableArrayList();
    private ObservableList<NotaAluno> alunosDaTurma = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarDadosSimulados();

        turmaCombo.setItems(turmasDoProfessor);
        turmaCombo.setConverter(new TurmaStringConverter());

        turmaCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldTurma, newTurma) -> {
            if (newTurma != null) {
                disciplinaCombo.setItems(FXCollections.observableArrayList(newTurma.getDisciplinas()));
                disciplinaCombo.setConverter(new DisciplinaStringConverter());
                tabelaNotas.getItems().clear();
            }
        });

        configurarTabela();
    }
    
    private void configurarTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));

        // Configurando colunas editáveis
        colNota1.setCellValueFactory(cellData -> cellData.getValue().nota1Property().asObject());
        colNota1.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colNota1.setOnEditCommit(event -> {
            NotaAluno aluno = event.getRowValue();
            aluno.setNota1(event.getNewValue());
        });

        colNota2.setCellValueFactory(cellData -> cellData.getValue().nota2Property().asObject());
        colNota2.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colNota2.setOnEditCommit(event -> event.getRowValue().setNota2(event.getNewValue()));

        colNota3.setCellValueFactory(cellData -> cellData.getValue().nota3Property().asObject());
        colNota3.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colNota3.setOnEditCommit(event -> event.getRowValue().setNota3(event.getNewValue()));

        colMedia.setCellValueFactory(new PropertyValueFactory<>("media"));
        formatarColunaMedia();

        tabelaNotas.setItems(alunosDaTurma);
    }

    @FXML
    private void handleCarregarAlunos() {
        Turma turmaSelecionada = turmaCombo.getValue();
        if (turmaSelecionada == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção Necessária", "Por favor, selecione uma turma.");
            return;
        }
        alunosDaTurma.clear();
        for (Usuario aluno : turmaSelecionada.getAlunos()) {
            alunosDaTurma.add(new NotaAluno(aluno));
        }
    }

    @FXML
    private void salvarNotas() {
        if (alunosDaTurma.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Não há notas para salvar.");
            return;
        }
        System.out.println("--- SALVANDO NOTAS ---");
        for (NotaAluno na : alunosDaTurma) {
            System.out.printf("Aluno: %s | Nota 1: %.2f | Nota 2: %.2f | Nota 3: %.2f | Média: %.2f%n",
                na.getNome(), na.getNota1(), na.getNota2(), na.getNota3(), na.getMedia());
        }
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Notas salvas com sucesso!");
    }
    
    private void carregarDadosSimulados() {
        Usuario prof = new Usuario(); prof.setNome("Mariana Costa"); prof.setTipoUsuario("Professor");
        Disciplina d1 = new Disciplina(1, "Matemática", prof);
        Disciplina d2 = new Disciplina(2, "Português", prof);
        Usuario a1 = new Usuario(); a1.setNome("Carlos Souza"); a1.setMatricula("2024001"); a1.setTipoUsuario("Aluno");
        Usuario a2 = new Usuario(); a2.setNome("Beatriz Lima"); a2.setMatricula("2024003"); a2.setTipoUsuario("Aluno");
        Turma turmaA = new Turma(1, "1º Ano A", "Matutino");
        turmaA.setDisciplinas(new ArrayList<>(List.of(d1, d2)));
        turmaA.setAlunos(new ArrayList<>(List.of(a1, a2)));
        turmasDoProfessor.add(turmaA);
    }

    private void formatarColunaMedia() {
        DecimalFormat df = new DecimalFormat("#.##");
        colMedia.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : df.format(item));
            }
        });
    }

    @FXML private void voltar(ActionEvent event) { /* ... */ }
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type, msg, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    private static class TurmaStringConverter extends javafx.util.StringConverter<Turma> {
        @Override public String toString(Turma t) { return t == null ? "" : t.getNome(); }
        @Override public Turma fromString(String s) { return null; }
    }
    private static class DisciplinaStringConverter extends javafx.util.StringConverter<Disciplina> {
        @Override public String toString(Disciplina d) { return d == null ? "" : d.getNome(); }
        @Override public Disciplina fromString(String s) { return null; }
    }
}