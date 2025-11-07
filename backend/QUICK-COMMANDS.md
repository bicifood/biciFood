# 🚴‍♂️ BiciFood - Comandes Ràpides per l'Equip

## 🚀 Començar Ràpidament

```bash
# 1. Setup complet (primera vegada)
./dev-setup.sh

# 2. Només iniciar l'aplicació (si ja tens MySQL)
mvn spring-boot:run

# 3. Aturar tot i netejar
./cleanup.sh
```

## 🗄️ Base de Dades

```bash
# Iniciar MySQL amb Docker
docker-compose up -d mysql

# Connectar a MySQL
mysql -h localhost -u bicifood_user -pbicifood_password bicifood_db

# Importar dades de prova
mysql -h localhost -u bicifood_user -pbicifood_password bicifood_db < sample-data.sql

# PhpMyAdmin (UI visual)
open http://localhost:8081
```

## 📊 Monitorització

```bash
# Logs de l'aplicació
tail -f logs/application.log

# Status dels contenidors
docker-compose ps

# Health check
curl http://localhost:8080/api/actuator/health

# Info de l'aplicació
curl http://localhost:8080/api/actuator/info
```

## 🧪 Testing APIs

```bash
# Llistar productes
curl http://localhost:8080/api/productes

# Obtenir producte específic  
curl http://localhost:8080/api/productes/1

# Crear nou producte
curl -X POST http://localhost:8080/api/productes \
  -H "Content-Type: application/json" \
  -d '{"nom":"Producte Test","descripcio":"Test","preu":10.50,"stock":5,"categoria":{"id":1}}'
```

## 🔧 Desenvolupament

```bash
# Compilar sense executar
mvn clean compile

# Executar tests
mvn test

# Empaquetar
mvn clean package -DskipTests

# Executar amb perfil específic
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 📖 Documentació

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/v3/api-docs
- **README Complet**: [README.md](README.md)

## 🆘 Solució de Problemes

```bash
# Port ocupat (8080)
lsof -ti:8080 | xargs kill -9

# Port ocupat (3306) 
lsof -ti:3306 | xargs kill -9

# Reiniciar Docker
docker-compose down && docker-compose up -d

# Netejar tot Maven
mvn clean install -U

# Logs detallats
mvn spring-boot:run -Dlogging.level.com.bicifood=DEBUG
```

## 🔐 Usuaris de Prova

| Email | Password | Rol |
|-------|----------|-----|
| joan@bicifood.cat | bicifood123 | CLIENT |
| maria@bicifood.cat | bicifood123 | CLIENT |
| pau.repartidor@bicifood.cat | bicifood123 | REPARTIDOR |
| admin@bicifood.cat | admin123 | ADMIN |

## 🌐 URLs Importants

- **API Base**: http://localhost:8080/api
- **Swagger**: http://localhost:8080/api/swagger-ui.html  
- **PhpMyAdmin**: http://localhost:8081
- **Health**: http://localhost:8080/api/actuator/health