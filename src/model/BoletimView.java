// Local: src/model/BoletimView.java
package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BoletimView {

    private final StringProperty disciplina;
    private final StringProperty professor;
    private final DoubleProperty nota1;
    private final DoubleProperty nota2;
    private final DoubleProperty nota3;
    private final DoubleProperty media;
    private final IntegerProperty faltas;
    private final StringProperty situacao;

    // Construtor que recebe as informações prontas
    public BoletimView(String disciplina, String professor, double n1, double n2, double n3, int faltas) {
        this.disciplina = new SimpleStringProperty(disciplina);
        this.professor = new SimpleStringProperty(professor);
        this.nota1 = new SimpleDoubleProperty(n1);
        this.nota2 = new SimpleDoubleProperty(n2);
        this.nota3 = new SimpleDoubleProperty(n3);
        
        // Calcula a média
        double mediaCalculada = (n1 + n2 + n3) / 3.0;
        this.media = new SimpleDoubleProperty(mediaCalculada);
        
        this.faltas = new SimpleIntegerProperty(faltas);
        
        // Determina a situação
        this.situacao = new SimpleStringProperty(mediaCalculada >= 6.0 ? "Aprovado" : "Reprovado");
    }

    // --- Getters e Properties ---
    public String getDisciplina() { return disciplina.get(); }
    public StringProperty disciplinaProperty() { return disciplina; }

    public String getProfessor() { return professor.get(); }
    public StringProperty professorProperty() { return professor; }

    public double getNota1() { return nota1.get(); }
    public DoubleProperty nota1Property() { return nota1; }

    public double getNota2() { return nota2.get(); }
    public DoubleProperty nota2Property() { return nota2; }

    public double getNota3() { return nota3.get(); }
    public DoubleProperty nota3Property() { return nota3; }
    
    public double getMedia() { return media.get(); }
    public DoubleProperty mediaProperty() { return media; }

    public int getFaltas() { return faltas.get(); }
    public IntegerProperty faltasProperty() { return faltas; }

    public String getSituacao() { return situacao.get(); }
    public StringProperty situacaoProperty() { return situacao; }
}