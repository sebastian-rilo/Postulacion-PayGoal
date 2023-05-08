# Postulacion PayGoal

### Autor: Sebastian Rilo

## Tecnologías
`Spring Boot` `Java 11` `HSQLDB`

## Descripción
Este es un Proyecto de pequeña escala desarrollado para modelar un sistema de gestion de productos de poca complejidad, que seran almacenados en memoria utilizando la base de datos relacional HSQLDB.

## Avisos importantes
Este proyecto cuenta con datos precargados para facilitar la prueba de funcionalidades de la aplicación. En caso de querer inicializar la App con una base de datos vacia, se debe comentar/eliminar el [metodo inicializador ubicado en la clase principal de la aplicacion](https://github.com/sebastian-rilo/Postulacion-PayGoal/blob/272ce5e0aaf063c7455e485f70e53d750ee3dc0d/src/main/java/com/PayGoal/Postulacion/PostulacionApplication.java#L30). 

## Endpoints
Este proyecto cuenta con un total de 7 ednpoints que van a permitir al usuario crear, actualizar, eliminar y hacer consultas sobre los produtos de la base de datos

 - [Obtener todos los Productos](#obtener-todos-los-productos)
 - [Obtener todos los productos ordenados por precio](#obtener-todos-los-productos-ordenados-por-precio)
 - [Obtener todos los productos con el mismo nombre](#obtener-todos-los-productos-con-el-mismo-nombre)
 - [Obtener un producto por Id](#obtener-un-producto-por-id)
 - [Crear un producto](#crear-un-producto)
 - [Actualizar un producto](#actualizar-un-producto)
 - [Eliminar un producto](#eliminar-un-producto)

## Obtener todos los productos
````http
GET https://localhost:8080/api/productos/ 
````
Endpoint que nos permite obtener de manera directa una lista de productos:
````json
[
    {
        "id": 1,
        "nombre": "producto A",
        "descripcion": "El producto N°1 de la base de datos",
        "precio": 10.00,
        "cantidad": 10
    },
    {
        "id": 2,
        "nombre": "producto B",
        "descripcion": "El producto N°2 de la base de datos",
        "precio": 5.00,
        "cantidad": 250
    },
    {
        "id": 3,
        "nombre": "producto C",
        "descripcion": "El producto N°3 de la base de datos",
        "precio": 100.00,
        "cantidad": 5
    },
    {
        "id": 4,
        "nombre": "producto A",
        "descripcion": "El producto N°4 de la base de datos",
        "precio": 200.00,
        "cantidad": 50
    }
]
````
En el caso de que no exista ningun producto cargado en la tienda se recibira el mensaje correspondiente:
````json
{"message": "No se ha encontrado ningun producto en la base de datos"}
````

---
## Obtener todos los productos ordenados por precio
````http
GET https://localhost:8080/api/productos/?orden={orden}
````
Endpoint que nos permite obtener de manera directa una lista de productos, esta vez ordenados segun su precio.
La direccion del ordenamiento cambiara segun el parametro que se envie en el endpoint.

Las las direcciones posibles son ``ASC `` (Orden Ascendente) y `` DESC`` (Orden Descendente)
por defecto el ordenamiento es ascendente:
````json
[
    {
        "id": 2,
        "nombre": "producto B",
        "descripcion": "El producto N°2 de la base de datos",
        "precio": 5.00,
        "cantidad": 250
    },
    {
        "id": 1,
        "nombre": "producto A",
        "descripcion": "El producto N°1 de la base de datos",
        "precio": 10.00,
        "cantidad": 10
    },
    {
        "id": 3,
        "nombre": "producto C",
        "descripcion": "El producto N°3 de la base de datos",
        "precio": 100.00,
        "cantidad": 5
    },
    {
        "id": 4,
        "nombre": "producto A",
        "descripcion": "El producto N°4 de la base de datos",
        "precio": 200.00,
        "cantidad": 50
    }
]
````
En el caso de que no exista ningun producto cargado en la tienda se recibira el mensaje correspondiente:
````json
{"message": "No se ha encontrado ningun producto en la base de datos"}
````
---
## Obtener todos los productos con el mismo nombre
````http
GET https://localhost:8080/api/productos/?nombre={nombre}
````
Endpoint utilizado para obtener una lista de todos los productos con el mismo nombre
````json
[
    {
        "id": 1,
        "nombre": "producto A",
        "descripcion": "El producto N°1 de la base de datos",
        "precio": 10.00,
        "cantidad": 10
    },
    {
        "id": 4,
        "nombre": "producto A",
        "descripcion": "El producto N°4 de la base de datos",
        "precio": 200.00,
        "cantidad": 50
    }
]
````
En el caso de que no haya ningun producto con el nombre elegido se mostrara el mensaje correspondiente:
````json
{
    "message": "No se ha encontrado ningun producto con el nombre 'producto D'"
}
````

---
## Obtener un producto por Id
````http
GET https://localhost:8080/api/productos/{id}
````
Endpoint utilizado para obtener un producto unico a partir de su Id:
````json
{
    "id": 1,
    "nombre": "producto A",
    "descripcion": "El producto N°1 de la base de datos",
    "precio": 10.00,
    "cantidad": 10
}
````

En el caso de no existir un producto con dicho id. se le informara al cliente con el mensaje correspondiente
````json
{
    "message": "No se ha encontrado un producto con el id: '5'"
}
````

---
## Crear un producto
````http
POST https://localhost:8080/api/productos/
````
Endpoint utilizado para cargar un producto en la Base de Datos:
````json
{
    "id": 5,
    "nombre": "Producto nuevo",
    "descripcion": "El produto N°5 de la Base de Datos",
    "precio": 1600,
    "cantidad": 3000
}
````
En el caso de querer cargar a la base de datos un producto invalido, se mostraran los errores encontrados:
````json
{
    "precio": "El precio no puede ser menor a $0",
    "cantidad": "La cantidad no puede ser menor a 1",
    "nombre": "El nombre no puede ser nulo ni debe estar vacio"
}
````

---
## Actualizar un producto
````http
PATCH https://localhost:8080/api/productos/{id}
````
Endpoint utilizado para reemplazar valores de un producto encontrado por su id.
Unicamente se deben enviar los campos que se quieran cambiar en formato JSON:
````json
{
   "nombre": "Producto nuevo modificado",
   "descripcion": "el producto N°5 pero modificado",
   "precio": 2
}
````
Todos los campos a excepcion del Id son modificables. 
````json
{
    "id": 5,
    "nombre": "Producto nuevo modificado",
    "descripcion": "el producto N°5 pero modificado",
    "precio": 2,
    "cantidad": 3000
}
````
En el caso de que se pasen campos invalidos para actualizar un producto se mostraran los errores encontrados:
````json
{
    "precio": "El precio no puede ser menor a $0",
    "cantidad": "La cantidad no puede ser menor a 1"
}
````
En el caso de no existir un producto con dicho id. se le informara al cliente con el mensaje correspondiente
````json
{
    "message": "No se ha encontrado un producto con el id: '6'"
}
````
---
## Eliminar un producto
````http
DELETE https://localhost:8080/api/productos/{id}
````
Endpoint utilizado para elimina un producto encontrado por su Id.

En el caso de ser eliminado correctamente de la Base de Datos, el cliente recibira un mensaje de exito:
````json
{
    "message": "El producto con el id '5' ha sido eliminado con exito"