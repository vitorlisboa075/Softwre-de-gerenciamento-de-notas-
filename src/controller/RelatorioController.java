package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RelatorioController {

    /* ---------- FXML ---------- */
    @FXML private ComboBox<Turma>      turmaComboBox;
    @FXML private ComboBox<Disciplina> disciplinaComboBox;

    @FXML private TableView<RelatorioAluno> tabelaRelatorio;
    @FXML private TableColumn<RelatorioAluno,String>  colNome;
    @FXML private TableColumn<RelatorioAluno,String>  colMatricula;
    @FXML private TableColumn<RelatorioAluno,Integer> colPresencas;
    @FXML private TableColumn<RelatorioAluno,Double>  colMedia;
    @FXML private TableColumn<RelatorioAluno,String>  colSituacao;

    /* ---------- Dados ---------- */
    private final ObservableList<RelatorioAluno> relatorioData = FXCollections.observableArrayList();
    private final ObservableList<Turma>          turmas        = FXCollections.observableArrayList();

    /* ---------- INIT ---------- */
    @FXML
    public void initialize() {
        carregarTurmasDoBanco();

        /* combos ------------------------------------------------------- */
        turmaComboBox.setItems(turmas);
        turmaComboBox.setConverter(new TurmaStringConverter());

        disciplinaComboBox.setDisable(true);
        disciplinaComboBox.setConverter(new DisciplinaStringConverter());

        turmaComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldT, newT) -> {
                    disciplinaComboBox.getItems().clear();
                    disciplinaComboBox.getSelectionModel().clearSelection();
                    relatorioData.clear();

                    if (newT != null) {
                        disciplinaComboBox.setItems(
                                FXCollections.observableArrayList(newT.getDisciplinas()));
                        disciplinaComboBox.setDisable(false);
                    } else {
                        disciplinaComboBox.setDisable(true);
                    }
                });

        /* colunas ------------------------------------------------------- */
        colNome     .setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colPresencas.setCellValueFactory(new PropertyValueFactory<>("presencas"));
        colMedia    .setCellValueFactory(new PropertyValueFactory<>("media"));
        colSituacao .setCellValueFactory(new PropertyValueFactory<>("situacao"));

        tabelaRelatorio.setItems(relatorioData);
    }

    /* ---------- Gera Relatório ---------- */
    @FXML
    public void gerarRelatorio() {
        Turma turma     = turmaComboBox.getValue();
        Disciplina disc = disciplinaComboBox.getValue();

        if (turma == null || disc == null) {
            alert(Alert.AlertType.WARNING, "Seleção incompleta",
                  "Escolha a turma e, depois, a disciplina.");
            return;
        }

        relatorioData.clear();

        String sql = """
            SELECT u.nome, a.matricula, a.cpf,
                   SUM(p.presente) AS presencas,
                   COUNT(*)        AS totalReg
              FROM presencas p
              JOIN alunos   a ON a.cpf = p.aluno_cpf
              JOIN usuarios u ON u.cpf = a.cpf
             WHERE p.turma_id = ?
             GROUP BY a.cpf, a.matricula, u.nome
             ORDER BY u.nome
        """;

        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, turma.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    String nome      = rs.getString("nome");
                    String matricula = rs.getString("matricula");
                    String cpf       = rs.getString("cpf");
                    int    pres      = rs.getInt("presencas");
                    int    total     = rs.getInt("totalReg");
                    int    faltas    = total - pres;

                    double media = buscarMedia(con, turma.getId(), disc.getId(), cpf);

                    String situ  = (media >= 6.0 && faltas <= total * 0.25)
                                   ? "Aprovado" : "Reprovado";

                    relatorioData.add(new RelatorioAluno(
                            nome, matricula, pres, media, situ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Erro",
                  "Falha ao gerar o relatório.");
        }
    }

    /* ---------- Média ---------- */
    private double buscarMedia(Connection c, int turmaId, int discId, String cpf) {
        String q = """
            SELECT (nota1 + nota2 + nota3) / 3 AS m
              FROM notas
             WHERE turma_id = ? AND disciplina_id = ? AND aluno_cpf = ?
        """;
        try (PreparedStatement ps = c.prepareStatement(q)) {
            ps.setInt(1, turmaId);
            ps.setInt(2, discId);
            ps.setString(3, cpf);
            try (ResultSet r = ps.executeQuery()) {
                if (r.next()) return r.getDouble("m");
            }
        } catch (SQLException ignore) { }
        return 0.0;
    }

    /* ---------- Carrega turmas + TODAS as disciplinas ----------------- */
    private void carregarTurmasDoBanco() {
        turmas.clear();
        String sql = """
            SELECT t.id              AS turma_id,
                   t.nome            AS turma_nome,
                   t.periodo,
                   d.id              AS disc_id,
                   d.nome            AS disc_nome
              FROM turmas t
              JOIN turma_disciplina td ON td.turma_id = t.id
              JOIN disciplinas      d  ON d.id        = td.disciplina_id
             ORDER BY t.nome, d.nome
        """;

        try (Connection c = Conexao.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Map<Integer, Turma> mapa = new LinkedHashMap<>();

            while (rs.next()) {
                int    turmaId   = rs.getInt("turma_id");
                String nomeTurma = rs.getString("turma_nome");
                String periodo   = rs.getString("periodo");

                /* instancia (ou recupera) a turma */
                Turma turma = mapa.computeIfAbsent(turmaId, id -> {
                    Turma nt = new Turma(id, nomeTurma, periodo);
                    nt.setDisciplinas(new ArrayList<>());
                    return nt;
                });

                /* adiciona disciplina */
                Disciplina d = new Disciplina(
                        rs.getInt("disc_id"),
                        rs.getString("disc_nome"),
                        null);
                turma.getDisciplinas().add(d);
            }
            turmas.addAll(mapa.values());

        } catch (SQLException e) {
            e.printStackTrace();
            alert(Alert.AlertType.ERROR, "Erro",
                  "Falha ao carregar turmas.");
        }
    }

    /* ---------- Utils / Converters ----------------------------------- */
    private void alert(Alert.AlertType t, String title, String msg) {
        Alert a = new Alert(t, msg, ButtonType.OK);
        a.setHeaderText(null); a.setTitle(title); a.showAndWait();
    }

    private static class TurmaStringConverter
            extends javafx.util.StringConverter<Turma> {
        @Override public String toString(Turma t) { return t==null?"" : t.getNome(); }
        @Override public Turma fromString(String s) { return null; }
    }
    private static class DisciplinaStringConverter
            extends javafx.util.StringConverter<Disciplina> {
        @Override public String toString(Disciplina d){ return d==null?"" : d.getNome(); }
        @Override public Disciplina fromString(String s){ return null; }
    }
}
