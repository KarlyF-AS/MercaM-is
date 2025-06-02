import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.HashMap.newHashMap;

public class Supermercado {

    private int id;
    private String nombre;
    private List<Producto> productos;
    private Map<Integer, List<Double>> historialPrecios; // lista historial de precios por producto

    public Supermercado(int id, String nombre, List<Producto> productos, Map<Integer, List<Double>> historialPrecios) {
        this.id = id;
        this.nombre = nombre;
        this.productos = new ArrayList<>();
        this.historialPrecios = new HashMap<>();
    }
    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Map<Integer, List<Double>> getHistorialPrecios() {
        return historialPrecios;
    }

    public void setHistorialPrecios(Map<Integer, List<Double>> historialPrecios) {
        this.historialPrecios = historialPrecios;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        producto.getSupermercados().add(this);
    }

    public void agregarPrecioHistorico(int productoId, double precio) {
        historialPrecios.computeIfAbsent(productoId, k -> new ArrayList<>()).add(precio);
    }
    public List<Double> obtenerHistorialPrecios(int productoId) {
        return historialPrecios.getOrDefault(productoId, new ArrayList<>());
    }
    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
        producto.getSupermercados().remove(this);
    }
    public void actualizarProducto(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigoBarras() == producto.getCodigoBarras()) {
                productos.set(i, producto);
                return;
            }
        }
        agregarProducto(producto);
    }

}
