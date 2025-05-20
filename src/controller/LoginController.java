
package controller;

import java.util.List;
import java.util.Scanner;
import util.LoginUtils;
import model.Usuario;

public class LoginController {

    private List<Usuario> usuarios;
    private Scanner entrada;

    public LoginController(List<Usuario> usuarios, Scanner entrada) {
        this.usuarios = usuarios;
        this.entrada = entrada;
    }

    public Usuario autenticarUsuario() {
        System.out.println("=== LOGIN ===");

        System.out.print("Email: ");
        String email = entrada.nextLine();

        if (!LoginUtils.validarEmail(email)) {
            System.out.println("E-mail inválido.");
            return null;
        }

        System.out.print("Senha: ");
        String senha = entrada.nextLine();

        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                System.out.println("Bem-vindo(a), " + u.getNome() + "!\n");
                return u;
            }
        }

        System.out.println("E-mail ou senha incorretos.\n");
        return null;
    }

    public void direcionarMenu(Usuario usuarioLogado) {
        if (usuarioLogado == null) return;

        switch (usuarioLogado.getTipoUsuario().toLowerCase()) {
            case "admin" -> menuAdmin();
            case "secretaria" -> menuSecretaria();
            case "professor" -> menuProfessor();
            default -> System.out.println("Tipo de usuário desconhecido.");
        }
    }

    private void menuAdmin() {
        System.out.println("=== MENU ADMIN ===");
        System.out.println("1 - Cadastrar novo professor");
        System.out.println("2 - Cadastrar nova secretaria");
        System.out.println("3 - Listar usuários");
        System.out.println("(opções reais serão adicionadas posteriormente)");
    }

    private void menuSecretaria() {
        System.out.println("=== MENU SECRETARIA ===");
        System.out.println("[Simulação] Acesso ao cadastro de alunos, cursos, turmas e relatórios");
    }

    private void menuProfessor() {
        System.out.println("=== MENU PROFESSOR ===");
        System.out.println("[Simulação] Acesso à inserção de notas e presenças");
    }
}
