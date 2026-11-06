<div align="center">

<img src="src/main/webapp/assets/icons/Vectorheart.png" alt="Logo de Vectorheart" width="110" />

# VECTORHEART // ART ARCHIVE

### Tienda web de arte digital con estética *cyberpunk*, desarrollada en Java

Aplicación web **Jakarta EE (Servlets + JSP)** con base de datos **PostgreSQL**: catálogo híbrido (Unsplash + obras propias), carrito y favoritos por usuario, pago con facturación, roles de usuario y panel logístico para gestionar el inventario.

<br />

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Jakarta EE](https://img.shields.io/badge/Jakarta_EE-Servlets_%26_JSP-F9A03C?style=flat-square)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![Tomcat](https://img.shields.io/badge/Apache_Tomcat-F8DC75?style=flat-square&logo=apachetomcat&logoColor=black)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=javascript&logoColor=black)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=css3&logoColor=white)
![Versión](https://img.shields.io/badge/versión-final-D4FF00?style=flat-square&labelColor=0A0A0A)

</div>

---

## 📑 Tabla de contenido

- [Sobre el proyecto](#-sobre-el-proyecto)
- [Características](#-características)
- [Roles de usuario](#-roles-de-usuario)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Base de datos](#-base-de-datos)
- [Instalación y ejecución](#-instalación-y-ejecución)
- [Configuración de la API de Unsplash](#-configuración-de-la-api-de-unsplash)
- [Endpoints](#-endpoints)
- [Licencia](#-licencia)

---

## 📖 Sobre el proyecto

**Vectorheart** es una tienda de arte digital con una identidad visual inspirada en interfaces tácticas de ciencia ficción: tipografías monoespaciadas, cursor personalizado con coordenadas en vivo, efectos *glitch* y una paleta de neón sobre fondo casi negro.

Esta versión final evoluciona el sitio estático original hacia una **aplicación web completa con backend en Java**, donde los usuarios, el carrito, los favoritos, las compras y el inventario se guardan en una base de datos **PostgreSQL**.

### 🎨 Paleta y tipografía

| Elemento | Valor |
| --- | --- |
| Color primario | `#D4FF00` (neón chartreuse) |
| Color secundario | `#5D00FF` (púrpura profundo) |
| Alerta / administrador | `#FF003C` |
| Fondo | `#0A0A0A` |
| Titulares | *Archivo Black* / *Bebas Neue* |
| Datos y HUD | *Space Mono* |

---

## ✨ Características

### 🛍️ Tienda
- **Catálogo híbrido:** combina las obras de la **API de Unsplash** con las obras propias guardadas en PostgreSQL (solo las que están en estado `ACTIVO`).
- **Buscador** con sugerencias e historial de búsquedas recientes.
- **Filtros** por categoría, color, orientación y precio máximo, con botón *Cargar más*.
- **Modal de inspección** con datos de la obra, etiquetas clicables y **modo 3D** (figura geométrica giratoria).
- **Carrito y favoritos por usuario**, guardados en la base de datos y sincronizados al iniciar sesión.

### 💳 Compra y facturación
- Pantalla de **pago seguro** (`checkout.jsp`) con resumen de la transacción.
- Métodos de pago: **tarjeta de crédito** y **PayPal / cripto**, con validación de la tarjeta.
- Registro de cada compra como **factura** (`tb_factura`) y vaciado automático del carrito tras el pago.
- **Historial de adquisiciones** en el perfil de cada usuario.

### 👤 Usuarios y seguridad
- **Registro** con validación de **cédula ecuatoriana** (módulo 10), detección automática de la provincia, contraseña fuerte y foto de perfil.
- **Inicio y cierre de sesión** con manejo de sesión (`HttpSession`).
- Las acciones de carrito, favoritos y pago exigen sesión iniciada.
- El panel de inventario y el panel de cada perfil se muestran según el rol del usuario.

### 🗂️ Gestión de inventario
- **Carga de nuevas obras** con imagen (`.jpg` / `.png`, hasta 10 MB), título, artista, precio, categoría, formato, color base y nivel de amenaza (estrellas).
- **Borrado lógico:** ocultar o reactivar una obra sin eliminarla de la base de datos.

### 🖥️ Interfaz
- Cursor cibernético, menú lateral, paneles deslizables y cierre con `Esc`.
- Carrusel hero, formulario de contacto (vía Formspree) y página 404 personalizada.

---

## 🔐 Roles de usuario

El sistema define tres perfiles (`id_per` en la tabla de usuarios). Cada uno ve un panel distinto en `perfil.jsp`:

| Rol | Perfil | Qué puede hacer |
| :---: | --- | --- |
| `1` | **Administrador supremo** | Ver ingresos totales y número de facturas, y consultar el registro de todos los usuarios |
| `2` | **Gestor logístico** | Acceder al panel de inventario: cargar obras y ocultarlas o activarlas |
| `3` | **Cliente estándar** | Comprar, guardar favoritos y consultar su historial de compras |

> Los usuarios nuevos se registran con el rol **cliente (3)**.

---

## 🧱 Arquitectura

El backend sigue una separación en capas dentro del paquete `com.productos`:

```text
┌───────────────────────────┐
│  Navegador (JSP + JS)     │  index.jsp, checkout.jsp, perfil.jsp ...
└─────────────┬─────────────┘
              │ fetch / formularios
┌─────────────▼─────────────┐
│  controlador / negocio    │  Servlets (CatalogoServlet, CarritoServlet,
│  (Servlets)               │  FavoritosServlet, FacturacionServlet, ...)
└─────────────┬─────────────┘
              │
┌─────────────▼─────────────┐
│  negocio                  │  OperacionesCliente, OperacionesProducto,
│  (lógica y consultas)     │  OperacionesFactura, Usuario
└─────────────┬─────────────┘
              │ JDBC
┌─────────────▼─────────────┐
│  datos                    │  Conexion, Producto, Factura
└─────────────┬─────────────┘
              │
        PostgreSQL (bd_productos)
```

---

## 🛠️ Tecnologías

| Área | Herramienta |
| --- | --- |
| Lenguaje backend | Java |
| Servidor web | Apache Tomcat (Jakarta Servlet 6) |
| Vistas | JSP, HTML5, CSS3 |
| Lógica de cliente | JavaScript (ES6+), `fetch` / `async-await` |
| Base de datos | PostgreSQL (driver JDBC `postgresql-42.7.3`) |
| Gestión del proyecto | Maven (con Maven Wrapper) |
| API externa | [Unsplash API](https://unsplash.com/developers) |
| Formulario de contacto | Formspree |

---

## 📂 Estructura del proyecto

```text
Vectorheart-Web/
├── pom.xml                                   # Configuración de Maven (empaquetado WAR)
├── mvnw / mvnw.cmd                           # Maven Wrapper
└── src/main/
    ├── java/com/productos/
    │   ├── controlador/
    │   │   ├── CatalogoServlet.java          # GET  /api/catalogo (obras ACTIVO en JSON)
    │   │   ├── FacturacionServlet.java       # POST /FacturacionServlet (registrar compra)
    │   │   ├── InventarioServlet.java        # POST /InventarioServlet (cargar nueva obra)
    │   │   └── EstadoObraServlet.java        # POST /EstadoObraServlet (ocultar / activar)
    │   ├── negocio/
    │   │   ├── CarritoServlet.java           # POST /CarritoServlet (add / remove / clear)
    │   │   ├── FavoritosServlet.java         # POST /FavoritosServlet (add / remove)
    │   │   ├── OperacionesCliente.java       # Carrito, favoritos, historial y usuarios
    │   │   ├── OperacionesProducto.java      # Inventario de obras
    │   │   ├── OperacionesFactura.java       # Facturas y estadísticas
    │   │   └── Usuario.java                  # Registro y verificación de login
    │   └── datos/
    │       ├── Conexion.java                 # Conexión JDBC a PostgreSQL
    │       ├── Producto.java
    │       └── Factura.java
    └── webapp/
        ├── index.jsp                         # Catálogo principal
        ├── login.jsp / registro.jsp / respuesta.jsp / logout.jsp
        ├── perfil.jsp                        # Panel según el rol
        ├── inventario.jsp                    # Panel logístico (gestor / admin)
        ├── checkout.jsp                      # Pago y facturación
        ├── filosofia.jsp / servicios.jsp / contacto.jsp / 404.jsp
        ├── css/                              # style.css, auth.css
        ├── js/                               # app.js, auth.js
        ├── data/productos.json
        ├── assets/                           # Logo e imágenes
        └── WEB-INF/
            ├── web.xml
            └── lib/postgresql-42.7.3.jar
```

---

## 🗄️ Base de datos

El proyecto se conecta a una base **PostgreSQL** llamada `bd_productos`. Estas son las tablas que usa el código:

| Tabla | Uso |
| --- | --- |
| `tb_usuario` | Usuarios: nombre, cédula, correo, clave, foto, perfil (`id_per`) y estado |
| `tb_producto` | Obras: nombre, artista, precio, categoría, color base, formato, amenaza, ruta de imagen y `estado_obra` (`ACTIVO` / `OCULTO`) |
| `tb_carrito` | Carrito por usuario (producto, título, imagen y precio unitario) |
| `tb_favorito` | Favoritos por usuario |
| `tb_factura` | Facturas: usuario, método de pago, total pagado y fecha |

> ℹ️ El repositorio no incluye un script SQL para crear estas tablas. Créalas en tu base `bd_productos` antes de ejecutar el proyecto, usando los nombres de columna que aparecen en las clases `Operaciones*` y `Usuario`.

---

## 🚀 Instalación y ejecución

### Requisitos
- **JDK 17** o superior
- **Apache Tomcat 10.1** o superior (Jakarta EE)
- **PostgreSQL** en ejecución
- Maven (opcional, ya que el proyecto incluye `mvnw`)

### Pasos

**1. Clona el repositorio**

```bash
git clone https://github.com/xErnyx/Vectorheart-Web.git
cd Vectorheart-Web
```

**2. Prepara la base de datos**

Crea la base `bd_productos` en PostgreSQL y sus tablas (ver [Base de datos](#-base-de-datos)).

**3. Configura la conexión**

Edita las credenciales en `src/main/java/com/productos/datos/Conexion.java`:

```java
this.user   = "postgres";
this.pwd    = "tu_contraseña";
this.cadena = "jdbc:postgresql://localhost:5432/bd_productos";
```

**4. Compila el proyecto**

```bash
./mvnw clean package        # Linux / macOS
mvnw.cmd clean package      # Windows
```

Se genera el archivo `target/vectorheartweb-1.0-SNAPSHOT.war`.

**5. Despliega en Tomcat**

Copia el `.war` en la carpeta `webapps/` de Tomcat (o despliégalo desde tu IDE) e inicia el servidor. Luego abre:

```text
http://localhost:8080/vectorheartweb-1.0-SNAPSHOT/
```

---

## 🔑 Configuración de la API de Unsplash

El catálogo externo necesita una *Access Key* de Unsplash:

1. Crea una aplicación en [unsplash.com/developers](https://unsplash.com/developers).
2. Copia tu **Access Key**.
3. Reemplaza el valor de `UNSPLASH_ACCESS_KEY` en `src/main/webapp/js/app.js`.

---

## 🔌 Endpoints

| Método | Ruta | Descripción | Acceso |
| :---: | --- | --- | --- |
| `GET` | `/api/catalogo` | Devuelve en JSON las obras con estado `ACTIVO` | Público |
| `POST` | `/CarritoServlet` | Acciones `add`, `remove` y `clear` del carrito | Sesión iniciada |
| `POST` | `/FavoritosServlet` | Acciones `add` y `remove` de favoritos | Sesión iniciada |
| `POST` | `/FacturacionServlet` | Registra la compra y vacía el carrito | Sesión iniciada |
| `POST` | `/InventarioServlet` | Carga una nueva obra con su imagen | Sesión iniciada (la interfaz limita el panel a gestor / administrador) |
| `POST` | `/EstadoObraServlet` | Oculta o reactiva una obra | Gestor / Administrador |

---

## 📄 Licencia

Este repositorio todavía no define una licencia. Agrega un archivo `LICENSE` si quieres indicar los términos de uso.

<div align="center">

<sub>© 2026 VECTORHEART · Arte digital en modo táctico</sub>

</div>
