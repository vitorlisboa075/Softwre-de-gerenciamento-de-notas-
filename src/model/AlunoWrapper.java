package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class AlunoWrapper {
    private Usuario aluno;
    private final BooleanProperty selecionado = new SimpleBooleanProperty(false);

    public AlunoWrapper(Usuario aluno) {
        this.aluno = aluno;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public BooleanProperty selecionadoProperty() {
        return selecionado;
    }

    public boolean isSelecionado() {
        return selecionado.get();
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado.set(selecionado);
    }
}
