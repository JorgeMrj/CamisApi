# GraphQL API - Guía de Consultas

#### Obtener todos los carritos
```graphql
query {
  carritos {
    id
    userId
    items
    totalItems
    modificadoEn
    creadoEn
    isValid
  }
}
```

#### Obtener carrito por ID
```graphql
query {
  carrito(id: "1") {
    id
    userId
    items
    totalItems
    modificadoEn
    creadoEn
    isValid
  }
}
```

#### Obtener carrito por ID de usuario
```graphql
query {
  carritoByUserId(userId: "123") {
    id
    userId
    items
    totalItems
    modificadoEn
    creadoEn
    isValid
  }
}
```

###  Productos

#### Obtener todos los productos
```graphql
query {
  productos {
    id
    nombre
    equipo
    descripcion
    precio
    imageUrl
    estado
    talla
    fechaCreacion
    esDisponible
  }
}
```

#### Obtener producto por ID
```graphql
query {
  producto(id: "1") {
    id
    nombre
    equipo
    descripcion
    precio
    imageUrl
    estado
    talla
    fechaCreacion
    esDisponible
  }
}
```

#### Buscar productos por nombre
```graphql
query {
  productosPorNombre(nombre: "Madrid") {
    id
    nombre
    equipo
    precio
    estado
    esDisponible
  }
}
```

#### Buscar productos por equipo
```graphql
query {
  productosPorEquipo(equipo: "Real Madrid") {
    id
    nombre
    equipo
    precio
    estado
    esDisponible
  }
}
```

#### Buscar productos por estado
```graphql
query {
  productosPorEstado(estado: "DISPONIBLE") {
    id
    nombre
    equipo
    precio
    estado
    esDisponible
  }
}
```

#### Buscar productos por talla
```graphql
query {
  productosPorTalla(talla: "M") {
    id
    nombre
    equipo
    precio
    talla
    estado
    esDisponible
  }
}
```

### Usuarios

#### Obtener todos los usuarios
```graphql
query {
  usuarios {
    id
    nombre
    username
    roles
    creadoEn
    modificadoEn
    esActivo
    esAdmin
  }
}
```

#### Obtener usuario por ID
```graphql
query {
  usuario(id: "1") {
    id
    nombre
    username
    roles
    creadoEn
    modificadoEn
    esActivo
    esAdmin
  }
}
```

#### Buscar usuarios por nombre
```graphql
query {
  usuariosPorNombre(nombre: "Juan") {
    id
    nombre
    username
    roles
    esActivo
    esAdmin
  }
}
```

### Pedidos

#### Obtener todos los pedidos
```graphql
query {
  pedidos {
    id
    usuario
    fechaPedido
    total
    estado
    detalles
    fechaPago
    fechaEnvio
    esPagado
    esEnviado
  }
}
```

#### Obtener pedido por ID
```graphql
query {
  pedido(id: "1") {
    id
    usuario
    fechaPedido
    total
    estado
    detalles
    fechaPago
    fechaEnvio
    esPagado
    esEnviado
  }
}
```

#### Buscar pedidos por usuario
```graphql
query {
  pedidosPorUsuario(usuario: "user123") {
    id
    usuario
    fechaPedido
    total
    estado
    esPagado
    esEnviado
  }
}
```

#### Buscar pedidos por estado
```graphql
query {
  pedidosPorEstado(estado: "PENDIENTE") {
    id
    usuario
    fechaPedido
    total
    estado
    esPagado
    esEnviado
  }
}
```

## Consultas Combinadas

### Obtener información completa de productos y carritos
```graphql
query {
  productos {
    id
    nombre
    equipo
    precio
    esDisponible
  }
  carritos {
    id
    userId
    totalItems
    isValid
  }
}
```

### Dashboard completo
```graphql
query {
  productos {
    id
    nombre
    estado
    precio
    esDisponible
  }
  usuarios {
    id
    nombre
    esActivo
    esAdmin
  }
  pedidos {
    id
    total
    estado
    esPagado
  }
  carritos {
    id
    totalItems
    isValid
  }
}
```