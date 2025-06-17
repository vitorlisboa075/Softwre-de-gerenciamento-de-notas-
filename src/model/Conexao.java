package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "sistemanotas";       // Substitua pelo nome do seu banco
    private static final String USER = "root";         // Ex: "root"
    private static final String PASS = "";           // Ex: ""
    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
            "?useSSL=false&serverTimezone=UTC";

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("STATUS: Conectado com sucesso!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Falha na conexão: " + e.getMessage());
        }
        return null;
    }

    
    public static boolean fecharConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexão encerrada.");
                return true;
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
        return false;
    }

    // ✅ MÉTODO MAIN ADICIONADO PARA TESTAR A CONEXÃO DIRETAMENTE
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Conexão testada com sucesso.");
            fecharConnection(conn);
        } else {
            System.out.println("Erro ao tentar conectar.");
        }
    }
}
