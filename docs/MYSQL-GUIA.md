# 🗄️ GUIA MySQL - BiciFood
## ✅ IMPLEMENTACIÓ COMPLETA I TESTEJADA

### 🎯 **ESTAT ACTUAL: 100% FUNCIONAL**
- ✅ **MySQL 8.0**: Funcionant al port 3307  
- ✅ **6 categories**: CARNS, PEIXOS, AMANIDES, ARRÒS I PASTA, BEGUDES, POSTRES
- ✅ **10 productes**: Dades reals carregades automàticament
- ✅ **3 usuaris**: Sistema d'autenticació preparat
- ✅ **Spring Boot**: Connectat i operatiu amb persistència
- ✅ **APIs de test**: Endpoints `/api/mysql/*` funcionals

---

## 🚀 OPCIONS PER UTILITZAR MYSQL

### � **Opció 1: DOCKER (Recomanada i Testejada)**

#### A. Només MySQL (per desenvolupament local)
```bash
# 🎛️ GESTOR AUTOMÀTIC (RECOMANAT)
./mysql-manager.sh
# Escollir opció 1: 🚀 Iniciar MySQL

# 📊 VERIFICAR ESTAT
curl http://localhost:8080/api/mysql/stats

# 🔗 CONNECTAR DIRECTAMENT
docker exec -it bicifood-mysql-only mysql -u bicifood_user -pbicifood_pass123 bicifood_db
```

**⚠️ IMPORTANT**: MySQL funciona al **PORT 3307** (no 3306) per evitar conflictes.

#### B. Aplicació completa amb MySQL persistent
```bash
# 1. Iniciar MySQL primer
./mysql-manager.sh  # Opció 1

# 2. Executar aplicació amb MySQL
cd backend
java -jar target/bicifood-web-1.0.0-SNAPSHOT.jar --spring.profiles.active=local-mysql

# 3. Verificar funcionament
curl http://localhost:8080/api/mysql/categories
```

### 🔧 Opció 2: MYSQL LOCAL NATIVO (PORT 3306)

> **ℹ️ NOTA**: Aquesta opció és alternativa a Docker. Si Docker ja funciona, no cal fer aquests passos.

#### **📦 PAS 1: INSTAL·LAR MYSQL**

##### **🍎 macOS (amb Homebrew):**
```bash
# 1. Instal·lar MySQL
brew install mysql

# 2. Iniciar servei MySQL
brew services start mysql

# 3. Configuració segura inicial (OPCIONAL)
mysql_secure_installation
# Respon:
# - Set root password? Y → Escriu contrasenya
# - Remove anonymous users? Y  
# - Disallow root login remotely? Y
# - Remove test database? Y
# - Reload privilege tables? Y

# 4. Verificar instal·lació
mysql --version
brew services list | grep mysql
```

##### **🐧 Ubuntu/Debian:**
```bash
# 1. Actualitzar paquets
sudo apt update

# 2. Instal·lar MySQL
sudo apt install mysql-server

# 3. Iniciar servei
sudo systemctl start mysql
sudo systemctl enable mysql

# 4. Configuració segura
sudo mysql_secure_installation

# 5. Verificar estat
sudo systemctl status mysql
mysql --version
```

##### **🪟 Windows:**
```bash
# 1. Descarregar des de https://dev.mysql.com/downloads/mysql/
# 2. Executar l'instal·lador MySQL Installer
# 3. Escollir "Developer Default"
# 4. Configurar contrasenya root
# 5. Iniciar MySQL des de Services
```

#### **🗄️ PAS 2: CONFIGURAR BASE DE DADES**

##### **🔐 2.1 Connectar com a root:**
```bash
# Connectar amb contrasenya (si s'ha establert)
mysql -u root -p

# O sense contrasenya (si no s'ha configurat)
mysql -u root
```

##### **🏗️ 2.2 Crear base de dades i usuari:**
```sql
-- Crear base de dades amb encoding UTF8MB4
CREATE DATABASE bicifood_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear usuari específic per BiciFood
CREATE USER 'bicifood_user'@'localhost' IDENTIFIED BY 'bicifood_pass123';

-- Donar tots els permisos a l'usuari sobre la BD
GRANT ALL PRIVILEGES ON bicifood_db.* TO 'bicifood_user'@'localhost';

-- Aplicar canvis
FLUSH PRIVILEGES;

-- Verificar usuari creat
SELECT User, Host FROM mysql.user WHERE User = 'bicifood_user';

-- Sortir de MySQL
EXIT;
```

##### **📥 2.3 Importar dades inicials:**
```bash
# Opció A: Importar fitxer SQL directament
mysql -u bicifood_user -pbicifood_pass123 bicifood_db < bicifood_db_v5.0/bicifood_db_v5.0.sql

# Opció B: Importar des de dins de MySQL
mysql -u bicifood_user -pbicifood_pass123 bicifood_db
```

```sql
-- Dins de MySQL
USE bicifood_db;
SOURCE bicifood_db_v5.0/bicifood_db_v5.0.sql;

-- Verificar dades importades
SHOW TABLES;
SELECT COUNT(*) as categories FROM categoria;
SELECT COUNT(*) as productes FROM producte;
EXIT;
```

#### **✅ PAS 3: VERIFICAR INSTAL·LACIÓ**

```bash
# Test connexió bàsica
mysql -u bicifood_user -pbicifood_pass123 bicifood_db -e "SELECT 'MySQL Local Funcional!' as test;"

# Verificar taules i dades
mysql -u bicifood_user -pbicifood_pass123 bicifood_db -e "
SHOW TABLES; 
SELECT COUNT(*) as categories FROM categoria; 
SELECT COUNT(*) as productes FROM producte;"

# Verificar port 3306
netstat -tuln | grep :3306
# O a macOS:
lsof -i :3306
```

**Resultat esperat:**
```
+---------------------------+
| test                      |
+---------------------------+
| MySQL Local Funcional!    |
+---------------------------+

+-----------------------+
| Tables_in_bicifood_db |
+-----------------------+
| categoria             |
| comanda               |
| producte              |
| usuari                |
| ...                   |
+-----------------------+
```

#### 3. Configurar Spring Boot per MySQL Local

**⚠️ ATENCIÓ**: MySQL local utilitza el **port 3306** (Docker utilitza 3307)

**Opció 1: Crear perfil mysql-local-nativo (RECOMANAT)**

Crea el fitxer `src/main/resources/application-mysql-local-nativo.properties`:
```properties
# MySQL Local Configuration (Port 3306)
spring.datasource.url=jdbc:mysql://localhost:3306/bicifood_db?useSSL=false&serverTimezone=Europe/Madrid
spring.datasource.username=bicifood_user
spring.datasource.password=bicifood_pass123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Desactivar H2
spring.h2.console.enabled=false
```

**Executar amb perfil:**
```bash
# Des de l'IDE (IntelliJ/Eclipse):
# VM Options: -Dspring.profiles.active=mysql-local-nativo

# Des de terminal:
mvn clean install
java -jar target/bicifood-web-1.0.0-SNAPSHOT.jar --spring.profiles.active=mysql-local-nativo

# O amb Maven:
mvn spring-boot:run -Dspring-boot.run.profiles=mysql-local-nativo
```

**Opció 2: Modificar application.properties directament**
```properties
# Comenta aquestes línies H2:
# spring.datasource.url=jdbc:h2:mem:testdb
# spring.datasource.driver-class-name=org.h2.Driver
# spring.h2.console.enabled=true

# Afegeix configuració MySQL local (PORT 3306):
spring.datasource.url=jdbc:mysql://localhost:3306/bicifood_db?useSSL=false&serverTimezone=Europe/Madrid
spring.datasource.username=bicifood_user  
spring.datasource.password=bicifood_pass123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

---

## 📊 CONFIGURACIONS SPRING BOOT

#### 4. Verificar Connexió Spring Boot → MySQL Local

**Test 1: Verificar connexió de xarxa**
```bash
# Comprovar que MySQL local escolta al port 3306
netstat -an | grep 3306
# O a macOS:
lsof -i :3306

# Hauria de mostrar quelcom com:
# tcp46      0      0  *.3306                 *.*                    LISTEN
```

**Test 2: Executar aplicació amb logs detallats**
```bash
java -jar target/bicifood-web-1.0.0-SNAPSHOT.jar \
  --spring.profiles.active=mysql-local-nativo \
  --logging.level.org.springframework.jdbc=DEBUG \
  --logging.level.com.zaxxer.hikari=DEBUG
```

**Logs esperats (ÈXIT):**
```
2024-01-15 10:30:22.145  INFO 12345 --- [main] com.zaxxer.hikari.HikariDataSource: HikariPool-1 - Starting...
2024-01-15 10:30:22.156  INFO 12345 --- [main] com.zaxxer.hikari.HikariDataSource: HikariPool-1 - Start completed.
2024-01-15 10:30:22.489  INFO 12345 --- [main] o.h.e.t.j.p.i.JtaPlatformInitiator: HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
2024-01-15 10:30:22.497  INFO 12345 --- [main] j.LocalContainerEntityManagerFactoryBean: Initialized JPA EntityManagerFactory for persistence unit 'default'
```

**Test 3: Utilitzar endpoints de test**
```bash
# Test connexió (si tens MySQLTestController)
curl http://localhost:8080/api/mysql/test

# Resposta esperada:
# {"status":"success","message":"MySQL Local Funcional!","database":"bicifood_db","port":3306}
```

### 🎛️ **Perfils Disponibles (ACTUALITZATS):**

| Perfil | Fitxer | Base de Dades | Port | Ús |
|--------|--------|---------------|------|-----|
| **default** | `application.properties` | H2 en memòria | - | 🧪 Desenvolupament ràpid |
| **local-mysql** | `application-local-mysql.properties` | MySQL Docker | 3307 | 🐳 **RECOMANAT** |
| **mysql-local-nativo** | `application-mysql-local-nativo.properties` | MySQL Local | 3306 | 💻 Instal·lació local |
| **mysql** | `application-mysql.properties` | MySQL Docker | 3307 | 🐳 Producció Docker |

### 🔄 **Canviar entre bases de dades:**
```bash
# Docker MySQL (RECOMANAT - ja funciona)
java -jar target/bicifood-web-1.0.0-SNAPSHOT.jar --spring.profiles.active=local-mysql

# MySQL Local (després d'instal·lar)
java -jar target/bicifood-web-1.0.0-SNAPSHOT.jar --spring.profiles.active=mysql-local-nativo

# H2 temporal (desenvolupament)
java -jar target/bicifood-web-1.0.0-SNAPSHOT.jar
# (no especificar perfil)
```

### ❌ **Troubleshooting MySQL Local**

**Error: "Access denied for user"**
```bash
# Verificar usuari i password
mysql -u bicifood_user -p bicifood_db
# Si falla, tornar a crear l'usuari:

mysql -u root -p
CREATE USER 'bicifood_user'@'localhost' IDENTIFIED BY 'bicifood_pass123';
GRANT ALL PRIVILEGES ON bicifood_db.* TO 'bicifood_user'@'localhost';
FLUSH PRIVILEGES;
```

**Error: "Unknown database 'bicifood_db'"**
```sql
-- Crear la base de dades
mysql -u root -p
CREATE DATABASE bicifood_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**Error: "Communications link failure"**
```bash
# Verificar que MySQL està executant-se
# macOS:
brew services list | grep mysql
sudo brew services start mysql

# Ubuntu/Debian:
sudo systemctl status mysql
sudo systemctl start mysql

# Windows:
# Servei "MySQL80" ha d'estar executant-se
```

**Error de port (3306 ocupat)**
```bash
# Verificar què utilitza el port 3306
lsof -i :3306
netstat -an | grep 3306

# Si hi ha conflicte, utilitzar port alternatiu:
# A my.cnf afegir: port = 3307
# I canviar Spring Boot: spring.datasource.url=jdbc:mysql://localhost:3307/bicifood_db
```

---

## 🧪 VERIFICACIÓ I TESTING

### ✅ **APIs de Test MySQL (NOVES!):**
```bash
# Test connexió bàsica
curl http://localhost:8080/api/mysql/test

# Estadístiques de la BD
curl http://localhost:8080/api/mysql/stats

# Categories des de MySQL
curl http://localhost:8080/api/mysql/categories

# Productes des de MySQL  
curl http://localhost:8080/api/mysql/productes
```

### 📋 **Resultats Esperats:**
```json
// GET /api/mysql/stats
{
  "database": "MySQL 8.0 (Port 3307)",
  "database_stats": {
    "categories": 6,
    "productes": 10,
    "usuaris": 3,
    "comandes": 0
  },
  "success": true,
  "persistence": "PERMANENT"
}
```

### 🔍 **Comparativa H2 vs MySQL:**

| Aspecte | H2 (Desenvolupament) | MySQL (Producció) |
|---------|---------------------|-------------------|
| **Persistència** | ❌ Es perd al reiniciar | ✅ **PERMANENT** |
| **Dades reals** | ❌ Bàsiques de prova | ✅ **6 categories + 10 productes** |
| **Port** | En memòria | **3307** |
| **Per presentació** | Testing ràpid | **DEMOSTRACIÓ REAL** |
| **Configuració** | Automàtica | Docker + perfil |

---

## ⚡ COMANDAMENTS RÀPIDS (TESTEJATS)

### 🎛️ **Gestió MySQL:**
```bash
# Gestor interactiu (RECOMANAT)
./mysql-manager.sh
# Opcions: Iniciar(1), Aturar(2), Connectar(3), Logs(4), Reset(5), Backup(6)

# Comandaments manuals
docker-compose -f docker-compose-mysql-only.yml up -d    # Iniciar
docker-compose -f docker-compose-mysql-only.yml down     # Aturar
docker logs bicifood-mysql-only -f                       # Logs en temps real
```

### 🔍 **Verificació i Testing:**
```bash
# Estat contenidors
docker ps --filter "name=mysql"

# Test connexió directa
docker exec -it bicifood-mysql-only mysql -u bicifood_user -pbicifood_pass123 bicifood_db -e "SELECT COUNT(*) FROM categoria;"

# Test aplicació Spring Boot
curl http://localhost:8080/api/mysql/stats | python3 -m json.tool
```

### 💾 **Backup i Restauració:**
```bash
# Crear backup
docker exec bicifood-mysql-only mysqldump -u bicifood_user -pbicifood_pass123 bicifood_db > backup_$(date +%Y%m%d).sql

# Restaurar backup
docker exec -i bicifood-mysql-only mysql -u bicifood_user -pbicifood_pass123 bicifood_db < backup_20241030.sql

# Verificar dades després de restaurar
curl http://localhost:8080/api/mysql/categories
```

---

## 🎯 GUIA DE PRESENTACIÓ ACADÈMICA

### 📋 **Demostració de Persistència (Pas a Pas):**

```bash
# 1. Mostrar H2 (dades temporals)
java -jar target/bicifood-web-1.0.0-SNAPSHOT.jar
# → Visitar: http://localhost:8080/api/categories
# → Reiniciar aplicació → Dades perdudes ❌

# 2. Canviar a MySQL (dades persistents)  
./mysql-manager.sh  # Iniciar MySQL
java -jar target/bicifood-web-1.0.0-SNAPSHOT.jar --spring.profiles.active=local-mysql
# → Visitar: http://localhost:8080/api/mysql/stats
# → Reiniciar aplicació → Dades mantingudes ✅
```

### 🏆 **Avantatges per Presentació:**
- ✅ **Dades reals**: 6 categories + 10 productes precarregats
- ✅ **Persistència visual**: Demostració abans/després reinici
- ✅ **APIs de prova**: Endpoints específics per testing
- ✅ **Zero configuració**: Docker + scripts automatitzats
- ✅ **Professionalisme**: Base de dades real vs. desenvolupament

---

## 🎉 RESUM EXECUTIU

| Característica | Estat | Descripció |
|----------------|--------|------------|
| **🗄️ MySQL 8.0** | ✅ **FUNCIONAL** | Port 3307, contenidor healthy |
| **📊 Dades carregades** | ✅ **COMPLETES** | 6 categories, 10 productes, 3 usuaris |  
| **🔗 Spring Boot** | ✅ **CONNECTAT** | Perfil local-mysql operatiu |
| **🧪 APIs testejades** | ✅ **VERIFICADES** | /api/mysql/* endpoints funcionals |
| **💾 Persistència** | ✅ **CONFIRMADA** | Dades es mantenen després de reinicis |
| **🎛️ Gestió** | ✅ **AUTOMATITZADA** | Scripts mysql-manager.sh + docker-compose |

### 🚀 **Per passar de H2 a MySQL:**
**NOMÉS cal executar**: `./mysql-manager.sh` → Opció 1 → `java -jar target/*.jar --spring.profiles.active=local-mysql`

**🎯 Resultat: Base de dades persistent completa per presentació acadèmica!**