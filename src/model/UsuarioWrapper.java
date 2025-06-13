package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class UsuarioWrapper {

    private final Usuario usuario;
    private final BooleanProperty selecionado = new SimpleBooleanProperty(false);

    public UsuarioWrapper(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean isSelecionado() {
        return selecionado.get();
    }

    public BooleanProperty selecionadoProperty() {
        return selecionado;
    }
    
    // O método set é útil para o CheckBoxTableCell funcionar corretamente
    public void setSelecionado(boolean selecionado) {
        this.selecionado.set(selecionado);
    }
}