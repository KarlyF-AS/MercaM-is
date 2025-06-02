import java.util.HashMap;

public class App {
    public static void main(String[] args) {

        // Aquí puedes crear instancias de Usuario, Producto, Lista_UnidadFamiliar y ListaCompra
        // para probar la funcionalidad las cosas.

        // Ejemplo de creación de un usuario
        Usuario usuario = new Usuario(1, "Juan Perez", "dani@gmail.com", "password123");
        // Ejemplo de creación de un producto
        Producto producto = new Producto(1, "Leche", "Larsa", 1, 11,"Lacteo");
        // Ejemplo de creación de una lista de compra
        ListaCompra listaCompra = new ListaCompra(1, 1, "Lista de la compra", new HashMap<>(), false);
        // Agregar un producto a la lista de compra
        listaCompra.agregarProducto(1, 2); // Agrega 2 unidades del producto con código de barras 1

        // Ejemplo de generar el archivo de texto de la lista de compra
        listaCompra.generarArchivoTxt("lista_compra.txt");
    }
}
