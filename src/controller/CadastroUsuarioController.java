// Local: src/controller/CadastroUsuarioController.java

package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import model.Usuario;

public class CadastroUsuarioController {

    // --- Campos de Dados Pessoais ---
    @FXML private ComboBox<String> tipoUsuarioCombo;
    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField emailField;
    @FXML private TextField telefoneField;
    @FXML private Label matriculaLabel;
    @FXML private TextField matriculaField;
    
    // --- Campos de Endereço ---
    @FXML private TextField logradouroField;
    @FXML private TextField numeroField;
    @FXML private TextField complementoField;
    @FXML private TextField bairroField;
    @FXML private TextField cidadeField;
    @FXML private TextField estadoField;
    @FXML private TextField cepField;
    
    @FXML private Button btnVoltar;
    
    /**
     * Método executado quando a tela é inicializada.
     * Configura o ComboBox de tipo de usuário e o listener para o campo de matrícula.
     */
    @FXML
    public void initialize() {
        tipoUsuarioCombo.getItems().addAll("Aluno", "Professor", "Secretaria");

        // Adiciona um listener para mostrar/ocultar o campo de matrícula dinamicamente
        tipoUsuarioCombo.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            boolean isAluno = "Aluno".equals(newValue);
            matriculaLabel.setVisible(isAluno);
            matriculaField.setVisible(isAluno);
        });
    }

    /**
     * Ação do botão "Salvar Cadastro".
     * Coleta todos os dados da tela, valida, cria um objeto Usuario e o "salva" (atualmente, imprime no console).
     */
    @FXML
    private void cadastrarUsuario() {
        // --- Validação dos Campos Obrigatórios ---
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

        // --- Criação do Objeto Usuario ---
        Usuario novoUsuario = new Usuario();
        novoUsuario.setTipoUsuario(tipo);
        novoUsuario.setNome(nome);
        novoUsuario.setCpf(cpf);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(cpf); // Senha inicial é o CPF

        // Coleta de dados opcionais
        novoUsuario.setTelefone(telefoneField.getText());
        
        // Coleta de endereço
        // (Em um projeto real, os campos de endereço também poderiam ser validados)
        novoUsuario.setLogradouro(logradouroField.getText());
        novoUsuario.setNumero(numeroField.getText());
        // ... setar os outros campos de endereço ...

        // Lógica específica para o tipo Aluno
        if (tipo.equals("Aluno")) {
            String matricula = matriculaField.getText();
            if (matricula.trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erro no Cadastro", "O campo Matrícula é obrigatório para alunos.");
                return;
            }
            novoUsuario.setMatricula(matricula);
        }

        // --- "Salvando" o usuário ---
        // Em um projeto real, você passaria o 'novoUsuario' para um UsuarioService
        // Ex: usuarioService.salvar(novoUsuario);

        System.out.println("--- Novo Usuário Pronto para Salvar ---");
        System.out.println("  Tipo: " + novoUsuario.getTipoUsuario());
        System.out.println("  Nome: " + novoUsuario.getNome());
        System.out.println("  CPF: " + novoUsuario.getCpf());
        System.out.println("  Email: " + novoUsuario.getEmail());
        System.out.println("  Senha Padrão: " + novoUsuario.getSenha());
        if (novoUsuario.isAluno()) {
            System.out.println("  Matrícula: " + novoUsuario.getMatricula());
        }
        
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário cadastrado com sucesso!\nA senha inicial é o CPF.");
        limparCampos();
    }

    /**
     * Limpa todos os campos de entrada de dados da tela.
     */
    @FXML
    private void limparCampos() {
        // Limpa dados pessoais
        tipoUsuarioCombo.getSelectionModel().clearSelection();
        nomeField.clear();
        cpfField.clear();
        emailField.clear();
        telefoneField.clear();
        matriculaField.clear();
        
        // Limpa dados de endereço
        logradouroField.clear();
        numeroField.clear();
        complementoField.clear();
        bairroField.clear();
        cidadeField.clear();
        estadoField.clear();
        cepField.clear();
    }

    /**
     * Retorna para a tela do menu principal.
     */
    @FXML
    private void voltar(ActionEvent event) {
        MenuPrincipalController menuController = buscarMenuController();
        if (menuController != null) {
            menuController.restaurarEstado();
            menuController.limparConteudo();
        }
    }

    /**
     * Busca a instância do controller do menu principal para poder interagir com ele.
     * @return O controller do menu principal, ou null se não for encontrado.
     */
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


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}