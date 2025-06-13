package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RelatorioController {

    // --- Componentes FXML ---
    @FXML private ComboBox<Turma> turmaComboBox;
    @FXML private ComboBox<Disciplina> disciplinaComboBox;
    @FXML private TableView<RelatorioAluno> tabelaRelatorio;
    @FXML private TableColumn<RelatorioAluno, String> colNome;
    @FXML private TableColumn<RelatorioAluno, String> colMatricula;
    @FXML private TableColumn<RelatorioAluno, Integer> colFaltas;
    @FXML private TableColumn<RelatorioAluno, Double> colMedia;
    @FXML private TableColumn<RelatorioAluno, String> colSituacao;
    @FXML private Button btnVoltar;

    private ObservableList<RelatorioAluno> relatorioData = FXCollections.observableArrayList();
    private ObservableList<Turma> todasAsTurmas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Simula o carregamento de todas as turmas do sistema
        carregarDadosSimulados();
        
        // Configura os ComboBoxes
        turmaComboBox.setItems(todasAsTurmas);
        turmaComboBox.setConverter(new TurmaStringConverter());

        turmaComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                disciplinaComboBox.setItems(FXCollections.observableArrayList(newV.getDisciplinas()));
                disciplinaComboBox.setConverter(new DisciplinaStringConverter());
            }
        });

        // Configura as colunas da tabela
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colFaltas.setCellValueFactory(new PropertyValueFactory<>("faltas"));
        colMedia.setCellValueFactory(new PropertyValueFactory<>("media"));
        colSituacao.setCellValueFactory(new PropertyValueFactory<>("situacao"));

        tabelaRelatorio.setItems(relatorioData);
    }

    @FXML
    public void gerarRelatorio() {
        Turma turma = turmaComboBox.getValue();
        Disciplina disciplina = disciplinaComboBox.getValue();

        if (turma == null || disciplina == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção Incompleta", "Por favor, selecione uma turma e uma disciplina.");
            return;
        }

        relatorioData.clear();

        // --- SIMULAÇÃO DE DADOS ---
        // Em um sistema real, você buscaria as notas e faltas do banco de dados
        // para cada aluno desta turma e disciplina.
        Random random = new Random();
        for (Usuario aluno : turma.getAlunos()) {
            double media = 5 + random.nextDouble() * 5; // Média aleatória entre 5.0 e 10.0
            int faltas = random.nextInt(15); // Faltas aleatórias entre 0 e 14
            String situacao = (media >= 6.0 && faltas < 20) ? "Aprovado" : "Reprovado"; // Exemplo de regra

            relatorioData.add(new RelatorioAluno(aluno.getNome(), aluno.getMatricula(), faltas, media, situacao));
        }
    }

    // Mantendo sua funcionalidade original de exportação
    @FXML
    public void exportarCSV() {
        if (relatorioData.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Aviso", "Não há dados no relatório para exportar.");
            return;
        }
        try (FileWriter writer = new FileWriter("relatorio_turma.csv")) {
            // Escreve o cabeçalho do CSV
            writer.write("Matricula,Nome,Media,Faltas,Situacao\n");
            // Escreve os dados de cada aluno
            for (RelatorioAluno r : relatorioData) {
                writer.write(String.format("%s,%s,%.2f,%d,%s\n",
                        r.getMatricula(), r.getNome(), r.getMedia(), r.getFaltas(), r.getSituacao()));
            }
            showAlert(Alert.AlertType.INFORMATION, "Exportação Concluída", "Relatório exportado com sucesso para relatorio_turma.csv");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Exportação", "Ocorreu um erro ao exportar o arquivo CSV.");
        }
    }

    @FXML
    public void onExportPdfClick(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Funcionalidade Futura", "A exportação para PDF ainda não foi implementada.");
    }

    private void carregarDadosSimulados() {
        // Simulação de dados para preencher os ComboBoxes
        Usuario prof1 = new Usuario(); prof1.setNome("Mariana Costa");
        Usuario prof2 = new Usuario(); prof2.setNome("Ricardo Alves");
        Disciplina d1 = new Disciplina(1, "Matemática", prof1);
        Disciplina d2 = new Disciplina(2, "Português", prof2);

        Usuario a1 = new Usuario(); a1.setNome("Carlos Souza"); a1.setMatricula("2024001"); a1.setTipoUsuario("Aluno");
        Usuario a2 = new Usuario(); a2.setNome("Beatriz Lima"); a2.setMatricula("2024003"); a2.setTipoUsuario("Aluno");
        
        Turma turmaA = new Turma(1, "1º Ano A", "Matutino");
        turmaA.setDisciplinas(new ArrayList<>(List.of(d1, d2)));
        turmaA.setAlunos(new ArrayList<>(List.of(a1, a2)));
        
        todasAsTurmas.add(turmaA);
    }
    
    @FXML private void voltar(ActionEvent event) { /* ... */ }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Classes internas para converter objetos para String nos ComboBoxes
    private static class TurmaStringConverter extends javafx.util.StringConverter<Turma> {
        @Override public String toString(Turma t) { return t == null ? null : t.getNome(); }
        @Override public Turma fromString(String s) { return null; }
    }
    private static class DisciplinaStringConverter extends javafx.util.StringConverter<Disciplina> {
        @Override public String toString(Disciplina d) { return d == null ? null : d.getNome(); }
        @Override public Disciplina fromString(String s) { return null; }
    }
}