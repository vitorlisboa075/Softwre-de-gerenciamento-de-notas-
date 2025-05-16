
package softwre.de.gerenciamento.de.notas;
import java.util.Scanner;

public class login {

Scanner entrada = new Scanner(System.in);
Usuario u = new Usuario();
    void criarLogin() {
        
        
        System.out.print("Vamos fazer seu cadastro  \nDigite seu e-mail ");
        String email = entrada.nextLine();
        u.setEmail(email);
           
        System.out.print("CRIE sua senha ");
        String senha = entrada.nextLine();
        u.setSenha(senha);
    
        
    }
    
    void fazerLogin(){
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
    
    
    
   
}
