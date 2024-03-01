# BCI - Ejercicio de Java para especialista de Integracion

Ejercicio donde se puede crear, modificar, obetener y borrar logicamente usuarios.

## API Reference

#### Register

```http
  POST /user
```

Crea un usuario y guarda su token generado en la base

##### Ejemplo de request:

```JSON
{
  "name": "BicTest",
  "email": "bictest@domain.cl",
  "password": "bic",
  "phones": [
    {
      "number": "11223344",
      "cityCode": "11",
      "countryCode": "54"
    }
  ]
}
```

##### Ejemplo de response :

```JSON
{
  "data": {
    "userId": "342bece1-0a8d-585c-8915-3114fbc49acd",
    "createdAt": "2024-03-01T16:11:38.7459109",
    "modifiedAt": null,
    "lastLogin": "2024-03-01T16:11:38.7459109",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJCaWNUZXN0IiwiZXhwIjoxNzA5MzIzODk4LCJpYXQiOjE3MDkzMjAyOTh9.awBqio9SjVh-dW8GN8htK8ShUHFDEDtmhBgvG3gU0hjm7R-NLXNcGIqs81eNAvTu6T0aW1RZ2nagAVmHhjN1Rw",
    "user": {
      "uuid": "342bece1-0a8d-585c-8915-3114fbc49acd",
      "name": "BicTest",
      "email": "bictest@domain.cl",
      "phones": [
        {
          "number": "11223344",
          "cityCode": "11",
          "countryCode": "54"
        }
      ]
    },
    "active": true
  }
}
```

#### Authenticate

```http
  POST /user/authenticate
```

Loguea al usuario y guarda su nuevo token generado en la base y lo retorna

##### Ejemplo de request:

```JSON
{
  "email": "bictest@domain.cl",
  "password": "bic"
}
```

##### Ejemplo de response :

```JSON
{
  "jwtToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJCaWNUZXN0IiwiZXhwIjoxNzA5MjMzMTE1LCJpYXQiOjE3MDkyMjk1MTV9.bE3B7XgiwzPDvCbDvckkUTU2G2AmtYyn1HtjFiaYc7122SNVkoeHQLWfxIqhXsN2Traeca2EUssd2lcQ_nMVdA"
}
```

#### ModifyEmailOrPasword

```http
  PUT /user
```

Modifuca un usuario . Se necesita ser autenticado con token por requestHeader.

##### Ejemplo de request:

```JSON
{
  "email": "anotherEmail@domain.cl",
  "password": "bicpasss"
}
```

##### Ejemplo de response :

```JSON
{
  "message": "Usuario modificado.",
  "data": {
    "id": 1,
    "uuid": "342bece1-0a8d-585c-8915-3114fbc49acd",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiaWN0ZXN0QGRvbWFpbi5jbCIsImV4cCI6MTcwOTMyMzk3NywiaWF0IjoxNzA5MzIwMzc3fQ.egGExdNWClhVzU052wabm8j8dk3csg8AvBw9uEH-CYZJeVppKhO0IR_Pybn0gtfzUephUb1fE_1w4pBIkcTfFw",
    "name": "BicTest",
    "email": "anotherEmail@domain.cl",
    "password": "bicpasss",
    "phones": [
      {
        "id": 1,
        "number": "11223344",
        "cityCode": "11",
        "countryCode": "54"
      }
    ],
    "createdAt": "2024-03-01T16:11:38.745911",
    "updatedAt": "2024-03-01T16:16:27.6170912",
    "lastLogin": "2024-03-01T16:16:27.6170912",
    "isActive": true
  }
} 
```

#### GetByUserToken

```http
  GET /user
```

Se busca el usuario con el token en el requestHeader y retornandolo.

##### Ejemplo de response:

```JSON
{
  "message": "Usuario encontrado.",
  "data": {
    "id": 1,
    "uuid": "342bece1-0a8d-585c-8915-3114fbc49acd",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiaWN0ZXN0QGRvbWFpbi5jbCIsImV4cCI6MTcwOTMyMzk3NywiaWF0IjoxNzA5MzIwMzc3fQ.egGExdNWClhVzU052wabm8j8dk3csg8AvBw9uEH-CYZJeVppKhO0IR_Pybn0gtfzUephUb1fE_1w4pBIkcTfFw",
    "name": "BicTest",
    "email": "anotherEmail@domain.cl",
    "password": "bicpasss",
    "phones": [
      {
        "id": 1,
        "number": "11223344",
        "cityCode": "11",
        "countryCode": "54"
      }
    ],
    "createdAt": "2024-03-01T16:11:38.745911",
    "updatedAt": "2024-03-01T16:16:27.617091",
    "lastLogin": "2024-03-01T16:16:27.617091",
    "isActive": true
  }
}
```

#### LogicDelete

```http
  DELETE /user
```

Se busca el usuario con el token en el requestHeader y se hace un borrado logico.

##### Como respuesta obtendremos un 200 ok sin body

## Documentacion

Lo primero que se debe hacer es ejecutar el endpoint POST /user que creara un nuevo usuario y nos lo devolvera. Asi
habilitandonos a modificarlo, obtenerlo y borrarlo logicamente. Tambien podremos loguearlo obteniendo un nuevo token.

Tambien dentro de la carpeta resources he adjuntado una collection de postman para realizar las pruebas facilmente.
