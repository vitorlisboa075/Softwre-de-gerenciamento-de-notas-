package util;

public class Sessao {
    private static String email;
    private static String tipoUsuario;
    private static String cpf;

    public static void setEmail(String e) { email = e; }
    public static void setTipoUsuario(String tipo) { tipoUsuario = tipo; }
    public static void setCpf(String c) { cpf = c; }

    public static String getEmail() { return email; }
    public static String getTipoUsuario() { return tipoUsuario; }
    public static String getCpf() { return cpf; }

    public static void limparSessao() {
        email = null;
        tipoUsuario = null;
        cpf = null;
    }
}
