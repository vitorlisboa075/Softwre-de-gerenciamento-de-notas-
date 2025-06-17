package model;

import javafx.beans.property.*;

public class RelatorioAluno {

    private final StringProperty nome      = new SimpleStringProperty();
    private final StringProperty matricula = new SimpleStringProperty();
    private final IntegerProperty presencas = new SimpleIntegerProperty(0);
    private final DoubleProperty media     = new SimpleDoubleProperty(0.0);
    private final StringProperty situacao  = new SimpleStringProperty();

    public RelatorioAluno(String nome, String matricula,
                          int presencas, double media, String situacao) {
        this.nome.set(nome);
        this.matricula.set(matricula);
        this.presencas.set(presencas);
        this.media.set(media);
        this.situacao.set(situacao);
    }

    // Getters obrigatórios para o TableView funcionar com PropertyValueFactory
    public String getNome() {
        return nome.get();
    }

    public String getMatricula() {
        return matricula.get();
    }

    public int getPresencas() {
        return presencas.get();
    }

    public double getMedia() {
        return media.get();
    }

    public String getSituacao() {
        return situacao.get();
    }

    // Properties usadas pelo JavaFX
    public StringProperty nomeProperty() {
        return nome;
    }

    public StringProperty matriculaProperty() {
        return matricula;
    }

    public IntegerProperty presencasProperty() {
        return presencas;
    }

    public DoubleProperty mediaProperty() {
        return media;
    }

    public StringProperty situacaoProperty() {
        return situacao;
    }
}
