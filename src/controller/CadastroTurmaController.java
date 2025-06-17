package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import model.*;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

public class CadastroTurmaController {

    @FXML private TextField nomeTurmaField;
    @FXML private ComboBox<String> periodoCombo;

    @FXML private TextField pesquisaDisciplinaField;
    @FXML private TableView<DisciplinaWrapper> tabelaDisciplinas;
    @FXML private TableColumn<DisciplinaWrapper, Boolean> colSelecaoDisciplina;
    @FXML private TableColumn<DisciplinaWrapper, String> colNomeDisciplina;

    @FXML private TextField pesquisaAlunoField;
    @FXML private TableView<UsuarioWrapper> tabelaAlunos;
    @FXML private TableColumn<UsuarioWrapper, Boolean> colSelecaoAluno;
    @FXML private TableColumn<UsuarioWrapper, String> colNomeAluno;
    @FXML private TableColumn<UsuarioWrapper, String> colCpfAluno;

    @FXML private Button btnVoltar;

    private final ObservableList<DisciplinaWrapper> masterDisciplinaList = FXCollections.observableArrayList();
    private final ObservableList<UsuarioWrapper>     masterUsuarioList   = FXCollections.observableArrayList();

    /* ---------- Inicialização ---------- */
    @FXML
    public void initialize() {
        periodoCombo.getItems().addAll("Matutino", "Vespertino", "Noturno", "Integral");

        carregarDisciplinasDoBanco();
        carregarAlunosDoBanco();

        configurarTabelaDisciplinas();
        configurarTabelaAlunos();
    }

    /* ---------- Configuração Tabela Disciplinas ---------- */
    private void configurarTabelaDisciplinas() {
        colSelecaoDisciplina.setCellValueFactory(cd -> cd.getValue().selecionadoProperty());
        colSelecaoDisciplina.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecaoDisciplina));
        colSelecaoDisciplina.setEditable(true);                       // <-- habilita clique

        colNomeDisciplina.setCellValueFactory(cd -> cd.getValue().getDisciplina().nomeProperty());

        tabelaDisciplinas.setEditable(true);

        FilteredList<DisciplinaWrapper> filtrada = new FilteredList<>(masterDisciplinaList, p -> true);
        pesquisaDisciplinaField.textProperty().addListener((obs, o, n) -> {
            String f = n == null ? "" : n.toLowerCase();
            filtrada.setPredicate(dw -> dw.getDisciplina().getNome().toLowerCase().contains(f));
        });
        tabelaDisciplinas.setItems(filtrada);
    }

    /* ---------- Configuração Tabela Alunos ---------- */
    private void configurarTabelaAlunos() {
        // Só alunos
        FilteredList<UsuarioWrapper> apenasAlunos = new FilteredList<>(masterUsuarioList,
                uw -> "Aluno".equalsIgnoreCase(uw.getUsuario().getTipoUsuario()));

        colSelecaoAluno.setCellValueFactory(cd -> cd.getValue().selecionadoProperty());
        colSelecaoAluno.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecaoAluno));
        colSelecaoAluno.setEditable(true);                            // <-- habilita clique

        colNomeAluno.setCellValueFactory(cd -> cd.getValue().getUsuario().nomeProperty());
        colCpfAluno.setCellValueFactory(cd -> cd.getValue().getUsuario().cpfProperty());

        tabelaAlunos.setEditable(true);

        FilteredList<UsuarioWrapper> filtrada = new FilteredList<>(apenasAlunos, p -> true);
        pesquisaAlunoField.textProperty().addListener((obs, o, n) -> {
            String f = n == null ? "" : n.toLowerCase();
            filtrada.setPredicate(uw ->
                uw.getUsuario().getNome().toLowerCase().contains(f) ||
                uw.getUsuario().getCpf().toLowerCase().contains(f)
            );
        });
        tabelaAlunos.setItems(filtrada);
    }

    /* ---------- Salvar Turmas ---------- */
    @FXML
    void salvarTurma() {
        String nome    = nomeTurmaField.getText();
        String periodo = periodoCombo.getValue();

        if (nome == null || nome.trim().isEmpty() || periodo == null) {
            showAlert(Alert.AlertType.WARNING, "Campos Incompletos",
                      "Preencha o nome e o período da turma.");
            return;
        }

        List<Disciplina> disciplinasSelecionadas = masterDisciplinaList.stream()
                .filter(DisciplinaWrapper::isSelecionado)
                .map(DisciplinaWrapper::getDisciplina)
                .collect(Collectors.toList());

        List<Usuario> alunosSelecionados = masterUsuarioList.stream()
                .filter(uw -> "Aluno".equalsIgnoreCase(uw.getUsuario().getTipoUsuario()) && uw.isSelecionado())
                .map(UsuarioWrapper::getUsuario)
                .collect(Collectors.toList());

        if (disciplinasSelecionadas.isEmpty() || alunosSelecionados.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Seleção Necessária",
                      "Selecione ao menos uma disciplina e um aluno.");
            return;
        }

        boolean sucesso = true;
        // Como cada turma só comporta uma disciplina, cria-se uma turma para cada disciplina selecionada
        for (Disciplina disc : disciplinasSelecionadas) {
            if (!salvarUmaTurma(nome, periodo, disc, alunosSelecionados)) {
                sucesso = false;
                break;
            }
        }

        if (sucesso) {
            showAlert(Alert.AlertType.INFORMATION, "Sucesso",
                      "Turmas cadastradas com sucesso.");
            limparCampos();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro",
                      "Falha ao cadastrar uma ou mais turmas.");
        }
    }

    private boolean salvarUmaTurma(String nome, String periodo,
                                   Disciplina disciplina, List<Usuario> alunos) {

        Connection conn = null;
        PreparedStatement psTurma = null;
        PreparedStatement psTurmaAluno = null;

        try {
            conn = Conexao.getConnection();
            if (conn == null) {
                showAlert(Alert.AlertType.ERROR, "Erro",
                          "Falha na conexão com o banco de dados.");
                return false;
            }
            conn.setAutoCommit(false);

            // Inserir turma
            String insertTurma = "INSERT INTO turmas (nome, disciplina_id, professor_cpf, periodo) "
                               + "VALUES (?,?,?,?)";
            psTurma = conn.prepareStatement(insertTurma, PreparedStatement.RETURN_GENERATED_KEYS);
            psTurma.setString(1, nome);
            psTurma.setInt(2, disciplina.getId());
            psTurma.setString(3, disciplina.getProfessor().getCpf());
            psTurma.setString(4, periodo);

            if (psTurma.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }

            int turmaId;
            try (ResultSet keys = psTurma.getGeneratedKeys()) {
                if (keys.next()) {
                    turmaId = keys.getInt(1);
                } else {
                    conn.rollback();
                    return false;
                }
            }

            // Inserir alunos na turma
            String insertTA = "INSERT INTO turma_aluno (turma_id, aluno_cpf) VALUES (?,?)";
            psTurmaAluno = conn.prepareStatement(insertTA);

            for (Usuario aluno : alunos) {
                psTurmaAluno.setInt(1, turmaId);
                psTurmaAluno.setString(2, aluno.getCpf());
                psTurmaAluno.addBatch();
            }
            psTurmaAluno.executeBatch();

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            return false;

        } finally {
            try {
                if (psTurmaAluno != null) psTurmaAluno.close();
                if (psTurma     != null) psTurma.close();
                if (conn        != null) {
                    conn.setAutoCommit(true);
                    Conexao.fecharConnection(conn);
                }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /* ---------- Utilidades ---------- */
    @FXML
    void limparCampos() {
        nomeTurmaField.clear();
        periodoCombo.getSelectionModel().clearSelection();
        pesquisaDisciplinaField.clear();
        pesquisaAlunoField.clear();
        masterDisciplinaList.forEach(dw -> dw.selecionadoProperty().set(false));
        masterUsuarioList.forEach(uw -> uw.selecionadoProperty().set(false));
    }

    private void carregarAlunosDoBanco() {
        masterUsuarioList.clear();
        String sql = "SELECT cpf, nome, tipo FROM usuarios WHERE tipo = 'Aluno'";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario aluno = new Usuario();
                aluno.setCpf(rs.getString("cpf"));
                aluno.setNome(rs.getString("nome"));
                aluno.setTipoUsuario(rs.getString("tipo"));
                masterUsuarioList.add(new UsuarioWrapper(aluno));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro",
                      "Falha ao carregar alunos do banco.");
        }
    }

    private void carregarDisciplinasDoBanco() {
        masterDisciplinaList.clear();
        String sql = """
                SELECT d.id, d.nome,
                       p.cpf   AS professor_cpf,
                       p.nome  AS professor_nome
                  FROM disciplinas d
                       JOIN usuarios p ON d.professor_cpf = p.cpf
                """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario prof = new Usuario();
                prof.setCpf(rs.getString("professor_cpf"));
                prof.setNome(rs.getString("professor_nome"));
                prof.setTipoUsuario("Professor");

                Disciplina disc = new Disciplina();
                disc.setId(rs.getInt("id"));
                disc.setNome(rs.getString("nome"));
                disc.setProfessor(prof);

                masterDisciplinaList.add(new DisciplinaWrapper(disc));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro",
                      "Falha ao carregar disciplinas do banco.");
        }
    }

    @FXML
    private void voltar(ActionEvent e) {
        btnVoltar.getScene().getWindow().hide();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
