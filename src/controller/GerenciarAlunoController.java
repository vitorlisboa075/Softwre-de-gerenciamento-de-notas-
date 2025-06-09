package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import model.Aluno;
import service.AlunoService;
import util.UsuarioSession;
import util.TrocaDeTela;

public class GerenciarAlunoController {

    @FXML private TableView<Aluno> tabelaAlunos;
    @FXML private TableColumn<Aluno, Number> colId;
    @FXML private TableColumn<Aluno, String> colNome;
    @FXML private TableColumn<Aluno, String> colMatricula;
    @FXML private TableColumn<Aluno, String> colCurso;
    @FXML private TableColumn<Aluno, String> colTurma;
    @FXML private TableColumn<Aluno, String> colEmail;
    @FXML private Button btnVoltar;

    private final AlunoService alunoService = new AlunoService();

    public void initialize() {
        if (!UsuarioSession.temPermissao("ADMIN", "SECRETARIA")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Acesso negado.");
            alert.showAndWait();
            System.exit(0);
        }

        colId.setCellValueFactory(data -> data.getValue().idProperty());
        colNome.setCellValueFactory(data -> data.getValue().nomeProperty());
        colMatricula.setCellValueFactory(data -> data.getValue().matriculaProperty());
        colCurso.setCellValueFactory(data -> data.getValue().cursoProperty());
        colTurma.setCellValueFactory(data -> data.getValue().turmaProperty());
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());

        carregarAlunos();
    }

    private void carregarAlunos() {
        ObservableList<Aluno> lista = FXCollections.observableArrayList(alunoService.buscarTodos());
        tabelaAlunos.setItems(lista);
    }

    @FXML
    private void onNovoAluno() {
        alunoService.abrirFormulario(null);
        carregarAlunos();
    }

    @FXML
    private void onEditarAluno() {
        Aluno alunoSelecionado = tabelaAlunos.getSelectionModel().getSelectedItem();
        if (alunoSelecionado != null) {
            alunoService.abrirFormulario(alunoSelecionado);
            carregarAlunos();
        } else {
            new Alert(Alert.AlertType.WARNING, "Selecione um aluno.").showAndWait();
        }
    }

    @FXML
    private void onExcluirAluno() {
        Aluno alunoSelecionado = tabelaAlunos.getSelectionModel().getSelectedItem();
        if (alunoSelecionado != null) {
            alunoService.excluir(alunoSelecionado.getId()); // getId() agora retorna long
            carregarAlunos();
        }
    }
    
    // Método para limpar os campos
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
