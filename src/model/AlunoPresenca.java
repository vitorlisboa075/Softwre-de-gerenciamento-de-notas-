package model;

import javafx.beans.property.*;

public class AlunoPresenca {

    private final StringProperty nome;
    private final StringProperty matricula;
    private final BooleanProperty presente;

    public AlunoPresenca(String nome, String matricula) {
        this.nome = new SimpleStringProperty(nome);
        this.matricula = new SimpleStringProperty(matricula);
        this.presente = new SimpleBooleanProperty(false);
    }

    public String getNome() { return nome.get(); }
    public StringProperty nomeProperty() { return nome; }

    public String getMatricula() { return matricula.get(); }
    public StringProperty matriculaProperty() { return matricula; }

    public boolean isPresente() { return presente.get(); }
    public BooleanProperty presenteProperty() { return presente; }

    public void setPresente(boolean presente) { this.presente.set(presente); }
}
