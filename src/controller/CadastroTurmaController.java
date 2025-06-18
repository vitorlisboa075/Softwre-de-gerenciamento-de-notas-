package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import model.*;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

public class CadastroTurmaController {

    /* ======================================================================
                                FXML
    ====================================================================== */
    @FXML private TextField  nomeTurmaField;
    @FXML private ComboBox<String> periodoCombo;

    // -------- Disciplinas -------------
    @FXML private TextField  pesquisaDisciplinaField;
    @FXML private TableView<DisciplinaWrapper> tabelaDisciplinas;
    @FXML private TableColumn<DisciplinaWrapper,Boolean> colSelecaoDisciplina;
    @FXML private TableColumn<DisciplinaWrapper,String>  colNomeDisciplina;

    // ---------- Alunos ----------------
    @FXML private TextField  pesquisaAlunoField;
    @FXML private TableView<UsuarioWrapper> tabelaAlunos;
    @FXML private TableColumn<UsuarioWrapper,Boolean> colSelecaoAluno;
    @FXML private TableColumn<UsuarioWrapper,String>  colNomeAluno,colCpfAluno;

    @FXML private Button btnVoltar;

    /* ======================================================================
                                Dados
    ====================================================================== */
    private final ObservableList<DisciplinaWrapper> masterDisciplinaList = FXCollections.observableArrayList();
    private final ObservableList<UsuarioWrapper>     masterUsuarioList   = FXCollections.observableArrayList();

    /* ======================================================================
                             Inicialização
    ====================================================================== */
    @FXML
    public void initialize(){
        periodoCombo.getItems().addAll("Matutino","Vespertino","Noturno","Integral");

        carregarDisciplinasDoBanco();
        carregarAlunosDoBanco();

        configurarTabelaDisciplinas();
        configurarTabelaAlunos();
    }

    /* ------------------------------------------------------------------ */
    private void configurarTabelaDisciplinas(){
        colSelecaoDisciplina.setCellValueFactory(cd->cd.getValue().selecionadoProperty());
        colSelecaoDisciplina.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecaoDisciplina));
        colSelecaoDisciplina.setEditable(true);

        colNomeDisciplina.setCellValueFactory(cd->cd.getValue().getDisciplina().nomeProperty());

        FilteredList<DisciplinaWrapper> filtrada =
                new FilteredList<>(masterDisciplinaList,p->true);
        pesquisaDisciplinaField.textProperty().addListener((o,ov,nv)->{
            String f = nv==null? "" : nv.toLowerCase();
            filtrada.setPredicate(dw->dw.getDisciplina().getNome().toLowerCase().contains(f));
        });
        tabelaDisciplinas.setItems(filtrada);
        tabelaDisciplinas.setEditable(true);
    }

    /* ------------------------------------------------------------------ */
    private void configurarTabelaAlunos(){
        colSelecaoAluno.setCellValueFactory(cd->cd.getValue().selecionadoProperty());
        colSelecaoAluno.setCellFactory(CheckBoxTableCell.forTableColumn(colSelecaoAluno));
        colSelecaoAluno.setEditable(true);

        colNomeAluno.setCellValueFactory(cd->cd.getValue().getUsuario().nomeProperty());
        colCpfAluno .setCellValueFactory(cd->cd.getValue().getUsuario().cpfProperty());

        FilteredList<UsuarioWrapper> somenteAlunos =
                new FilteredList<>(masterUsuarioList,
                                   uw->"Aluno".equalsIgnoreCase(uw.getUsuario().getTipoUsuario()));

        FilteredList<UsuarioWrapper> filtrada =
                new FilteredList<>(somenteAlunos,p->true);
        pesquisaAlunoField.textProperty().addListener((o,ov,nv)->{
            String f = nv==null? "" : nv.toLowerCase();
            filtrada.setPredicate(uw->
                     uw.getUsuario().getNome().toLowerCase().contains(f) ||
                     uw.getUsuario().getCpf().toLowerCase().contains(f));
        });
        tabelaAlunos.setItems(filtrada);
        tabelaAlunos.setEditable(true);
    }

    /* ======================================================================
                        BOTÃO  Salvar  (1 turma • N disciplinas)
    ====================================================================== */
    @FXML
    private void salvarTurma(){
        String nome    = nomeTurmaField.getText();
        String periodo = periodoCombo.getValue();

        if(nome==null||nome.isBlank()||periodo==null){
            showAlert(Alert.AlertType.WARNING,"Campos obrigatórios",
                      "Informe nome e período da turma.");
            return;
        }
        // disciplinas marcadas
        List<Disciplina> disciplinasSelecionadas = masterDisciplinaList.stream()
                .filter(DisciplinaWrapper::isSelecionado)
                .map(DisciplinaWrapper::getDisciplina)
                .collect(Collectors.toList());

        // alunos marcados
        List<Usuario> alunosSelecionados = masterUsuarioList.stream()
                .filter(w->w.isSelecionado() &&
                          "Aluno".equalsIgnoreCase(w.getUsuario().getTipoUsuario()))
                .map(UsuarioWrapper::getUsuario)
                .collect(Collectors.toList());

        if(disciplinasSelecionadas.isEmpty()||alunosSelecionados.isEmpty()){
            showAlert(Alert.AlertType.WARNING,"Seleção necessária",
                      "Marque ao menos 1 disciplina e 1 aluno.");
            return;
        }

        if(salvarTurmaBD(nome,periodo,disciplinasSelecionadas,alunosSelecionados)){
            showAlert(Alert.AlertType.INFORMATION,"Sucesso","Turma cadastrada.");
            limparCampos();
        }else{
            showAlert(Alert.AlertType.ERROR,"Erro","Falha ao cadastrar a turma.");
        }
    }

    /* ------------------------------------------------------------------ */
    /** Grava:  turmas  +  turma_disciplina  +  turma_aluno  */
    private boolean salvarTurmaBD(String nome,String periodo,
                                  List<Disciplina> disciplinas,
                                  List<Usuario> alunos){

        Connection conn = null;
        PreparedStatement psTurma = null, psTD = null, psTA = null;

        try{
            conn = Conexao.getConnection();
            if(conn==null){ return false; }
            conn.setAutoCommit(false);

            /* 1) TURMA -------------------------------------------------- */
            // usa a 1ª disciplina para preencher disciplina_id & professor_cpf
            Disciplina primeira = disciplinas.get(0);

            // (se disciplina_id / professor_cpf puderem ser NULL,
            //  troque insertTurma pelos campos que realmente sejam NOT NULL)
            String insertTurma = """
               INSERT INTO turmas (nome, disciplina_id, professor_cpf, periodo)
               VALUES (?,?,?,?)
            """;
            psTurma = conn.prepareStatement(insertTurma, Statement.RETURN_GENERATED_KEYS);
            psTurma.setString(1,nome);
            psTurma.setInt   (2,primeira.getId());
            psTurma.setString(3,primeira.getProfessor().getCpf());
            psTurma.setString(4,periodo);

            if(psTurma.executeUpdate()==0){
                conn.rollback(); return false;
            }
            int turmaId;
            try(ResultSet k = psTurma.getGeneratedKeys()){
                if(!k.next()){ conn.rollback(); return false; }
                turmaId = k.getInt(1);
            }

            /* 2) TURMA_DISCIPLINA  ------------------------------------- */
            String insertTD =
                "INSERT INTO turma_disciplina (turma_id, disciplina_id) VALUES (?,?)";
            psTD = conn.prepareStatement(insertTD);
            for(Disciplina d: disciplinas){
                psTD.setInt(1,turmaId);
                psTD.setInt(2,d.getId());
                psTD.addBatch();
            }
            psTD.executeBatch();

            /* 3) TURMA_ALUNO  ------------------------------------------ */
            String insertTA =
                "INSERT INTO turma_aluno (turma_id, aluno_cpf) VALUES (?,?)";
            psTA = conn.prepareStatement(insertTA);
            for(Usuario a: alunos){
                psTA.setInt(1,turmaId);
                psTA.setString(2,a.getCpf());
                psTA.addBatch();
            }
            psTA.executeBatch();

            conn.commit();
            return true;

        }catch(SQLException ex){
            ex.printStackTrace();
            if(conn!=null) try{ conn.rollback(); }catch(SQLException ignored){}
            return false;

        }finally{
            try{
                if(psTA   !=null) psTA.close();
                if(psTD   !=null) psTD.close();
                if(psTurma!=null) psTurma.close();
                if(conn   !=null){
                    conn.setAutoCommit(true);
                    Conexao.fecharConnection(conn);
                }
            }catch(SQLException ignored){}
        }
    }

    /* ======================================================================
                       Carregamento de dados (BD)
    ====================================================================== */
    private void carregarAlunosDoBanco(){
        masterUsuarioList.clear();
        String sql="SELECT cpf,nome,'Aluno' AS tipo FROM usuarios WHERE tipo='Aluno'";
        try(Connection c=Conexao.getConnection();
            PreparedStatement ps=c.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()){
            while(rs.next()){
                Usuario u=new Usuario();
                u.setCpf(rs.getString("cpf"));
                u.setNome(rs.getString("nome"));
                u.setTipoUsuario("Aluno");
                masterUsuarioList.add(new UsuarioWrapper(u));
            }
        }catch(SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"Erro","Falha ao carregar alunos.");
        }
    }

    private void carregarDisciplinasDoBanco(){
        masterDisciplinaList.clear();
        String sql="""
            SELECT d.id,d.nome,
                   p.cpf AS professor_cpf,
                   p.nome AS professor_nome
              FROM disciplinas d
              JOIN usuarios p ON p.cpf = d.professor_cpf
        """;
        try(Connection c=Conexao.getConnection();
            PreparedStatement ps=c.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()){
            while(rs.next()){
                Usuario prof=new Usuario();
                prof.setCpf (rs.getString("professor_cpf"));
                prof.setNome(rs.getString("professor_nome"));
                prof.setTipoUsuario("Professor");

                Disciplina d=new Disciplina();
                d.setId(rs.getInt("id"));
                d.setNome(rs.getString("nome"));
                d.setProfessor(prof);

                masterDisciplinaList.add(new DisciplinaWrapper(d));
            }
        }catch(SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"Erro","Falha ao carregar disciplinas.");
        }
    }

    /* ======================================================================
                             Outros botões
    ====================================================================== */
    @FXML private void limparCampos(){
        nomeTurmaField.clear();
        periodoCombo.getSelectionModel().clearSelection();
        pesquisaDisciplinaField.clear();
        pesquisaAlunoField.clear();
        masterDisciplinaList.forEach(dw->dw.selecionadoProperty().set(false));
        masterUsuarioList.forEach(uw->uw.selecionadoProperty().set(false));
    }

    @FXML private void voltar(ActionEvent e){
        btnVoltar.getScene().getWindow().hide();
    }

    /* ======================================================================
                                Util
    ====================================================================== */
    private void showAlert(Alert.AlertType t,String title,String msg){
        Alert a=new Alert(t);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }
}
