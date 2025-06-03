import java.util.List;
import java.util.Map;
import java.util.Set;

public class Producto {
    private int codigoBarras;
    private String nombre;
    private String marca;
    private int puntuacion;
    private double precio;
    private String categoria;
    private Set<Supermercado> supermercados;

    // Datos específicos por familia
    private Map<Integer, Integer> stockPorFamilia;
    private Map<Integer, Integer> minimoPorFamilia;
    private Map<Integer, List<Double>> puntuacionesPorFamilia;

    public Producto(int codigoBarras, String nombre, String marca, int puntuacion, double precio, String categoria) {
        this.codigoBarras = codigoBarras;
        this.nombre = nombre;
        this.marca = marca;
        this.puntuacion = puntuacion;
        this.precio = precio;
        this.categoria = categoria;
    }

    public Producto(int codigoBarras, String nombre, String marca, double precio, String categoria) {
        this.codigoBarras = codigoBarras;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.categoria = categoria;
    }
    // Getters y Setters

    public int getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(int codigoBarras) {
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

    public Set<Supermercado> getSupermercados() {
        return supermercados;
    }

    public void setSupermercados(Set<Supermercado> supermercados) {
        this.supermercados = supermercados;
    }

    public Map<Integer, Integer> getStockPorFamilia() {
        return stockPorFamilia;
    }

    public void setStockPorFamilia(Map<Integer, Integer> stockPorFamilia) {
        this.stockPorFamilia = stockPorFamilia;
    }

    public Map<Integer, Integer> getMinimoPorFamilia() {
        return minimoPorFamilia;
    }

    public void setMinimoPorFamilia(Map<Integer, Integer> minimoPorFamilia) {
        this.minimoPorFamilia = minimoPorFamilia;
    }

    public Map<Integer, List<Double>> getPuntuacionesPorFamilia() {
        return puntuacionesPorFamilia;
    }

    public void setPuntuacionesPorFamilia(Map<Integer, List<Double>> puntuacionesPorFamilia) {
        this.puntuacionesPorFamilia = puntuacionesPorFamilia;
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
                ", supermercados=" + supermercados +
                ", stockPorFamilia=" + stockPorFamilia +
                ", minimoPorFamilia=" + minimoPorFamilia +
                ", puntuacionesPorFamilia=" + puntuacionesPorFamilia +
                '}';
    }

    public int setStockPorFamilia(int familiaId, int stock) {
        return stockPorFamilia.put(familiaId, stock);
    }

    public int getStockPorFamilia(int familiaId) {
        return stockPorFamilia.getOrDefault(familiaId, 0);
    }

    public int setMinimoPorFamilia(int familiaId, int minimo) {
        return minimoPorFamilia.put(familiaId, minimo);
    }
    public int getMinimoPorFamilia(int familiaId) {
        return minimoPorFamilia.getOrDefault(familiaId, 0);
    }
    public List<Double> setPuntuacionesPorFamilia(int familiaId, List<Double> puntuaciones) {
        return puntuacionesPorFamilia.put(familiaId, puntuaciones);
    }
    public List<Double> getPuntuacionesPorFamilia(int familiaId) {
        return puntuacionesPorFamilia.getOrDefault(familiaId, List.of());
    }
    public void addSupermercado(Supermercado supermercado) {
        this.supermercados.add(supermercado);
    }
    public void removeSupermercado(Supermercado supermercado) {
        this.supermercados.remove(supermercado);
    }



}
