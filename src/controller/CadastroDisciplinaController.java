// Local: src/controller/CadastroDisciplinaController.java
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import model.Disciplina;
import model.Usuario; // MUDANÇA: Importamos Usuario em vez de Professor

import java.util.stream.Collectors;

public class CadastroDisciplinaController {

    @FXML private TextField nomeDisciplinaField;
    @FXML private TextField pesquisaProfessorField;
    @FXML private TableView<Usuario> tabelaProfessores; // MUDANÇA: A tabela agora é de Usuario
    @FXML private TableColumn<Usuario, Void> colSelecaoProfessor;
    @FXML private TableColumn<Usuario, String> colNomeProfessor;
    @FXML private TableColumn<Usuario, String> colCpfProfessor;
    @FXML private Button btnVoltar;

    private ObservableList<Usuario> listaProfessores = FXCollections.observableArrayList();
    private ToggleGroup professorToggleGroup = new ToggleGroup();

    @FXML
    public void initialize() {
        carregarDadosSimulados();

        // Configura as colunas para o objeto Usuario
        colNomeProfessor.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colCpfProfessor.setCellValueFactory(cellData -> cellData.getValue().cpfProperty());

        // Configuração da coluna de seleção com RadioButton (a lógica interna não muda)
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

        // Configura o filtro para a lista de Usuários
        FilteredList<Usuario> filteredData = new FilteredList<>(listaProfessores, p -> true);
        pesquisaProfessorField.textProperty().addListener((obs, oldV, newV) -> {
            filteredData.setPredicate(usuario -> {
                if (newV == null || newV.isEmpty()) return true;
                String filter = newV.toLowerCase();
                if (usuario.getNome().toLowerCase().contains(filter)) return true;
                if (usuario.getCpf().toLowerCase().contains(filter)) return true;
                return false;
            });
        });

        tabelaProfessores.setItems(filteredData);
    }
    
    private void carregarDadosSimulados() {
        // Criamos usuários genéricos e filtramos para pegar apenas os professores
        Usuario u1 = new Usuario();
        u1.setNome("Mariana Costa"); u1.setCpf("44455566677"); u1.setTipoUsuario("Professor");

        Usuario u2 = new Usuario();
        u2.setNome("Ricardo Alves"); u2.setCpf("55566677788"); u2.setTipoUsuario("Professor");

        Usuario u3 = new Usuario();
        u3.setNome("Julio Cesar"); u3.setCpf("12312312312"); u3.setTipoUsuario("Professor");

        Usuario u4 = new Usuario(); // Este não deve aparecer na lista
        u4.setNome("Fernanda Abreu"); u4.setCpf("99988877766"); u4.setTipoUsuario("Secretaria");

        // Adicionamos todos os usuários a uma lista temporária
        ObservableList<Usuario> todosOsUsuarios = FXCollections.observableArrayList(u1, u2, u3, u4);

        // Filtramos a lista para popular nossa tabela APENAS com professores
        listaProfessores.setAll(todosOsUsuarios.stream()
            .filter(u -> "Professor".equalsIgnoreCase(u.getTipoUsuario()))
            .collect(Collectors.toList()));
    }

    @FXML
    void cadastrarDisciplina() {
        String nome = nomeDisciplinaField.getText();
        Toggle selectedToggle = professorToggleGroup.getSelectedToggle();

        if (nome.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campo Vazio", "Por favor, informe o nome da disciplina.");
            return;
        }

        if (selectedToggle == null) {
            showAlert(Alert.AlertType.WARNING, "Seleção Necessária", "Por favor, selecione um professor para a disciplina.");
            return;
        }

        // MUDANÇA: O objeto recuperado agora é um Usuario
        Usuario professorSelecionado = (Usuario) selectedToggle.getUserData();

        // Cria a nova disciplina
        // Disciplina novaDisciplina = new Disciplina(0, nome, professorSelecionado);
        
        System.out.println("--- Disciplina Salva ---");
        System.out.println("Nome: " + nome);
        System.out.println("Professor Responsável: " + professorSelecionado.getNome());

        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Disciplina cadastrada e associada ao professor com sucesso!");
        limparCampos();
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