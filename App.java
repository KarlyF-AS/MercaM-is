import java.util.HashMap;
/**
 * Clase principal que inicia la aplicación desde consola,
 * creando una vista y su controlador correspondiente.
 */
public class App {
    /**
     * Método principal que arranca la ejecución del programa.
     * @param args los argumentos de la línea de comandos (no se utilizan en esta implementación)
     */
    public static void main(String[] args) {
        VistaConsola vista = new VistaConsola(new Controlador());
        vista.iniciar();
    }
}
