
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Clase que representa la vista principal en consola de la aplicación.
 * Esta clase gestiona la interacción con el usuario a través de menús y entrada estándar.
 * Permite al usuario iniciar sesión o registrarse, y se comunica con la lógica de negocio
 * a través del {@link Controlador}.
 */

// Definición de la clase principal de la vista en consola
public class VistaConsola {
    // Declaración de variables miembro de la clase
    private Controlador controlador; // Conexión con el controlador (lógica de negocio)
    private Scanner scanner; // Objeto para leer entrada del usuario
    private Usuario usuarioActual; // Almacena el usuario que ha iniciado sesión
    private Lista_UnidadFamiliar unidadActual; // Almacena la unidad familiar actual

    // Constructor que recibe el controlador como parámetro
    public VistaConsola(Controlador controlador) {
        this.controlador = controlador; // Asigna el controlador recibido
        this.scanner = new Scanner(System.in); // Inicializa el scanner para leer entrada
    }

    /**
     * Muestra el menú principal en consola y permite al usuario
     * iniciar sesión, registrarse o salir del programa.
     * Se ejecuta en bucle hasta que se elige la opción de salir.
     */
    // Metodo principal que inicia la aplicación
    public void iniciar() {
        // Bucle infinito hasta que el usuario elija salir
        while (true) {
            // Menú de bienvenida
            System.out.println("\n=== BIENVENIDO ===");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarse");
            System.out.println("3. Salir");

            // Lee la opción del usuario
            int opcion = leerEntero("Seleccione una opción: ");

            // Switch para manejar las diferentes opciones
            switch (opcion) {
                case 1:
                    iniciarSesion(); // Llama al metodo de inicio de sesión
                    break;
                case 2:
                    registrarUsuario(); // Llama al metodo de registro
                    break;
                case 3: {
                    System.out.println("Saliendo...");
                    return;
                } // Sale del programa
                default:
                    System.out.println("Opción inválida."); // Opción no válida
            }
        }
    }
    /**
     * Maneja el proceso de inicio de sesión.
     * Solicita al usuario su correo y contraseña, valida el formato del correo
     * y verifica las credenciales con el controlador.
     * Si el inicio es exitoso, continúa con la gestión de la unidad familiar.
     */
    // Metodo para manejar el inicio de sesión
    private void iniciarSesion() {
        System.out.println("\n=== INICIAR SESIÓN ===");

        // Validación del formato de correo electrónico
        String correo;
        while (true) {
            System.out.print("Correo electrónico: ");
            correo = scanner.nextLine();
            // Verifica que el correo tenga @ y
            if (correo.matches(".*@.*\\..*")) {
                break; // Sale del bucle si el formato es correcto
            }
            System.out.println("El correo debe tener formato válido (ejemplo@dominio.com)");
        }

        // Solicita la contraseña
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        // Intenta iniciar sesión a través del controlador
        usuarioActual = controlador.iniciarSesion(correo, contrasena);

        // Verifica si el inicio de sesión fue exitoso
        if (usuarioActual == null) {
            System.out.println("Credenciales incorrectas o usuario no existe.");
            return; // Vuelve al menú principal
        }

        // Si el inicio de sesión fue exitoso, gestiona la unidad familiar
        gestionarUnidadFamiliar();
    }

    /**
     * Registra un nuevo usuario solicitando nombre, correo y contraseña.
     * Valida los datos ingresados y los envía al controlador.
     * Si el registro es exitoso, continúa con la gestión de unidad familiar.
     */
    // Metodo para registrar un nuevo usuario
    private void registrarUsuario() {
        System.out.println("\n=== REGISTRARSE ===");

        // Validación del nombre de usuario (que no exista)
        String nombreUsuario;
        while (true) {
            System.out.print("Nombre de usuario: ");
            nombreUsuario = scanner.nextLine().trim();
            if (nombreUsuario.isEmpty()) {
                System.out.println("El nombre de usuario no puede estar vacío.");
                continue;
            }
            break;
        }

        // Validación del formato de correo electrónico y si ya existe
        String correo;
        while (true) {
            System.out.print("Correo electrónico: ");
            correo = scanner.nextLine().trim();

            if (!correo.matches(".*@.*\\..*")) {
                System.out.println("El correo debe tener formato válido (ejemplo@dominio.com)");
                continue;
            }

            if (!controlador.existeEmail(correo)) {
                break; // Sale si el correo está disponible
            }

            System.out.println("Este email ya existe. Elija otro.");
        }

        // Validación de que ambas contraseñas coincidan
        String contrasena, confirmacion;
        while (true) {
            System.out.print("Contraseña: ");
            contrasena = scanner.nextLine();
            System.out.print("Confirmar contraseña: ");
            confirmacion = scanner.nextLine();
            if (contrasena.equals(confirmacion)) {
                break;
            }
            System.out.println("Las contraseñas no coinciden. Intente de nuevo.");
        }

        // Registra el usuario a través del controlador
        usuarioActual = controlador.registrarUsuario(nombreUsuario, correo, contrasena);
        System.out.println("¡Registro exitoso!");

        // Después de registrar, gestiona la unidad familiar
        unirseOCrearUnidadFamiliar();
    }

    /**
     * Gestiona la unidad familiar del usuario.
     * Si ya tiene una, le da opción de entrar o unirse a otra.
     * Si no tiene, lo lleva directamente a crear o unirse a una.
     */
    // Metodo para gestionar la unidad familiar del usuario
    private void gestionarUnidadFamiliar() {
        // Obtiene la unidad familiar del usuario actual
        unidadActual = controlador.obtenerUnidadFamiliar(usuarioActual);


        // Si el usuario ya tiene una unidad familiar
        if (unidadActual != null) {
            System.out.println("1. Entrar a tu habitual Unidad Familiar " + unidadActual.getNombre());
            System.out.println("2. Unirse o crear otra Unidad Familiar");

            int opcion = leerEntero("Seleccione una opción: ");

            if (opcion == 1) {
                menuPrincipal(); // Va al menú principal
            } else if (opcion == 2) {
                unirseOCrearUnidadFamiliar(); // Permite cambiar de unidad
            }
        } else {
            // Si no tiene unidad familiar, va directamente a crear/unión
            unirseOCrearUnidadFamiliar();
        }
    }

    /**
     * Permite al usuario unirse a una unidad familiar existente con código
     * o crear una nueva si lo desea.
     */
    // Metodo para unirse o crear una unidad familiar
    private void unirseOCrearUnidadFamiliar() {
        System.out.println("\n=== UNIRSE O CREAR UNIDAD FAMILIAR ===");
        System.out.print("¿Tienes un código de Unidad Familiar? (S/N): ");
        String respuesta = scanner.nextLine().toUpperCase();

        // Si el usuario tiene un código
        if (respuesta.equals("S")) {
            System.out.print("Introduce el código de la Unidad Familiar: ");
            String codigo = scanner.nextLine();
            // Intenta unirse a la unidad
            unidadActual = controlador.unirseAUnidadFamiliar(usuarioActual, codigo);

            if (unidadActual != null) {
                System.out.println("¡Te has unido a la unidad familiar " + unidadActual.getNombre() + "!");
                menuPrincipal(); // Va al menú principal
            } else {
                // Si el código es inválido, pregunta si quiere crear una nueva
                System.out.println("Código inválido. ¿Deseas crear una nueva unidad familiar? (S/N)");
                respuesta = scanner.nextLine().toUpperCase();
                if (respuesta.equals("S")) {
                    crearUnidadFamiliar(); // Crea nueva unidad
                }
            }
        } else {
            // Si no tiene código, pregunta si quiere crear unidad
            System.out.print("¿Deseas crear una nueva unidad familiar? (S/N): ");
            respuesta = scanner.nextLine().toUpperCase();
            if (respuesta.equals("S")) {
                crearUnidadFamiliar(); // Crea nueva unidad
            }
        }
    }

    // Metodo para crear una nueva unidad familiar
    private void crearUnidadFamiliar() {
        System.out.print("Introduce el nombre para tu nueva Unidad Familiar: ");
        String nombre = scanner.nextLine();

        // Crea la unidad a través del controlador
        unidadActual = controlador.crearUnidadFamiliar(usuarioActual, nombre, Controller2.generarIdUsuario());
        System.out.println("¡Unidad Familiar creada con éxito! Código: " + unidadActual.getCodigo());
        menuPrincipal(); // Va al menú principal
    }

    /**
     * Metodo para leer un número entero desde la entrada estándar.
     * Si la entrada no es un número válido, solicita al usuario que vuelva a intentarlo.
     */
    private void menuPrincipal() {
        int opcion;
        do {
            inicializarStock();
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Navegar en la lista de compra");
            System.out.println("2. Ver productos");
            System.out.println("3. Gestión de stock");
            System.out.println("4. Configuración");
            System.out.println("0. Cerrar sesión");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    mostrarMenu();
                    break;
                case 2:
                    menuProductos();
                    break;
                case 3:
                    menuStock();
                    break;
                case 4:
                    menuConfiguracion();
                    break;
                case 0:
                    usuarioActual = null;
                    unidadActual = null;
                    System.out.println("Sesión cerrada.");
                    iniciar();

                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    /**
     * Metodo para leer un número entero desde la entrada estándar.
     * Si la entrada no es un número válido, solicita al usuario que vuelva a intentarlo.
     */
    private void menuProductos() {
        int opcion;
        do {
            System.out.println("\n=== PRODUCTOS ===");
            System.out.println("1. Ver todos los productos");
            System.out.println("2. Añadir producto");
            System.out.println("0. Volver atrás");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    verTodosProductos(); // Muestra todos los productos
                    continue;
                case 2:
                    anadirProducto(); // Permite añadir un producto
                    continue;
                case 0:
                    System.out.println("Volviendo..."); // Vuelve al menú anterior
                    menuPrincipal()
                    ;

                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    /**
     * Muestra el menú de gestión de stock.
     */
    private void menuStock() {
        int opcion;
        do {

            System.out.println("\n=== GESTIÓN DE STOCK ===");
            System.out.println("1. Ver stock actual");
            System.out.println("2. Añadir producto al stock");
            System.out.println("3. Actualizar cantidad en stock");
            System.out.println("4. Eliminar producto del stock");
            System.out.println("0. Volver");

            opcion = leerEntero("Seleccione una opción: ");
            // Producto de prueba


            switch (opcion) {
                case 1:
                    verStockActual();
                    continue;
                case 2:
                    añadirProductoStock();
                    continue;
                case 3:
                    actualizarCantidadStock();
                    continue;
                case 4:
                    eliminarProductoStock();
                    continue;
                case 0:
                    System.out.println("Volviendo...");
                    menuPrincipal();
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    /**
     * Inicializa el stock con un producto de prueba para validar la conexión.
     */
    private void inicializarStock() {
        // Creamos un producto de prueba
        Producto productoPrueba = new Producto(
                1234567890123L,
                "Producto de prueba",
                "Marca de prueba",
                1.99,
                "Categoría de prueba",
                "Supermercado de prueba",
                "Descripción de prueba"
        );

        // 1) Intentamos añadirlo al stock y 2) después lo borramos.
        // Si falla (por FK), lo capturamos y no hacemos nada.
        try {
            controlador.anadirProductoStock(unidadActual, productoPrueba, 10);
            controlador.eliminarProductoStock(unidadActual, productoPrueba);
        } catch (Exception ignored) {
            // Aquí ignoramos cualquier excepción (p.ej. FK violation)
        }
    }

    /**
     * Muestra todos los productos disponibles y permite filtrarlos o seleccionarlos.
     */
    // Metodo para mostrar todos los productos
    private void verTodosProductos() {
        try {
            List<Producto> productos = controlador.obtenerTodosProductos();

            if (productos.isEmpty()) {
                System.out.println("\nNo hay productos disponibles.");
                return;
            }

            System.out.println(String.format("\nMostrando %d productos", productos.size()));
            System.out.println("Nombre              | Marca          | Categoría           | Supermercado      | Precio  | Punt.");
            System.out.println("------------------------------------------------------------------------------------------");

            String formatoProducto = "%-20s | %-15s | %-20s | %-17s | %6.2f€ | %4.1f★%n";
            for (Producto p : productos) {
                // Obtener puntuaciones del producto
                Map<Usuario, Integer> puntuaciones = controlador.getPuntuaciones(p);

                // Calcular media de puntuaciones
                double puntuacionMedia = 0.0;
                if (!puntuaciones.isEmpty()) {
                    puntuacionMedia = puntuaciones.values().stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0);
                }

                System.out.printf(formatoProducto,
                        p.getNombre(),
                        p.getMarca(),
                        p.getCategoria(),
                        p.getSupermercado(),
                        p.getPrecio(),
                        puntuacionMedia);
            }

            System.out.println("\n1. Filtrar");
            System.out.println("2. Seleccionar producto");
            System.out.println("0. Volver atrás");

            int opcion = leerEntero("\nSeleccione una opción: ");
            switch (opcion) {
                case 1 -> menuFiltros();
                case 2 -> seleccionarProducto();
                case 0 -> { /* volver */ }
                default -> System.out.println("Opción no válida");
            }

        } catch (Model.BaseDatosException e) {
            System.err.println("Error al obtener los productos: " + e.getMessage());
        }
    }



    /**
     * Muestra el stock actual de productos de la unidad familiar en la consola.
     * Esta función permite al usuario visualizar qué productos están disponibles y en qué cantidad.
     */
    private void verStockActual() {
        // Imprime el encabezado de la sección de stock
        System.out.println("\n=== STOCK ACTUAL ===");

        /**
         * Obtiene el mapa de stock de la unidad familiar actual
         * - Clave: Producto
         * - Valor: Cantidad en stock
         */
        Map<Producto, Integer> stock = controlador.obtenerProductosUnidadFamiliar(unidadActual);

        // Verifica si el stock está vacío
        if (stock.isEmpty()) {
            System.out.println("El stock está vacío.");
            return;  // Sale del metodo si no hay productos
        }

        /**
         * Obtiene todos los productos de la lista de compra actual
         * de la unidad familiar para verificar qué productos están
         * tanto en stock como en la lista de compra
         */
        Map<Producto,Integer> productosListaCompra = controlador.obtenerProductosUnidadFamiliar(unidadActual);

        System.out.println("Nombre\t| Marca\t| Stock\t| En Lista\t| Punt.\t| Precio\t| Supermercados");
        System.out.println("--------------------------------------------------------------------------");

        /**
         * Itera a través de todas las entradas del mapa de stock
         * - entry.getKey(): Producto
         * - entry.getValue(): Cantidad en stock
         */
        for (Map.Entry<Producto, Integer> entry : stock.entrySet()) {
            Producto p = entry.getKey();  // Obtiene el producto actual
            int cantidadStock = entry.getValue();  // Obtiene la cantidad en stock

            // Variables para verificar presencia en lista de compra
            String enLista = "No";  // Valor por defecto: no está en lista
            int cantidadLista = 0;  // Cantidad en lista (0 por defecto)

            /**
             * Verifica si este producto está en la lista de compra
             * Compara por ID para asegurar que es el mismo producto
             */
            for (Map.Entry<Producto, Integer> entryLista : productosListaCompra.entrySet()) {
                Producto productoLista = entryLista.getKey();
                if (productoLista.getCodigoBarras() == (p.getCodigoBarras())) {
                    enLista = "Sí";  // Marca como presente en lista
                    cantidadLista = entryLista.getValue();  // Captura la cantidad deseada
                    break;  // Termina el bucle al encontrar coincidencia
                }
            }

            /**
             * Construye la línea de la tabla con formato:
             * 1. Nombre del producto
             * 2. Marca
             * 3. Cantidad actual en stock
             * 4. Indicador si está en lista + cantidad deseada (si aplica)
             * 5. Puntuación media formateada a 1 decimal
             * 6. Último precio formateado a 2 decimales con símbolo €
             * 7. Lista de supermercados separados por coma
             */
            System.out.println(
                    p.getNombre() + "\t| " +  // Columna 1: Nombre
                            p.getMarca() + "\t| " +   // Columna 2: Marca
                            cantidadStock + "\t| " +  // Columna 3: Stock actual
                            enLista + (enLista.equals("Sí") ? " (" + cantidadLista + ")" : "") + "\t| " +  // Columna 4: En lista con cantidad
                            String.format("%.1f", Controlador.getPuntuacionMedia(p.getNombre(), p.getMarca())) + "\t| " +  // Columna 5: Puntuación
                            String.format("%.2f€", p.getPrecio()) + "\t| " +  // Columna 6: Precio
                            String.join(", ", Controlador.getSupermercados(p))  // Columna 7: Supermercados
            );
        }
    }
    /**
     * Añade una cantidad inicial de un producto al stock.
     * Busca el producto y, si existe, solicita la cantidad y actualiza el stock.
     * Muestra el stock total actualizado.
     */
    private void añadirProductoStock() {
        Producto producto = buscarProducto();
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.print("Cantidad inicial: ");
        int cantidad = Integer.parseInt(scanner.nextLine());
        controlador.anadirProductoStock(unidadActual, producto, cantidad);
        System.out.println("Actaulmente el stock total es de : " + controlador.obtenerCantidadStock(unidadActual, producto) + " unidades.");
    }

    /**
     * Actualiza la cantidad en stock de un producto existente.
     * Solicita el producto y la nueva cantidad, y realiza la actualización.
     * Informa al usuario sobre el cambio.
     */
    private void actualizarCantidadStock() {
        System.out.print("\nNombre del producto: ");
        String nombre = scanner.nextLine();
        Producto producto = buscarProducto();
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.print("Nueva cantidad: ");
        int nuevaCantidad = Integer.parseInt(scanner.nextLine());
        controlador.actualizarCantidadStock(unidadActual, producto, nuevaCantidad);
        System.out.println("Cantidad actualizada.");
    }

    /**
     * Elimina un producto del stock tras confirmación del usuario.
     * Busca el producto, pide confirmación y lo elimina si se confirma.
     */
    private void eliminarProductoStock() {
        System.out.print("\nNombre del producto: ");
        String nombreBusqueda = scanner.nextLine().trim();

        // Obtener el stock actual
        Map<Integer, Producto> stockActual = controlador.obtenerStock(unidadActual);

        if (stockActual.isEmpty()) {
            System.out.println("No hay ningún producto en stock.");
            return;
        }

        // Buscar el producto por nombre
        Producto productoAEliminar = null;
        for (Map.Entry<Integer, Producto> entry : stockActual.entrySet()) {
            if (entry.getValue().getNombre().equalsIgnoreCase(nombreBusqueda)) {
                productoAEliminar = entry.getValue();
                break;
            }
        }

        if (productoAEliminar == null) {
            System.out.println("No se encontró el producto '" + nombreBusqueda + "' en el stock.");
            return;
        }

        System.out.print("¿Está seguro de eliminar '" + productoAEliminar.getNombre() +
                "' de " + productoAEliminar.getMarca() + "? (S/N): ");
        if (!scanner.nextLine().equalsIgnoreCase("S")) {
            System.out.println("Operación cancelada");
            return;
        }

        int resultado = controlador.eliminarProductoStock(unidadActual, productoAEliminar);
        if (resultado >= 0) {
            System.out.println("Producto eliminado del stock correctamente.");
        } else {
            System.out.println("Error al eliminar el producto del stock.");
        }
    }

    /**
     * Permite al usuario elegir un producto de los que hay en stock
     * (cantidad > 0) en su unidad familiar actual.
     */
    /**
     * Permite al usuario elegir un producto de los que hay en stock
     * (cantidad > 0), usando Map<stock,producto> para mostrar cantidad primero.
     */
    private Producto buscarProductoEnStock() {
        // 1) Recuperar el map cantidad→producto
        Map<Integer,Producto> stockMap = controlador.obtenerProductosConStock(unidadActual);

        if (stockMap.isEmpty()) {
            System.out.println("No hay ningún producto en stock.");
            return null;
        }
        // 2) Convertir a lista de entradas para indexar
        List<Map.Entry<Integer,Producto>> entries = new ArrayList<>(stockMap.entrySet());

        // 3) Si sólo hay uno, lo devolvemos
        if (entries.size() == 1) {
            Map.Entry<Integer,Producto> e = entries.get(0);
            System.out.printf("Solo hay uno en stock: %s (%d unidades)%n",
                    e.getValue().getNombre(), e.getKey());
            return e.getValue();
        }

        // 4) Mostrar varios
        System.out.println("\n=== PRODUCTOS EN STOCK ===");
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<Integer,Producto> e = entries.get(i);
            Producto p = e.getValue();
            int cantidad = e.getKey();
            System.out.printf(
                    "%d) %s | Marca: %s | Supermercado: %s | Cantidad: %d%n",
                    i + 1,
                    p.getNombre(),
                    p.getMarca(),
                    p.getSupermercado(),
                    cantidad
            );
        }

        // 5) Pedir selección
        System.out.print("Selecciona un producto (0 para cancelar): ");
        int sel;
        try {
            sel = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Selección no válida.");
            return null;
        }
        if (sel == 0) return null;
        if (sel < 1 || sel > entries.size()) {
            System.out.println("Selección fuera de rango.");
            return null;
        }

        return entries.get(sel - 1).getValue();
    }

    /**
     * Muestra el menú para filtrar o ordenar productos.
     * Permite al usuario elegir entre aplicar filtros o cambiar el orden.
     */
    // Menú de filtros para productos
    private void menuFiltros() {
        int opcion;
        do {
            System.out.println("\n=== FILTRAR ===");
            System.out.println("1. Filtrar");
            System.out.println("2. Ordenar");
            System.out.println("0. Volver atrás");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    filtrado(); // Filtra por categoría/marca
                    continue;
                case 2:
                    ordenar(); // Ordena los productos
                    continue;
                case 0:
                    System.out.println("Volviendo...");
                    menuPrincipal();
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    /**
     * Menú para filtrar productos por categoría, marca o supermercado.
     * Navega por opciones para seleccionar el criterio de filtrado.
     */
    // Metodo para filtrar por categoría o marca
    private void filtrado() {
        int opcion;
        do {
            System.out.println("\n=== FILTRAR POR CATEGORÍA/MARCA ===");
            System.out.println("1. Ver categorías");
            System.out.println("2. Ver marcas");
            System.out.println("3. Ver supermercado");
            System.out.println("0. Volver atrás");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    verCategorias(); // Muestra categorías
                    continue;
                case 2:
                    verMarcas(); // Muestra marcas
                    continue;
                case 3:
                    filtrarPorSupermercado(); // Muestra supermercados
                    continue;
                case 0:
                    System.out.println("Volviendo...");
                    menuPrincipal();
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    /**
     * Muestra las categorías principales disponibles y permite seleccionar una.
     * Luego muestra las subcategorías de la categoría seleccionada.
     */
    // Metodo para mostrar todas las categorías disponibles
    private void verCategorias() {
        // 1) Sacamos solo las categorías (parte antes del punto)
        List<String> categorias = controlador.obtenerCategoriasPrincipales();

        // 2) Las mostramos numeradas
        System.out.println("\n=== CATEGORÍAS DISPONIBLES ===");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, categorias.get(i));
        }

        // 3) Le pedimos al usuario que elija
        int opcion = leerEntero("\nSeleccione una categoría (0 para volver): ");
        try {
            opcion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return;
        }

        if (opcion == 0) {
            return;  // vuelve atrás
        } else if (opcion < 0 || opcion > categorias.size()) {
            System.out.println("Opción fuera de rango.");
            return;
        }

        // 4) Si es válida, obtenemos las subcategorías de esa categoría
        String categoriaSeleccionada = categorias.get(opcion - 1);
        List<String> subcategorias = controlador.obtenerSubcategorias(categoriaSeleccionada);

        // 5) Delegamos en el metodo que ya tenías para mostrar subcategorías
        verSubcategorias(categoriaSeleccionada, subcategorias);
    }

    /**
     * Muestra las subcategorías de una categoría y permite seleccionar una para mostrar productos.
     * @param categoria La categoría principal seleccionada.
     * @param subcategorias Lista de subcategorías para esa categoría.
     */
    // Metodo para mostrar las subcategorías de una categoría
    private void verSubcategorias(String categoria, List<String> subcategorias) {
        if (subcategorias.isEmpty()) {
            System.out.println("\nNo hay subcategorías disponibles para " + categoria);
            return;
        }

        System.out.println("\n=== SUBCATEGORÍAS DE " + categoria.toUpperCase() + " ===");
        for (int i = 0; i < subcategorias.size(); i++) {
            System.out.println((i + 1) + ". " + subcategorias.get(i));
        }

        System.out.print("\nSeleccione una categoría (0 para volver): ");
        int opcion = leerEntero("");

        if (opcion > 0 && opcion <= subcategorias.size()) {
            String subcategoriaSeleccionada = categoria + "." + subcategorias.get(opcion - 1);
            List<Producto> productos = controlador.obtenerProductosPorSubcategoria(subcategoriaSeleccionada);

            if (productos.isEmpty()) {
                System.out.println("\nNo hay productos en esta subcategoría.");
            } else {;
                System.out.println("-------------------------------------------------------------");
                mostrarProductosTabla(productos);
            }
        }
    }
    /**
     * Muestra todas las marcas disponibles y permite filtrar productos por marca.
     */
    // Metodo para mostrar todas las marcas disponibles
    private void verMarcas() {
        // Obtiene las marcas del controlador
        List<String> marcas = controlador.obtenerMarcas();

        System.out.println("\n=== MARCAS ===");
        // Muestra todas las marcas numeradas
        for (int i = 0; i < marcas.size(); i++) {
            System.out.println((i + 1) + ". " + marcas.get(i));
        }

        int opcion = leerEntero("\nSeleccione una categoría (0 para volver): ");

        // Si seleccionó una marca válida, muestra sus productos
        if (opcion > 0 && opcion <= marcas.size()) {
            String marca = marcas.get(opcion - 1);
            List<Producto> productos = controlador.obtenerProductosPorMarca(marca);
            mostrarProductosTabla(productos); // Muestra en formato de tabla
        }
    }

    /**
     * Permite filtrar productos según el supermercado seleccionado.
     */
    private void filtrarPorSupermercado() {
        List<String> supermercados = controlador.obtenerTodosSupermercados();
        System.out.println("\nSupermercados disponibles:");
        for (int i = 0; i < supermercados.size(); i++) {
            System.out.println((i + 1) + ". " + supermercados.get(i));
        }
        int opcion = leerEntero("\nSeleccione una categoría (0 para volver): ");
        if (opcion > 0 && opcion <= supermercados.size()) {
            List<Producto> productos = controlador.filtrarPorSupermercado(supermercados.get(opcion - 1));
            mostrarProductosTabla(productos);
        }
    }
    /**
     * Muestra opciones para ordenar productos por precio, nombre o marca.
     * Aplica el orden seleccionado y muestra los productos.
     */
    // Metodo para ordenar los productos por diferentes criterios
    private void ordenar() {
        int opcion;
        do {
            System.out.println("\n=== ORDENAR POR ===");
            System.out.println("1. Precio total (más barato primero)");
            System.out.println("2. Precio total (más caro primero)");
            System.out.println("3. Precio por unidad (más barato primero)");
            System.out.println("4. Precio por unidad (más caro primero)");
            System.out.println("5. Nombre (A-Z)");
            System.out.println("6. Nombre (Z-A)");
            System.out.println("7. Marca (A-Z)");
            System.out.println("8. Marca (Z-A)");
            System.out.println("0. Volver atrás");

            opcion = leerEntero("Seleccione una opción: ");

            // Si seleccionó un orden válido, muestra los productos ordenados
            if (opcion >= 1 && opcion <= 8) {
                List<Producto> productos = controlador.ordenarProductos(opcion);
                mostrarProductosTabla(productos);
            } else if (opcion != 0) {
                System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    /**
     * Permite buscar un producto por nombre y mostrar sus detalles.
     */
    // Metodo para seleccionar un producto específico
    private void seleccionarProducto() {
        System.out.print("\nIntroduce el nombre del producto a seleccionar: ");
        String nombre = scanner.nextLine();

        // Obtiene el producto por nombre
        Producto producto = buscarProducto();
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        // Muestra el detalle del producto
        verDetalleProducto(producto);
    }

    /**
     * Muestra la información completa de un producto y opciones para gestionar historial,
     * puntuaciones y supermercados relacionados.
     * @param producto El producto cuyo detalle se muestra.
     */
    // Metodo para mostrar el detalle de un producto
    private void verDetalleProducto(Producto producto) {
        int opcion;
        do {
            System.out.println("\n=== DETALLE DE PRODUCTO ===");
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Marca: " + producto.getMarca());
            System.out.println("Precio: " + producto.getPrecio() + "€ (pulsa 1 para ver/modificar historial)");
            System.out.println("Puntuación media: " + Controlador.getPuntuacionMedia(producto.getNombre(), producto.getMarca()) + " (pulsa 2 para ver/modificar puntuaciones)");
            System.out.println("ID/Código de barras: " + producto.getCodigoBarras());
            System.out.println("Categoría: " + producto.getCategoria());
            System.out.println("Subcategoría: " + producto.getSubcategoria());
            System.out.println("Supermercados: " + Controlador.getSupermercados(producto));

            System.out.println("\n1. Ver/modificar historial de precios");
            System.out.println("2. Ver/modificar puntuaciones");
            System.out.println("3. Modificar supermercados");
            System.out.println("0. Volver atrás");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    gestionarHistorialPrecios(producto); // Gestiona precios
                    continue;
                case 2:
                    gestionarPuntuaciones(producto); // Gestiona puntuaciones
                    continue;
                case 3:
                    modificarSupermercados(producto); // Modifica supermercados
                    continue;
                case 0:
                    System.out.println("Volviendo...");
                    menuPrincipal();
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
    /**
     * Muestra y permite gestionar el historial de precios de un producto.
     * @param producto El producto cuyo historial de precios se gestiona.
     */
    // Metodo para gestionar el historial de precios de un producto
    private void gestionarHistorialPrecios(Producto producto) {
        System.out.println("\n=== HISTORIAL DE PRECIOS ===");
        // Obtiene el historial de precios
        List<Double> historial = producto.getHistorialPrecios();

        if (historial.isEmpty()) {
            System.out.println("No hay historial de precios para este producto.");
        } else {
            System.out.println("Historial de precios:");
            // Muestra todos los precios numerados
            for (int i = 0; i < historial.size(); i++) {
                System.out.println((i + 1) + ". " + historial.get(i) + "€");
            }
        }

        // Pregunta si quiere añadir nuevo precio
        boolean respuesta = leerConfirmacion("\n¿Desea añadir un nuevo precio?");
        if (respuesta) {
            double nuevoPrecio = leerDouble("Introduce el nuevo precio: ");
            controlador.actualizarPrecioProducto(producto, nuevoPrecio);
            System.out.println("Precio actualizado correctamente.");
        }
    }

    /**
     * Gestiona las puntuaciones de un producto.
     * Muestra las puntuaciones actuales y permite al usuario añadir o modificar su puntuación.
     * @param producto El producto cuyas puntuaciones se van a gestionar.
     */
    // Metodo para gestionar las puntuaciones de un producto
    private void gestionarPuntuaciones(Producto producto) {
        System.out.println("\n=== PUNTUACIONES ===");
        // Obtiene las puntuaciones del producto
        Map<Usuario, Integer> puntuaciones = Controlador.getPuntuaciones(producto);

        if (puntuaciones.isEmpty()) {
            System.out.println("No hay puntuaciones para este producto.");
        } else {
            System.out.println("Puntuaciones actuales:");
            // Muestra todas las puntuaciones
            for (Map.Entry<Usuario, Integer> entry : puntuaciones.entrySet()) {
                System.out.println("- " + entry.getKey().getNombre() + ": " + entry.getValue() + " estrellas");
            }
        }

        // Pregunta si quiere añadir/modificar su puntuación
        boolean respuesta = leerConfirmacion("\n¿Desea añadir/modificar su puntuación?");
        if (respuesta) {
            int puntuacion = leerEntero("Introduce tu puntuación (1-5 estrellas): ");
            controlador.anadirPuntuacionProducto(producto, usuarioActual, puntuacion);
            System.out.println("Puntuación guardada correctamente.");
        }
    }
    /**
     * Modifica los supermercados asociados a un producto.
     * Permite añadir o eliminar supermercados de la lista del producto.
     * @param producto El producto cuyos supermercados se desean modificar.
     */
    // Metodo para modificar los supermercados de un producto
    private void modificarSupermercados(Producto producto) {
        System.out.println("\n=== SUPERMERCADOS ===");
        // Obtiene los supermercados del producto
        List<String> supermercados = Controlador.getSupermercados(producto);

        System.out.println("Supermercados actuales:");
        // Muestra los supermercados numerados
        for (int i = 0; i < supermercados.size(); i++) {
            System.out.println((i + 1) + ". " + supermercados.get(i));
        }

        System.out.println("\n1. Añadir supermercado");
        System.out.println("2. Eliminar supermercado");
        System.out.println("0. Volver");

        int opcion = leerEntero("Seleccione una opción: ");

        switch (opcion) {
            case 1: {
                // Añade un nuevo supermercado
                System.out.print("Introduce el nombre del nuevo supermercado: ");
                String nuevoSuper = scanner.nextLine();
                controlador.anadirSupermercadoProducto(producto, nuevoSuper);
                System.out.println("Supermercado añadido correctamente.");
                break;
            }
            case 2: {
                // Elimina un supermercado existente
                System.out.print("Introduce el número del supermercado a eliminar: ");
                int numEliminar = Integer.parseInt(scanner.nextLine());
                if (numEliminar > 0 && numEliminar <= supermercados.size()) {
                    controlador.eliminarSupermercadoProducto(producto, supermercados.get(numEliminar - 1));
                    System.out.println("Supermercado eliminado correctamente.");
                } else {
                    System.out.println("Número inválido.");
                }
            }
            case 0:
                System.out.println("Volviendo...");
            default:
                System.out.println("Opción inválida.");
        }
    }

    /**
     * Añade un nuevo producto, solicitando datos básicos, categoría y subcategoría (opcional),
     * código de barras y descripción. Finalmente añade el producto al stock de la unidad familiar actual.
     */
    // Metodo para añadir un nuevo producto, con categoría y subcategoría opcional
    private void anadirProducto() {
        System.out.println("\n=== AÑADIR PRODUCTO ===");

        // 1) Datos básicos
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Marca: ");
        String marca = scanner.nextLine().trim();
        double precio = leerDouble("Precio: ");

        // 2) Elegir categoría principal
        List<String> categorias = controlador.obtenerCategoriasPrincipales();
        System.out.println("\nCategorías disponibles:");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, categorias.get(i));
        }
        System.out.print("Seleccione una categoría (número): ");
        int opcCat = Integer.parseInt(scanner.nextLine().trim());
        String catBase = categorias.get(opcCat - 1);

        // 3) Elegir subcategoría (opcional)
        List<String> subcats = controlador.obtenerSubcategorias(catBase);
        String catFull = catBase;
        if (!subcats.isEmpty()) {
            System.out.println("\nSubcategorías disponibles (0 = ninguna):");
            for (int j = 0; j < subcats.size(); j++) {
                System.out.printf("%d. %s%n", j + 1, subcats.get(j));
            }
            System.out.print("Seleccione subcategoría (número): ");
            int opcSub = Integer.parseInt(scanner.nextLine().trim());
            if (opcSub > 0 && opcSub <= subcats.size()) {
                catFull = catBase + "." + subcats.get(opcSub - 1);
            }
        }

        // 4) Código de barras y descripción
        long codigo = leerLong("Código de barras: ");
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();

        // 5) Insertar producto en BD
        Producto p = controlador.crearProducto(
                nombre, marca, precio,
                catFull,    // categoría o "categoría.subcategoría"
                codigo, descripcion
        );
        if (p == null) {
            System.out.println("Error al crear el producto.");
            return;
        }

        // 6) Añadir al stock de la unidad familiar (cantidad inicial 1)
        controlador.anadirProductoStock(unidadActual, p, 1);
        System.out.println("Producto añadido correctamente con categoría: " + catFull);
    }

    private void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n=== MENÚ DE LISTA ===");
            System.out.println("1. Ver lista de la compra");
            System.out.println("2. Exportar lista de la compra en TXT");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1 :
                    verLista(); // Llama al metodo de optimización
                    break;
                case 2 :
                    exportarListaTXT();
                    break;
                case 0 :
                    System.out.println("Volviendo...");
                    break;
                default :
                    System.out.println("Opción inválida. Intente de nuevo.");
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Exporta la lista de productos optimizada a un archivo TXT.
     * Agrupa los productos por nombre, seleccionando el mejor producto
     * de cada grupo según la cantidad en stock, puntuación y precio.
     */
    private void exportarListaTXT() {
        Map<Producto, Integer> productosMap = controlador.obtenerProductosUnidadFamiliar(unidadActual);
        if (productosMap == null || productosMap.isEmpty()) {
            System.out.println("No hay productos para exportar.");
            return;
        }
        String supermercado = null;
        int orden = 0;
        int opcion;
        do {
            System.out.println("\n=== OPCIONES DE FILTRO Y ORDENACIÓN PARA EXPORTAR ===");
            System.out.println("1. Filtrar por supermercado");
            System.out.println("2. Ordenar por precio (más barato primero)");
            System.out.println("3. Ordenar por nombre (A-Z)");
            System.out.println("0. Exportar lista");
            opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 : {
                    List<String> supermercados = controlador.obtenerTodosSupermercados();
                    for (int i = 0; i < supermercados.size(); i++) {
                        System.out.printf("%d. %s%n", i + 1, supermercados.get(i));
                    }
                    int sel = leerEntero("Seleccione un supermercado (0 para cancelar): ");
                    if (sel > 0 && sel <= supermercados.size()) {
                        supermercado = supermercados.get(sel - 1);
                    }
                } break;
                case 2 :
                    orden = 1;
                    break;
                case 3 :
                    orden = 2;
                    break;
                case 0 :
                    break;
                default :
                    System.out.println("Opción inválida.");
                    break;
            }
        } while (opcion != 0);

        // Tudo este bloque debe estar dentro del metodo
        List<Producto> listaFinal = obtenerListaOptimizada(productosMap, supermercado, orden);

        if (listaFinal.isEmpty()) {
            System.out.println("No hay productos para exportar con los filtros seleccionados.");
            return;
        }

        try (FileWriter writer = new FileWriter("lista_optimizada.txt")) {
            writer.write("Nombre | Marca | Categoría | Punt. | Precio | Supermercados\n");
            writer.write("-------------------------------------------------------------\n");
            for (Producto p : listaFinal) {
                writer.write(String.format("%s | %s | %s | %.1f | %.2f | %s%n",
                        p.getNombre(),
                        p.getMarca(),
                        p.getSubcategoria(),
                        Controlador.getPuntuacionMedia(p.getNombre(), p.getMarca()),
                        p.getPrecio(),
                        String.join(", ", Controlador.getSupermercados(p))));
            }
            System.out.println("Lista exportada correctamente a lista_optimizada.txt");
        } catch (IOException e) {
            System.out.println("Error al exportar la lista: " + e.getMessage());
        }
    }
    private List<Producto> obtenerListaOptimizada(Map<Producto, Integer> productosMap, String supermercado, int orden) {
        // Filtra productos con cantidad <= 3
        Map<Producto, Integer> filtrados = new HashMap<>();
        for (Map.Entry<Producto, Integer> entry : productosMap.entrySet()) {
            if (entry.getValue() <= 3) {
                filtrados.put(entry.getKey(), entry.getValue());
            }
        }

        Map<String, List<Producto>> agrupados = new HashMap<>();
        for (Producto p : filtrados.keySet()) {
            agrupados.computeIfAbsent(p.getNombre().toLowerCase(), k -> new ArrayList<>()).add(p);
        }
        List<Producto> listaFinal = new ArrayList<>();
        for (List<Producto> grupo : agrupados.values()) {
            grupo.sort(
                    Comparator
                            .comparing((Producto p) -> filtrados.get(p))
                            .thenComparing(p -> -Controlador.getPuntuacionMedia(p.getNombre(), p.getMarca()))
                            .thenComparing(Producto::getPrecio)
            );
            listaFinal.add(grupo.get(0));
        }
        if (supermercado != null) {
            listaFinal.removeIf(p -> !Controlador.getSupermercados(p).contains(supermercado));
        }
        switch (orden) {
            case 1:
                listaFinal.sort(Comparator.comparing(Producto::getPrecio));
                break;
            case 2:
                listaFinal.sort(Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER));
                break;
            default:
                break;
        }
        return listaFinal;
    }

    // Añade este metodo para mostrar la lista con opciones de filtro y ordenación
    // Cambia el bucle para que tras elegir filtro/ordenación se muestre la lista y se salga
    private void verLista() {
        Map<Producto, Integer> productosMap = controlador.obtenerProductosUnidadFamiliar(unidadActual);
        if (productosMap == null || productosMap.isEmpty()) {
            System.out.println("No hay productos en la lista.");
            return;
        }
        String supermercado = null;
        int orden = 0;
        int opcion;
        do {
            System.out.println("\n=== OPCIONES DE FILTRO Y ORDENACIÓN ===");
            System.out.println("1. Filtrar por supermercado");
            System.out.println("2. Ordenar por precio (más barato primero)");
            System.out.println("3. Ordenar por nombre (A-Z)");
            System.out.println("0. Mostrar lista");
            opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1: {
                    List<String> supermercados = controlador.obtenerTodosSupermercados();
                    for (int i = 0; i < supermercados.size(); i++) {
                        System.out.printf("%d. %s%n", i + 1, supermercados.get(i));
                    }
                    int sel = leerEntero("Seleccione un supermercado (0 para cancelar): ");
                    if (sel > 0 && sel <= supermercados.size()) {
                        supermercado = supermercados.get(sel - 1);
                    }
                    break;
                }
                case 2:
                    orden = 1;
                    break;
                case 3:
                    orden = 2;
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
            // Si la opción es distinta de 0, salir del bucle y mostrar la lista
            if (opcion != 0) break;
        } while (opcion != 0);

        List<Producto> listaFinal = obtenerListaOptimizada(productosMap, supermercado, orden);
        System.out.println("\n=== LISTA DE PRODUCTOS ===");
        mostrarProductosTabla(listaFinal);
    }

    // Metodo auxiliar para mostrar productos en formato de tabla
    private void mostrarProductosTabla(List<Producto> productos) {
        // Encabezado simple
        System.out.println("Nombre | Marca | Categoría | Punt. | Precio | Supermercados");
        System.out.println("-------------------------------------------------------------");

        // Mostrar cad producto sin formato de ancho fijo
        for (Producto p : productos) {
            System.out.printf("%s | %s | %s | %.1f | %.2f | %s%n",
                    p.getNombre(),
                    p.getMarca(),
                    p.getSubcategoria(),
                    Controlador.getPuntuacionMedia(p.getNombre(), p.getMarca()),
                    p.getPrecio(),
                    String.join(", ", Controlador.getSupermercados(p)));
        }
    }

    /**
     * Muestra productos con stock de una lista de unidad familiar.
     *
     * @param unidad La unidad familiar para la cual se mostrarán los productos con stock.
     */
    // Supón que tienes acceso a una instancia de tu controlador y a la unidad familiar
    private void verProductosListaCompra(Lista_UnidadFamiliar unidad) {
        Map<Integer, Producto> productosConStock = controlador.obtenerProductosConStock(unidad);
        if (productosConStock == null || productosConStock.isEmpty()) {
            System.out.println("No hay productos con stock.");
            return;
        }
        System.out.println("Productos con stock:");
        for (Map.Entry<Integer, Producto> entry : productosConStock.entrySet()) {
            int cantidad = entry.getKey();
            Producto producto = entry.getValue();
            System.out.println("Cantidad: " + cantidad + " - Producto: " + producto.getNombre() + " (" + producto.getMarca() + ")");
        }
    }

    /**
     * Muestra el menú de configuración general, permitiendo cambiar nombre de usuario,
     * contraseña y gestionar la unidad familiar.
     */
    // Menú de configuración
    private void menuConfiguracion() {
        int opcion;
        do {
            System.out.println("\n=== CONFIGURACIÓN ===");
            System.out.println("1. Cambiar nombre de usuario");
            System.out.println("2. Cambiar contraseña");
            System.out.println("3. Gestionar unidad familiar");
            System.out.println("0. Volver");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    cambiarNombreUsuario();
                    break;
                case 2:
                    cambiarContrasena();
                    break;
                case 3:
                    gestionarUnidadFamiliarConfig();
                    break;
                case 0:
                    System.out.println("Volviendo...");
                    menuPrincipal();
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Cambia el nombre del usuario actual, solicitando el nuevo nombre.
     * Verifica que el nuevo nombre no esté ya en uso.
     */
    // Metodo para cambiar el nombre de usuario
    private void cambiarNombreUsuario() {
        System.out.print("\nNuevo nombre de usuario: ");
        String nuevoNombre = scanner.nextLine();

        // Intenta cambiar el nombre a través del controlador
        if (controlador.cambiarNombreUsuario(usuarioActual, nuevoNombre)) {
            System.out.println("Nombre de usuario cambiado correctamente.");
        } else {
            System.out.println("El nombre de usuario ya existe. Elija otro.");
        }
    }

    /**
     * Cambia la contraseña del usuario actual.
     * Solicita la contraseña actual y la nueva contraseña con confirmación.
     * Verifica que la contraseña actual sea correcta y que la nueva coincida en confirmación.
     */
    // Metodo para cambiar la contraseña
    private void cambiarContrasena() {
        System.out.print("\nContraseña actual: ");
        String actual = scanner.nextLine();

        System.out.print("Nueva contraseña: ");
        String nueva = scanner.nextLine();

        System.out.print("Confirmar nueva contraseña: ");
        String confirmacion = scanner.nextLine();

        // Verifica que ambas contraseñas coincidan
        if (!nueva.equals(confirmacion)) {
            System.out.println("Las contraseñas no coinciden.");
            return;
        }

        // Intenta cambiar la contraseña a través del controlador
        if (controlador.cambiarContrasena(usuarioActual, actual, nueva)) {
            System.out.println("Contraseña cambiada correctamente.");
        } else {
            System.out.println("Contraseña actual incorrecta.");
        }
    }

    /**
     * Gestiona la configuración de la unidad familiar actual.
     * Permite cambiar el nombre de la unidad o abandonarla.
     */
    // Metodo para gestionar la configuración de la unidad familiar
    private void gestionarUnidadFamiliarConfig() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE UNIDAD FAMILIAR ===");
            System.out.println("1. Cambiar nombre de la unidad");
            System.out.println("2. Abandonar unidad familiar");
            System.out.println("0. Volver");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1: {
                    // Cambia el nombre de la unidad familiar
                    System.out.print("Nuevo nombre para la unidad familiar: ");
                    String nuevoNombre = scanner.nextLine();
                    controlador.cambiarNombreUnidadFamiliar(unidadActual, nuevoNombre);
                    System.out.println("Nombre cambiado correctamente.");
                    break;
                }
                case 2: {
                    // Abandona la unidad familiar
                    System.out.print("¿Estás seguro de que deseas abandonar esta unidad familiar? (S/N): ");
                    String respuesta = scanner.nextLine().toUpperCase();
                    if (respuesta.equals("S")) {
                        controlador.abandonarUnidadFamiliar(usuarioActual, unidadActual);
                        unidadActual = null;
                        System.out.println("Has abandonado la unidad familiar.");
                        opcion = 0; // Para salir del menú
                        unirseOCrearUnidadFamiliar();
                        break;
                    }
                }
                break;
                case 0:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } while (opcion != 0);
    }
    // =================================================================
    // METODO AUXILIAR PARA BÚSQUEDA CON SUGERENCIAS
    // =================================================================

    /**
     * Permite al usuario localizar un producto de dos maneras:
     * 1) Mostrar todos los productos
     * 2) Buscar por nombre exacto
     * 3) Buscar por código de barras
     *
     * Devuelve el Producto seleccionado, o null si el usuario cancela o no hay resultados.
     */
    private Producto buscarProducto() {
        System.out.println("\n=== BUSCAR PRODUCTO ===");
        System.out.println("1) Mostrar todos los productos");
        System.out.println("2) Buscar por nombre");
        System.out.println("3) Buscar por código de barras");

        int modo = leerEntero("Elige una opción (0 para cancelar): ");

        List<Producto> resultados = Collections.emptyList();

        switch (modo) {
            case 0:
                return null; // Cancelar
            case 1:
                resultados = controlador.obtenerTodosProductos();
                break;
            case 2:
                System.out.print("Nombre del producto: ");
                String nombre = scanner.nextLine().trim();
                if (nombre.isEmpty()) {
                    System.out.println("El nombre no puede estar vacío.");
                    return null;
                }
                resultados = controlador.obtenerProductoPorNombre(nombre);
                break;
            case 3:
                System.out.print("Código de barras: ");
                try {
                    long cb = Long.parseLong(scanner.nextLine().trim());
                    if (cb <= 0) {
                        System.out.println("Código no válido.");
                        return null;
                    }
                    resultados = controlador.buscarProductoPorCodigoBarras(cb);
                } catch (NumberFormatException e) {
                    System.out.println("Código no válido.");
                    return null;
                }
                break;
            default:
                System.out.println("Opción fuera de rango.");
                return null;
        }

        // 4) Manejo de resultados
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron productos.");
            return null;
        }
        if (resultados.size() == 1) {
            return resultados.get(0);
        }

        // 5) Varios resultados: mostramos y dejamos elegir
        System.out.println("\nSe encontraron varios productos:");
        for (int i = 0; i < resultados.size(); i++) {
            Producto p = resultados.get(i);
            System.out.printf(
                    "%d) %s | Marca: %s | Supermercado: %s | €%.2f%n",
                    i+1, p.getNombre(), p.getMarca(), p.getSupermercado(), p.getPrecio()
            );
        }
        System.out.print("Selecciona el número (0 para cancelar): ");
        int sel;
        try {
            sel = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Selección no válida.");
            return null;
        }
        if (sel < 1 || sel > resultados.size()) {
            System.out.println("Selección fuera de rango.");
            return null;
        }
        return resultados.get(sel - 1);
    }

    /**
     * Lee un número entero del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @return El número entero ingresado.
     */
    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido.");
            }
        }
    }

    /**
     * Lee un número decimal (double) del usuario.
     * @param mensaje Mensaje a mostrar al usuario.
     * @return El número decimal ingresado.
     */
    private double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String entrada = scanner.nextLine()
                        .trim()
                        .replace(',', '.');

                // Verificar formato válido
                if (!entrada.matches("^\\d*\\.?\\d+$")) {
                    System.out.println("Formato inválido. Use números y ',' o '.' para decimales (ejemplo: 12,99 o 12.99)");
                    continue;
                }

                double precio = Double.parseDouble(entrada);

                // Validar rango
                if (precio < 0) {
                    System.out.println("El precio no puede ser negativo.");
                    continue;
                }
                if (precio > 99999.99) {
                    System.out.println("El precio es demasiado alto (máximo 99999,99)");
                    continue;
                }

                // Redondear a 2 decimales
                return Math.round(precio * 100.0) / 100.0;


            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número decimal válido.");
            }
        }
    }

    /**
     * Lee un número largo desde la entrada estándar.
     * Solicita al usuario ingresar un número largo válido y realiza validación.
     * @param mensaje Mensaje que se muestra al usuario para solicitar la entrada.
     * @return El número largo ingresado por el usuario.
     */
    private long leerLong(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero largo válido.");
            }
        }
    }

    /**
     * Lee una confirmación del usuario.
     * @param mensaje
     * @return true si el usuario confirma (S), false si cancela (N).
     */
    private boolean leerConfirmacion(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (S/N): ");
            String respuesta = scanner.nextLine().trim().toUpperCase();
            if (respuesta.equals("S")) return true;
            if (respuesta.equals("N")) return false;
            System.out.println("Error: Debe ingresar S o N.");
        }
    }

}


