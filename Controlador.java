public class Controlador {

    public static Usuario iniciarSesion(String correo, String contraseña) {
        return Model.validarLogin(correo, contraseña);
    }

    public static boolean existeEmail(String nombreEmail) {
        return Model.existeEmail(nombreEmail);
    }

    public static Usuario registrarUsuario(String nombre, String correo, String contraseña) {
        if (!Controller2.filtroContrasena(contraseña) && !Controller2.validarEmail(correo)) {
            return null;
        }
        return Model.crearUsuario(nombre, correo, contraseña);
    }

    public static Lista_UnidadFamiliar obtenerUnidadFamiliar(Usuario usuarioActual) {
        return Model.obtenerUnidadFamiliar(usuarioActual);
    }

    public static Lista_UnidadFamiliar unirseAUnidadFamiliar(Usuario usuario, String codigo) {
        return Model.unirseAUnidadFamiliar(usuario, codigo);
    }

    public static Lista_UnidadFamiliar crearUnidadFamiliar(Usuario usuario, String nombreUnidadFamiliar) {
        return Model.crearUnidadFamiliar(usuario, nombreUnidadFamiliar);
    }

    public static java.util.List<Producto> obtenerTodosProductos(Lista_UnidadFamiliar unidadActual) {
        return Model.obtenerTodosProductos(unidadActual);
    }

    public static java.util.Map<String, java.util.List<String>> obtenerCategorias() {
        return Model.obtenerCategorias();
    }

    public static java.util.List<Producto> obtenerProductosPorSubcategoria(String subcategoria) {
        return Model.obtenerProductosPorSubcategoria(subcategoria);
    }

    public static java.util.List<String> obtenerMarcas() {
        return Model.obtenerMarcas();
    }

    public static java.util.List<Producto> obtenerProductosPorMarca(String marca) {
        return Model.obtenerProductosPorMarca(marca);
    }

    public static java.util.List<Producto> ordenarProductos(int opcion) {
        Controller2.ordenarProductos(opcion);
        return Model.ordenarProductos(opcion);
    }

    public static Producto obtenerProductoPorNombre(String nombre) {
        return Model.obtenerProductoPorNombre(nombre);
    }

    public static void actualizarPrecioProducto(Producto producto, double nuevoPrecio) {
        Model.actualizarPrecioProducto(producto, nuevoPrecio);
    }

    public static void anadirPuntuacionProducto(Producto producto, Usuario usuario, int puntuacion) {
        Model.anadirPuntuacionProducto(producto, usuario, puntuacion);
    }

    public static void anadirSupermercadoProducto(Producto producto, String supermercado) {
        Model.anadirSupermercadoProducto(producto, supermercado);
    }

    public static void eliminarSupermercadoProducto(Producto producto, String supermercado) {
        Model.eliminarSupermercadoProducto(producto, supermercado);
    }

    public static Producto crearProducto(String nombre, String marca, double precio, String categoria, String subcategoria, String id, Lista_UnidadFamiliar unidadActual) {
        String categoriaCompleta = categoria +"."+ subcategoria
        return Model.crearProducto(nombre, marca, precio, categoriaCompleta, id, unidadActual);
    }

    public static java.util.List<Producto> obtenerProductosUnidadFamiliar(Lista_UnidadFamiliar unidadActual) {
        return Model.obtenerProductosUnidadFamiliar(unidadActual);
    }

    public static boolean cambiarNombreUsuario(Usuario usuario, String nuevoNombre) {
        return Model.cambiarNombreUsuario(usuario, nuevoNombre);
    }

    public static boolean cambiarContrasena(Usuario usuario, String actual, String nueva) {
        return Model.cambiarContrasena(usuario, actual, nueva);
    }

    public static void cambiarNombreUnidadFamiliar(Lista_UnidadFamiliar unidad, String nuevoNombre) {
        Model.cambiarNombreUnidadFamiliar(unidad, nuevoNombre);
    }

    public static void abandonarUnidadFamiliar(Usuario usuario, Lista_UnidadFamiliar unidad) {
        Model.abandonarUnidadFamiliar(usuario, unidad);
    }
}