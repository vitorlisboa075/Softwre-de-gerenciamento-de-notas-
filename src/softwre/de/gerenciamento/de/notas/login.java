package softwre.de.gerenciamento.de.notas;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login {
    
    Scanner entrada = new Scanner(System.in);
    Usuario u = new Usuario();

    // Valida e-mail com expressão regular
    boolean validarEmail(String email) {
        String regexEmail = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Valida senha conforme critérios de segurança
    boolean validarSenha(String senha) {
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

    void criarLogin() {
        System.out.print("Vamos fazer seu cadastro  \nDigite seu e-mail: ");
        String email = entrada.nextLine();

        // Validação do e-mail
        while (!validarEmail(email)) {
            System.out.println("E-mail inválido. Digite um e-mail válido.");
            System.out.print("Digite seu e-mail: ");
            email = entrada.nextLine();
        }
        u.setEmail(email);

        System.out.print("CRIE sua senha: ");
        String senha = entrada.nextLine();

        // Validação da senha
        while (!validarSenha(senha)) {
            System.out.print("Digite uma nova senha: ");
            senha = entrada.nextLine();
        }
        u.setSenha(senha);
    }

    void fazerLogin() {
        System.out.print("Digite seu e-mail: ");
        String loginEmail = entrada.nextLine();

        System.out.print("Digite sua senha: ");
        String loginSenha = entrada.nextLine();

        boolean emailValido = loginEmail.equals(u.getEmail());
        boolean senhaValida = loginSenha.equals(u.getSenha());

        if (emailValido && senhaValida) {
            System.out.println("Login realizado com sucesso!");
        } else {
            System.out.println("E-mail ou senha incorretos.");
        }
    }
}