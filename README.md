
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
  "code": 200,
  "data": {
    "userId": "342bece1-0a8d-585c-8915-3114fbc49acd",
    "createdAt": "2024-02-29T10:32:03.0486677",
    "modifiedAt": null,
    "lastLogin": "2024-02-29T10:32:03.0486677",
    "token": null,
    "user": {
      "uuid": "342bece1-0a8d-585c-8915-3114fbc49acd",
      "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJCaWNUZXN0IiwiZXhwIjoxNzA5MjE3MTIzLCJpYXQiOjE3MDkyMTM1MjN9.X-HfCjpswcIo-i671mkq22EQjTBoyQw8ntGFNwUqSHPMv0KRIygETy8795niHuNubB2Aq4OgNVattaj_ioqkMg",
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
    "username":"BicTest",
    "password":"bic"
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
Crea un usuario y guarda su token generado en la base
##### Ejemplo de request:
```JSON
{
  "name": "BicTest",
  "email": "eeee@dom.cl",
  "password": "bicpasss"
}
```

##### Ejemplo de response :
```JSON
{
    "code": 200,
    "message": "Usuario modificado.",
    "data": {
        "id": 1,
        "uuid": "342bece1-0a8d-585c-8915-3114fbc49acd",
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJCaWNUZXN0IiwiZXhwIjoxNzA5MjMwNTQxLCJpYXQiOjE3MDkyMjY5NDF9.TxpOg10y_tyHpbB_6WI3cN7--sTRHYFGBX_kQTavv7BIcGJx_h-5osUqYQAdskIJJ-ZITuSEZkH85awLPunbZA",
        "name": "BicTest",
        "email": "eeee@dom.cl",
        "password": "",
        "phones": [
            {
                "id": 1,
                "number": "11223344",
                "cityCode": "11",
                "countryCode": "54"
            }
        ],
        "createdAt": "2024-02-29T14:05:53.747808",
        "updatedAt": "2024-02-29T14:16:10.9103256",
        "lastLogin": "2024-02-29T14:16:10.9103256",
        "isActive": true
    }
}
```
#### GetByUserToken

```http
  GET /user
```
Se busca el usuario con el token en el requestHeader y se lo devuelve

##### Ejemplo de response:
```JSON
{
    "code": 200,
    "message": "Usuario encontrado.",
    "data": {
        "id": 1,
        "uuid": "342bece1-0a8d-585c-8915-3114fbc49acd",
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJCaWNUZXN0IiwiZXhwIjoxNzA5MjMzMTE1LCJpYXQiOjE3MDkyMjk1MTV9.bE3B7XgiwzPDvCbDvckkUTU2G2AmtYyn1HtjFiaYc7122SNVkoeHQLWfxIqhXsN2Traeca2EUssd2lcQ_nMVdA",
        "name": "BicTest",
        "email": "bictest@domain.cl",
        "password": "bic",
        "phones": [
            {
                "id": 1,
                "number": "11223344",
                "cityCode": "11",
                "countryCode": "54"
            }
        ],
        "createdAt": "2024-02-29T14:58:06.874795",
        "updatedAt": "2024-02-29T14:58:35.879987",
        "lastLogin": "2024-02-29T14:58:35.740468",
        "isActive": true
    }
}
```
#### LogicDelete

```http
  DELETE /user
```
Se busca el usuario con el token en el requestHeader y se hace un borrado logico

##### Como respuesta obtendremos un 200 ok sin body


## Documentacion

Lo primero que se debe hacer es ejecutar el endpoint POST /user que creara un nuevo usuario y nos lo devolvera. Asi habilitandonos a modificarlo, obtenerlo y borrarlo logicamente. Tambien podremos loguearlo obteniendo un nuevo token.

