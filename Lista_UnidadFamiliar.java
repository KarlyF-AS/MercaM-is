import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lista_UnidadFamiliar {

    private String nombre;
    private String codigo;
    private String descripcion;
    private List<Usuario> miembros;
    private Map<Integer, Producto> productos; // La cantidad del producto y el producto

    public Lista_UnidadFamiliar(int id, String nombre, String codigo, List<Usuario> miembros, Map<Integer, Producto> productos) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.miembros = new ArrayList<>();
        this.productos = new HashMap<>();
    }

    /**
     * Constructor para crear una unidad familiar con solo un miembro y sin productos.
     * @author Daniel Figueroa
     * @return
     */
    public Lista_UnidadFamiliar(String nombre, String codigo, Usuario miembro) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.miembros = new ArrayList<>();
        this.productos = new HashMap<>();
        addMiembro(miembro); // Añade el miembro al crear la unidad familiar
    }
    public Lista_UnidadFamiliar(String nombre, String codigo, String descripcion) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
    }

    public void addProducto(Producto producto) {
        this.productos.put((int) producto.getCodigoBarras(), producto); // Añade un producto al mapa ( no tengo claro si es necesario el codigo de barras como clave o no)
    }




}
