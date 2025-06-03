package util;

public class Sessao {

    private static String tipoUsuario;
    private static String email;

    public static void setTipoUsuario(String tipo) {
        tipoUsuario = tipo;
    }

    public static String getTipoUsuario() {
        return tipoUsuario;
    }

    public static void setEmail(String e) {
        email = e;
    }

    public static String getEmail() {
        return email;
    }

    public static void limpar() {
        tipoUsuario = null;
        email = null;
    }
}
