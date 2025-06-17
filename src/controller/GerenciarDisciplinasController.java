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
import model.Disciplina;
import model.DisciplinaWrapper;
import model.Usuario;
import model.Conexao;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller da tela "Gerenciamento de Disciplinas" – agora com edição inline
 * que persiste imediatamente no banco de dados.
 */
public class GerenciarDisciplinasController {

    /* -------------------- FXML -------------------- */
    @FXML private TextField pesquisaField;
    @FXML private TableView<DisciplinaWrapper> tabelaDisciplinas;
    @FXML private TableColumn<DisciplinaWrapper, Boolean> colSelecao;
    @FXML private TableColumn<DisciplinaWrapper, String> colNome;
    @FXML private TableColumn<DisciplinaWrapper, String> colProfessor;
    @FXML private Button btnVoltar;

    /* -------------------- Dados -------------------- */
    private final ObservableList<DisciplinaWrapper> masterDisciplinaList = FXCollections.observableArrayList();

    /* =================== Inicialização =================== */
    @FXML
    public void initialize() {
        carregarDadosDoBanco();

        /* ------ Coluna seleção ------ */
        colSelecao.setCellValueFactory(c -> c.getValue().selecionadoProperty());
        colSelecao.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecao));

        /* ------ Coluna Nome (EDITÁVEL) ------ */
        colNome.setCellValueFactory(c -> c.getValue().getDisciplina().nomeProperty());
        colNome.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        colNome.setOnEditCommit(evt -> {
            DisciplinaWrapper dw = evt.getRowValue();
            String novoNome = evt.getNewValue();
            if (novoNome != null && !novoNome.trim().isEmpty() && !novoNome.equals(dw.getDisciplina().getNome())) {
                if (atualizarNomeDisciplinaNoBanco(dw.getDisciplina().getId(), novoNome)) {
                    dw.getDisciplina().setNome(novoNome);
                    tabelaDisciplinas.refresh();
                }
            }
        });
        tabelaDisciplinas.setEditable(true);

        /* ------ Coluna Professor (somente leitura) ------ */
        colProfessor.setCellValueFactory(c -> {
            Usuario p = c.getValue().getDisciplina().getProfessor();
            return new SimpleStringProperty(p != null ? p.getNome() : "Não definido");
        });

        /* ------ Filtro de pesquisa ------ */
        FilteredList<DisciplinaWrapper> filtered = new FilteredList<>(masterDisciplinaList, p -> true);
        pesquisaField.textProperty().addListener((obs, oldV, newV) -> {
            String f = newV == null ? "" : newV.toLowerCase();
            filtered.setPredicate(dw -> dw.getDisciplina().getNome().toLowerCase().contains(f));
        });
        tabelaDisciplinas.setItems(filtered);
    }

    /* =================== CRUD no Banco =================== */
    private void carregarDadosDoBanco() {
        masterDisciplinaList.clear();
        final String sql = "SELECT d.id, d.nome, p.cpf AS professor_cpf, p.nome AS professor_nome " +
                           "FROM disciplinas d LEFT JOIN usuarios p ON p.cpf = d.professor_cpf";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpfProf = rs.getString("professor_cpf");
                Usuario prof = null;
                if (cpfProf != null) {
                    prof = new Usuario();
                    prof.setCpf(cpfProf);
                    prof.setNome(rs.getString("professor_nome"));
                }
                masterDisciplinaList.add(new DisciplinaWrapper(new Disciplina(id, nome, prof)));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao carregar disciplinas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Atualiza o nome da disciplina no banco. */
    private boolean atualizarNomeDisciplinaNoBanco(int id, String novoNome) {
        final String sql = "UPDATE disciplinas SET nome = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, novoNome);
            ps.setInt(2, id);
            int linhas = ps.executeUpdate();
            return linhas > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao atualizar nome: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void excluirDisciplinasDoBanco(List<DisciplinaWrapper> sel) {
        final String sql = "DELETE FROM disciplinas WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (DisciplinaWrapper dw : sel) {
                ps.setInt(1, dw.getDisciplina().getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro ao excluir", "Não foi possível excluir disciplina(s): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* =================== Ações de Botão =================== */
    @FXML private void handleNovaDisciplina() {
        showAlert(Alert.AlertType.INFORMATION, "Ação", "Navegando para a tela de Cadastro de Disciplina...");
    }

    @FXML private void handleEditarDisciplina() {
        List<DisciplinaWrapper> sel = getSelecionadas();
        if (sel.size() != 1) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Selecione exatamente UMA disciplina para editar.");
            return;
        }
        // Aqui você pode abrir um formulário de edição completo
        showAlert(Alert.AlertType.INFORMATION, "Editar", "Abrir editor para: " + sel.get(0).getDisciplina().getNome());
    }

    @FXML private void handleExcluirDisciplinas() {
        List<DisciplinaWrapper> sel = getSelecionadas();
        if (sel.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Selecione pelo menos uma disciplina para excluir.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Excluir " + sel.size() + " disciplina(s)?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmação");
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.YES) {
            excluirDisciplinasDoBanco(sel);
            masterDisciplinaList.removeAll(sel);
        }
    }

    /* =================== Utilitários =================== */
    private List<DisciplinaWrapper> getSelecionadas() {
        return masterDisciplinaList.stream().filter(DisciplinaWrapper::isSelecionado).collect(Collectors.toList());
    }

    @FXML private void voltar(ActionEvent evt) { /* implemente a navegação */ }

    private void showAlert(Alert.AlertType t, String title, String msg) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
