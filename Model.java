import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Model {
    public static Usuario crearUsuario(String nombre,
                                       String email,
                                       String password) {
        Usuario usuario = new Usuario(1,nombre,email,password);

        final String SQL =
                "INSERT INTO usuarios (email, nombre_usuario, contrasena) " +
                        "VALUES (?, ?, ?) RETURNING email";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = ejecutar(stmt, usuario)) {

            return rs.next() ? usuario : null;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.err.println("Email o nombre duplicado.");
            } else {
                e.printStackTrace();
            }
            return null;
        }
    }

    /* Pequeño helper para mantener el try-with-resources limpio */
    private static ResultSet ejecutar(PreparedStatement stmt, Usuario u) throws SQLException {
        stmt.setString(1, u.getEmail());
        stmt.setString(2, u.getNombre());
        stmt.setString(3, u.getPassword());
        return stmt.executeQuery();           // se devuelve como recurso autocerrable
    }



}
