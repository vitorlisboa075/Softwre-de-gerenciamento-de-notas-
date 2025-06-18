package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class RegistrarPresencaController {

    /* ---------- FXML ---------- */
    @FXML private ComboBox<Turma> turmaCombo;
    @FXML private ComboBox<Disciplina> disciplinaCombo;
    @FXML private TableView<AlunoPresenca> tabelaAlunos;
    @FXML private TableColumn<AlunoPresenca, String> colNome;
    @FXML private TableColumn<AlunoPresenca, String> colMatricula;
    @FXML private TableColumn<AlunoPresenca, Boolean> colPresenca;
    @FXML private Button btnVoltar;

    /* ---------- Dados ---------- */
    private final ObservableList<AlunoPresenca> listaAlunos = FXCollections.observableArrayList();
    private final ObservableList<Turma> turmas = FXCollections.observableArrayList();

    /* ---------- INIT ---------- */
    @FXML
    public void initialize() {
        carregarTurmasDoBanco();

        turmaCombo.setItems(turmas);
        turmaCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Turma t){ return t == null ? "" : t.getNome(); }
            @Override public Turma fromString(String s){ return null; }
        });
        turmaCombo.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            disciplinaCombo.getItems().clear();
            listaAlunos.clear();
            if (nv != null) {
                carregarDisciplinas(nv);
                carregarAlunosDaTurma(nv);
            }
        });

        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colPresenca.setCellValueFactory(data -> data.getValue().presenteProperty());
        colPresenca.setCellFactory(CheckBoxTableCell.forTableColumn(colPresenca));

        tabelaAlunos.setItems(listaAlunos);
        tabelaAlunos.setEditable(true);
    }

    /* ---------- Carregar disciplinas da turma ---------- */
    private void carregarDisciplinas(Turma turma) {
        disciplinaCombo.setItems(FXCollections.observableArrayList(turma.getDisciplinas()));
        disciplinaCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Disciplina d){ return d == null ? "" : d.getNome(); }
            @Override public Disciplina fromString(String s){ return null; }
        });
    }

    /* ---------- Carregar alunos da turma ---------- */
    private void carregarAlunosDaTurma(Turma turma) {
        listaAlunos.clear();
        String sql = """
            SELECT u.nome, a.matricula, a.cpf
              FROM turma_aluno ta
              JOIN alunos  a ON a.cpf = ta.aluno_cpf
              JOIN usuarios u ON u.cpf = a.cpf
             WHERE ta.turma_id = ?
        """;
        try (Connection c = Conexao.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, turma.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaAlunos.add(new AlunoPresenca(
                            rs.getString("nome"),
                            rs.getString("matricula"),
                            rs.getString("cpf")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao listar alunos.");
        }
    }

    /* ---------- Salvar presença ---------- */
    @FXML
    private void salvarPresenca() {
        Turma turma = turmaCombo.getValue();
        if (turma == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção", "Escolha uma turma.");
            return;
        }

        String sql = """
            INSERT INTO presencas (turma_id, aluno_cpf, data, presente)
            VALUES (?,?,?,?)
            ON DUPLICATE KEY UPDATE presente = VALUES(presente)
        """;
        try (Connection con = Conexao.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                java.sql.Date hoje = java.sql.Date.valueOf(LocalDate.now());
                for (AlunoPresenca ap : listaAlunos) {
                    ps.setInt(1, turma.getId());
                    ps.setString(2, ap.getCpf());
                    ps.setDate(3, hoje);
                    ps.setBoolean(4, ap.isPresente());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            con.commit();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Presenças registradas.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao salvar presenças.");
        }
    }

    /* ---------- Carregar turmas e todas as disciplinas associadas ---------- */
    private void carregarTurmasDoBanco() {
        turmas.clear();
        String sql = """
            SELECT t.id AS turma_id,
                   t.nome AS turma_nome,
                   t.periodo,
                   d.id AS disc_id,
                   d.nome AS disc_nome
              FROM turmas t
              JOIN turma_disciplina td ON td.turma_id = t.id
              JOIN disciplinas d ON d.id = td.disciplina_id
             ORDER BY t.nome, d.nome
        """;
        try (Connection c = Conexao.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Map<Integer, Turma> mapa = new LinkedHashMap<>();

            while (rs.next()) {
                int turmaId = rs.getInt("turma_id");
                String nomeTurma = rs.getString("turma_nome");
                String periodo = rs.getString("periodo");

                Turma turma = mapa.computeIfAbsent(turmaId, id -> {
                    Turma nt = new Turma(id, nomeTurma, periodo);
                    nt.setDisciplinas(new ArrayList<>());
                    return nt;
                });

                Disciplina d = new Disciplina(
                        rs.getInt("disc_id"),
                        rs.getString("disc_nome"),
                        null);
                turma.getDisciplinas().add(d);
            }
            turmas.addAll(mapa.values());

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao carregar turmas.");
        }
    }

    /* ---------- Utils ---------- */
    @FXML private void limparCampos() {
        turmaCombo.getSelectionModel().clearSelection();
        disciplinaCombo.getSelectionModel().clearSelection();
        listaAlunos.clear();
    }

    @FXML private void voltar(ActionEvent e) {
        btnVoltar.getScene().getWindow().hide();
    }

    private void showAlert(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
