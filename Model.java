import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    /**
     * Registra un nuevo usuario en la base de datos.
     * El usuario se crea con un nombre, email y contraseña.
     * Si el email ya existe, se captura la excepción y se devuelve null.
     * @param nombre
     * @param email
     * @param password
     * @return
     */
    public static Usuario registrarUsuario(String nombre,
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

    /**
     * Valida el login de un usuario.
     * Comprueba si el email y la contraseña coinciden con un usuario en la base de datos.
     * @param email
     * @param password
     * @return Un objeto Usuario si las credenciales son correctas, null en caso contrario.
     */
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
    /**
     * Crea un nuevo producto en la base de datos.
     * El producto se crea con un nombre, marca, precio, categoría, supermercado, código de barras y descripción.
     * Si el código de barras ya existe, se captura la excepción y se devuelve null.
     * Si el producto ya existe en el mismo supermercado, se captura la excepción y se devuelve null.
     *
     * @param nombre
     * @param marca
     * @param precio
     * @param categoria
     * @param supermercado
     * @param codigoBarras
     * @param descripcion
     * @return Un objeto Producto si la inserción es exitosa, null en caso contrario.
     */
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

            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getMarca());
            stmt.setString(3, p.getSupermercado());
            stmt.setLong  (4, p.getCodigoBarras());
            stmt.setString(5, p.getDescripcion());
            stmt.setDouble(6, p.getPrecio());
            stmt.setString(7, p.getCategoria());

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

    public static List<Producto> buscarProductoPorCodigoBarras(long codigoBarras) {
        final String SQL = "SELECT * FROM producto WHERE codigo_barras = ?";
        List<Producto> lista = new ArrayList<>();

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setLong(1, codigoBarras);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Producto(
                            rs.getLong   ("codigo_barras"),
                            rs.getString ("nombre"),
                            rs.getString ("marca"),
                            rs.getDouble ("precio"),
                            rs.getString ("categoria"),
                            rs.getString ("supermercado"),
                            rs.getString ("descripcion")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;  // si está vacío, no encontró nada; si tiene 1, devolvió el producto
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
    public static Lista_UnidadFamiliar crearUnidadFamiliar(
            Usuario usuario,
            String nombreUnidadFamiliar,
            String codigo) {

        // 1) Creamos el objeto en memoria.
        Lista_UnidadFamiliar unidadFamiliar =
                new Lista_UnidadFamiliar(nombreUnidadFamiliar, codigo, usuario);

        // 2) Insertar en "listas" (id_lista = código, titulo = nombreUnidad)
        //    RETURNING id_lista para que nos devuelva el mismo código que acabamos de insertar
        final String SQL_LISTA =
                "INSERT INTO listas (id_lista, titulo) " +
                        "VALUES (?, ?) " +
                        "RETURNING id_lista";

        // 3) Insertar en "decision" (email, id_lista)
        final String SQL_DECISION =
                "INSERT INTO decision (email, id_lista) " +
                        "VALUES (?, ?)";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmtLista    = conn.prepareStatement(SQL_LISTA);
             PreparedStatement stmtDecision = conn.prepareStatement(SQL_DECISION)) {

            // ───── Paso 1: Insert en "listas" ─────
            stmtLista.setString(1, unidadFamiliar.getCodigo()); // id_lista = código
            stmtLista.setString(2, unidadFamiliar.getNombre()); // titulo  = nombreUnidadFamiliar
            ResultSet rsLista = stmtLista.executeQuery();

            if (rsLista.next()) {
                // Obtenemos el id_lista generado (será igual al "código" que pasaste)
                String idGenerado = rsLista.getString("id_lista");
                unidadFamiliar.setCodigo(idGenerado);

                // ───── Paso 2: Insert en "decision" ─────
                stmtDecision.setString(1, usuario.getEmail());      // email
                stmtDecision.setString(2, unidadFamiliar.getCodigo()); // id_lista
                stmtDecision.executeUpdate();

                // Devolvemos la unidad familiar con su código asignado
                return unidadFamiliar;
            } else {
                // No devolvió nada → falló el INSERT en "listas"
                return null;
            }

        } catch (SQLException e) {
            // Si la SQLState es 23505, significa que ya existía ese id_lista (código)
            if ("23505".equals(e.getSQLState())) {
                System.err.println("Ya existe una unidad familiar con ese código.");
            } else {
                e.printStackTrace();
            }
            return null;
        }
    }



    /**
     * Obtiene la unidad familiar que el usuario pertenece.
     * Si el usuario no pertenece a ninguna unidad familiar, devuelve null.
     * La clave principal de la tabla es email, por lo que se busca por email en la tabla decision.
     * Decision es la tabla donde se guarda la relación entre usuarios y unidades familiares(listas).
     *
     * @param usuario
     * @return Una unidad familiar si el usuario pertenece a una, null en caso contrario.
     * @author Daniel Figueroa
     */
    public static Lista_UnidadFamiliar obtenerUnidadFamiliar(Usuario usuario) {

        final String SQL = """
            SELECT l.id_lista, l.titulo, l.descripcion
            FROM listas l
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
                        rs.getString("id_lista"),
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
     * @return Una unidad familiar si el usuario se unió correctamente, null en caso contrario.
     * @author Daniel Figueroa
     */
    public static Lista_UnidadFamiliar unirseAUnidadFamiliar(Usuario usuario, String codigoUnidadFamiliar) {
        final String SQL_BUSCAR_LISTA = "SELECT id_lista FROM listas WHERE id_lista = ?";
        final String SQL_INSERT_DECISION = "INSERT INTO decision (email, id_lista) VALUES (?, ?) RETURNING email;";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmtBuscar = conn.prepareStatement(SQL_BUSCAR_LISTA)) {

            stmtBuscar.setString(1, codigoUnidadFamiliar);
            ResultSet rsBuscar = stmtBuscar.executeQuery();

            if (!rsBuscar.next()) {
                // No existe la lista con ese código
                return null;
            }

            String idLista = rsBuscar.getString("id_lista");

            try (PreparedStatement stmtInsert = conn.prepareStatement(SQL_INSERT_DECISION)) {
                stmtInsert.setString(1, usuario.getEmail());
                stmtInsert.setString(2, idLista);
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
     * @return
     * @author Daniel Figueroa
     */
    public static List<Producto> obtenerTodosProductos() {
        final String SQL = """
        SELECT codigo_barras,
               nombre,
               marca,
               precio,
               categoria,
               supermercado,
               descripcion
        FROM producto
        """;

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
            return null;
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
    public static List<Producto> obtenerProductosPorCategoria(String categoria) { // PENDIENTE
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
        // Corrección: Usar igualdad exacta
        final String SQL = "SELECT * FROM producto WHERE categoria = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, subcategoria); // Sin agregar "%"
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
            return null;
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
        final String SQL = "UPDATE producto SET precio = ? WHERE nombre = ? AND marca = ? AND supermercado = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setDouble(1, nuevoPrecio);
            stmt.setString(2, producto.getNombre());
            stmt.setString(3, producto.getMarca());
            stmt.setString(4, producto.getSupermercado());
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
    public static Producto anadirPuntuacionProducto(
            Producto producto,
            Usuario usuario,
            int puntuacion) {

        final String SQL = """
        INSERT INTO puntua (
            email,
            nombre,
            marca,
            supermercado,
            puntuacion
        ) VALUES (?, ?, ?, ?, ?)
        ON CONFLICT (email, nombre, marca, supermercado)
        DO UPDATE
          SET puntuacion = EXCLUDED.puntuacion
        RETURNING nombre
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, producto.getNombre());
            stmt.setString(3, producto.getMarca());
            stmt.setString(4, producto.getSupermercado());
            stmt.setInt   (5, puntuacion);

            try (ResultSet rs = stmt.executeQuery()) {
                // si se insertó o actualizó, devolvemos el mismo objeto producto
                return rs.next() ? producto : null;
            }

        } catch (SQLException e) {
            System.err.println("Error al añadir puntuación: " + e.getMessage());
            e.printStackTrace();
            return null;
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
        final String SQL = """
        SELECT codigo_barras,
               nombre,
               marca,
               precio,
               categoria,
               supermercado,
               descripcion
          FROM producto
         WHERE LOWER(nombre) = LOWER(?)
    """;

        List<Producto> lista = new ArrayList<>();
        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Producto(
                            rs.getLong("codigo_barras"),
                            rs.getString("nombre"),
                            rs.getString("marca"),
                            rs.getDouble("precio"),
                            rs.getString("categoria"),
                            rs.getString("supermercado"),
                            rs.getString("descripcion")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
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
    public static Producto eliminarSupermercadoProducto(Producto producto, String supermercado) {

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

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getMarca());
            stmt.setString(3, supermercado);

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
    public static Map<Producto, Integer> obtenerProductosUnidadFamiliar(
            Lista_UnidadFamiliar unidadFamiliar) {

        final String SQL = """
        SELECT
          p.codigo_barras,
          p.nombre,
          p.marca,
          p.precio,
          p.categoria,
          p.supermercado,
          p.descripcion,
          c.cantidad
        FROM producto p
        JOIN contiene c
          ON c.nombre = p.nombre
         AND c.marca = p.marca
         AND c.supermercado = p.supermercado
        WHERE c.id_lista = ?
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            // 1) Ponemos el id_lista (string) como parámetro
            stmt.setString(1, unidadFamiliar.getCodigo());

            // 2) Ejecutamos la consulta
            ResultSet rs = stmt.executeQuery();

            // 3) Construimos el Map<Producto,Integer>
            Map<Producto, Integer> productos = new HashMap<>();
            while (rs.next()) {
                // Leemos datos del producto
                Producto producto = new Producto(
                        rs.getLong   ("codigo_barras"),
                        rs.getString ("nombre"),
                        rs.getString ("marca"),
                        rs.getDouble ("precio"),
                        rs.getString ("categoria"),
                        rs.getString ("supermercado"),
                        rs.getString ("descripcion")
                );
                int cantidad = rs.getInt("cantidad");
                productos.put(producto, cantidad);
            }
            return productos;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // En caso de error devolvemos null
        }
    }
    public static Map<Integer, Producto> stock(
            Lista_UnidadFamiliar unidadFamiliar) {
        final String SQL = """
        SELECT
          p.codigo_barras,
          p.nombre,
          p.marca,
          p.precio,
          p.categoria,
          p.supermercado,
          p.descripcion,
          c.cantidad
        FROM producto p
        JOIN contiene c
          ON c.nombre = p.nombre
         AND c.marca = p.marca
         AND c.supermercado = p.supermercado
        WHERE c.id_lista = ?
        """;
        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            // 1) Ponemos el id_lista (string) como parámetro
            stmt.setString(1, unidadFamiliar.getCodigo());

            // 2) Ejecutamos la consulta
            ResultSet rs = stmt.executeQuery();

            // 3) Construimos el Map<Integer, Producto>
            Map<Integer, Producto> productos = new HashMap<>();
            while (rs.next()) {
                // Leemos datos del producto
                Producto producto = new Producto(
                        rs.getLong   ("codigo_barras"),
                        rs.getString ("nombre"),
                        rs.getString ("marca"),
                        rs.getDouble ("precio"),
                        rs.getString ("categoria"),
                        rs.getString ("supermercado"),
                        rs.getString ("descripcion")
                );
                int cantidad = rs.getInt("cantidad");
                productos.put(cantidad, producto);
            }
            return productos;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // En caso de error devolvemos null
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
        if (validarLogin(usuario.getEmail(), actual) == null) {
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
    /**
     * Cambia el nombre de una unidad familiar.
     * Se actualiza el nombre de la unidad familiar en la tabla lista.
     * Si la actualización es exitosa, se devuelve true.
     * Si ocurre un error, se imprime el error y se devuelve false.
     *
     * @param unidadFamiliar
     * @param nuevoNombre
     * @return
     * @author Daniel Figueroa
     */
    public static boolean cambiarNombreUnidadFamiliar(Lista_UnidadFamiliar unidadFamiliar, String nuevoNombre) {
        final String SQL = "UPDATE listas SET titulo = ? WHERE id_lista = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, nuevoNombre);
            stmt.setString(2, unidadFamiliar.getCodigo());
            int filasActualizadas = stmt.executeUpdate();

            return filasActualizadas > 0; // Retorna true si se actualizó al menos una fila

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error en la conexión o consulta
        }
    }
    /**
     * Permite a un usuario abandonar una unidad familiar.
     * Se elimina la relación entre el usuario y la unidad familiar en la tabla decision.
     * Si la eliminación es exitosa, se devuelve true.
     * Si ocurre un error, se imprime el error y se devuelve false.
     *
     * @param usuario
     * @param unidadFamiliar
     * @return
     * @author Daniel Figueroa
     */
    public static boolean abandonarUnidadFamiliar(Usuario usuario, Lista_UnidadFamiliar unidadFamiliar) {
        final String SQL = "DELETE FROM decision WHERE email = ? AND id_lista = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, unidadFamiliar.getCodigo());
            int filasEliminadas = stmt.executeUpdate();

            return filasEliminadas > 0; // Retorna true si se eliminó al menos una fila

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error en la conexión o consulta
        }
    }
    /**
     * Actualiza el precio de un producto en un supermercado específico.
     * Se actualiza el precio del producto en la tabla producto.
     * Si la actualización es exitosa, se devuelve true.
     * Si ocurre un error, se imprime el error y se devuelve false.
     *
     * @param producto
     * @param nuevoPrecio
     * @return
     * @author Daniel Figueroa
     */
    public static boolean actualizarPrecioSupermercado(Producto producto, double nuevoPrecio) {
        final String SQL = "UPDATE producto SET precio = ? WHERE codigo_barras = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setDouble(1, nuevoPrecio);
            stmt.setLong(2, producto.getCodigoBarras());
            int filasActualizadas = stmt.executeUpdate();

            return filasActualizadas > 0; // Retorna true si se actualizó al menos una fila

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error en la conexión o consulta
        }
    }
    /**
     * Modifica la cantidad de un producto en una unidad familiar.
     * Se actualiza la cantidad del producto en la tabla contiene.
     * Si la actualización es exitosa, se devuelve el producto modificado.
     * Si ocurre un error, se imprime el error y se devuelve null.
     * la cantidad es un campo que se añade a la tabla contiene.
     * @return
     * @author Daniel Figueroa
     */
    public static int modificarCantidadProducto(
            Lista_UnidadFamiliar unidad,
            Producto producto,
            int incrementar) {

        // Corrección: Usar nombre, marca y supermercado
        final String SQL = """
    UPDATE contiene c
    SET    cantidad = c.cantidad + ?
    WHERE  c.id_lista      = ?
      AND  c.nombre        = ?
      AND  c.marca         = ?
      AND  c.supermercado  = ?
    RETURNING c.cantidad;
    """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, incrementar);
            stmt.setString(2, unidad.getCodigo());
            stmt.setString(3, producto.getNombre());
            stmt.setString(4, producto.getMarca());
            stmt.setString(5, producto.getSupermercado());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad");
                }
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Filtra los productos por supermercado.
     * Se consulta la tabla producto y se filtra por el supermercado especificado.
     * Esta consulta devuelve todos los productos que pertenecen al supermercado especificado.
     * @author Daniel Figueroa
     * @param supermercado
     * @return
     */

    public static List<Producto> filtrarPorSupermercado(String supermercado) {
        final String SQL = "SELECT * FROM producto WHERE supermercado = ?";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, supermercado);
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
     * Con los productos que se guardan de la lista de la unidad familiar se vuelve a generar la lista de productos,
     * en su map de productos se guardan los productos que tienen stock.
     * @param lista
     * @return
     */
    public static Lista_UnidadFamiliar obtenerListaDeProductosConStock(
            Lista_UnidadFamiliar lista) {

        final String SQL = """
        SELECT 
          p.codigo_barras,
          p.nombre,
          p.marca,
          p.precio,
          p.categoria,
          p.supermercado,
          p.descripcion,
          c.cantidad
        FROM producto p
        JOIN contiene c
          ON c.nombre       = p.nombre
         AND c.marca        = p.marca
         AND c.supermercado = p.supermercado
        WHERE c.id_lista    = ?
          AND c.cantidad   > 0
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, lista.getCodigo());
            ResultSet rs = stmt.executeQuery();

            // Cambiamos el Map: clave = Producto, valor = cantidad
            Map<Integer,Producto> productoMap = new HashMap<>();

            while (rs.next()) {
                Producto prod = new Producto(
                        rs.getLong   ("codigo_barras"),
                        rs.getString ("nombre"),
                        rs.getString ("marca"),
                        rs.getDouble ("precio"),
                        rs.getString ("categoria"),
                        rs.getString ("supermercado"),
                        rs.getString ("descripcion")
                );
                int cantidad = rs.getInt("cantidad");
                productoMap.put(cantidad,prod);
            }

            // Asegúrate de que tu Lista_UnidadFamiliar tenga un setter que acepte
            // Map<Producto,Integer>, no Map<Long,Producto>
            lista.setProductos(productoMap);
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Añade “cantidad” al stock de un producto en la unidad familiar dada.
     * Verifica primero que la unidad existe en 'listas' y, si no, informa con un mensaje.
     * Luego hace INSERT … ON CONFLICT … para sumar la cantidad si ya existía.
     *
     * @param unidad    La unidad familiar (provee id_lista).
     * @param producto  El producto a añadir (usa nombre, marca, supermercado).
     * @param cantidad  Cantidad a sumar.
     */
    public static void añadirProductoStock(
            Lista_UnidadFamiliar unidad,
            Producto producto,
            int cantidad) {

        final String SQL_FIND =
                "SELECT id_lista FROM listas WHERE id_lista = ? OR titulo = ?";
        final String SQL_UPSERT = """
        INSERT INTO contiene (
            id_lista,
            nombre,
            marca,
            supermercado,
            cantidad
        ) VALUES (?, ?, ?, ?, ?)
        ON CONFLICT (id_lista, nombre, marca, supermercado)
        DO UPDATE
          SET cantidad = contiene.cantidad + EXCLUDED.cantidad;
        """;

        try (Connection conn = Conexion.abrir();
             // 1) Intentamos resolver un id_lista válido
             PreparedStatement psFind = conn.prepareStatement(SQL_FIND)) {

            psFind.setString(1, unidad.getCodigo());
            psFind.setString(2, unidad.getCodigo());
            try (ResultSet rs = psFind.executeQuery()) {
                if (!rs.next()) {
                    System.err.println("❌ No existe unidad familiar con código o nombre = '"
                            + unidad.getCodigo() + "'");
                    return;
                }
                // Si el usuario pasó el título por error, aquí obtenemos el verdadero id_lista
                unidad.setCodigo(rs.getString("id_lista"));
            }

            // 2) Ya tenemos id_lista correcto en unidad.getCodigo(), hacemos el UPSERT
            try (PreparedStatement psUpsert = conn.prepareStatement(SQL_UPSERT)) {
                psUpsert.setString(1, unidad.getCodigo());
                psUpsert.setString(2, producto.getNombre());
                psUpsert.setString(3, producto.getMarca());
                psUpsert.setString(4, producto.getSupermercado());
                psUpsert.setInt(   5, cantidad);
                psUpsert.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("❌ Error añadiendo al stock: " + e.getMessage());
            e.printStackTrace();
        }
    }



    /**
     * Elimina un producto del stock de una unidad familiar.
     * Si el producto no existe en la lista, no se hace nada.
     * Esta operación devuelve la cantidad que ha quedado en el stock tras la eliminación.
     * Devuelve -1 si hubo un error de SQL o conexión.
     * @param unidad
     * @param producto
     * @return
     */
    public static int eliminarProductoStock(Lista_UnidadFamiliar unidad, Producto producto) {
        final String SQL = """
        DELETE FROM contiene
        WHERE id_lista = ?
          AND nombre = ?
          AND marca = ?
          AND supermercado = ?
        RETURNING cantidad;
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, unidad.getCodigo());                     // id_lista
            stmt.setString(2, producto.getNombre());            // nombre
            stmt.setString(3, producto.getMarca());             // marca
            stmt.setString(4, producto.getSupermercado());      // supermercado

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad");               // Retorna la cantidad que quedaba tras la eliminación
                }
                return 0;                                       // No había stock del producto
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;                                          // Error de SQL o conexión
        }

    }
    /**
     * Actualiza la cantidad de un producto en el stock de una unidad familiar.
     * Si el producto no existe, no se actualiza nada y se devuelve -1.
     * Si la actualización es exitosa, se devuelve la nueva cantidad del producto.
     * Devuelve -1 si hubo un error de SQL o conexión.
     *
     * @param unidad   La unidad familiar (provee id_lista).
     * @param producto El producto a actualizar (usa nombre, marca, supermercado).
     * @param cantidad Nueva cantidad a establecer.
     */
    public static int actualizarCantidadStock(
            Lista_UnidadFamiliar unidad,
            Producto producto,
            int cantidad) {

        final String SQL = """
        UPDATE contiene
        SET cantidad = ?
        WHERE id_lista = ?
          AND nombre = ?
          AND marca = ?
          AND supermercado = ?
        RETURNING cantidad;
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setInt(1, cantidad);                          // Nueva cantidad
            stmt.setString(2, unidad.getCodigo());                    // id_lista
            stmt.setString(3, producto.getNombre());           // nombre
            stmt.setString(4, producto.getMarca());            // marca
            stmt.setString(5, producto.getSupermercado());     // supermercado

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad");               // Retorna la nueva cantidad tras la actualización
                }
                return -1;                                      // No se actualizó nada (producto no encontrado)
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;                                          // Error de SQL o conexión
        }
    }

    /**
     * Obtiene todos los supermercados disponibles en la base de datos.
     * Se consulta la tabla producto y se extraen los supermercados únicos.
     * Esta consulta devuelve una lista de nombres de supermercados.
     * @author Daniel Figueroa
     * @return
     */
    public static List<String> obtenerTodosSupermercados() {
        final String SQL = "SELECT DISTINCT supermercado FROM producto";

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            List<String> supermercados = new ArrayList<>();
            while (rs.next()) {
                supermercados.add(rs.getString("supermercado"));
            }
            return supermercados;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }
    /**
     * Obtener la puntuacion media del producto teniendo en cuenta que el mismo nombre y marca puede estar en varios supermercados.
     * hay que tratar a los productos como si fueran iguales si tienen el mismo nombre y marca.
     * Se consulta la tabla puntua y se calcula la media de las puntuaciones del producto.
     * Esta consulta devuelve la puntuación media del producto especificado.
     * @param producto
     * @return
     * @author Daniel Figueroa
     */
    public static double obtenerPuntuacionMediaProducto(String nombreProducto,
                                                        String marcaProducto) {
        // Observa que NO hay ';' al final de la línea WHERE
        final String SQL = """
        SELECT AVG(puntuacion) AS puntuacion_media
        FROM puntua
        WHERE nombre = ?
          AND marca  = ?
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, nombreProducto);
            stmt.setString(2, marcaProducto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("puntuacion_media");
            } else {
                return 0.0;   // Si no hay filas (sin puntuaciones)
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;       // En caso de error, devolvemos 0.0
        }
    }


    /**
     * Obtiene los supermercados donde se vende un producto específico.
     * A partir de un nombre y marca, obtener todos los supermercados donde se vende ese producto.
     * Se consulta la tabla producto y se filtra por el nombre y marca del producto.
     * retorna una lista de supermercados donde se vende el producto.
     * @return
     */
    public static List<String> getSupermercados(Producto producto){
        final String SQL = """
        SELECT DISTINCT supermercado
        FROM producto
        WHERE nombre = ? AND marca = ?;
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getMarca());
            ResultSet rs = stmt.executeQuery();

            List<String> supermercados = new ArrayList<>();
            while (rs.next()) {
                supermercados.add(rs.getString("supermercado"));
            }
            return supermercados;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }
    public static Map<Usuario,Integer> getPuntuaciones(Producto p) {
        final String SQL = """
        SELECT 
          u.email,
          u.nombre_usuario,
          u.contrasena,
          pt.puntuacion
        FROM puntua pt
        JOIN usuarios u
          ON pt.email = u.email
        WHERE pt.nombre        = ?
          AND pt.marca         = ?
          AND pt.supermercado  = ?
        """;

        Map<Usuario,Integer> puntuaciones = new HashMap<>();
        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            // 1) Ponemos los parámetros (nombre, marca y supermercado vienen del objeto Producto)
            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getMarca());
            stmt.setString(3, p.getSupermercado());

            // 2) Ejecutamos y leemos resultados
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario(
                            rs.getString("nombre_usuario"),
                            rs.getString("email"),
                            rs.getString("contrasena")
                    );
                    int punt = rs.getInt("puntuacion");
                    puntuaciones.put(usuario, punt);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener puntuaciones: " + e.getMessage());
            // devolvemos un Map vacío para no romper la vista
        }
        return puntuaciones;
    }

    public static List<Double> getHistorialPrecios(String nombre, String marca) {
        final String SQL = """
        SELECT precio
        FROM historial_precios
        WHERE nombre = ? AND marca = ?
        ORDER BY fecha DESC;
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, nombre);
            stmt.setString(2, marca);
            ResultSet rs = stmt.executeQuery();

            List<Double> precios = new ArrayList<>();
            while (rs.next()) {
                precios.add(rs.getDouble("precio"));
            }
            return precios;

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error en la conexión o consulta
        }
    }

    public static int obtenerCantidadStock(Lista_UnidadFamiliar unidad, Producto producto) {
        final String SQL = """
        SELECT cantidad
        FROM contiene
        WHERE id_lista     = ?
          AND nombre       = ?
          AND marca        = ?
          AND supermercado = ?
        """;

        try (Connection conn = Conexion.abrir();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, unidad.getCodigo());            // id_lista
            stmt.setString(2, producto.getNombre());          // nombre
            stmt.setString(3, producto.getMarca());           // marca
            stmt.setString(4, producto.getSupermercado());    // supermercado

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cantidad");
            }
            return 0;   // Si no hay fila, devolvemos stock = 0

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;  // Error de conexión o SQL
        }
    }
}
