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
        // Exemplo de autenticação para teste
        if (email.equals("professor@if.com") && senha.equals("Abcd1234!")) {
            Sessao.setTipoUsuario("professor");
            Sessao.setEmail(email);
            return true;
        } else if (email.equals("secretaria@if.com") && senha.equals("Abcd1234!")) {
            Sessao.setTipoUsuario("secretaria");
            Sessao.setEmail(email);
            return true;
        } else if (email.equals("admin@if.com") && senha.equals("Abcd1234!")) {
            Sessao.setTipoUsuario("admin");
            Sessao.setEmail(email);
            return true;
        }
        return false;
    }

    private void abrirMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPrincipal.fxml"));
            Parent root = loader.load();
            MenuPrincipalController controller = loader.getController();
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            controller.configureStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Falha ao carregar o menu principal.");
            alert.showAndWait();
        }
    }
}
