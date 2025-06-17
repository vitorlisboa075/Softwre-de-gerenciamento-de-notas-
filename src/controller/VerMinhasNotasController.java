package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.BoletimView;
import model.Conexao;
import util.Sessao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class VerMinhasNotasController {

    // --- FXML ---
    @FXML private Label labelBoasVindas;
    @FXML private TableView<BoletimView> tabelaBoletim;
    @FXML private TableColumn<BoletimView, String> colDisciplina;
    @FXML private TableColumn<BoletimView, String> colProfessor;
    @FXML private TableColumn<BoletimView, Double> colNota1;
    @FXML private TableColumn<BoletimView, Double> colNota2;
    @FXML private TableColumn<BoletimView, Double> colNota3;
    @FXML private TableColumn<BoletimView, Double> colMedia;
    @FXML private TableColumn<BoletimView, Integer> colPresencas;
    @FXML private TableColumn<BoletimView, String> colSituacao;

    // --- Dados ---
    private final ObservableList<BoletimView> boletimData = FXCollections.observableArrayList();

    // --- Inicialização ---
    @FXML
    public void initialize() {
        String email = Sessao.getEmail();
        String nome  = (email != null) ? email.split("@")[0] : "Aluno";
        labelBoasVindas.setText("Boletim de Notas: " + nome);

        colDisciplina.setCellValueFactory(new PropertyValueFactory<>("disciplina"));
        colProfessor.setCellValueFactory(new PropertyValueFactory<>("professor"));
        colNota1.setCellValueFactory(new PropertyValueFactory<>("nota1"));
        colNota2.setCellValueFactory(new PropertyValueFactory<>("nota2"));
        colNota3.setCellValueFactory(new PropertyValueFactory<>("nota3"));
        colMedia.setCellValueFactory(new PropertyValueFactory<>("media"));
        colPresencas.setCellValueFactory(new PropertyValueFactory<>("presencas"));
        colSituacao.setCellValueFactory(new PropertyValueFactory<>("situacao"));

        tabelaBoletim.setItems(boletimData);
        carregarBoletimDoBanco();
    }

    // --- Carregar dados do BD ---
    private void carregarBoletimDoBanco() {
        boletimData.clear();

        String cpfAluno = Sessao.getCpf();
        if (cpfAluno == null) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Sessão inválida",
                    "CPF do aluno não encontrado na sessão.");
            return;
        }

        String sqlNotas = """
            SELECT n.turma_id,
                   d.nome       AS disciplina,
                   uProf.nome   AS professor,
                   n.nota1, n.nota2, n.nota3
            FROM notas n
            JOIN turmas      t     ON t.id = n.turma_id
            JOIN disciplinas d     ON d.id = t.disciplina_id
            JOIN usuarios    uProf ON uProf.cpf = t.professor_cpf
            WHERE n.aluno_cpf = ?
        """;

        String sqlPresencas = """
            SELECT turma_id,
                   COUNT(*)      AS totalReg,
                   SUM(presente) AS presencas
            FROM presencas
            WHERE aluno_cpf = ?
            GROUP BY turma_id
        """;

        Map<Integer, int[]> mapaPresenca = new HashMap<>();

        try (Connection con = Conexao.getConnection()) {

            // Presenças por turma
            try (PreparedStatement ps = con.prepareStatement(sqlPresencas)) {
                ps.setString(1, cpfAluno);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int turmaId = rs.getInt("turma_id");
                        int total = rs.getInt("totalReg");
                        int presencas = rs.getInt("presencas");
                        mapaPresenca.put(turmaId, new int[]{total, presencas});
                    }
                }
            }

            // Notas
            try (PreparedStatement ps = con.prepareStatement(sqlNotas)) {
                ps.setString(1, cpfAluno);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int turmaId = rs.getInt("turma_id");
                        String disciplina = rs.getString("disciplina");
                        String professor = rs.getString("professor");
                        double n1 = rs.getDouble("nota1");
                        double n2 = rs.getDouble("nota2");
                        double n3 = rs.getDouble("nota3");

                        int total = 0, presencas = 0;
                        if (mapaPresenca.containsKey(turmaId)) {
                            int[] p = mapaPresenca.get(turmaId);
                            total = p[0];
                            presencas = p[1];
                        }
                        int faltas = total - presencas;

                        double media = (n1 + n2 + n3) / 3.0;
                        boolean aprovadoNotas = media >= 6.0;
                        boolean aprovadoFaltas = total == 0 || faltas <= total * 0.25;

                        String situacao = (aprovadoNotas && aprovadoFaltas) ? "Aprovado" : "Reprovado";

                        boletimData.add(new BoletimView(
                                disciplina, professor,
                                n1, n2, n3,
                                media, presencas, situacao));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Erro de banco de dados",
                    "Não foi possível carregar o boletim.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert a = new Alert(tipo, msg, ButtonType.OK);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
