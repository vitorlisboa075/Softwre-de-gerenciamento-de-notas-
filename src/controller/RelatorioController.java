package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import model.*;

import java.io.FileWriter;
import java.util.List;
import model.RelatorioAluno;

public class RelatorioController {

    @FXML private ComboBox<String> cursoComboBox;
    @FXML private ComboBox<String> turmaComboBox;
    @FXML private ComboBox<String> disciplinaComboBox;
    @FXML private TableView<RelatorioAluno> tabelaRelatorio;
    @FXML private TableColumn<RelatorioAluno, String> colNome;
    @FXML private TableColumn<RelatorioAluno, String> colMatricula;
    @FXML private TableColumn<RelatorioAluno, Integer> colFaltas;
    @FXML private TableColumn<RelatorioAluno, Double> colMedia;
    @FXML private TableColumn<RelatorioAluno, String> colSituacao;

    private final ObservableList<RelatorioAluno> relatorioData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Carregue os valores nos ComboBoxes
       

        // Configure colunas
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colMatricula.setCellValueFactory(cellData -> cellData.getValue().matriculaProperty());
        colFaltas.setCellValueFactory(cellData -> cellData.getValue().faltasProperty().asObject());
        colMedia.setCellValueFactory(cellData -> cellData.getValue().mediaProperty().asObject());
        colSituacao.setCellValueFactory(cellData -> cellData.getValue().situacaoProperty());

        tabelaRelatorio.setItems(relatorioData);
    }

    @FXML
    public void gerarRelatorio() {
        String curso = cursoComboBox.getValue();
        String turma = turmaComboBox.getValue();
        String disciplina = disciplinaComboBox.getValue();

        if (curso == null || turma == null || disciplina == null) {
            showAlert("Erro", "Selecione todos os campos.");
            return;
        }

        relatorioData.clear();

        List<Aluno> alunos = Dados.getAlunosPorTurma(turma);
        for (Aluno aluno : alunos) {
            int faltas = Dados.getFaltas(aluno.getId(), disciplina);
            double media = Dados.getMedia(aluno.getId(), disciplina);
            String situacao = (media >= 7.0 && faltas <= 25) ? "Aprovado" : "Reprovado"; // Ex: 25 faltas máx

            relatorioData.add(new RelatorioAluno(aluno.getNome(), aluno.getMatricula(), faltas, media, situacao));
        }
    }

    @FXML
    public void exportarCSV() {
        try (FileWriter writer = new FileWriter("relatorio.csv")) {
            writer.write("Nome,Matrícula,Faltas,Média,Situação\n");
            for (RelatorioAluno r : relatorioData) {
                writer.write(String.format("%s,%s,%d,%.2f,%s\n",
                        r.getNome(), r.getMatricula(), r.getFaltas(), r.getMedia(), r.getSituacao()));
            }
            showAlert("Exportação", "Relatório exportado como CSV.");
        } catch (Exception e) {
            showAlert("Erro", "Erro ao exportar CSV.");
        }
    }

    @FXML
    public void exportarPDF() {
        // Aqui você pode usar a biblioteca iText ou Apache PDFBox
        showAlert("PDF", "Funcionalidade de PDF ainda não implementada.");
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

