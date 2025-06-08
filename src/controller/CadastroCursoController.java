package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.Curso;
import javafx.scene.layout.BorderPane;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.TrocaDeTela;

public class CadastroCursoController {

    // Campos da interface (FXML)
    @FXML private TextField nomeField;
    @FXML private TextField descricaoField;
    @FXML private TextField codigoField;
    @FXML private TextField cargaHorariaField;
    @FXML private ComboBox<String> modalidadeCombo;
    @FXML private Button btnVoltar;


    //  Lista simulada de cursos
    private static List<Curso> listaCursos = new ArrayList<>();

    //  Este método é chamado automaticamente quando a tela abre
    @FXML
    public void initialize() {
        modalidadeCombo.getItems().addAll("Presencial", "EAD", "Semi-presencial");
    }

    // Método que roda quando clica no botão "Cadastrar"
    @FXML
    private void cadastrarCurso() {
        // Captura os dados dos campos
        String nome = nomeField.getText();
        String descricao = descricaoField.getText();
        String codigo = codigoField.getText();
        String carga = cargaHorariaField.getText();
        String modalidade = modalidadeCombo.getValue();

        // Validação dos campos obrigatórios
        if (nome.isEmpty() || codigo.isEmpty() || carga.isEmpty() || modalidade == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Preencha todos os campos!");
            alert.showAndWait();
            return;
        }

        // Convertendo carga horária para inteiro
        int cargaInt;
        try {
            cargaInt = Integer.parseInt(carga);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Carga Horária deve ser um número!");
            alert.showAndWait();
            return;
        }

        // Criar o objeto Curso
        Curso novo = new Curso(gerarId(), nome, descricao, cargaInt, modalidade);

        // Adicionar na lista 
        listaCursos.add(novo);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Curso cadastrado com sucesso!");
        alert.showAndWait();

        // Limpa os campos após cadastrar
        limparCampos();
    }

    // Método para limpar os campos
    @FXML
    private void limparCampos() {
        nomeField.clear();
        descricaoField.clear();
        codigoField.clear();
        cargaHorariaField.clear();
        modalidadeCombo.getSelectionModel().clearSelection();
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



    // Gerador de IDs 
    private int gerarId() {
        return listaCursos.size() + 1;
    }

    // Getter se quiser acessar a lista em outro lugar
    public static List<Curso> getListaCursos() {
        return listaCursos;
    }
}
