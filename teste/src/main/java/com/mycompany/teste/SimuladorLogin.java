
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimuladorLogin {

    // Lista de usuários do sistema (simulando um banco de dados)
    static List<Usuario> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        // Criar usuário admin manualmente
        Usuario admin = new Usuario(
                1,
                "Diretor João",
                "123.456.789-00",
                "diretor@if.com",
                "masculino",
                "10/10/1980",
                "99999-0000",
                "senha123",
                "admin",
                "Rua Central",
                "100",
                "Sala 1",
                "Centro",
                "Cidade X",
                "UF",
                "00000-000"
        );
        usuarios.add(admin);

        System.out.println("=== SISTEMA DE LOGIN ===");
        System.out.print("Email: ");
        String email = entrada.nextLine();
        System.out.print("Senha: ");
        String senha = entrada.nextLine();

        Usuario logado = autenticarUsuario(email, senha);

        if (logado != null) {
            System.out.println("\nBem-vindo(a), " + logado.getNome());

            if (logado.isAdmin()) {
                menuAdmin(entrada);
            } else if (logado.isSecretaria()) {
                menuSecretaria();
            } else if (logado.isProfessor()) {
                menuProfessor();
            }

        } else {
            System.out.println("\nUsuário ou senha incorretos.");
        }

        entrada.close();
    }

    public static Usuario autenticarUsuario(String email, String senha) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }

    public static void menuAdmin(Scanner entrada) {
        System.out.println("\n=== MENU ADMIN ===");
        System.out.println("1 - Cadastrar novo professor");
        System.out.println("2 - Cadastrar nova secretaria");
        System.out.println("3 - Listar usuários");
        System.out.print("Escolha: ");

        int opcao = entrada.nextInt();
        entrada.nextLine(); // limpar buffer

        switch (opcao) {
            case 1 -> cadastrarUsuario(entrada, "professor");
            case 2 -> cadastrarUsuario(entrada, "secretaria");
            case 3 -> listarUsuarios();
            default -> System.out.println("Opção inválida.");
        }
    }

    public static void menuSecretaria() {
        System.out.println("\n=== MENU SECRETARIA ===");
        System.out.println("[Simulação] Acesso à criação de alunos, cursos, turmas e relatórios");
    }

    public static void menuProfessor() {
        System.out.println("\n=== MENU PROFESSOR ===");
        System.out.println("[Simulação] Acesso à inserção de notas e presenças");
    }

    public static void cadastrarUsuario(Scanner entrada, String tipo) {
        System.out.println("\nCadastro de novo usuário (" + tipo + "):");

        System.out.print("Nome: ");
        String nome = entrada.nextLine();
        System.out.print("CPF: ");
        String cpf = entrada.nextLine();
        System.out.print("Email: ");
        String email = entrada.nextLine();
        System.out.print("Telefone: ");
        String telefone = entrada.nextLine();
        System.out.print("Senha: ");
        String senha = entrada.nextLine();
        System.out.print("Gênero: ");
        String genero = entrada.nextLine();
        System.out.print("Data de nascimento: ");
        String nascimento = entrada.nextLine();
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

        int novoId = usuarios.size() + 1;
        Usuario novo = new Usuario(novoId, nome, cpf, email, genero, nascimento,
                telefone, senha, tipo, logradouro, numero, complemento, bairro, cidade, estado, cep);
        usuarios.add(novo);

        System.out.println("Usuário " + tipo + " cadastrado com sucesso!");
    }

    public static void listarUsuarios() {
        System.out.println("\n=== LISTA DE USUÁRIOS ===");
        for (Usuario u : usuarios) {
            System.out.println("- " + u.getId() + " | " + u.getNome() + " | " + u.getTipoUsuario());
        }
    }
}
