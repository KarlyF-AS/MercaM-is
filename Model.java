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
                                         long   codigoBarras,
                                         String descripcion) {

        Producto p = new Producto(codigoBarras, nombre, marca,
                precio, categoria, supermercado, descripcion);

        final String SQL = """
        INSERT INTO producto
        (codigo_barras, nombre, marca, precio, categoria, supermercado, descripcion)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        RETURNING codigo_barras;
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setLong  (1, p.getCodigoBarras());
            stmt.setString(2, p.getNombre());
            stmt.setString(3, p.getMarca());
            stmt.setDouble(4, p.getPrecio());
            stmt.setString(5, p.getCategoria());
            stmt.setString(6, p.getSupermercado());
            stmt.setString(7, p.getDescripcion());

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? p : null;

        } catch (SQLException e) {

            switch (e.getSQLState()) {
                case "P0001" -> {                      // ← trigger
                    System.err.println(
                            "Conflicto de código de barras: "
                                    + e.getMessage());
                    return null;
                }
                case "23505" -> {                      // ← PK (nombre, marca, supermercado)
                    System.err.println(
                            "Ya existe ese producto en el mismo supermercado.");
                    return null;
                }
                default -> {
                    e.printStackTrace();               // otros errores SQL
                    return null;
                }
            }
        }
    }

    public static Producto buscarProductoPorCodigoBarras(long codigoBarras) {
        final String SQL = "SELECT * FROM producto WHERE codigo_barras = ?";
        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setLong(1, codigoBarras);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Producto(rs.getLong("codigo_barras"),
                        rs.getString("nombre"),
                        rs.getString("marca"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getString("supermercado"),
                        rs.getString("descripcion"));
            } else {
                return null; // Producto no encontrado
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }





}
