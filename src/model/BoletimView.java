package model;

import javafx.beans.property.*;

public class BoletimView {

    /* ---------- Propriedades ---------- */
    private final StringProperty  disciplina  = new SimpleStringProperty();
    private final StringProperty  professor   = new SimpleStringProperty();
    private final DoubleProperty  nota1       = new SimpleDoubleProperty();
    private final DoubleProperty  nota2       = new SimpleDoubleProperty();
    private final DoubleProperty  nota3       = new SimpleDoubleProperty();
    private final DoubleProperty  media       = new SimpleDoubleProperty();
    private final IntegerProperty presencas   = new SimpleIntegerProperty();
    private final StringProperty  situacao    = new SimpleStringProperty();

    /* ---------- Construtor ---------- */
    public BoletimView(String disciplina,
                       String professor,
                       double nota1,
                       double nota2,
                       double nota3,
                       double media,
                       int presencas,
                       String situacao) {

        this.disciplina.set(disciplina);
        this.professor .set(professor);
        this.nota1     .set(nota1);
        this.nota2     .set(nota2);
        this.nota3     .set(nota3);
        this.media     .set(media);
        this.presencas .set(presencas);
        this.situacao  .set(situacao);
    }

    /* ---------- Getters (usados pelo TableView) ---------- */
    public String getDisciplina() { return disciplina.get(); }
    public String getProfessor () { return professor .get(); }
    public double getNota1     () { return nota1     .get(); }
    public double getNota2     () { return nota2     .get(); }
    public double getNota3     () { return nota3     .get(); }
    public double getMedia     () { return media     .get(); }
    public int    getPresencas () { return presencas .get(); }
    public String getSituacao  () { return situacao  .get(); }

    /* ---------- Properties (caso precise fazer bindings) ---------- */
    public StringProperty  disciplinaProperty() { return disciplina; }
    public StringProperty  professorProperty () { return professor ; }
    public DoubleProperty  nota1Property     () { return nota1     ; }
    public DoubleProperty  nota2Property     () { return nota2     ; }
    public DoubleProperty  nota3Property     () { return nota3     ; }
    public DoubleProperty  mediaProperty     () { return media     ; }
    public IntegerProperty presencasProperty () { return presencas ; }
    public StringProperty  situacaoProperty  () { return situacao  ; }
}
