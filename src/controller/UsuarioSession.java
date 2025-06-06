package session;

public class UsuarioSession {
    private static final String role = "SECRETARIA"; // simulado

    public static boolean temPermissao(String... permissoes) {
        for (String p : permissoes) {
            if (p.equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }
}

