
import java.util.*;

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

    // Metodo principal que inicia la aplicación
    public void iniciar() {
        // Bucle infinito hasta que el usuario elija salir
        while (true) {
            // Menú de bienvenida
            System.out.println("\n=== BIENVENIDO ===");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarse");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            // Lee la opción del usuario
            int opcion = Integer.parseInt(scanner.nextLine());

            // Switch para manejar las diferentes opciones
            switch (opcion) {
                case 1:
                    iniciarSesion(); // Llama al metodo de inicio de sesión
                    continue;
                case 2:
                    registrarUsuario(); // Llama al metodo de registro
                    continue;
                case 3: {
                    System.out.println("Saliendo...");
                    return;
                } // Sale del programa
                default:
                    System.out.println("Opción inválida."); // Opción no válida
            }
        }
    }

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


    // Metodo para gestionar la unidad familiar del usuario
    private void gestionarUnidadFamiliar() {
        // Obtiene la unidad familiar del usuario actual
        unidadActual = controlador.obtenerUnidadFamiliar(usuarioActual);


        // Si el usuario ya tiene una unidad familiar
        if (unidadActual != null) {
            System.out.println("\nEstás conectado a la Unidad Familiar: " + unidadActual.getNombre());
            System.out.println("1. Entrar en la Unidad Familiar");
            System.out.println("2. Unirse o crear otra Unidad Familiar");
            System.out.print("Seleccione una opción: ");

            int opcion = Integer.parseInt(scanner.nextLine());

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

    // Menú principal de la aplicación
    private void menuPrincipal() {
        int opcion;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Ver lista");
            System.out.println("2. Ver productos");
            System.out.println("3. Gestión de stock");
            System.out.println("4. Configuración");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1; // Opción inválida
            }

            switch (opcion) {
                case 1:
                    verLista();
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
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
    // Menú de gestión de productos
    private void menuProductos() {
        int opcion;
        do {
            System.out.println("\n=== PRODUCTOS ===");
            System.out.println("1. Ver todos los productos");
            System.out.println("2. Añadir producto");
            System.out.println("0. Volver atrás");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    verTodosProductos(); // Muestra todos los productos
                    continue;
                case 2:
                    anadirProducto(); // Permite añadir un producto
                    continue;
                case 0:
                    System.out.println("Volviendo..."); // Vuelve al menú anterior
                    menuPrincipal();

                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void menuStock() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE STOCK ===");
            System.out.println("1. Ver stock actual");
            System.out.println("2. Añadir producto al stock");
            System.out.println("3. Actualizar cantidad en stock");
            System.out.println("4. Eliminar producto del stock");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

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

    // Metodo para mostrar todos los productos
    private void verTodosProductos() {
        int opcion;
        do {
            // Obtiene todos los productos de la unidad familiar
            List<Producto> productos = controlador.obtenerTodosProductos();

            System.out.println("\n=== TODOS LOS PRODUCTOS ===");
            mostrarProductosTabla(productos); // Muestra en formato de tabla

            System.out.println("\n1. Filtrar");
            System.out.println("2. Seleccionar producto");
            System.out.println("0. Volver atrás");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    menuFiltros(); // Muestra menú de filtros
                    continue;
                case 2:
                    seleccionarProducto(); // Permite seleccionar un producto
                    continue;
                case 0:
                    System.out.println("Volviendo...");
                    menuPrincipal();
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

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

    private void eliminarProductoStock() {
        System.out.print("\nNombre del producto: ");
        String nombre = scanner.nextLine();
        Producto producto = buscarProductoEnStock();
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.print("¿Está seguro de eliminar " + producto.getNombre() + "? (S/N): ");
        if (!scanner.nextLine().equalsIgnoreCase("S")) {
            System.out.println("Operación cancelada");
            return;
        }
        controlador.eliminarProductoStock(unidadActual, producto);
        System.out.println("Producto eliminado del stock.");
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


    // Menú de filtros para productos
    private void menuFiltros() {
        int opcion;
        do {
            System.out.println("\n=== FILTRAR ===");
            System.out.println("1. Filtrar");
            System.out.println("2. Ordenar");
            System.out.println("0. Volver atrás");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    filtrado(); // Filtra por categoría/marca
                    continue;
                case 2:
                    ordenar(); // Ordena los productos
                    continue;
                case 0:
                    System.out.println("Volviendo...");
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // Metodo para filtrar por categoría o marca
    private void filtrado() {
        int opcion;
        do {
            System.out.println("\n=== FILTRAR POR CATEGORÍA/MARCA ===");
            System.out.println("1. Ver categorías");
            System.out.println("2. Ver marcas");
            System.out.println("3. Ver supermercado");
            System.out.println("0. Volver atrás");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

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
        System.out.print("\nSeleccione una categoría (0 para volver): ");
        int opcion;
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

        // 5) Delegamos en el método que ya tenías para mostrar subcategorías
        verSubcategorias(categoriaSeleccionada, subcategorias);
    }


    // Metodo para mostrar las subcategorías de una categoría
    private void verSubcategorias(String categoria, List<String> subcategorias) {
        System.out.println("\n=== SUBCATEGORÍAS DE " + categoria.toUpperCase() + " ===");
        // Muestra todas las subcategorías numeradas
        for (int i = 0; i < subcategorias.size(); i++) {
            System.out.println((i + 1) + ". " + subcategorias.get(i));
        }

        System.out.print("\nSeleccione una subcategoría (0 para volver): ");
        int opcion = Integer.parseInt(scanner.nextLine());

        // Si seleccionó una subcategoría válida, muestra sus productos
        if (opcion > 0 && opcion <= subcategorias.size()) {
            String subcategoria = subcategorias.get(opcion - 1);
            List<Producto> productos = controlador.obtenerProductosPorSubcategoria(subcategoria);
            mostrarProductosTabla(productos); // Muestra en formato de tabla
        }
    }

    // Metodo para mostrar todas las marcas disponibles
    private void verMarcas() {
        // Obtiene las marcas del controlador
        List<String> marcas = controlador.obtenerMarcas();

        System.out.println("\n=== MARCAS ===");
        // Muestra todas las marcas numeradas
        for (int i = 0; i < marcas.size(); i++) {
            System.out.println((i + 1) + ". " + marcas.get(i));
        }

        System.out.print("\nSeleccione una marca (0 para volver): ");
        int opcion = Integer.parseInt(scanner.nextLine());

        // Si seleccionó una marca válida, muestra sus productos
        if (opcion > 0 && opcion <= marcas.size()) {
            String marca = marcas.get(opcion - 1);
            List<Producto> productos = controlador.obtenerProductosPorMarca(marca);
            mostrarProductosTabla(productos); // Muestra en formato de tabla
        }
    }

    private void filtrarPorSupermercado() {
        List<String> supermercados = controlador.obtenerTodosSupermercados();
        System.out.println("\nSupermercados disponibles:");
        for (int i = 0; i < supermercados.size(); i++) {
            System.out.println((i + 1) + ". " + supermercados.get(i));
        }
        System.out.print("Seleccione supermercado (0 para cancelar): ");
        int opcion = Integer.parseInt(scanner.nextLine());
        if (opcion > 0 && opcion <= supermercados.size()) {
            List<Producto> productos = controlador.filtrarPorSupermercado(supermercados.get(opcion - 1));
            mostrarProductosTabla(productos);
        }
    }

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
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

            // Si seleccionó un orden válido, muestra los productos ordenados
            if (opcion >= 1 && opcion <= 8) {
                List<Producto> productos = controlador.ordenarProductos(opcion);
                mostrarProductosTabla(productos);
            } else if (opcion != 0) {
                System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

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
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

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
        System.out.print("\n¿Desea añadir un nuevo precio? (S/N): ");
        String respuesta = scanner.nextLine().toUpperCase();

        if (respuesta.equals("S")) {
            System.out.print("Introduce el nuevo precio: ");
            double nuevoPrecio = Double.parseDouble(scanner.nextLine());
            // Añade el nuevo precio a través del controlador
            controlador.actualizarPrecioProducto(producto, nuevoPrecio);
            System.out.println("Precio actualizado correctamente.");
        }
    }

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
        System.out.print("\n¿Desea añadir/modificar su puntuación? (S/N): ");
        String respuesta = scanner.nextLine().toUpperCase();

        if (respuesta.equals("S")) {
            System.out.print("Introduce tu puntuación (1-5 estrellas): ");
            int puntuacion = Integer.parseInt(scanner.nextLine());
            // Añade la puntuación a través del controlador
            controlador.anadirPuntuacionProducto(producto, usuarioActual, puntuacion);
            System.out.println("Puntuación guardada correctamente.");
        }
    }

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
        System.out.print("Seleccione una opción: ");

        int opcion = Integer.parseInt(scanner.nextLine());

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

    // Metodo para añadir un nuevo producto, con categoría y subcategoría opcional
    private void anadirProducto() {
        System.out.println("\n=== AÑADIR PRODUCTO ===");

        // 1) Datos básicos
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Marca: ");
        String marca = scanner.nextLine().trim();
        System.out.print("Precio: ");
        double precio = Double.parseDouble(scanner.nextLine().trim());

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
        System.out.print("Código de barras: ");
        long codigo = Long.parseLong(scanner.nextLine().trim());
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();

        // 5) Insertar producto en BD
        Producto p = controlador.crearProducto(
                nombre, marca, precio,
                catFull,    // categoría o "categoría.subcategoría"
                codigo, descripcion
        );
        if (p == null) {
            System.out.println("❌ Error al crear el producto.");
            return;
        }

        // 6) Añadir al stock de la unidad familiar (cantidad inicial 1)
        controlador.anadirProductoStock(unidadActual, p, 1);
        System.out.println("Producto añadido correctamente con categoría: " + catFull);
    }


    // Metodo para mostrar la lista de productos de la unidad familiar
    private void verLista() {
        // Obtiene los productos de la unidad familiar como un Map
        Map<Producto, Integer> productosMap = controlador.obtenerProductosUnidadFamiliar(unidadActual);
        List<Producto> productos = new ArrayList<>(productosMap.keySet());

        System.out.println("\n=== LISTA DE PRODUCTOS ===");
        mostrarProductosTabla(productos); // Muestra en formato de tabla
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


    // Menú de configuración
    private void menuConfiguracion() {
        int opcion;
        do {
            System.out.println("\n=== CONFIGURACIÓN ===");
            System.out.println("1. Cambiar nombre de usuario");
            System.out.println("2. Cambiar contraseña");
            System.out.println("3. Gestionar unidad familiar");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    cambiarNombreUsuario(); // Cambia nombre de usuario
                    continue;
                case 2:
                    cambiarContrasena(); // Cambia contraseña
                    continue;

                case 3:
                    gestionarUnidadFamiliarConfig(); // Gestiona unidad familiar
                    continue;

                case 0:
                    System.out.println("Volviendo...");
                    menuPrincipal();
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

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

    // Metodo para gestionar la configuración de la unidad familiar
    private void gestionarUnidadFamiliarConfig() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE UNIDAD FAMILIAR ===");
            System.out.println("1. Cambiar nombre de la unidad");
            System.out.println("2. Abandonar unidad familiar");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1: {
                    // Cambia el nombre de la unidad familiar
                    System.out.print("Nuevo nombre para la unidad familiar: ");
                    String nuevoNombre = scanner.nextLine();
                    controlador.cambiarNombreUnidadFamiliar(unidadActual, nuevoNombre);
                    System.out.println("Nombre cambiado correctamente.");
                    continue;
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
                    }
                }
                case 0:
                    System.out.println("Volviendo...");
                default:
                    System.out.println("Opción inválida.");
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
        System.out.print("Elige una opción (0 para cancelar): ");

        int modo;
        try {
            modo = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Entrada no válida.");
            return null;
        }
        if (modo == 0) return null;

        List<Producto> resultados = Collections.emptyList();

        switch (modo) {
            case 1 -> resultados = controlador.obtenerTodosProductos();
            case 2 -> {
                System.out.print("Nombre del producto: ");
                String nombre = scanner.nextLine().trim();
                resultados = controlador.obtenerProductoPorNombre(nombre);

            }
            case 3 -> {
                System.out.print("Código de barras: ");
                try {
                    long cb = Long.parseLong(scanner.nextLine().trim());
                    resultados = controlador.buscarProductoPorCodigoBarras(cb);
                } catch (NumberFormatException e) {
                    System.out.println("Código no válido.");
                    return null;
                }
            }
            default -> {
                System.out.println("Opción fuera de rango.");
                return null;
            }
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


}


