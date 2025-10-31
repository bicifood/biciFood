# 🚴‍♂️ BiciFood - Plataforma de Menjar a Domicili Sostenible

<b>El directori a on es troba la plantilla de l'HTML i les imatges, es la carpeta html a GitHub.</b>

## 📖 Descripció del Projecte

**BiciFood** és una plataforma web de comanda de menjar a domicili que es diferencia per la seva aposta per la **sostenibilitat** i el **respecte al medi ambient**. El projecte combina una experiència d'usuari moderna amb valors ecològics, oferint repartiment exclusivament amb **bicicletes** per reduir la petjada de carboni.

### 🌱 Missió i Valors
- **Sostenibilitat**: Productes ecològics i de proximitat
- **Repartiment ECO**: Exclusivament amb bicicletes
- **Qualitat**: Menjar fresc i de qualitat
- **Comunitat**: Suport als productors locals

## 🚀 Desplegament i Accés

### 🐳 **Opció 1: Docker (RECOMANAT per presentacions)**

#### ✅ Prerequisits mínims
- Docker Desktop instal·lat

#### 🚀 Desplegament súper fàcil
```bash
# Executar script Docker
./docker-deploy.sh

# Escollir opció 1 (Primera vegada)
```

#### 🗄️ **Gestió Base de Dades**
```bash
# Gestor MySQL independent
./mysql-manager.sh

# Per més informació
📋 Veure: MYSQL-GUIA.md
```

**🎯 Avantatges Docker:**
- ✅ Zero configuració manual - ✅ Funciona a qualsevol màquina  
- ✅ Desplegament en 2-3 minuts - ✅ Perfecte per presentacions

📚 **Guia completa**: [docker-deploy-readme.md](docker-deploy-readme.md)

### �️ **Opció 2: Instal·lació Manual**
```bash
# Script automàtic tradicional
./deploy.sh
```
📚 **Guia completa**: [GUIA-ACCÉS-RÀPID.md](GUIA-ACCÉS-RÀPID.md)

### �📋 Documentació Essencial
- **[📚 Documentació API REST](./info-api.md)** - Guia completa per utilitzar l'API
- **[� Guia Docker](./docker-deploy-readme.md)** - Desplegament amb Docker  
- **[� Guia Manual](./GUIA-ACCÉS-RÀPID.md)** - Instal·lació tradicional

### 🌐 Aplicació en Funcionament
- **Frontend**: `http://localhost:3000` (Interfície web)
- **Backend API**: `http://localhost:8080` (API REST)
- **Health Check**: `http://localhost:8080/api/health` (Verificació)

---

## 🏗️ Arquitectura del Projecte

### 📁 Estructura de Carpetes

```
biciFood/
│
├── 📂 html/                           # Frontend del projecte
│   ├── 📂 Plantilla V2/              # Plantilla inicial (Bootstrap)
│   ├── 📂 Plantilla V3/              # Plantilla intermèdia
│   ├── 📂 versio-final-TEA3/         # ✅ VERSIÓ PRINCIPAL
│   └── 📂 images/                     # Galeria d'imatges de productes
│
├── 📂 bicifood_db_v4.0/              # Base de dades
│   └── bicifood_dbV4.0.sql          # Script SQL complet
│
├── 📂 images/                         # Logos i recursos gràfics
├── 📂 Recursos/                       # Documentació i wireframes
└── README.md                          # Aquest fitxer
```

---

## 🎨 Frontend - Interfície Web

### 🌟 Pàgines Implementades

| Pàgina | Fitxer | Descripció | Estat |
|--------|--------|------------|-------|
| **🏠 Inici** | `index.html` | Pàgina principal amb categories | ✅ Complet |
| **🛒 Cistella** | `cistella.html` | Carret de compra interactiu | ✅ Complet |
| **🍽️ Detall Producte** | `detall_product.html` | Vista detallada d'un plat | ✅ Complet |
| **📋 Categories** | `categories.html` | Llistat de categories de menjar | ✅ Complet |
| **🥩 Carns** | `carns.html` | Categoria específica de carns | ✅ Complet |
| **👥 Nosaltres** | `nosaltres.html` | Informació de l'empresa | ✅ Complet |
| **📞 Contacte** | `contacte.html` | Formulari de contacte | ✅ Complet |
| **💳 Pagament** | `pagament.html` | Procés de pagament + mapa | ✅ Complet |
| **🔍 Resultats Cerca** | `resultats_cerca.html` | Resultats de cerques | ✅ Complet |

### 🎨 Tecnologies Frontend

#### **🖼️ UI/UX Framework:**
- **Bootstrap 5.3.3** - Framework CSS responsiu
- **Bootstrap Icons 1.11.1** - Iconografia consistent
- **Google Fonts (Lora)** - Tipografia personalitzada

#### **🎯 Características Clau:**
- ✅ **Responsive Design** - Adaptat a mòbil, tablet i desktop
- ✅ **Navegació intuïtiva** - Header sticky amb dropdown categories
- ✅ **Cistella interactiva** - Gestió de quantitats i eliminació d'articles
- ✅ **Cerca en temps real** - Barra de cerca integrada
- ✅ **Mapes interactius** - Leaflet.js per seguiment de comandes
- ✅ **Animacions CSS** - Transicions suaus i efectes hover

#### **🎨 Paleta de Colors:**
```css
--color-brown: #6b5a3e        /* Marró principal */
--color-lightbrown: #f0e0b2   /* Marró clar per fons */
--color-green: #38761d        /* Verd per bicicletes */
```

---

## 🗄️ Base de Dades - MySQL Persistent

### 🔄 **Opcions de Base de Dades**

#### **🚀 H2 (Per defecte - Desenvolupament)**
- **Tipus**: En memòria
- **Persistència**: ❌ Dades es perden al reiniciar
- **Ús**: Desenvolupament ràpid i testing
- **Accés**: http://localhost:8080/h2-console

#### **💾 MySQL (Producció - Persistència completa)**
- **Tipus**: Base de dades real
- **Persistència**: ✅ Dades es mantenen sempre
- **Ús**: Producció i dades reals
- **Gestió**: `./mysql-manager.sh`

### � **Com canviar de H2 a MySQL:**

```bash
# Opció 1: Docker (automàtic)
./mysql-manager.sh
# Inicia MySQL amb totes les dades carregades

# Opció 2: Activar MySQL a application.properties  
# Descomenta les línies MySQL i comenta H2
```

### �📊 Esquema de la BD (Vàlid per H2 i MySQL)

```sql
📂 bicifood_db (Base de dades principal)
├── 👤 usuari              # Gestió d'usuaris (clients, admins, repartidors)
├── 🏷️ categoria           # Categories de productes (6 categories)
├── 🍽️ producte           # Catàleg de productes (21 productes)
├── 🛒 comanda            # Històrial de comandes
├── 📝 linia_comanda      # Detall de cada comanda
├── 🚚 lliurament         # Gestió de repartiments
├── 📊 estat_comanda      # Estats: PENDENT, PREPARANT, EN RUTA, LLIURADA
└── 👨‍💼 rol              # Rols: ADMIN, CLIENT, REPARTIDOR
```

### 📋 Categories Implementades

| ID | Categoria | Productes | Descripció |
|----|-----------|-----------|------------|
| 1 | **PLATS** | Verdures, generals | Plats principals diversos |
| 2 | **BEGUDES** | Refrescos, aigua | Begudes fredes |
| 3 | **POSTRES** | Dolços | Postres casolanes |
| 4 | **CARNS** | Vedella, pollastre | Plats amb carn |
| 5 | **PEIXOS** | Lluç, peix fresc | Especialitats marines |
| 6 | **AMANIDES** | Fresques, gourmet | Amanides variades |
| 7 | **ARRÒS I PASTA** | Paella, pasta | Carbohidrats |
| 8 | **VINS** | Negres, blancs | Begudes alcohòliques |

### 🍽️ Productes Destacats (21 total)

#### **🥩 Carns:**
- Vedella amb arròs de l'hort - 13,95€
- Pollastre amb xampinyons - 12,95€

#### **🐟 Peixos:**
- Lluç amb verdures - 15,95€
- Peix a la sal - 18,50€

#### **🥗 Amanides:**
- Amanida Gourmet - 9,75€

#### **🍝 Arròs i Pasta:**
- Paella Valenciana - 16,50€
- Espaguetis Carbonara - 11,50€

---

## � Backend - API REST amb Spring Boot

### 📖 Descripció del Backend

El backend de **BiciFood** és una **API REST** completa desenvolupada amb **Spring Boot 3.2.1** que proporciona tota la funcionalitat necessària per gestionar la plataforma de menjar a domicili. Implementa una arquitectura en capes (MVC) amb les millors pràctiques de desenvolupament Java.

### 📚 Documentació de l'API

> **📋 Documentació Completa de l'API**: [info-api.md](./info-api.md)  
> Conté tots els endpoints, exemples d'ús, paràmetres i respostes de l'API REST

### 🏗️ Arquitectura del Backend

#### **🔍 Sistema de Cerca:**
```html
<!-- Barra de cerca global -->
<input type="search" placeholder="Cerca..." aria-label="Cerca">
```

#### **📱 Navegació Responsive:**
```css
/* Header adaptatiu amb dropdown categories */
.navbar-expand-lg
.dropdown-menu
.d-flex.align-items-center
```

#### **🗺️ Integració de Mapes:**
```javascript
// Leaflet.js per seguiment de repartiment
L.map('map').setView([coordenades], zoom)
```

### 🚀 Funcionalitats Avançades

#### **💳 Procés de Pagament:**
- Resum detallat de comanda
- Opcions de pagament múltiples
- Integració WhatsApp per encarrecs
- Seguiment en temps real

#### **📊 Dashboard d'Estat:**
```sql
-- Estats de comanda disponibles
PENDENT → PREPARANT → EN RUTA → LLIURADA
```

---

## 🏃‍♂️ Execució Ràpida - Veure l'Aplicació

### 🌟 **Per Usuaris que Volen Veure l'Aplicació Funcionant**

Si només vols **veure com funciona BiciFood** sense configurar res complex, segueix aquests passos senzills:

#### **📱 Opció 1: Veure el Frontend (Recomanat per Demo)**

1. **Descarregar el Projecte:**
```bash
# Descarregar ZIP des de GitHub o clonar
git clone https://github.com/bicifood/biciFood.git
cd biciFood
```

2. **Obrir amb Servidor Local:**
```bash
# Opció A: Amb Python (més fàcil)
cd html/versio-final-TEA3
python3 -m http.server 8080

# Opció B: Amb Node.js
npx serve html/versio-final-TEA3

# Opció C: Obrir directament al navegador
open html/versio-final-TEA3/index.html
```

3. **Navegar per l'Aplicació:**
- **🏠 Pàgina Principal:** `http://localhost:8080` o `index.html`
- **📋 Categories:** Veure categories de menjar
- **🥩 Productes:** Navegar per carns, postres, etc.
- **🛒 Cistella:** Afegir productes (simulat)
- **💳 Pagament:** Procés de compra amb mapa
- **📞 Contacte:** Formulari de contacte

#### **🎯 Funcionalitats que Podràs Provar:**

✅ **Navegació Completa** - Totes les pàgines estan connectades  
✅ **Responsive Design** - Funciona en mòbil, tablet i desktop  
✅ **Cistella Interactiva** - Afegir/eliminar productes  
✅ **Barra de Cerca** - Cercar productes  
✅ **Mapa Interactiu** - Seguiment de repartiment a la pàgina de pagament  
✅ **Formularis** - Login, registre, contacte  
✅ **Dropdown Categories** - Navegació per tipus de menjar  

#### **� Consells per la Demo:**

- **Comença per `index.html`** - Pàgina principal amb tot el contingut
- **Prova la cistella** - Afegeix productes des de `carns.html` o `detall_product.html`
- **Veure el mapa** - A `pagament.html` hi ha un mapa amb simulació de repartiment
- **Responsive** - Prova redimensionar la finestra o usar el mode mòbil del navegador
- **Formularis** - `login.html` i `registrat.html` tenen validació JavaScript

#### **🔥 Demo Guiada - 5 Minuts:**

1. **🏠 Inici** → `index.html` - Veure la pàgina principal
2. **🍖 Categories** → Clic a "Carns" al dropdown o botó
3. **🍽️ Producte** → Clic a qualsevol plat per veure detalls
4. **🛒 Cistella** → "Afegir a la cistella" i anar a `cistella.html`
5. **💳 Pagament** → "Procedir al pagament" per veure el mapa
6. **📱 Mòbil** → Canviar a vista mòbil per veure responsive

#### **⚠️ Nota Important:**

Aquesta és la **versió frontend estàtica**. Per veure la **funcionalitat completa amb backend** (base de dades real, login real, comandes reals), segueix la secció **"Configuració Completa amb Backend"** més avall.

---

## 🚀 Configuració Completa amb Backend

*Per desenvolupadors que volen integrar frontend + backend + base de dades*

### 📋 Requisits Previs

- **Servidor Web** (Apache/Nginx)
- **MySQL 8.0+** o MariaDB
- **PHP 7.4+** (opcional per funcions dinàmiques)
- **Navegador modern** amb suport JavaScript ES6+

### ⚙️ Configuració Pas a Pas

#### **1️⃣ Clonar el Repositori**
```bash
git clone https://github.com/bicifood/biciFood.git
cd biciFood
```

#### **2️⃣ Configurar Base de Dades**
```bash
# Importar l'esquema de BD
mysql -u root -p < bicifood_db_v4.0/bicifood_dbV4.0.sql

# O des de MySQL Workbench:
# File → Open SQL Script → bicifood_dbV4.0.sql → Execute
```

#### **3️⃣ Configurar Servidor Web**
```bash
# Apache/Nginx document root apuntant a:
/html/versio-final-TEA3/

# O servidor de desenvolupament simple:
python3 -m http.server 8000
# Accedir a: http://localhost:8000/html/versio-final-TEA3/
```

### 🔧 Verificació de la Instal·lació

1. **✅ Pàgina d'Inici**: `index.html` es carrega correctament
2. **✅ Imatges**: Els logos i imatges de productes es mostren
3. **✅ Navegació**: Els enllaços entre pàgines funcionen
4. **✅ Base de Dades**: 8 categories i 21 productes carregats

---

## 🎮 Com Utilitzar el Projecte

### 👤 Flux d'Usuari Típic

1. **🏠 Pàgina d'Inici** → Navegar per categories
2. **📋 Categories** → Seleccionar tipus de menjar
3. **🍽️ Detall Producte** → Veure informació del plat
4. **🛒 Afegir a Cistella** → Gestionar quantitats
5. **💳 Pagament** → Completar comanda
6. **🗺️ Seguiment** → Veure ubicació del repartidor

### 🛠️ Panell d'Administració

```sql
-- Crear usuari administrador
INSERT INTO usuari (email, password_hash, id_rol, `Nom i cognoms`) 
VALUES ('admin@bicifood.cat', 'hash_password', 1, 'Administrador BiciFood');
```

### 📊 Gestió de Productes

```sql
-- Afegir nou producte
INSERT INTO producte (nom, preu, imatge_path, stock, id_categoria, descripcio) 
VALUES ('Nou Plat', 14.95, 'images/nou_plat.png', 50, 4, 'Descripció del nou plat');
```

---

## 🏆 Característiques Destacades

### 🌟 **Sostenibilitat Digital**
- Codi optimitzat per reduir consum energètic
- Imatges comprimides per càrrega ràpida
- CSS/JS minificat per millor rendiment

### 🎨 **Experiència d'Usuari**
- Transicions CSS suaus
- Feedback visual immediate
- Navegació intuïtiva
- Accessibilitat WCAG compliant

### 📱 **Mobile-First Design**
- Responsive en totes les pantalles
- Touch-friendly interface
- Optimització per dispositius mòbils

### 🔒 **Seguretat**
- Validació de formularis
- Protecció SQL injection (preparada)
- Hash de contrasenyes
- Sanitització d'inputs

---

## 🚧 Desenvolupament Futur

### 📈 Roadmap

#### **Fase 1 - Backend Integration** (Pròxim)
- [ ] API REST amb PHP/Laravel
- [ ] Autenticació JWT
- [ ] Panel d'administració complet
- [ ] Sistema de comandes en temps real

#### **Fase 2 - Features Avançades**
- [ ] Notificacions push
- [ ] Programa de fidelització
- [ ] Xat en viu amb repartidor
- [ ] Integració pagaments (Stripe/PayPal)

#### **Fase 3 - Expansió**
- [ ] App mòbil (React Native/Flutter)
- [ ] Dashboard analytics
- [ ] Multi-idioma (Català/Castellà/Anglès)
- [ ] API pública per tercers

### 🔧 Millores Tècniques Pendents

- **Performance**: Lazy loading d'imatges
- **SEO**: Meta tags i structured data
- **PWA**: Service workers per offline
- **Testing**: Unit tests i E2E testing

---

## 🤝 Contribució

### 👥 Equip de Desenvolupament

- **Frontend**: HTML5, CSS3, JavaScript ES6+
- **UI/UX**: Bootstrap 5, responsive design
- **Backend**: MySQL, preparació per PHP
- **DevOps**: Git, GitHub, deployment

### 📝 Guia de Contribució

1. Fork del repositori
2. Crear branch per feature (`git checkout -b feature/nova-funcionalitat`)
3. Commit dels canvis (`git commit -m 'Afegir nova funcionalitat'`)
4. Push al branch (`git push origin feature/nova-funcionalitat`)
5. Crear Pull Request

---

## � Backend - API REST amb Spring Boot

### 📖 Descripció del Backend

El backend de **BiciFood** és una **API REST** completa desenvolupada amb **Spring Boot 3.2.1** que proporciona tota la funcionalitat necessària per gestionar la plataforma de menjar a domicili. Implementa una arquitectura en capes (MVC) amb les millors pràctiques de desenvolupament Java.

### 🏗️ Arquitectura del Backend

```
backend/
├── 📂 src/main/java/com/bicifood/
│   ├── 📂 model/                    # Entitats JPA (Base de Dades)
│   │   ├── Usuari.java             # Gestió d'usuaris amb rols
│   │   ├── Categoria.java          # Categories de productes
│   │   ├── Producte.java           # Catàleg de productes
│   │   ├── Comanda.java            # Sistema de comandes
│   │   ├── DetallComanda.java      # Detalls de comandes
│   │   ├── MetodePagament.java     # Mètodes de pagament
│   │   └── EstatComanda.java       # Estats de comandes (enum)
│   │
│   ├── 📂 repository/              # Accés a Dades (Data Access Layer)
│   │   ├── UsuariRepository.java   # CRUD usuaris + consultes personalitzades
│   │   ├── CategoriaRepository.java # CRUD categories + estadístiques
│   │   ├── ProducteRepository.java # CRUD productes + filtres avançats
│   │   ├── ComandaRepository.java  # CRUD comandes + filtres per estat
│   │   └── DetallComandaRepository.java # Gestió detalls de comandes
│   │
│   ├── 📂 service/                 # Lògica de Negoci (Business Logic)
│   │   ├── UsuariService.java      # Lògica usuaris, registre, auth
│   │   ├── CategoriaService.java   # Lògica categories i validacions
│   │   ├── ProducteService.java    # Lògica productes i stock
│   │   ├── ComandaService.java     # Lògica comandes i canvis d'estat
│   │   ├── AuthService.java        # Autenticació i gestió de sessions
│   │   └── AdminService.java       # Funcions administratives avançades
│   │
│   ├── 📂 controller/              # API REST (Presentation Layer)
│   │   ├── UsuariController.java   # Endpoints gestió usuaris
│   │   ├── CategoriaController.java # Endpoints gestió categories
│   │   ├── ProducteController.java # Endpoints gestió productes
│   │   ├── ComandaController.java  # Endpoints gestió comandes
│   │   ├── AuthController.java     # Endpoints autenticació
│   │   └── AdminController.java    # Endpoints administració
│   │
│   └── 📂 config/                  # Configuració i Seguretat
│       ├── SecurityConfig.java     # Configuració Spring Security
│       ├── DatabaseConfig.java     # Configuració connexió BD
│       └── CorsFilter.java         # Configuració CORS per frontend
│
├── 📂 src/main/resources/
│   ├── application.properties      # Configuració de l'aplicació
│   └── data.sql                   # Dades inicials (opcional)
│
└── pom.xml                        # Dependències Maven
```

### 🛠️ Tecnologies i Frameworks Utilitzats

#### **🔧 Framework Principal:**
- **Spring Boot 3.2.1** - Framework principal Java
- **Spring MVC** - Arquitectura Model-Vista-Controlador
- **Spring Data JPA** - Persistència de dades simplificada
- **Spring Security** - Autenticació i autorització
- **Hibernate 6.4.1** - ORM (Object-Relational Mapping)

#### **📊 Base de Dades:**
- **MySQL 8.0+** - Base de dades relacional principal
- **HikariCP** - Pool de connexions d'alt rendiment
- **JPA/Hibernate** - Mapatge objecte-relacional

#### **🔧 Eines de Desenvolupament:**
- **Maven 3.8+** - Gestió de dependències i build
- **Java 17** - Versió LTS de Java
- **Jackson** - Serialització JSON
- **Validation API** - Validació de dades d'entrada

### 📋 Funcionalitats Implementades

#### **👤 Sistema d'Usuaris**
- ✅ **Registre i Login** - Autenticació amb sessions
- ✅ **Gestió de Perfils** - Actualització de dades personals
- ✅ **Rols d'Usuari** - CLIENT, ADMIN, REPARTIDOR
- ✅ **Validació d'Emails** - Comprovació de duplicats
- ✅ **Encriptació Contrasenyes** - BCrypt per seguretat

#### **🏷️ Gestió de Categories**
- ✅ **CRUD Complet** - Crear, llegir, actualitzar, eliminar
- ✅ **Estadístiques** - Recompte de productes per categoria
- ✅ **Validació de Duplicats** - No permetre categories repetides
- ✅ **Gestió de Productes Associats** - Control de dependencies

#### **🍽️ Catàleg de Productes**
- ✅ **Gestió d'Stock** - Control de quantitats disponibles
- ✅ **Filtres Avançats** - Per categoria, preu, disponibilitat
- ✅ **Cerca de Productes** - Per nom i descripció
- ✅ **Productes Destacats** - Sistema de promocions
- ✅ **Paginació** - Gestió eficient de grans volums de dades

#### **🛒 Sistema de Comandes**
- ✅ **Cicle de Vida Complet** - Des de creació fins entrega
- ✅ **Estats de Comanda** - PENDENT → CONFIRMADA → EN_PREPARACIO → LLESTA → ENTREGADA
- ✅ **Gestió de Detalls** - Múltiples productes per comanda
- ✅ **Càlcul Automàtic** - Preus totals i subtotals
- ✅ **Historial** - Seguiment complet de comandes

#### **🔐 Seguretat i Autenticació**
- ✅ **Sessions HTTP** - Gestió d'estat d'usuari
- ✅ **Autorització per Rols** - Accés basat en permisos
- ✅ **Validació d'Entrada** - Protecció contra injeccions
- ✅ **CORS Configurat** - Integració segura amb frontend
- ✅ **Encriptació de Dades** - Protecció d'informació sensible

#### **📊 Panel d'Administració**
- ✅ **Dashboard Estadístic** - Mètriques clau en temps real
- ✅ **Gestió Avançada d'Usuaris** - Filtres, bulk operations
- ✅ **Control de Comandes** - Vista completa i gestió d'estats
- ✅ **Inventari de Productes** - Alertes de stock baix
- ✅ **Generació de Reports** - Estadístiques per períodes
- ✅ **Configuració del Sistema** - Paràmetres globals
- ✅ **Logs del Sistema** - Monitorització i debugging
- ✅ **Manteniment** - Operacions de neteja i optimització

### 🌐 Compatibilitat amb Frontend

#### **🔌 Endpoints REST Essencials per Frontend**

El backend proporciona una API REST completa que permet al frontend HTML/JavaScript integrar-se perfectament amb totes les funcionalitats. Aquests són els endpoints **més importants** per la integració:

#### **🏷️ Categories:**
```http
GET    /api/categories                 # Llistat de categories (per navbar i pàgina categories)
GET    /api/categories/{id}            # Detall categoria específica
GET    /api/categories/{id}/productes  # Productes d'una categoria (per pàgines com carns.html)
```

#### **🍽️ Productes:**
```http
GET    /api/productes                  # Llistat productes (per catàleg general)
GET    /api/productes/{id}             # Detall producte (per detall_product.html)
GET    /api/productes/categoria/{categoriaId} # Productes per categoria (filtratge)
GET    /api/productes/destacats        # Productes destacats (per pàgina principal)
GET    /api/productes/cerca/{terme}    # Cerca de productes (per barra de cerca)
```

#### **👤 Usuaris:**
```http
POST   /api/usuaris/register           # Registre usuari (per registrat.html)
GET    /api/usuaris/{id}               # Perfil usuari (per perfil.html)
PUT    /api/usuaris/{id}               # Actualitzar perfil (editar perfil)
GET    /api/usuaris/email/{email}/disponible # Verificar email (validació en temps real)
```

#### **🔐 Autenticació:**
```http
POST   /api/auth/login                 # Login (per login.html)
POST   /api/auth/logout                # Logout (botó sortir)
GET    /api/auth/profile               # Perfil sessió actual (verificar login)
GET    /api/auth/check                 # Verificar autenticació (protegir pàgines)
```

#### **🛒 Comandes:**
```http
POST   /api/comandes                   # Crear comanda (per cistella.html → pagament.html)
GET    /api/comandes/{id}              # Detall comanda (seguiment)
PUT    /api/comandes/{id}/estat        # Actualitzar estat (seguiment en temps real)
POST   /api/comandes/{id}/productes    # Afegir producte (gestió cistella)
DELETE /api/comandes/{id}/productes/{producteId} # Eliminar producte (cistella)
GET    /api/comandes/usuari/{usuariId} # Historial comandes usuari
```

#### **🎯 Integració Pràctica Frontend:**

**Exemple JavaScript per obtenir categories:**
```javascript
// Carregar categories per al dropdown del navbar
async function loadCategories() {
    try {
        const response = await fetch('http://localhost:8080/api/categories');
        const categories = await response.json();
        
        // Actualitzar dropdown del navbar
        const dropdown = document.getElementById('categoriesDropdown');
        categories.forEach(categoria => {
            const link = document.createElement('a');
            link.className = 'dropdown-item';
            link.href = `carns.html?categoria=${categoria.id}`;
            link.textContent = categoria.nom;
            dropdown.appendChild(link);
        });
    } catch (error) {
        console.error('Error carregant categories:', error);
    }
}
```

**Exemple per gestió de cistella:**
```javascript
// Afegir producte a la cistella
async function addToCart(producteId, quantitat) {
    try {
        const response = await fetch(`http://localhost:8080/api/comandes/${cartId}/productes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({
                producte_id: producteId,
                quantitat: quantitat
            })
        });
        
        if (response.ok) {
            updateCartUI();
            showSuccessMessage('Producte afegit a la cistella!');
        }
    } catch (error) {
        console.error('Error afegint a cistella:', error);
    }
}
```

**Exemple per login d'usuari:**
```javascript
// Gestió del login
async function login(email, password) {
    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ email, password })
        });
        
        if (response.ok) {
            const user = await response.json();
            sessionStorage.setItem('user', JSON.stringify(user));
            window.location.href = 'index.html';
        } else {
            showError('Email o contrasenya incorrectes');
        }
    } catch (error) {
        console.error('Error en login:', error);
    }
}
```

### 🌐 API REST Endpoints Complets

#### **🔐 Autenticació (`/api/auth`)**
```http
POST   /api/auth/login          # Login d'usuari
POST   /api/auth/logout         # Logout i tancament de sessió  
GET    /api/auth/profile        # Obtenir perfil de la sessió actual
GET    /api/auth/check          # Verificar si usuari està autenticat
```

#### **👤 Usuaris (`/api/usuaris`)**
```http
GET    /api/usuaris                    # Llistat usuaris (paginat)
GET    /api/usuaris/{id}               # Detall d'usuari específic
GET    /api/usuaris/email/{email}      # Buscar usuari per email
POST   /api/usuaris/register           # Registre nou usuari
PUT    /api/usuaris/{id}               # Actualitzar perfil usuari
PUT    /api/usuaris/{id}/password      # Canviar contrasenya
PUT    /api/usuaris/{id}/estat         # Activar/desactivar usuari
GET    /api/usuaris/{id}/comandes      # Comandes de l'usuari
GET    /api/usuaris/{id}/stats         # Estadístiques de l'usuari
DELETE /api/usuaris/{id}               # Eliminar usuari
GET    /api/usuaris/email/{email}/disponible    # Verificar email disponible
GET    /api/usuaris/username/{username}/disponible # Verificar username disponible
GET    /api/usuaris/stats              # Estadístiques generals d'usuaris
```

#### **🏷️ Categories (`/api/categories`)**
```http
GET    /api/categories                 # Llistat totes les categories
GET    /api/categories/{id}            # Detall categoria específica
GET    /api/categories/{id}/productes  # Productes d'una categoria
POST   /api/categories                 # Crear nova categoria
PUT    /api/categories/{id}            # Actualitzar categoria
DELETE /api/categories/{id}            # Eliminar categoria
GET    /api/categories/stats           # Estadístiques de categories
```

#### **🍽️ Productes (`/api/productes`)**
```http
GET    /api/productes                  # Llistat productes (paginat, filtres)
GET    /api/productes/{id}             # Detall producte específic
GET    /api/productes/categoria/{categoriaId} # Productes per categoria
GET    /api/productes/destacats        # Productes destacats
GET    /api/productes/disponibles      # Productes amb stock > 0
POST   /api/productes                  # Crear nou producte
PUT    /api/productes/{id}             # Actualitzar producte
DELETE /api/productes/{id}             # Eliminar producte
GET    /api/productes/cerca/{terme}    # Cercar productes per nom/descripció
PUT    /api/productes/{id}/stock       # Actualitzar stock del producte
```

#### **🛒 Comandes (`/api/comandes`)**
```http
GET    /api/comandes                   # Llistat comandes (paginat, filtres)
GET    /api/comandes/{id}              # Detall comanda específica
POST   /api/comandes                   # Crear nova comanda
PUT    /api/comandes/{id}              # Actualitzar comanda
DELETE /api/comandes/{id}              # Cancel·lar comanda
POST   /api/comandes/{id}/productes    # Afegir producte a comanda
DELETE /api/comandes/{id}/productes/{producteId} # Eliminar producte de comanda
PUT    /api/comandes/{id}/estat        # Actualitzar estat de comanda
POST   /api/comandes/{id}/confirmar    # Confirmar comanda
POST   /api/comandes/{id}/cancelar     # Cancel·lar comanda
GET    /api/comandes/{id}/detalls      # Detalls de la comanda
GET    /api/comandes/usuari/{usuariId} # Comandes d'un usuari específic
GET    /api/comandes/stats             # Estadístiques de comandes
```

#### **👨‍💼 Administració (`/api/admin`)**
```http
GET    /api/admin/dashboard            # Dades del dashboard principal
GET    /api/admin/stats                # Estadístiques detallades
GET    /api/admin/usuaris              # Gestió avançada d'usuaris
PUT    /api/admin/usuaris/bulk-update  # Actualització massiva d'usuaris
GET    /api/admin/comandes             # Gestió avançada de comandes
GET    /api/admin/productes            # Gestió avançada de productes
POST   /api/admin/reports              # Generar reports
GET    /api/admin/config               # Configuració del sistema
PUT    /api/admin/config               # Actualitzar configuració
GET    /api/admin/logs                 # Logs del sistema
POST   /api/admin/maintenance          # Operacions de manteniment
```

### 🚀 Manual d'Instal·lació i Execució

#### **📋 Requisits Previs**

Abans de començar, assegura't de tenir instal·lat:

```bash
# Verificar versions
java -version          # Java 17 o superior
mvn -version          # Maven 3.8 o superior  
mysql --version       # MySQL 8.0 o superior
```

#### **🗄️ Configuració de la Base de Dades**

1. **Crear la Base de Dades:**
```sql
-- Connectar a MySQL com a root
mysql -u root -p

-- Crear base de dades
CREATE DATABASE bicifood_db;

-- Crear usuari per l'aplicació
CREATE USER 'bicifood_user'@'localhost' IDENTIFIED BY 'bicifood_password';
GRANT ALL PRIVILEGES ON bicifood_db.* TO 'bicifood_user'@'localhost';
FLUSH PRIVILEGES;

-- Usar la nova base de dades
USE bicifood_db;

-- Importar l'esquema (si tens el fitxer SQL)
SOURCE /path/to/bicifood_db_v3.0/bicifood_dbV3.0.sql;
```

2. **Configurar Connexió a l'Aplicació:**

Edita el fitxer `backend/src/main/resources/application.properties`:

```properties
# Configuració de la Base de Dades
spring.datasource.url=jdbc:mysql://localhost:3306/bicifood_db
spring.datasource.username=bicifood_user
spring.datasource.password=bicifood_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuració Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Configuració del Servidor
server.port=8080
server.servlet.context-path=/api

# Configuració de Sessions
spring.session.store-type=jdbc
spring.session.jdbc.initialize-schema=always

# Configuració de Logs
logging.level.com.bicifood=DEBUG
logging.level.org.springframework.security=DEBUG
```

#### **⚙️ Compilació i Execució**

1. **Navegar al Directori del Backend:**
```bash
cd /path/to/biciFood/backend
```

2. **Compilar el Projecte:**
```bash
# Netejar i compilar
mvn clean compile

# Executar tests (opcional)
mvn test

# Crear el package
mvn package
```

3. **Executar l'Aplicació:**
```bash
# Opció 1: Amb Maven (desenvolupament)
mvn spring-boot:run

# Opció 2: Amb JAR generat (producció)
java -jar target/bicifood-web-1.0.0-SNAPSHOT.war
```

4. **Verificar que Funciona:**
```bash
# L'aplicació hauria d'estar disponible a:
curl http://localhost:8080/api/categories
```

#### **🌐 Integració Frontend-Backend**

1. **Configurar CORS al Frontend:**

Al teu codi JavaScript del frontend, utilitza aquesta URL base:

```javascript
// Configuració de l'API
const API_BASE_URL = 'http://localhost:8080/api';

// Exemple de crida a l'API
async function getCategories() {
    try {
        const response = await fetch(`${API_BASE_URL}/categories`);
        const categories = await response.json();
        return categories;
    } catch (error) {
        console.error('Error obtenint categories:', error);
    }
}

// Exemple de login
async function login(email, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
            credentials: 'include' // Important per sessions
        });
        
        if (response.ok) {
            const user = await response.json();
            return user;
        }
    } catch (error) {
        console.error('Error en login:', error);
    }
}
```

2. **Exemples d'Ús de l'API:**

```javascript
// Obtenir productes d'una categoria
async function getProductsByCategory(categoryId) {
    const response = await fetch(`${API_BASE_URL}/categories/${categoryId}/productes`);
    return await response.json();
}

// Crear una comanda
async function createOrder(orderData) {
    const response = await fetch(`${API_BASE_URL}/comandes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orderData),
        credentials: 'include'
    });
    return await response.json();
}

// Afegir producte a comanda
async function addProductToOrder(orderId, productId, quantity) {
    const response = await fetch(`${API_BASE_URL}/comandes/${orderId}/productes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
            producte_id: productId, 
            quantitat: quantity 
        }),
        credentials: 'include'
    });
    return await response.json();
}
```

#### **🔧 Configuració Avançada**

1. **Variables d'Entorn per Producció:**
```bash
# Crear fitxer .env o configurar al sistema
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=bicifood_db
export DB_USER=bicifood_user
export DB_PASSWORD=bicifood_password
export SERVER_PORT=8080
export JWT_SECRET=your_secret_key_here
```

2. **Configuració de Perfils:**

**application-dev.properties** (Desenvolupament):
```properties
spring.jpa.show-sql=true
logging.level.com.bicifood=DEBUG
```

**application-prod.properties** (Producció):
```properties
spring.jpa.show-sql=false
logging.level.com.bicifood=WARN
server.compression.enabled=true
```

3. **Docker (Opcional):**
```dockerfile
# Dockerfile per al backend
FROM openjdk:17-jdk-slim
COPY target/bicifood-web-1.0.0-SNAPSHOT.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.war"]
```

#### **📊 Monitorització i Logs**

1. **Logs de l'Aplicació:**
```bash
# Veure logs en temps real
tail -f logs/bicifood.log

# Buscar errors específics
grep -i "error" logs/bicifood.log
```

2. **Endpoints de Salut:**
```bash
# Verificar estat de l'aplicació
curl http://localhost:8080/api/admin/health

# Mètriques del sistema
curl http://localhost:8080/api/admin/stats
```

#### **🚀 Desplegament en Producció**

1. **Preparar per Producció:**
```bash
# Compilar per producció
mvn clean package -Pprod

# Verificar el WAR generat
ls -la target/bicifood-web-1.0.0-SNAPSHOT.war
```

2. **Desplegar a Tomcat:**
```bash
# Copiar WAR a Tomcat
cp target/bicifood-web-1.0.0-SNAPSHOT.war /path/to/tomcat/webapps/

# Reiniciar Tomcat
sudo systemctl restart tomcat
```

### 🛡️ Seguretat i Millors Pràctiques

#### **🔐 Configuració de Seguretat**
- ✅ **Autenticació basada en sessions** amb Spring Security
- ✅ **Encriptació de contrasenyes** amb BCrypt
- ✅ **Validació d'entrada** en tots els endpoints
- ✅ **Protecció CSRF** per formularis web
- ✅ **CORS configurat** per integració segura amb frontend

#### **📊 Optimització de Rendiment**
- ✅ **Paginació** en tots els llistats grans
- ✅ **Consultes optimitzades** amb JPA Criteria API  
- ✅ **Pool de connexions** HikariCP per millor rendiment
- ✅ **Lazy loading** per entitats relacionades
- ✅ **Cache de sessions** per millorar la resposta

#### **🔍 Debugging i Troubleshooting**

**Problemes Comuns:**

1. **Error de Connexió a la BD:**
```bash
# Verificar que MySQL està executant-se
sudo systemctl status mysql

# Comprovar connexió
mysql -u bicifood_user -p bicifood_db
```

2. **Port ja en Ús:**
```bash
# Trobar procés que usa el port 8080
lsof -i :8080

# Matar procés si cal
kill -9 PID
```

3. **Errors de Compilació:**
```bash
# Netejar completament
mvn clean

# Regenerar dependències
mvn dependency:resolve

# Compilar amb informació detallada
mvn compile -X
```

### 📈 Estadístiques i Monitorització

L'API proporciona endpoints detallats per obtenir estadístiques en temps real:

- **Dashboard Principal:** Resum executiu amb KPIs clau
- **Estadístiques d'Usuaris:** Registres, activitat, segments
- **Mètriques de Comandes:** Volum, estat, tendències
- **Inventari:** Stock, productes populars, alertes
- **Rendiment:** Temps de resposta, càrrega del sistema

### 🎯 Conclusions del Backend

El backend de BiciFood proporciona una **base sólida i escalable** per a la plataforma de menjar a domicili. Amb una arquitectura ben estructurada, APIs REST completes i funcionalitats avançades d'administració, està preparat per:

- ✅ **Escalar** amb l'augment d'usuaris i comandes
- ✅ **Integrar-se** perfectament amb qualsevol frontend modern  
- ✅ **Mantenir-se** amb facilitat gràcies al codi net i documentat
- ✅ **Evolucionar** amb noves funcionalitats i requisits futurs
- ✅ **Proporcionar** una experiència d'usuari excel·lent i fiable

---

## �📞 Contacte i Suport

### 🌐 Enllaços del Projecte

- **🔗 Repositori**: [GitHub - BiciFood](https://github.com/bicifood/biciFood)
- **📧 Contacte**: info@bicifood.cat
- **🐛 Issues**: [GitHub Issues](https://github.com/bicifood/biciFood/issues)

### 📜 Llicència

Aquest projecte està sota llicència **MIT**. Veure `LICENSE` per més detalls.

---

## 🙏 Agraïments

Gràcies a tots els que han contribuït al desenvolupament d'aquest projecte sostenible. **BiciFood** no seria possible sense la comunitat que creu en un futur més verd! 🌱

---

<div align="center">
  <img src="images/logoTransparent.png" width="100" alt="BiciFood Logo">
  
  **🚴‍♂️ BiciFood - Menjar sostenible a casa teva 🌱**
  
  [![Status](https://img.shields.io/badge/Status-In%20Development-yellow)]()
  [![Version](https://img.shields.io/badge/Version-4.0-blue)]()
  [![License](https://img.shields.io/badge/License-MIT-green)]()
</div>
