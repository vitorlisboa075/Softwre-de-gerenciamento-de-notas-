package model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class NotaAluno {

    private final Usuario aluno; // Referência ao aluno original
    private final StringProperty nome;
    private final StringProperty matricula;
    
    private final DoubleProperty nota1;
    private final DoubleProperty nota2;
    private final DoubleProperty nota3;
    private final ReadOnlyDoubleWrapper media;

    public NotaAluno(Usuario aluno) {
        this.aluno = aluno;
        this.nome = new SimpleStringProperty(aluno.getNome());
        this.matricula = new SimpleStringProperty(aluno.getMatricula());
        
        // Inicializa as notas
        this.nota1 = new SimpleDoubleProperty(0.0);
        this.nota2 = new SimpleDoubleProperty(0.0);
        this.nota3 = new SimpleDoubleProperty(0.0);

        // A Média é uma propriedade calculada que se atualiza sozinha
        this.media = new ReadOnlyDoubleWrapper();
        this.media.bind(Bindings.createDoubleBinding(
            () -> (getNota1() + getNota2() + getNota3()) / 3.0,
            nota1, nota2, nota3 // A média será recalculada se qualquer uma dessas notas mudar
        ));
    }

    // --- Getters e Properties ---

    public Usuario getAluno() { return aluno; }

    public String getNome() { return nome.get(); }
    public StringProperty nomeProperty() { return nome; }

    public String getMatricula() { return matricula.get(); }
    public StringProperty matriculaProperty() { return matricula; }

    public double getNota1() { return nota1.get(); }
    public void setNota1(double value) { nota1.set(value); }
    public DoubleProperty nota1Property() { return nota1; }

    public double getNota2() { return nota2.get(); }
    public void setNota2(double value) { nota2.set(value); }
    public DoubleProperty nota2Property() { return nota2; }

    public double getNota3() { return nota3.get(); }
    public void setNota3(double value) { nota3.set(value); }
    public DoubleProperty nota3Property() { return nota3; }

    public double getMedia() { return media.get(); }
    public ReadOnlyDoubleWrapper mediaProperty() { return media; }
}