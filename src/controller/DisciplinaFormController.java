package controller;

import model.MockDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Curso;
import model.Disciplina;
import model.Professor;

import java.util.List;

public class DisciplinaFormController {

    @FXML private TextField nomeField;
    @FXML private TextArea descricaoField;
    @FXML private TextField codigoField;
    @FXML private TextField cargaHorariaField;
    @FXML private TextArea ementaField;
    @FXML private TextField semestreField;
    @FXML private ComboBox<Curso> cursoCombo;
    @FXML private ComboBox<Professor> professorCombo;

    private Disciplina disciplina;
    private ObservableList<Disciplina> listaDisciplinas;

    private List<Curso> cursos;
    private List<Professor> professores;

    public void setDisciplina(Disciplina disciplina, ObservableList<Disciplina> listaDisciplinas) {
        this.listaDisciplinas = listaDisciplinas;

        // ✅ Carregar listas
        cursos = MockDB.getCursos();
        professores = MockDB.getProfessores();

        cursoCombo.setItems(FXCollections.observableArrayList(cursos));
        professorCombo.setItems(FXCollections.observableArrayList(professores));

        if (disciplina == null) {
            // ✅ Agora você pode acessar cursos e professores aqui
            this.disciplina = new Disciplina(0, "", "", "", 0, "", 0, cursos.get(0), professores.get(0));
        } else {
            this.disciplina = disciplina;

            nomeField.setText(disciplina.getNome());
            descricaoField.setText(disciplina.getDescricao());
            codigoField.setText(disciplina.getCodigo());
            cargaHorariaField.setText(String.valueOf(disciplina.getCargaHoraria()));
            ementaField.setText(disciplina.getEmenta());
            semestreField.setText(String.valueOf(disciplina.getSemestre()));
            cursoCombo.setValue(disciplina.getCurso());
            professorCombo.setValue(disciplina.getProfessor());
        }
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    @FXML
    private void onSalvar() {
        try {
            String nome = nomeField.getText();
            String descricao = descricaoField.getText();
            String codigo = codigoField.getText();
            int cargaHoraria = Integer.parseInt(cargaHorariaField.getText());
            String ementa = ementaField.getText();
            int semestre = Integer.parseInt(semestreField.getText());
            Curso curso = cursoCombo.getValue();
            Professor professor = professorCombo.getValue();

            if (nome.isEmpty() || codigo.isEmpty() || curso == null || professor == null) {
                showAlert("Preencha todos os campos obrigatórios.");
                return;
            }

            for (Disciplina d : listaDisciplinas) {
                if (d != disciplina && d.getCodigo().equalsIgnoreCase(codigo)) {
                    showAlert("Código da disciplina já existe.");
                    return;
                }
            }

            disciplina.setNome(nome);
            disciplina.setDescricao(descricao);
            disciplina.setCodigo(codigo);
            disciplina.setCargaHoraria(cargaHoraria);
            disciplina.setEmenta(ementa);
            disciplina.setSemestre(semestre);
            disciplina.setCurso(curso);
            disciplina.setProfessor(professor);

        } catch (NumberFormatException e) {
            showAlert("Carga Horária e Semestre devem ser numéricos.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
