package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GerenciarTurmasController {

    /* ---------- FXML ---------- */
    @FXML private TextField pesquisaField;
    @FXML private TableView<TurmaWrapper> tabelaTurmas;
    @FXML private TableColumn<TurmaWrapper,Boolean> colSelecao;
    @FXML private TableColumn<TurmaWrapper,String>  colNome;
    @FXML private TableColumn<TurmaWrapper,String>  colPeriodo;

    @FXML private TableView<AlunoWrapper> tabelaAlunos;
    @FXML private TableColumn<AlunoWrapper, Boolean> colSelAluno;
    @FXML private TableColumn<AlunoWrapper, String> colNomeAluno;
    @FXML private TableColumn<AlunoWrapper, String> colCpfAluno;

    @FXML private Button btnVoltar;
    
    // 1. Adicione a referência para o novo botão de adicionar aluno
    @FXML private Button btnAdicionarAluno;


    /* ---------- Dados ---------- */
    private final ObservableList<TurmaWrapper> masterTurmaList = FXCollections.observableArrayList();
    private final ObservableList<AlunoWrapper> alunosDaTurma = FXCollections.observableArrayList();

    /* ---------- Inicialização ---------- */
    @FXML
    public void initialize() {
        carregarDadosDoBanco();

        /* checkbox de turmas */
        colSelecao.setCellValueFactory(c -> c.getValue().selecionadoProperty());
        colSelecao.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecao));
        colSelecao.setEditable(true); // <-- CORREÇÃO AQUI

        /* Nome (editável) */
        colNome.setCellValueFactory(c -> c.getValue().getTurma().nomeProperty());
        colNome.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        colNome.setOnEditCommit(evt -> {
            TurmaWrapper tw = evt.getRowValue();
            String novoNome = evt.getNewValue();
            if (novoNome != null && !novoNome.trim().isEmpty()
                    && !novoNome.equals(tw.getTurma().getNome())) {
                if (atualizarCampo("nome", novoNome, tw.getTurma().getId())) {
                    tw.getTurma().setNome(novoNome);
                    tabelaTurmas.refresh();
                }
            }
        });

        /* Período (editável) */
        colPeriodo.setCellValueFactory(c -> c.getValue().getTurma().periodoProperty());
        colPeriodo.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        colPeriodo.setOnEditCommit(evt -> {
            TurmaWrapper tw = evt.getRowValue();
            String novoPeriodo = evt.getNewValue();
            if (novoPeriodo != null && !novoPeriodo.trim().isEmpty()
                    && !novoPeriodo.equals(tw.getTurma().getPeriodo())) {
                if (atualizarCampo("periodo", novoPeriodo, tw.getTurma().getId())) {
                    tw.getTurma().setPeriodo(novoPeriodo);
                    tabelaTurmas.refresh();
                }
            }
        });

        tabelaTurmas.setEditable(true); // Tabela editável

        /* filtro */
        FilteredList<TurmaWrapper> filtered = new FilteredList<>(masterTurmaList, p -> true);
        pesquisaField.textProperty().addListener((obs, o, n) -> {
            String f = n == null ? "" : n.toLowerCase();
            filtered.setPredicate(tw -> tw.getTurma().getNome().toLowerCase().contains(f));
        });
        tabelaTurmas.setItems(filtered);

        /* seleção de turma */
        tabelaTurmas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarAlunosDaTurma(newVal.getTurma().getId());
            } else {
                alunosDaTurma.clear();
            }
        });

        /* colunas de alunos */
        colSelAluno.setCellValueFactory(a -> a.getValue().selecionadoProperty());
        colSelAluno.setCellFactory(CheckBoxTableCell.forTableColumn(colSelAluno));
        colSelAluno.setEditable(true);

        colNomeAluno.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getAluno().getNome()));
        colCpfAluno.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getAluno().getCpf()));
        tabelaAlunos.setItems(alunosDaTurma);
        tabelaAlunos.setEditable(true); // Tabela de alunos também editável
    }

    /* ---------- Banco: SELECT - Turmas ---------- */
    private void carregarDadosDoBanco() {
        masterTurmaList.clear();
        final String sql = "SELECT id, nome, periodo FROM turmas";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Turma t = new Turma();
                t.setId(rs.getInt("id"));
                t.setNome(rs.getString("nome"));
                t.setPeriodo(rs.getString("periodo"));
                masterTurmaList.add(new TurmaWrapper(t));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao carregar turmas:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /* ---------- Banco: SELECT - Alunos da turma ---------- */
    private void carregarAlunosDaTurma(int turmaId) {
        alunosDaTurma.clear();
        final String sql = """
                SELECT u.cpf, u.nome
                FROM turma_aluno ta
                JOIN usuarios u ON ta.aluno_cpf = u.cpf
                WHERE ta.turma_id = ?
                """;
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, turmaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario aluno = new Usuario();
                    aluno.setCpf(rs.getString("cpf"));
                    aluno.setNome(rs.getString("nome"));
                    alunosDaTurma.add(new AlunoWrapper(aluno));
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao carregar alunos:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /* ---------- Banco: UPDATE ---------- */
    private boolean atualizarCampo(String coluna, String valor, int idTurma) {
        final String sql = "UPDATE turmas SET "+coluna+" = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, valor);
            ps.setInt(2, idTurma);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR,"Erro","Falha ao atualizar "+coluna+":\n"+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /* ---------- Banco: DELETE ---------- */
    private void excluirTurmasDoBanco(List<TurmaWrapper> selecionadas) throws SQLException {
        final String delAlunos = "DELETE FROM turma_aluno WHERE turma_id = ?";
        final String delTurma  = "DELETE FROM turmas WHERE id = ?";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pAlunos = conn.prepareStatement(delAlunos);
                 PreparedStatement pTurma  = conn.prepareStatement(delTurma)) {

                for (TurmaWrapper tw : selecionadas) {
                    int id = tw.getTurma().getId();
                    pAlunos.setInt(1,id);
                    pAlunos.addBatch();

                    pTurma.setInt(1,id);
                    pTurma.addBatch();
                }
                pAlunos.executeBatch();
                pTurma.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    /* ---------- Botões ---------- */
    @FXML private void handleNovaTurma() {
        showAlert(Alert.AlertType.INFORMATION,"Ação","Navegando para o cadastro de Turma...");
    }

    @FXML private void handleEditarTurma() {
        List<TurmaWrapper> sel = getSelecionadas();
        if (sel.size()!=1){
            showAlert(Alert.AlertType.WARNING,"Seleção Inválida","Selecione exatamente UMA turma para editar.");
            return;
        }
        showAlert(Alert.AlertType.INFORMATION,"Editar","Abrir editor para: "+sel.get(0).getTurma().getNome());
    }

    @FXML private void handleExcluirTurmas() {
        List<TurmaWrapper> sel = getSelecionadas();
        if (sel.isEmpty()){
            showAlert(Alert.AlertType.WARNING,"Seleção Inválida","Selecione pelo menos uma turma para excluir.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir "+sel.size()+" turma(s)?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmação");
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get()==ButtonType.YES){
            try {
                excluirTurmasDoBanco(sel);
                masterTurmaList.removeAll(sel);
            } catch (SQLException e){
                showAlert(Alert.AlertType.ERROR,"Erro","Não foi possível excluir:\n"+e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    // ==================================================================
    // 2. INÍCIO DAS NOVAS FUNÇÕES ADICIONADAS
    // ==================================================================

    /**
     * NOVA FUNÇÃO: Abre um diálogo para adicionar um aluno à turma selecionada.
     */
    @FXML
    private void handleAdicionarAluno() {
        TurmaWrapper turmaSelecionada = tabelaTurmas.getSelectionModel().getSelectedItem();
        if (turmaSelecionada == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Por favor, selecione uma turma para adicionar um aluno.");
            return;
        }

        List<Usuario> alunosDisponiveis = getAlunosDisponiveis(turmaSelecionada.getTurma().getId());

        if (alunosDisponiveis.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Nenhum Aluno", "Não há novos alunos disponíveis para adicionar a esta turma.");
            return;
        }

        ChoiceDialog<Usuario> dialog = new ChoiceDialog<>(null, alunosDisponiveis);
        dialog.setTitle("Adicionar Aluno");
        dialog.setHeaderText("Selecione o aluno que deseja adicionar à turma: " + turmaSelecionada.getTurma().getNome());
        dialog.setContentText("Aluno:");

        dialog.setConverter(new javafx.util.StringConverter<Usuario>() {
            @Override
            public String toString(Usuario usuario) {
                return usuario == null ? "" : usuario.getNome() + " (CPF: " + usuario.getCpf() + ")";
            }
            @Override
            public Usuario fromString(String string) {
                return null;
            }
        });

        Optional<Usuario> result = dialog.showAndWait();
        result.ifPresent(alunoParaAdicionar -> {
            if (adicionarAlunoNaTurma(turmaSelecionada.getTurma().getId(), alunoParaAdicionar.getCpf())) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Aluno adicionado à turma com sucesso!");
                carregarAlunosDaTurma(turmaSelecionada.getTurma().getId());
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível adicionar o aluno à turma.");
            }
        });
    }

    /**
     * NOVA FUNÇÃO: Busca no banco de dados os alunos que não pertencem a uma turma.
     */
    private List<Usuario> getAlunosDisponiveis(int turmaId) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT cpf, nome FROM usuarios WHERE tipo = 'aluno' AND cpf NOT IN (SELECT aluno_cpf FROM turma_aluno WHERE turma_id = ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, turmaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario aluno = new Usuario();
                aluno.setCpf(rs.getString("cpf"));
                aluno.setNome(rs.getString("nome"));
                lista.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Banco de Dados", "Falha ao buscar alunos disponíveis.");
        }
        return lista;
    }

    /**
     * NOVA FUNÇÃO: Insere a relação aluno-turma no banco de dados.
     */
    private boolean adicionarAlunoNaTurma(int turmaId, String alunoCpf) {
        String sql = "INSERT INTO turma_aluno (turma_id, aluno_cpf) VALUES (?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, turmaId);
            ps.setString(2, alunoCpf);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==================================================================
    // FIM DAS NOVAS FUNÇÕES
    // ==================================================================


    @FXML
    private void handleRemoverAluno() {
        TurmaWrapper turmaSel = tabelaTurmas.getSelectionModel().getSelectedItem();
        List<AlunoWrapper> alunosSelecionados = alunosDaTurma.stream()
                .filter(AlunoWrapper::isSelecionado)
                .toList();

        if (turmaSel == null || alunosSelecionados.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Seleção inválida", "Selecione uma turma e pelo menos um aluno para remover.");
            return;
        }

        final String sql = "DELETE FROM turma_aluno WHERE turma_id = ? AND aluno_cpf = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (AlunoWrapper wrapper : alunosSelecionados) {
                Usuario aluno = wrapper.getAluno();
                ps.setInt(1, turmaSel.getTurma().getId());
                ps.setString(2, aluno.getCpf());
                ps.addBatch();
            }
            ps.executeBatch();

            alunosDaTurma.removeAll(alunosSelecionados);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Alunos removidos da turma.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover alunos:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /* ---------- Util ---------- */
    private List<TurmaWrapper> getSelecionadas(){
        return masterTurmaList.stream().filter(TurmaWrapper::isSelecionado).collect(Collectors.toList());
    }

    @FXML private void voltar(ActionEvent e){ /* navegação */ }

    private void showAlert(Alert.AlertType t,String title,String msg){
        Alert a=new Alert(t); a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}