# 🛒 MercaMáis

## Sistema de Gestión de Unidades Familiares

### 👥 Equipo de Desarrollo

* **Daniel Rodríguez** – Controller
* **Daniel Figueroa** – Model
* **Saray Ruzo** – View
* **Karly Albarrán** – Validaciones y filtros

📅 **Fecha de entrega:** 05/06/2025

---

## 📋 Índice

1. [Descripción del Proyecto](#-descripción-del-proyecto)
2. [Funcionalidades Actuales](#-funcionalidades-actuales)
3. [Estructura del Código](#-estructura-del-código)
4. [Colecciones Utilizadas](#-colecciones-utilizadas)
5. [Gestión de Archivos](#-gestión-de-archivos)
6. [Instalación y Ejecución](#-instalación-y-ejecución)
7. [División del Trabajo](#-división-del-trabajo)
8. [Mejoras Futuras](#-mejoras-futuras)

---

## 🎯 Descripción del Proyecto

**MercaMáis** es una aplicación de consola en Java diseñada para facilitar la gestión compartida de la lista de la compra dentro de una unidad familiar. El sistema permite que varios usuarios colaboren en tiempo real (en consola) con funcionalidades como añadir productos, organizarlos por prioridad o supermercado, marcarlos como comprados y exportar la lista a un archivo `.txt`.

El enfoque principal fue aplicar conceptos fundamentales del primer curso de DAM: clases, colecciones, control de errores, validación de entrada, separación en capas y exportación de datos.

---

## ✅ Funcionalidades Actuales

* Registro y autenticación de usuarios.
* Asociación a una unidad familiar.
* Añadir, eliminar, editar y buscar productos.
* Marcar productos como comprados.
* Filtrar productos por estado (pendiente/comprado), supermercado o prioridad.
* Exportación de la lista de productos a archivos de texto.
* Validaciones consistentes en todas las entradas del usuario.
* Navegación por menús clara e intuitiva desde consola.

---

## 📦 Estructura del Código

El programa está dividido en varias capas:

* `Modelo`: clases como `Producto`, `Usuario`, `UnidadFamiliar`.
* `Vista`: `VistaConsola.java`, gestiona menús e interacción con el usuario.
* `Controladores`: coordinan la lógica del programa.
* `Utilidades`: funciones de validación, ordenación y filtrado.

---

## 📚 Colecciones Utilizadas

Se utilizan `ArrayList` y `Map` para almacenar:

* Usuarios y sus relaciones con unidades familiares.
* Productos dentro de cada unidad.
* Filtrados temporales en memoria.

No se ha utilizado base de datos en esta versión.

---

## 🗃️ Gestión de Archivos

Se incluye la funcionalidad para exportar la lista de productos (comprados o pendientes) en formato `.txt`, ideal para imprimir o consultar fuera del programa.

---

## 👥 División del Trabajo

* **Daniel Rodríguez**: Controlador general del flujo del programa.
* **Daniel Figueroa**: Desarrollo del modelo y estructuras de datos.
* **Saray Ruzo**: Interfaz en consola (vista) y menús.
* **Karly Albarrán**: Validaciones, filtros y control de errores.

