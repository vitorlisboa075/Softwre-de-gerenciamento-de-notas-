package model;

import java.util.ArrayList;
import java.util.List;

public class Curso {
    private int id;
    private String nome;
    private String descricao;
    private int cargaHoraria;
    private String modalidade;

    private List<Usuario> professores; // Usuários do tipo "professor"

    // Construtor
    public Curso(int id, String nome, String descricao, int cargaHoraria, String modalidade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.cargaHoraria = cargaHoraria;
        this.modalidade = modalidade;
        this.professores = new ArrayList<>();
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getCargaHoraria() { return cargaHoraria; }
    public void setCargaHoraria(int cargaHoraria) { this.cargaHoraria = cargaHoraria; }

    public String getModalidade() { return modalidade; }
    public void setModalidade(String modalidade) { this.modalidade = modalidade; }

    public List<Usuario> getProfessores() { return professores; }

    // Métodos para adicionar e remover professores
    public void adicionarProfessor(Usuario professor) {
        if (professor.isProfessor()) {
            professores.add(professor);
        } else {
            System.out.println("Erro: Usuário não é um professor.");
        }
    }

    public void removerProfessor(Usuario professor) {
        professores.remove(professor);
    }

    // Método para exibir professores
    public void listarProfessores() {
        System.out.println("Professores do curso " + nome + ":");
        for (Usuario prof : professores) {
            System.out.println("- " + prof.getNome());
        }
    }
    
}

