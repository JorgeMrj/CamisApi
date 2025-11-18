# Instalación de MongoDB para Spring Security JWT

## ¿Por qué MongoDB?

Tu aplicación usa MongoDB para almacenar usuarios según la arquitectura híbrida:
- **MongoDB**: Usuarios (autenticación)
- **PostgreSQL**: Productos, Carritos, Pedidos

## Instalación rápida de MongoDB (Windows)

### Opción 1: Docker (Recomendado para estudiantes)

```bash
# Descargar e instalar Docker Desktop
# Luego ejecutar:
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

### Opción 2: Instalación directa

1. Descargar MongoDB Community Server desde: https://www.mongodb.com/try/download/community
2. Instalar con configuración por defecto
3. MongoDB se ejecutará automáticamente en `localhost:27017`

### Opción 3: MongoDB Atlas (Cloud - Gratis)

1. Crear cuenta en https://www.mongodb.com/atlas
2. Crear cluster gratuito
3. Obtener connection string
4. Actualizar `application-dev.properties`:

```properties
spring.data.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/camisapi_test
```

## Verificar instalación

### Con Docker:
```bash
docker exec -it mongodb mongosh
```

### Con instalación local:
```bash
mongosh
```

### En ambos casos:
```javascript
// Crear base de datos de prueba
use camisapi_test

// Verificar que está funcionando
db.test.insertOne({mensaje: "MongoDB funciona!"})
db.test.find()
```

## Estructura que creará la aplicación

```javascript
// Colección: users
{
  "_id": ObjectId("..."),
  "idUsuario": 1,
  "nombre": "Administrador", 
  "username": "admin",
  "password": "$2a$10$...", // BCrypt hash
  "roles": ["ADMIN"],
  "createdAt": ISODate("..."),
  "updatedAt": ISODate("..."),
  "isDeleted": false
}
```

## Usuarios por defecto que crea la app

- **Admin**: `admin` / `admin123` 
- **User**: `user` / `user123`

## Comandos útiles

```bash
# Ver bases de datos
show dbs

# Usar base de datos
use camisapi_test

# Ver colecciones
show collections

# Ver usuarios
db.users.find().pretty()

# Limpiar usuarios (si es necesario)
db.users.deleteMany({})
```

## Si MongoDB no está disponible

La aplicación funcionará parcialmente:
- ✅ Endpoints públicos (`/auth/login`, `/api/public`)
- ❌ Endpoints protegidos (fallarán porque no puede cargar usuarios)

## Próximos pasos

1. Instalar MongoDB
2. Ejecutar la aplicación: `./gradlew bootRun`
3. Probar login: `POST /auth/login` con `admin/admin123`
4. Usar el token JWT en requests protegidos

¡Listo para usar Spring Security con JWT! 🚀
