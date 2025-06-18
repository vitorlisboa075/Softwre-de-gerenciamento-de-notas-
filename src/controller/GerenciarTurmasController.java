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
import model.*;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class GerenciarTurmasController {

    /* ----------- FXML ----------- */
    @FXML private TextField                       pesquisaField;
    @FXML private TableView<TurmaWrapper>         tabelaTurmas;
    @FXML private TableColumn<TurmaWrapper,Boolean> colSelecao;
    @FXML private TableColumn<TurmaWrapper,String>  colNome,colPeriodo;

    @FXML private TableView<AlunoWrapper>           tabelaAlunos;
    @FXML private TableColumn<AlunoWrapper,Boolean> colSelAluno;
    @FXML private TableColumn<AlunoWrapper,String>  colNomeAluno,colCpfAluno;

    /* ----------- Dados ---------- */
    private final ObservableList<TurmaWrapper> masterTurmaList = FXCollections.observableArrayList();
    private final ObservableList<AlunoWrapper> alunosDaTurma   = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarDadosDoBanco();

        colSelecao.setCellValueFactory(c -> c.getValue().selecionadoProperty());
        colSelecao.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecao));
        colSelecao.setEditable(true);

        configurarColunaEditavel(colNome,    "nome");
        configurarColunaEditavel(colPeriodo, "periodo");

        tabelaTurmas.setEditable(true);

        FilteredList<TurmaWrapper> filtradas = new FilteredList<>(masterTurmaList, p -> true);
        pesquisaField.textProperty().addListener((o,ov,nv)-> {
            String f = nv==null? "": nv.toLowerCase();
            filtradas.setPredicate(tw -> tw.getTurma().getNome().toLowerCase().contains(f));
        });
        tabelaTurmas.setItems(filtradas);
        tabelaAlunos.setEditable(true);


        tabelaTurmas.getSelectionModel().selectedItemProperty().addListener(
            (o,ov,nv)-> { if(nv!=null) carregarAlunosDaTurma(nv.getTurma().getId()); else alunosDaTurma.clear(); });

        colSelAluno.setCellValueFactory(a -> a.getValue().selecionadoProperty());
        colSelAluno.setCellFactory(CheckBoxTableCell.forTableColumn(colSelAluno));
        colSelAluno.setEditable(true);

        colNomeAluno.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getAluno().getNome()));
        colCpfAluno .setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getAluno().getCpf()));
        tabelaAlunos.setItems(alunosDaTurma);
    }

    private void configurarColunaEditavel(TableColumn<TurmaWrapper,String> col, String campoBD){
        col.setCellValueFactory(c -> {
            if(campoBD.equals("nome"))    return c.getValue().getTurma().nomeProperty();
            else                          return c.getValue().getTurma().periodoProperty();
        });
        col.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        col.setOnEditCommit(evt -> {
            TurmaWrapper tw = evt.getRowValue();
            String novo  = evt.getNewValue();
            String atual = campoBD.equals("nome") ? tw.getTurma().getNome() : tw.getTurma().getPeriodo();
            if(novo!=null && !novo.trim().isEmpty() && !novo.equals(atual)){
                if(atualizarCampo(campoBD, novo, tw.getTurma().getId())){
                    if(campoBD.equals("nome")) tw.getTurma().setNome(novo);
                    else                       tw.getTurma().setPeriodo(novo);
                    tabelaTurmas.refresh();
                }
            }
        });
    }

    private void carregarDadosDoBanco(){
        masterTurmaList.clear();
        String sql = "SELECT id,nome,periodo FROM turmas";
        try(Connection c = Conexao.getConnection();
            Statement  st= c.createStatement();
            ResultSet  rs= st.executeQuery(sql)){
            while(rs.next()){
                Turma t = new Turma();
                t.setId(rs.getInt("id"));
                t.setNome(rs.getString("nome"));
                t.setPeriodo(rs.getString("periodo"));
                masterTurmaList.add(new TurmaWrapper(t));
            }
        }catch(SQLException e){ showAlert(Alert.AlertType.ERROR,"Erro",e.getMessage()); }
    }

    private void carregarAlunosDaTurma(int id){
        alunosDaTurma.clear();
        String sql = """
            SELECT u.cpf,u.nome
            FROM turma_aluno ta
            JOIN usuarios u ON u.cpf = ta.aluno_cpf
            WHERE ta.turma_id = ?""";
        try(Connection c = Conexao.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Usuario al = new Usuario();
                al.setCpf(rs.getString("cpf"));
                al.setNome(rs.getString("nome"));
                alunosDaTurma.add(new AlunoWrapper(al));
            }
        }catch(SQLException e){ showAlert(Alert.AlertType.ERROR,"Erro",e.getMessage()); }
    }

    private boolean atualizarCampo(String col,String val,int id){
        String sql="UPDATE turmas SET "+col+"=? WHERE id=?";
        try(Connection c=Conexao.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,val); ps.setInt(2,id);
            return ps.executeUpdate()>0;
        }catch(SQLException e){ showAlert(Alert.AlertType.ERROR,"Erro",e.getMessage()); return false;}
    }

    @FXML private void handleExcluirTurmas(ActionEvent e){
        List<TurmaWrapper> sel = masterTurmaList.stream()
                                   .filter(TurmaWrapper::isSelecionado)
                                   .collect(Collectors.toList());
        if(sel.isEmpty()){
            showAlert(Alert.AlertType.WARNING,"Aviso","Marque pelo menos uma turma.");
            return;
        }
        String msg = "Excluir "+sel.size()+" turma(s) selecionada(s)?";
        if(new Alert(Alert.AlertType.CONFIRMATION,msg,ButtonType.OK,ButtonType.CANCEL)
              .showAndWait().orElse(ButtonType.CANCEL)!=ButtonType.OK) return;

        try{
            excluirTurmasDoBanco(sel);
            masterTurmaList.removeAll(sel);
            alunosDaTurma.clear();
            showAlert(Alert.AlertType.INFORMATION,"Sucesso","Turmas excluídas.");
        }catch(SQLException ex){
            showAlert(Alert.AlertType.ERROR,"Erro ao excluir",ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void excluirTurmasDoBanco(List<TurmaWrapper> turmas) throws SQLException{
        String delNotas      = "DELETE FROM notas      WHERE turma_id = ?";
        String delPresencas  = "DELETE FROM presencas  WHERE turma_id = ?";
        String delTurmaAluno = "DELETE FROM turma_aluno WHERE turma_id = ?";
        String delTurma      = "DELETE FROM turmas     WHERE id       = ?";

        try(Connection c = Conexao.getConnection()){
            c.setAutoCommit(false);
            try(PreparedStatement psNotas     = c.prepareStatement(delNotas);
                PreparedStatement psPres      = c.prepareStatement(delPresencas);
                PreparedStatement psTurAl     = c.prepareStatement(delTurmaAluno);
                PreparedStatement psTurma     = c.prepareStatement(delTurma)){

                for(TurmaWrapper tw: turmas){
                    int id = tw.getTurma().getId();
                    psNotas.setInt(1,id); psNotas.addBatch();
                    psPres .setInt(1,id); psPres .addBatch();
                    psTurAl.setInt(1,id); psTurAl.addBatch();
                    psTurma.setInt(1,id); psTurma.addBatch();
                }
                psNotas.executeBatch();
                psPres .executeBatch();
                psTurAl.executeBatch();
                psTurma.executeBatch();
                c.commit();
            }catch(SQLException ex){ c.rollback(); throw ex; }
        }
    }

    @FXML private void handleAdicionarAluno(){
        TurmaWrapper selTur = tabelaTurmas.getSelectionModel().getSelectedItem();
        if(selTur==null){ showAlert(Alert.AlertType.WARNING,"Turma","Selecione uma turma."); return;}

        List<Usuario> disponiveis = getAlunosDisponiveis(selTur.getTurma().getId());
        if(disponiveis.isEmpty()){ showAlert(Alert.AlertType.INFORMATION,"Info","Nenhum aluno disponível."); return;}

        Map<String, Usuario> mapa = new LinkedHashMap<>();
        for (Usuario u : disponiveis) {
            mapa.put(u.getNome()+" (CPF "+u.getCpf()+")", u);
        }

        ChoiceDialog<String> dlg = new ChoiceDialog<>(null, mapa.keySet());
        dlg.setTitle("Adicionar Aluno");
        dlg.setHeaderText("Escolha o aluno para adicionar à turma "+selTur.getTurma().getNome());
        dlg.setContentText("Aluno:");
        dlg.showAndWait().ifPresent(chave->{
            Usuario u = mapa.get(chave);
            if(u!=null && adicionarAlunoNaTurma(selTur.getTurma().getId(),u.getCpf())){
                showAlert(Alert.AlertType.INFORMATION,"Sucesso","Aluno adicionado.");
                carregarAlunosDaTurma(selTur.getTurma().getId());
            }else{
                showAlert(Alert.AlertType.ERROR,"Erro","Não foi possível adicionar.");
            }
        });
    }

    private List<Usuario> getAlunosDisponiveis(int turmaId) {
        List<Usuario> lista = new ArrayList<>();
        String sql = """
            SELECT u.cpf, u.nome
            FROM usuarios u
            JOIN alunos  a ON a.cpf = u.cpf
            WHERE u.tipo = 'aluno'
              AND u.cpf NOT IN (SELECT aluno_cpf FROM turma_aluno WHERE turma_id = ?)
        """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, turmaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario aluno = new Usuario();
                aluno.setCpf(rs.getString("cpf"));
                aluno.setNome(rs.getString("nome"));
                lista.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Banco", "Falha ao buscar alunos disponíveis.");
        }
        return lista;
    }

    private boolean adicionarAlunoNaTurma(int turmaId,String cpf){
        String sql="INSERT INTO turma_aluno(turma_id,aluno_cpf) VALUES(?,?)";
        try(Connection c=Conexao.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,turmaId); ps.setString(2,cpf);
            return ps.executeUpdate()>0;
        }catch(SQLException e){e.printStackTrace(); return false;}
    }

    @FXML private void handleRemoverAluno(){
        TurmaWrapper selTur = tabelaTurmas.getSelectionModel().getSelectedItem();
        List<AlunoWrapper> selAlunos = alunosDaTurma.stream()
                                    .filter(AlunoWrapper::isSelecionado).toList();
        if(selTur==null||selAlunos.isEmpty()){
            showAlert(Alert.AlertType.WARNING,"Aviso","Selecione turma e aluno(s)."); return;}
        String sql="DELETE FROM turma_aluno WHERE turma_id=? AND aluno_cpf=?";
        try(Connection c=Conexao.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            for(AlunoWrapper aw: selAlunos){
                ps.setInt(1,selTur.getTurma().getId());
                ps.setString(2,aw.getAluno().getCpf());
                ps.addBatch();
            }
            ps.executeBatch();
            alunosDaTurma.removeAll(selAlunos);
            showAlert(Alert.AlertType.INFORMATION,"Sucesso","Aluno(s) removido(s).");
        }catch(SQLException e){ showAlert(Alert.AlertType.ERROR,"Erro",e.getMessage()); }
    }

    private void showAlert(Alert.AlertType t,String title,String msg){
        Alert a=new Alert(t); a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}
