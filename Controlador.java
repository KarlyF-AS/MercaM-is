public class Controlador {

    public static Usuario iniciarSesion(String correo, String contraseña) {
        return Model.validarLogin(correo, contraseña);
    }

    public static existeEmail(String nombreEmail) {
        return Model.existeEmail(nombreEmail);
    }
    public static registrarUsuario (String nombre, String correo, String contraseña) {
        return Model.registrarUsuario(nombre, correo, contraseña);
    }

    public static obtenerUnidadFamiliar(String usuarioActual) {
        return Model.obtenerUnidadFamiliar(usuarioActual);
    }

    public static unirseUnidadFamiliar(String codigo, String usuarioActual) {
        return Model.unirseUnidadFamiliar(codigo, usuarioActual);
    }

    public static crearUnidadFamiliar(String usuarioActual, String nombreUnidadFamiliar) {
        return Model.crearUnidadFamiliar(usuarioActual, nombreUnidadFamiliar);
    }

    public static obtenerTodosProductos(String unidadActual) {
        return Model.obtenerTodosProductos(unidadActual);
    }

    public static obtenerCategorias () {
        return Model.obtenerCategorias();
    }

    public static obtenerProductosPorSubcategoria(String subcategoria) {
        return Model.obtenerProductosPorSubcategoria(subcategoria);
    }

    public static obtenerMarcas(){
        return Model.obtenerMarcas();
    }

    public static obtenerProductosPorMarca(marca) {
        return Model.obtenerProductosPorMarca(marca);
    }
}
