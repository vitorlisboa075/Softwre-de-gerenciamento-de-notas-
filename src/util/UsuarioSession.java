package util;

import model.Usuario;

public class UsuarioSession {

    private static Usuario usuarioLogado;

    // Define o usuário logado (chamada no momento do login)
    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    // Retorna o usuário logado
    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    // Retorna o ID do usuário logado (usado pelo ConfiguracoesController)
    public static long getIdUsuarioLogado() {
        return usuarioLogado != null ? usuarioLogado.getId() : -1;
    }

    // Verifica se o usuário tem permissão
    public static boolean temPermissao(String... permissoes) {
        if (usuarioLogado == null) return false;
        String role = usuarioLogado.getTipoUsuario();
        for (String p : permissoes) {
            if (p.equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }

    // Limpa a sessão (logout)
    public static void limparSessao() {
        usuarioLogado = null;
    }
}
