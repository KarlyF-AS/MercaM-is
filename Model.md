

### **Variables**
1. **`BaseDatosException`**:  
   Es una clase interna que extiende `RuntimeException`. Se utiliza para encapsular errores relacionados con la base de datos, proporcionando un mensaje y la causa del error.

---

### **Funciones**

#### **1. `registrarUsuario`**
Registra un nuevo usuario en la base de datos.
- **Parámetros**: `nombre`, `email`, `password`.
- **Retorno**: Un objeto `Usuario` si el registro es exitoso, o `null` si el email ya existe o hay un error.
- **Detalles**: Usa una consulta SQL para insertar el usuario en la tabla `usuarios`. Si el email ya está registrado, captura la excepción SQL y devuelve `null`.

---


#### **2. `ejecutar`**
Ejecuta una consulta preparada con los datos de un usuario.
- **Parámetros**: `stmt` (consulta preparada), `u` (objeto `Usuario`).
- **Retorno**: Un objeto `ResultSet` con los resultados de la consulta..
- **Detalles**: Asigna los valores del usuario a los parámetros de la consulta y la ejecuta.

---


#### **3. `validarLogin`**
Valida las credenciales de un usuario.
- **Parámetros**: `email`, `password`.
- **Retorno**: Un objeto `Usuario` si las credenciales son correctas, o `null` si no coinciden.
- **Detalles**: Consulta la tabla `usuarios` para verificar si el email y la contraseña coinciden.

---

#### **4. `crearProducto`**
Crea un nuevo producto en la base de datos.
- **Parámetros**: `nombre`, `marca`, `precio`, `categoria`, `supermercado`, `codigoBarras`.
- **Retorno**: Un objeto `Producto` si la inserción es exitosa, o `null` si hay un conflicto (por ejemplo, código de barras duplicado).
- **Detalles**: Usa una consulta SQL para insertar el producto en la tabla `producto`. Maneja conflictos con triggers y claves únicas.

---

#### **5. `buscarProductoPorCodigoBarras`**
Busca productos por su código de barras.
- **Parámetros**: `codigoBarras`.
- **Retorno**: Una lista de objetos `Producto` que coinciden con el código de barras.
- **Detalles**: Consulta la tabla `producto` y construye una lista con los resultados.

---

#### **6. `existeEmail`**
Verifica si un email ya está registrado.
- **Parámetros**: `email`.
- **Retorno**: `true` si el email existe, `false` en caso contrario.
- **Detalles**: Usa una consulta SQL para contar las filas en la tabla `usuarios` con el email especificado.

---

#### **7. `crearUnidadFamiliar`**
Crea una nueva unidad familiar y añade al usuario como miembro.
- **Parámetros**: `usuario`, `nombreUnidadFamiliar`, `codigo`.
- **Retorno**: Un objeto `Lista_UnidadFamiliar` si la creación es exitosa, o `null` si hay un error.
- **Detalles**: Inserta datos en las tablas `listas` y `decision`.

---

#### **8. `obtenerUnidadFamiliar`**
Obtiene la unidad familiar a la que pertenece un usuario.
- **Parámetros**: `usuario`.
- **Retorno**: Un objeto `Lista_UnidadFamiliar` si el usuario pertenece a una unidad, o `null` si no pertenece.
- **Detalles**: Consulta las tablas `listas` y `decision`.

---

#### **9. `unirseAUnidadFamiliar`**
Permite a un usuario unirse a una unidad familiar existente.
- **Parámetros**: `usuario`, `codigoUnidadFamiliar`.
- **Retorno**: Un objeto `Lista_UnidadFamiliar` si la operación es exitosa, o `null` si hay un error.
- **Detalles**: Inserta datos en la tabla `decision`.

---

#### **10. `obtenerTodosProductos`**
Obtiene todos los productos disponibles en el sistema.
- **Parámetros**: Ninguno.
- **Retorno**: Una lista de objetos `Producto`.
- **Detalles**: Consulta la tabla `producto` y construye una lista con los resultados.

---


#### **11. `construirProducto`**
Construye un objeto `Producto` a partir de un `ResultSet`.
- **Parámetros**: `rs` (objeto `ResultSet`).
- **Retorno**: Un objeto `Producto`.
- **Detalles**: Extrae los datos del `ResultSet` y los asigna a un nuevo objeto `Producto`.

---

#### **12. `obtenerCategorias`**
Obtiene todas las categorías de productos disponibles.
- **Parámetros**: Ninguno.
- **Retorno**: Una lista de categorías únicas.
- **Detalles**: Consulta la tabla `producto` para extraer las categorías.

---

#### **13. `obtenerProductosPorCategoria`**
Obtiene productos que pertenecen a una categoría específica.
- **Parámetros**: `categoria`.
- **Retorno**: Una lista de objetos `Producto`.
- **Detalles**: Filtra los productos por la categoría especificada.

---

#### **14. `obtenerProductosPorSubcategoria`**
Obtiene productos que pertenecen a una subcategoría específica.
- **Parámetros**: `subcategoria`.
- **Retorno**: Una lista de objetos `Producto`.
- **Detalles**: Filtra los productos por subcategoría y calcula la puntuación media.

---

#### **15. `obtenerMarcas`**
Obtiene todas las marcas de productos disponibles.
- **Parámetros**: Ninguno.
- **Retorno**: Una lista de marcas únicas.
- **Detalles**: Consulta la tabla `producto` para extraer las marcas.

---

#### **16. `obtenerProductosPorMarca`**
Obtiene productos que pertenecen a una marca específica.
- **Parámetros**: `marca`.
- **Retorno**: Una lista de objetos `Producto`.
- **Detalles**: Filtra los productos por la marca especificada.

---

#### **17. `recogerTodosProductos`**
Obtiene todos los productos del sistema.
- **Parámetros**: Ninguno.
- **Retorno**: Una lista de objetos `Producto`.
- **Detalles**: Similar a `obtenerTodosProductos`.

---

#### **18. `actualizarPrecioProducto`**
Actualiza el precio de un producto.
- **Parámetros**: `producto`, `nuevoPrecio`.
- **Retorno**: Ninguno.
- **Detalles**: Actualiza el precio en la tabla `producto`.

---

#### **19. `anadirPuntuacionProducto`**
Añade una puntuación a un producto por parte de un usuario.
- **Parámetros**: `producto`, `usuario`, `puntuacion`.
- **Retorno**: El producto modificado, o `null` si hay un error.
- **Detalles**: Inserta o actualiza datos en la tabla `puntua`.

---

#### **20. `obtenerProductoPorNombre`**
Obtiene productos por su nombre.
- **Parámetros**:  `nombre` (nombre del producto).
- **Retorno**: Una lista de objetos `Producto`.
- **Detalles**: Filtra los productos por nombre en la tabla `producto`.

---

#### **21. `anadirSupermercadoProducto`**
Añade un supermercado a un producto existente.
- **Parámetros**: `producto`, `supermercado`.
- **Retorno**: El producto modificado, o `null` si hay un error.
- **Detalles**: Inserta datos en la tabla `producto`.

---

#### **22. `eliminarSupermercadoProducto`**
Elimina un supermercado de un producto.
- **Parámetros**: `producto`, `supermercado`.
- **Retorno**: El producto modificado, o `null` si hay un error.
- **Detalles**: Elimina datos de la tabla `producto`.

---

#### **21. `eliminarSupermercadoProducto`**
Obtiene los productos de una unidad familiar con sus cantidades.
- **Parámetros**: `producto`, `supermercado`.
- **Retorno**: El objeto `Producto` eliminado, o `null` si no existía.
- **Detalles**: Elimina el producto de la tabla `producto` filtrando por nombre, marca y supermercado.

---

#### **22. `obtenerProductosUnidadFamiliar`**
Obtiene los productos de una unidad familiar con sus cantidades.
- **Parámetros**: `unidadFamiliar`.
- **Retorno**: Un mapa de `Producto` a `Integer` (cantidad).
- **Detalles**: Consulta las tablas `producto` y `contiene`.

---

#### **23. `stock`**
Obtiene el stock de productos de una unidad familiar.
- **Parámetros**: `unidadFamiliar`.
- **Retorno**: Un mapa de `Integer` (cantidad) a `Producto`.
- **Detalles**: Similar a `obtenerProductosUnidadFamiliar`.

---

#### **24. `cambiarNombreUsuario`**
Cambia el nombre de un usuario.
- **Parámetros**: `usuario`, `nuevoNombre`.
- **Retorno**: `true` si la operación es exitosa, `false` en caso contrario.
- **Detalles**: Actualiza el nombre en la tabla `usuarios`.

---

#### **25. `cambiarContrasena`**
Cambia la contraseña de un usuario.
- **Parámetros**: `usuario`, `actual`, `nueva`.
- **Retorno**: `true` si la operación es exitosa, `false` en caso contrario.
- **Detalles**: Verifica la contraseña actual y actualiza la nueva en la tabla `usuarios`.

---

#### **26. `cambiarNombreUnidadFamiliar`**
Cambia el nombre de una unidad familiar.
- **Parámetros**: `unidadFamiliar`, `nuevoNombre`.
- **Retorno**: `true` si la operación es exitosa, `false` en caso contrario.
- **Detalles**: Actualiza el nombre en la tabla `listas`.

---

#### **27. `abandonarUnidadFamiliar`**
Permite a un usuario abandonar una unidad familiar.
- **Parámetros**: `usuario`, `unidadFamiliar`.
- **Retorno**: `true` si la operación es exitosa, `false` en caso contrario.
- **Detalles**: Elimina la relación en la tabla `decision`.

---

#### **28. `actualizarPrecioSupermercado`**
Actualiza el precio de un producto en un supermercado específico.
- **Parámetros**: `producto`, `nuevoPrecio`.
- **Retorno**:  `true` si la operación es exitosa, `false` en caso contrario.
- **Detalles**:  Actualiza el precio del producto en la tabla `producto` filtrando por código de barras.

---
#### **29. `modificarCantidadProducto`**
Modifica la cantidad de un producto en una unidad familiar.
- **Parámetros**: `unidad`, `producto`, `cantidad`.
- **Retorno**: La nueva cantidad, o `-1` si hay un error.
- **Detalles**: Actualiza la cantidad en la tabla `contiene`.

---

#### **30. `filtrarPorSupermercado`**
Filtra productos por supermercado.
- **Parámetros**: `supermercado`.
- **Retorno**: Una lista de objetos `Producto`.
- **Detalles**: Filtra los productos por el supermercado especificado.

---

#### **31. `obtenerListaDeProductosConStock`**
Obtiene los productos con stock de una unidad familiar.
- **Parámetros**: `lista`.
- **Retorno**: Un objeto `Lista_UnidadFamiliar` con los productos y sus cantidades.
- **Detalles**: Filtra los productos con cantidad mayor a 0.

---

#### **32. `añadirProductoStock`**
Añade un producto al stock de una unidad familiar.
- **Parámetros**: `unidad`, `producto`, `cantidad`.
- **Retorno**: Ninguno.
- **Detalles**: Usa `INSERT ON CONFLICT` para sumar la cantidad si ya existe.

---

#### **33. `eliminarProductoStock`**
Elimina un producto del stock de una unidad familiar.
- **Parámetros**: `unidad`, `producto`.
- **Retorno**: La cantidad que quedaba antes de eliminar, `0` si no existía, `-1` si hay un error.
- **Detalles**: Elimina datos de la tabla `contiene`.

---

#### **34. `actualizarCantidadStock`**
Actualiza la cantidad de un producto en el stock de una unidad familiar.
- **Parámetros**: `unidad`, `producto`, `cantidad`.
- **Retorno**: La nueva cantidad, o `-1` si hay un error.
- **Detalles**: Actualiza la cantidad en la tabla `contiene`.

---

#### **35. `obtenerTodosSupermercados`**
Obtiene todos los supermercados disponibles.
- **Parámetros**: Ninguno.
- **Retorno**: Una lista de nombres de supermercados.
- **Detalles**: Consulta la tabla `producto`.

---

#### **36. `obtenerPuntuacionMediaProducto`**
Obtiene la puntuación media de un producto.
- **Parámetros**: `nombreProducto`, `marcaProducto`.
- **Retorno**: La puntuación media, o `0.0` si no hay puntuaciones.
- **Detalles**: Calcula la media en la tabla `puntua`.

---

#### **37. `getSupermercados`**
Obtiene los supermercados donde se vende un producto.
- **Parámetros**: `producto`.
- **Retorno**: Una lista de nombres de supermercados.
- **Detalles**: Filtra por nombre y marca en la tabla `producto`.

---

#### **38. `getPuntuaciones`**
Obtiene las puntuaciones individuales de un producto.
- **Parámetros**: `p`.
- **Retorno**: Un mapa de `Usuario` a `Integer` (puntuación).
- **Detalles**: Consulta las tablas `puntua` y `usuarios`.

---

#### **39. `getHistorialPrecios`**
Obtiene el historial de precios de un producto.
- **Parámetros**: `nombre`, `marca`.
- **Retorno**: Una lista de precios históricos ordenados por fecha.
- **Detalles**: Consulta la tabla `historial_precios`.

---

#### **40. `obtenerCantidadStock`**
Obtiene la cantidad de un producto en el stock de una unidad familiar.
- **Parámetros**: `unidad`, `producto`.
- **Retorno**: La cantidad en stock, `0` si no existe, `-1` si hay un error.
- **Detalles**: Consulta la tabla `contiene`.

