// Controlador.java

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador principal que actúa como intermediario entre la vista y el modelo.
 * Gestiona todas las operaciones relacionadas con usuarios, unidades familiares y productos.
 */
public class Controlador {

    /**
     * Valida las credenciales de un usuario para iniciar sesión.
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Objeto Usuario si las credenciales son válidas, null en caso contrario
     */
    public Usuario iniciarSesion(String email, String password) {
        return Model.validarLogin(email, password);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @param nombre Nombre del usuario
     * @param email Email del usuario (debe ser único)
     * @param password Contraseña del usuario
     * @return Objeto Usuario creado, o null si el email ya existe
     */
    public Usuario registrarUsuario(String nombre, String email, String password) {
        return Model.registrarUsuario(nombre, email, password);
    }

    /**
     * Verifica si un email ya está registrado en el sistema.
     * @param email Email a verificar
     * @return true si el email existe, false en caso contrario
     */
    public boolean existeEmail(String email) {
        return Model.existeEmail(email);
    }

    /**
     * Obtiene la unidad familiar a la que pertenece un usuario.
     * @param usuario Usuario del que obtener la unidad familiar
     * @return Objeto Lista_UnidadFamiliar, o null si no pertenece a ninguna
     */
    public Lista_UnidadFamiliar obtenerUnidadFamiliar(Usuario usuario) {
        return Model.obtenerUnidadFamiliar(usuario);
    }

    /**
     * Permite a un usuario unirse a una unidad familiar existente.
     * @param usuario Usuario que quiere unirse
     * @param codigo Código de la unidad familiar
     * @return Objeto Lista_UnidadFamiliar si tuvo éxito, null en caso contrario
     */
    public Lista_UnidadFamiliar unirseAUnidadFamiliar(Usuario usuario, String codigo) {
        return Model.unirseAUnidadFamiliar(usuario, codigo);
    }

    /**
     * Crea una nueva unidad familiar.
     * @param usuario Usuario que crea la unidad (será el primer miembro)
     * @param nombre Nombre de la unidad familiar
     * @param codigo Código único para la unidad familiar
     * @return Objeto Lista_UnidadFamiliar creado, o null si hubo error
     */
    public Lista_UnidadFamiliar crearUnidadFamiliar(Usuario usuario, String nombre, String codigo) {
        return Model.crearUnidadFamiliar(usuario, nombre, codigo);
    }

    /**
     * Obtiene todos los productos disponibles en el sistema.
     * @return Lista de todos los productos
     */
    public List<Producto> obtenerTodosProductos() {
        return Model.obtenerTodosProductos();
    }

    /**
     * Obtiene los productos de una unidad familiar con sus cantidades.
     * @param unidad Unidad familiar de la que obtener los productos
     * @return Mapa de Producto a cantidad (Integer)
     */
    public Map<Producto, Integer> obtenerProductosUnidadFamiliar(Lista_UnidadFamiliar unidad) {
        return Model.obtenerProductosUnidadFamiliar(unidad);
    }

    /**
     * Obtiene el stock de productos de una unidad familiar.
     * @param unidad Unidad familiar de la que obtener el stock
     * @return Mapa de cantidades (Integer) a Productos
     */
    public Map<Integer, Producto> obtenerStock(Lista_UnidadFamiliar unidad) {
        return Model.stock(unidad);
    }

    /**
     * Añade un producto al stock de una unidad familiar.
     * @param unidad Unidad familiar a la que añadir el producto
     * @param producto Producto a añadir
     * @param cantidad Cantidad a añadir
     */
    public void anadirProductoStock(Lista_UnidadFamiliar unidad, Producto producto, int cantidad) {
        Model.añadirProductoStock(unidad, producto, cantidad);
    }
    /**
     * Inicializar el stock de una unidad familiar con productos iniciales.
     */


    /**
     * Actualiza la cantidad de un producto en el stock.
     * @param unidad Unidad familiar que contiene el producto
     * @param producto Producto a actualizar
     * @param cantidad Nueva cantidad
     * @return Cantidad actualizada, o -1 si hubo error
     */
    public int actualizarCantidadStock(Lista_UnidadFamiliar unidad, Producto producto, int cantidad) {
        return Model.actualizarCantidadStock(unidad, producto, cantidad);
    }

    /**
     * Elimina un producto del stock de una unidad familiar.
     * @param unidad Unidad familiar de la que eliminar el producto
     * @param producto Producto a eliminar
     * @return Cantidad que había antes de eliminar, 0 si no existía, -1 si hubo error
     */
    public int eliminarProductoStock(Lista_UnidadFamiliar unidad, Producto producto) {
        return Model.eliminarProductoStock(unidad, producto);
    }

    /**
     * Obtiene las categorías principales de productos (sin subcategorías).
     * @return Lista de categorías principales únicas
     */
    public List<String> obtenerCategoriasPrincipales() {
        List<String> raw = Model.obtenerCategorias();
        Set<String> únicas = new LinkedHashSet<>();
        for (String full : raw) {
            int dot = full.indexOf('.');
            String cat = dot >= 0 ? full.substring(0, dot) : full;
            únicas.add(cat);
        }
        return new ArrayList<>(únicas);
    }

    /**
     * Obtiene las subcategorías de una categoría principal.
     * @param categoria Categoría principal
     * @return Lista de subcategorías
     */
    public List<String> obtenerSubcategorias(String categoria) {
        List<String> raw = Model.obtenerCategorias();
        return raw.stream()
                .filter(s -> s.startsWith(categoria + "."))
                .map(s -> s.substring(s.indexOf('.') + 1))
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las marcas de productos disponibles.
     * @return Lista de marcas
     */
    public List<String> obtenerMarcas() {
        return Model.obtenerMarcas();
    }

    /**
     * Obtiene todos los supermercados disponibles.
     * @return Lista de supermercados
     */
    public List<String> obtenerTodosSupermercados() {
        return Model.obtenerTodosSupermercados();
    }

    /**
     * Filtra productos por supermercado.
     * @param supermercado Supermercado por el que filtrar
     * @return Lista de productos del supermercado especificado
     */
    public List<Producto> filtrarPorSupermercado(String supermercado) {
        return Model.filtrarPorSupermercado(supermercado);
    }

    /**
     * Obtiene productos por subcategoría.
     * @param subcategoria Subcategoría completa (formato "Categoria.Subcategoria")
     * @return Lista de productos de la subcategoría
     */
    public List<Producto> obtenerProductosPorSubcategoria(String subcategoria) {
        return Model.obtenerProductosPorSubcategoria(subcategoria);
    }

    /**
     * Obtiene productos por marca.
     * @param marca Marca por la que filtrar
     * @return Lista de productos de la marca especificada
     */
    public List<Producto> obtenerProductosPorMarca(String marca) {
        return Model.obtenerProductosPorMarca(marca);
    }

    /**
     * Ordena productos según criterio especificado.
     * @param opcion Criterio de ordenación (1: nombre, 2: precio, etc.)
     * @return Lista de productos ordenada
     */
    public List<Producto> ordenarProductos(int opcion) {
        return Controller2.ordenarProductos(opcion);
    }

    /**
     * Busca productos por nombre.
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de productos que coinciden
     */
    public List<Producto> obtenerProductoPorNombre(String nombre) {
        return Model.obtenerProductoPorNombre(nombre);
    }

    /**
     * Crea un nuevo producto en el sistema.
     * @param nombre Nombre del producto
     * @param marca Marca del producto
     * @param precio Precio del producto
     * @param categoria Categoría del producto
     * @param id Código de barras del producto
     * @param descripcion Descripción del producto
     * @return Producto creado, o null si hubo error
     */
    public Producto crearProducto(String nombre, String marca, double precio, String categoria, long id, String descripcion) {
        return Model.crearProducto(nombre, marca, precio, categoria, "sinSupermercado", id, descripcion);
    }

    /**
     * Añade un supermercado a un producto existente.
     * @param producto Producto a modificar
     * @param supermercado Supermercado a añadir
     * @return Producto modificado, o null si hubo error
     */
    public Producto anadirSupermercadoProducto(Producto producto, String supermercado) {
        return Model.anadirSupermercadoProducto(producto, supermercado);
    }

    /**
     * Elimina un supermercado de un producto.
     * @param producto Producto a modificar
     * @param supermercado Supermercado a eliminar
     * @return Producto modificado, o null si hubo error
     */
    public Producto eliminarSupermercadoProducto(Producto producto, String supermercado) {
        return Model.eliminarSupermercadoProducto(producto, supermercado);
    }

    /**
     * Busca un producto por su código de barras.
     * @param codigoBarras Código de barras a buscar
     * @return Lista de productos con ese código (normalmente 1 o 0 elementos)
     */
    public List<Producto> buscarProductoPorCodigoBarras(long codigoBarras) {
        return Model.buscarProductoPorCodigoBarras(codigoBarras);
    }

    /**
     * Actualiza el precio de un producto.
     * @param producto Producto a actualizar
     * @param nuevoPrecio Nuevo precio
     */
    public void actualizarPrecioProducto(Producto producto, double nuevoPrecio) {
        Model.actualizarPrecioProducto(producto, nuevoPrecio);
    }

    /**
     * Añade una puntuación a un producto por parte de un usuario.
     * @param producto Producto a puntuar
     * @param usuario Usuario que puntúa
     * @param puntuacion Puntuación (normalmente 1-5)
     */
    public void anadirPuntuacionProducto(Producto producto, Usuario usuario, int puntuacion) {
        Model.anadirPuntuacionProducto(producto, usuario, puntuacion);
    }

    /**
     * Cambia el nombre de un usuario.
     * @param usuario Usuario a modificar
     * @param nuevoNombre Nuevo nombre
     * @return true si tuvo éxito, false en caso contrario
     */
    public boolean cambiarNombreUsuario(Usuario usuario, String nuevoNombre) {
        return Model.cambiarNombreUsuario(usuario, nuevoNombre);
    }

    /**
     * Cambia la contraseña de un usuario.
     * @param usuario Usuario a modificar
     * @param actual Contraseña actual
     * @param nueva Nueva contraseña
     * @return true si tuvo éxito, false en caso contrario
     */
    public boolean cambiarContrasena(Usuario usuario, String actual, String nueva) {
        return Model.cambiarContrasena(usuario, actual, nueva);
    }

    /**
     * Cambia el nombre de una unidad familiar.
     * @param unidad Unidad familiar a modificar
     * @param nuevoNombre Nuevo nombre
     * @return true si tuvo éxito, false en caso contrario
     */
    public boolean cambiarNombreUnidadFamiliar(Lista_UnidadFamiliar unidad, String nuevoNombre) {
        return Model.cambiarNombreUnidadFamiliar(unidad, nuevoNombre);
    }

    /**
     * Permite a un usuario abandonar una unidad familiar.
     * @param usuario Usuario que abandona
     * @param unidad Unidad familiar que abandona
     * @return true si tuvo éxito, false en caso contrario
     */
    public boolean abandonarUnidadFamiliar(Usuario usuario, Lista_UnidadFamiliar unidad) {
        return Model.abandonarUnidadFamiliar(usuario, unidad);
    }

    /**
     * Modifica la cantidad de un producto en una unidad familiar.
     * @param unidad Unidad familiar que contiene el producto
     * @param producto Producto a modificar
     * @param cantidad Cantidad a añadir/restar (puede ser negativo)
     * @return Nueva cantidad, o -1 si hubo error
     */
    public int modificarCantidadProducto(Lista_UnidadFamiliar unidad, Producto producto, int cantidad) {
        return Model.modificarCantidadProducto(unidad, producto, cantidad);
    }

    /**
     * Busca productos cuyo nombre contenga un fragmento de texto.
     * @param fragmento Texto a buscar (no sensible a mayúsculas)
     * @return Lista de productos que coinciden
     */
    public List<Producto> buscarProductosPorFragmento(String fragmento) {
        return Model.recogerTodosProductos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(fragmento.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un producto por su nombre y marca exactos.
     * @param nombre Nombre exacto del producto
     * @param marca Marca exacta del producto
     * @return Producto encontrado, o null si no existe
     */
    public Producto obtenerProductoPorNombreYMarca(String nombre, String marca) {
        return Model.recogerTodosProductos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre) && p.getMarca().equalsIgnoreCase(marca))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene los supermercados donde está disponible un producto.
     * @param producto Producto a consultar
     * @return Lista de supermercados donde está disponible
     */
    public static List<String> getSupermercados(Producto producto) {
        return Model.getSupermercados(producto);
    }

    /**
     * Obtiene la puntuación media de un producto.
     * @param nombre Nombre del producto
     * @param marca Marca del producto
     * @return Puntuación media (0 si no hay puntuaciones)
     */
    public static double getPuntuacionMedia(String nombre, String marca) {
        return Model.obtenerPuntuacionMediaProducto(nombre, marca);
    }

    /**
     * Obtiene las puntuaciones individuales de un producto.
     * @param p Producto a consultar
     * @return Mapa de Usuario a puntuación (Integer)
     */
    public static Map<Usuario, Integer> getPuntuaciones(Producto p) {
        return Model.getPuntuaciones(p);
    }

    /**
     * Obtiene el historial de precios de un producto.
     * @param nombre Nombre del producto
     * @param marca Marca del producto
     * @return Lista de precios históricos ordenados por fecha (más reciente primero)
     */
    public static List<Double> getHistorialPrecio(String nombre, String marca) {
        return Model.getHistorialPrecios(nombre, marca);
    }

    /**
     * Obtiene la cantidad de un producto en el stock de una unidad familiar.
     * @param unidad Unidad familiar a consultar
     * @param producto Producto a consultar
     * @return Cantidad en stock, 0 si no existe, -1 si hubo error
     */
    public int obtenerCantidadStock(Lista_UnidadFamiliar unidad, Producto producto) {
        return Model.obtenerCantidadStock(unidad, producto);
    }

    /**
     * Obtiene los productos con sus cantidades en stock para una unidad familiar.
     * @param unidad Unidad familiar a consultar
     * @return Mapa de cantidad (Integer) a Producto
     */
    public Map<Integer,Producto> obtenerProductosConStock(Lista_UnidadFamiliar unidad) {
        Map<Integer,Producto> orig = Model.obtenerListaDeProductosConStock(unidad).getProductos();
        Map<Integer,Producto> indexMap = new LinkedHashMap<>();
        return indexMap;
    }

    /**
     * Obtiene solo los productos que tienen stock (cantidad > 0) en una unidad familiar.
     * @param unidad Unidad familiar a consultar
     * @return Lista de productos con stock
     */
    public List<Producto> obtenerListaProductosConStock(Lista_UnidadFamiliar unidad) {
        Map<Integer,Producto> stockMap = obtenerProductosConStock(unidad);
        return new ArrayList<>(stockMap.values());
    }
}