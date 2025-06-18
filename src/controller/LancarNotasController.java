package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import model.*;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;

public class LancarNotasController {

    @FXML private ComboBox<Turma> turmaCombo;
    @FXML private ComboBox<Disciplina> disciplinaCombo;
    @FXML private Button btnCarregarAlunos;

    @FXML private TableView<NotaAluno> tabelaNotas;
    @FXML private TableColumn<NotaAluno, String> colNome;
    @FXML private TableColumn<NotaAluno, String> colMatricula;
    @FXML private TableColumn<NotaAluno, Double> colNota1, colNota2, colNota3, colMedia;

    @FXML private Button btnVoltar;

    private final ObservableList<Turma> turmasDisponiveis = FXCollections.observableArrayList();
    private final ObservableList<NotaAluno> alunosDaTurma = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarTurmasDoBanco();
        turmaCombo.setItems(turmasDisponiveis);
        turmaCombo.setConverter(new TurmaStringConverter());

        turmaCombo.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            disciplinaCombo.getItems().clear();
            if (n != null) {
                disciplinaCombo.setItems(FXCollections.observableArrayList(n.getDisciplinas()));
                disciplinaCombo.setConverter(new DisciplinaStringConverter());
            }
            tabelaNotas.getItems().clear();
        });

        configurarTabela();
    }

    private void configurarTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));

        setNotaEditable(colNota1, "nota1", NotaAluno::nota1Property, NotaAluno::setNota1);
        setNotaEditable(colNota2, "nota2", NotaAluno::nota2Property, NotaAluno::setNota2);
        setNotaEditable(colNota3, "nota3", NotaAluno::nota3Property, NotaAluno::setNota3);

        colMedia.setCellValueFactory(c -> c.getValue().mediaProperty().asObject());
        formatarColunaMedia();

        tabelaNotas.setItems(alunosDaTurma);
        tabelaNotas.setEditable(true);
    }

    private void setNotaEditable(TableColumn<NotaAluno, Double> col, String campoSQL,
                                 Function<NotaAluno, javafx.beans.property.DoubleProperty> prop,
                                 TriConsumer<NotaAluno, Double> setter) {
        col.setCellValueFactory(c -> prop.apply(c.getValue()).asObject());
        col.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        col.setOnEditCommit(evt -> {
            NotaAluno na = evt.getRowValue();
            double nv = evt.getNewValue() == null ? 0.0 : evt.getNewValue();
            setter.accept(na, nv);
            updateNotaNoBanco(na, campoSQL, nv);
            tabelaNotas.refresh();
        });
    }

    @FXML
    private void handleCarregarAlunos() {
        Turma turma = turmaCombo.getValue();
        Disciplina discSel = disciplinaCombo.getValue();

        if (turma == null || discSel == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção", "Escolha a turma e a disciplina.");
            return;
        }

        alunosDaTurma.clear();

        String sql = """
                SELECT u.cpf,
                       a.matricula,
                       u.nome,
                       COALESCE(n.nota1, 0) AS nota1,
                       COALESCE(n.nota2, 0) AS nota2,
                       COALESCE(n.nota3, 0) AS nota3
                FROM turma_aluno ta
                JOIN alunos a ON a.cpf = ta.aluno_cpf
                JOIN usuarios u ON u.cpf = a.cpf
                LEFT JOIN notas n ON n.aluno_cpf = a.cpf
                                  AND n.turma_id = ta.turma_id
                                  AND n.disciplina_id = ?
                WHERE ta.turma_id = ?
                """;

        try (Connection c = Conexao.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, discSel.getId());
            ps.setInt(2, turma.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NotaAluno na = new NotaAluno(
                            rs.getString("nome"),
                            rs.getString("matricula"),
                            rs.getString("cpf"),
                            turma.getId(),
                            rs.getDouble("nota1"),
                            rs.getDouble("nota2"),
                            rs.getDouble("nota3")
                    );
                    alunosDaTurma.add(na);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao carregar alunos.");
        }
    }

    private void updateNotaNoBanco(NotaAluno na, String campo, double valor) {
        Turma turma = turmaCombo.getValue();
        Disciplina d = disciplinaCombo.getValue();
        if (turma == null || d == null) return;

        String upd = "UPDATE notas SET " + campo + "=? WHERE turma_id=? AND disciplina_id=? AND aluno_cpf=?";
        String ins = """
                INSERT INTO notas (turma_id, disciplina_id, aluno_cpf, nota1, nota2, nota3)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection c = Conexao.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(upd)) {
                ps.setDouble(1, valor);
                ps.setInt(2, turma.getId());
                ps.setInt(3, d.getId());
                ps.setString(4, na.getCpf());

                if (ps.executeUpdate() == 0) {
                    try (PreparedStatement pi = c.prepareStatement(ins)) {
                        pi.setInt(1, turma.getId());
                        pi.setInt(2, d.getId());
                        pi.setString(3, na.getCpf());
                        pi.setDouble(4, na.getNota1());
                        pi.setDouble(5, na.getNota2());
                        pi.setDouble(6, na.getNota3());
                        pi.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível salvar a nota.");
        }
    }

    private void carregarTurmasDoBanco() {
        turmasDisponiveis.clear();

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
                String turmaNome = rs.getString("turma_nome");
                String periodo = rs.getString("periodo");

                Turma t = mapa.computeIfAbsent(turmaId, id -> {
                    Turma nt = new Turma(id, turmaNome, periodo);
                    nt.setDisciplinas(new ArrayList<>());
                    return nt;
                });

                Disciplina d = new Disciplina(
                        rs.getInt("disc_id"),
                        rs.getString("disc_nome"),
                        null
                );
                t.getDisciplinas().add(d);
            }

            turmasDisponiveis.addAll(mapa.values());

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao carregar turmas.");
        }
    }

    private void formatarColunaMedia() {
        DecimalFormat df = new DecimalFormat("#.##");
        colMedia.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : df.format(v));
            }
        });
    }

    @FXML private void salvarNotas(ActionEvent e) {
        showAlert(Alert.AlertType.INFORMATION, "Salvar", "Notas salvas com sucesso!");
    }

    @FXML private void voltar(ActionEvent e) {
        btnVoltar.getScene().getWindow().hide();
    }

    private void showAlert(Alert.AlertType t, String title, String msg) {
        Alert a = new Alert(t);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }

    private static class TurmaStringConverter extends javafx.util.StringConverter<Turma> {
        @Override public String toString(Turma t) { return t == null ? "" : t.getNome(); }
        @Override public Turma fromString(String s) { return null; }
    }

    private static class DisciplinaStringConverter extends javafx.util.StringConverter<Disciplina> {
        @Override public String toString(Disciplina d) { return d == null ? "" : d.getNome(); }
        @Override public Disciplina fromString(String s) { return null; }
    }

    @FunctionalInterface
    private interface TriConsumer<T, U> {
        void accept(T t, U u);
    }
}
