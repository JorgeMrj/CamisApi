# 🏛️ Arquitectura Híbrida - CamisAPI

## 📋 Resumen

Esta API utiliza una **arquitectura híbrida** que combina **MongoDB** y **PostgreSQL** para aprovechar las fortalezas de ambas bases de datos:

- **MongoDB**: Para datos dinámicos y flexibles (catálogo de productos)
- **PostgreSQL**: Para datos transaccionales, usuarios y pedidos (carritos, pedidos, autenticación)

---

## 📦 Modelos en MongoDB

### 👕 Producto (`Producto.java`)

**Colección**: `productos`  
**Ubicación**: `src/main/java/srangeldev/camisapi/rest/productos/models/Producto.java`

Cada camiseta es **única**. No existe concepto de "stock" ni unidades múltiples.

| Campo           | Tipo              | Descripción                           |
|-----------------|-------------------|---------------------------------------|
| `id`            | `String`          | ObjectId de MongoDB                   |
| `nombre`        | `String`          | Nombre del producto                   |
| `equipo`        | `String`          | Nombre del club/selección             |
| `talla`         | `String`          | Talla (S, M, L, XL, XXL)              |
| `descripcion`   | `String`          | Descripción detallada                 |
| `precio`        | `Double`          | Precio en euros                       |
| `imageUrl`      | `String`          | URL de la imagen                      |
| `estado`        | `EstadoProducto`  | **DISPONIBLE**, **RESERVADO**, **VENDIDO** |
| `fechaCreacion` | `LocalDateTime`   | Fecha de alta en catálogo             |

#### 🔑 Estados del Producto (`EstadoProducto.java`)

```java
public enum EstadoProducto {
    DISPONIBLE,  // Puede ser añadido al carrito
    RESERVADO,   // Está en un carrito (temporal)
    VENDIDO      // Comprado, no disponible
}
```

**Flujo de estados**:
1. **DISPONIBLE** → Usuario añade al carrito → **RESERVADO**
2. **RESERVADO** → Usuario elimina o expira → **DISPONIBLE**
3. **RESERVADO** → Usuario completa compra → **VENDIDO**

---

## 💾 Modelos en PostgreSQL

### 🧍‍♂️ User (`User.java`)

**Tabla**: `users`  
**Ubicación**: `src/main/java/srangeldev/camisapi/rest/users/models/User.java`

Representa a cada usuario/cliente del sistema.

| Campo       | Tipo              | Descripción                               |
|-------------|-------------------|-------------------------------------------|
| `id`        | `Long`            | Primary Key autogenerada                  |
| `nombre`    | `String`          | Nombre completo del usuario               |
| `username`  | `String`          | Nombre de usuario único                   |
| `password`  | `String`          | Contraseña cifrada                        |
| `roles`     | `Set<Rol>`        | Roles del usuario (USER, ADMIN)           |
| `createdAt` | `LocalDateTime`   | Fecha de creación                         |
| `updatedAt` | `LocalDateTime`   | Fecha de última actualización             |
| `isDeleted` | `Boolean`         | Marcado de borrado lógico                 |

---

### 🛒 Carrito (`Carrito.java`)

**Tabla**: `carritos`  
**Ubicación**: `src/main/java/srangeldev/camisapi/rest/carrito/models/Carrito.java`

Almacena productos temporales del usuario.

| Campo          | Tipo              | Descripción                            |
|----------------|-------------------|----------------------------------------|
| `id`           | `Long`            | Primary Key (autoincremental)          |
| `userId`       | `Long`            | Referencia al id del User              |
| `items`        | `List<String>`    | Lista de IDs (String) de Productos     |
| `modificadoEn` | `LocalDateTime`   | Última modificación                    |
| `creadoEn`     | `LocalDateTime`   | Fecha de creación                      |

**Comportamiento**:
- Al añadir un producto → cambiar estado a `RESERVADO` en MongoDB
- Al eliminar o expirar → cambiar estado a `DISPONIBLE` en MongoDB

---

### 📦 Pedido (`Pedido.java`)

**Tabla**: `pedidos`  
**Ubicación**: `src/main/java/srangeldev/camisapi/rest/pedidos/models/Pedido.java`

Representa una compra confirmada con snapshot de productos.

| Campo              | Tipo                   | Descripción                        |
|--------------------|------------------------|------------------------------------|
| `id`               | `Long`                 | Primary Key (autoincremental)      |
| `userId`           | `Long`                 | Referencia al id del User          |
| `estado`           | `EstadoPedido`         | Estado del pedido                  |
| `fechaCreacion`    | `LocalDateTime`        | Fecha de creación                  |
| `total`            | `Double`               | Total del pedido en euros          |
| `detalles`         | `List<DetallePedido>`  | Snapshot de productos vendidos     |
| `direccionEnvio`   | `String`               | Dirección de entrega (opcional)    |
| `fechaPago`        | `LocalDateTime`        | Fecha del pago (opcional)          |
| `fechaEnvio`       | `LocalDateTime`        | Fecha de envío (opcional)          |
| `numeroSeguimiento`| `String`               | Número de tracking (opcional)      |

#### 🧩 DetallePedido (`DetallePedido.java`)

**Embebido en**: `Pedido`  
**Tabla auxiliar**: `pedido_detalles`

Snapshot inmutable del producto en el momento de la venta.

| Campo          | Tipo     | Descripción                           |
|----------------|----------|---------------------------------------|
| `productoId`   | `String` | Referencia al ObjectId del Producto   |
| `nombre`       | `String` | Nombre del producto (snapshot)        |
| `talla`        | `String` | Talla (snapshot)                      |
| `equipo`       | `String` | Equipo (snapshot)                     |
| `precioPagado` | `Double` | Precio final pagado                   |
| `imageUrl`     | `String` | URL de la imagen (snapshot)           |

#### 📊 Estados del Pedido (`EstadoPedido.java`)

```java
public enum EstadoPedido {
    PENDIENTE_PAGO,  // Esperando confirmación de pago
    PAGADO,          // Pago confirmado
    ENVIADO,         // En tránsito
    ENTREGADO,       // Entregado al cliente
    CANCELADO        // Pedido cancelado
}
```

---

## 🔄 Flujo de Datos

### 1️⃣ Usuario añade producto al carrito

```
1. Usuario selecciona producto (estado: DISPONIBLE)
2. Sistema verifica disponibilidad en MongoDB
3. Crear/Actualizar Carrito en PostgreSQL con productoId
4. Cambiar estado del producto a RESERVADO en MongoDB
```

### 2️⃣ Usuario completa la compra

```
1. Usuario confirma carrito
2. Sistema crea Pedido en PostgreSQL
3. Para cada producto en carrito:
   a. Leer datos completos del producto en MongoDB
   b. Crear DetallePedido con snapshot de datos
   c. Cambiar estado del producto a VENDIDO en MongoDB
4. Eliminar Carrito en PostgreSQL
5. Establecer estado del pedido como PENDIENTE_PAGO
```

### 3️⃣ Usuario abandona el carrito (expiración)

```
1. Job programado busca carritos antiguos
2. Para cada producto en carrito expirado:
   a. Cambiar estado a DISPONIBLE en MongoDB
3. Eliminar Carrito en PostgreSQL
```

---

## 📁 Estructura de Directorios

```
src/main/java/srangeldev/camisapi/rest/
├── users/
│   └── models/
│       ├── User.java             (PostgreSQL JPA)
│       └── Rol.java              (Enum)
├── productos/
│   └── models/
│       ├── Producto.java         (MongoDB)
│       └── EstadoProducto.java   (Enum)
├── carrito/
│   └── models/
│       └── Carrito.java          (PostgreSQL JPA)
└── pedidos/
    └── models/
        ├── Pedido.java           (PostgreSQL JPA)
        ├── DetallePedido.java    (Embedded)
        └── EstadoPedido.java     (Enum)
```

---

## ⚡ Ventajas de esta Arquitectura

### MongoDB (Productos)
✅ Flexibilidad para cambios en el catálogo  
✅ Búsquedas rápidas por equipo, talla, etc.  
✅ Escalabilidad horizontal  
✅ No requiere migraciones para nuevos campos  

### PostgreSQL (Users, Carritos y Pedidos)
✅ Transacciones ACID para compras  
✅ Consistencia en operaciones críticas  
✅ Integridad referencial entre usuarios, carritos y pedidos  
✅ Histórico inmutable de pedidos  
✅ Gestión segura de autenticación y roles  

---

## 🔐 Consideraciones de Seguridad

1. **Passwords**: Siempre cifrar con BCrypt antes de guardar en MongoDB
2. **Validación**: Usar `@Valid` en controladores para validar DTOs
3. **Estados**: Validar transiciones de estados (no permitir VENDIDO → DISPONIBLE)
4. **Concurrencia**: Usar transacciones en PostgreSQL para operaciones de carrito/pedido
5. **Referencias**: Validar que los ObjectIds existan antes de crear referencias

---

## 📌 Próximos Pasos

- [ ] Crear Repositories (MongoRepository y JpaRepository)
- [ ] Implementar Services con lógica de negocio
- [ ] Crear DTOs para requests/responses
- [ ] Implementar Controllers REST
- [ ] Configurar seguridad con JWT
- [ ] Implementar job de expiración de carritos
- [ ] Crear tests unitarios e integración

---

**Fecha de creación**: 10 de noviembre de 2025  
**Versión**: 1.0
