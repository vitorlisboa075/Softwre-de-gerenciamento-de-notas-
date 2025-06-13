package controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.Sessao;
import javafx.event.ActionEvent;
import java.io.IOException;
import model.MockDB;
import model.Usuario;

public class LoginController {

    @FXML
    private VBox loginContainer;

    @FXML
    private VBox loginCard;

    @FXML
    private ImageView logoImage;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private TextField txtSenhaVisible;

    @FXML
    private CheckBox chkShowPassword;

    @FXML
    private ImageView imgShowPassword;

    @FXML
    private Button btnEntrar;

    @FXML
    private Label lblErro;

    @FXML
    public void initialize() {
        // Fade-in animation for login container (logo and card)
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), loginContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Fade-in for logo
        FadeTransition logoFade = new FadeTransition(Duration.millis(700), logoImage);
        logoFade.setFromValue(0);
        logoFade.setToValue(1);
        logoFade.play();

        // Sync PasswordField and TextField
        txtSenhaVisible.textProperty().bindBidirectional(txtSenha.textProperty());
    }

    @FXML
    private void togglePasswordVisibility() {
        boolean showPassword = chkShowPassword.isSelected();
        txtSenha.setVisible(!showPassword);
        txtSenhaVisible.setVisible(showPassword);
        imgShowPassword.setImage(new javafx.scene.image.Image(getClass().getResource(
            showPassword ? "/images/eye-open.png" : "/images/eye-closed.png").toExternalForm()));
    }

    @FXML
    private void fazerLogin(ActionEvent event) {
        if (txtEmail == null || txtSenha == null) {
            lblErro.setText("Erro: Campos de entrada não inicializados.");
            lblErro.setVisible(true);
            return;
        }

        String email = txtEmail.getText().trim();
        String senha = txtSenha.getText().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            lblErro.setText("Por favor, preencha todos os campos.");
            lblErro.setVisible(true);
            return;
        }

        // Simulação de autenticação (substitua por sua lógica de banco de dados)
        if (autenticarUsuario(email, senha)) {
            abrirMenu();
        } else {
            lblErro.setText("Email ou senha inválidos.");
            lblErro.setVisible(true);
        }
    }

    private boolean autenticarUsuario(String email, String senha) {
        // Busca o usuário no nosso banco de dados simulado
        Usuario usuarioEncontrado = MockDB.findUsuarioByEmail(email);

        if (usuarioEncontrado != null && usuarioEncontrado.getSenha().equals(senha)) {
            // Se encontrou o usuário e a senha bate, inicia a sessão
            Sessao.setTipoUsuario(usuarioEncontrado.getTipoUsuario());
            Sessao.setEmail(usuarioEncontrado.getEmail());
            return true;
        }

        // Se não encontrou ou a senha está errada
        return false;
    }

    // Dentro da classe LoginController.java

    private void abrirMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPrincipal.fxml"));
            Parent root = loader.load();
            MenuPrincipalController controller = loader.getController();
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);

            // --- CORREÇÃO APLICADA AQUI ---
            // O caminho correto para o seu arquivo de estilos
            scene.getStylesheets().add(getClass().getResource("/view/styles.css").toExternalForm());

            stage.setScene(scene);

            // Esta chamada é do seu controller original, que espera um Stage
            if (controller != null) {
                controller.configureStage(stage);
            }

            stage.show();
        } catch (Exception e) { // Mudei para Exception para capturar qualquer tipo de erro
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Crítico");
            alert.setHeaderText("Falha ao carregar o menu principal.");
            alert.setContentText("Ocorreu um erro durante a inicialização do menu. Verifique se o arquivo FXML e o Controller estão corretos. Erro: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
