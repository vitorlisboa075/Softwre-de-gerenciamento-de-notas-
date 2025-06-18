package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;  
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import model.Conexao;
import model.Usuario;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class GerenciarAlunoController implements Initializable {

    /* ---------- FXML ---------- */
    @FXML private TableView<Usuario>            tabelaAlunos;
    @FXML private TableColumn<Usuario,Boolean>  colSelecao;
    @FXML private TableColumn<Usuario,String>   colNome,colMatricula,colCpf,
                                                colEmail,colTelefone,colBairro,
                                                colSenha,colRua,colNumero,
                                                colCidade,colEstado,colCep,
                                                colComplemento;
    @FXML private TextField pesquisaCpfField;

    /* ---------- Dados ---------- */
    private final ObservableList<Usuario> listaAlunos = FXCollections.observableArrayList();

    /* ===================================================================== */
    /*  INIT                                                                 */
    /* ===================================================================== */
    @Override public void initialize(URL u, ResourceBundle rb){
        configurarColunasEditaveis();
        tabelaAlunos.setItems(listaAlunos);
        tabelaAlunos.setEditable(false);
        carregarAlunosDoBanco();
        pesquisaCpfField.textProperty().addListener((o,ov,nv)->filtrarPorCpf(nv));
    }

    /* ---------- Config columns ---------- */
    private void configurarColunasEditaveis(){
        configurar(colNome      ,Usuario::nomeProperty      ,Usuario::setNome);
        configurar(colMatricula ,Usuario::matriculaProperty ,Usuario::setMatricula);
        colCpf.setCellValueFactory(c->c.getValue().cpfProperty());
        configurar(colEmail     ,Usuario::emailProperty     ,Usuario::setEmail);
        configurar(colTelefone  ,Usuario::telefoneProperty  ,Usuario::setTelefone);
        configurar(colBairro    ,Usuario::bairroProperty    ,Usuario::setBairro);
        configurar(colSenha     ,Usuario::senhaProperty     ,Usuario::setSenha);
        configurar(colRua       ,Usuario::logradouroProperty,Usuario::setLogradouro);
        configurar(colNumero    ,Usuario::numeroProperty    ,Usuario::setNumero);
        configurar(colCidade    ,Usuario::cidadeProperty    ,Usuario::setCidade);
        configurar(colComplemento,Usuario::complementoProperty,Usuario::setComplemento);
        configurar(colEstado    ,Usuario::estadoProperty    ,Usuario::setEstado);
        configurar(colCep       ,Usuario::cepProperty       ,Usuario::setCep);

        colSelecao.setCellValueFactory(c->c.getValue().selectedProperty());
        colSelecao.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecao));
    }
    private void configurar(TableColumn<Usuario,String> col,
                            javafx.util.Callback<Usuario, javafx.beans.property.StringProperty> prop,
                            BiConsumer<Usuario,String> setter){
        col.setCellValueFactory(cd->prop.call(cd.getValue()));
        col.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        col.setOnEditCommit(evt->{
            Usuario u=evt.getRowValue();
            setter.accept(u,evt.getNewValue());
            salvarAlteracoes(u);
        });
    }

    /* ---------- Load + filter ---------- */
    private void carregarAlunosDoBanco(){
        listaAlunos.clear();
        String sql="""
            SELECT u.nome,a.matricula,u.cpf,u.email,u.telefone,u.senha,u.tipo,
                   u.rua,u.numero,u.complemento,u.bairro,u.cidade,u.estado,u.cep
            FROM usuarios u JOIN alunos a ON a.cpf=u.cpf
            WHERE u.tipo='aluno'
        """;
        try(Connection c=Conexao.getConnection();
            PreparedStatement ps=c.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()){
            while(rs.next()){
                Usuario u=new Usuario();
                u.setNome(rs.getString("nome"));
                u.setMatricula(rs.getString("matricula"));
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setTelefone(rs.getString("telefone"));
                u.setSenha(rs.getString("senha"));
                u.setTipoUsuario(rs.getString("tipo"));
                u.setLogradouro(rs.getString("rua"));
                u.setNumero(rs.getString("numero"));
                u.setComplemento(rs.getString("complemento"));
                u.setBairro(rs.getString("bairro"));
                u.setCidade(rs.getString("cidade"));
                u.setEstado(rs.getString("estado"));
                u.setCep(rs.getString("cep"));
                listaAlunos.add(u);
            }
        }catch(SQLException e){e.printStackTrace();}
    }
    private void filtrarPorCpf(String cpf){
        tabelaAlunos.setItems((cpf==null||cpf.isBlank())
            ? listaAlunos
            : listaAlunos.filtered(u->u.getCpf()!=null&&u.getCpf().contains(cpf)));
    }

    /* ---------- Update single ---------- */
    private void salvarAlteracoes(Usuario u){
        try{atualizarAlunoNoBanco(u);}
        catch(SQLException e){
            new Alert(Alert.AlertType.ERROR,"Erro ao salvar:\n"+e.getMessage()).showAndWait();
        }
    }
    private void atualizarAlunoNoBanco(Usuario u)throws SQLException{
        String upUser="""
           UPDATE usuarios SET nome=?,email=?,telefone=?,senha=?,rua=?,numero=?,
                               complemento=?,bairro=?,cidade=?,estado=?,cep=? WHERE cpf=?
        """;
        String upAluno="UPDATE alunos SET matricula=? WHERE cpf=?";
        try(Connection c=Conexao.getConnection()){
            c.setAutoCommit(false);
            try(PreparedStatement pu=c.prepareStatement(upUser);
                PreparedStatement pa=c.prepareStatement(upAluno)){
                pu.setString(1,u.getNome()); pu.setString(2,u.getEmail());
                pu.setString(3,u.getTelefone()); pu.setString(4,u.getSenha());
                pu.setString(5,u.getLogradouro()); pu.setString(6,u.getNumero());
                pu.setString(7,u.getComplemento()); pu.setString(8,u.getBairro());
                pu.setString(9,u.getCidade()); pu.setString(10,u.getEstado());
                pu.setString(11,u.getCep()); pu.setString(12,u.getCpf());
                pa.setString(1,u.getMatricula()); pa.setString(2,u.getCpf());
                pu.executeUpdate(); pa.executeUpdate(); c.commit();
            }catch(SQLException ex){c.rollback();throw ex;}
        }
    }

    /* ---------- UI actions ---------- */
    @FXML private void handleEditarAluno(){
        tabelaAlunos.setEditable(true);
        new Alert(Alert.AlertType.INFORMATION,
           "Modo edição ativado. Duplo‑clique e Enter para salvar.").showAndWait();
    }

    @FXML private void handleExcluirSelecionados(){
        ObservableList<Usuario> marcados=
            FXCollections.observableArrayList(listaAlunos.filtered(Usuario::isSelected));
        if(marcados.isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Nenhum aluno selecionado.").showAndWait();
            return;
        }
        Alert a=new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir "+marcados.size()+" aluno(s) selecionado(s)?");
        a.showAndWait().ifPresent(bt->{ if(bt==ButtonType.OK) excluirAlunos(marcados);});
    }

    /* ---------- Exclusão em lote (notas → presenças → alunos → usuários) -- */
    private void excluirAlunos(ObservableList<Usuario> lista){
        String delNotas="DELETE FROM notas     WHERE aluno_cpf = ?";
        String delPres ="DELETE FROM presencas WHERE aluno_cpf = ?";
        String delAlun ="DELETE FROM alunos    WHERE cpf       = ?";
        String delUser ="DELETE FROM usuarios  WHERE cpf       = ?";

        try(Connection c=Conexao.getConnection()){
            c.setAutoCommit(false);
            try(PreparedStatement psNotas=c.prepareStatement(delNotas);
                PreparedStatement psPres =c.prepareStatement(delPres);
                PreparedStatement psAlun =c.prepareStatement(delAlun);
                PreparedStatement psUser =c.prepareStatement(delUser)){
                for(Usuario u:lista){
                    psNotas.setString(1,u.getCpf()); psNotas.addBatch();
                    psPres .setString(1,u.getCpf()); psPres .addBatch();
                    psAlun .setString(1,u.getCpf()); psAlun .addBatch();
                    psUser .setString(1,u.getCpf()); psUser .addBatch();
                }
                psNotas.executeBatch();
                psPres .executeBatch();
                psAlun .executeBatch();
                psUser .executeBatch();
                c.commit();
                new Alert(Alert.AlertType.INFORMATION,"Alunos excluídos!").showAndWait();
            }catch(SQLException ex){c.rollback();throw ex;}
        }catch(SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Erro:\n"+e.getMessage()).showAndWait();
        }
        carregarAlunosDoBanco();
        filtrarPorCpf(pesquisaCpfField.getText());
    }
    
    @FXML
private void handleNovoAluno(ActionEvent event) {
    // Sua lógica aqui
}

}
