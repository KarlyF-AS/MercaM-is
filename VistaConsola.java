
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
                case 1 : iniciarSesion(); // Llama al metodo de inicio de sesión
                case 2 : registrarUsuario(); // Llama al metodo de registro
                case 3 : { System.out.println("Saliendo..."); return; } // Sale del programa
                default : System.out.println("Opción inválida."); // Opción no válida
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
        String contraseña = scanner.nextLine();

        // Intenta iniciar sesión a través del controlador
        usuarioActual = controlador.iniciarSesion(correo, contraseña);

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
            nombreUsuario = scanner.nextLine();
        }

        // Validación del formato de correo electrónico
        String correo;
        while (true) {
            System.out.print("Correo electrónico: ");
            correo = scanner.nextLine();
            if (correo.matches(".*@.*\\..*")) {
                break;
            }
            System.out.println("El correo debe tener formato válido (ejemplo@dominio.com)");
            // Verifica con el controlador si el usuario ya existe
            if (!controlador.existeEmail(nombreEmail)) {
                break; // Sale del bucle si el nombre está disponible
            }
            System.out.println("Este email ya existe. Elija otro.");
        }

        // Validación de que ambas contraseñas coincidan
        String contraseña, confirmacion;
        while (true) {
            System.out.print("Contraseña: ");
            contraseña = scanner.nextLine();
            System.out.print("Confirmar contraseña: ");
            confirmacion = scanner.nextLine();
            if (contraseña.equals(confirmacion)) {
                break;
            }
            System.out.println("Las contraseñas no coinciden. Intente de nuevo.");
        }

        // Registra el usuario a través del controlador
        usuarioActual = controlador.registrarUsuario(nombreUsuario, correo, contraseña);
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
        unidadActual = controlador.crearUnidadFamiliar(usuarioActual, nombre);
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

            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1 : verLista(); // Muestra la lista de productos
                case 2 : menuProductos(); // Muestra el menú de productos
                case 3 : menuStock(); // Muestra el menú de gestión de stock
                case 4 : menuConfiguracion(); // Muestra el menú de configuración
                case 0 : {
                    usuarioActual = null; // Cierra sesión
                    unidadActual = null;
                    System.out.println("Sesión cerrada.");
                }
                default : System.out.println("Opción inválida.");
            }
        } while (opcion != 0); // Repite hasta que elija salir
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
                case 1 : verTodosProductos(); // Muestra todos los productos
                case 2 : anadirProducto(); // Permite añadir un producto
                case 0 : System.out.println("Volviendo..."); // Vuelve al menú anterior
                default : System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void menuStock(){
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
                case 1 : verStockActual();
                case 2 : añadirProductoStock();
                case 3 : actualizarCantidadStock();
                case 4 : eliminarProductoStock();
                case 0 : System.out.println("Volviendo...");
                default : System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // Metodo para mostrar todos los productos
    private void verTodosProductos() {
        int opcion;
        do {
            // Obtiene todos los productos de la unidad familiar
            List<Producto> productos = controlador.obtenerTodosProductos(unidadActual);

            System.out.println("\n=== TODOS LOS PRODUCTOS ===");
            mostrarProductosTabla(productos); // Muestra en formato de tabla

            System.out.println("\n1. Filtrar");
            System.out.println("2. Seleccionar producto");
            System.out.println("0. Volver atrás");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1 : menuFiltros(); // Muestra menú de filtros
                case 2 : seleccionarProducto(); // Permite seleccionar un producto
                case 0 : System.out.println("Volviendo...");
                default : System.out.println("Opción inválida.");
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
        Map<Producto, Integer> stock = controlador.obtenerStock(unidadActual);

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
        List<Producto> productosListaCompra = controlador.obtenerProductosUnidadFamiliar(unidadActual);

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
            for (Producto productoLista : productosListaCompra) {
                if (productoLista.getId().equals(p.getId())) {
                    enLista = "Sí";  // Marca como presente en lista
                    cantidadLista = productoLista.getCantidad();  // Captura la cantidad deseada
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
                            String.format("%.1f", p.getPuntuacionMedia()) + "\t| " +  // Columna 5: Puntuación
                            String.format("%.2f€", p.getUltimoPrecio()) + "\t| " +  // Columna 6: Precio
                            String.join(", ", p.getSupermercados())  // Columna 7: Supermercados
            );
        }
    }

    private void añadirProductoStock(){
        System.out.print("\nNombre del producto: ");
        String nombre = scanner.nextLine();
        Producto producto = buscarProductoConSugerencias();
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.print("Cantidad inicial: ");
        int cantidad = Integer.parseInt(scanner.nextLine());
        controlador.añadirProductoStock(unidadActual, producto, cantidad);
        System.out.println("Producto añadido al stock.");
    }

    private void actualizarCantidadStock(){
        System.out.print("\nNombre del producto: ");
        String nombre = scanner.nextLine();
        Producto producto = buscarProductoConSugerencias();
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.print("Nueva cantidad: ");
        int nuevaCantidad = Integer.parseInt(scanner.nextLine());
        controlador.actualizarCantidadStock(unidadActual, producto, nuevaCantidad);
        System.out.println("Cantidad actualizada.");
    }

    private void eliminarProductoStock(){
        System.out.print("\nNombre del producto: ");
        String nombre = scanner.nextLine();
        Producto producto = buscarProductoConSugerencias();
        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.print("¿Está seguro de eliminar "+producto.getNombre()+"? (S/N): ");
        if (!scanner.nextLine().equalsIgnoreCase("S")) {
            System.out.println("Operación cancelada");
            return;
        }
        controlador.eliminarProductoStock(unidadActual, producto);
        System.out.println("Producto eliminado del stock.");
    }

    // Menú de filtros para productos
    private void menuFiltros() {
        int opcion;
        do {
            System.out.println("\n=== FILTRAR ===");
            System.out.println("1. Filtrar por categoría/marca");
            System.out.println("2. Ordenar");
            System.out.println("0. Volver atrás");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1 : filtrarPorCategoriaMarca(); // Filtra por categoría/marca
                case 2 : filtrarPorOrden(); // Ordena los productos
                case 0 : System.out.println("Volviendo...");
                default : System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // Metodo para filtrar por categoría o marca
    private void filtrarPorCategoriaMarca() {
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
                case 1 : verCategorias(); // Muestra categorías
                case 2 : verMarcas(); // Muestra marcas
                case 3 : filtrarPorSupermercado(); // Muestra supermercados
                case 0 : System.out.println("Volviendo...");
                default : System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // Metodo para mostrar todas las categorías disponibles
    private void verCategorias() {
        // Obtiene las categorías del controlador
        Map<String, List<String>> categorias = controlador.obtenerCategorias();

        System.out.println("\n=== CATEGORÍAS ===");
        int i = 1;
        // Muestra todas las categorías numeradas
        for (String categoria : categorias.keySet()) {
            System.out.println(i++ + ". " + categoria);
        }

        System.out.print("\nSeleccione una categoría (0 para volver): ");
        int opcion = Integer.parseInt(scanner.nextLine());

        // Si seleccionó una categoría válida, muestra sus subcategorías
        if (opcion > 0 && opcion <= categorias.size()) {
            String categoriaSeleccionada = (String) categorias.keySet().toArray()[opcion-1];
            verSubcategorias(categoriaSeleccionada, categorias.get(categoriaSeleccionada));
        }
    }

    // Metodo para mostrar las subcategorías de una categoría
    private void verSubcategorias(String categoria, List<String> subcategorias) {
        System.out.println("\n=== SUBCATEGORÍAS DE " + categoria.toUpperCase() + " ===");
        // Muestra todas las subcategorías numeradas
        for (int i = 0; i < subcategorias.size(); i++) {
            System.out.println((i+1) + ". " + subcategorias.get(i));
        }

        System.out.print("\nSeleccione una subcategoría (0 para volver): ");
        int opcion = Integer.parseInt(scanner.nextLine());

        // Si seleccionó una subcategoría válida, muestra sus productos
        if (opcion > 0 && opcion <= subcategorias.size()) {
            String subcategoria = subcategorias.get(opcion-1);
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
            System.out.println((i+1) + ". " + marcas.get(i));
        }

        System.out.print("\nSeleccione una marca (0 para volver): ");
        int opcion = Integer.parseInt(scanner.nextLine());

        // Si seleccionó una marca válida, muestra sus productos
        if (opcion > 0 && opcion <= marcas.size()) {
            String marca = marcas.get(opcion-1);
            List<Producto> productos = controlador.obtenerProductosPorMarca(marca);
            mostrarProductosTabla(productos); // Muestra en formato de tabla
        }
    }
    private void filtrarPorSupermercado() {
        List<String> supermercados = controlador.obtenerTodosSupermercados();
        System.out.println("\nSupermercados disponibles:");
        for (int i = 0; i < supermercados.size(); i++) {
            System.out.println((i+1) + ". " + supermercados.get(i));
        }
        System.out.print("Seleccione supermercado (0 para cancelar): ");
        int opcion = Integer.parseInt(scanner.nextLine());
        if (opcion > 0 && opcion <= supermercados.size()) {
            List<Producto> productos = controlador.filtrarPorSupermercado(supermercados.get(opcion-1));
            mostrarProductosTabla(productos);
        }
    }

    // Metodo para ordenar los productos por diferentes criterios
    private void filtrarPorOrden() {
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
        Producto producto = controlador.obtenerProductoPorNombre(nombre);
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
            System.out.println("Precio: " + producto.getUltimoPrecio() + "€ (pulsa 1 para ver/modificar historial)");
            System.out.println("Puntuación media: " + producto.getPuntuacionMedia() + " (pulsa 2 para ver/modificar puntuaciones)");
            System.out.println("ID/Código de barras: " + producto.getId());
            System.out.println("Categoría: " + producto.getCategoria());
            System.out.println("Subcategoría: " + producto.getSubcategoria());
            System.out.println("Supermercados: " + String.join(", "+ producto.getSupermercados()));

            System.out.println("\n1. Ver/modificar historial de precios");
            System.out.println("2. Ver/modificar puntuaciones");
            System.out.println("3. Modificar supermercados");
            System.out.println("0. Volver atrás");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1 : gestionarHistorialPrecios(producto); // Gestiona precios
                case 2 : gestionarPuntuaciones(producto); // Gestiona puntuaciones
                case 3 : modificarSupermercados(producto); // Modifica supermercados
                case 0 : System.out.println("Volviendo...");
                default : System.out.println("Opción inválida.");
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
                System.out.println((i+1) + ". " + historial.get(i) + "€");
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
        Map<Usuario, Integer> puntuaciones = producto.getPuntuaciones();

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
        List<String> supermercados = producto.getSupermercados();

        System.out.println("Supermercados actuales:");
        // Muestra los supermercados numerados
        for (int i = 0; i < supermercados.size(); i++) {
            System.out.println((i+1) + ". " + supermercados.get(i));
        }

        System.out.println("\n1. Añadir supermercado");
        System.out.println("2. Eliminar supermercado");
        System.out.println("0. Volver");
        System.out.print("Seleccione una opción: ");

        int opcion = Integer.parseInt(scanner.nextLine());

        switch (opcion) {
            case 1 : {
                // Añade un nuevo supermercado
                System.out.print("Introduce el nombre del nuevo supermercado: ");
                String nuevoSuper = scanner.nextLine();
                controlador.anadirSupermercadoProducto(producto, nuevoSuper);
                System.out.println("Supermercado añadido correctamente.");
            }
            case 2 : {
                // Elimina un supermercado existente
                System.out.print("Introduce el número del supermercado a eliminar: ");
                int numEliminar = Integer.parseInt(scanner.nextLine());
                if (numEliminar > 0 && numEliminar <= supermercados.size()) {
                    controlador.eliminarSupermercadoProducto(producto, supermercados.get(numEliminar-1));
                    System.out.println("Supermercado eliminado correctamente.");
                } else {
                    System.out.println("Número inválido.");
                }
            }
            case 0 : System.out.println("Volviendo...");
            default : System.out.println("Opción inválida.");
        }
    }

    // Metodo para añadir un nuevo producto
    private void anadirProducto() {
        System.out.println("\n=== AÑADIR PRODUCTO ===");

        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();

        System.out.print("Marca: ");
        String marca = scanner.nextLine();

        System.out.print("Precio: ");
        double precio = Double.parseDouble(scanner.nextLine());

        // Obtiene todas las categorías disponibles
        Map<String, List<String>> categorias = controlador.obtenerCategorias();
        System.out.println("\nCategorías disponibles:");
        int i = 1;
        // Muestra las categorías numeradas
        for (String categoria : categorias.keySet()) {
            System.out.println(i++ + ". " + categoria);
        }

        System.out.print("Seleccione una categoría: ");
        int opcionCategoria = Integer.parseInt(scanner.nextLine());
        String categoriaSeleccionada = (String) categorias.keySet().toArray()[opcionCategoria-1];

        // Obtiene las subcategorías de la categoría seleccionada
        List<String> subcategorias = categorias.get(categoriaSeleccionada);
        System.out.println("\nSubcategorías disponibles:");
        // Muestra las subcategorías numeradas
        for (i = 0; i < subcategorias.size(); i++) {
            System.out.println((i+1) + ". " + subcategorias.get(i));
        }

        System.out.print("Seleccione una subcategoría: ");
        int opcionSubcategoria = Integer.parseInt(scanner.nextLine());
        String subcategoriaSeleccionada = subcategorias.get(opcionSubcategoria-1);

        System.out.print("ID/Código de barras: ");
        String id = scanner.nextLine();

        // Crea el nuevo producto a través del controlador
        Producto nuevoProducto = controlador.crearProducto(
                nombre, marca, precio, categoriaSeleccionada,
                subcategoriaSeleccionada, id, unidadActual );

        // Pregunta si quiere añadir supermercados
        System.out.print("¿Desea añadir supermercados para este producto? (S/N): ");
        String respuesta = scanner.nextLine().toUpperCase();

        // Bucle para añadir varios supermercados
        while (respuesta.equals("S")) {
            System.out.print("Nombre del supermercado: ");
            String supermercado = scanner.nextLine();
            controlador.anadirSupermercadoProducto(nuevoProducto, supermercado);

            System.out.print("¿Añadir otro supermercado? (S/N): ");
            respuesta = scanner.nextLine().toUpperCase();
        }

        System.out.println("Producto añadido correctamente.");
    }

    // Metodo para mostrar la lista de productos de la unidad familiar
    private void verLista() {
        // Obtiene los productos de la unidad familiar
        List<Producto> productos = controlador.obtenerProductosUnidadFamiliar(unidadActual);

        System.out.println("\n=== LISTA DE PRODUCTOS ===");
        mostrarProductosTabla(productos); // Muestra en formato de tabla
    }

    // Metodo auxiliar para mostrar productos en formato de tabla
    private void mostrarProductosTabla(List<Producto> productos) {
        // Encabezado simple
        System.out.println("Nombre | Marca | Categoría | Punt. | Precio | Supermercados");
        System.out.println("-------------------------------------------------------------");

        // Mostrar cada producto sin formato de ancho fijo
        for (Producto p : productos) {
            System.out.printf("%s | %s | %s | %.1f | %.2f | %s%n",
                    p.getNombre(),
                    p.getMarca(),
                    p.getSubcategoria(),
                    p.getPuntuacionMedia(),
                    p.getUltimoPrecio(),
                    String.join(", ", p.getSupermercados()));
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
                case 1 : cambiarNombreUsuario(); // Cambia nombre de usuario
                case 2 : cambiarContrasena(); // Cambia contraseña
                case 3 : gestionarUnidadFamiliarConfig(); // Gestiona unidad familiar
                case 0 : System.out.println("Volviendo...");
                default : System.out.println("Opción inválida.");
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
                case 1 : {
                    // Cambia el nombre de la unidad familiar
                    System.out.print("Nuevo nombre para la unidad familiar: ");
                    String nuevoNombre = scanner.nextLine();
                    controlador.cambiarNombreUnidadFamiliar(unidadActual, nuevoNombre);
                    System.out.println("Nombre cambiado correctamente.");
                }
                case 2 : {
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
                case 0 : System.out.println("Volviendo...");
                default : System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
}
