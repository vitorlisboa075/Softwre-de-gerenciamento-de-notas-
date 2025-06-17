package model;

import javafx.beans.property.*;

public class NotaAluno {

    private final StringProperty nome       = new SimpleStringProperty();
    private final StringProperty matricula  = new SimpleStringProperty();
    private final StringProperty cpf        = new SimpleStringProperty();
    private final DoubleProperty nota1      = new SimpleDoubleProperty(0.0);
    private final DoubleProperty nota2      = new SimpleDoubleProperty(0.0);
    private final DoubleProperty nota3      = new SimpleDoubleProperty(0.0);
    private final DoubleProperty media      = new SimpleDoubleProperty(0.0);
    private final IntegerProperty idTurma   = new SimpleIntegerProperty();

    // Construtor principal
    public NotaAluno(String nome, String matricula, String cpf, int idTurma, double nota1, double nota2, double nota3) {
        this.nome.set(nome);
        this.matricula.set(matricula);
        this.cpf.set(cpf);
        this.idTurma.set(idTurma);
        this.nota1.set(nota1);
        this.nota2.set(nota2);
        this.nota3.set(nota3);
        calcularMedia();
    }

    public void calcularMedia() {
        double m = (nota1.get() + nota2.get() + nota3.get()) / 3.0;
        media.set(m);
    }

    // Getters
    public String getNome()        { return nome.get(); }
    public String getMatricula()   { return matricula.get(); }
    public String getCpf()         { return cpf.get(); }
    public int getIdTurma()        { return idTurma.get(); }
    public double getNota1()       { return nota1.get(); }
    public double getNota2()       { return nota2.get(); }
    public double getNota3()       { return nota3.get(); }
    public double getMedia()       { return media.get(); }

    // Setters de nota (recalcula média)
    public void setNota1(double v) { nota1.set(v); calcularMedia(); }
    public void setNota2(double v) { nota2.set(v); calcularMedia(); }
    public void setNota3(double v) { nota3.set(v); calcularMedia(); }

    // Propriedades para uso em TableView
    public StringProperty nomeProperty()       { return nome; }
    public StringProperty matriculaProperty()  { return matricula; }
    public StringProperty cpfProperty()        { return cpf; }
    public IntegerProperty idTurmaProperty()   { return idTurma; }
    public DoubleProperty nota1Property()      { return nota1; }
    public DoubleProperty nota2Property()      { return nota2; }
    public DoubleProperty nota3Property()      { return nota3; }
    public DoubleProperty mediaProperty()      { return media; }
}
