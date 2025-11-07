# 🚴‍♂️ BiciFood - Plataforma de Menjar a Domicili Sostenible

## 📖 Descripció del Projecte

**BiciFood** és una plataforma web innovadora de comanda de menjar a domicili que revoluciona el sector amb la seva aposta ferma per la **sostenibilitat** i el **respecte al medi ambient**. 

Combinem tecnologia moderna amb valors ecològics per oferir una experiència única: menjar fresc, de qualitat i lliurat exclusivament amb **bicicletes** per reduir la petjada de carboni.

### 🌱 Missió i Valors

- **🌍 Sostenibilitat**: Productes ecològics i de proximitat
- **🚴‍♂️ Repartiment ECO**: Exclusivament amb bicicletes elèctriques
- **🍽️ Qualitat Premium**: Menjar fresc i de alta qualitat
- **🏘️ Comunitat Local**: Suport als productors i restaurants locals
- **💚 Medi Ambient**: Zero emissions en el transport

### ✨ Característiques Principals

- **🛒 Cistella Intel·ligent**: Sistema de carret amb persistència local
- **🎨 Disseny Responsive**: Optimitzat per a mòbils, tablets i desktop
- **⚡ Rendiment**: Interfície ràpida i fluida
- **🔄 Temps Real**: Actualitzacions instantànies de stock i preus
- **📱 PWA Ready**: Preparada per convertir-se en aplicació mòbil

---

## 🚀 Inici Ràpid

### ⚡ Executar l'Aplicació (1 Comanda)

```bash
# Clonar el repositori
git clone https://github.com/bicifood/biciFood.git
cd biciFood

# Iniciar tota l'aplicació (backend + frontend)
./start-bicifood.sh
```

**Això farà:**
1. 📦 Compilar el backend automàticament
2. 🚀 Iniciar Spring Boot API (port 8080)
3. 🌐 Iniciar servidor web (port 3000)
4. 🌍 Obrir l'aplicació al navegador

### 🛑 Aturar l'Aplicació

```bash
./stop-bicifood.sh
```

---

## 🏗️ Arquitectura del Projecte

### 🎯 Stack Tecnològic

**Frontend:**
- HTML5 + CSS3 + JavaScript ES6
- Bootstrap 5.3.3 per al disseny responsive
- LocalStorage per persistència del carret
- Fetch API per comunicació amb backend

**Backend:**
- Spring Boot 3.3.5 (Java 21)
- H2 Database (desenvolupament)
- Spring Data JPA + Hibernate
- Spring Security + JWT
- Maven per gestió de dependències

### 📁 Estructura del Projecte

```
biciFood/
├── � start-bicifood.sh              # Script principal per iniciar
├── 🛑 stop-bicifood.sh               # Script per aturar aplicació
│
├── 📂 backend/                        # API Spring Boot
│   ├── src/main/java/                 # Codi Java
│   ├── src/main/resources/            # Configuració
│   └── README.md                      # Documentació tècnica
│
├── 📂 html/versio-final-TEA3/        # Frontend principal
│   ├── index.html                     # Pàgina principal
│   ├── categories.html                # Catàleg de productes
│   ├── cistella.html                  # Carret de compra
│   ├── js/api.js                      # Lògica JavaScript
│   └── style.css                      # Estils personalitzats
│
└── 📂 images/                         # Imatges de productes
```

---

## � Funcionalitats Implementades

### 🎨 Interfície d'Usuari

| Component | Descripció | Característiques |
|-----------|------------|------------------|
| **🏠 Pàgina Principal** | Portal d'entrada | Productes destacats, navegació intuïtiva |
| **📋 Catàleg** | Exploració de productes | Filtres per categoria, cerca en temps real |
| **🛒 Cistella Intel·ligent** | Carret de compra | Persistència local, càlcul automàtic de preus |
| **📱 Disseny Responsive** | Adaptació multiplataforma | Optimitzat per mòbil, tablet i desktop |
| **⚡ Experiència Fluida** | Interfície moderna | Animacions CSS, transicions suaus |

### 🔧 Funcionalitats Tècniques

- **🔄 Comunicació API** - Integració completa amb backend
- **� Persistència Local** - Cistella guardada en localStorage
- **🎨 Disseny Modular** - Components reutilitzables
- **🌐 PWA Ready** - Preparada per aplicació mòbil
- **� Gestió d'Estat** - Control eficient de dades

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

## 🗄️ Base de Dades - MySQL

### 📊 Esquema de la BD

```sql
📂 bicifood_db (Base de dades principal)
├── 👤 usuari              # Gestió d'usuaris (clients, admins, repartidors)
├── 🏷️ categoria           # Categories de productes (8 categories)
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

## 🛠️ Funcionalitats Implementades

### 🎯 Funcionalitats Core

#### **🛒 Sistema de Cistella:**
```javascript
// Gestió interactiva de productes
- Afegir/eliminar productes
- Modificar quantitats (+/-)
- Càlcul automàtic de preus
- IVA i costos d'enviament
- Codis promocionals
```

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

## 🚀 Instal·lació i Configuració

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

## 📞 Contacte i Suport

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
