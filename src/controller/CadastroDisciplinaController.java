package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import model.Conexao;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CadastroDisciplinaController {

    @FXML private TextField nomeDisciplinaField;
    @FXML private TextField pesquisaProfessorField;
    @FXML private TableView<Usuario> tabelaProfessores;
    @FXML private TableColumn<Usuario, Void> colSelecaoProfessor;
    @FXML private TableColumn<Usuario, String> colNomeProfessor;
    @FXML private TableColumn<Usuario, String> colCpfProfessor;
    @FXML private Button btnVoltar;

    private ObservableList<Usuario> listaProfessores = FXCollections.observableArrayList();
    private ToggleGroup professorToggleGroup = new ToggleGroup();

    @FXML
    public void initialize() {
        configurarTabela();
        carregarProfessoresDoBanco();
        configurarFiltro();
    }

    private void configurarTabela() {
        colNomeProfessor.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colCpfProfessor.setCellValueFactory(cellData -> cellData.getValue().cpfProperty());

        colSelecaoProfessor.setCellFactory(param -> new TableCell<>() {
            private final RadioButton radioButton = new RadioButton();

            {
                radioButton.setToggleGroup(professorToggleGroup);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    radioButton.setUserData(getTableView().getItems().get(getIndex()));
                    setGraphic(radioButton);
                }
            }
        });
    }

    private void configurarFiltro() {
        FilteredList<Usuario> filteredData = new FilteredList<>(listaProfessores, p -> true);
        pesquisaProfessorField.textProperty().addListener((obs, oldV, newV) -> {
            filteredData.setPredicate(usuario -> {
                if (newV == null || newV.isEmpty()) return true;
                String filter = newV.toLowerCase();
                return usuario.getNome().toLowerCase().contains(filter) || usuario.getCpf().toLowerCase().contains(filter);
            });
        });
        tabelaProfessores.setItems(filteredData);
    }

    private void carregarProfessoresDoBanco() {
        listaProfessores.clear();
        String sql = "SELECT cpf, nome, tipo FROM usuarios WHERE tipo = 'professor'";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario professor = new Usuario();
                professor.setCpf(rs.getString("cpf"));
                professor.setNome(rs.getString("nome"));
                professor.setTipoUsuario(rs.getString("tipo"));

                listaProfessores.add(professor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao carregar professores do banco.");
        }
    }

    @FXML
    void cadastrarDisciplina() {
        String nome = nomeDisciplinaField.getText().trim();
        Toggle selectedToggle = professorToggleGroup.getSelectedToggle();

        if (nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campo Vazio", "Por favor, informe o nome da disciplina.");
            return;
        }

        if (selectedToggle == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção Necessária", "Por favor, selecione um professor para a disciplina.");
            return;
        }

        Usuario professorSelecionado = (Usuario) selectedToggle.getUserData();

        boolean sucesso = salvarDisciplinaNoBanco(nome, professorSelecionado);

        if (sucesso) {
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Disciplina cadastrada e associada ao professor com sucesso!");
            limparCampos();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao cadastrar a disciplina.");
        }
    }

    private boolean salvarDisciplinaNoBanco(String nomeDisciplina, Usuario professor) {
        String sql = "INSERT INTO disciplinas (nome, professor_cpf) VALUES (?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Falha na conexão com o banco de dados.");
                return false;
            }

            conn.setAutoCommit(false);

            ps.setString(1, nomeDisciplina);
            ps.setString(2, professor.getCpf());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void limparCampos() {
        nomeDisciplinaField.clear();
        pesquisaProfessorField.clear();
        if (professorToggleGroup.getSelectedToggle() != null) {
            professorToggleGroup.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    private void voltar(ActionEvent event) {
        MenuPrincipalController menuController = buscarMenuController();
        if (menuController != null) {
            menuController.restaurarEstado();
            menuController.limparConteudo();
        }
    }

    private MenuPrincipalController buscarMenuController() {
        Parent root = btnVoltar.getScene().getRoot();
        if (root instanceof BorderPane) {
            return (MenuPrincipalController) ((BorderPane) root).getUserData();
        }
        return null;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
