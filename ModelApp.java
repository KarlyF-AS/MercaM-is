public class ModelApp {
    public static void main(String[] args) {
        //double precio, String categoria, String supermercado, String marca, String nombre, int codigoBarras
        Producto producto = Model.crearProducto( "Leche",
                "Central Lechera Asturiana",
                1.20,
                "Lácteos",
                "Supermercado A",
                123456789);


    }

}
