package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import model.Usuario;
import model.UsuarioWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GerenciarAlunoController {

    @FXML private TextField pesquisaCpfField;
    @FXML private TableView<UsuarioWrapper> tabelaAlunos;
    @FXML private TableColumn<UsuarioWrapper, Boolean> colSelecao;
    @FXML private TableColumn<UsuarioWrapper, String> colNome;
    @FXML private TableColumn<UsuarioWrapper, String> colMatricula;
    @FXML private TableColumn<UsuarioWrapper, String> colCpf;
    @FXML private TableColumn<UsuarioWrapper, String> colEmail;
    @FXML private Button btnVoltar;

    // Lista que simula o banco de dados, contendo TODOS os usuários.
    private ObservableList<UsuarioWrapper> listaMestraDeUsuarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarDadosSimulados();

        // 1. Cria uma lista que mostra apenas os usuários do tipo "Aluno"
        FilteredList<UsuarioWrapper> listaDeAlunos = new FilteredList<>(
            listaMestraDeUsuarios,
            uw -> "Aluno".equalsIgnoreCase(uw.getUsuario().getTipoUsuario())
        );
        
        // 2. Configura as colunas da tabela
        colSelecao.setCellValueFactory(cellData -> cellData.getValue().selecionadoProperty());
        colSelecao.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecao));
        tabelaAlunos.setEditable(true);

        colNome.setCellValueFactory(cellData -> cellData.getValue().getUsuario().nomeProperty());
        colMatricula.setCellValueFactory(cellData -> cellData.getValue().getUsuario().matriculaProperty());
        colCpf.setCellValueFactory(cellData -> cellData.getValue().getUsuario().cpfProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().getUsuario().emailProperty());
        
        // 3. Configura a barra de pesquisa para filtrar a lista de alunos
        FilteredList<UsuarioWrapper> dadosFiltradosParaTabela = new FilteredList<>(listaDeAlunos, p -> true);
        
        pesquisaCpfField.textProperty().addListener((observable, oldValue, newValue) -> {
            dadosFiltradosParaTabela.setPredicate(alunoWrapper -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Mostra todos os alunos se a busca estiver vazia
                }
                // Filtra pelo CPF
                return alunoWrapper.getUsuario().getCpf().contains(newValue);
            });
        });

        // 4. Associa os dados finalmente filtrados à tabela
        tabelaAlunos.setItems(dadosFiltradosParaTabela);
    }
    
    private void carregarDadosSimulados() {
        // Dados de exemplo com diferentes tipos de usuário
        Usuario a1 = new Usuario();
        a1.setNome("Carlos Souza"); a1.setMatricula("2024001"); a1.setCpf("11122233344"); a1.setEmail("carlos@email.com"); a1.setTipoUsuario("Aluno");
        
        Usuario a2 = new Usuario();
        a2.setNome("Ana Pereira"); a2.setMatricula("2024002"); a2.setCpf("22233344455"); a2.setEmail("ana@email.com"); a2.setTipoUsuario("Aluno");
        
        Usuario p1 = new Usuario();
        p1.setNome("Mariana Costa"); p1.setCpf("44455566677"); p1.setTipoUsuario("Professor"); // Este não aparecerá na tabela
        
        listaMestraDeUsuarios.addAll(
            new UsuarioWrapper(a1), 
            new UsuarioWrapper(a2), 
            new UsuarioWrapper(p1)
        );
    }
    
    @FXML
    private void handleNovoAluno() {
        showAlert(Alert.AlertType.INFORMATION, "Ação", "Navegando para a tela de cadastro de usuários...");
        // A lógica de navegação já está no MenuPrincipalController
    }

    @FXML
    private void handleEditarAluno() {
        List<UsuarioWrapper> selecionados = getAlunosSelecionados();
        if (selecionados.size() != 1) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Por favor, selecione exatamente UM aluno para editar.");
            return;
        }
        Usuario alunoParaEditar = selecionados.get(0).getUsuario();
        showAlert(Alert.AlertType.INFORMATION, "Ação", "Abrindo formulário para editar: " + alunoParaEditar.getNome());
        // Aqui, você navegaria para a tela de cadastro, passando os dados do aluno.
    }

    @FXML
    private void handleExcluirAlunos() {
        List<UsuarioWrapper> selecionados = getAlunosSelecionados();
        if (selecionados.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Seleção Inválida", "Selecione pelo menos um aluno para excluir.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir " + selecionados.size() + " aluno(s)?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmação de Exclusão");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            // A exclusão ocorre na lista mestra, que é a fonte de todos os dados.
            // A tabela se atualizará automaticamente.
            listaMestraDeUsuarios.removeAll(selecionados);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Alunos excluídos com sucesso.");
        }
    }

    // Método auxiliar para pegar apenas os alunos selecionados na tabela
    private List<UsuarioWrapper> getAlunosSelecionados() {
        return tabelaAlunos.getItems().stream()
                .filter(UsuarioWrapper::isSelecionado)
                .collect(Collectors.toList());
    }

    @FXML
    private void voltar(ActionEvent event) {
        // A lógica de voltar ao menu é gerenciada pelo MenuPrincipalController
        System.out.println("Botão voltar clicado. A navegação é centralizada no Menu Principal.");
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}