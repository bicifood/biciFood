# 🐳 BiciFood - Docker

## Executar

```bash
docker build -t bicifood .
docker run -d -p 8080:8080 -p 3000:3000 --name bicifood bicifood
```

## Accés

- **Frontend**: http://localhost:3000/frontend/html/TEA4/
- **Backend**:
  - API: http://localhost:8080/api/v1
  - Productes: http://localhost:8080/api/v1/products
  - Health Check: http://localhost:8080/api/v1/actuator/health
  - Swagger UI: http://localhost:8080/api/v1/swagger-ui/index.html
  - H2 Console: http://localhost:8080/api/v1/h2-console
    - JDBC URL: `jdbc:h2:mem:bicifood_db`
    - Username: `sa`
    - Password: (buit)

## Aturar

```bash
docker stop bicifood && docker rm bicifood
```
