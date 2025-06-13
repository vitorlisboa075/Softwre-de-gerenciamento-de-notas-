// Local: src/model/Disciplina.java
package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Disciplina {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nome = new SimpleStringProperty();
    // AQUI ESTÁ A MUDANÇA: Agora associamos a um Usuario
    private final ObjectProperty<Usuario> professor = new SimpleObjectProperty<>();

    // Construtor
    public Disciplina(int id, String nome, Usuario professor) {
        this.setId(id);
        this.setNome(nome);
        this.setProfessor(professor);
    }
    
    public Disciplina() {}


    // --- Getters e Setters atualizados para Usuario ---
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNome() { return nome.get(); }
    public void setNome(String nome) { this.nome.set(nome); }
    public StringProperty nomeProperty() { return nome; }

    public Usuario getProfessor() { return professor.get(); }
    public void setProfessor(Usuario professor) { this.professor.set(professor); }
    public ObjectProperty<Usuario> professorProperty() { return professor; }

    @Override
    public String toString() {
        return getNome();
    }
}