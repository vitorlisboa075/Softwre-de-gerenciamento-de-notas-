package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginUtils {

    // Valida e-mail com expressão regular
    public static boolean validarEmail(String email) {
        String regexEmail = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Valida senha conforme critérios de segurança
    public static boolean validarSenha(String senha) {
        if (senha.length() < 8) {
            System.out.println("A senha deve ter pelo menos 8 caracteres.");
            return false;
        }
        if (!senha.matches(".*[A-Z].*")) {
            System.out.println("A senha deve conter pelo menos uma letra MAIÚSCULA.");
            return false;
        }
        if (!senha.matches(".*[a-z].*")) {
            System.out.println("A senha deve conter pelo menos uma letra minúscula.");
            return false;
        }
        if (!senha.matches(".*\\d.*")) {
            System.out.println("A senha deve conter pelo menos um número.");
            return false;
        }
        if (!senha.matches(".*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/].*")) {
            System.out.println("A senha deve conter pelo menos um caractere especial.");
            return false;
        }
        return true;
    }
}
