// Controlador.java

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Map<Integer, Producto> obtenerProductosUnidadFamiliar(Lista_UnidadFamiliar unidad) {
        // Si Model ya devuelve el Map, por qué lo cambiamos ayer?
        return Model.obtenerProductosUnidadFamiliar(unidad);
    }

    // Obtener stock
    public Map<Producto, Integer> obtenerStock(Lista_UnidadFamiliar unidad) {
        return Model.obtenerStock(unidad);
    }

    // Añadir producto al stock
    public void anadirProductoStock(Lista_UnidadFamiliar unidad, Producto producto, int cantidad) {
        Model.añadirProductoStock(unidad, producto, cantidad);
    }

    // Actualizar cantidad en stock
    public void actualizarCantidadStock(Lista_UnidadFamiliar unidad, Producto producto, int cantidad) {
        Model.actualizarCantidadStock(unidad, producto, cantidad);
    }

    // Eliminar producto del stock
    public void eliminarProductoStock(Lista_UnidadFamiliar unidad, Producto producto) {
        Model.eliminarProductoStock(unidad, producto);
    }

    // Obtener categorías (devolver Map de categoría a subcategorías)
    public Map<String, List<String>> obtenerCategorias() {
        List<String> categorias = Model.obtenerCategorias();
        // Quedamos en hacerlo así al final?
        Map<String, List<String>> mapa = new HashMap<>();
        for (String cat : categorias) {
            mapa.put(cat, new ArrayList<>());
        }
        return mapa;
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
    public Producto crearProducto(String nombre, String marca, double precio, String categoria, String subcategoria, long id) {
        // pasar subcategoria a categoria a formateo "categoria.subcategoria"
        if (subcategoria != null && !subcategoria.isEmpty()) {
            categoria = categoria + "." + subcategoria;
        }
        return Model.crearProducto(nombre, marca, precio, categoria, "sinSupermercado", id, null);
    }

    // Añadir supermercado a producto
    public Producto anadirSupermercadoProducto(Producto producto, String supermercado) {
        return Model.anadirSupermercadoProducto(producto, supermercado);
    }

    // Eliminar supermercado de producto
    public Producto eliminarSupermercadoProducto(Producto producto, String supermercado) {
        return Model.eliminarSupermercadoProducto(producto, supermercado);
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

}