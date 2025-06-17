package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public static Usuario buscarUsuarioPorEmail(String email) {
        String sql = "SELECT cpf, email, senha, tipo FROM usuarios WHERE email = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setCpf(rs.getString("cpf"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setTipoUsuario(rs.getString("tipo"));
                return u;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null;
    }
}
