# 🚴‍♂️ BiciFood API - Backend

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.8.9-blue.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

API REST per l'aplicació BiciFood - Plataforma de lliurament de menjar amb bicicletes a Catalunya.

## 📋 Taula de Continguts

- [Arquitectura del Projecte](#arquitectura-del-projecte)
- [Prerequisits](#prerequisits)
- [Instal·lació i Configuració](#installació-i-configuració)
- [Executar l'Aplicació](#executar-laplicació)
- [Estructura del Projecte](#estructura-del-projecte)
- [Entitats de la Base de Dades](#entitats-de-la-base-de-dades)
- [API Endpoints](#api-endpoints)
- [Configuració de la Base de Dades](#configuració-de-la-base-de-dades)
- [Testing](#testing)
- [Documentació API](#documentació-api)
- [Roadmap - Què falta implementar](#roadmap---què-falta-implementar)

---

## 🏗️ Arquitectura del Projecte

```
Backend (Spring Boot 3.3.5)
├── API REST Controllers
├── Business Logic Services  
├── JPA Repositories
├── MySQL Database
└── Spring Security (JWT)
```

**Stack Tecnològic:**
- **Java 21 LTS** - Llenguatge de programació
- **Spring Boot 3.3.5** - Framework d'aplicacions
- **Spring Data JPA** - ORM per a base de dades
- **Spring Security** - Seguretat i autenticació
- **MySQL 8.0+** - Base de dades relacional
- **Maven 3.8.9** - Gestió de dependències
- **Swagger/OpenAPI** - Documentació d'API
- **JWT** - Autenticació amb tokens
- **ModelMapper** - Mapejat entitats-DTOs

---

## � Prerequisits

Abans de començar, assegureu-vos de tenir instal·lat:

### Software Requerit

1. **Java 21 LTS**
   ```bash
   java -version
   # Ha de mostrar: openjdk version "21.x.x"
   ```

2. **Maven 3.8.9+**
   ```bash
   mvn -version
   # Ha de mostrar: Apache Maven 3.8.9+
   ```

3. **MySQL 8.0+**
   ```bash
   mysql --version
   # Ha de mostrar: mysql Ver 8.0.x
   ```

4. **Git**
   ```bash
   git --version
   ```

### Instal·lació de Prerequisits (macOS)

```bash
# Instal·lar Java 21 amb Homebrew
brew install openjdk@21

# Instal·lar Maven
brew install maven

# Instal·lar MySQL
brew install mysql
brew services start mysql
```

---

## ⚙️ Instal·lació i Configuració

### 1. Clonar el Repositori
```bash
git clone https://github.com/bicifood/biciFood.git
cd biciFood/backend
```

### 2. Configurar la Base de Dades

#### Crear la Base de Dades
```bash
# Connectar a MySQL
mysql -u root -p

# Crear la base de dades
CREATE DATABASE bicifood_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Crear usuari per l'aplicació (opcional)
CREATE USER 'bicifood_user'@'localhost' IDENTIFIED BY 'bicifood_password';
GRANT ALL PRIVILEGES ON bicifood_db.* TO 'bicifood_user'@'localhost';
FLUSH PRIVILEGES;

# Sortir de MySQL
EXIT;
```

#### Importar l'Schema de la Base de Dades
```bash
# Des del directori root del projecte
mysql -u root -p bicifood_db < bicifood_db_v5.0/bicifood_db_v5.0.sql
```

### 3. Configurar application.properties

Editar el fitxer `src/main/resources/application.properties`:

```properties
# Configuració de la Base de Dades
spring.datasource.url=jdbc:mysql://localhost:3306/bicifood_db?useSSL=false&serverTimezone=Europe/Madrid&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuració JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# Configuració del Servidor
server.port=8080
server.servlet.context-path=/api

# Configuració de Logging
logging.level.com.bicifood=DEBUG
logging.level.org.springframework.security=DEBUG

# Configuració JWT (per implementar)
jwt.secret=bicifood-secret-key-2024
jwt.expiration=86400000

# Configuració de CORS
cors.allowed-origins=http://localhost:3000,http://localhost:4200
```

---

## 🚀 Executar l'Aplicació

### Opció 1: Maven Spring Boot Plugin
```bash
# Des del directori backend/
mvn clean compile
mvn spring-boot:run
```

### Opció 2: Jar Executable
```bash
# Construir el projecte
mvn clean package -DskipTests

# Executar el JAR
java -jar target/bicifood-api-1.0.0.jar
```

### Opció 3: Script d'Execució
```bash
# Fer executable l'script
chmod +x run.sh

# Executar l'script
./run.sh
```

### Verificar que Funciona
```bash
# L'aplicació s'hauria d'executar a:
http://localhost:8080/api

# Endpoint de salut:
curl http://localhost:8080/api/actuator/health

# Documentació Swagger:
http://localhost:8080/api/swagger-ui.html
```

---

## 📁 Estructura del Projecte

```
backend/
├── src/main/java/com/bicifood/api/
│   ├── BiciFoodApiApplication.java      # Classe principal Spring Boot
│   ├── config/
│   │   ├── AppConfig.java               # Configuració beans (ModelMapper)
│   │   └── SecurityConfig.java          # Configuració Spring Security
│   ├── entity/                          # Entitats JPA
│   │   ├── Usuari.java                  # Entitat d'usuaris
│   │   ├── Rol.java                     # Rols d'usuari (CLIENT, REPARTIDOR, ADMIN)
│   │   ├── Categoria.java               # Categories de productes
│   │   ├── Producte.java                # Productes del catàleg
│   │   ├── Comanda.java                 # Comandes dels clients
│   │   ├── EstatComanda.java            # Estats de comandes (PENDENT, EN_RUTA...)
│   │   ├── LiniaComanda.java            # Línies de comandes (producte + quantitat)
│   │   └── Lliurament.java              # Informació de lliuraments
│   ├── repository/                      # Repositoris Spring Data JPA
│   │   ├── UsuariRepository.java        # Queries d'usuaris
│   │   ├── RolRepository.java           # Queries de rols
│   │   ├── CategoriaRepository.java     # Queries de categories
│   │   ├── ProducteRepository.java      # Queries de productes
│   │   ├── ComandaRepository.java       # Queries de comandes
│   │   ├── EstatComandaRepository.java  # Queries d'estats
│   │   ├── LiniaComandaRepository.java  # Queries de línies
│   │   └── LliuramentRepository.java    # Queries de lliuraments
│   ├── service/                         # Lògica de negoci
│   │   ├── UsuariService.java           # Servei d'usuaris
│   │   ├── ProducteService.java         # Servei de productes
│   │   └── ComandaService.java          # Servei de comandes
│   └── controller/                      # Controllers REST
│       └── ProducteController.java      # API REST de productes
├── src/main/resources/
│   └── application.properties           # Configuració de l'aplicació
├── src/test/                           # Tests unitaris i d'integració
├── pom.xml                             # Configuració Maven
├── run.sh                              # Script d'execució
└── README.md                           # Aquesta documentació
```

---

## 🗄️ Entitats de la Base de Dades

### Diagrama d'Entitats
```
Usuari (1) ←→ (N) Comanda (N) ←→ (1) EstatComanda
   ↕                 ↕
  Rol             LiniaComanda (N) ←→ (1) Producte (N) ←→ (1) Categoria
   ↕                 ↕
Lliurament      [quantitat, preu_unitari]
```

### 1. **Usuari** - Gestió d'usuaris del sistema
```java
- id: Integer (PK)
- nom: String
- cognoms: String  
- email: String (UNIQUE)
- telefon: String
- adreca: String
- passwordHash: String
- punts: Integer (sistema de fidelització)
- dataRegistre: LocalDateTime
- actiu: Boolean
- rol: Rol (FK) // CLIENT, REPARTIDOR, ADMIN
```

### 2. **Rol** - Tipus d'usuaris
```java
- id: Integer (PK)
- nom: String // CLIENT, REPARTIDOR, ADMIN
- descripcio: String
```

### 3. **Categoria** - Categories de productes
```java
- id: Integer (PK)  
- nom: String // CARNS, PEIXOS, BEGUDES, POSTRES...
- descripcio: String
- activa: Boolean
```

### 4. **Producte** - Catàleg de productes
```java
- id: Integer (PK)
- nom: String
- descripcio: String
- preu: BigDecimal
- stock: Integer
- disponible: Boolean
- categoria: Categoria (FK)
- dataCreacio: LocalDateTime
```

### 5. **Comanda** - Comandes dels clients
```java
- id: Integer (PK)
- client: Usuari (FK)
- dataComanda: LocalDateTime
- totalComanda: BigDecimal
- observacions: String
- estat: EstatComanda (FK)
```

### 6. **EstatComanda** - Estats de les comandes
```java
- id: Integer (PK)
- nom: String // PENDENT, PREPARANT, EN_RUTA, LLIURADA, CANCEL·LADA
- descripcio: String
```

### 7. **LiniaComanda** - Línies de comandes (productes dins una comanda)
```java
- id: Integer (PK)
- comanda: Comanda (FK)
- producte: Producte (FK)
- quantitat: Integer
- preuUnitari: BigDecimal
- subtotal: BigDecimal
```

### 8. **Lliurament** - Gestió de lliuraments
```java
- id: Integer (PK)
- comanda: Comanda (FK)
- repartidor: Usuari (FK)
- adrecaLliurament: String
- dataLliurament: LocalDateTime
- observacions: String
```

---

## 🌐 API Endpoints

### Productes API (Implementat ✅)
```
GET    /api/productes                    # Llistar tots els productes (paginat)
GET    /api/productes/{id}               # Obtenir producte per ID
POST   /api/productes                    # Crear nou producte
PUT    /api/productes/{id}               # Actualitzar producte
DELETE /api/productes/{id}               # Eliminar producte

# Endpoints amb filtres
GET    /api/productes/categoria/{categoriaId}    # Productes per categoria
GET    /api/productes/cerca?q={terme}            # Cercar productes
GET    /api/productes/disponibles               # Només productes disponibles
```

### APIs Pendents d'Implementar
```
# Usuaris
POST   /api/auth/register               # Registrar nou usuari
POST   /api/auth/login                  # Login usuari
GET    /api/usuaris/perfil              # Perfil usuari actual
PUT    /api/usuaris/perfil              # Actualitzar perfil

# Comandes  
GET    /api/comandes                    # Historial de comandes
POST   /api/comandes                    # Crear nova comanda
GET    /api/comandes/{id}               # Detall de comanda
PUT    /api/comandes/{id}/estat         # Actualitzar estat comanda

# Categories
GET    /api/categories                  # Llistar categories
```

---

## 🔧 Configuració de la Base de Dades

### Configuració per Desenvolupament
```properties
# application-dev.properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
```

### Configuració per Producció
```properties  
# application-prod.properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.org.hibernate.SQL=WARN
```

### Pool de Connexions
```properties
# Configuració HikariCP (per defecte a Spring Boot)
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=600000
```

---

## 🧪 Testing

### Executar Tests
```bash
# Tots els tests
mvn test

# Tests d'una classe específica
mvn test -Dtest=ProducteServiceTest

# Tests d'integració
mvn test -Dtest=**/*IntegrationTest
```

### Configuració de Tests
- **H2 Database** per tests en memòria
- **TestContainers** per tests d'integració amb MySQL
- **MockMvc** per tests de controllers
- **@DataJpaTest** per tests de repositoris

---

## 📖 Documentació API

### Swagger UI
Un cop l'aplicació estigui funcionant:
```
http://localhost:8080/api/swagger-ui.html
```

### OpenAPI JSON
```
http://localhost:8080/api/v3/api-docs
```

---

## 🚧 Roadmap - Què Falta Implementar

### ⚠️ BACKEND - Funcionalitats Pendents

#### 1. **Controllers REST** (Prioritat Alta)
- [ ] **UsuariController** - Gestió d'usuaris i perfils
- [ ] **AuthController** - Autenticació (login/register)
- [ ] **ComandaController** - Gestió de comandes
- [ ] **CategoriaController** - Gestió de categories
- [ ] **LliuramentController** - Gestió de lliuraments

#### 2. **Sistema d'Autenticació JWT** (Prioritat Alta)
```java
// Classes a implementar:
- JwtUtils.java          // Utilitats per generar/validar tokens
- JwtAuthFilter.java     // Filtre per validar JWT
- AuthService.java       // Servei d'autenticació
- UserDetailsImpl.java   // Implementació UserDetails
```

#### 3. **DTOs (Data Transfer Objects)** (Prioritat Mitjana)
```java
// DTOs a crear:
- UsuariDto.java, UsuariCreateDto.java
- ProducteDto.java, ProducteCreateDto.java  
- ComandaDto.java, ComandaCreateDto.java
- LoginRequest.java, LoginResponse.java
```

#### 4. **Gestió d'Excepcions** (Prioritat Mitjana)
```java
// Classes a implementar:
- GlobalExceptionHandler.java    // Manejo global d'excepcions
- BusinessException.java         // Excepcions de negoci
- ValidationException.java       // Excepcions de validació
```

#### 5. **Validacions** (Prioritat Mitjana)
- Validacions de camp amb `@Valid` i Bean Validation
- Validacions de negoci personalitzades
- Missatges d'error en català

#### 6. **Tests** (Prioritat Baixa)
- Tests unitaris per tots els services
- Tests d'integració per controllers
- Tests de seguretat i autenticació

---

### 🎨 FRONTEND - Aplicació Web

#### Tecnologies Recomanades
```javascript
// Opció 1: React + TypeScript
- React 18+ amb TypeScript
- Material-UI o Tailwind CSS
- Axios per APIs
- React Router per navegació
- Context API per estat global

// Opció 2: Angular
- Angular 17+ amb TypeScript
- Angular Material
- RxJS per gestió d'estat
- Angular Router

// Opció 3: Vue.js
- Vue 3 amb TypeScript  
- Vuetify o Quasar
- Pinia per estat global
- Vue Router
```

#### Pàgines a Implementar
```
📱 Frontend Web App
├── 🏠 Pàgina Principal (index.html ✅)
├── 🔐 Autenticació
│   ├── Login (login.html ✅)
│   └── Registre (registrat.html ✅)
├── 📋 Catàleg
│   ├── Categories (categories.html ✅)  
│   ├── Productes per categoria (carns.html ✅)
│   └── Detall producte (detall_product.html ✅)
├── 🛒 Compra
│   ├── Cistella (cistella.html ✅)
│   ├── Checkout (checkout.html ✅)
│   └── Pagament (pagament.html ✅)
├── 👤 Perfil Usuari
│   └── Historial comandes
├── 📞 Contacte (contacte.html ✅)
└── ℹ️ Nosaltres (nosaltres.html ✅)
```

#### Integració amb Backend
```javascript
// Configuració API Client
const API_BASE_URL = 'http://localhost:8080/api';

// Exemples de crides a l'API:
// GET productes
fetch(`${API_BASE_URL}/productes`)
  .then(response => response.json())

// POST nova comanda
fetch(`${API_BASE_URL}/comandes`, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify(comanda)
})
```

---

### 🗄️ BASE DE DADES - Configuració Completa

#### 1. **Instal·lació MySQL amb Docker** (Recomanat)
```yaml
# docker-compose.yml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: bicifood-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: bicifood_db
      MYSQL_USER: bicifood_user
      MYSQL_PASSWORD: bicifood_password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./bicifood_db_v5.0/bicifood_db_v5.0.sql:/docker-entrypoint-initdb.d/init.sql
volumes:
  mysql_data:
```

```bash
# Executar amb Docker Compose
docker-compose up -d mysql

# Verificar que funciona
docker-compose logs mysql
```

#### 2. **Scripts de Base de Dades**
```sql
-- Crear usuari per l'aplicació
CREATE USER 'bicifood_user'@'localhost' IDENTIFIED BY 'bicifood_password';
GRANT ALL PRIVILEGES ON bicifood_db.* TO 'bicifood_user'@'localhost';

-- Crear dades de prova
INSERT INTO rol (nom, descripcio) VALUES 
('CLIENT', 'Usuari client que fa comandes'),
('REPARTIDOR', 'Usuari repartidor que lliura comandes'),
('ADMIN', 'Administrador del sistema');

INSERT INTO categoria (nom, descripcio, activa) VALUES
('CARNS', 'Productes càrnics', true),
('PEIXOS', 'Productes del mar', true), 
('BEGUDES', 'Begudes i refrescos', true);
```

#### 3. **Migrations amb Flyway** (Opcional)
```bash
# Afegir al pom.xml
<plugin>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-maven-plugin</artifactId>
    <version>9.22.3</version>
</plugin>

# Executar migrations
mvn flyway:migrate
```

---

### 🔗 ENLLAÇAR FRONTEND ↔ BACKEND ↔ DATABASE

#### 1. **Configuració CORS** (Ja implementat ✅)
```java
// SecurityConfig.java - Ja configurat per acceptar:
- http://localhost:3000 (React)
- http://localhost:4200 (Angular)
```

#### 2. **Pipeline de Desenvolupament Complet**
```bash
# 1. Iniciar MySQL
docker-compose up -d mysql

# 2. Iniciar Backend
cd backend/
mvn spring-boot:run
# API disponible a: http://localhost:8080/api

# 3. Iniciar Frontend (exemple React)
cd frontend/
npm install
npm start  
# Web disponible a: http://localhost:3000

# 4. Verificar connexió
curl http://localhost:8080/api/productes
```

#### 3. **Variables d'Entorn per Producció**
```bash
# .env per Frontend
REACT_APP_API_URL=https://api.bicifood.cat
REACT_APP_ENV=production

# application-prod.properties per Backend
server.port=8080
spring.datasource.url=jdbc:mysql://production-db:3306/bicifood_db
jwt.secret=${JWT_SECRET}
```

---

## 🛠️ Scripts Útils

### Script de Desenvolupament Complet
```bash
#!/bin/bash
# dev-setup.sh

echo "🚴‍♂️ Iniciant BiciFood Development Environment..."

# Iniciar MySQL
echo "📊 Iniciant MySQL..."
docker-compose up -d mysql

# Esperar que MySQL estigui llest
echo "⏳ Esperant MySQL..."
sleep 10

# Compilar i executar backend
echo "🏗️ Compilant Backend..."
cd backend/
mvn clean compile

echo "🚀 Iniciant Backend..."
mvn spring-boot:run &

# Esperar que el backend estigui llest
echo "⏳ Esperant Backend..."
sleep 15

echo "✅ Environment llest!"
echo "📖 Documentació API: http://localhost:8080/api/swagger-ui.html"
echo "🔍 Endpoint test: http://localhost:8080/api/productes"
```

### Script de Neteja
```bash
#!/bin/bash
# cleanup.sh

echo "🧹 Netejant entorn BiciFood..."

# Aturar processos Java
pkill -f "spring-boot:run"
pkill -f "bicifood-api"

# Aturar Docker
docker-compose down

# Netejar Maven
cd backend/
mvn clean

echo "✅ Entorn netejat!"
```

---

## 🔄 Fitxers Creats i Implementats

### ✅ **Completament Implementats**

#### **Configuració de l'Aplicació**
- `BiciFoodApiApplication.java` - Classe principal Spring Boot
- `AppConfig.java` - Configuració de beans (ModelMapper, PasswordEncoder)
- `SecurityConfig.java` - Configuració Spring Security amb CORS
- `application.properties` - Configuració MySQL, JPA, servidor
- `pom.xml` - Dependències Maven completes

#### **Entitats JPA (8/8)**
- `Usuari.java` - Entitat usuaris amb relacions bidireccionals
- `Rol.java` - Entitat rols (CLIENT, REPARTIDOR, ADMIN)  
- `Categoria.java` - Entitat categories de productes
- `Producte.java` - Entitat productes amb stock i preus
- `Comanda.java` - Entitat comandes amb tracking d'estat
- `EstatComanda.java` - Estats de comandes (PENDENT, LLIURADA...)
- `LiniaComanda.java` - Línies de comandes (productes + quantitats)
- `Lliurament.java` - Entitat lliuraments amb repartidor

#### **Repositoris Spring Data JPA (8/8)**
- `UsuariRepository.java` - Queries d'usuaris amb filtres avançats
- `RolRepository.java` - Queries de rols
- `CategoriaRepository.java` - Queries de categories amb estadístiques
- `ProducteRepository.java` - Queries de productes amb cerca i filtres
- `ComandaRepository.java` - Queries de comandes amb reports
- `EstatComandaRepository.java` - Queries d'estats
- `LiniaComandaRepository.java` - Queries de línies amb estadístiques
- `LliuramentRepository.java` - Queries de lliuraments amb tracking

#### **Services de Negoci (3/3)**
- `UsuariService.java` - Lògica usuaris amb encriptació passwords
- `ProducteService.java` - Lògica productes amb gestió stock
- `ComandaService.java` - Lògica comandes amb validacions

#### **Controllers REST (1/5)**
- `ProducteController.java` - API REST productes amb documentació Swagger ✅

#### **Scripts i Documentació**
- `run.sh` - Script per executar l'aplicació
- `README.md` - Documentació completa ✅

---

### ⚠️ **Pendent d'Implementar**

#### **Controllers REST (4/5)**
- `UsuariController.java` - API gestió usuaris i perfils
- `AuthController.java` - API autenticació (login/register)
- `ComandaController.java` - API gestió comandes  
- `CategoriaController.java` - API gestió categories

#### **Sistema de Seguretat JWT**
- `JwtUtils.java` - Utilitats per tokens JWT
- `JwtAuthenticationFilter.java` - Filtre autenticació
- `UserDetailsServiceImpl.java` - Implementació UserDetails
- `AuthService.java` - Servei d'autenticació

#### **DTOs i Validacions**
- DTOs per a totes les entitats (UsuariDto, ProducteDto...)
- Validacions de camp amb Bean Validation
- Gestió global d'excepcions

---

## 🤝 Contribuir

### Workflow de Desenvolupament
1. **Fork** del repositori
2. **Crear branch** per la funcionalitat: `git checkout -b feature/nova-funcionalitat`
3. **Commit** canvis: `git commit -m "Afegir nova funcionalitat"`
4. **Push** al branch: `git push origin feature/nova-funcionalitat`
5. **Pull Request** al branch main

### Estàndards de Codi
- **Java**: Seguir Google Java Style Guide
- **Nomenclatura**: Català per variables i mètodes de negoci
- **Comentaris**: JavaDoc per classes i mètodes públics
- **Tests**: Cobertura mínima 80%

---

## 📞 Suport

Per dubtes o problemes:
- 📧 **Email**: dev@bicifood.cat
- 💬 **Slack**: #bicifood-dev
- 📖 **Wiki**: [Documentació completa](https://wiki.bicifood.cat)

---

## 📄 Llicència

Aquest projecte està sota llicència MIT. Veure `LICENSE` per més detalls.

---

**Made with ❤️ by BiciFood Team**  
*Lliurament sostenible a Catalunya* 🚴‍♂️🌱
│   ├── BiciFoodApiApplication.java    # Classe principal
│   ├── config/                       # Configuracions
│   │   ├── AppConfig.java            # Configuració general
│   │   └── SecurityConfig.java       # Configuració de seguretat
│   ├── controller/                   # Controllers REST
│   │   └── ProducteController.java   # Endpoints de productes
│   ├── dto/                          # Data Transfer Objects
│   │   ├── LoginRequestDto.java      # DTO per login
│   │   ├── LoginResponseDto.java     # DTO resposta login
│   │   └── ProducteDto.java          # DTO per productes
│   ├── entity/                       # Entitats JPA
│   │   ├── Usuario.java              # Entitat usuari
│   │   ├── Rol.java                  # Entitat rol
│   │   ├── Categoria.java            # Entitat categoria
│   │   ├── Producte.java             # Entitat producte
│   │   ├── Comanda.java              # Entitat comanda
│   │   ├── EstatComanda.java         # Entitat estat comanda
│   │   ├── LiniaComanda.java         # Entitat línia comanda
│   │   └── Lliurament.java           # Entitat lliurament
│   ├── repository/                   # Repositories Spring Data
│   │   ├── UsuarioRepository.java    # Repository usuaris
│   │   ├── RolRepository.java        # Repository rols
│   │   ├── CategoriaRepository.java  # Repository categories
│   │   ├── ProducteRepository.java   # Repository productes
│   │   ├── ComandaRepository.java    # Repository comandes
│   │   ├── EstatComandaRepository.java # Repository estats
│   │   ├── LiniaComandaRepository.java # Repository línies
│   │   └── LliuramentRepository.java # Repository lliuraments
│   └── service/                      # Serveis de negoci
│       ├── UsuarioService.java       # Lògica usuaris
│       ├── ProducteService.java      # Lògica productes
│       └── ComandaService.java       # Lògica comandes
└── resources/
    └── application.properties        # Configuració aplicació
```

## 🗄️ Model de Dades

L'API gestiona les següents entitats principals:

### Usuaris i Rols
- **Usuario** - Clients, repartidors i administradors
- **Rol** - Tipus d'usuari (CLIENT, REPARTIDOR, ADMIN)

### Catàleg de Productes
- **Categoria** - Categories de productes (CARNS, PEIXOS, BEGUDES, etc.)
- **Producte** - Productes disponibles amb preu, stock i descripció

### Gestió de Comandes
- **Comanda** - Comanda realitzada per un client
- **EstatComanda** - Estat de la comanda (PENDENT, PREPARANT, EN RUTA, LLIURADA)
- **LiniaComanda** - Línia individual d'una comanda amb producte i quantitat
- **Lliurament** - Assignació i seguiment del lliurament

## 🚀 Configuració i Execució

### Prerequisits
- **Java 21 LTS** instal·lat
- **Maven 3.8.9** instal·lat
- **MySQL 8.0+** instal·lat i funcionant
- Base de dades `bicifood_db` creada

### 1. Configurar Base de Dades
```sql
CREATE DATABASE bicifood_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Executar l'script SQL proporcionat a `bicifood_db_v5.0/bicifood_db_v5.0.sql`

### 2. Configurar Connexió
Modificar `application.properties` si cal:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bicifood_db
spring.datasource.username=root
spring.datasource.password=LA_TEVA_PASSWORD
```

### 3. Executar l'Aplicació
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

L'API estarà disponible a: `http://localhost:8080/api/v1`

### 4. Documentació Swagger
Accedeix a la documentació interactiva a:
`http://localhost:8080/api/v1/swagger-ui.html`

## 📡 Endpoints Principals

### Productes
- `GET /api/v1/products` - Llistar tots els productes
- `GET /api/v1/products/{id}` - Obtenir producte per ID
- `POST /api/v1/products` - Crear nou producte
- `PUT /api/v1/products/{id}` - Actualitzar producte
- `DELETE /api/v1/products/{id}` - Eliminar producte
- `GET /api/v1/products/search?term={terme}` - Cercar productes
- `GET /api/v1/products/category/{categoryId}` - Productes per categoria
- `GET /api/v1/products/available` - Productes amb stock
- `GET /api/v1/products/popular` - Productes més populars

### Exemple de Petició
```bash
# Obtenir tots els productes
curl -X GET "http://localhost:8080/api/v1/products" \
  -H "accept: application/json"

# Cercar productes per nom
curl -X GET "http://localhost:8080/api/v1/products/search?term=pollastre" \
  -H "accept: application/json"

# Obtenir productes d'una categoria
curl -X GET "http://localhost:8080/api/v1/products/category/1" \
  -H "accept: application/json"
```

## 🔧 Desenvolupament

### Executar Tests
```bash
mvn test
```

### Compilar JAR
```bash
mvn clean package
java -jar target/bicifood-api-1.0.0.jar
```

### Profiles d'Execució
- **dev** - Desenvolupament (per defecte)
- **prod** - Producció
- **test** - Testing

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 🔐 Seguretat

**⚠️ NOTA IMPORTANT**: La configuració actual de seguretat està en mode desenvolupament i permet tots els accessos.

Per a producció, cal implementar:
- Autenticació JWT
- Autorització per rols
- Protecció d'endpoints sensibles
- Rate limiting

## 🏥 Monitoring

### Health Check
`GET /api/v1/actuator/health`

### Métriques
`GET /api/v1/actuator/metrics`

## 📝 Logs

Els logs es configuren per nivell DEBUG per desenvolupament:
- Consultes SQL visibles
- Logs de seguretat actius
- Binding de paràmetres visible

## 🚧 Pròxims Passos

1. **Implementar autenticació JWT completa**
2. **Crear controllers per usuaris i comandes**
3. **Afegir validacions avançades**
4. **Implementar cache amb Redis**
5. **Afegir testing complet**
6. **Dockeritzar l'aplicació**
7. **CI/CD pipeline**

## 👥 Equip

**BiciFood Team** - Plataforma de menjar a domicili sostenible amb bicicletes 🚴‍♂️🌱