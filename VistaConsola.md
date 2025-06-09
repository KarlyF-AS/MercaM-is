# VistaConsola - Documentación

## 1. `iniciar()`

**Descripción:**
Punto de entrada de la aplicación. Muestra el menú de bienvenida y permite al usuario elegir entre iniciar sesión, registrarse o salir.

**Flujo interno:**

1. Entra en un bucle infinito que persiste hasta que el usuario selecciona la opción “Salir”.
2. Imprime en consola las opciones: iniciar sesión, registrarse o salir.
3. Lee la opción del usuario usando `leerEntero()` para garantizar que se introduce un número.
4. Mediante un `switch`, redirige la ejecución a:

    * `iniciarSesion()`: si el usuario elige iniciar sesión.
    * `registrarUsuario()`: si decide registrarse.
    * `return`: si elige salir, finalizando la aplicación.
    * Muestra un mensaje de “Opción inválida.” para entradas fuera de rango.

---

## 2. `iniciarSesion()`

**Descripción:**
Gestiona todo el proceso de login: validación de formato, solicitud de credenciales y verificación con el controlador.

**Flujo interno:**

1. Solicita el correo electrónico en bucle hasta que cumpla el patrón básico de email (`.*@.*\..*`).
2. Pide la contraseña sin validaciones adicionales sobre contenido.
3. Llama a `controlador.iniciarSesion(correo, contrasena)`, almacenando el resultado en `usuarioActual`.
4. Si `usuarioActual` es `null`, informa de credenciales incorrectas y regresa al menú principal.
5. En caso de éxito, invoca `gestionarUnidadFamiliar()` para continuar con la configuración de la unidad familiar.

---

## 3. `registrarUsuario()`

**Descripción:**
Recopila datos para crear un nuevo usuario y los envía al controlador. Tras un registro exitoso, dirige al paso de unidad familiar.

**Flujo interno:**

1. Pide el nombre de usuario hasta que no esté vacío.
2. Solicita el correo electrónico:

    * Valida el formato con expresión regular.
    * Comprueba con `controlador.existeEmail(correo)` que no esté registrado.
3. Pide y confirma la contraseña en bucle hasta que ambas coincidan.
4. Llama a `controlador.registrarUsuario(nombreUsuario, correo, contrasena)` y asigna el resultado a `usuarioActual`.
5. Muestra mensaje de éxito y llama a `unirseOCrearUnidadFamiliar()`.

---

## 4. `gestionarUnidadFamiliar()`

**Descripción:**
Determina si el usuario ya pertenece a una unidad familiar y ofrece opciones para gestionarla o cambiar a otra.

**Flujo interno:**

1. Recupera la unidad actual con `controlador.obtenerUnidadFamiliar(usuarioActual)`.
2. Si existe una unidad:

    * Muestra dos opciones: entrar a la actual o unirse/crear otra.
    * Según la elección, invoca `menuPrincipal()` o `unirseOCrearUnidadFamiliar()`.
3. Si no hay unidad familiar asignada, llama directamente a `unirseOCrearUnidadFamiliar()`.

---

## 5. `unirseOCrearUnidadFamiliar()`

**Descripción:**
Permite al usuario unirse a una unidad existente mediante código o crear una nueva.

**Flujo interno:**

1. Pregunta si el usuario dispone de un código (S/N).
2. Si la respuesta es “S”:

    * Solicita el código y prueba `controlador.unirseAUnidadFamiliar(usuarioActual, codigo)`.
    * Si el código es válido, informa y lanza `menuPrincipal()`.
    * Si no, vuelve a preguntar si desea crear una nueva unidad.
3. Si la respuesta inicial fue “N” o tras error, pregunta si desea crear la unidad:

    * En caso afirmativo, invoca `crearUnidadFamiliar()`.

---

## 6. `crearUnidadFamiliar()`

**Descripción:**
Solicita un nombre para una nueva unidad familiar, la crea y redirige al menú principal.

**Flujo interno:**

1. Pide al usuario el nombre de la unidad.
2. Llama a `controlador.crearUnidadFamiliar(usuarioActual, nombre, idGenerado)` donde `idGenerado` proviene de la lógica (e.g., `Controller2.generarIdUsuario()`).
3. Informa del éxito mostrando el código generado.
4. Lanza `menuPrincipal()`.

---

## 7. `menuPrincipal()`

**Descripción:**
Muestra las principales opciones de la aplicación (lista de compra, productos, stock, configuración) y gestiona la navegación.

**Flujo interno:**

1. En cada iteración, llama a `inicializarStock()` para testear la conexión.
2. Muestra las opciones numeradas y lee la elección con `leerEntero()`.
3. Mediante `switch`, redirige a:

    * `mostrarMenu()`: navegación en la lista de compra.
    * `menuProductos()`: gestión de productos.
    * `menuStock()`: gestión de stock.
    * `menuConfiguracion()`: ajustes de usuario.
    * Cerrar sesión: reinicia `usuarioActual` y `unidadActual` y vuelve a `iniciar()`.
4. Bucle hasta que el usuario elija cerrar sesión.

---

## 8. `menuProductos()`

**Descripción:**
Submenú para ver productos disponibles o añadir uno nuevo.

**Flujo interno:**

1. Muestra tres opciones: ver todos los productos, añadir producto o volver.
2. Lee la opción con `leerEntero()`.
3. Según la opción:

    * `verTodosProductos()`: lista y detalle de productos.
    * `anadirProducto()`: invoca al controlador para añadir.
    * Volver: imprime “Volviendo...” y llama a `menuPrincipal()`.
4. Repite hasta que el usuario seleccione “Volver”.

---

## 9. `menuStock()`

**Descripción:**
Submenú para gestionar el stock de la unidad familiar: ver, añadir, actualizar o eliminar.

**Flujo interno:**

1. Imprime las opciones de stock y lee la elección.
2. Según la opción:

    * `verStockActual()`: muestra la cantidad de cada producto en stock.
    * `añadirProductoStock()`: añade unidades de un producto.
    * `actualizarCantidadStock()`: modifica la cantidad existente.
    * `eliminarProductoStock()`: quita totalmente un producto.
    * Volver: imprime “Volviendo...” y retorna a `menuPrincipal()`.
3. Bucle hasta opción “Volver”.

---

## 10. `inicializarStock()`

**Descripción:**
Comprueba la conectividad con la base de datos creando y eliminando un producto de prueba.

**Flujo interno:**

1. Crea un objeto `Producto` de prueba con valores genéricos.
2. Intenta:

    * `controlador.anadirProductoStock(unidadActual, productoPrueba, 10)`
    * `controlador.eliminarProductoStock(unidadActual, productoPrueba)`
3. Captura y desprecia cualquier excepción para evitar interrupciones si falla la FK.

---

## 11. `verTodosProductos()`

**Descripción:**
Muestra en consola la lista de todos los productos disponibles y permite filtrarlos o seleccionar uno.

**Flujo interno:**

1. Llama a `controlador.obtenerTodosProductos()` y almacena la lista.
2. Si está vacía, muestra “No hay productos disponibles.” y retorna.
3. Formatea e imprime encabezado y cada producto con nombre, marca, categoría, supermercado, precio y puntuación media:

    * Obtiene puntuaciones individuales con `controlador.getPuntuaciones(p)`.
    * Calcula la media usando streams.
4. Al final, ofrece tres opciones: `menuFiltros()`, `seleccionarProducto()` o volver.
5. Captura `BaseDatosException` para informar de errores al obtener datos.

---

### 12. `verStockActual()`

Este método muestra por consola todos los productos disponibles actualmente en el stock de la unidad familiar.

**Pasos principales:**

1. Muestra un encabezado: `"=== STOCK ACTUAL ==="`.
2. Recupera el stock actual como un `Map<Producto, Integer>` del controlador (donde la clave es el producto y el valor, la cantidad).
3. Si el mapa está vacío, indica que el stock está vacío y termina el método.
4. También recupera la lista de productos en la lista de compra para indicar qué productos están en ambas listas.
5. Recorre todos los productos del stock:

    * Comprueba si también están en la lista de compra.
    * Imprime una tabla con:

        * Nombre del producto.
        * Marca.
        * Cantidad en stock.
        * Si está en lista de compra y cuánta cantidad.
        * Puntuación media.
        * Precio.
        * Supermercados donde se puede adquirir.

Este método permite a los usuarios tener una visión rápida, clara y detallada del estado actual de su despensa o inventario doméstico.

---

### 13. `añadirProductoStock()`

Permite al usuario añadir una cantidad inicial de un producto al stock.

**Funcionamiento:**

1. Llama a `buscarProducto()` para identificar un producto existente.
2. Si el producto no se encuentra, se notifica y se cancela la operación.
3. Pide al usuario la cantidad inicial de unidades que desea añadir.
4. Llama al controlador para registrar esa cantidad en el stock de la unidad familiar.
5. Muestra el nuevo total actualizado para ese producto en stock.

---

### 14. `actualizarCantidadStock()`

Modifica la cantidad de unidades disponibles de un producto que ya existe en el stock.

**Pasos:**

1. Solicita al usuario el nombre del producto.
2. Busca el producto con `buscarProducto()`.
3. Si no se encuentra, se interrumpe el proceso.
4. Pide la nueva cantidad a asignar.
5. Se actualiza el stock a través del controlador.
6. Informa de que la cantidad se ha actualizado correctamente.

---

### 15. `eliminarProductoStock()`

Elimina un producto específico del stock, tras confirmar con el usuario.

**Detalles del proceso:**

1. Solicita el nombre del producto a eliminar.
2. Obtiene todos los productos actualmente en stock.
3. Busca el producto por nombre (ignorando mayúsculas/minúsculas).
4. Si se encuentra, pide confirmación al usuario.
5. Si se confirma, llama al controlador para eliminar el producto del stock.
6. Informa si la operación fue exitosa o si hubo algún error.

---

### 16. `buscarProductoEnStock()`

Permite al usuario seleccionar un producto dentro del stock (solo si hay unidades disponibles).

**Funcionamiento:**

1. Recupera los productos con stock disponible (como `Map<Integer, Producto>`).
2. Si no hay productos, lo informa y finaliza.
3. Si solo hay uno, lo devuelve directamente.
4. Si hay varios:

    * Muestra una lista numerada con detalles de cada producto (nombre, marca, supermercado, cantidad).
    * Solicita una selección al usuario.
    * Verifica que la opción esté dentro del rango válido.
    * Devuelve el producto seleccionado.

---

### 17. `menuFiltros()`

Muestra al usuario un menú para acceder a las funcionalidades de filtrado u ordenación de productos.

**Opciones que ofrece:**

1. Filtrar productos (por categoría, marca o supermercado).
2. Ordenar productos según un criterio.
3. Volver atrás al menú principal.

Dependiendo de la elección, llama a los métodos correspondientes (`filtrado()` u `ordenar()`).

---

### 18. `filtrado()`

Despliega un submenú que permite al usuario elegir el tipo de filtro que quiere aplicar.

**Opciones de filtrado:**

1. Ver categorías (y subcategorías).
2. Ver marcas.
3. Ver supermercados.
4. Volver atrás al menú anterior.

Cada opción conduce a otro método específico (`verCategorias()`, `verMarcas()`, `filtrarPorSupermercado()`).

---

### 19. `verCategorias()`

Muestra todas las categorías principales existentes y permite al usuario seleccionar una para ver sus subcategorías.

**Proceso:**

1. Recupera todas las categorías disponibles a través del controlador.
2. Muestra cada categoría con un número asociado.
3. Solicita al usuario una selección.
4. Valida la opción elegida.
5. Si es válida, obtiene y muestra las subcategorías asociadas a esa categoría mediante el método `verSubcategorias()`.

---

### 20. `verSubcategorias()`

(Asumido aunque no se incluyó en el fragmento anterior. Se documenta por coherencia.)

Muestra las subcategorías relacionadas con una categoría seleccionada previamente.

**Pasos:**

1. Llama al controlador para obtener las subcategorías de una categoría específica.
2. Muestra por consola todas las subcategorías asociadas.

---

### 21. `verMarcas()`

Muestra todas las marcas disponibles en el sistema.

**Funcionamiento:**

1. Recupera la lista de marcas disponibles.
2. Muestra cada marca en una lista numerada.
3. No realiza acciones adicionales, simplemente sirve como referencia visual para el usuario.

---

### 22. `filtrarPorSupermercado()`

Muestra los supermercados registrados y permite aplicar un filtro por supermercado.

**Pasos:**

1. Obtiene todos los supermercados únicos que figuran en los productos.
2. Muestra una lista numerada de ellos.
3. Solicita al usuario seleccionar uno.
4. Filtra los productos que pertenecen a ese supermercado.
5. Muestra la lista filtrada por consola.

---

### 23. `ordenar()`

Permite ordenar los productos según un criterio elegido por el usuario (como nombre, precio o puntuación).

**Funcionamiento típico:**

1. Muestra un menú con distintas opciones de ordenación.
2. Captura la selección del usuario.
3. Aplica el criterio de orden correspondiente (al usar el controlador o algún comparador).
4. Muestra los productos ordenados.

---

### 24. `seleccionarProducto()`

Permite al usuario elegir un producto de entre una lista, no necesariamente limitada al stock.

**Funcionalidad:**

* Muestra todos los productos disponibles (no necesariamente con unidades en stock).
* Permite al usuario seleccionar uno a través de un índice.
* Devuelve el producto seleccionado o `null` si se cancela.

*(Este método es similar a `buscarProductoEnStock()`, pero aplicado a una lista general de productos).*

---

### 25. `verDetalleProducto()`

Muestra toda la información detallada de un producto específico.

**Información típica que puede mostrar:**

* Nombre
* Marca
* Categoría y subcategoría
* Precio
* Puntuación media
* Lista de supermercados
* Historial de precios

Este método proporciona una vista individual muy útil para comparar o decidir si un producto es conveniente.

---

### 26. `gestionarHistorialPrecios()`

Permite al usuario ver o modificar el historial de precios de un producto concreto.

**Posibles funcionalidades:**

* Mostrar los cambios de precio registrados a lo largo del tiempo.
* Añadir un nuevo precio al historial.
* Editar o eliminar precios anteriores.

Esta funcionalidad es útil para el control de ahorro y seguimiento del mercado en el tiempo.

---

#### **27. `gestionarPuntuaciones(Producto producto)`**

Este método permite al usuario visualizar las puntuaciones que ha recibido un producto por parte de los distintos usuarios, y también le da la opción de añadir o modificar su propia puntuación.

* Se imprimen todas las puntuaciones actuales del producto, si las hay.
* Luego, el sistema pregunta al usuario si quiere añadir o cambiar su puntuación.
* Si acepta, introduce una puntuación entre 1 y 5, la cual se registra mediante el controlador.
* Esta funcionalidad ayuda a valorar la calidad percibida de los productos dentro de la lista de la compra.

---

#### **28. `modificarSupermercados(Producto producto)`**

Este método gestiona los supermercados asociados a un producto.

* Muestra la lista actual de supermercados donde está disponible el producto.
* Ofrece tres opciones al usuario:

    1. Añadir un nuevo supermercado a la lista del producto.
    2. Eliminar un supermercado existente (por número en la lista).
    3. Volver al menú anterior.
* Esta información puede ser útil para saber en qué supermercados comprar cada producto y planificar mejor las compras.

---

#### **29. `anadirProducto()`**

Este método se encarga de añadir un nuevo producto al sistema. La operación es guiada paso a paso:

1. **Datos básicos**: nombre, marca y precio.
2. **Categoría**: se elige de una lista de categorías principales y opcionalmente una subcategoría.
3. **Código de barras y supermercado**: el usuario introduce estos datos manualmente.
4. **Creación del producto**: se invoca al controlador para crear el producto y se comprueba si la operación fue exitosa.
5. **Añadir al stock familiar**: si el producto se crea correctamente, se añade a la unidad familiar con cantidad inicial de 1 unidad.

Este flujo asegura que todos los productos se registren con la información esencial para el resto del sistema.

---

#### **30. `mostrarMenu()`**

Este método presenta el menú principal de gestión de la lista de la compra. Las opciones disponibles son:

1. Ver la lista de la compra actual, mostrando los productos optimizados.
2. Exportar dicha lista a un archivo `.txt`.
3. Volver al menú anterior.

El método emplea un bucle `do-while` que repite el menú hasta que el usuario elige salir (opción 0).

---

#### **31. `exportarListaTXT()`**

Este método exporta la lista de productos a un archivo de texto llamado `lista_optimizada.txt`.

* Primero, permite aplicar filtros:

    * Por supermercado.
    * Por orden de precio o nombre.
* Luego, utiliza el método `obtenerListaOptimizada()` para generar la lista final de productos a exportar.
* Si la lista no está vacía, se guarda en un archivo `.txt`, con columnas como nombre, marca, categoría, puntuación media, precio y supermercados asociados.
* Se informa al usuario si la operación fue exitosa o si ocurrió un error durante la escritura del archivo.

Ideal para planificar compras impresas o compartirlas fuera del sistema.

---

#### **32. `obtenerListaOptimizada(Map<Producto, Integer> productosMap, String supermercado, int orden)`**

Este método realiza el trabajo de optimizar la lista de productos antes de mostrarla o exportarla. Sigue los siguientes pasos:

1. **Filtrado**: solo se consideran productos cuya cantidad en stock sea 3 o menos.
2. **Agrupación**: los productos se agrupan por nombre.
3. **Selección del mejor producto por grupo**, siguiendo criterios en orden:

    * Menor cantidad en stock.
    * Mayor puntuación media.
    * Menor precio.
4. **Filtrado por supermercado**, si el usuario lo indicó.
5. **Ordenación final** según:

    * Precio (si se eligió).
    * Nombre (si se eligió).
    * O ningún orden si no se especifica.

El resultado es una lista optimizada para facilitar las compras prioritarias.

---

#### **33. `verLista()`**

Permite al usuario visualizar la lista optimizada directamente por consola, con filtros similares a los del método de exportación:

* Filtrar por supermercado específico.
* Ordenar por precio (menor a mayor).
* Ordenar por nombre (alfabéticamente).

Una vez aplicados los filtros, se invoca a `obtenerListaOptimizada()` y se muestran los productos en pantalla. Si no hay productos o si los filtros resultan en una lista vacía, se notifica al usuario.

Este método permite al usuario tomar decisiones informadas sobre qué productos comprar.

---

#### **34. `mostrarProductosTabla(List<Producto> listaFinal)`**

Este método es responsable de imprimir los productos en formato de tabla, normalmente después de filtrar y ordenar.

* Imprime una cabecera con los campos relevantes: nombre, marca, categoría, puntuación, precio y supermercados.
* Recorre la lista de productos y muestra los valores alineados en columnas.
* Es utilizado tanto para la visualización en consola (`verLista`) como para depuración o impresión organizada.

Mejora la presentación visual de los datos, haciéndolos más legibles.

---

#### **35. `verProductosListaCompra()`**

Este método es una versión simplificada de `verLista()` que muestra todos los productos en la lista de compra sin aplicar ningún filtro ni orden.

* Se obtiene el mapa de productos de la unidad familiar.
* Si hay productos, se llama a `mostrarProductosTabla()` para imprimirlos.
* Si no hay productos, se informa al usuario.

Ideal para obtener una vista rápida de todos los productos disponibles.

---

#### **36. `menuConfiguracion()`**

Este método muestra un menú de configuración para cambiar ajustes del sistema.

* Aunque no se muestra completamente en el fragmento proporcionado, normalmente este menú permite:

    * Cambiar la unidad familiar activa.
    * Gestionar cuentas de usuario.
    * Ajustar parámetros globales (si el sistema lo permite).

El menú se estructura de forma similar a los anteriores, usando un bucle `do-while` con opciones numeradas.

---

#### **37. cambiarNombreUsuario()**

Este método permite al usuario actual modificar su nombre de usuario.

1. Solicita al usuario que introduzca un nuevo nombre.
2. Llama al método `controlador.cambiarNombreUsuario()`, pasándole como parámetros el usuario actual y el nuevo nombre propuesto.
3. Si el controlador confirma el cambio (devuelve `true`), se notifica al usuario que el nombre fue cambiado correctamente.
4. Si el nuevo nombre ya está en uso, se avisa al usuario de que el nombre ya existe y se debe elegir otro.

---

#### **38. cambiarContrasena()**

Este método gestiona el cambio de contraseña del usuario actual mediante varias validaciones:

1. Se solicita al usuario la contraseña actual.
2. A continuación, se pide que introduzca la nueva contraseña y su confirmación.
3. Se compara la nueva contraseña con su confirmación. Si no coinciden, se informa del error y se cancela el proceso.
4. Si coinciden, se llama a `controlador.cambiarContrasena()` con los datos proporcionados.
5. Si el controlador valida correctamente la contraseña actual y realiza el cambio, se muestra un mensaje de éxito.
6. Si la contraseña actual no es correcta, se informa al usuario del error.

---

#### **39. gestionarUnidadFamiliarConfig()**

Este método presenta un pequeño menú para gestionar la configuración de la unidad familiar del usuario:

1. Muestra un menú con tres opciones:

    * Cambiar el nombre de la unidad familiar.
    * Abandonar la unidad familiar actual.
    * Volver al menú anterior.
2. Según la opción elegida:

    * Si se elige cambiar el nombre, se solicita uno nuevo y se llama al método `controlador.cambiarNombreUnidadFamiliar()`.
    * Si se elige abandonar la unidad familiar, se pide confirmación. Si el usuario confirma con "S", se llama al método `controlador.abandonarUnidadFamiliar()`, se borra la referencia a la unidad actual (`unidadActual = null`), y se redirige a la función `unirseOCrearUnidadFamiliar()`.
3. Este proceso se repite hasta que el usuario elige la opción de volver (0).

---

#### **40. buscarProducto()**

Este método facilita la localización de un producto dentro del sistema mediante distintos criterios:

1. Muestra un submenú con tres opciones de búsqueda:

    * Ver todos los productos.
    * Buscar por nombre.
    * Buscar por código de barras.
2. Según la opción seleccionada:

    * Opción 1: Se obtienen todos los productos disponibles.
    * Opción 2: Se solicita un nombre, se valida que no esté vacío y se realiza la búsqueda por nombre.
    * Opción 3: Se solicita el código de barras, que debe ser un número positivo. Si el formato no es válido, se cancela la búsqueda.
3. Si no se encuentra ningún producto, se informa al usuario.
4. Si se encuentra un solo producto, se devuelve directamente.
5. Si hay varios resultados, se enumeran y el usuario puede elegir uno introduciendo el número correspondiente. Si la selección no es válida, se cancela.

---

#### **41. leerEntero(String mensaje)**

Este método lee un número entero desde la entrada estándar:

1. Muestra el mensaje pasado como argumento.
2. Intenta leer la entrada del usuario y convertirla en un número entero (`int`).
3. Si la conversión falla (`NumberFormatException`), muestra un mensaje de error y vuelve a pedir el valor.
4. Repite este proceso hasta que se introduce un valor válido, que es retornado.

---

#### **42. leerDouble(String mensaje)**

Este método solicita al usuario un número decimal válido, generalmente utilizado para introducir precios:

1. Muestra el mensaje indicado.
2. Lee la entrada y reemplaza comas por puntos para permitir formatos con "," o "." como separador decimal.
3. Verifica mediante expresión regular que el formato ingresado sea correcto (solo números y un punto decimal).
4. Verifica que el número esté dentro del rango permitido (mayor o igual a 0 y menor o igual a 99999,99).
5. Si todo es correcto, redondea el número a 2 decimales y lo devuelve.
6. En caso de error, muestra un mensaje y vuelve a pedir el dato.

---

#### **43. leerLong(String mensaje)**

Este método permite leer un número entero largo (`long`):

1. Muestra el mensaje al usuario.
2. Lee el valor introducido y trata de convertirlo a tipo `long`.
3. Si el usuario introduce un valor no numérico, muestra un mensaje de error y vuelve a pedir el dato.
4. Repite este proceso hasta que el valor introducido sea válido, que es el que se devuelve.

---

#### **44. leerConfirmacion(String mensaje)**

Este método gestiona respuestas del tipo sí/no del usuario:

1. Muestra el mensaje con la coletilla "(S/N)" para pedir confirmación.
2. Convierte la entrada a mayúsculas y la valida.
3. Si el usuario introduce "S", devuelve `true` (confirmación afirmativa).
4. Si introduce "N", devuelve `false` (cancelación).
5. Si introduce cualquier otra cosa, se muestra un mensaje de error y se repite el proceso.
