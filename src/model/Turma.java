// Local: src/model/Turma.java
package model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Turma {
    private int id;
    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty periodo = new SimpleStringProperty();
    
    private List<Disciplina> disciplinas;
    private List<Usuario> alunos; // MUDANÇA: Agora é uma lista de Usuario

    // Construtor
    public Turma(int id, String nome, String periodo) {
        this.id = id;
        this.setNome(nome);
        this.setPeriodo(periodo);
        this.disciplinas = new ArrayList<>();
        this.alunos = new ArrayList<>(); // Inicializa a lista de Usuario
    }

    // --- Getters e Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome.get(); }
    public void setNome(String nome) { this.nome.set(nome); }
    public StringProperty nomeProperty() { return nome; }

    public String getPeriodo() { return periodo.get(); }
    public void setPeriodo(String periodo) { this.periodo.set(periodo); }
    public StringProperty periodoProperty() { return periodo; }

    public List<Disciplina> getDisciplinas() { return disciplinas; }
    public void setDisciplinas(List<Disciplina> disciplinas) { this.disciplinas = disciplinas; }

    // Getters e Setters atualizados para Usuario
    public List<Usuario> getAlunos() { return alunos; }
    public void setAlunos(List<Usuario> alunos) { this.alunos = alunos; }

    // --- Métodos para gerenciar listas ---
    public void adicionarDisciplina(Disciplina disciplina) {
        if (!disciplinas.contains(disciplina)) {
            disciplinas.add(disciplina);
        }
    }

    public void adicionarAluno(Usuario aluno) {
        if (aluno.isAluno() && !alunos.contains(aluno)) {
            alunos.add(aluno);
        }
    }
}