import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public String getCategoria() {
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
}
