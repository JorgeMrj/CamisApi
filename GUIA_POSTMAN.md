# 📮 Guía Completa de Pruebas con Postman - CamisAPI

## 📋 Índice
1. [Introducción y Configuración](#introducción)
2. [Arquitectura de la API](#arquitectura)
3. [Autenticación JWT](#autenticación)
4. [Endpoints de Usuarios](#usuarios)
5. [Endpoints de Productos](#productos)
6. [Endpoints de Carritos](#carritos)
7. [Endpoints de Pedidos](#pedidos)
8. [Flujo Completo de Compra](#flujo-completo)
9. [Colección Postman](#colección-postman)

---

## 🎯 Introducción

**CamisAPI** es una API REST para la gestión de una tienda de camisetas de fútbol con arquitectura híbrida:
- **PostgreSQL**: Usuarios, Carritos y Pedidos (datos transaccionales)
- **MongoDB**: Productos/Camisetas (catálogo flexible)

### ⚙️ Configuración Inicial

**URL Base**: `http://localhost:8081`

**Requisitos previos**:
1. Tener la API ejecutándose (`./gradlew bootRun`)
2. PostgreSQL y MongoDB activos
3. Postman instalado

---

## 🏛️ Arquitectura

### Bases de Datos

#### PostgreSQL (Transaccional)
- **Users**: Usuarios del sistema con roles (USER, ADMIN)
- **Carritos**: Carritos temporales con referencias a productos
- **Pedidos**: Compras confirmadas con snapshot de productos

#### MongoDB (Catálogo)
- **Productos**: Camisetas únicas con estados (DISPONIBLE, RESERVADO, VENDIDO)

### Estados del Producto
```
DISPONIBLE → (añadir al carrito) → RESERVADO
RESERVADO → (completar compra) → VENDIDO
RESERVADO → (eliminar/expirar) → DISPONIBLE
```

### Estados del Pedido
- `PENDIENTE_PAGO`: Esperando confirmación de pago
- `PAGADO`: Pago confirmado
- `ENVIADO`: En tránsito
- `ENTREGADO`: Entregado al cliente
- `CANCELADO`: Pedido cancelado

---

## 🔐 1. Autenticación JWT

La API usa JWT (JSON Web Tokens) para autenticación. Debes obtener un token y usarlo en todas las peticiones protegidas.

### 🔹 1.1 Login (Obtener Token)

**Endpoint**: `POST /auth/login`

**Headers**:
```
Content-Type: application/json
```

**Body** (raw JSON):
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta exitosa** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "message": "Login exitoso"
}
```

**Respuesta fallida** (401 Unauthorized):
```json
{
  "token": null,
  "username": null,
  "message": "Credenciales incorrectas"
}
```

### 🔹 1.2 Configurar Token en Postman

**Opción A: Por petición individual**
1. En la pestaña **Headers** añade:
   - Key: `Authorization`
   - Value: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

**Opción B: Variable de colección (recomendado)**
1. Copia el token de la respuesta del login
2. En Postman → Pestaña **Authorization**
3. Type: `Bearer Token`
4. Token: Pega el token copiado
5. O usa una variable: `{{jwt_token}}`

**Script Post-Response para automatizar** (en el endpoint de login):
```javascript
// Test tab del endpoint POST /auth/login
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.collectionVariables.set("jwt_token", jsonData.token);
    console.log("Token guardado:", jsonData.token);
}
```

### 🔹 1.3 Verificar Autenticación

**Endpoint**: `GET /auth/test`

**Headers**:
```
Authorization: Bearer {{jwt_token}}
```

**Respuesta exitosa** (200 OK):
```
Acceso autorizado - JWT funciona correctamente
```

---

## 👤 2. Endpoints de Usuarios

Base URL: `/api/v1/users`

**⚠️ Requiere**: Autenticación JWT + Rol ADMIN

### 🔹 2.1 Listar Todos los Usuarios

**Endpoint**: `GET /api/v1/users`

**Headers**:
```
Authorization: Bearer {{jwt_token}}
```

**Respuesta** (200 OK):
```json
[
  {
    "id": 1,
    "nombre": "Administrador",
    "username": "admin",
    "roles": ["ROLE_ADMIN", "ROLE_USER"],
    "createdAt": "2025-11-10T10:30:00",
    "updatedAt": "2025-11-10T10:30:00",
    "isDeleted": false
  },
  {
    "id": 2,
    "nombre": "Juan Pérez",
    "username": "juanp",
    "roles": ["ROLE_USER"],
    "createdAt": "2025-11-11T14:20:00",
    "updatedAt": "2025-11-11T14:20:00",
    "isDeleted": false
  }
]
```

### 🔹 2.2 Obtener Usuario por ID

**Endpoint**: `GET /api/v1/users/{id}`

**Ejemplo**: `GET /api/v1/users/1`

**Respuesta** (200 OK):
```json
{
  "id": 1,
  "nombre": "Administrador",
  "username": "admin",
  "roles": ["ROLE_ADMIN", "ROLE_USER"],
  "createdAt": "2025-11-10T10:30:00",
  "updatedAt": "2025-11-10T10:30:00",
  "isDeleted": false
}
```

**Respuesta error** (404 Not Found):
```json
{
  "statusCode": 404,
  "message": "Usuario no encontrado con id: 999"
}
```

### 🔹 2.3 Buscar Usuarios por Nombre

**Endpoint**: `GET /api/v1/users/nombre/{nombre}`

**Ejemplo**: `GET /api/v1/users/nombre/Juan`

**Respuesta** (200 OK):
```json
[
  {
    "id": 2,
    "nombre": "Juan Pérez",
    "username": "juanp",
    "roles": ["ROLE_USER"],
    "createdAt": "2025-11-11T14:20:00",
    "updatedAt": "2025-11-11T14:20:00",
    "isDeleted": false
  }
]
```

### 🔹 2.4 Crear Usuario

**Endpoint**: `POST /api/v1/users`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {{jwt_token}}
```

**Body** (raw JSON):
```json
{
  "nombre": "María López",
  "username": "marial",
  "password": "password123",
  "roles": ["USER"]
}
```

**Respuesta** (201 Created):
```json
{
  "id": 3,
  "nombre": "María López",
  "username": "marial",
  "roles": ["ROLE_USER"],
  "createdAt": "2025-11-18T09:15:00",
  "updatedAt": "2025-11-18T09:15:00",
  "isDeleted": false
}
```

### 🔹 2.5 Actualizar Usuario

**Endpoint**: `PUT /api/v1/users/{id}`

**Ejemplo**: `PUT /api/v1/users/3`

**Body** (raw JSON):
```json
{
  "nombre": "María López García",
  "username": "marial",
  "password": "newpassword456",
  "roles": ["USER", "ADMIN"]
}
```

**Respuesta** (200 OK):
```json
{
  "id": 3,
  "nombre": "María López García",
  "username": "marial",
  "roles": ["ROLE_USER", "ROLE_ADMIN"],
  "createdAt": "2025-11-18T09:15:00",
  "updatedAt": "2025-11-18T10:30:00",
  "isDeleted": false
}
```

### 🔹 2.6 Eliminar Usuario

**Endpoint**: `DELETE /api/v1/users/{id}`

**Ejemplo**: `DELETE /api/v1/users/3`

**Respuesta** (204 No Content):
```
(Sin contenido)
```

---

## 👕 3. Endpoints de Productos

Base URL: `/api/productos`

**ℹ️ Nota**: Los productos se almacenan en MongoDB con ObjectIds

### 🔹 3.1 Crear Producto

**Endpoint**: `POST /api/productos`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {{jwt_token}}
```

**Body** (raw JSON):
```json
{
  "nombre": "Camiseta Real Madrid 23/24 Local",
  "equipo": "Real Madrid",
  "talla": "L",
  "descripcion": "Camiseta oficial del Real Madrid temporada 2023/2024",
  "precio": 89.99,
  "imageUrl": "https://example.com/real-madrid-home.jpg",
  "estado": "DISPONIBLE"
}
```

**Valores válidos**:
- **talla**: `"S"`, `"M"`, `"L"`, `"XL"`, `"XXL"`
- **estado**: `"DISPONIBLE"`, `"RESERVADO"`, `"VENDIDO"`

**Respuesta** (201 Created):
```json
{
  "id": "673b1f2e8d4c9a5b3c1d2e3f",
  "nombre": "Camiseta Real Madrid 23/24 Local",
  "equipo": "Real Madrid",
  "talla": "L",
  "descripcion": "Camiseta oficial del Real Madrid temporada 2023/2024",
  "precio": 89.99,
  "imageUrl": "https://example.com/real-madrid-home.jpg",
  "estado": "DISPONIBLE",
  "fechaCreacion": "2025-11-18T10:45:00"
}
```

### 🔹 3.2 Listar Todos los Productos

**Endpoint**: `GET /api/productos`

**Respuesta** (200 OK):
```json
[
  {
    "id": "673b1f2e8d4c9a5b3c1d2e3f",
    "nombre": "Camiseta Real Madrid 23/24 Local",
    "equipo": "Real Madrid",
    "talla": "L",
    "descripcion": "Camiseta oficial del Real Madrid temporada 2023/2024",
    "precio": 89.99,
    "imageUrl": "https://example.com/real-madrid-home.jpg",
    "estado": "DISPONIBLE",
    "fechaCreacion": "2025-11-18T10:45:00"
  },
  {
    "id": "673b201a9e5d7b6c4d3e4f5g",
    "nombre": "Camiseta FC Barcelona 23/24 Local",
    "equipo": "Barcelona",
    "talla": "M",
    "descripcion": "Camiseta oficial del FC Barcelona temporada 2023/2024",
    "precio": 85.99,
    "imageUrl": "https://example.com/barcelona-home.jpg",
    "estado": "DISPONIBLE",
    "fechaCreacion": "2025-11-18T11:00:00"
  }
]
```

### 🔹 3.3 Obtener Producto por ID

**Endpoint**: `GET /api/productos/{id}`

**Ejemplo**: `GET /api/productos/673b1f2e8d4c9a5b3c1d2e3f`

**Respuesta** (200 OK):
```json
{
  "id": "673b1f2e8d4c9a5b3c1d2e3f",
  "nombre": "Camiseta Real Madrid 23/24 Local",
  "equipo": "Real Madrid",
  "talla": "L",
  "descripcion": "Camiseta oficial del Real Madrid temporada 2023/2024",
  "precio": 89.99,
  "imageUrl": "https://example.com/real-madrid-home.jpg",
  "estado": "DISPONIBLE",
  "fechaCreacion": "2025-11-18T10:45:00"
}
```

### 🔹 3.4 Actualizar Producto

**Endpoint**: `PUT /api/productos/{id}`

**Ejemplo**: `PUT /api/productos/673b1f2e8d4c9a5b3c1d2e3f`

**Body** (raw JSON):
```json
{
  "nombre": "Camiseta Real Madrid 23/24 Local - OFERTA",
  "equipo": "Real Madrid",
  "talla": "L",
  "descripcion": "Camiseta oficial con descuento",
  "precio": 69.99,
  "imageUrl": "https://example.com/real-madrid-home.jpg",
  "estado": "DISPONIBLE"
}
```

**Respuesta** (200 OK):
```json
{
  "id": "673b1f2e8d4c9a5b3c1d2e3f",
  "nombre": "Camiseta Real Madrid 23/24 Local - OFERTA",
  "equipo": "Real Madrid",
  "talla": "L",
  "descripcion": "Camiseta oficial con descuento",
  "precio": 69.99,
  "imageUrl": "https://example.com/real-madrid-home.jpg",
  "estado": "DISPONIBLE",
  "fechaCreacion": "2025-11-18T10:45:00"
}
```

### 🔹 3.5 Eliminar Producto

**Endpoint**: `DELETE /api/productos/{id}`

**Ejemplo**: `DELETE /api/productos/673b1f2e8d4c9a5b3c1d2e3f`

**Respuesta** (204 No Content):
```
(Sin contenido)
```

### 🔹 3.6 Buscar por Nombre

**Endpoint**: `GET /api/productos/buscar/nombre?nombre={texto}`

**Ejemplo**: `GET /api/productos/buscar/nombre?nombre=Madrid`

**Respuesta** (200 OK):
```json
[
  {
    "id": "673b1f2e8d4c9a5b3c1d2e3f",
    "nombre": "Camiseta Real Madrid 23/24 Local",
    "equipo": "Real Madrid",
    "talla": "L",
    "precio": 89.99,
    "estado": "DISPONIBLE",
    "fechaCreacion": "2025-11-18T10:45:00"
  }
]
```

### 🔹 3.7 Buscar por Equipo

**Endpoint**: `GET /api/productos/buscar/equipo?equipo={nombre}`

**Ejemplo**: `GET /api/productos/buscar/equipo?equipo=Barcelona`

**Respuesta** (200 OK):
```json
[
  {
    "id": "673b201a9e5d7b6c4d3e4f5g",
    "nombre": "Camiseta FC Barcelona 23/24 Local",
    "equipo": "Barcelona",
    "talla": "M",
    "precio": 85.99,
    "estado": "DISPONIBLE",
    "fechaCreacion": "2025-11-18T11:00:00"
  }
]
```

### 🔹 3.8 Buscar por Estado

**Endpoint**: `GET /api/productos/buscar/estado?estado={estado}`

**Ejemplo**: `GET /api/productos/buscar/estado?estado=DISPONIBLE`

**Valores válidos**: `DISPONIBLE`, `RESERVADO`, `VENDIDO`

**Respuesta** (200 OK):
```json
[
  {
    "id": "673b1f2e8d4c9a5b3c1d2e3f",
    "nombre": "Camiseta Real Madrid 23/24 Local",
    "estado": "DISPONIBLE",
    "precio": 89.99
  }
]
```

### 🔹 3.9 Buscar por Talla

**Endpoint**: `GET /api/productos/buscar/talla?talla={talla}`

**Ejemplo**: `GET /api/productos/buscar/talla?talla=L`

**Valores válidos**: `S`, `M`, `L`, `XL`, `XXL`

**Respuesta** (200 OK):
```json
[
  {
    "id": "673b1f2e8d4c9a5b3c1d2e3f",
    "nombre": "Camiseta Real Madrid 23/24 Local",
    "talla": "L",
    "precio": 89.99,
    "estado": "DISPONIBLE"
  }
]
```

---

## 🛒 4. Endpoints de Carritos

Base URL: `/api/carritos`

**ℹ️ Importante**: Al añadir productos al carrito, su estado cambia a `RESERVADO` automáticamente.

### 🔹 4.1 Crear Carrito

**Endpoint**: `POST /api/carritos`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {{jwt_token}}
```

**Body** (raw JSON):
```json
{
  "userId": 2,
  "items": [
    "673b1f2e8d4c9a5b3c1d2e3f",
    "673b201a9e5d7b6c4d3e4f5g"
  ]
}
```

**⚠️ Nota**: Los `items` son ObjectIds de MongoDB (String)

**Respuesta** (201 Created):
```json
{
  "id": 1,
  "userId": 2,
  "items": [
    "673b1f2e8d4c9a5b3c1d2e3f",
    "673b201a9e5d7b6c4d3e4f5g"
  ],
  "creadoEn": "2025-11-18T12:00:00",
  "modificadoEn": "2025-11-18T12:00:00"
}
```

**Efecto secundario**: Los productos con esos IDs cambiarán su estado a `RESERVADO` en MongoDB.

### 🔹 4.2 Listar Todos los Carritos

**Endpoint**: `GET /api/carritos`

**Respuesta** (200 OK):
```json
[
  {
    "id": 1,
    "userId": 2,
    "items": [
      "673b1f2e8d4c9a5b3c1d2e3f",
      "673b201a9e5d7b6c4d3e4f5g"
    ],
    "creadoEn": "2025-11-18T12:00:00",
    "modificadoEn": "2025-11-18T12:00:00"
  }
]
```

### 🔹 4.3 Obtener Carrito por ID

**Endpoint**: `GET /api/carritos/{id}`

**Ejemplo**: `GET /api/carritos/1`

**Respuesta** (200 OK):
```json
{
  "id": 1,
  "userId": 2,
  "items": [
    "673b1f2e8d4c9a5b3c1d2e3f",
    "673b201a9e5d7b6c4d3e4f5g"
  ],
  "creadoEn": "2025-11-18T12:00:00",
  "modificadoEn": "2025-11-18T12:00:00"
}
```

### 🔹 4.4 Obtener Carrito por Usuario

**Endpoint**: `GET /api/carritos/usuario/{userId}`

**Ejemplo**: `GET /api/carritos/usuario/2`

**Respuesta** (200 OK):
```json
{
  "id": 1,
  "userId": 2,
  "items": [
    "673b1f2e8d4c9a5b3c1d2e3f",
    "673b201a9e5d7b6c4d3e4f5g"
  ],
  "creadoEn": "2025-11-18T12:00:00",
  "modificadoEn": "2025-11-18T12:00:00"
}
```

### 🔹 4.5 Actualizar Carrito

**Endpoint**: `PUT /api/carritos/{id}`

**Ejemplo**: `PUT /api/carritos/1`

**Body** (raw JSON):
```json
{
  "userId": 2,
  "items": [
    "673b1f2e8d4c9a5b3c1d2e3f",
    "673b201a9e5d7b6c4d3e4f5g",
    "673b2105af6e8c7d5e4f5g6h"
  ]
}
```

**Respuesta** (200 OK):
```json
{
  "id": 1,
  "userId": 2,
  "items": [
    "673b1f2e8d4c9a5b3c1d2e3f",
    "673b201a9e5d7b6c4d3e4f5g",
    "673b2105af6e8c7d5e4f5g6h"
  ],
  "creadoEn": "2025-11-18T12:00:00",
  "modificadoEn": "2025-11-18T12:15:00"
}
```

### 🔹 4.6 Eliminar Carrito

**Endpoint**: `DELETE /api/carritos/{id}`

**Ejemplo**: `DELETE /api/carritos/1`

**Respuesta** (200 OK):
```json
{
  "id": 1,
  "userId": 2,
  "items": [
    "673b1f2e8d4c9a5b3c1d2e3f",
    "673b201a9e5d7b6c4d3e4f5g"
  ],
  "creadoEn": "2025-11-18T12:00:00",
  "modificadoEn": "2025-11-18T12:15:00"
}
```

**Efecto secundario**: Los productos del carrito volverán al estado `DISPONIBLE` en MongoDB.

---

## 📦 5. Endpoints de Pedidos

Base URL: `/api/pedidos`

**ℹ️ Importante**: Los pedidos almacenan un snapshot de los productos en el momento de la compra.

### 🔹 5.1 Crear Pedido

**Endpoint**: `POST /api/pedidos`

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {{jwt_token}}
```

**Body** (raw JSON):
```json
{
  "userId": 2,
  "direccionEnvio": "Calle Falsa 123, Madrid, 28001",
  "productosIds": [
    "673b1f2e8d4c9a5b3c1d2e3f",
    "673b201a9e5d7b6c4d3e4f5g"
  ]
}
```

**Respuesta** (201 Created):
```json
{
  "id": 1,
  "userId": 2,
  "estado": "PENDIENTE_PAGO",
  "fechaCreacion": "2025-11-18T13:00:00",
  "total": 175.98,
  "direccionEnvio": "Calle Falsa 123, Madrid, 28001",
  "fechaPago": null,
  "fechaEnvio": null,
  "numeroSeguimiento": null,
  "detalles": [
    {
      "productoId": "673b1f2e8d4c9a5b3c1d2e3f",
      "nombre": "Camiseta Real Madrid 23/24 Local",
      "talla": "L",
      "equipo": "Real Madrid",
      "precioPagado": 89.99,
      "imageUrl": "https://example.com/real-madrid-home.jpg"
    },
    {
      "productoId": "673b201a9e5d7b6c4d3e4f5g",
      "nombre": "Camiseta FC Barcelona 23/24 Local",
      "talla": "M",
      "equipo": "Barcelona",
      "precioPagado": 85.99,
      "imageUrl": "https://example.com/barcelona-home.jpg"
    }
  ]
}
```

**Efecto secundario**: Los productos cambian a estado `VENDIDO` en MongoDB.

### 🔹 5.2 Listar Todos los Pedidos

**Endpoint**: `GET /api/pedidos`

**Respuesta** (200 OK):
```json
[
  {
    "id": 1,
    "userId": 2,
    "estado": "PENDIENTE_PAGO",
    "fechaCreacion": "2025-11-18T13:00:00",
    "total": 175.98,
    "direccionEnvio": "Calle Falsa 123, Madrid, 28001",
    "detalles": [ /* ... */ ]
  }
]
```

### 🔹 5.3 Obtener Pedido por ID

**Endpoint**: `GET /api/pedidos/{id}`

**Ejemplo**: `GET /api/pedidos/1`

**Respuesta** (200 OK):
```json
{
  "id": 1,
  "userId": 2,
  "estado": "PENDIENTE_PAGO",
  "fechaCreacion": "2025-11-18T13:00:00",
  "total": 175.98,
  "direccionEnvio": "Calle Falsa 123, Madrid, 28001",
  "fechaPago": null,
  "fechaEnvio": null,
  "numeroSeguimiento": null,
  "detalles": [
    {
      "productoId": "673b1f2e8d4c9a5b3c1d2e3f",
      "nombre": "Camiseta Real Madrid 23/24 Local",
      "talla": "L",
      "equipo": "Real Madrid",
      "precioPagado": 89.99,
      "imageUrl": "https://example.com/real-madrid-home.jpg"
    }
  ]
}
```

### 🔹 5.4 Obtener Pedidos por Usuario

**Endpoint**: `GET /api/pedidos/usuario/{userId}`

**Ejemplo**: `GET /api/pedidos/usuario/2`

**Respuesta** (200 OK):
```json
[
  {
    "id": 1,
    "userId": 2,
    "estado": "PENDIENTE_PAGO",
    "total": 175.98,
    "fechaCreacion": "2025-11-18T13:00:00"
  },
  {
    "id": 3,
    "userId": 2,
    "estado": "PAGADO",
    "total": 89.99,
    "fechaCreacion": "2025-11-17T10:30:00"
  }
]
```

### 🔹 5.5 Obtener Pedidos por Estado

**Endpoint**: `GET /api/pedidos/estado?estado={estado}`

**Ejemplo**: `GET /api/pedidos/estado?estado=PAGADO`

**Valores válidos**: 
- `PENDIENTE_PAGO`
- `PAGADO`
- `ENVIADO`
- `ENTREGADO`
- `CANCELADO`

**Respuesta** (200 OK):
```json
[
  {
    "id": 2,
    "userId": 3,
    "estado": "PAGADO",
    "total": 89.99,
    "fechaCreacion": "2025-11-17T15:00:00"
  }
]
```

### 🔹 5.6 Actualizar Estado del Pedido

**Endpoint**: `PATCH /api/pedidos/{id}/estado?estado={nuevoEstado}`

**Ejemplo**: `PATCH /api/pedidos/1/estado?estado=PAGADO`

**Respuesta** (200 OK):
```json
{
  "id": 1,
  "userId": 2,
  "estado": "PAGADO",
  "fechaCreacion": "2025-11-18T13:00:00",
  "fechaPago": "2025-11-18T13:30:00",
  "total": 175.98,
  "detalles": [ /* ... */ ]
}
```

**Transiciones válidas**:
```
PENDIENTE_PAGO → PAGADO → ENVIADO → ENTREGADO
                    ↓
                CANCELADO
```

### 🔹 5.7 Eliminar Pedido

**Endpoint**: `DELETE /api/pedidos/{id}`

**Ejemplo**: `DELETE /api/pedidos/1`

**Respuesta** (204 No Content):
```
(Sin contenido)
```

---

## 🔄 6. Flujo Completo de Compra

Aquí está el proceso completo de una compra, paso a paso:

### Paso 1: Autenticación
```
POST /auth/login
Body: { "username": "admin", "password": "admin123" }
→ Guardar token JWT
```

### Paso 2: Crear Productos (ADMIN)
```
POST /api/productos
Headers: Authorization: Bearer {token}
Body: {
  "nombre": "Camiseta Real Madrid Local",
  "equipo": "Real Madrid",
  "talla": "L",
  "precio": 89.99,
  "estado": "DISPONIBLE"
}
→ Guardar productoId1

POST /api/productos
Body: {
  "nombre": "Camiseta Barcelona Local",
  "equipo": "Barcelona",
  "talla": "M",
  "precio": 85.99,
  "estado": "DISPONIBLE"
}
→ Guardar productoId2
```

### Paso 3: Crear Usuario
```
POST /api/v1/users
Headers: Authorization: Bearer {token}
Body: {
  "nombre": "Juan Pérez",
  "username": "juanp",
  "password": "juan123",
  "roles": ["USER"]
}
→ Guardar userId
```

### Paso 4: Crear Carrito
```
POST /api/carritos
Headers: Authorization: Bearer {token}
Body: {
  "userId": {userId},
  "items": ["{productoId1}", "{productoId2}"]
}
→ Productos cambian a estado RESERVADO
```

### Paso 5: Verificar Productos Reservados
```
GET /api/productos/buscar/estado?estado=RESERVADO
→ Debe mostrar los 2 productos del carrito
```

### Paso 6: Crear Pedido
```
POST /api/pedidos
Headers: Authorization: Bearer {token}
Body: {
  "userId": {userId},
  "direccionEnvio": "Calle Ejemplo 123",
  "productosIds": ["{productoId1}", "{productoId2}"]
}
→ Productos cambian a estado VENDIDO
→ Se crea snapshot en detalles del pedido
→ Estado inicial: PENDIENTE_PAGO
```

### Paso 7: Confirmar Pago
```
PATCH /api/pedidos/{pedidoId}/estado?estado=PAGADO
→ Estado cambia a PAGADO
→ Se registra fechaPago
```

### Paso 8: Procesar Envío
```
PATCH /api/pedidos/{pedidoId}/estado?estado=ENVIADO
→ Estado cambia a ENVIADO
→ Se registra fechaEnvio
```

### Paso 9: Confirmar Entrega
```
PATCH /api/pedidos/{pedidoId}/estado?estado=ENTREGADO
→ Estado cambia a ENTREGADO
```

### Paso 10: Consultar Historial
```
GET /api/pedidos/usuario/{userId}
→ Ver todos los pedidos del usuario
```

---

## 📋 7. Colección de Postman

### Crear Variables de Colección

1. En Postman, crea una nueva colección: **"CamisAPI"**
2. Click derecho → **Edit**
3. Pestaña **Variables**
4. Añade estas variables:

| Variable | Initial Value | Current Value |
|----------|--------------|---------------|
| `base_url` | `http://localhost:8081` | `http://localhost:8081` |
| `jwt_token` | (dejar vacío) | (dejar vacío) |
| `user_id` | `2` | `2` |
| `producto_id_1` | (dejar vacío) | (dejar vacío) |
| `producto_id_2` | (dejar vacío) | (dejar vacío) |
| `carrito_id` | (dejar vacío) | (dejar vacío) |
| `pedido_id` | (dejar vacío) | (dejar vacío) |

### Scripts Automáticos

#### En POST /auth/login (Tests tab)
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.collectionVariables.set("jwt_token", jsonData.token);
    console.log("✅ Token guardado");
}
```

#### En POST /api/productos (Tests tab)
```javascript
if (pm.response.code === 201) {
    var jsonData = pm.response.json();
    if (!pm.collectionVariables.get("producto_id_1")) {
        pm.collectionVariables.set("producto_id_1", jsonData.id);
        console.log("✅ Producto 1 guardado:", jsonData.id);
    } else if (!pm.collectionVariables.get("producto_id_2")) {
        pm.collectionVariables.set("producto_id_2", jsonData.id);
        console.log("✅ Producto 2 guardado:", jsonData.id);
    }
}
```

#### En POST /api/carritos (Tests tab)
```javascript
if (pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.collectionVariables.set("carrito_id", jsonData.id);
    console.log("✅ Carrito guardado:", jsonData.id);
}
```

#### En POST /api/pedidos (Tests tab)
```javascript
if (pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.collectionVariables.set("pedido_id", jsonData.id);
    console.log("✅ Pedido guardado:", jsonData.id);
}
```

---

## 🧪 8. Casos de Prueba Importantes

### ✅ Caso 1: Producto No Disponible
```
1. Crear producto con estado VENDIDO
2. Intentar añadirlo al carrito
→ Debe fallar (producto no disponible)
```

### ✅ Caso 2: Carrito Vacío
```
1. Crear carrito con items vacíos: []
2. Intentar crear pedido
→ Debe fallar (carrito vacío)
```

### ✅ Caso 3: Doble Reserva
```
1. Usuario A añade producto al carrito
2. Usuario B intenta añadir el mismo producto
→ Debe fallar (producto RESERVADO)
```

### ✅ Caso 4: Autorización
```
1. Hacer petición sin token JWT
→ Debe devolver 401 Unauthorized

2. Usuario sin rol ADMIN intenta GET /api/v1/users
→ Debe devolver 403 Forbidden
```

### ✅ Caso 5: Validaciones
```
1. Crear usuario sin username
→ Debe devolver 400 Bad Request

2. Crear producto con precio negativo
→ Debe devolver 400 Bad Request
```

---

## 📊 9. Resumen de Endpoints

### Autenticación
| Método | Endpoint | Autenticación | Descripción |
|--------|----------|---------------|-------------|
| POST | `/auth/login` | ❌ | Obtener JWT token |
| GET | `/auth/test` | ✅ | Verificar token |

### Usuarios (Requiere ADMIN)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/users` | Listar todos |
| GET | `/api/v1/users/{id}` | Obtener por ID |
| GET | `/api/v1/users/nombre/{nombre}` | Buscar por nombre |
| POST | `/api/v1/users` | Crear usuario |
| PUT | `/api/v1/users/{id}` | Actualizar usuario |
| DELETE | `/api/v1/users/{id}` | Eliminar usuario |

### Productos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos` | Listar todos |
| GET | `/api/productos/{id}` | Obtener por ID |
| POST | `/api/productos` | Crear producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| DELETE | `/api/productos/{id}` | Eliminar producto |
| GET | `/api/productos/buscar/nombre?nombre=X` | Buscar por nombre |
| GET | `/api/productos/buscar/equipo?equipo=X` | Buscar por equipo |
| GET | `/api/productos/buscar/estado?estado=X` | Buscar por estado |
| GET | `/api/productos/buscar/talla?talla=X` | Buscar por talla |

### Carritos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/carritos` | Listar todos |
| GET | `/api/carritos/{id}` | Obtener por ID |
| GET | `/api/carritos/usuario/{userId}` | Obtener por usuario |
| POST | `/api/carritos` | Crear carrito |
| PUT | `/api/carritos/{id}` | Actualizar carrito |
| DELETE | `/api/carritos/{id}` | Eliminar carrito |

### Pedidos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/pedidos` | Listar todos |
| GET | `/api/pedidos/{id}` | Obtener por ID |
| GET | `/api/pedidos/usuario/{userId}` | Obtener por usuario |
| GET | `/api/pedidos/estado?estado=X` | Filtrar por estado |
| POST | `/api/pedidos` | Crear pedido |
| PATCH | `/api/pedidos/{id}/estado?estado=X` | Actualizar estado |
| DELETE | `/api/pedidos/{id}` | Eliminar pedido |

---

## 🚨 Códigos de Estado HTTP

| Código | Significado | Cuándo ocurre |
|--------|-------------|---------------|
| 200 | OK | Operación exitosa |
| 201 | Created | Recurso creado correctamente |
| 204 | No Content | Eliminación exitosa |
| 400 | Bad Request | Datos de entrada inválidos |
| 401 | Unauthorized | Token JWT inválido o ausente |
| 403 | Forbidden | Sin permisos (rol insuficiente) |
| 404 | Not Found | Recurso no encontrado |
| 500 | Internal Server Error | Error del servidor |

---

## 🎓 Conclusión

Esta guía cubre **TODOS** los endpoints de la API y proporciona ejemplos completos para probar cada funcionalidad con Postman. 

### Orden Recomendado de Pruebas:
1. ✅ Login y obtener token
2. ✅ Crear productos
3. ✅ Crear usuario
4. ✅ Añadir productos al carrito
5. ✅ Crear pedido
6. ✅ Actualizar estados del pedido

### Recursos Adicionales:
- Documentación de arquitectura: `ARQUITECTURA_HIBRIDA.md`
- Diagrama E/R: `images/Diagrama ER.png`

---

**Autor**: SrAngelDev  
**Fecha**: 18 de noviembre de 2025  
**Versión**: 1.0
