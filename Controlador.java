// Controlador.java

import java.util.*;
import java.util.stream.Collectors;

public class Controlador {
    // Iniciar sesión
    public Usuario iniciarSesion(String email, String password) {
        return Model.validarLogin(email, password);
    }

    // Registrar usuario
    public Usuario registrarUsuario(String nombre, String email, String password) {
        return Model.registrarUsuario(nombre, email, password);
    }

    // Comprobar si existe email
    public boolean existeEmail(String email) {
        return Model.existeEmail(email);
    }

    // Obtener unidad familiar
    public Lista_UnidadFamiliar obtenerUnidadFamiliar(Usuario usuario) {
        return Model.obtenerUnidadFamiliar(usuario);
    }

    // Unirse a unidad familiar
    public Lista_UnidadFamiliar unirseAUnidadFamiliar(Usuario usuario, String codigo) {
        return Model.unirseAUnidadFamiliar(usuario, codigo);
    }

    // Crear unidad familiar
    public Lista_UnidadFamiliar crearUnidadFamiliar(Usuario usuario, String nombre, String codigo) {
        return Model.crearUnidadFamiliar(usuario, nombre, codigo);
    }


    // Obtener todos los productos de la unidad familiar
    public List<Producto> obtenerTodosProductos() {
        return Model.obtenerTodosProductos();
    }

    // Obtener productos de la unidad familiar
    public Map<Producto, Integer> obtenerProductosUnidadFamiliar(Lista_UnidadFamiliar unidad) {
        // Si Model ya devuelve el Map, por qué lo cambiamos ayer?
        return Model.obtenerProductosUnidadFamiliar(unidad);
    }

    // Obtener stock
    public Map<Integer, Producto> obtenerStock(Lista_UnidadFamiliar unidad) {
        return Model.stock(unidad);
    }

    // Añadir producto al stock
    public void anadirProductoStock(Lista_UnidadFamiliar unidad, Producto producto, int cantidad) {
        Model.añadirProductoStock(unidad, producto, cantidad);
    }

    // Actualizar cantidad en stock
    public int actualizarCantidadStock(Lista_UnidadFamiliar unidad, Producto producto, int cantidad) {
        return Model.actualizarCantidadStock(unidad, producto, cantidad);
    }

    // Eliminar producto del stock
    public int eliminarProductoStock(Lista_UnidadFamiliar unidad, Producto producto) {
        return Model.eliminarProductoStock(unidad, producto);
    }

    public List<String> obtenerCategoriasPrincipales() {
        List<String> raw = Model.obtenerCategorias(); // ["A.B", "A.C", "D.E", ...]
        Set<String> únicas = new LinkedHashSet<>();
        for (String full : raw) {
            int dot = full.indexOf('.');
            String cat = dot >= 0 ? full.substring(0, dot) : full;
            únicas.add(cat);
        }
        return new ArrayList<>(únicas);
    }

    public List<String> obtenerSubcategorias(String categoria) {
        List<String> raw = Model.obtenerCategorias(); // ["A.B", "A.C", ...]
        return raw.stream()
                .filter(s -> s.startsWith(categoria + "."))
                .map(s -> s.substring(s.indexOf('.') + 1))
                .distinct()
                .collect(Collectors.toList());
    }
    // Obtener marcas
    public List<String> obtenerMarcas() {
        return Model.obtenerMarcas();
    }

    // Obtener todos los supermercados
    public List<String> obtenerTodosSupermercados() {
        return Model.obtenerTodosSupermercados();
    }

    // Filtrar productos por supermercado
    public List<Producto> filtrarPorSupermercado(String supermercado) {
        return Model.filtrarPorSupermercado(supermercado);
    }

    // Obtener productos por subcategoría
    public List<Producto> obtenerProductosPorSubcategoria(String subcategoria) {
        return Model.obtenerProductosPorSubcategoria(subcategoria);
    }

    // Obtener productos por marca
    public List<Producto> obtenerProductosPorMarca(String marca) {
        return Model.obtenerProductosPorMarca(marca);
    }

    // Ordenar productos según opción
    public List<Producto> ordenarProductos(int opcion) {
        return Controller2.ordenarProductos(opcion);
    }

    // Obtener producto por nombre
    public List<Producto> obtenerProductoPorNombre(String nombre) {
        return Model.obtenerProductoPorNombre(nombre);
    }

    // Crear producto
    public Producto crearProducto(String nombre, String marca, double precio, String categoria,long id, String descripcion) {
        return Model.crearProducto(nombre, marca, precio, categoria, "sinSupermercado", id, descripcion);
    }

    // Añadir supermercado a producto
    public Producto anadirSupermercadoProducto(Producto producto, String supermercado) {
        return Model.anadirSupermercadoProducto(producto, supermercado);
    }

    // Eliminar supermercado de producto
    public Producto eliminarSupermercadoProducto(Producto producto, String supermercado) {
        return Model.eliminarSupermercadoProducto(producto, supermercado);
    }
    public List<Producto> buscarProductoPorCodigoBarras(long codigoBarras) {
        // Busca un producto por su código de barras
        return Model.buscarProductoPorCodigoBarras(codigoBarras);
    }

    // Actualizar precio de producto
    public void actualizarPrecioProducto(Producto producto, double nuevoPrecio) {
        Model.actualizarPrecioProducto(producto, nuevoPrecio);
    }

    // Añadir puntuación a producto
    public void anadirPuntuacionProducto(Producto producto, Usuario usuario, int puntuacion) {
        Model.anadirPuntuacionProducto(producto, usuario, puntuacion);
    }

    // Cambiar nombre de usuario
    public boolean cambiarNombreUsuario(Usuario usuario, String nuevoNombre) {
        return Model.cambiarNombreUsuario(usuario, nuevoNombre);
    }

    // Cambiar contraseña
    public boolean cambiarContrasena(Usuario usuario, String actual, String nueva) {
        return Model.cambiarContrasena(usuario, actual, nueva);
    }

    // Cambiar nombre de unidad familiar
    public boolean cambiarNombreUnidadFamiliar(Lista_UnidadFamiliar unidad, String nuevoNombre) {
        return Model.cambiarNombreUnidadFamiliar(unidad, nuevoNombre);
    }

    // Abandonar unidad familiar
    public boolean abandonarUnidadFamiliar(Usuario usuario, Lista_UnidadFamiliar unidad) {
        return Model.abandonarUnidadFamiliar(usuario, unidad);
    }

    public int modificarCantidadProducto(Lista_UnidadFamiliar unidad, Producto producto, int cantidad) {
        return Model.modificarCantidadProducto(unidad, producto, cantidad);
    }

    // PRUEBAS
    public List<Producto> buscarProductosPorFragmento(String fragmento) {
        // Busca productos cuyo nombre contenga el fragmento (ignorando mayúsculas/minúsculas)
        return Model.recogerTodosProductos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(fragmento.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Producto obtenerProductoPorNombreYMarca(String nombre, String marca) {
        // Busca el producto que coincida exactamente con el nombre y la marca
        return Model.recogerTodosProductos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre) && p.getMarca().equalsIgnoreCase(marca))
                .findFirst()
                .orElse(null);
    }
    public static List<String> getSupermercados(Producto producto){
        return Model.getSupermercados(producto);
    }
    public static double getPuntuacionMedia(String nombre, String marca) {
        return Model.obtenerPuntuacionMediaProducto(nombre, marca);
    }
    public static Map<Usuario, Integer> getPuntuaciones(Producto p){
        return Model.getPuntuaciones(p);
    }
    public static List<Double> getHistorialPrecio(String nombre, String marca) {
        return Model.getHistorialPrecios(nombre, marca);
    }
    public int obtenerCantidadStock(Lista_UnidadFamiliar unidad, Producto producto) {
        // Devuelve la cantidad de un producto en el stock de la unidad familiar
        return Model.obtenerCantidadStock(unidad, producto);
    }
    /** Devuelve el map de Producto→cantidad para la unidad dada */
    /**
     * Devuelve un Map<stock, producto> para la unidad dada.
     * La clave es la cantidad en stock (Integer),
     * el valor es el Producto correspondiente.
     */
    public Map<Integer,Producto> obtenerProductosConStock(Lista_UnidadFamiliar unidad) {
        // 1) Recogemos el Map<Producto,cantidad> filtrado
        Map<Integer,Producto> orig = Model.obtenerListaDeProductosConStock(unidad).getProductos();
        // 2) Creamos un LinkedHashMap para preservar orden
        Map<Integer,Producto> indexMap = new LinkedHashMap<>();
        return indexMap;
    }

    /**
     * Devuelve sólo la lista de Productos que tienen stock (cantidad > 0)
     * a partir del Map<stock, producto> (cantidad→producto).
     */
    public List<Producto> obtenerListaProductosConStock(Lista_UnidadFamiliar unidad) {
        // Obtenemos el Map<cantidad, producto>
        Map<Integer,Producto> stockMap = obtenerProductosConStock(unidad);
        // Devolvemos todos los productos (los values del map)
        return new ArrayList<>(stockMap.values());
    }

}