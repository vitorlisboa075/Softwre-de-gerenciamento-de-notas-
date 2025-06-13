// Local: src/controller/ConfiguracoesController.java
package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.MockDB; // Importa nosso banco de dados simulado
import model.Usuario;
import util.Sessao;

import java.util.Arrays;
import java.util.List;

public class ConfiguracoesController {

    // --- Campos de Dados Pessoais e Endereço ---
    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField emailField;
    @FXML private TextField telefoneField;
    @FXML private TextField cepField;
    @FXML private TextField logradouroField;
    @FXML private TextField numeroField;
    @FXML private TextField bairroField;
    @FXML private TextField cidadeField;
    @FXML private TextField estadoField;
    
    // --- Campos de Senha ---
    @FXML private PasswordField senhaAtualField;
    @FXML private PasswordField novaSenhaField;
    @FXML private PasswordField confirmarSenhaField;

    // --- Controles de Ação ---
    @FXML private CheckBox habilitarEdicaoCheckBox;
    @FXML private Button btnSalvar;

    private Usuario usuarioLogado;
    private List<TextField> camposDeDados;
    private List<PasswordField> camposDeSenha;

    @FXML
    public void initialize() {
        // Agrupa os campos para facilitar o bloqueio/desbloqueio
        camposDeDados = Arrays.asList(
            nomeField, telefoneField, cepField, logradouroField, 
            numeroField, bairroField, cidadeField, estadoField
        );
        camposDeSenha = Arrays.asList(senhaAtualField, novaSenhaField, confirmarSenhaField);
        
        // Inicia com os campos e o botão salvar desabilitados
        setarEditabilidade(false);
        
        carregarDadosDoUsuario();
    }

    private void carregarDadosDoUsuario() {
        String emailSessao = Sessao.getEmail();
        if (emailSessao == null) {
            showAlert(Alert.AlertType.ERROR, "Erro de Sessão", "Não foi possível identificar o usuário logado.");
            return;
        }

        // Busca o usuário logado no nosso MockDB
        this.usuarioLogado = MockDB.findUsuarioByEmail(emailSessao);

        if (this.usuarioLogado != null) {
            // Preenche os campos da tela com os dados do usuário
            nomeField.setText(usuarioLogado.getNome());
            cpfField.setText(usuarioLogado.getCpf());
            emailField.setText(usuarioLogado.getEmail());
            telefoneField.setText(usuarioLogado.getTelefone());
            logradouroField.setText(usuarioLogado.getLogradouro());
            // Preencha os outros campos de endereço aqui...
        }
    }

    /**
     * Este método é chamado pelo checkbox para habilitar ou desabilitar a edição.
     */
    @FXML
    private void toggleEdicao() {
        boolean habilitar = habilitarEdicaoCheckBox.isSelected();
        setarEditabilidade(habilitar);
    }

    /**
     * Método auxiliar para alterar a propriedade 'editable' de todos os campos de uma vez.
     */
    private void setarEditabilidade(boolean isEditable) {
        // Habilita/desabilita campos de texto
        for (TextField campo : camposDeDados) {
            campo.setEditable(isEditable);
        }
        // Habilita/desabilita campos de senha
        for (PasswordField campo : camposDeSenha) {
            campo.setEditable(isEditable);
        }
        // Habilita/desabilita o botão de salvar
        btnSalvar.setDisable(!isEditable);
    }

    @FXML
    private void handleSalvar() {
        if (usuarioLogado == null) return;

        // Atualiza os dados do objeto Usuario com os valores dos campos de texto
        usuarioLogado.setNome(nomeField.getText());
        usuarioLogado.setTelefone(telefoneField.getText());
        usuarioLogado.setLogradouro(logradouroField.getText());
        // ... salvar outros campos de endereço ...

        // Lógica para alterar a senha, apenas se o usuário preencheu o campo de nova senha
        String novaSenha = novaSenhaField.getText();
        if (!novaSenha.isEmpty()) {
            // Valida se a senha atual fornecida está correta
            if (!senhaAtualField.getText().equals(usuarioLogado.getSenha())) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Senha atual incorreta. A senha não foi alterada.");
                return; // Para a execução para não salvar outras coisas se a senha estiver errada
            }
            // Valida se a nova senha e a confirmação são iguais
            if (!novaSenha.equals(confirmarSenhaField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Erro", "A 'Nova Senha' e a 'Confirmação de Senha' não correspondem.");
                return;
            }
            usuarioLogado.setSenha(novaSenha);
            System.out.println("Senha do usuário " + usuarioLogado.getEmail() + " foi alterada.");
        }
        
        // Como o MockDB guarda a lista de usuários, ao alterar o objeto "usuarioLogado",
        // a alteração já está refletida na lista estática. Não é preciso um "save".
        System.out.println("Dados do usuário " + usuarioLogado.getEmail() + " foram atualizados.");
        
        showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Suas informações foram atualizadas com sucesso!");
        
        // Desabilita a edição novamente após salvar
        habilitarEdicaoCheckBox.setSelected(false);
        setarEditabilidade(false);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}