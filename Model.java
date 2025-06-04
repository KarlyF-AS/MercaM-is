import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Model {
    public static Usuario crearUsuario(String nombre,
                                       String email,
                                       String password) {
        Usuario usuario = new Usuario(nombre, email, password);

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
                                         long codigoBarras,
                                         String descripcion) {

        Producto p = new Producto(codigoBarras, nombre, marca,
                precio, categoria, supermercado, descripcion);

        final String SQL = """
                INSERT INTO producto
                (nombre, marca, supermercado, codigo_barras, descripcion, precio, categoria)
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
    /**
     * Verifica si un email ya existe en la base de datos.
     * El email es la clave unica en la tabla usuarios.
     *
     * @param email El email a verificar.
     * @return true si el email existe, false en caso contrario.
     * @author Daniel Figueroa
     */
    public static boolean existeEmail(String email) {
        final String SQL = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Si el conteo es mayor que 0, el email existe
            } else {
                return false; // No se encontró el email
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error en la conexión o consulta
        }
    }
    /**
     * Crea una unidad familiar, crear el objeto unidad familiar y añadir al usuario actual como miembro.
     * Unidad_familiar es la equivalente a Lista en la base de datos.
     * Se inserta en la tabla lista el nombre de la unidad y el codigo.
     * En la tabla contiene se inserta el usuario, que ya debería existir en la tabla usuarios.
     *
     * @return Una nueva unidad familiar.
     * @author Daniel Figueroa
     */
    public static Usuario crearUnidadFamiliar(Usuario usuario, String nombreUnidadFamiliar, String codigo) {
        Lista_UnidadFamiliar unidadFamiliar = new Lista_UnidadFamiliar(nombreUnidadFamiliar, codigo, usuario);

        final String SQL_LISTA = "INSERT INTO lista (nombre, codigo) VALUES (?, ?) RETURNING id";
        final String SQL_CONTIENE = "INSERT INTO contiene (id_lista, email_usuario) VALUES (?, ?)";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmtLista = conn.prepareStatement(SQL_LISTA);
             PreparedStatement stmtContiene = conn.prepareStatement(SQL_CONTIENE)) {

            // Insertar en la tabla lista
            stmtLista.setString(1, unidadFamiliar.getNombre());
            stmtLista.setString(2, unidadFamiliar.getCodigo());
            ResultSet rsLista = stmtLista.executeQuery();

            if (rsLista.next()) {
                unidadFamiliar.setId(rsLista.getInt("id"));

                // Insertar en la tabla contiene usando el email como identificador
                stmtContiene.setInt(1, unidadFamiliar.getId());
                stmtContiene.setString(2, usuario.getEmail());
                stmtContiene.executeUpdate();

                return usuario; // Retorna el usuario que se añadió a la unidad familiar
            } else {
                return null; // Error al insertar en la tabla lista
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.err.println("Unidad familiar o relación duplicada.");
            } else {
                e.printStackTrace();
            }
            return null; // Error en la conexión o consulta
        }
    }

    /**
     * Obtiene la unidad familiar que el usuario pertenece.
     * Si el usuario no pertenece a ninguna unidad familiar, devuelve null.
     * La clave principal de la tabla es email, por lo que se busca por email en la tabla decision.
     * Decision es la tabla donde se guarda la relación entre usuarios y unidades familiares(listas).
     *
     * @param usuario
     * @return
     * @author Daniel Figueroa
     */
public static Lista_UnidadFamiliar obtenerUnidadFamiliar(Usuario usuario) {

    final String SQL =
            """
            SELECT l.id_lista, l.nombre, l.descripcion
            FROM lista l
            JOIN decision d ON d.id_lista = l.id_lista
            WHERE d.email = ?
            LIMIT 1
            """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, usuario.getEmail());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Lista_UnidadFamiliar(
                        rs.getInt("id_lista"),
                        rs.getString("titulo"),
                        rs.getString("descripcion")
                );
            }
            return null;    // el usuario no pertenece a ninguna lista

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Permite a un usuario unirse a una unidad familiar existente.
     * El usuario debe existir en la tabla usuarios y la unidad familiar debe existir en la tabla decision.
     * Se inserta en la tabla decision el email del usuario y el codigo de la unidad familiar.
     * Si la inserción es exitosa, se devuelve la unidad familiar a la que se unió el usuario.
     *
     * @param usuario
     * @param codigoUnidadFamiliar
     * @return
     * @author Daniel Figueroa
     */
    public static Lista_UnidadFamiliar unirseAUnidadFamiliar(Usuario usuario, String codigoUnidadFamiliar) {
        // Primero, buscar la lista por código
        final String SQL_BUSCAR_LISTA = "SELECT id_lista FROM lista WHERE codigo = ?";
        final String SQL_INSERT_DECISION = "INSERT INTO decision (email, id_lista) VALUES (?, ?) RETURNING email;";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmtBuscar = conn.prepareStatement(SQL_BUSCAR_LISTA)) {

            stmtBuscar.setString(1, codigoUnidadFamiliar);
            ResultSet rsBuscar = stmtBuscar.executeQuery();

            if (!rsBuscar.next()) {
                // No existe la lista con ese código
                return null;
            }

            int idLista = rsBuscar.getInt("id_lista");

            try (PreparedStatement stmtInsert = conn.prepareStatement(SQL_INSERT_DECISION)) {
                stmtInsert.setString(1, usuario.getEmail());
                stmtInsert.setInt(2, idLista);
                ResultSet rsInsert = stmtInsert.executeQuery();

                return rsInsert.next() ? obtenerUnidadFamiliar(usuario) : null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }
    /**
     * Obtiene todos los productos de una unidad familiar.
     * Se busca en la tabla decision el email del usuario y se obtiene el codigo de la unidad familiar.
     * Luego se busca en la tabla producto los productos que pertenecen a esa unidad familiar.
     *
     * @param unidadFamiliar
     * @return
     * @author Daniel Figueroa
     */
public static List<Producto> obtenerTodosProductos(Lista_UnidadFamiliar unidadFamiliar) {
    final String SQL = """
        SELECT p.codigo_barras, p.nombre, p.marca, p.precio, p.categoria, p.supermercado, p.descripcion
        FROM producto p
        JOIN contiene c ON c.id_lista = p.codigo_barras
        WHERE c.id_lista = ?
    """;

    try (Connection conn = Conexion.abrir();
         PreparedStatement stmt = conn.prepareStatement(SQL)) {

        stmt.setInt(1, unidadFamiliar.getId());
        ResultSet rs = stmt.executeQuery();

        List<Producto> productos = new ArrayList<>();
        while (rs.next()) {
            productos.add(new Producto(
                    rs.getLong("codigo_barras"),
                    rs.getString("nombre"),
                    rs.getString("marca"),
                    rs.getDouble("precio"),
                    rs.getString("categoria"),
                    rs.getString("supermercado"),
                    rs.getString("descripcion")
            ));
        }
        return productos;

    } catch (SQLException e) {
        e.printStackTrace();
        return null; // Error en la conexión o consulta
    }
}

    /**
     * Obtiene todas las categorías de productos disponibles en la base de datos.
     * Se consulta la tabla producto y se extraen las categorías únicas.
     * Tambien hay subcategorias(el formato seria "Categoria.Subcategoria").
     * Pero este metodo devuelve las categorias sin distinguirn entre subcategorias y categorias.
     *
     * @return
     * @author Daniel Figueroa
     */
    public static ArrayList<String> obtenerCategorias() {
        final String SQL = "SELECT DISTINCT categoria FROM producto";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            ArrayList<String> categorias = new ArrayList<>();
            while (rs.next()) {
                categorias.add(rs.getString("categoria"));
            }
            return categorias;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }

    /**
     * Obtiene los productos por categoria.
     * Se consulta la tabla producto y se filtra por la categoria especificada.
     * Esta consulta devuelve todos los productos que pertenecen a la categoria especificada.
     * @author Daniel Figueroa
     * @param categoria
     * @return
     */
    public static List<Producto> obtenerProductosPorCategoria(String categoria) {
        final String SQL = "SELECT * FROM producto WHERE categoria = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();

            List<Producto> productos = new ArrayList<>();
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getLong("codigo_barras"),
                        rs.getString("nombre"),
                        rs.getString("marca"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getString("supermercado"),
                        rs.getString("descripcion")
                ));
            }
            return productos;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }
    /**
     * Obtiene los productos por subcategoria.
     * Se consulta la tabla producto y se filtra por la subcategoria especificada.
     * Esta consulta devuelve todos los productos que pertenecen a la subcategoria especificada.
     *(Esto tiene que recibir la categoria con el formato "Categoria.Subcategoria").
     * @param subcategoria
     * @return
     * @author Daniel Figueroa
     */
    public static List<Producto> obtenerProductosPorSubcategoria(String subcategoria) {
        final String SQL = "SELECT * FROM producto WHERE categoria LIKE ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, "%" + subcategoria);
            ResultSet rs = stmt.executeQuery();

            List<Producto> productos = new ArrayList<>();
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getLong("codigo_barras"),
                        rs.getString("nombre"),
                        rs.getString("marca"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getString("supermercado"),
                        rs.getString("descripcion")
                ));
            }
            return productos;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }
    /**
     * Obtiene todas las marcas de productos disponibles en la base de datos.
     * Se consulta la tabla producto y se extraen las marcas únicas.
     *
     * @return
     * @author Daniel Figueroa
     */
    public static List<String> obtenerMarcas() {
        final String SQL = "SELECT DISTINCT marca FROM producto";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            List<String> marcas = new ArrayList<>();
            while (rs.next()) {
                marcas.add(rs.getString("marca"));
            }
            return marcas;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }
    /**
     * Obtiene los productos por marca.
     * Se consulta la tabla producto y se filtra por la marca especificada.
     * Esta consulta devuelve todos los productos que pertenecen a la marca especificada.
     *
     * @param marca
     * @return
     * @author Daniel Figueroa
     */
    public static List<Producto> obtenerProductosPorMarca(String marca) {
        final String SQL = "SELECT * FROM producto WHERE marca = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, marca);
            ResultSet rs = stmt.executeQuery();

            List<Producto> productos = new ArrayList<>();
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getLong("codigo_barras"),
                        rs.getString("nombre"),
                        rs.getString("marca"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getString("supermercado"),
                        rs.getString("descripcion")
                ));
            }
            return productos;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }
    public static List<Producto> recogerTodosProductos() {
        final String SQL = "SELECT * FROM producto";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            List<Producto> productos = new ArrayList<>();
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getLong("codigo_barras"),
                        rs.getString("nombre"),
                        rs.getString("marca"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getString("supermercado"),
                        rs.getString("descripcion")
                ));
            }
            return productos;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }
    /**
     * Actualiza el precio de un producto. y se guarda en el historial de precios(esto lo hace un trigger automaticamente).
     * Se actualiza el precio del producto en la tabla producto.
     * @param producto
     * @param nuevoPrecio
     * @author Daniel Figueroa
     */
    public static void actualizarPrecioProducto(Producto producto, double nuevoPrecio) {
        final String SQL = "UPDATE producto SET precio = ? WHERE codigo_barras = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setDouble(1, nuevoPrecio);
            stmt.setLong(2, producto.getCodigoBarras());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de errores
            System.err.println("Error al actualizar el precio del producto: " + e.getMessage());
        }
    }
    /**
     * Añade una puntuación a un producto por parte de un usuario.
     * Se inserta en la tabla puntua el email del usuario, nombre(producto),marca,supermercado y la puntuación(del 0 al 5).
     * La tabla puntuacion tiene guardados de forma fija numericos del 0 al 5.
     * Si el usuario ya ha puntuado el producto, se actualiza la puntuación.
     *
     * @param producto
     * @param usuario
     * @param puntuacion
     * @author Daniel Figueroa
     */
    public static Producto void anadirPuntuacionProducto(Producto producto, Usuario usuario, int puntuacion) {
        final String SQL = """
                INSERT INTO puntua (email_usuario, nombre_producto, marca_producto, supermercado_producto, puntuacion)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (email_usuario, nombre_producto, marca_producto, supermercado_producto)
                DO UPDATE SET puntuacion = EXCLUDED.puntuacion
                RETURNING nombre_producto;
                """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, producto.getNombre());
            stmt.setString(3, producto.getMarca());
            stmt.setString(4, producto.getSupermercado());
            stmt.setInt(5, puntuacion);

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? producto : null;

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de errores
            return null; // Error en la conexión o consulta
        }

    }
    /**
     * Añade un supermercado a un producto.
     * Se inserta en la tabla pertenece el nombre del supermercado y el codigo de barras del producto.
     * Si el producto ya pertenece al supermercado, no se hace nada.
     * @return El producto al que se le ha añadido el supermercado.
     * @param producto
     * @param supermercado
     * @author Daniel Figueroa
     */
    public static List<Producto> obtenerProductoPorNombre(String nombre) {
        final String SQL = "SELECT * FROM producto WHERE nombre = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();

            List<Producto> productos = new ArrayList<>();
            while (rs.next()) {
                productos.add(new Producto(
                    rs.getLong("codigo_barras"),
                    rs.getString("nombre"),
                    rs.getString("marca"),
                    rs.getDouble("precio"),
                    rs.getString("categoria"),
                    rs.getString("supermercado"),
                    rs.getString("descripcion")
                ));
            }
            return productos;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }
    /**
     * Añade un supermercado a un producto.
     * Se inserta en la tabla producto el producto entero, nombre, marca etc..
     * Si el producto ya pertenece al supermercado, no se hace nada(todo está en la tabla producto).
     * Partiendo que el producto ya existe en la base de datos se clona ese producto con el supermercado nuevo.
     * Si el producto no existe, se crea un nuevo producto con el supermercado.
     * @return El producto al que se le ha añadido el supermercado.
     * @param producto
     * @param supermercado
     * @author Daniel Figueroa
     */
    public static Producto anadirSupermercadoProducto(Producto producto, String supermercado) {
        // insert el produco en la tabla producto
        final String SQL = """
        INSERT INTO producto (nombre, marca, supermercado, codigo_barras, descripcion, precio, categoria)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT (nombre, marca, supermercado) DO NOTHING
        RETURNING codigo_barras;
        """;
        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getMarca());
            stmt.setString(3, supermercado);
            stmt.setLong(4, producto.getCodigoBarras());
            stmt.setString(5, producto.getDescripcion());
            stmt.setDouble(6, producto.getPrecio());
            stmt.setString(7, producto.getCategoria());

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? producto : null;

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de errores
            return null; // Error en la conexión o consulta
        }

    }
    /**
     * Elimina un supermercado de un producto.
     * Se elimina el producto de la tabla producto.
     * Si el producto no existe, no se hace nada.
     * no se necesita el supermercado, ya que el producto ya tiene el supermercado asociado como propiedad.
     * @return el producto al que se le ha eliminado el supermercado.
     * @param producto
     * @author Daniel Figueroa
     */
    public static Producto eliminarSupermercadoProducto(Producto criterio) {

        final String SQL = """
        DELETE FROM producto
        WHERE nombre = ? AND marca = ? AND supermercado = ?
        RETURNING  codigo_barras,
                  nombre,
                  marca,
                  precio,
                  categoria,
                  supermercado,
                  descripcion;
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, criterio.getNombre());
            stmt.setString(2, criterio.getMarca());
            stmt.setString(3, criterio.getSupermercado());

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;           // no existía ningún producto que borrar
                }

                /* Construimos el objeto con el constructor que indicas */
                return new Producto(
                        rs.getLong  ("codigo_barras"),
                        rs.getString("nombre"),
                        rs.getString("marca"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getString("supermercado"),
                        rs.getString("descripcion")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;                   // error de SQL o conexión
        }
    }

    /**
     * Obtiene los productos de una unidad familiar.
     * Se consulta la tabla contiene para obtener los productos asociados a la unidad familiar.
     * Esta consulta devuelve todos los productos que pertenecen a la unidad familiar especificada.
     * @author Daniel Figueroa
     * @param unidadFamiliar
     * @return
     */
    public static List<Producto> obtenerProductosUnidadFamiliar(Lista_UnidadFamiliar unidadFamiliar) {
        final String SQL = """
            SELECT p.codigo_barras, p.nombre, p.marca, p.precio, p.categoria, p.supermercado, p.descripcion
            FROM producto p
            JOIN contiene c ON c.id_lista = p.codigo_barras
            WHERE c.id_lista = ?
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, unidadFamiliar.getId());
            ResultSet rs = stmt.executeQuery();

            List<Producto> productos = new ArrayList<>();
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getLong("codigo_barras"),
                        rs.getString("nombre"),
                        rs.getString("marca"),
                        rs.getDouble("precio"),
                        rs.getString("categoria"),
                        rs.getString("supermercado"),
                        rs.getString("descripcion")
                ));
            }
            return productos;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }

    /**
     * Cambia el nombre de usuario de un usuario.
     * Se actualiza el nombre de usuario en la tabla usuarios.
     * Si la actualización es exitosa, se devuelve true.
     * Si ocurre un error, se imprime el error y se devuelve false.
     * @author Daniel Figueroa
     * @param usuario
     * @param nuevoNombre
     * @return
     */
    public static boolean cambiarNombreUsuario(Usuario usuario, String nuevoNombre) {
        final String SQL = "UPDATE usuarios SET nombre_usuario = ? WHERE email = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, nuevoNombre);
            stmt.setString(2, usuario.getEmail());
            int filasActualizadas = stmt.executeUpdate();

            return filasActualizadas > 0; // Retorna true si se actualizó al menos una fila

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error en la conexión o consulta
        }
    }
    /**
     * Obtiene un producto por su nombre.
     * Se consulta la tabla producto y se filtra por el nombre especificado.
     * Esta consulta devuelve el producto que coincide con el nombre especificado.
     *
     * @param nombre
     * @return
     * @author Daniel Figueroa
     */
    public static boolean cambiarContrasena(Usuario usuario, String actual, String nueva) {
        // Verifica si la contraseña actual es correcta
        if (!validarLogin(usuario.getEmail(), actual)) {
            return false; // Contraseña actual incorrecta
        }

        final String SQL = "UPDATE usuarios SET contrasena = ? WHERE email = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, nueva);
            stmt.setString(2, usuario.getEmail());
            int filasActualizadas = stmt.executeUpdate();

            return filasActualizadas > 0; // Retorna true si se actualizó al menos una fila

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error en la conexión o consulta
        }
    }




}
