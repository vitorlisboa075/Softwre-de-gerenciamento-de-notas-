package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.*;
import model.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class RegistrarPresencaController {

    /* ---------- FXML ---------- */
    @FXML private ComboBox<Turma>      turmaCombo;      // Combo de turmas
    @FXML private ComboBox<Disciplina> disciplinaCombo; // (opcional)
    @FXML private TableView<AlunoPresenca> tabelaAlunos;
    @FXML private TableColumn<AlunoPresenca,String>  colNome;
    @FXML private TableColumn<AlunoPresenca,String>  colMatricula;
    @FXML private TableColumn<AlunoPresenca,Boolean> colPresenca;
    @FXML private Button btnVoltar;

    /* ---------- Dados ---------- */
    private final ObservableList<AlunoPresenca> listaAlunos = FXCollections.observableArrayList();
    private final ObservableList<Turma>         turmas      = FXCollections.observableArrayList();

    /* ---------- INIT ---------- */
    @FXML
    public void initialize() {
        carregarTurmasDoBanco();

        /* --- Configura combo de turmas --- */
        turmaCombo.setItems(turmas);
        turmaCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Turma t){ return t==null?"":t.getNome(); }
            @Override public Turma fromString(String s){ return null; }
        });
        turmaCombo.getSelectionModel().selectedItemProperty().addListener((o,oldVal,newVal)->{
            if(newVal!=null){
                carregarDisciplinas(newVal);
                carregarAlunosDaTurma(newVal);      // <-- agora por ID
            }
        });

        /* --- Configura colunas da tabela --- */
        colNome     .setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colPresenca .setCellValueFactory(data -> data.getValue().presenteProperty());
        colPresenca .setCellFactory(CheckBoxTableCell.forTableColumn(colPresenca));

        tabelaAlunos.setItems(listaAlunos);
        tabelaAlunos.setEditable(true);
    }

    /* ---------- Carrega disciplinas da turma (se quiser exibir) ---------- */
    private void carregarDisciplinas(Turma turma){
        disciplinaCombo.setItems(FXCollections.observableArrayList(turma.getDisciplinas()));
        disciplinaCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Disciplina d){ return d==null?"":d.getNome(); }
            @Override public Disciplina fromString(String s){ return null; }
        });
    }

    /* ---------- Carrega alunos da turma ---------- */
    private void carregarAlunosDaTurma(Turma turma){
        listaAlunos.clear();
        String sql = """
            SELECT u.nome, a.matricula, a.cpf
            FROM alunos a
            JOIN usuarios u ON u.cpf = a.cpf
            JOIN turma_aluno ta ON ta.aluno_cpf = a.cpf
            WHERE ta.turma_id = ?
        """;
        try(Connection c = Conexao.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, turma.getId());
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    String nome       = rs.getString("nome");
                    String matricula  = rs.getString("matricula");
                    String cpf        = rs.getString("cpf");
                    listaAlunos.add(new AlunoPresenca(nome, matricula, cpf)); // presença default = false
                }
            }
        }catch(SQLException e){
            showAlert(Alert.AlertType.ERROR,"Erro","Falha ao listar alunos."); e.printStackTrace();
        }
    }

    /* ---------- SALVAR PRESENÇAS ---------- */
    @FXML
    private void salvarPresenca() {
        Turma turma = turmaCombo.getValue();
        if(turma==null){
            showAlert(Alert.AlertType.WARNING,"Seleção incompleta","Escolha uma turma.");
            return;
        }

        String sql = """
            INSERT INTO presencas (turma_id, aluno_cpf, data, presente)
            VALUES (?,?,?,?)
            ON DUPLICATE KEY UPDATE presente = VALUES(presente)
        """;
        try(Connection con = Conexao.getConnection()){
            con.setAutoCommit(false);
            try(PreparedStatement ps = con.prepareStatement(sql)){
                Date hoje = Date.valueOf(LocalDate.now());
                for(AlunoPresenca ap : listaAlunos){
                    ps.setInt   (1, turma.getId());
                    ps.setString(2, ap.getCpf());
                    ps.setDate  (3, hoje);
                    ps.setBoolean(4, ap.isPresente());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            con.commit();
            showAlert(Alert.AlertType.INFORMATION,"Sucesso","Presenças registradas.");
        }catch(SQLException e){
            showAlert(Alert.AlertType.ERROR,"Erro","Falha ao salvar presenças."); e.printStackTrace();
        }
    }

    /* ---------- Carrega turmas do banco ---------- */
    private void carregarTurmasDoBanco(){
        turmas.clear();
        String sql = """
            SELECT t.id, t.nome, t.periodo,
                   d.id AS disc_id, d.nome AS disc_nome
            FROM turmas t
            JOIN disciplinas d ON d.id = t.disciplina_id
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
                turmas.add(t);
            }
        }catch(SQLException e){
            showAlert(Alert.AlertType.ERROR,"Erro","Falha ao carregar turmas."); e.printStackTrace();
        }
    }

    /* ---------- Limpar ---------- */
    @FXML
    private void limparCampos() {
        turmaCombo     .getSelectionModel().clearSelection();
        disciplinaCombo.getSelectionModel().clearSelection();
        listaAlunos.clear();
    }

    /* ---------- Voltar (placeholder) ---------- */
    @FXML private void voltar(ActionEvent e){ /* fechar / navegar */ }

    /* ---------- Utils ---------- */
    private void showAlert(Alert.AlertType t,String title,String msg){
        Alert a=new Alert(t,msg,ButtonType.OK);
        a.setTitle(title); a.setHeaderText(null); a.showAndWait();
    }
}
