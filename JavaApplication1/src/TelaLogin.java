import javax.swing.*;

public class TelaLogin {

    public static void main(String[] args) {
        // Criar janela
        JFrame frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null); // Layout manual

        // Criar componentes
        JLabel labelUsuario = new JLabel("Usuário:");
        labelUsuario.setBounds(20, 20, 80, 25);
        JTextField campoUsuario = new JTextField();
        campoUsuario.setBounds(100, 20, 160, 25);

        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(20, 60, 80, 25);
        JPasswordField campoSenha = new JPasswordField();
        campoSenha.setBounds(100, 60, 160, 25);

        JButton botaoLogin = new JButton("Entrar");
        botaoLogin.setBounds(100, 100, 80, 25);

        // Adicionar componentes ao frame
        frame.add(labelUsuario);
        frame.add(campoUsuario);
        frame.add(labelSenha);
        frame.add(campoSenha);
        frame.add(botaoLogin);
        
    }

}
