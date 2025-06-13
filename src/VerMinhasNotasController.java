package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.BoletimView;
import util.Sessao;

public class VerMinhasNotasController {

    @FXML private Label labelBoasVindas;
    @FXML private TableView<BoletimView> tabelaBoletim;
    @FXML private TableColumn<BoletimView, String> colDisciplina;
    @FXML private TableColumn<BoletimView, String> colProfessor;
    @FXML private TableColumn<BoletimView, Double> colNota1;
    @FXML private TableColumn<BoletimView, Double> colNota2;
    @FXML private TableColumn<BoletimView, Double> colNota3;
    @FXML private TableColumn<BoletimView, Double> colMedia;
    @FXML private TableColumn<BoletimView, Integer> colFaltas;
    @FXML private TableColumn<BoletimView, String> colSituacao;

    private ObservableList<BoletimView> boletimData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Personaliza a mensagem de boas-vindas
        String emailAluno = Sessao.getEmail();
        if (emailAluno != null) {
            // Pega o nome do usuário antes do @
            String nomeUsuario = emailAluno.split("@")[0];
            labelBoasVindas.setText("Boletim de Notas: " + nomeUsuario);
        }

        // Configura as colunas da tabela
        colDisciplina.setCellValueFactory(new PropertyValueFactory<>("disciplina"));
        colProfessor.setCellValueFactory(new PropertyValueFactory<>("professor"));
        colNota1.setCellValueFactory(new PropertyValueFactory<>("nota1"));
        colNota2.setCellValueFactory(new PropertyValueFactory<>("nota2"));
        colNota3.setCellValueFactory(new PropertyValueFactory<>("nota3"));
        colMedia.setCellValueFactory(new PropertyValueFactory<>("media"));
        colFaltas.setCellValueFactory(new PropertyValueFactory<>("faltas"));
        colSituacao.setCellValueFactory(new PropertyValueFactory<>("situacao"));
        
        // Carrega os dados (simulados) e popula a tabela
        carregarDadosDoBoletim();
        tabelaBoletim.setItems(boletimData);
    }

    private void carregarDadosDoBoletim() {
        // --- SIMULAÇÃO ---
        // Em um sistema real, aqui você faria uma consulta ao banco de dados:
        // "SELECT notas FROM tabela_notas WHERE aluno_id = ?"
        // Por enquanto, vamos criar dados fixos para demonstração.

        boletimData.add(new BoletimView("Matemática", "Mariana Costa", 8.5, 7.0, 9.0, 2));
        boletimData.add(new BoletimView("Português", "Ricardo Alves", 6.0, 5.5, 4.0, 5));
        boletimData.add(new BoletimView("História", "Julio Cesar", 9.5, 10.0, 8.0, 0));
        boletimData.add(new BoletimView("Ciências", "Sofia Lima", 5.0, 4.5, 6.0, 10));
    }
}