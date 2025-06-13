package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class DisciplinaWrapper {
    
    private final Disciplina disciplina;
    private final BooleanProperty selecionado = new SimpleBooleanProperty(false);

    public DisciplinaWrapper(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Disciplina getDisciplina() {
        return disciplina;
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