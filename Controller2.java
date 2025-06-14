import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * Controlador secundario para ordenar productos y validar datos de entrada.
 * @author Karly Albarrán
 */
public class Controller2 {

    /**
     * Ordena productos según la opción seleccionada (precio, nombre o marca).
     * @param opcion número que indica el criterio de orden.
     * @return lista de productos ordenada.
     */

    public static List<Producto> ordenarProductos(int opcion) {
        List<Producto> productos = Model.recogerTodosProductos();

        switch (opcion) {
            case 1: // Precio total (más barato primero)
                productos.sort(Comparator.comparing(Producto::getPrecio));
                break;

            case 2: // Precio total (más caro primero)
                productos.sort(Comparator.comparing(Producto::getPrecio).reversed());
                break;

            case 3: // Precio por unidad (más barato primero)
                productos.sort(Comparator.comparing(Producto::getPrecio));
                break;

            case 4: // Precio por unidad (más caro primero)
                productos.sort(Comparator.comparing(Producto::getPrecio).reversed());
                break;

            case 5: // Nombre (A-Z)
                productos.sort(Comparator.comparing(Producto::getNombre,
                        String.CASE_INSENSITIVE_ORDER));
                break;

            case 6: // Nombre (Z-A)
                productos.sort(Comparator.comparing(Producto::getNombre,
                        String.CASE_INSENSITIVE_ORDER).reversed());
                break;

            case 7: // Marca (A-Z)
                productos.sort(Comparator.comparing(Producto::getMarca,
                        String.CASE_INSENSITIVE_ORDER));
                break;

            case 8: // Marca (Z-A)
                productos.sort(Comparator.comparing(Producto::getMarca,
                        String.CASE_INSENSITIVE_ORDER).reversed());
                break;

            default:
                System.out.println("Opción de ordenamiento no válida");
        }

        return productos;
    }

    /**
     * Verifica si una contraseña tiene al menos 8 caracteres, 4 letras y 2 dígitos.
     * @param password contraseña a verificar.
     * @author Karly Albarrán
     * @return "true" si cumple los requisitos.
     */
        //Filtro de contraseña
        public static Boolean filtroContrasena(String password) {
         if (password == null || password.length() < 8) return false;
         int letras = 0, digitos = 0;

         for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) letras++;
            else if (Character.isDigit(c)) digitos++;
        }

        return letras >= 4 && digitos >= 2;
    }
    /**
     * Verifica si un correo electrónico tiene formato válido.
     * @param email email a verificar.
     * @return true si el formato es correcto, false si no.
     */
        //Filtro de email
         public static boolean validarEmail(String email) {
        if (email == null) return false;

        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    /**
     * Verfica el nombre del usuario. El programa permite espacios, tildes y otros caracteres.
     * @param nombre nombre a verificar.
     * @return "null" si es válido; mensaje de error si hay algún carácter inválido.
     */
    //Filtro de nombre
    public static String validarNombreUsuario(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre no puede estar vacío.";
        }
        nombre = nombre.trim();
        for (char c : nombre.toCharArray()) {
            if (!(Character.isLetterOrDigit(c)
                    || c == ' ' || c == '_' || c == '.' || c == '\'' || c == '-')) {
                return "Carácter inválido: " + c;
            }
        }
        return null;
    }
    /**
     * Verifica si la puntuación está entre 0 y 5.
     * @param puntuacion puntuación a verificar.
     * @return la puntuación si es válida; -1 si no lo es.
     */
    public static int validarPuntuacion(int puntuacion){
        if (puntuacion < 0 || puntuacion > 5) {
            System.out.println("Puntuación inválida. Debe estar entre 0 y 5.");
            return -1; // Indica puntuación inválida
        }
        return puntuacion; // Puntuación válida
    }
    /**
     * Genera un id unico de 4 digitos para cada usuario.
     * @return id de usuarios.
     */
    public static String generarIdUsuario() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int randomDigit = (int) (Math.random() * 10);
            id.append(randomDigit);
        }
        return id.toString();
    }
}