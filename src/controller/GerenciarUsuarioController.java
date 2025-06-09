package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import model.Usuario;
import service.UsuarioService;
import util.TrocaDeTela;

public class GerenciarUsuarioController {

    @FXML private TableView<Usuario> tabelaUsuarios;
    @FXML private TableColumn<Usuario, Number> colId;
    @FXML private TableColumn<Usuario, String> colNome;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colTipo;
    @FXML private Button btnVoltar;

    private final UsuarioService usuarioService = new UsuarioService();

    public void initialize() {
        colId.setCellValueFactory(data -> data.getValue().idProperty());
        colNome.setCellValueFactory(data -> data.getValue().nomeProperty());
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());
        colTipo.setCellValueFactory(data -> data.getValue().tipoProperty());

        carregarUsuarios();
    }

    private void carregarUsuarios() {
        ObservableList<Usuario> lista = FXCollections.observableArrayList(usuarioService.buscarTodos());
        tabelaUsuarios.setItems(lista);
    }

    @FXML
    private void onNovoUsuario() {
        usuarioService.abrirFormulario(null);
        carregarUsuarios();
    }

    @FXML
    private void onEditarUsuario() {
        Usuario usuario = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            usuarioService.abrirFormulario(usuario);
            carregarUsuarios();
        } else {
            new Alert(Alert.AlertType.WARNING, "Selecione um usuário.").showAndWait();
        }
    }

    @FXML
    private void onExcluirUsuario() {
        Usuario usuario = tabelaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            usuarioService.excluir(usuario.getId());
            carregarUsuarios();
        }
    }
    
    @FXML
    private void limparCampos() {
        
    }
    
    
    // Voltar para o Menu Principal
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
        if (root instanceof BorderPane borderPane) {
            Object controller = borderPane.getUserData();
            if (controller instanceof MenuPrincipalController menuController) {
                return menuController;
            }
        }
        return null;
    }
}
