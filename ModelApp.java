/**
 * Clase de prueba para crear un producto usando la lógica del modelo.
 * Crea una instancia de {@link Producto} utilizando el método {@code crearProducto} de {@link Model}.
 */
public class ModelApp {
    public static void main(String[] args) {
        //double precio, String categoria, String supermercado, String marca, String nombre, int codigoBarras
        Producto producto2 = Model.crearProducto(
                "Pan",
                "Panadería Artesana",
                0.80,
                "Panadería",
                "Supermercado d",
                987654328,
                "Pan fresco de masa madre"
        );
    }
}
