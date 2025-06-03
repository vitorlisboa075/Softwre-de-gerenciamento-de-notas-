package model;

import javafx.beans.property.*;

public class RelatorioAluno {
    private final StringProperty nome;
    private final StringProperty matricula;
    private final IntegerProperty faltas;
    private final DoubleProperty media;
    private final StringProperty situacao;

    public RelatorioAluno(String nome, String matricula, int faltas, double media, String situacao) {
        this.nome = new SimpleStringProperty(nome);
        this.matricula = new SimpleStringProperty(matricula);
        this.faltas = new SimpleIntegerProperty(faltas);
        this.media = new SimpleDoubleProperty(media);
        this.situacao = new SimpleStringProperty(situacao);
    }

    public String getNome() { return nome.get(); }
    public StringProperty nomeProperty() { return nome; }

    public String getMatricula() { return matricula.get(); }
    public StringProperty matriculaProperty() { return matricula; }

    public int getFaltas() { return faltas.get(); }
    public IntegerProperty faltasProperty() { return faltas; }

    public double getMedia() { return media.get(); }
    public DoubleProperty mediaProperty() { return media; }

    public String getSituacao() { return situacao.get(); }
    public StringProperty situacaoProperty() { return situacao; }
}
