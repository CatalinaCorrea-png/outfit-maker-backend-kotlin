# 👗 Outfit Maker — Backend (prototipo en Kotlin)

> [!IMPORTANT]
> **Este prototipo se migró a Java 21 + Spring Boot 4.** El desarrollo sigue en
> **[outfit-maker-backend-java](https://github.com/CatalinaCorrea-png/outfit-maker-backend-java)**.
>
> Este repo queda archivado como referencia. En la migración se revisó cada decisión y se
> corrigieron bugs que venían de acá: un endpoint que serializaba las entidades directo (con el
> hash de la contraseña incluido) y un refresh token vencido que devolvía 500 en vez de 401,
> entre otros. La versión en Java suma además tests en tres niveles y CI.

API de **Outfit Maker**, un armario virtual para cargar tus prendas y combinarlas en outfits.
El frontend está en [outfit-maker-frontend-react-ts](https://github.com/CatalinaCorrea-png/outfit-maker-frontend-react-ts).

![Estado](https://img.shields.io/badge/estado-archivado-6B7280?style=flat-square)
![Kotlin](https://img.shields.io/badge/Kotlin_2.3-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.3-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security_+_JWT-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![Java](https://img.shields.io/badge/JDK_21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)

## Qué está hecho

- **Modelo del dominio** (JPA + PostgreSQL). Cada **prenda** tiene una categoría ubicada en una
  zona del cuerpo (superior, inferior, calzado, abrigo, accesorio o cuerpo entero), colores,
  estampado, material, calce, temporada, nivel de formalidad y varias imágenes ordenadas. Los
  **outfits** agrupan prendas y llevan etiquetas de ocasión, estilo y ánimo.
- **Autenticación JWT sin sesión en el servidor.** El login devuelve un *access token*; el
  *refresh token* va en una cookie `httpOnly` + `SameSite=Strict` y rota en cada uso. Todas las
  rutas, salvo el login y el refresh, exigen un token válido.
- **Prendas con filtros combinables** (`GET /garments/filtered-garments`): categoría, nombre,
  marca, colores, estampado, material, calce, formalidad, temporada y activas/archivadas, con
  paginación y orden. Los filtros se arman con *JPA Specifications*, así que cada uno es una
  pieza independiente que se suma solo si viene en la request.
- **Outfits paginados** (`GET /outfits/filtered-outfits`).
- **Errores con código estable.** Cada error responde `{ status, code, error, detail, timestamp }`
  con un `code` fijo (por ejemplo `AUTH_INVALID_CREDENTIALS`). El frontend usa ese código para
  mostrar el mensaje en el idioma del usuario.
- **Datos de ejemplo** al arrancar: usuarios, categorías y prendas.

## Todavía no

- **Registro:** el endpoint `POST /auth/register` existe, pero todavía no está habilitado como
  ruta pública.
- **Crear y editar** prendas y outfits: por ahora la API es solo de lectura.
- Tests.

## Cómo correrlo

```bash
docker compose up -d        # PostgreSQL en :5433 (y pgAdmin en :5050)

export JWT_KEY="una-clave-de-al-menos-32-caracteres"   # obligatoria, sin valor por defecto
./gradlew bootRun           # API en http://localhost:8080
```

Por defecto, CORS acepta `http://localhost:5173` (el frontend en modo desarrollo). Se puede
cambiar con la propiedad `cors.allowed-origins`.
