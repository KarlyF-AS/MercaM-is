import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lista_UnidadFamiliar {

    private int id;
    private String nombre;
    private String codigo;
    private List<Usuario> miembros;
    private Map<Integer, Producto> productos;

    public Lista_UnidadFamiliar(int id, String nombre, String codigo, List<Usuario> miembros, Map<Integer, Producto> productos) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.miembros = new ArrayList<>();
        this.productos = new HashMap<>();
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<Usuario> getMiembros() {
        return miembros;
    }

    public void setMiembros(List<Usuario> miembros) {
        this.miembros = miembros;
    }

    public Map<Integer, Producto> getProductos() {
        return productos;
    }

    public void setProductos(Map<Integer, Producto> productos) {
        this.productos = productos;
    }

    public  void addMiembro(Usuario miembro) {
        this.miembros.add(miembro); // Añade un miembro a la lista y quizas tengo que hacerlo a la inversa???
        miembro.setFamilia(this); // Establece la familia del miembro
    }

    public void addProducto(Producto producto) {
        this.productos.put(producto.getId(), producto); // Añade un producto al mapa
    }




}
