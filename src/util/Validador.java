package util;

import java.util.regex.Pattern;

public class Validador {

    public static boolean validarEmail(String email) {
        return Pattern.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$", email);
    }

    public static boolean validarCPF(String cpf) {
        // opcional: lógica real de validação de CPF
        return cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }
}

