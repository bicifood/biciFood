# 🐳 BiciFood - Docker

## Construcció i Primera Execució

```bash
# 1. Construir la imatge
docker build -t bicifood .

# 2. Crear i executar el contenidor (primera vegada)
docker run -d -p 8080:8080 -p 3000:3000 --name bicifood bicifood
```

## Gestió del Contenidor

### Iniciar contenidor existent
```bash
docker start bicifood
```

### Aturar contenidor
```bash
docker stop bicifood
```

### Veure estat del contenidor
```bash
# Contenidors en execució
docker ps

# Tots els contenidors (inclosos els parats)
docker ps -a
```

### Eliminar contenidor
```bash
docker rm bicifood
```

### Veure logs del contenidor
```bash
docker logs bicifood
```

## ⚠️ Diferència Important

- **`docker run`**: Crea un **NOU** contenidor. Només s'usa la primera vegada.
- **`docker start`**: Arrenca un contenidor **EXISTENT** que està parat.

Si obtens l'error *"The container name is already in use"*, utilitza `docker start bicifood` en lloc de `docker run`.

## Accés

- **Frontend**: http://localhost:3000/frontend/html/TEA5/
- **Backend**:
  - API: http://localhost:8080/api/v1
  - Productes: http://localhost:8080/api/v1/products
  - Health Check: http://localhost:8080/api/v1/actuator/health
  - Swagger UI: http://localhost:8080/api/v1/swagger-ui/index.html
  - H2 Console: http://localhost:8080/api/v1/h2-console
    - JDBC URL: `jdbc:h2:mem:bicifood_db`
    - Username: `sa`
    - Password: (buit)

## Neteja Completa

```bash
# Aturar i eliminar contenidor
docker stop bicifood && docker rm bicifood

# Eliminar imatge
docker rmi bicifood
```

