package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import model.Usuario;
import model.UsuarioWrapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GerenciarUsuarioController {

    @FXML private TextField pesquisaField;
    @FXML private TableView<UsuarioWrapper> tabelaUsuarios;
    @FXML private TableColumn<UsuarioWrapper, Boolean> colSelecao;
    @FXML private TableColumn<UsuarioWrapper, String> colNome;
    @FXML private TableColumn<UsuarioWrapper, String> colCpf;
    @FXML private TableColumn<UsuarioWrapper, String> colEmail;
    @FXML private TableColumn<UsuarioWrapper, String> colTipo;
    @FXML private Button btnVoltar;

    // Lista principal que simula o banco de dados com todos os usuários
    private ObservableList<UsuarioWrapper> masterUserList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarDadosSimulados();

        // 1. Cria uma lista filtrada que mostra APENAS Professores e Secretarias
        FilteredList<UsuarioWrapper> filteredUsers = new FilteredList<>(
            masterUserList,
            uw -> !uw.getUsuario().isAluno() // A condição é: não seja Aluno
        );

        // 2. Configura as colunas da tabela
        colSelecao.setCellValueFactory(cellData -> cellData.getValue().selecionadoProperty());
        colSelecao.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecao));
        tabelaUsuarios.setEditable(true);

        colNome.setCellValueFactory(cellData -> cellData.getValue().getUsuario().nomeProperty());
        colCpf.setCellValueFactory(cellData -> cellData.getValue().getUsuario().cpfProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().getUsuario().emailProperty());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().getUsuario().tipoProperty());

        // 3. Configura a barra de pesquisa para filtrar os usuários visíveis
        FilteredList<UsuarioWrapper> searchFilteredData = new FilteredList<>(filteredUsers, p -> true);
        pesquisaField.textProperty().addListener((obs, oldV, newV) -> {
            searchFilteredData.setPredicate(userWrapper -> {
                if (newV == null || newV.isEmpty()) return true;
                String filter = newV.toLowerCase();
                // Permite buscar por nome ou CPF
                return userWrapper.getUsuario().getNome().toLowerCase().contains(filter) ||
                       userWrapper.getUsuario().getCpf().toLowerCase().contains(filter);
            });
        });
        
        // 4. Associa os dados finalmente filtrados à tabela
        tabelaUsuarios.setItems(searchFilteredData);
    }

    /**
     * O método para voltar ao menu principal.
     */
    @FXML
    private void voltar(ActionEvent event) {
        MenuPrincipalController menuController = buscarMenuController();
        if (menuController != null) {
            menuController.limparConteudo();
        }
    }
    
    /**
     * Busca a instância do controller do menu principal para poder interagir com ele.
     */
    private MenuPrincipalController buscarMenuController() {
        Parent root = btnVoltar.getScene().getRoot();
        if (root instanceof BorderPane) {
            Object controller = ((BorderPane) root).getUserData();
            if (controller instanceof MenuPrincipalController) {
                return (MenuPrincipalController) controller;
            }
        }
        return null;
    }

    // --- Métodos de Ação e Simulação ---

    private void carregarDadosSimulados() {
        Usuario prof1 = new Usuario(); prof1.setNome("Mariana Costa"); prof1.setCpf("44455566677"); prof1.setTipoUsuario("Professor");
        Usuario prof2 = new Usuario(); prof2.setNome("Ricardo Alves"); prof2.setCpf("55566677788"); prof2.setTipoUsuario("Professor");
        Usuario sec1 = new Usuario(); sec1.setNome("Jorge Silva"); sec1.setCpf("88877766655"); sec1.setTipoUsuario("Secretaria");
        Usuario aluno1 = new Usuario(); aluno1.setNome("Carlos Souza"); aluno1.setCpf("11122233344"); aluno1.setTipoUsuario("Aluno"); // Não aparecerá na tabela
        
        masterUserList.addAll(
            new UsuarioWrapper(prof1), 
            new UsuarioWrapper(prof2),
            new UsuarioWrapper(sec1), 
            new UsuarioWrapper(aluno1)
        );
    }
    
    @FXML private void handleNovoUsuario() {
        showAlert(Alert.AlertType.INFORMATION, "Ação", "Navegando para a tela de Cadastro de Usuário...");
    }

    @FXML private void handleEditarUsuario() {
        // Lógica para editar (precisaria selecionar um)
    }

    @FXML private void handleExcluirUsuarios() {
        List<UsuarioWrapper> selecionados = masterUserList.stream()
                .filter(UsuarioWrapper::isSelecionado)
                .filter(uw -> !uw.getUsuario().isAluno()) // Garante que só está pegando os visíveis
                .collect(Collectors.toList());

        if (selecionados.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Selecione pelo menos um usuário para excluir.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir " + selecionados.size() + " usuário(s)?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmação de Exclusão");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            masterUserList.removeAll(selecionados);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário(s) excluído(s) com sucesso.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}