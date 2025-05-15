
package softwre.de.gerenciamento.de.notas;
import java.util.Scanner;

public class login {

    String senha = "";
    void criarLogin() {
        
        Scanner cadastroEmail = new Scanner(System.in);
        System.out.print("Vamos fazer seu cadastro  \nDigite seu e-mail ");
        String email = cadastroEmail.nextLine();    
        
        
        Scanner cadastroSenha = new Scanner(System.in);
        System.out.print("CRIE sua senha ");
        String senha = cadastroSenha.nextLine();
    
        
    }
    
    void fazerLogin(){
        Scanner usuario = new Scanner(System.in);
        System.out.print("Digite seu e-mail ");
        String loginSenha = usuario.nextLine();
        
        System.out.print(loginSenha );
        
        boolean vld = loginSenha.equals(senha);
         System.out.print(senha );

        
        if(vld){
          
        System.out.print(loginSenha);

        
        }else{
        System.out.print("errado");
        
        }
        
        
        
        
        
    }
    
    
    
   
}
