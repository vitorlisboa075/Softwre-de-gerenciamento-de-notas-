package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import model.Conexao;
import model.Disciplina;
import model.DisciplinaWrapper;
import model.Usuario;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Tela de gerenciamento de Disciplinas com edição inline.
 * Agora impede a exclusão de disciplinas que ainda estejam em uso por alguma turma.
 */
public class GerenciarDisciplinasController {

    /* ------------ FXML ------------ */
    @FXML private TextField                       pesquisaField;
    @FXML private TableView<DisciplinaWrapper>    tabelaDisciplinas;
    @FXML private TableColumn<DisciplinaWrapper,Boolean> colSelecao;
    @FXML private TableColumn<DisciplinaWrapper,String>  colNome,colProfessor;

    /* ------------ Dados ----------- */
    private final ObservableList<DisciplinaWrapper> masterDisciplinaList = FXCollections.observableArrayList();

    /* =================================================================== */
    /*  INIT                                                               */
    /* =================================================================== */
    @FXML public void initialize() {
        carregarDadosDoBanco();

        /* Coluna de seleção */
        colSelecao.setCellValueFactory(c -> c.getValue().selecionadoProperty());
        colSelecao.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecao));

        /* Coluna Nome — editável */
        colNome.setCellValueFactory(c -> c.getValue().getDisciplina().nomeProperty());
        colNome.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        colNome.setOnEditCommit(evt -> {
            DisciplinaWrapper dw = evt.getRowValue();
            String novo = evt.getNewValue();
            if (novo != null && !novo.trim().isEmpty() && !novo.equals(dw.getDisciplina().getNome())) {
                if (atualizarNomeDisciplinaNoBanco(dw.getDisciplina().getId(), novo)) {
                    dw.getDisciplina().setNome(novo);
                    tabelaDisciplinas.refresh();
                }
            }
        });
        tabelaDisciplinas.setEditable(true);

        /* Coluna Professor (somente leitura) */
        colProfessor.setCellValueFactory(c -> {
            Usuario p = c.getValue().getDisciplina().getProfessor();
            return new SimpleStringProperty(p != null ? p.getNome() : "—");
        });

        /* Filtro de busca */
        FilteredList<DisciplinaWrapper> filtrado = new FilteredList<>(masterDisciplinaList, p -> true);
        pesquisaField.textProperty().addListener((o,ov,nv)->{
            String f = nv==null ? "" : nv.toLowerCase();
            filtrado.setPredicate(dw -> dw.getDisciplina().getNome().toLowerCase().contains(f));
        });
        tabelaDisciplinas.setItems(filtrado);
    }

    /* =================================================================== */
    /*  DB helpers                                                          */
    /* =================================================================== */
    private void carregarDadosDoBanco() {
        masterDisciplinaList.clear();
        String sql = """
            SELECT d.id, d.nome,
                   p.cpf  AS professor_cpf,
                   p.nome AS professor_nome
              FROM disciplinas d
              LEFT JOIN usuarios p ON p.cpf = d.professor_cpf
            """;
        try (Connection c = Conexao.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                Usuario prof = null;
                if (rs.getString("professor_cpf") != null) {
                    prof = new Usuario();
                    prof.setCpf(rs.getString("professor_cpf"));
                    prof.setNome(rs.getString("professor_nome"));
                }
                masterDisciplinaList.add(new DisciplinaWrapper(new Disciplina(id, nome, prof)));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR,"Erro","Falha ao carregar disciplinas:\n"+e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean atualizarNomeDisciplinaNoBanco(int id, String novoNome){
        String sql="UPDATE disciplinas SET nome=? WHERE id=?";
        try(Connection c=Conexao.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,novoNome);
            ps.setInt(2,id);
            return ps.executeUpdate()>0;
        }catch(SQLException e){
            showAlert(Alert.AlertType.ERROR,"Erro","Falha ao atualizar:\n"+e.getMessage());
            return false;
        }
    }

    /* Verifica se existe ao menos 1 turma usando essa disciplina */
    private boolean disciplinaTemTurmas(int disciplinaId){
        String sql="SELECT 1 FROM turmas WHERE disciplina_id=? LIMIT 1";
        try(Connection c=Conexao.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,disciplinaId);
            return ps.executeQuery().next();  // true se encontrou
        }catch(SQLException e){
            e.printStackTrace();
            return true; // por segurança considera que tem vínculo
        }
    }

    /** Tenta excluir; pula (avisando) disciplinas que ainda têm turmas. */
    private void excluirDisciplinasDoBanco(List<DisciplinaWrapper> selecionadas){
        String del="DELETE FROM disciplinas WHERE id=?";
        int excluidas=0, bloqueadas=0;

        try(Connection c=Conexao.getConnection();
            PreparedStatement ps=c.prepareStatement(del)){
            for(DisciplinaWrapper dw: selecionadas){
                if(disciplinaTemTurmas(dw.getDisciplina().getId())){
                    bloqueadas++;
                    continue;
                }
                ps.setInt(1,dw.getDisciplina().getId());
                ps.addBatch();
            }
            ps.executeBatch();
            excluidas=ps.getUpdateCount(); // apenas indicativo
        }catch(SQLException e){
            showAlert(Alert.AlertType.ERROR,"Erro","Não foi possível excluir:\n"+e.getMessage());
            e.printStackTrace();
            return;
        }

        // feedback
        if(excluidas>0)
            showAlert(Alert.AlertType.INFORMATION,"Sucesso",
                      excluidas+" disciplina(s) excluída(s) com sucesso.");
        if(bloqueadas>0)
            showAlert(Alert.AlertType.WARNING,"Aviso",
                      bloqueadas+" disciplina(s) não puderam ser excluídas pois \nestão vinculadas a turmas.");
    }

    /* =================================================================== */
    /*  Botões                                                              */
    /* =================================================================== */
    @FXML private void handleNovaDisciplina(){
        showAlert(Alert.AlertType.INFORMATION,"Ação","(stub) Abrir tela de cadastro.");
    }

    @FXML private void handleEditarDisciplina(){
        List<DisciplinaWrapper> sel=getSelecionadas();
        if(sel.size()!=1){
            showAlert(Alert.AlertType.WARNING,"Seleção","Escolha UMA disciplina para editar."); return;
        }
        showAlert(Alert.AlertType.INFORMATION,"Editar",
                  "(stub) Abrir editor para "+sel.get(0).getDisciplina().getNome());
    }

    @FXML private void handleExcluirDisciplinas(){
        List<DisciplinaWrapper> sel=getSelecionadas();
        if(sel.isEmpty()){
            showAlert(Alert.AlertType.WARNING,"Seleção","Marque ao menos uma disciplina."); return;
        }
        Alert conf=new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir "+sel.size()+" disciplina(s)?",ButtonType.OK,ButtonType.CANCEL);
        if(conf.showAndWait().orElse(ButtonType.CANCEL)!=ButtonType.OK) return;

        excluirDisciplinasDoBanco(sel);
        /* remove apenas as que de fato sumiram do BD */
        masterDisciplinaList.removeIf(dw -> !disciplinaTemTurmas(dw.getDisciplina().getId())
                                            && !tabelaDisciplinas.getItems().contains(dw));
    }

    /* util */
    private List<DisciplinaWrapper> getSelecionadas(){
        return masterDisciplinaList.stream().filter(DisciplinaWrapper::isSelecionado).collect(Collectors.toList());
    }
    @FXML private void voltar(ActionEvent e){ /* navegação aqui */ }

    private void showAlert(Alert.AlertType t,String title,String msg){
        Alert a=new Alert(t); a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}
