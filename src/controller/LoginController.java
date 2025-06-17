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
import model.Usuario;
import model.UsuarioDAO;

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
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), loginContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        FadeTransition logoFade = new FadeTransition(Duration.millis(700), logoImage);
        logoFade.setFromValue(0);
        logoFade.setToValue(1);
        logoFade.play();

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
        String email = txtEmail.getText().trim();
        String senha = txtSenha.getText().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            lblErro.setText("Por favor, preencha todos os campos.");
            lblErro.setVisible(true);
            return;
        }

        Usuario usuario = UsuarioDAO.buscarUsuarioPorEmail(email);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            // Define os dados do usuário na sessão
            Sessao.setEmail(usuario.getEmail());
            Sessao.setTipoUsuario(usuario.getTipoUsuario());
            Sessao.setCpf(usuario.getCpf()); // <-- adicionando o CPF

            abrirMenu();
        } else {
            lblErro.setText("Email ou senha inválidos.");
            lblErro.setVisible(true);
        }
    }

    private void abrirMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPrincipal.fxml"));
            Parent root = loader.load();
            MenuPrincipalController controller = loader.getController();
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);

            scene.getStylesheets().add(getClass().getResource("/view/styles.css").toExternalForm());
            stage.setScene(scene);

            if (controller != null) {
                controller.configureStage(stage);
            }

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Crítico");
            alert.setHeaderText("Falha ao carregar o menu principal.");
            alert.setContentText("Erro: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
