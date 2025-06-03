package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import util.Sessao;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuPrincipalController implements Initializable {

    @FXML
    private Label labelUsuario;

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
    private Button btnGerenciarUsuarios;
    @FXML
    private Button btnCadastrarCurso;


    private String tipoUsuario;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoUsuario = Sessao.getTipoUsuario();
        labelUsuario.setText("Logado como: " + tipoUsuario);
        aplicarPermissoes();
    }

    private void aplicarPermissoes() {
        switch (tipoUsuario) {
            case "secretaria":
                btnLancarNotas.setVisible(false);
                btnRegistrarPresenca.setVisible(false);
                btnGerenciarUsuarios.setVisible(false);
                break;
            case "professor":
                btnCadastrarAluno.setVisible(false);
                btnCadastrarTurma.setVisible(false);
                btnCadastrarDisciplina.setVisible(false);
                btnCadastrarCurso.setVisible(false);
                btnGerenciarUsuarios.setVisible(false);
                break;
            case "admin":
                btnCadastrarAluno.setVisible(false);
                btnCadastrarTurma.setVisible(false);
                btnCadastrarDisciplina.setVisible(false);
                btnCadastrarCurso.setVisible(false);
                btnLancarNotas.setVisible(false);
                btnRegistrarPresenca.setVisible(false);
                break;
        }
    }


    // Método genérico para abrir telas
    private void abrirTela(String caminhoFXML, ActionEvent event, String titulo) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(caminhoFXML));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Métodos dos botões
    @FXML
    private void abrirCadastroAluno(ActionEvent event) {
        abrirTela("/view/CadastroAluno.fxml", event, "Cadastro de Aluno");
    }

    @FXML
    private void abrirCadastroTurma(ActionEvent event) {
        abrirTela("/view/CadastroTurma.fxml", event, "Cadastro de Turma");
    }

    @FXML
    private void abrirCadastroDisciplina(ActionEvent event) {
        abrirTela("/view/CadastroDisciplina.fxml", event, "Cadastro de Disciplina");
    }

    @FXML
    private void abrirLancarNotas(ActionEvent event) {
        abrirTela("/view/LancarNotas.fxml", event, "Lançamento de Notas");
    }

    @FXML
    private void abrirRegistrarPresenca(ActionEvent event) {
        abrirTela("/view/RegistrarPresenca.fxml", event, "Registro de Presença");
    }

    @FXML
    private void sair(ActionEvent event) {
        Sessao.limpar();
        abrirTela("/view/Login.fxml", event, "Login");
    }
    
    @FXML
    private void abrirCadastroCurso(ActionEvent event) {
        abrirTela("/view/CadastroCurso.fxml", event, "Cadastro de Curso");
    }

}
