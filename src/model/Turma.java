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
    private List<Usuario> alunos;

    /* -------- Construtores -------- */
    // Construtor vazio – necessário para new Turma()
    public Turma() {
        this.disciplinas = new ArrayList<>();
        this.alunos = new ArrayList<>();
    }

    // Construtor completo
    public Turma(int id, String nome, String periodo) {
        this();                 // reaproveita inicialização das listas
        this.id = id;
        setNome(nome);
        setPeriodo(periodo);
    }

    /* -------- Getters / Setters -------- */
    public int getId()         { return id; }
    public void setId(int id)  { this.id = id; }

    public String getNome()    { return nome.get(); }
    public void setNome(String n) { nome.set(n); }
    public StringProperty nomeProperty() { return nome; }

    public String getPeriodo() { return periodo.get(); }
    public void setPeriodo(String p) { periodo.set(p); }
    public StringProperty periodoProperty() { return periodo; }

    public List<Disciplina> getDisciplinas()           { return disciplinas; }
    public void setDisciplinas(List<Disciplina> list)   { disciplinas = list; }

    public List<Usuario> getAlunos()                   { return alunos; }
    public void setAlunos(List<Usuario> list)           { alunos = list; }

    /* -------- Métodos utilitários -------- */
    public void adicionarDisciplina(Disciplina d) {
        if (!disciplinas.contains(d)) disciplinas.add(d);
    }

    public void adicionarAluno(Usuario u) {
        if (u.isAluno() && !alunos.contains(u)) alunos.add(u);
    }
}
