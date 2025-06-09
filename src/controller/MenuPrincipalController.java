package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.Sessao;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Pos;

public class MenuPrincipalController implements Initializable {

    @FXML
    private Label labelUsuario;

    @FXML
    private VBox sidebarVBox;

    @FXML
    private StackPane contentArea;

    @FXML
    private Pane contentPane;

    @FXML
    private Label contentTitle;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button btnToggleSidebar;

    @FXML
    private Button btnCadastrarAluno;
    @FXML
    private Button btnCadastrarTurma;
    @FXML
    private Button btnCadastrarDisciplina;
    @FXML
    private Button btnLancarNotas;
    @FXML
    private Button btnRegistrarPresenca;
    @FXML
    private Button btnCadastrarCurso;
    @FXML
    private Button btnRelatorio;
    @FXML
    private Button btnGerenciarUsuario;
    @FXML
    private Button btnGerenciarAluno;
    @FXML
    private Button btnGerenciarTurmas;
    @FXML
    private Button btnGerenciarCursos;
    @FXML
    private Button btnGerenciarDisciplinas;
    @FXML
    private Button btnConfiguracoes;
    @FXML
    private Button btnSair;

    private String tipoUsuario;
    private Button activeButton;
    private Map<Button, String> buttonStyles;
    private boolean sidebarCollapsed = false;
    private final double SIDEBAR_EXPANDED_WIDTH = 220.0;
    private final double SIDEBAR_COLLAPSED_WIDTH = 60.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoUsuario = Sessao.getTipoUsuario();
        String email = Sessao.getEmail();
        String displayText = String.format("Sistema Acadêmico - Logado como: %s (%s)", 
            email.split("@")[0].replace(".", " "), 
            tipoUsuario.substring(0, 1).toUpperCase() + tipoUsuario.substring(1));
        labelUsuario.setText(displayText);

        // Initialize button styles map
        buttonStyles = new HashMap<>();
        buttonStyles.put(btnCadastrarAluno, btnCadastrarAluno.getStyle());
        buttonStyles.put(btnCadastrarTurma, btnCadastrarTurma.getStyle());
        buttonStyles.put(btnCadastrarDisciplina, btnCadastrarDisciplina.getStyle());
        buttonStyles.put(btnCadastrarCurso, btnCadastrarCurso.getStyle());
        buttonStyles.put(btnLancarNotas, btnLancarNotas.getStyle());
        buttonStyles.put(btnRegistrarPresenca, btnRegistrarPresenca.getStyle());
        buttonStyles.put(btnRelatorio, btnRelatorio.getStyle());
        buttonStyles.put(btnGerenciarUsuario, btnGerenciarUsuario.getStyle());
        buttonStyles.put(btnGerenciarAluno, btnGerenciarAluno.getStyle());
        
        buttonStyles.put(btnGerenciarTurmas, btnGerenciarTurmas.getStyle());
        buttonStyles.put(btnGerenciarCursos, btnGerenciarCursos.getStyle());
        buttonStyles.put(btnGerenciarDisciplinas, btnGerenciarDisciplinas.getStyle());
        buttonStyles.put(btnConfiguracoes, btnConfiguracoes.getStyle());

        // Apply animations to buttons
        setupButtonAnimations();

        // Armazenar a instância do controlador no userData
        rootPane.setUserData(this);

        aplicarPermissoes();
    }

    public void configureStage(Stage stage) {
        stage.setTitle("Sistema de Gerenciamento Acadêmico");
        stage.setResizable(true);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
    }

    private void setupButtonAnimations() {
        for (Button btn : new Button[]{btnCadastrarAluno, btnCadastrarTurma, btnCadastrarDisciplina, 
                                       btnCadastrarCurso, btnLancarNotas, btnRegistrarPresenca, btnRelatorio,
                                       btnGerenciarUsuario, btnGerenciarAluno, btnGerenciarTurmas, btnGerenciarCursos, btnGerenciarDisciplinas, btnConfiguracoes}) {
            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(100), btn);
            scaleIn.setToX(1.05);
            scaleIn.setToY(1.05);

            ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), btn);
            scaleOut.setToX(1.0);
            scaleOut.setToY(1.0);

            btn.setOnMouseEntered(e -> scaleIn.playFromStart());
            btn.setOnMouseExited(e -> scaleOut.playFromStart());
        }
    }

    private void aplicarPermissoes() {
        // ocultar e desabilitar o gerenciamento de layout para todos os botões
        btnCadastrarAluno.setVisible(false);
        btnCadastrarAluno.setManaged(false);
        btnCadastrarTurma.setVisible(false);
        btnCadastrarTurma.setManaged(false);
        btnCadastrarDisciplina.setVisible(false);
        btnCadastrarDisciplina.setManaged(false);
        btnCadastrarCurso.setVisible(false);
        btnCadastrarCurso.setManaged(false);
        btnLancarNotas.setVisible(false);
        btnLancarNotas.setManaged(false);
        btnRegistrarPresenca.setVisible(false);
        btnRegistrarPresenca.setManaged(false);
        btnRelatorio.setVisible(false);
        btnRelatorio.setManaged(false);
        btnGerenciarUsuario.setVisible(false);
        btnGerenciarUsuario.setManaged(false);
        btnGerenciarAluno.setVisible(false);
        btnGerenciarAluno.setManaged(false);
        
        btnGerenciarTurmas.setVisible(false);
        btnGerenciarTurmas.setManaged(false);
        btnGerenciarCursos.setVisible(false);
        btnGerenciarCursos.setManaged(false);
        btnGerenciarDisciplinas.setVisible(false);
        btnGerenciarDisciplinas.setManaged(false);
        btnConfiguracoes.setVisible(false);
        btnConfiguracoes.setManaged(false);

        // Habilitar botões com base no tipo de usuário 
        int visibleButtonCount = 0;
        switch (tipoUsuario) {
            case "secretaria":
                btnCadastrarAluno.setVisible(true);
                btnCadastrarAluno.setManaged(true);
                btnCadastrarTurma.setVisible(true);
                btnCadastrarTurma.setManaged(true);
                btnCadastrarDisciplina.setVisible(true);
                btnCadastrarDisciplina.setManaged(true);
                btnCadastrarCurso.setVisible(true);
                btnCadastrarCurso.setManaged(true);
                btnRelatorio.setVisible(true);
                btnRelatorio.setManaged(true);
                btnGerenciarUsuario.setVisible(true);
                btnGerenciarUsuario.setManaged(true);
                btnGerenciarAluno.setVisible(true);
                btnGerenciarAluno.setManaged(true);
                
                btnGerenciarTurmas.setVisible(true);
                btnGerenciarTurmas.setManaged(true);
                btnGerenciarCursos.setVisible(true);
                btnGerenciarCursos.setManaged(true);
                btnGerenciarDisciplinas.setVisible(true);
                btnGerenciarDisciplinas.setManaged(true);
                btnConfiguracoes.setVisible(true);
                btnConfiguracoes.setManaged(true);
                visibleButtonCount = 11;
                break;
            case "professor":
                btnLancarNotas.setVisible(true);
                btnLancarNotas.setManaged(true);
                btnRegistrarPresenca.setVisible(true);
                btnRegistrarPresenca.setManaged(true);
                btnRelatorio.setVisible(true);
                btnRelatorio.setManaged(true);
                btnConfiguracoes.setVisible(true);
                btnConfiguracoes.setManaged(true);
                visibleButtonCount = 3;
                break;
            case "admin":
                btnRelatorio.setVisible(true);
                btnRelatorio.setManaged(true);
                btnCadastrarTurma.setVisible(true);
                btnCadastrarTurma.setManaged(true);
                btnCadastrarDisciplina.setVisible(true);
                btnCadastrarDisciplina.setManaged(true);
                btnCadastrarCurso.setVisible(true);
                btnCadastrarCurso.setManaged(true);
                btnRelatorio.setVisible(true);
                btnRelatorio.setManaged(true);
                btnGerenciarUsuario.setVisible(true);
                btnGerenciarUsuario.setManaged(true);
                btnGerenciarAluno.setVisible(true);
                btnGerenciarAluno.setManaged(true);
                
                btnGerenciarTurmas.setVisible(true);
                btnGerenciarTurmas.setManaged(true);
                btnGerenciarCursos.setVisible(true);
                btnGerenciarCursos.setManaged(true);
                btnGerenciarDisciplinas.setVisible(true);
                btnGerenciarDisciplinas.setManaged(true);
                btnConfiguracoes.setVisible(true);
                btnConfiguracoes.setManaged(true);
                visibleButtonCount = 11;
                break;
        }

        // Adjust VBox height dynamically: 40px per button (30px height + 12px spacing) + padding
        double buttonHeight = 30.0;
        double spacing = 12.0;
        double totalHeight = visibleButtonCount * buttonHeight + (visibleButtonCount > 0 ? (visibleButtonCount - 1) * spacing : 0) + 30.0;
        sidebarVBox.setPrefHeight(totalHeight);
        sidebarVBox.setMaxHeight(totalHeight);
    }

    private void setActiveButton(Button button) {
        if (activeButton != null) {
            activeButton.getStyleClass().remove("active");
        }
        if (button != null) {
            button.getStyleClass().add("active");
        }
        activeButton = button;
    }

    public void restaurarEstado() {
        // Restaurar estado da sidebar (mantém expandida por padrão)
        if (sidebarCollapsed) {
            toggleSidebar();
        }
        // Reaplicar estilos aos botões
        for (Map.Entry<Button, String> entry : buttonStyles.entrySet()) {
            Button btn = entry.getKey();
            btn.setStyle(entry.getValue());
            btn.getStyleClass().remove("active");
        }
        if (activeButton != null) {
            activeButton.getStyleClass().add("active");
        }
    }

    @FXML
    private void toggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        if (sidebarCollapsed) {
            sidebarVBox.setPrefWidth(SIDEBAR_COLLAPSED_WIDTH);
            sidebarVBox.getChildren().forEach(node -> {
                if (node instanceof Button btn) {
                    btn.setText("");
                    btn.setGraphicTextGap(0);
                    btn.setAlignment(javafx.geometry.Pos.CENTER);
                }
            });
            labelUsuario.setVisible(false);
        } else {
            sidebarVBox.setPrefWidth(SIDEBAR_EXPANDED_WIDTH);
            labelUsuario.setVisible(true);
            // Restore button text based on fx:id
            btnCadastrarAluno.setText("Cadastrar Aluno");
            btnCadastrarTurma.setText("Cadastrar Turma");
            btnCadastrarDisciplina.setText("Cadastrar Disciplina");
            btnCadastrarCurso.setText("Cadastrar Curso");
            btnLancarNotas.setText("Lançar Notas");
            btnRegistrarPresenca.setText("Registrar Presença");
            btnRelatorio.setText("Relatório");
            btnGerenciarUsuario.setText("Gerenciar Usuarios");
            btnGerenciarAluno.setText("Gerenciar Aluno");
            
            btnGerenciarTurmas.setText("Gerenciar Turmas");
            btnGerenciarCursos.setText("Gerenciar Cursos"); 
            btnGerenciarDisciplinas.setText("Gerenciar Disciplinas");
            btnConfiguracoes.setText("Configurações");
            
            sidebarVBox.getChildren().forEach(node -> {
                if (node instanceof Button btn) {
                    btn.setGraphicTextGap(12);
                    btn.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                }
            });
        }
    }

    private void carregarConteudo(String caminhoFXML, String titulo, Button button) {
        try {
            Parent content = FXMLLoader.load(getClass().getResource(caminhoFXML));
            contentPane.getChildren().clear();
            content.setOpacity(0);
            contentPane.getChildren().add(content);
            // Ensure content scales with contentPane
            if (content instanceof Region region) {
                region.setMaxWidth(Double.MAX_VALUE);
                region.setMaxHeight(Double.MAX_VALUE);
                region.prefWidthProperty().bind(contentPane.widthProperty());
                region.prefHeightProperty().bind(contentPane.heightProperty());

                StackPane.setAlignment(region, Pos.CENTER);
            }



            // Fade-in animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), content);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            // Update content title
            contentTitle.setText(titulo);
            // Set active button style
            setActiveButton(button);
            // Restaurar estado da interface
            restaurarEstado();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Falha ao carregar a tela: " + caminhoFXML + "\nErro: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void abrirCadastroAluno(ActionEvent event) {
        carregarConteudo("/view/CadastroAluno.fxml", "Cadastro de Aluno", btnCadastrarAluno);
    }

    @FXML
    private void abrirCadastroTurma(ActionEvent event) {
        carregarConteudo("/view/CadastroTurma.fxml", "Cadastro de Turma", btnCadastrarTurma);
    }

    @FXML
    private void abrirCadastroDisciplina(ActionEvent event) {
        carregarConteudo("/view/CadastroDisciplina.fxml", "Cadastro de Disciplina", btnCadastrarDisciplina);
    }

    @FXML
    private void abrirLancarNotas(ActionEvent event) {
        carregarConteudo("/view/LancarNotas.fxml", "Lançamento de Notas", btnLancarNotas);
    }

    @FXML
    private void abrirRegistrarPresenca(ActionEvent event) {
        carregarConteudo("/view/RegistrarPresenca.fxml", "Registro de Presença", btnRegistrarPresenca);
    }

    @FXML
    private void abrirCadastroCurso(ActionEvent event) {
        carregarConteudo("/view/CadastroCurso.fxml", "Cadastro de Curso", btnCadastrarCurso);
    }

    @FXML
    private void abrirRelatorio(ActionEvent event) {
        carregarConteudo("/view/Relatorio.fxml", "Relatórios", btnRelatorio);
    }
    
    @FXML
    private void abrirGerenciarUsuario(ActionEvent event) {
        carregarConteudo("/view/GerenciamentoUsuario.fxml", "Gerenciar Usuarios", btnGerenciarUsuario);
    }
    
    @FXML
    private void abrirGerenciarAluno(ActionEvent event) {
        carregarConteudo("/view/GerenciamentoAluno.fxml", "Gerenciar Aluno", btnGerenciarAluno);
    }
    
    
    public void limparConteudo() {
    contentTitle.setText("Painel Inicial");
    contentPane.getChildren().clear();
    }


    @FXML
    private void sair(ActionEvent event) {
        Sessao.limpar();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.setResizable(false); // Login screen non-resizable
            stage.show();
            setActiveButton(null);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Falha ao carregar a tela de login.");
            alert.showAndWait();
        }
    }
}