package service;

import java.util.List;
import model.Usuario;
import service.UsuarioRepository;

import java.util.regex.Pattern;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    public boolean emailUnico(String email, Long idAtual) {
        return usuarioRepository.verificarEmailUnico(email, idAtual);
    }

    public boolean senhaValida(String senha) {
        return senha.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    public boolean cpfValido(String cpf) {
        return cpf.matches("\\d{11}");
    }

    public void abrirFormulario(Usuario usuario) {
        // Lógica para abrir o formulário de criação/edição
    }

    public void excluir(Long id) {
        usuarioRepository.excluir(id);
    }

    public List<Usuario> buscarTodos() {
        return usuarioRepository.buscarTodos();
    }
}

