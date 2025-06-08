package model;

public class Disciplina {
    private int id;
    private String nome;
    private String descricao;
    private String codigo;
    private int cargaHoraria;
    private String ementa;
    private int semestre;
    private Curso curso;
    private Professor professor;

    // ✅ Construtor vazio
    public Disciplina(int par, String programação, String introdução_à_programação, int par1, String proG01, String ementa_básica, String string, Curso get, Professor get1) {
    }

    // ✅ Construtor completo
    public Disciplina(int id, String nome, String descricao, String codigo, int cargaHoraria, String ementa, int semestre, Curso curso, Professor professor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.codigo = codigo;
        this.cargaHoraria = cargaHoraria;
        this.ementa = ementa;
        this.semestre = semestre;
        this.curso = curso;
        this.professor = professor;
    }

    // ✅ Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public int getCargaHoraria() { return cargaHoraria; }
    public void setCargaHoraria(int cargaHoraria) { this.cargaHoraria = cargaHoraria; }

    public String getEmenta() { return ementa; }
    public void setEmenta(String ementa) { this.ementa = ementa; }

    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }

    @Override
    public String toString() {
        return nome;
    }
}
