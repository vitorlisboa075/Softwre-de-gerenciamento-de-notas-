package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import model.Usuario;
import model.Conexao;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GerenciarAlunoController implements Initializable {

    @FXML private TableView<Usuario> tabelaAlunos;
    @FXML private TableColumn<Usuario, Boolean> colSelecao;
    @FXML private TableColumn<Usuario, String> colNome;
    @FXML private TableColumn<Usuario, String> colMatricula;
    @FXML private TableColumn<Usuario, String> colCpf;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colTelefone;
    @FXML private TableColumn<Usuario, String> colBairro;
    @FXML private TableColumn<Usuario, String> colSenha;
    @FXML private TableColumn<Usuario, String> colRua;
    @FXML private TableColumn<Usuario, String> colNumero;
    @FXML private TableColumn<Usuario, String> colCidade;
    @FXML private TableColumn<Usuario, String> colEstado;
    @FXML private TableColumn<Usuario, String> colCep;
    @FXML private TableColumn<Usuario, String> colComplemento;
    @FXML private TextField pesquisaCpfField;

    private final ObservableList<Usuario> listaAlunos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /* --------------------------- Configuração das colunas --------------------------- */
        configurarColunasEditaveis();

        /* --------------------------- Dados iniciais --------------------------- */
        tabelaAlunos.setItems(listaAlunos);
        tabelaAlunos.setEditable(true);

        carregarAlunosDoBanco();

        /* --------------------------- Filtro por CPF --------------------------- */
        pesquisaCpfField.textProperty().addListener((obs, oldVal, newVal) -> filtrarPorCpf(newVal));
    }

    /* =================================================================================================================
     *  CONFIGURAÇÃO DA TABLEVIEW
     * ===============================================================================================================*/

    private void configurarColunasEditaveis() {
        // Nome
        configurarColunaEditavel(colNome, Usuario::nomeProperty, Usuario::setNome);
        // Matrícula
        configurarColunaEditavel(colMatricula, Usuario::matriculaProperty, Usuario::setMatricula);
        // CPF (não editável)
        colCpf.setCellValueFactory(c -> c.getValue().cpfProperty());
        // Email
        configurarColunaEditavel(colEmail, Usuario::emailProperty, Usuario::setEmail);
        // Telefone
        configurarColunaEditavel(colTelefone, Usuario::telefoneProperty, Usuario::setTelefone);
        // Bairro
        configurarColunaEditavel(colBairro, Usuario::bairroProperty, Usuario::setBairro);
        // Senha
        configurarColunaEditavel(colSenha, Usuario::senhaProperty, Usuario::setSenha);
        // Rua
        configurarColunaEditavel(colRua, Usuario::logradouroProperty, Usuario::setLogradouro);
        // Número
        configurarColunaEditavel(colNumero, Usuario::numeroProperty, Usuario::setNumero);
        // Cidade
        configurarColunaEditavel(colCidade, Usuario::cidadeProperty, Usuario::setCidade);
        // Complemento
        configurarColunaEditavel(colComplemento, Usuario::complementoProperty, Usuario::setComplemento);
        // Estado
        configurarColunaEditavel(colEstado, Usuario::estadoProperty, Usuario::setEstado);
        // CEP
        configurarColunaEditavel(colCep, Usuario::cepProperty, Usuario::setCep);
        // Checkbox de seleção
        colSelecao.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        colSelecao.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecao));
    }

    private void configurarColunaEditavel(
    TableColumn<Usuario, String> coluna,
    javafx.util.Callback<Usuario, javafx.beans.property.StringProperty> propGetter,
    java.util.function.BiConsumer<Usuario, String> setter
) {
    coluna.setCellValueFactory(cellData -> propGetter.call(cellData.getValue()));
    coluna.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
    coluna.setOnEditCommit(event -> {
        Usuario aluno = event.getRowValue();
        setter.accept(aluno, event.getNewValue());
        salvarAlteracoes(aluno);
    });
}


    /* =================================================================================================================
     *  CARREGAR / FILTRAR DADOS
     * ===============================================================================================================*/

    private void carregarAlunosDoBanco() {
        listaAlunos.clear();
        final String sql = "SELECT u.nome, a.matricula, u.cpf, u.email, u.telefone, u.senha, u.tipo AS tipo_usuario, " +
                "u.rua AS logradouro, u.numero, u.complemento, u.bairro, u.cidade, u.estado, u.cep " +
                "FROM usuarios u JOIN alunos a ON u.cpf = a.cpf WHERE u.tipo = 'aluno'";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("nome"));
                usuario.setMatricula(rs.getString("matricula"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setTelefone(rs.getString("telefone"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setTipoUsuario(rs.getString("tipo_usuario"));
                usuario.setLogradouro(rs.getString("logradouro"));
                usuario.setNumero(rs.getString("numero"));
                usuario.setComplemento(rs.getString("complemento"));
                usuario.setBairro(rs.getString("bairro"));
                usuario.setCidade(rs.getString("cidade"));
                usuario.setEstado(rs.getString("estado"));
                usuario.setCep(rs.getString("cep"));
                listaAlunos.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filtrarPorCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            tabelaAlunos.setItems(listaAlunos);
            return;
        }
        ObservableList<Usuario> filtrados = FXCollections.observableArrayList();
        for (Usuario u : listaAlunos) {
            if (u.getCpf() != null && u.getCpf().contains(cpf)) {
                filtrados.add(u);
            }
        }
        tabelaAlunos.setItems(filtrados);
    }

    /* =================================================================================================================
     *  ATUALIZAÇÃO NO BANCO
     * ===============================================================================================================*/

    private void salvarAlteracoes(Usuario aluno) {
        try {
            atualizarAlunoNoBanco(aluno);
            carregarAlunosDoBanco();                 // Recarrega a lista principal
            filtrarPorCpf(pesquisaCpfField.getText()); // Reaplica o filtro (se houver)
            tabelaAlunos.refresh();                   // Força refresh visual
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao salvar");
            alert.setContentText("Não foi possível salvar as alterações.\n" + e.getMessage());
            alert.showAndWait();
        }
    }

    private void atualizarAlunoNoBanco(Usuario aluno) throws SQLException {
        final String sqlUsuarios = "UPDATE usuarios SET nome = ?, email = ?, telefone = ?, senha = ?, rua = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, estado = ?, cep = ? WHERE cpf = ?";
        final String sqlAlunos   = "UPDATE alunos SET matricula = ? WHERE cpf = ?";
        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtUsuarios = conn.prepareStatement(sqlUsuarios);
                 PreparedStatement stmtAlunos   = conn.prepareStatement(sqlAlunos)) {

                System.out.println("===> INICIANDO UPDATE");
                System.out.println("CPF: [" + aluno.getCpf() + "]");

                stmtUsuarios.setString(1,  aluno.getNome());
                stmtUsuarios.setString(2,  aluno.getEmail());
                stmtUsuarios.setString(3,  aluno.getTelefone());
                stmtUsuarios.setString(4,  aluno.getSenha());
                stmtUsuarios.setString(5,  aluno.getLogradouro());
                stmtUsuarios.setString(6,  aluno.getNumero());
                stmtUsuarios.setString(7,  aluno.getComplemento());
                stmtUsuarios.setString(8,  aluno.getBairro());
                stmtUsuarios.setString(9,  aluno.getCidade());
                stmtUsuarios.setString(10, aluno.getEstado());
                stmtUsuarios.setString(11, aluno.getCep());
                stmtUsuarios.setString(12, aluno.getCpf());

                int linhasUsuarios = stmtUsuarios.executeUpdate();
                System.out.println("UPDATE usuarios afetou: " + linhasUsuarios);

                stmtAlunos.setString(1, aluno.getMatricula());
                stmtAlunos.setString(2, aluno.getCpf());

                int linhasAlunos = stmtAlunos.executeUpdate();
                System.out.println("UPDATE alunos afetou: " + linhasAlunos);

                conn.commit();
                System.out.println("Commit realizado.\n");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    /* =================================================================================================================
     *  AÇÕES DA INTERFACE
     * ===============================================================================================================*/

    @FXML private void handleNovoAluno() {
        System.out.println("Novo aluno clicado");
    }

    @FXML private void handleEditarAluno() {
        tabelaAlunos.edit(-1, null); // Encerra edição em andamento
        Usuario selecionado = tabelaAlunos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            new Alert(Alert.AlertType.WARNING, "Por favor, selecione um aluno para editar.").showAndWait();
            return;
        }
        salvarAlteracoes(selecionado);
        new Alert(Alert.AlertType.INFORMATION, "Aluno atualizado com sucesso!").showAndWait();
    }

    @FXML private void handleExcluirSelecionados() {
        ObservableList<Usuario> selecionados = listaAlunos.filtered(Usuario::isSelected);
        if (selecionados.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Por favor, selecione pelo menos um aluno para excluir.").showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja excluir os alunos selecionados?");
        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                excluirAlunos(selecionados);
            }
        });
    }

    private void excluirAlunos(ObservableList<Usuario> selecionados) {
        try (Connection conn = Conexao.getConnection()) {
            for (Usuario a : selecionados) {
                try (PreparedStatement s1 = conn.prepareStatement("DELETE FROM alunos WHERE cpf = ?");
                     PreparedStatement s2 = conn.prepareStatement("DELETE FROM usuarios WHERE cpf = ?")) {
                    s1.setString(1, a.getCpf());
                    s1.executeUpdate();
                    s2.setString(1, a.getCpf());
                    s2.executeUpdate();
                }
            }
            carregarAlunosDoBanco();
            filtrarPorCpf(pesquisaCpfField.getText());
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao excluir alunos: " + e.getMessage()).showAndWait();
        }
    }
}