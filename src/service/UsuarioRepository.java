package repository;

import model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    private final List<Usuario> usuarios = new ArrayList<>();

    public List<Usuario> buscarTodos() {
        return new ArrayList<>(usuarios);
    }

    public void salvar(Usuario usuario) {
        if (usuario.getId() == 0) {
            usuario.setId((int) System.currentTimeMillis()); // Corrigido: sem cast
            usuarios.add(usuario);
        } else {
            excluir(usuario.getId());
            usuarios.add(usuario);
        }
    }

    public void excluir(long id) {
        usuarios.removeIf(u -> u.getId() == id);
    }

    public boolean verificarEmailUnico(String email, Long idAtual) {
        return usuarios.stream()
             .noneMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
