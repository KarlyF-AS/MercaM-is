/**
 * Representa un usuario del sistema con información básica como nombre, email, contraseña
 * y una lista de unidades familiares asociadas.
 */
public class Usuario {
    private String nombre;
    private String email;
    private String password;
    private Lista_UnidadFamiliar familia;

    // Constructor con ID (aunque no se usa en esta clase)
    public Usuario(int id, String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    // Constructor sin ID
    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    /**
     * Devuelve el nombre del usuario.
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el nombre del usuario.
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el email del usuario.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Cambia el email del usuario.
     * @param email nuevo email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve la contraseña del usuario.
     * @return contraseña
     */
    public String getPassword() {
        return password;
    }

    /**
     * Cambia la contraseña del usuario.
     * @param password nueva contraseña
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Devuelve la familia del usuario.
     * @return objeto Lista_UnidadFamiliar
     */
    public Lista_UnidadFamiliar getFamilia() {
        return familia;
    }

    /**
     * Asigna una familia al usuario.
     * @param familia objeto Lista_UnidadFamiliar
     */
    public void setFamilia(Lista_UnidadFamiliar familia) {
        this.familia = familia;
    }
}