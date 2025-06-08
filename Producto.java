import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/**
 * Representa un producto con información relevante como nombre, marca, precio,
 * categoría, supermercado, código de barras, puntuación y descripción.
 *
 * Esta clase incluye lógica para obtener subcategorías, historial de precios
 * y puntuaciones mediante el {@link Controlador}.
 */

public class Producto {
    private long codigoBarras;
    private String nombre;
    private String marca;
    private int puntuacion;
    private double precio;
    private String categoria;
    private String supermercado;
    private String descripcion;


    // Getters y Setters


    public Producto(double precio, String categoria, String supermercado, String marca, String nombre, long codigoBarras) {
        this.precio = precio;
        this.categoria = categoria;
        this.supermercado = supermercado;
        this.marca = marca;
        this.nombre = nombre;
        this.codigoBarras = codigoBarras;
    }

    public Producto(long codigoBarras, String nombre, String marca, double precio, String categoria, String supermercado, String descripcion) {
        this.codigoBarras = codigoBarras;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.categoria = categoria;
        this.supermercado = supermercado;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "codigoBarras=" + codigoBarras +
                ", nombre='" + nombre + '\'' +
                ", marca='" + marca + '\'' +
                ", puntuacion=" + puntuacion +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' +
                ", supermercado='" + supermercado + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
    /**
     * Obtiene la subcategoría a partir del campo {@code categoria}.
     * @return la subcategoría si está definida; {@code null} en caso contrario.
     */
    public String getSubcategoria() {
        //la categoria tiene el formato categoria.subcategoria, solo devolver lo que hay después del punto
        if (categoria != null && categoria.contains(".")) {
            return categoria.substring(categoria.indexOf('.') + 1);
        }
        return null; // o lanzar una excepción si no se encuentra la subcategoría
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(long codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    /**
     * Devuelve solo la categoría principal antes del punto.
     * @return la categoría principal.
     */
    public String getCategoria() {
        //la categoria tiene el formato categoria.subcategoria, devolver solo lo que hay antes del punto
        if (categoria != null && categoria.contains(".")) {
            return categoria.substring(0, categoria.indexOf('.'));
        }
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }
    /**
     * Devuelve la puntuación media del producto usando el {@link Controlador}.
     * @return puntuación media como {@code double}.
     */
    public double getPuntuacionMedia() {
        // Devuelve la puntuación media del producto
        return Controlador.getPuntuacionMedia(this.nombre,this.marca);
    }
    /**
     * Retorna el historial de precios del producto.
     * @return lista de precios históricos.
     */
    public List<Double> getHistorialPrecios() {
        // Devuelve el historial de precios del producto
        return Controlador.getHistorialPrecio(this.nombre, this.marca);
    }
    /**
     * Determina si dos productos son iguales según su código de barras, nombre y marca.
     * @param o objeto a comparar
     * @return {@code true} si son equivalentes, {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return codigoBarras == producto.codigoBarras && Objects.equals(nombre, producto.nombre) && Objects.equals(marca, producto.marca);
    }
    /**
     * Devuelve el hash del producto basado en código de barras, nombre y marca.
     * @return código hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(codigoBarras, nombre, marca);
    }
}
