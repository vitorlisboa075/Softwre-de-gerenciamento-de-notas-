package main;

import controller.LoginController;
import controller.SecretariaController;
import model.Aluno;
import model.Curso;
import model.Disciplina;
import model.Turma;
import model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SoftwreDeGerenciamentoDeNotas {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        List<Usuario> usuarios = new ArrayList<>();
        List<Curso> cursos = new ArrayList<>();
        List<Disciplina> disciplinas = new ArrayList<>();
        List<Turma> turmas = new ArrayList<>();
        List<Aluno> alunos = new ArrayList<>();

        // Criar curso
        Curso cursoTI = new Curso(1, "Análise e Desenvolvimento de Sistemas", "Curso de TI", 2000, "Presencial");
        cursos.add(cursoTI);

        // Criar disciplina
        Disciplina disciplinaPOO = new Disciplina(1, "Programação Orientada a Objetos",
                "POO com Java", 80, "POO123", "Classes, objetos, herança", "2025.1", cursoTI);
        disciplinas.add(disciplinaPOO);

        // Criar turma
        Turma turma1 = new Turma(1, "Turma ADS 1A", "Primeiro semestre", "2025.1",
                cursoTI, disciplinaPOO, "Seg a Sex - Noite", "Sala 10");
        turmas.add(turma1);

        // Criar secretaria
        Usuario secretaria = new Usuario(1, "Maria Secretaria", "000.000.000-00",
                "secretaria@if.com", "feminino", "15/02/1985", "88888-0000",
                "Senha123!", "secretaria", "Rua das Secretarias", "101",
                "Sala 2", "Centro", "Cidade Y", "UF", "11111-111");
        usuarios.add(secretaria);

        // Login
        LoginController loginCtrl = new LoginController(usuarios, entrada);
        Usuario logado = loginCtrl.autenticarUsuario();

        if (logado != null && logado.isSecretaria()) {
            SecretariaController secretariaCtrl = new SecretariaController();
            secretariaCtrl.cadastrarAluno(entrada, alunos, cursos, turmas);
        } else {
            loginCtrl.direcionarMenu(logado);
        }

        entrada.close();
    }
}
