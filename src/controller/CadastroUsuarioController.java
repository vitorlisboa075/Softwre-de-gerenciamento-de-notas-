package controller;

import model.Conexao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;

import java.sql.*;

public class CadastroUsuarioController {

    // --- Campos de Dados Pessoais ---
    @FXML private ComboBox<String> tipoUsuarioCombo;
    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField emailField;
    @FXML private TextField telefoneField;

    // --- Campos de Endereço ---
    @FXML private TextField logradouroField;
    @FXML private TextField numeroField;
    @FXML private TextField complementoField;
    @FXML private TextField bairroField;
    @FXML private TextField cidadeField;
    @FXML private TextField estadoField;
    @FXML private TextField cepField;

    @FXML private Button btnVoltar;

    @FXML
    public void initialize() {
        tipoUsuarioCombo.getItems().addAll("Aluno", "Professor", "Secretaria");
    }

    @FXML
    private void cadastrarUsuario() {
        String tipo = tipoUsuarioCombo.getValue();
        if (tipo == null) {
            showAlert(Alert.AlertType.ERROR, "Erro no Cadastro", "Por favor, selecione um tipo de usuário.");
            return;
        }

        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String email = emailField.getText();

        if (nome.trim().isEmpty() || cpf.trim().isEmpty() || email.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro no Cadastro", "Os campos Nome, CPF e Email são obrigatórios.");
            return;
        }

        // Verifica se o e-mail já está cadastrado
        if (emailJaExiste(email)) {
            showAlert(Alert.AlertType.ERROR, "Erro no Cadastro", "Este e-mail já está cadastrado.");
            return;
        }

        String telefone = telefoneField.getText();
        String senha = cpf; // Senha inicial = CPF
        String rua = logradouroField.getText();
        String numero = this.numeroField.getText();
        String complemento = complementoField.getText();
        String bairro = bairroField.getText();
        String cidade = cidadeField.getText();
        String estado = estadoField.getText();
        String cep = cepField.getText();

        boolean sucesso = cadastrarUsuarioNoBanco(
            cpf, nome, email, senha, telefone,
            rua, numero, complemento, bairro, cidade, estado, cep,
            tipo.toLowerCase()
        );

        if (sucesso && tipo.equalsIgnoreCase("Aluno")) {
            String matricula = recuperarMatriculaComTentativas(cpf, 20, 100);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso",
                      "Aluno cadastrado!\nMatrícula: " + matricula + "\nSenha inicial: " + cpf);
        } else if (sucesso) {
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", tipo + " cadastrado com sucesso!\nSenha inicial: " + cpf);
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao cadastrar " + tipo + ".");
        }

        limparCampos();
    }

    private boolean cadastrarUsuarioNoBanco(
        String cpf, String nome, String email, String senha, String telefone,
        String rua, String numero, String complemento, String bairro,
        String cidade, String estado, String cep, String tipo
    ) {
        Connection conn = Conexao.getConnection();

        if (conn == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha na conexão com o banco de dados.");
            return false;
        }

        try {
            conn.setAutoCommit(false);

            String sqlUsuario = "INSERT INTO usuarios (cpf, nome, email, senha, tipo, telefone, rua, numero, complemento, bairro, cidade, estado, cep) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario);
            stmtUsuario.setString(1, cpf);
            stmtUsuario.setString(2, nome);
            stmtUsuario.setString(3, email);
            stmtUsuario.setString(4, senha);
            stmtUsuario.setString(5, tipo);
            stmtUsuario.setString(6, telefone);
            stmtUsuario.setString(7, rua);
            stmtUsuario.setString(8, numero);
            stmtUsuario.setString(9, complemento);
            stmtUsuario.setString(10, bairro);
            stmtUsuario.setString(11, cidade);
            stmtUsuario.setString(12, estado);
            stmtUsuario.setString(13, cep);

            stmtUsuario.executeUpdate();

            if (tipo.equals("aluno")) {
                String sqlAluno = "INSERT INTO alunos (cpf) VALUES (?)";
                PreparedStatement stmtAluno = conn.prepareStatement(sqlAluno);
                stmtAluno.setString(1, cpf);
                stmtAluno.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Trata erro de chave duplicada no e-mail
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("email")) {
                showAlert(Alert.AlertType.ERROR, "Erro", "E-mail já cadastrado.");
            } else {
                e.printStackTrace();
            }

            return false;

        } finally {
            Conexao.fecharConnection(conn);
        }
    }

    private boolean emailJaExiste(String email) {
        Connection conn = Conexao.getConnection();
        if (conn == null) return false;

        String sql = "SELECT email FROM usuarios WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexao.fecharConnection(conn);
        }
        return false;
    }

    private String recuperarMatriculaComTentativas(String cpf, int tentativas, int intervaloMs) {
        String matricula = "N/A";

        for (int i = 0; i < tentativas; i++) {
            matricula = recuperarMatricula(cpf);
            if (!matricula.equals("N/A") && matricula != null) {
                return matricula;
            }
            try {
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return matricula;
    }

    private String recuperarMatricula(String cpf) {
        String matricula = "N/A";
        Connection conn = Conexao.getConnection();

        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT matricula FROM alunos WHERE cpf = ?");
                stmt.setString(1, cpf);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    matricula = rs.getString("matricula");
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                Conexao.fecharConnection(conn);
            }
        }
        return matricula;
    }

    @FXML
    private void limparCampos() {
        tipoUsuarioCombo.getSelectionModel().clearSelection();
        nomeField.clear();
        cpfField.clear();
        emailField.clear();
        telefoneField.clear();
        logradouroField.clear();
        numeroField.clear();
        complementoField.clear();
        bairroField.clear();
        cidadeField.clear();
        estadoField.clear();
        cepField.clear();
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
            BorderPane borderPane = (BorderPane) root;
            Object controller = borderPane.getUserData();
            if (controller instanceof MenuPrincipalController) {
                return (MenuPrincipalController) controller;
            }
        }
        return null;
    }

    private void showAlert(Alert.AlertType tipo, String titulo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
