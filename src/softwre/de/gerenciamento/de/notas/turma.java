
package softwre.de.gerenciamento.de.notas;

import java.util.List;

 public class turma {

    private int id;
    private String nome;
    private String descricao;
    private String semestre;
    private curso curso;
    private List<aluno> alunos;
    private String horarioAulas;

    // Construtor
 
    public turma(int id, String nome, String descricao, String semestre, curso curso, List<aluno> alunos, String horarioAulas) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.semestre = semestre;
        this.curso = curso;
        this.alunos = alunos;
        this.horarioAulas = horarioAulas;
    }

    // Construtor vazio
    public turma() {}

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public curso getCurso() {
        return curso;
    }

    public void setCurso(curso curso) {
        this.curso = curso;
    }

    public List<aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<aluno> alunos) {
        this.alunos = alunos;
    }

    public String getHorarioAulas() {
        return horarioAulas;
    }

    public void setHorarioAulas(String horarioAulas) {
        this.horarioAulas = horarioAulas;
    }

    // Método para adicionar um aluno à turma

    /**
     *
     * @param aluno
     */
    public void adicionarAluno(aluno aluno) {
        if (alunos != null) {
            alunos.add(aluno);
        }
    }

    // Método para remover um aluno da turma
    public void removerAluno(aluno aluno) {
        if (alunos != null) {
            alunos.remove(aluno);
        }
    }

    private static class aluno {

        public aluno() {
        }
    }

 
}
