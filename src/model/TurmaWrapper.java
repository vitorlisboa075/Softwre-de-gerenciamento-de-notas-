package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TurmaWrapper {
    
    private final Turma turma;
    private final BooleanProperty selecionado = new SimpleBooleanProperty(false);

    public TurmaWrapper(Turma turma) {
        this.turma = turma;
    }

    public Turma getTurma() {
        return turma;
    }

    public boolean isSelecionado() {
        return selecionado.get();
    }
    
    public void setSelecionado(boolean selecionado) {
        this.selecionado.set(selecionado);
    }

    public BooleanProperty selecionadoProperty() {
        return selecionado;
    }
}