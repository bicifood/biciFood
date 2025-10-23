# 🚴‍♂️ BiciFood - Plataforma de Menjar a Domicili Sostenible

<b>El directori a on es troba la plantilla de l'HTML i les imatges, es la carpeta html a GitHub.</b>

## 📖 Descripció del Projecte

**BiciFood** és una plataforma web de comanda de menjar a domicili que es diferencia per la seva aposta per la **sostenibilitat** i el **respecte al medi ambient**. El projecte combina una experiència d'usuari moderna amb valors ecològics, oferint repartiment exclusivament amb **bicicletes** per reduir la petjada de carboni.

### 🌱 Missió i Valors
- **Sostenibilitat**: Productes ecològics i de proximitat
- **Repartiment ECO**: Exclusivament amb bicicletes
- **Qualitat**: Menjar fresc i de qualitat
- **Comunitat**: Suport als productors locals

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
