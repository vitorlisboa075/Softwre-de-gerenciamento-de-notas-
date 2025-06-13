package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.Sessao;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MenuPrincipalController implements Initializable {

    // --- Componentes FXML do seu Layout Original ---
    @FXML private Label labelUsuario;
    @FXML private VBox sidebarVBox;
    @FXML private StackPane contentArea;
    @FXML private Pane contentPane;
    @FXML private Label contentTitle;
    @FXML private BorderPane rootPane;
    @FXML private Button btnToggleSidebar;
    
    // --- BOTÕES ATUALIZADOS PARA AS NOVAS FUNCIONALIDADES ---
    // Secretaria
    @FXML private Button btnCadastrarUsuario;
    @FXML private Button btnGerenciarAlunos;
    @FXML private Button btnGerenciarUsuarios;
    @FXML private Button btnCadastrarDisciplina;
    @FXML private Button btnCadastrarTurma;
    @FXML private Button btnGerenciarTurmas;
    @FXML private Button btnGerenciarDisciplinas;
    // Professor
    @FXML private Button btnLancarNotas;
    @FXML private Button btnRegistrarPresenca;
    // Aluno
    @FXML private Button btnVerMinhasNotas;
    @FXML private Button btnEditarMeusDados;
    // Comum
    @FXML private Button btnRelatorios;
    @FXML private Button btnConfiguracoes;
    @FXML private Button btnSair;

    // --- Lógica Visual Mantida do seu Controller Original ---
    private Button activeButton;
    private Map<Button, String> buttonStyles; // Essencial para o método restaurarEstado
    private boolean sidebarCollapsed = false;
    private final double SIDEBAR_EXPANDED_WIDTH = 220.0;
    private final double SIDEBAR_COLLAPSED_WIDTH = 60.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String tipoUsuario = Sessao.getTipoUsuario();
        String email = Sessao.getEmail();
        String displayText = String.format("Logado como: %s (%s)", email, tipoUsuario);
        labelUsuario.setText(displayText);
        contentTitle.setText("Painel Inicial");

        // Inicializa o mapa de estilos (MANTIDO DO ORIGINAL)
        buttonStyles = new HashMap<>();
        
        // ATUALIZADO: Garante que só botões existentes sejam adicionados ao mapa e animações
        Button[] allButtons = {
            btnCadastrarUsuario, btnGerenciarAlunos, btnGerenciarUsuarios,
            btnCadastrarDisciplina, btnCadastrarTurma, btnLancarNotas,
            btnRegistrarPresenca, btnVerMinhasNotas, btnEditarMeusDados,
            btnRelatorios, btnConfiguracoes, btnGerenciarTurmas, btnGerenciarDisciplinas
        };
        
        for (Button btn : allButtons) {
            if (btn != null) {
                buttonStyles.put(btn, btn.getStyle());
            }
        }

        setupButtonAnimations(allButtons);
        rootPane.setUserData(this);
        aplicarPermissoes(tipoUsuario);
    }
    
    // MANTIDO DO ORIGINAL
    private void setupButtonAnimations(Button[] buttons) {
        for (Button btn : buttons) {
            if (btn == null) continue;
            
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

    // ATUALIZADO: Permissões para os novos perfis e botões
    private void aplicarPermissoes(String tipoUsuario) {
        setButtonVisibility(false, btnCadastrarUsuario, btnGerenciarAlunos, btnGerenciarUsuarios, 
                                  btnCadastrarDisciplina, btnCadastrarTurma, btnLancarNotas, 
                                  btnRegistrarPresenca, btnVerMinhasNotas, btnEditarMeusDados,
                                  btnRelatorios, btnConfiguracoes, btnGerenciarTurmas,btnGerenciarDisciplinas);

        switch (tipoUsuario.toLowerCase()) {
            case "secretaria":
                setButtonVisibility(true, btnCadastrarUsuario, btnGerenciarAlunos, btnGerenciarUsuarios,
                                          btnCadastrarDisciplina, btnCadastrarTurma, btnRelatorios, btnConfiguracoes, btnGerenciarTurmas, btnGerenciarDisciplinas);
                break;
            case "professor":
                setButtonVisibility(true, btnLancarNotas, btnRegistrarPresenca, btnRelatorios, btnConfiguracoes);
                break;
            case "aluno":
                setButtonVisibility(true, btnVerMinhasNotas, btnEditarMeusDados, btnConfiguracoes);
                break;
        }
    }

    // MANTIDO DO ORIGINAL
    private void setActiveButton(Button button) {
        if (activeButton != null) {
            activeButton.getStyleClass().remove("active");
        }
        if (button != null) {
            button.getStyleClass().add("active");
        }
        activeButton = button;
    }

    // MANTIDO DO ORIGINAL
    public void restaurarEstado() {
        if (sidebarCollapsed) {
            toggleSidebar();
        }
        for (Map.Entry<Button, String> entry : buttonStyles.entrySet()) {
            Button btn = entry.getKey();
            if (btn != null) {
                btn.setStyle(entry.getValue());
                btn.getStyleClass().remove("active");
            }
        }
        if (activeButton != null) {
            activeButton.getStyleClass().add("active");
        }
    }

    // MANTIDO DO ORIGINAL (textos dos botões atualizados)
    @FXML
    private void toggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        if (sidebarCollapsed) {
            sidebarVBox.setPrefWidth(SIDEBAR_COLLAPSED_WIDTH);
            sidebarVBox.getChildren().forEach(node -> {
                if (node instanceof Button btn) {
                    btn.setText("");
                    btn.setGraphicTextGap(0);
                    btn.setAlignment(Pos.CENTER);
                }
            });
            labelUsuario.setVisible(false);
        } else {
            sidebarVBox.setPrefWidth(SIDEBAR_EXPANDED_WIDTH);
            labelUsuario.setVisible(true);
            
            // Textos atualizados para os novos botões
            if (btnCadastrarUsuario != null) btnCadastrarUsuario.setText("Cadastrar Usuário");
            if (btnGerenciarAlunos != null) btnGerenciarAlunos.setText("Gerenciar Alunos");
            if (btnGerenciarTurmas != null) btnGerenciarTurmas.setText("Gerenciamento de Alunos"); 
            if (btnGerenciarDisciplinas!= null) btnGerenciarDisciplinas.setText("Gerenciamento de Alunos"); 
            if (btnGerenciarUsuarios != null) btnGerenciarUsuarios.setText("Gerenciar Usuários");
            if (btnCadastrarDisciplina != null) btnCadastrarDisciplina.setText("Cad. Disciplina");
            if (btnCadastrarTurma != null) btnCadastrarTurma.setText("Cadastrar Turma");
            if (btnLancarNotas != null) btnLancarNotas.setText("Lançar Notas");
            if (btnRegistrarPresenca != null) btnRegistrarPresenca.setText("Reg. Presença");
            if (btnVerMinhasNotas != null) btnVerMinhasNotas.setText("Ver Minhas Notas");
            if (btnEditarMeusDados != null) btnEditarMeusDados.setText("Editar Meus Dados");
            if (btnRelatorios != null) btnRelatorios.setText("Relatórios");
            if (btnConfiguracoes != null) btnConfiguracoes.setText("Configurações");
            
            sidebarVBox.getChildren().forEach(node -> {
                if (node instanceof Button btn) {
                    btn.setGraphicTextGap(10);
                    btn.setAlignment(Pos.CENTER_LEFT);
                }
            });
        }
    }

    // MANTIDO DO ORIGINAL
    private void carregarConteudo(String fxmlPath, String title, Button button) {
        try {
            Parent content = FXMLLoader.load(getClass().getResource(fxmlPath));
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), content);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            
            contentPane.getChildren().setAll(content);
            contentTitle.setText(title);
            setActiveButton(button);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Falha ao carregar a tela: " + fxmlPath);
        }
    }

    // --- MÉTODOS DE AÇÃO (onAction) ATUALIZADOS ---
    
    @FXML void abrirCadastroUsuario(ActionEvent e) { carregarConteudo("/view/CadastroUsuario.fxml", "Cadastro de Usuário", btnCadastrarUsuario); }
    @FXML void abrirGerenciarUsuarios(ActionEvent e) { carregarConteudo("/view/GerenciamentoUsuario.fxml", "Gerenciamento de Usuários", btnGerenciarUsuarios); }
    @FXML void abrirCadastroDisciplina(ActionEvent e) { carregarConteudo("/view/CadastroDisciplina.fxml", "Cadastro de Disciplina", btnCadastrarDisciplina); }
    @FXML void abrirCadastroTurma(ActionEvent e) { carregarConteudo("/view/CadastroTurma.fxml", "Cadastro de Turma", btnCadastrarTurma); }
    @FXML void abrirLancarNotas(ActionEvent e) { carregarConteudo("/view/LancarNotas.fxml", "Lançamento de Notas", btnLancarNotas); }
    
    // Métodos para funcionalidades futuras
    @FXML void abrirRegistrarPresenca(ActionEvent e) { showAlert("Em Breve", "Funcionalidade de Registro de Presença em desenvolvimento."); }
    @FXML void abrirEditarMeusDados(ActionEvent e) { showAlert("Em Breve", "Funcionalidade de Edição de Dados em desenvolvimento."); }
    
   
    // Adicione estes métodos dentro da sua classe MenuPrincipalController
    @FXML void abrirGerenciarTurmas(ActionEvent e) {carregarConteudo("/view/GerenciarTurmas.fxml", "Gerenciamento de Turmas", btnGerenciarTurmas);}
    @FXML void abrirGerenciarDisciplinas(ActionEvent e) { carregarConteudo("/view/GerenciarDisciplinas.fxml", "Gerenciamento de Disciplinas", btnGerenciarDisciplinas);}
    @FXML void abrirVerMinhasNotas(ActionEvent e) {carregarConteudo("/view/VerMinhasNotas.fxml", "Minhas Notas e Frequência", btnVerMinhasNotas);}
    @FXML void abrirRelatorios(ActionEvent e) { carregarConteudo("/view/Relatorio.fxml", "Relatórios de Turmas", btnRelatorios);}
    @FXML void abrirGerenciarAlunos(ActionEvent e) {carregarConteudo("/view/GerenciamentoAluno.fxml", "Gerenciamento de Alunos", btnGerenciarAlunos);}
    @FXML void abrirConfiguracoes(ActionEvent e) { carregarConteudo("/view/Configuracoes.fxml", "Configurações", btnConfiguracoes);}
    
    @FXML
    private void sair(ActionEvent event) {
        try {
            // 1. Pega a janela atual (a do menu) e simplesmente a fecha.
            Stage menuStage = (Stage) rootPane.getScene().getWindow();
            menuStage.close();

            // 2. Cria uma janela de login completamente nova (um novo Stage).
            Stage loginStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));

            // 3. Define a cena com o tamanho fixo original da sua tela de login.
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/view/styles.css").toExternalForm());

            // 4. Configura e exibe a nova janela de login.
            loginStage.setTitle("Login");
            loginStage.setResizable(false);
            loginStage.setScene(scene);
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Adicione um alerta aqui se houver um erro ao carregar o FXML do login.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Crítico");
            alert.setHeaderText("Não foi possível reiniciar a tela de login.");
            alert.showAndWait();
        }
    }
    
    // MANTIDO DO ORIGINAL
    public void limparConteudo() {
        contentPane.getChildren().clear();
        contentTitle.setText("Painel Inicial");
        setActiveButton(null);
    }
    
    // Método de alerta auxiliar
    private void setButtonVisibility(boolean isVisible, Button... buttons) {
        for (Button button : buttons) {
            if (button != null) {
                button.setVisible(isVisible);
                button.setManaged(isVisible);
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void configureStage(Stage stage) {
        stage.setTitle("Sistema de Gerenciamento Escolar");
        stage.setResizable(true);
        // Ajustado para as dimensões do novo FXML
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
    }
}