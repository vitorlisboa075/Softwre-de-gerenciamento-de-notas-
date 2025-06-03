package model;

import java.util.Scanner;

public class AlunoNota {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("=== CADASTRO DE NOTAS ===");

        System.out.print("Nome do aluno: ");
        String nome = entrada.nextLine();

        System.out.print("Matrícula: ");
        String matricula = entrada.nextLine();

        System.out.print("Disciplina: ");
        String disciplina = entrada.nextLine();

        System.out.print("Nota 1: ");
        double nota1 = entrada.nextDouble();

        System.out.print("Nota 2: ");
        double nota2 = entrada.nextDouble();

        NotaAluno aluno = new NotaAluno(nome, matricula, disciplina, nota1, nota2);
        aluno.exibirInformacoes();

        entrada.close();
    }
}

 class NotaAluno {
    public String nome;
    public String matricula;
    public String disciplina;
    public double nota1;
    public double nota2;

    public NotaAluno(String nome, String matricula, String disciplina, double nota1, double nota2) {
        this.nome = nome;
        this.matricula = matricula;
        this.disciplina = disciplina;
        this.nota1 = nota1;
        this.nota2 = nota2;
    }

    public double calcularMedia() {
        return (nota1 + nota2) / 2;
    }

    public boolean isAprovado() {
        return calcularMedia() >= 6.0;
    }

    public void exibirInformacoes() {
        System.out.println("\n=== BOLETIM ===");
        System.out.println("Aluno: " + nome);
        System.out.println("Matrícula: " + matricula);
        System.out.println("Disciplina: " + disciplina);
        System.out.println("Nota 1: " + nota1);
        System.out.println("Nota 2: " + nota2);
        System.out.println("Média: " + calcularMedia());
        System.out.println("Status: " + (isAprovado() ? "Aprovado" : "Reprovado"));
    }
}
