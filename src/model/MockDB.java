package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockDB {

    // Nossa lista estática que atuará como um banco de dados de usuários
    private static final List<Usuario> usuarios = new ArrayList<>();

    // O bloco static é executado uma vez, quando a classe é carregada,
    // populando nosso "banco de dados" com dados iniciais.
    static {
        // Usuário Secretaria
        Usuario secretaria = new Usuario();
        secretaria.setNome("Fernanda Abreu");
        secretaria.setCpf("999.888.777-66");
        secretaria.setEmail("secretaria@if.com");
        secretaria.setTelefone("(11) 91111-2222");
        secretaria.setLogradouro("Rua da Diretoria, 10");
        secretaria.setSenha("Abcd1234!"); // Senha para login
        secretaria.setTipoUsuario("secretaria");
        
        // Usuário Professor
        Usuario professor = new Usuario();
        professor.setNome("Mariana Costa");
        professor.setCpf("444.555.666-77");
        professor.setEmail("professor@if.com");
        professor.setTelefone("(21) 93333-4444");
        professor.setLogradouro("Avenida dos Saberes, 20");
        professor.setSenha("Abcd1234!"); // Senha para login
        professor.setTipoUsuario("professor");

        // Usuário Aluno
        Usuario aluno = new Usuario();
        aluno.setNome("Carlos Souza");
        aluno.setCpf("111.222.333-44");
        aluno.setEmail("aluno@if.com");
        aluno.setTelefone("(31) 95555-6666");
        aluno.setLogradouro("Praça do Estudante, 30");
        aluno.setMatricula("2024001");
        // A senha inicial do aluno é o CPF (sem pontos ou traço)
        aluno.setSenha(aluno.getCpf().replaceAll("[^0-9]", "")); 
        aluno.setTipoUsuario("aluno");
        
        usuarios.add(secretaria);
        usuarios.add(professor);
        usuarios.add(aluno);
    }

    /**
     * Retorna um usuário com base no email, ou null se não encontrar.
     * @param email O email a ser buscado.
     * @return O objeto Usuario ou null.
     */
    public static Usuario findUsuarioByEmail(String email) {
        if (email == null) return null;
        
        for (Usuario u : usuarios) {
            if (email.equalsIgnoreCase(u.getEmail())) {
                return u;
            }
        }
        return null;
    }
}