# Guía de uso - Spring Security con JWT + MongoDB

## ¿Qué hemos implementado?

Una autenticación JWT básica para estudiantes de DAW que incluye:

- ✅ Login con username/password  
- ✅ Generación de token JWT
- ✅ Validación automática de tokens
- ✅ Protección de endpoints
- ✅ Roles básicos (USER, ADMIN)
- ✅ Almacenamiento en MongoDB

## ⚠️ Requisito previo: MongoDB

Necesitas tener MongoDB ejecutándose. Ver `MONGODB_SETUP.md` para instrucciones de instalación.

**TL;DR con Docker:**
```bash
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

## Usuarios por defecto

Se crean automáticamente al iniciar la app (si MongoDB está disponible):

```
Admin: username=admin, password=admin123
User:  username=user,  password=user123
```

## Endpoints disponibles

### Públicos (no requieren autenticación)
- `POST /auth/login` - Login
- `GET /api/public` - Endpoint público de prueba

### Privados (requieren JWT token)
- `GET /auth/test` - Prueba autenticación
- `GET /api/private` - Endpoint privado
- `GET /api/carritos/*` - Gestión de carritos
- `GET /api/productos/*` - Gestión de productos

### Solo Admin
- `GET /api/admin` - Solo administradores

## Cómo usar

### 1. Iniciar la aplicación
```bash
./gradlew bootRun
```

### 2. Hacer login
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta:**
```json
{
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "admin",
  "message": "Login exitoso"
}
```

### 3. Usar el token en requests protegidos
```http
GET http://localhost:8080/api/private
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...
```

### 4. Probar endpoints de prueba
```http
# Público - sin autenticación
GET http://localhost:8080/api/public

# Privado - requiere token
GET http://localhost:8080/api/private
Authorization: Bearer <tu-token-jwt>

# Solo admin - requiere token de admin
GET http://localhost:8080/api/admin  
Authorization: Bearer <token-de-admin>
```

## Estructura del proyecto

```
src/main/java/srangeldev/camisapi/
├── rest/users/
│   ├── models/User.java               # Usuario MongoDB + UserDetails
│   └── repositories/UserRepository.java  # MongoDB Repository
└── security/
    ├── config/SecurityDataLoader.java    # Carga usuarios por defecto
    ├── controller/
    │   ├── AuthController.java           # Login endpoint
    │   └── TestController.java           # Endpoints de prueba
    ├── dto/
    │   ├── AuthResponse.java             # Respuesta de autenticación  
    │   └── LoginRequest.java             # Petición de login
    ├── service/AuthService.java          # Gestión de usuarios
    ├── CustomUserDetailsService.java     # Cargar usuarios
    ├── JwtAuthenticationFilter.java      # Filtro JWT
    ├── JwtUtil.java                      # Utilidades JWT
    └── SecurityConfiguration.java        # Configuración principal
```

## Datos en MongoDB

Los usuarios se almacenan en la colección `users`:

```javascript
{
  "_id": ObjectId("..."),
  "idUsuario": 1,
  "nombre": "Administrador",
  "username": "admin", 
  "password": "$2a$10$...",  // BCrypt hash
  "roles": ["ADMIN"],
  "createdAt": ISODate("..."),
  "updatedAt": ISODate("..."),
  "isDeleted": false
}
```

## Propiedades configurables

En `application.properties`:
```properties
# JWT
jwt.secret=miClaveSecretaSuperSegura123ParaEstudiantesDAW
jwt.expiration=86400

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/camisapi_test
```

## Troubleshooting

### Error: "Usuario no encontrado"
- ✅ Verifica que MongoDB esté ejecutándose
- ✅ Revisa los logs de la aplicación al iniciar
- ✅ Conéctate a MongoDB y verifica que existen usuarios: `db.users.find()`

### Error: "Authentication failed" 
- ✅ Usa las credenciales correctas: `admin/admin123` o `user/user123`
- ✅ Verifica que las contraseñas estén hasheadas en MongoDB

### Error: "Connection refused"
- ✅ Instala y ejecuta MongoDB (ver `MONGODB_SETUP.md`)

## ¿Cómo funciona?

1. **Inicio**: La app crea usuarios por defecto en MongoDB
2. **Login**: Valida credenciales contra MongoDB y genera JWT  
3. **Requests**: El filtro JWT valida el token en cada request
4. **Autorización**: Spring Security verifica roles según los endpoints

## Siguiente paso

¡Ya puedes probar tu API con autenticación JWT y MongoDB! 🚀

Para desarrollo avanzado:
- Implementar registro de usuarios
- Añadir refresh tokens
- Configurar CORS para frontend
- Añadir más roles y permisos
