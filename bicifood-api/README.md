# 🚴‍♂️ BiciFood API REST - Java 21 LTS

API REST desenvolupada amb **Spring Boot 3.3.5** i **Java 21 LTS** per la plataforma de menjar a domicili sostenible BiciFood.

## 🚀 Tecnologies Utilitzades

- **Java 21 LTS** ⚡ amb Virtual Threads (Project Loom)
- **Spring Boot 3.3.5** (última versió estable)
- **Spring Data JPA** (ORM amb Hibernate)
- **MySQL 8.0+ / H2** (Base de dades)
- **Spring Security 6** (Autenticació/Autorització)
- **Swagger/OpenAPI 3** (Documentació interactiva)
- **Maven 3.9+** (Gestió de dependències)

## 📋 Funcionalitats

### ✅ Implementat
- 🏷️ **Categories**: CRUD complet per categories de productes
- 🍽️ **Productes**: CRUD complet amb cerques i filtres
- 📚 **Documentació Swagger**: Interfície web per provar l'API

### 🔄 Per implementar
- 👥 **Usuaris**: Registre, login i gestió de perfils
- 🛒 **Cistella**: Gestió de cistella de compra
- 📦 **Comandes**: Creació i seguiment de comandes
- 🚴‍♂️ **Lliuraments**: Gestió de lliuraments amb bicicletes
- 🔐 **Autenticació JWT**: Sistema complet de seguretat

## 🛠️ Instal·lació i Configuració

### 1. Prerrequisits
```bash
# Java 21 LTS (recomanat per Virtual Threads)
java -version
# Expected: openjdk version "21" 2023-09-19 LTS

# Maven 3.9+
mvn -version

# MySQL 8.0+ (opcional - H2 inclòs per desenvolupament)
mysql --version
```

### 2. Configurar Base de Dades
```sql
-- Crear base de dades
CREATE DATABASE bicifood_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Importar dades
mysql -u root -p bicifood_db < ../bicifood_db_v5.0/bicifood_db_v5.0.sql
```

### 3. Configurar Aplicació
Edita `src/main/resources/application.properties`:
```properties
# Actualitza aquestes credencials
spring.datasource.username=el_teu_usuari
spring.datasource.password=la_teva_contrasenya
spring.datasource.url=jdbc:mysql://localhost:3306/bicifood_db
```

### 4. Executar Aplicació

#### 🚀 **MODE FÀCIL (Recomanat) - Script Automàtic**
```bash
# 1. Anar al directori del projecte
cd bicifood-api

# 2. Executar script automàtic (tot en un!)
./start.sh
```

**El script `start.sh` fa automàticament:**
- ✅ Neteja processos anteriors
- ✅ Compila l'aplicació
- ✅ Executa en background
- ✅ Verifica que funciona
- ✅ Mostra URLs disponibles

#### 🗄️ **MODE MANUAL (Desenvolupament amb H2)**
```bash
# 1. Compilar i generar JAR executable
mvn clean package -DskipTests

# 2. Executar l'aplicació
java -jar target/bicifood-api-1.0.0.jar
```

#### 🏢 **MODE PRODUCCIÓ (amb MySQL)**
```bash
# 1. Assegurar-se que MySQL està executant-se
# 2. Crear base de dades (veure secció anterior)
# 3. Modificar application.properties per MySQL
# 4. Executar script automàtic:
./start.sh
```

#### 🔧 **Resolució de Problemes**
```bash
# Si el port 8080 està ocupat:
lsof -ti:8080 | xargs kill -9

# Si mvn spring-boot:run dona error, usar SEMPRE:
./start.sh
# o bé:
mvn clean package -DskipTests && java -jar target/bicifood-api-1.0.0.jar
```

## 📡 Endpoints Disponibles

### 🏷️ Categories
```
GET    /api/v1/categories              - Llistar totes les categories
GET    /api/v1/categories/{id}         - Obtenir categoria per ID
GET    /api/v1/categories/nom/{nom}    - Buscar categoria per nom
POST   /api/v1/categories              - Crear nova categoria
PUT    /api/v1/categories/{id}         - Actualitzar categoria
DELETE /api/v1/categories/{id}         - Eliminar categoria
```

### 🍽️ Productes
```
GET    /api/v1/productes                       - Llistar tots els productes
GET    /api/v1/productes/{id}                  - Obtenir producte per ID
GET    /api/v1/productes/categoria/{id}        - Productes per categoria
GET    /api/v1/productes/cerca?nom=text        - Buscar productes per nom
GET    /api/v1/productes/disponibles           - Productes amb stock
GET    /api/v1/productes/preu?preuMin=X&preuMax=Y - Productes per rang de preu
GET    /api/v1/productes/populars              - Productes populars
POST   /api/v1/productes                       - Crear nou producte
PUT    /api/v1/productes/{id}                  - Actualitzar producte
DELETE /api/v1/productes/{id}                  - Eliminar producte
```

## 📚 Documentació Swagger

### 🎯 **Accés a Swagger UI**

1. **Executar l'aplicació** (seguir passos anteriors)
2. **Verificar que està funcionant**:
   ```bash
   curl -s http://localhost:8080/api/v1/categories
   ```
3. **Accedir a Swagger UI**:
   🌐 **Swagger UI**: http://localhost:8080/swagger-ui.html
   📄 **API Docs**: http://localhost:8080/api-docs

### ✅ **Què trobaràs a Swagger:**
- 🏷️ **Categories API**: CRUD complet per categories
- 🍽️ **Productes API**: CRUD amb cerca i filtres
- 🧪 **Interfície de proves**: Executa requests directament
- 📖 **Documentació automàtica**: Paràmetres i respostes

## ⚡ **Funcionalitats Java 21 LTS**

### 🚀 **Virtual Threads (Project Loom)**
- **Rendiment excepcional**: Gestió de milers de connexions simultànies
- **Baixa latència**: Operacions I/O no bloquejants
- **Escalabilitat**: Menys consum de memòria per thread
- **Transparència**: Zero canvis en codi existent

### 🎯 **Records Moderns**
- **DTOs immutables**: `CategoriaDTO` i `ProducteDTO` amb Records
- **Validació automàtica**: Constructor compacts amb validacions
- **Serialització JSON**: Suport automàtic per Spring Boot

### 📦 **Pattern Matching**
- **Switch expressions**: Sintaxi moderna i eficient
- **Type checks**: Verificacions de tipus més segures
- **Destructuring**: Extracció de dades simplificada

## 🛡️ **Estabilitat i Millores**

### 🎯 **Millores d'Estabilitat Implementades:**

#### ✅ **Configuració CORS Simplificada**
- Eliminada duplicació de configuracions
- Configuració única i consistent
- No més conflictes entre `allowCredentials` i `allowedOrigins`

#### ✅ **Script Automàtic `start.sh`**
- Neteja automàtica de processos anteriors
- Compilació i execució automatitzada  
- Verificació que l'API respon correctament
- Instruccions clares per aturar l'aplicació

#### ✅ **Serialització JSON Optimitzada**
- `FetchType.EAGER` per evitar lazy loading issues
- Correcta serialització de relacions Categoria-Producte
- JSON consistent i sense errors

#### ✅ **Configuració Minimal**
- Properties simplificat amb només configuracions essencials
- Menys punts de fallida potencials
- Logging optimitzat per desenvolupament

### 🚀 **Resultats:**
- **Execució fiable**: Un sol comando `./start.sh`
- **Zero conflictes**: Configuracions harmonitzades
- **Debugging fàcil**: Logs clars i concisos
- **APIs estables**: Endpoints sempre disponibles

### 🗄️ **Consola H2 (Base de dades)**
Si uses H2 (mode desenvolupament):
🔗 **H2 Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:bicifood`
- **User**: `sa`
- **Password**: *(deixar buit)*

## ✅ Verificació de la Instal·lació

### 1. **Comprovar que l'aplicació està funcionant**
```bash
# Test bàsic de connexió
curl -s http://localhost:8080/api/v1/categories

# Si retorna [] (llista buida) = ✅ API funcionant
# Si retorna error de connexió = ❌ Revisar passos anteriors
```

### 2. **Accés als serveis**
- ✅ **API REST**: http://localhost:8080/api/v1/categories
- ✅ **Swagger UI**: http://localhost:8080/swagger-ui.html
- ✅ **H2 Console**: http://localhost:8080/h2-console (només mode dev)

## 🧪 Exemples d'Ús amb cURL

### Obtenir totes les categories
```bash
curl -X GET http://localhost:8080/api/v1/categories
```

### Crear nova categoria
```bash
curl -X POST http://localhost:8080/api/v1/categories \
  -H "Content-Type: application/json" \
  -d '{"nomCat": "PIZZES"}'
```

### Buscar productes per categoria
```bash
curl -X GET http://localhost:8080/api/v1/productes/categoria/1
```

### Crear nou producte
```bash
curl -X POST http://localhost:8080/api/v1/productes \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Pizza Margherita",
    "preu": 12.50,
    "descripcio": "Pizza clàssica amb tomàquet, mozzarella i alfàbrega",
    "stock": 10,
    "categoria": {"idCategoria": 1}
  }'
```

## 🚨 Solució de Problemes Comuns

### ❌ **Error: "Port 8080 was already in use"**
```bash
# Matar procés que ocupa el port
lsof -ti:8080 | xargs kill -9

# Tornar a executar
java -jar target/bicifood-api-1.0.0.jar
```

### ❌ **Error: "No plugin found for prefix 'spring-boot'"**
```bash
# NO usar: mvn spring-boot:run
# Usar SEMPRE:
mvn clean package -DskipTests
java -jar target/bicifood-api-1.0.0.jar
```

### ❌ **Error: "Unable to access jarfile"**
```bash
# Assegurar-se d'estar al directori correcte
cd bicifood-api

# Recompilar
mvn clean package -DskipTests

# Verificar que existeix el JAR
ls -la target/bicifood-api-1.0.0.jar
```

### ❌ **Swagger retorna 404**
- ✅ **URL correcta**: http://localhost:8080/swagger-ui.html
- ❌ **URL incorrecta**: http://localhost:8080/api/v1/swagger-ui.html

### ❌ **API retorna errors de CORS**
```bash
# Verificar que CORS està configurat a application.properties:
# bicifood.cors.allowed-origins=http://localhost:3000,http://localhost:8080
```

---

## 🏗️ Arquitectura del Projecte

```
bicifood-api/
├── start.sh                          # 🚀 Script d'execució automàtic
├── src/main/java/com/bicifood/api/
│   ├── BicifoodApiApplication.java   # Classe principal Spring Boot
│   ├── model/                        # Entitats JPA (Base de dades)
│   │   ├── Categoria.java           #   - Categories (CARNS, PEIXOS...)
│   │   └── Producte.java            #   - Productes del menú
│   ├── repository/                   # Capa d'accés a dades (Spring Data)
│   │   ├── CategoriaRepository.java #   - CRUD Categories
│   │   └── ProducteRepository.java  #   - CRUD Productes + cerques
│   ├── controller/                   # Controllers REST (APIs)
│   │   ├── CategoriaController.java #   - /api/v1/categories
│   │   └── ProducteController.java  #   - /api/v1/productes
│   └── config/                      # Configuracions del sistema
│       ├── SecurityConfig.java      #   - Seguretat (permissiu per dev)
│       └── CorsConfig.java          #   - CORS (simplificat i estable)
├── src/main/resources/
│   └── application.properties       # Configuració (H2, Swagger, logs)
├── pom.xml                         # Dependències Maven
├── test-api.html                   # Interfície de test HTML
└── README.md                       # Documentació completa
```

### 🔄 **Flux de l'aplicació:**
```
Client/Frontend → Controller → Repository → Database (H2/MySQL)
     ↑                ↓
   JSON Response ← Service Logic
```

---

## 📊 **Estat Actual del Projecte**

### ✅ **Completament Funcional:**
- 🏗️ **API REST estable** amb Spring Boot 3.2.0
- 🗄️ **Base de dades H2** (desenvolupament) + suport MySQL
- 📚 **Swagger UI** completament integrat i funcional
- 🔒 **Seguretat bàsica** configurada (Spring Security)
- 🌐 **CORS optimitzat** per frontend integration
- 🚀 **Script automàtic** per execució fiable

### 🎯 **Endpoints Disponibles:**
- **Categories**: 5 endpoints (GET, POST, PUT, DELETE, cerca)
- **Productes**: 8+ endpoints (CRUD + filtres avançats)
- **Swagger**: Documentació interactiva completa

### 🛠️ **Ús Immediat:**
```bash
cd bicifood-api
./start.sh
# → API disponible a http://localhost:8080/swagger-ui.html
```

### 🚀 **Següents Passos Suggerits:**
1. **Autenticació JWT** - Sistema complet d'usuaris
2. **Entitat Comandes** - Gestió de cistella i checkout  
3. **Integració Frontend** - Connectar amb l'HTML existent
4. **Base de dades MySQL** - Migració a producció
5. **Testing** - Tests unitaris i d'integració

**L'API està preparada per desenvolupament actiu i integració amb frontend!** 🚴‍♂️
├── repository/                    # Repositories JPA
│   ├── CategoriaRepository.java
│   └── ProducteRepository.java
├── service/                      # Lògica de negoci (per implementar)
├── config/                       # Configuracions
└── middleware/                   # Middleware personalitzat
```

## 🔄 Següents Passes

1. **Implementar entitats restants**: Usuari, Comanda, LliniaComanda, etc.
2. **Afegir Spring Security**: Sistema d'autenticació JWT
3. **Crear serveis de negoci**: Lògica de cistella i comandes
4. **Afegir validacions**: Validació de dades més robusta
5. **Implementar testing**: Tests unitaris i d'integració
6. **Configurar CORS**: Per integració amb frontend
7. **Gestió d'errors**: Millor gestió d'excepcions

## 📞 Suport

Per dubtes o problemes amb l'API, contacta amb l'equip de desenvolupament de BiciFood.

---
🌱 **BiciFood** - Menjar sostenible a casa teva amb bicicleta 🚴‍♂️