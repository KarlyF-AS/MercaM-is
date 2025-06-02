import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ListaCompra {

    private int id;
    private int familiaId;
    private String nombre;
    private Map<Integer, Integer> productosCantidad; // Map<codigoBarras, cantidad>
    private boolean completada;

    public ListaCompra(int id, int familiaId, String nombre, Map<Integer, Integer> productos, boolean completada) {
        this.id = id;
        this.familiaId = familiaId;
        this.nombre = nombre;
        this.productosCantidad = new HashMap<>();
        this.completada = false;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFamiliaId() {
        return familiaId;
    }

    public void setFamiliaId(int familiaId) {
        this.familiaId = familiaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Map<Integer, Integer> getProductosCantidad() {
        return productosCantidad;
    }

    public void setProductosCantidad(Map<Integer, Integer> productosCantidad) {
        this.productosCantidad = productosCantidad;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public void agregarProducto(int codigoBarras, int cantidad) {
        productosCantidad.put(codigoBarras, cantidad);
    }

    public void eliminarProducto(int codigoBarras) {
        productosCantidad.remove(codigoBarras);
    }

    /**
     * Genera un archivo de texto con la información de la lista de compra.
     * @param rutaArchivo Ruta donde se guardará el archivo.
     */
    public void generarArchivoTxt(String rutaArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write("Lista de la Compra\n");
            writer.write("ID: " + id + "\n");
            writer.write("Familia ID: " + familiaId + "\n");
            writer.write("Nombre: " + nombre + "\n");
            writer.write("Completada: " + (completada ? "Sí" : "No") + "\n");
            writer.write("Productos:\n");
            for (Map.Entry<Integer, Integer> entry : productosCantidad.entrySet()) {
                writer.write("  Código de Barras: " + entry.getKey() + ", Cantidad: " + entry.getValue() + "\n");
            }
            writer.write("Fin de la lista.\n");
        } catch (IOException e) {
            System.err.println("Error al generar el archivo: " + e.getMessage());
        }
    }
}
