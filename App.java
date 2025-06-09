import java.util.HashMap;
/**
 * Clase principal que inicia la aplicación desde consola,
 * creando una vista y su controlador correspondiente.
 */
public class App {
    /**
     * Metodo principal que arranca la ejecución del programa.
     * @param args los argumentos de la línea de comandos (no se utilizan en esta implementación)
     */
    public static void main(String[] args) {
        // Se crea una nueva vista de consola, pasándole un nuevo controlador
        VistaConsola vista = new VistaConsola(new Controlador());
        // Se inicia la aplicación en modo consola
        vista.iniciar();
    }
}
