package controller;

import model.Aluno;
import model.Curso;
import model.Disciplina;
import model.Turma;

import java.util.List;
import java.util.Scanner;

public class SecretariaController {

    public void cadastrarAluno(Scanner entrada, List<Aluno> listaAlunos, List<Curso> cursos, List<Turma> turmas) {
        System.out.println("=== CADASTRO DE ALUNO ===");

        System.out.print("Nome: ");
        String nome = entrada.nextLine();

        System.out.print("CPF: ");
        String cpf = entrada.nextLine();

        System.out.print("Email: ");
        String email = entrada.nextLine();

        System.out.print("Telefone: ");
        String telefone = entrada.nextLine();

        System.out.println("\nENDEREÇO:");
        System.out.print("Logradouro: ");
        String logradouro = entrada.nextLine();
        System.out.print("Número: ");
        String numero = entrada.nextLine();
        System.out.print("Complemento: ");
        String complemento = entrada.nextLine();
        System.out.print("Bairro: ");
        String bairro = entrada.nextLine();
        System.out.print("Cidade: ");
        String cidade = entrada.nextLine();
        System.out.print("Estado: ");
        String estado = entrada.nextLine();
        System.out.print("CEP: ");
        String cep = entrada.nextLine();

        // Exibir cursos disponíveis
        System.out.println("\nCursos disponíveis:");
        for (int i = 0; i < cursos.size(); i++) {
            System.out.println((i + 1) + " - " + cursos.get(i).getNome());
        }
        System.out.print("Escolha o curso (número): ");
        int opcCurso = entrada.nextInt();
        entrada.nextLine(); // limpar buffer
        Curso cursoSelecionado = cursos.get(opcCurso - 1);

        // Exibir turmas disponíveis
        System.out.println("\nTurmas disponíveis:");
        for (int i = 0; i < turmas.size(); i++) {
            System.out.println((i + 1) + " - " + turmas.get(i).getNome());
        }
        System.out.print("Escolha a turma (número): ");
        int opcTurma = entrada.nextInt();
        entrada.nextLine();
        Turma turmaSelecionada = turmas.get(opcTurma - 1);

        // Gerar ID simples baseado no tamanho da lista
        int novoId = listaAlunos.size() + 1;

        // Criar aluno
        Aluno novoAluno = new Aluno(novoId, nome, cpf, email, telefone,
                logradouro, numero, complemento, bairro, cidade, estado, cep,
                cursoSelecionado, turmaSelecionada);

        listaAlunos.add(novoAluno);
        turmaSelecionada.adicionarAluno(novoAluno);

        System.out.println("\nAluno cadastrado com sucesso!");
        System.out.println("Nome: " + novoAluno.getNome());
        System.out.println("Curso: " + cursoSelecionado.getNome());
        System.out.println("Turma: " + turmaSelecionada.getNome());
        System.out.println("Disciplina: " + turmaSelecionada.getDisciplina().getNome());
    }

    public void cadastrarDisciplina(Scanner entrada, List<Disciplina> listaDisciplinas, List<Curso> cursos) {
        System.out.println("=== CADASTRO DE DISCIPLINA ===");

        System.out.print("Nome da disciplina: ");
        String nome = entrada.nextLine();

        System.out.print("Descrição: ");
        String descricao = entrada.nextLine();

        System.out.print("Carga horária (em horas): ");
        int cargaHoraria = entrada.nextInt();
        entrada.nextLine();

        System.out.print("Código da disciplina: ");
        String codigo = entrada.nextLine();

        System.out.print("Ementa: ");
        String ementa = entrada.nextLine();

        System.out.print("Semestre oferecido (ex: 2025.1): ");
        String semestre = entrada.nextLine();

        System.out.println("\nCursos disponíveis:");
        for (int i = 0; i < cursos.size(); i++) {
            System.out.println((i + 1) + " - " + cursos.get(i).getNome());
        }
        System.out.print("Escolha o curso ao qual a disciplina pertence: ");
        int opcCurso = entrada.nextInt();
        entrada.nextLine();
        Curso cursoSelecionado = cursos.get(opcCurso - 1);

        int novoId = listaDisciplinas.size() + 1;

        Disciplina novaDisciplina = new Disciplina(novoId, nome, descricao, cargaHoraria, codigo, ementa, semestre, cursoSelecionado);
        listaDisciplinas.add(novaDisciplina);

        System.out.println("\nDisciplina cadastrada com sucesso!");
        System.out.println("Nome: " + nome);
        System.out.println("Curso: " + cursoSelecionado.getNome());
    }

    public void cadastrarTurma(Scanner entrada, List<Turma> listaTurmas, List<Curso> cursos, List<Disciplina> disciplinas) {
        System.out.println("=== CADASTRO DE TURMA ===");

        System.out.print("Nome da turma: ");
        String nome = entrada.nextLine();

        System.out.print("Descrição: ");
        String descricao = entrada.nextLine();

        System.out.print("Semestre (ex: 2025.1): ");
        String semestre = entrada.nextLine();

        System.out.print("Horário das aulas: ");
        String horario = entrada.nextLine();

        System.out.print("Sala: ");
        String sala = entrada.nextLine();

        // Selecionar curso
        System.out.println("\nCursos disponíveis:");
        for (int i = 0; i < cursos.size(); i++) {
            System.out.println((i + 1) + " - " + cursos.get(i).getNome());
        }
        System.out.print("Escolha o curso da turma: ");
        int opcCurso = entrada.nextInt();
        entrada.nextLine();
        Curso cursoSelecionado = cursos.get(opcCurso - 1);

        // Selecionar disciplina
        System.out.println("\nDisciplinas disponíveis:");
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.println((i + 1) + " - " + disciplinas.get(i).getNome());
        }
        System.out.print("Escolha a disciplina da turma: ");
        int opcDisciplina = entrada.nextInt();
        entrada.nextLine();
        Disciplina disciplinaSelecionada = disciplinas.get(opcDisciplina - 1);

        int novoId = listaTurmas.size() + 1;

        Turma novaTurma = new Turma(novoId, nome, descricao, semestre, cursoSelecionado, disciplinaSelecionada, horario, sala);
        listaTurmas.add(novaTurma);

        System.out.println("\nTurma cadastrada com sucesso!");
        System.out.println("Nome: " + nome);
        System.out.println("Curso: " + cursoSelecionado.getNome());
        System.out.println("Disciplina: " + disciplinaSelecionada.getNome());
    }
}
