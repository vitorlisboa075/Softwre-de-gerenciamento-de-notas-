package model;

import model.Curso;
import model.Disciplina;
import model.Professor;
import model.Turma;

import java.util.ArrayList;
import java.util.List;

public class MockDB {

    private static final List<Curso> cursos = new ArrayList<>();
    private static final List<Professor> professores = new ArrayList<>();
    private static final List<Disciplina> disciplinas = new ArrayList<>();
    private static final List<Turma> turmas = new ArrayList<>();

    static {
        // Inicializando cursos
        cursos.add(new Curso(1, "Sistemas de Informação", "SI01", 8, "Curso de Sistemas"));
        cursos.add(new Curso(2, "Engenharia de Software", "ES01", 8, "Curso de Engenharia"));

        // Inicializando professores
        professores.add(new Professor(1, "Carlos Silva"));
        professores.add(new Professor(2, "Ana Pereira"));

        // Inicializando disciplinas (id, nome, descricao, cargaHoraria, codigo, ementa, semestre (String), curso, professor)
        disciplinas.add(new Disciplina(1, "Programação", "Introdução à programação", 60, "PROG01", "Ementa básica", "2025.1", cursos.get(0), professores.get(0)));
        disciplinas.add(new Disciplina(2, "Banco de Dados", "Modelagem e SQL", 60, "BD01", "Ementa banco de dados", "2025.1", cursos.get(1), professores.get(1)));

        // Inicializando turmas (id, nome, descricao, semestre, curso, disciplina, horario, sala)
        turmas.add(new Turma(1, "Turma A", "Turma manhã", "2025.1", cursos.get(0), disciplinas.get(0), "08:00 - 10:00", "Sala 101"));
    }

    public static List<Curso> getCursos() {
        return cursos;
    }

    public static List<Professor> getProfessores() {
        return professores;
    }

    public static List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public static List<Turma> getTurmas() {
        return turmas;
    }

    // Verifica se há turmas ativas para uma disciplina (para impedir exclusão)
    public static boolean temTurmasAtivas(Disciplina disciplina) {
        for (Turma turma : turmas) {
            if (turma.getDisciplina() != null && turma.getDisciplina().getId() == disciplina.getId()) {
                return true;
            }
        }
        return false;
    }
}
