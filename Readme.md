# MercaMáis 🛒
**Sistema de Gestión de Unidades Familiares**  
📅 Fecha: 05/06/2025

## 👥 Equipo de Desarrollo

- Daniel Rodríguez – Controlador principal (`Controlador.java`)
- Daniel Figueroa – Modelo y base de datos (`Model.java`, `Conexion.java`)
- Saray Ruzo – Vista (`VistaConsola.java`)
- Karly Albarrán – Validaciones y filtros (`Controller2.java`)

---

## 📋 Índice

- [Descripción del Proyecto](#descripción-del-proyecto)
- [Documentación de Clases](#documentación-de-clases)
- [Funcionalidades Principales](#funcionalidades-principales)

---

## 🎯 Descripción del Proyecto

**MercaMáis** es un sistema colaborativo de gestión de compras familiares que permite:

- 👨‍👩‍👧‍👦 Crear y gestionar unidades familiares compartidas
- 🛒 Organizar listas de compra inteligentes
- 📊 Comparar precios y productos entre supermercados
- ⭐ Puntuar y evaluar productos
- 📱 Interactuar mediante una interfaz de consola intuitiva

El sistema utiliza arquitectura **MVC** con base de datos **PostgreSQL**, conectada vía **JDBC** y desplegada en **Supabase**.

---

## 📚 Documentación de Clases

---

## `App.java`
**Descripción:** Clase principal del sistema. Punto de entrada de la aplicación.

**Responsabilidad:**
- Inicializa el entorno.
- Llama a `Controlador` o `ModelApp` para iniciar el flujo de ejecución.
- Muestra mensajes iniciales y menú principal.

---

## `Conexion.java`
**Descripción:** Clase encargada de gestionar la conexión con la base de datos.

**Responsabilidad:**
- Establecer, mantener y cerrar conexiones SQL.
- Proporcionar una única instancia de conexión (patrón Singleton).
- Manejo de excepciones de base de datos.

---

## `Controlador.java`
**Descripción:** Controlador principal que conecta la vista con el modelo.

**Responsabilidad:**
- Registro y login de usuarios.
- Gestión de unidades familiares.
- Gestión de productos y stock.
- Orquestación de interacciones entre clases modelo y vista.

---

## `Controller2.java`
**Descripción:** Controlador auxiliar para tareas de validación y filtrado.

**Responsabilidad:**
- Validaciones de entrada del usuario.
- Filtrado y ordenamiento de productos.
- Lógica de búsqueda avanzada.

---

## `Lista_UnidadFamiliar.java`
**Descripción:** Representa una unidad familiar con stock compartido.

**Atributos:**
- `String nombre`: Nombre de la unidad.
- `String codigoUnico`: Identificador único.
- `List<Usuario> miembros`: Lista de miembros.
- `Map<Producto, Integer> stock`: Productos compartidos con sus cantidades.

---

## `ListaCompra.java`
**Descripción:** Representa una lista de compras asociada a una unidad familiar.

**Atributos:**
- `List<Producto> productos`: Productos en la lista.
- `String nombreLista`: Nombre identificador de la lista.
- `boolean completada`: Estado de la lista (completa o no).

**Responsabilidad:**
- Agregado y eliminación de productos.
- Marcar lista como completada o en curso.

---

## `Model.java`
**Descripción:** Capa de acceso a datos y lógica persistente.

**Responsabilidad:**
- Operaciones CRUD sobre entidades.
- Transacciones SQL seguras.
- Consultas y actualizaciones de base de datos.

---

## `ModelApp.java`
**Descripción:** Variante o extensión de `Model`, usada por la aplicación.

**Responsabilidad:**
- Métodos adicionales para inicialización o lógica específica de la app.
- Posible conexión directa a funcionalidades de `VistaConsola`.

---

## `Producto.java`
**Descripción:** Clase que representa un producto disponible en el sistema.

**Atributos:**
- `String codigoBarras`
- `String nombre`
- `String marca`
- `double precio`
- `String categoria`
- `String supermercado`
- `List<Double> historialPrecios`
- `List<Integer> puntuaciones`

**Responsabilidad:**
- Manejo de precios y puntuaciones.
- Comparación y visualización de productos.

---

## `Usuario.java`
**Descripción:** Entidad que representa un usuario del sistema.

**Atributos:**
- `String nombre`
- `String email` (único)
- `String contraseña`
- `Lista_UnidadFamiliar unidadFamiliar`

**Responsabilidad:**
- Datos personales y de acceso.
- Asociación a una unidad familiar.

---

## `VistaConsola.java`
**Descripción:** Interfaz de usuario en consola (CLI).

**Responsabilidad:**
- Mostrar menús y capturar entradas del usuario.
- Invocar métodos de `Controlador`.
- Presentar resultados de operaciones (productos, usuarios, stock, etc.).

---
## 🔑 Funcionalidades Clave

- ✅ Registro y autenticación de usuarios
- 🧾 Gestión de perfiles (cambio de nombre y contraseña)
- 🏘️ Asociación a unidades familiares

---
## 🚀 Cómo Funciona

1. **👤 Registro**  
   → Nombre + Email + Contraseña segura  
   → Validación automática


2. **👨👩👧👦 Crear Grupo**  
   → Nombre familia + Código único  
   → Invitar miembros


3. **🛒 Añadir Productos**  
   → Buscar por nombre/marca  
   → Definir precio y cantidad


4. **📄 Lista Automática**  
   → Genera TXT/PDF con:
    - Productos necesarios
    - Precios por supermercado
    - Cantidades óptimas

Ejemplo código:
```java
// 1. Registrar usuario
Usuario user = controlador.registrarUsuario("Ana", "ana@email.com", "Pass1234");

// 2. Crear unidad familiar
Lista_UnidadFamiliar familia = controlador.crearUnidadFamiliar(user, "Los García", "GARC2023");

// 3. Añadir producto
controlador.anadirProductoStock(familia, new Producto(...), 3);

// 4. Exportar lista
familia.exportarATxt("lista_compras.txt");