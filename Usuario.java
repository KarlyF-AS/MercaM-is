public class Usuario {
    private String nombre;
    private String email;
    private String password;
    private Lista_UnidadFamiliar familia;

    public Usuario(int id, String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }
    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Lista_UnidadFamiliar getFamilia() {
        return familia;
    }
    public void setFamilia(Lista_UnidadFamiliar familia) {
        this.familia = familia;
    }
}
