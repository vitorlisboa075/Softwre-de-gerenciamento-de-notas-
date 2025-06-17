package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import model.*;
import model.Conexao;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;

public class LancarNotasController {

    /* ---------- FXML ---------- */
    @FXML private ComboBox<Turma> turmaCombo;
    @FXML private ComboBox<Disciplina> disciplinaCombo;
    @FXML private Button btnCarregarAlunos;
    @FXML private TableView<NotaAluno> tabelaNotas;
    @FXML private TableColumn<NotaAluno,String>  colNome;
    @FXML private TableColumn<NotaAluno,String>  colMatricula;
    @FXML private TableColumn<NotaAluno,Double>  colNota1;
    @FXML private TableColumn<NotaAluno,Double>  colNota2;
    @FXML private TableColumn<NotaAluno,Double>  colNota3;
    @FXML private TableColumn<NotaAluno,Double>  colMedia;
    @FXML private Button btnVoltar;

    /* ---------- Dados ---------- */
    private final ObservableList<Turma>     turmasDisponiveis = FXCollections.observableArrayList();
    private final ObservableList<NotaAluno> alunosDaTurma     = FXCollections.observableArrayList();

    /* ---------- Init ----------- */
    @FXML
    public void initialize() {
        carregarTurmasDoBanco();

        turmaCombo.setItems(turmasDisponiveis);
        turmaCombo.setConverter(new TurmaStringConverter());

        turmaCombo.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            if (n!=null){
                disciplinaCombo.setItems(FXCollections.observableArrayList(n.getDisciplinas()));
                disciplinaCombo.setConverter(new DisciplinaStringConverter());
                tabelaNotas.getItems().clear();
            }
        });

        configurarTabela();
    }

    /* ---------- Config Tabela ---------- */
    private void configurarTabela(){
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));

        setNotaEditable(colNota1,"nota1", NotaAluno::nota1Property, NotaAluno::setNota1);
        setNotaEditable(colNota2,"nota2", NotaAluno::nota2Property, NotaAluno::setNota2);
        setNotaEditable(colNota3,"nota3", NotaAluno::nota3Property, NotaAluno::setNota3);

        colMedia.setCellValueFactory(c -> c.getValue().mediaProperty().asObject());
        formatarColunaMedia();

        tabelaNotas.setItems(alunosDaTurma);
        tabelaNotas.setEditable(true);
    }
    private void setNotaEditable(TableColumn<NotaAluno,Double> col,
                                 String campoSQL,
                                 Function<NotaAluno, javafx.beans.property.DoubleProperty> propGetter,
                                 TriConsumer<NotaAluno,Double> setter){
        col.setCellValueFactory(c -> propGetter.apply(c.getValue()).asObject());
        col.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        col.setOnEditCommit(evt->{
            NotaAluno na = evt.getRowValue();
            double nv    = evt.getNewValue()==null?0.0:evt.getNewValue();
            setter.accept(na,nv);
            updateNotaNoBanco(na,campoSQL,nv); // salva no banco
            tabelaNotas.refresh();
        });
    }

    /* ---------- Carregar Alunos ---------- */
    @FXML
    private void handleCarregarAlunos(){
        Turma t = turmaCombo.getValue();
        if (t==null){
            showAlert(Alert.AlertType.WARNING,"Seleção","Escolha uma turma.");
            return;
        }
        alunosDaTurma.clear();

        String sql = """
            SELECT u.cpf, u.nome,
                   n.nota1, n.nota2, n.nota3
            FROM turma_aluno ta
            JOIN usuarios u ON u.cpf = ta.aluno_cpf
            LEFT JOIN notas n ON n.aluno_cpf = u.cpf AND n.turma_id = ta.turma_id
            WHERE ta.turma_id = ?
        """;
        try(Connection c = Conexao.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,t.getId());
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    String cpf   = rs.getString("cpf");
                    String nome  = rs.getString("nome");
                    double n1    = rs.getDouble("nota1");
                    double n2    = rs.getDouble("nota2");
                    double n3    = rs.getDouble("nota3");

                    NotaAluno na = new NotaAluno(nome, cpf, cpf, t.getId(), n1, n2, n3);
                    alunosDaTurma.add(na);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"Erro","Falha ao carregar alunos.");
        }
    }

    /* ---------- Atualiza nota ---------- */
    private void updateNotaNoBanco(NotaAluno na, String campoSQL, double novaNota){
        Turma turma = turmaCombo.getValue();
        if (turma==null) return;

        String update = "UPDATE notas SET "+campoSQL+" = ? WHERE turma_id=? AND aluno_cpf=?";
        String insert = """
            INSERT INTO notas (turma_id, aluno_cpf, nota1, nota2, nota3)
            VALUES (?,?,?,?,?)
        """;

        try(Connection c = Conexao.getConnection()){
            // tenta UPDATE
            try(PreparedStatement ps = c.prepareStatement(update)){
                ps.setDouble(1,novaNota);
                ps.setInt   (2,turma.getId());
                ps.setString(3,na.getCpf());
                int linhas = ps.executeUpdate();

                if (linhas==0){
                    // registro não existe, faz INSERT com as três notas atuais
                    try(PreparedStatement ins = c.prepareStatement(insert)){
                        ins.setInt   (1,turma.getId());
                        ins.setString(2,na.getCpf());
                        ins.setDouble(3,na.getNota1());
                        ins.setDouble(4,na.getNota2());
                        ins.setDouble(5,na.getNota3());
                        ins.executeUpdate();
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"Erro","Não salvou no banco.");
        }
    }

    /* ---------- Turmas + disciplina ---------- */
    private void carregarTurmasDoBanco(){
        turmasDisponiveis.clear();
        String sql = """
            SELECT t.id, t.nome, t.periodo,
                   d.id AS disc_id, d.nome AS disc_nome
            FROM turmas t
            JOIN disciplinas d ON d.id = t.disciplina_id
            ORDER BY t.nome
        """;
        try(Connection c = Conexao.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                Turma t = new Turma(rs.getInt("id"),
                                    rs.getString("nome"),
                                    rs.getString("periodo"));
                Disciplina d = new Disciplina(rs.getInt("disc_id"),
                                              rs.getString("disc_nome"),
                                              null);
                t.setDisciplinas(List.of(d));
                turmasDisponiveis.add(t);
            }
        }catch(SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"Erro","Falha ao carregar turmas.");
        }
    }

    /* ---------- Utils ---------- */
    private void formatarColunaMedia(){
        DecimalFormat df = new DecimalFormat("#.##");
        colMedia.setCellFactory(tc->new TableCell<>(){
            @Override protected void updateItem(Double v, boolean empty){
                super.updateItem(v,empty);
                setText(empty||v==null?null:df.format(v));
            }
        });
    }
    @FXML private void voltar(ActionEvent e){ /* navegação / fechar */ }

    private void showAlert(Alert.AlertType t,String title,String msg){
        Alert a=new Alert(t,msg,ButtonType.OK);
        a.setTitle(title); a.setHeaderText(null); a.showAndWait();
    }

    /* ---------- Converters ------ */
    private static class TurmaStringConverter extends javafx.util.StringConverter<Turma>{
        @Override public String toString(Turma t){ return t==null?"":t.getNome(); }
        @Override public Turma fromString(String s){ return null; }
    }
    private static class DisciplinaStringConverter extends javafx.util.StringConverter<Disciplina>{
        @Override public String toString(Disciplina d){ return d==null?"":d.getNome(); }
        @Override public Disciplina fromString(String s){ return null; }
    }

    /* ---------- Func. Interface --- */
    @FunctionalInterface
    private interface TriConsumer<T,U>{
        void accept(T t, U u);
    }

    /* ---------- Botão salvar (opcional) ---------- */
    @FXML
    public void salvarNotas(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Salvar Notas", "Notas salvas com sucesso!");
    }
}
