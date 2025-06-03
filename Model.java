import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Model {
    public static Usuario crearUsuario(String nombre,
                                       String email,
                                       String password) {
        Usuario usuario = new Usuario(nombre,email,password);

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
    public static Usuario validarLogin(String email, String password) {
        final String SQL = "SELECT * FROM usuarios WHERE email = ? AND contrasena = ?";
        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(rs.getString("nombre_usuario"),
                        rs.getString("email"),
                        rs.getString("contrasena"));
            } else {
                return null; // Usuario no encontrado
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }
     public static Producto crearProducto(String nombre,
                                           String marca,
                                           double precio,
                                           String categoria,
                                           String supermercado,
                                           int codigoBarras) {
            Producto producto = new Producto(precio, categoria, supermercado, marca, nombre, codigoBarras);

            final String SQL =
                    "INSERT INTO productos (codigo_barras, nombre, marca, precio, categoria, supermercado) " +
                            "VALUES (?, ?, ?, ?, ?, ?) RETURNING codigo_barras";

            try (Connection conn = Conexion.abrir();
                 PreparedStatement stmt = conn.prepareStatement(SQL)) {

                stmt.setInt(1, producto.getCodigoBarras());
                stmt.setString(2, producto.getNombre());
                stmt.setString(3, producto.getMarca());
                stmt.setDouble(4, producto.getPrecio());
                stmt.setString(5, producto.getCategoria());
                stmt.setString(6, producto.getSupermercado());

                ResultSet rs = stmt.executeQuery();
                return rs.next() ? producto : null;

            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }




}
