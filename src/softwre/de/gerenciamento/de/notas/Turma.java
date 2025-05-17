/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package softwre.de.gerenciamento.de.notas;

import java.util.ArrayList;
import java.util.List;

public class Turma {
    private int id;
    private String nome;
    private String descricao;
    private String semestre;
    private Curso curso; // Associação com o curso
    private List<Aluno> alunos; // Alunos matriculados na turma
    private String horarioAulas;
    private String sala;

    // Construtor
    public Turma(int id, String nome, String descricao, String semestre, Curso curso, String horarioAulas, String sala) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.semestre = semestre;
        this.curso = curso;
        this.horarioAulas = horarioAulas;
        this.sala = sala;
        this.alunos = new ArrayList<>();
    }
    
    // Construtor vazio
    public Turma() {}

    // Getters e Setters (agrupados para facilitar a visualização)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public List<Aluno> getAlunos() { return alunos; }

    public String getHorarioAulas() { return horarioAulas; }
    public void setHorarioAulas(String horarioAulas) { this.horarioAulas = horarioAulas; }

    public String getSala() { return sala; }
    public void setSala(String sala) { this.sala = sala; }

    // Métodos para gerenciar alunos
    public void adicionarAluno(Aluno aluno) {
        if (!alunos.contains(aluno)) {
            alunos.add(aluno);
        }
    }

    public void removerAluno(Aluno aluno) {
        alunos.remove(aluno);
    }

    public void listarAlunos() {
        System.out.println("Alunos da turma " + nome + ":");
        for (Aluno a : alunos) {
            System.out.println("- " + a.getNome());
        }
    }
}
