Uso de Estructuras de Colección en el Proyecto

Este proyecto hace uso de List, Map y Set para gestionar los datos relacionados con productos, precios, listas de la compra y unidades familiares.


La interfaz List se emplea cuando es necesario mantener una colección ordenada de elementos, permitiendo acceso secuencial, ordenación y manipulación flexible.
1. Obtener y ordenar productos

List<Producto> productos = Model.recogerTodosProductos();

    Se utiliza para almacenar todos los productos recuperados del modelo.

    Permite ordenar la colección fácilmente con Collections.sort().

    Mantiene el orden de los elementos tras la ordenación.

    Es ideal para recorrer o presentar productos en la interfaz de usuario.

2. Método: ordenarProductos(int opcion)

public static List<Producto> ordenarProductos(int opcion) {
List<Producto> productos = Model.recogerTodosProductos();
// Código de ordenación...
return productos;
}

    Se devuelve una lista ordenada de productos.

    La lista es manipulable y permite aplicar distintos criterios de ordenación.

3. Historial de precios

public List<Double> getHistorialPrecios()

    Almacena un historial cronológico de precios.

    El orden es importante para representar la evolución temporal.

    Permite añadir nuevos precios manteniendo el historial.

    Facilita acceder al precio más reciente o a valores anteriores.

4. Obtener todos los productos

public List<Producto> obtenerTodosProductos()

    Devuelve todos los productos disponibles en el sistema.

    Facilita su presentación ordenada en la interfaz.

    Permite recorrerlos de forma sencilla para su procesamiento o visualización.

5. Buscar productos por código de barras

   La búsqueda puede devolver múltiples coincidencias.

   Se usa List<Producto> para mantener todas las coincidencias encontradas.

   Asegura consistencia con otros métodos que devuelven colecciones de productos.

 Uso de Map

Map se utiliza para almacenar pares clave-valor. En este proyecto, se emplea principalmente para asociar productos con cantidades.
1. Lista de la compra

private HashMap<Producto, Integer> productosCantidad;

public ListaCompra() {
this.productosCantidad = new HashMap<>();
}

    Asocia cada producto con su cantidad correspondiente.

    Permite acceso directo y rápido a la cantidad de un producto.

    Evita duplicados: cada producto aparece solo una vez como clave.

    Facilita la actualización de cantidades.

2. Productos por unidad familiar

public Map<Producto, Integer> obtenerProductosUnidadFamiliar(Lista_UnidadFamiliar unidad)

    Gestiona la relación producto-cantidad por unidad familiar.

    Permite saber cuánto tiene o necesita cada familia.

    Útil para llevar un control de inventario personalizado.

3. Lógica de negocio en el modelo

public static Map<Producto, Integer> obtenerProductosUnidadFamiliar(/*...*/) {
Map<Producto, Integer> productos = new HashMap<>();
// ...
}

    Encapsula la lógica de almacenamiento de productos por familia.

    Eficiente para operaciones CRUD sobre productos y sus cantidades.

 Uso de Set

Set se utiliza cuando no se permiten duplicados en la colección. Aquí se emplea para extraer categorías únicas.
Categorías principales

public List<String> obtenerCategoriasPrincipales() {
Set<String> únicas = new LinkedHashSet<>();
// Se agregan las categorías sin duplicados
}

    Elimina categorías duplicadas automáticamente.

    Usa LinkedHashSet para mantener el orden de inserción.

    Garantiza que cada categoría principal aparezca una sola vez.

    Ideal para generar menús o filtros únicos en la interfaz.