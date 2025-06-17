package model;

import javafx.beans.property.*;

public class AlunoPresenca {

    private final StringProperty nome       = new SimpleStringProperty();
    private final StringProperty matricula  = new SimpleStringProperty();
    private final StringProperty cpf        = new SimpleStringProperty();
    private final BooleanProperty presente  = new SimpleBooleanProperty(false);

    public AlunoPresenca(String nome, String matricula, String cpf) {
        this.nome.set(nome);
        this.matricula.set(matricula);
        this.cpf.set(cpf);
    }

    /* --- getters --- */
    public String getNome()       { return nome.get(); }
    public String getMatricula()  { return matricula.get(); }
    public String getCpf()        { return cpf.get(); }
    public boolean isPresente()   { return presente.get(); }

    /* --- setters --- */
    public void setPresente(boolean v) { presente.set(v); }

    /* --- properties (TableView) --- */
    public StringProperty  nomeProperty()      { return nome; }
    public StringProperty  matriculaProperty() { return matricula; }
    public StringProperty  cpfProperty()       { return cpf; }
    public BooleanProperty presenteProperty()  { return presente; }
}
