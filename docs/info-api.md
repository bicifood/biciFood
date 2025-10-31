# 📚 API REST BiciFood - Documentació Completa

## 🚀 Informació General

### 📍 URLs Base
- **Backend API**: `http://localhost:8080`
- **Frontend**: `http://localhost:3000`

### 🔐 Autenticació
- **Desenvolupament**: Deshabilitada (accés lliure a tots els endpoints)
- **Producció**: Implementar JWT/OAuth segons necessitats

---

## 🏥 Endpoints de Sistema

### ✅ Health Check
```bash
curl -X GET http://localhost:8080/api/health
```
**Resposta:**
```json
{
  "service": "BiciFood Backend API",
  "version": "1.0.0",
  "status": "UP",
  "timestamp": "2025-10-30T10:38:14.359168"
}
```

---

## 👥 Gestió d'Usuaris

### 📋 Llistar Usuaris
```bash
curl -X GET http://localhost:8080/api/usuaris
```
**Paràmetres opcionals:**
- `page`: Número de pàgina (per defecte: 0)
- `size`: Mida de pàgina (per defecte: 10)
- `rol`: Filtrar per rol (CLIENT, ADMIN, REPARTIDOR)

**Exemple amb paràmetres:**
```bash
curl -X GET http://localhost:8080/api/usuaris?page=0&size=5&rol=CLIENT
```

**Resposta:**
```json
{
  "pagination": {
    "total_elements": 2,
    "page_size": 10,
    "current_page": 0,
    "total_pages": 1
  },
  "data": [
    {
      "id": 1,
      "nom": "Joan",
      "cognom": "Pérez",
      "email": "joan@bicifood.com",
      "telefon": null,
      "adreca": null,
      "ciutat": null,
      "codiPostal": null,
      "dataRegistre": "2025-10-30T10:38:14.359168",
      "dataUltimAcces": null,
      "actiu": true,
      "verificat": false,
      "rol": "CLIENT",
      "comandes": [],
      "admin": false,
      "client": true,
      "nomComplet": "Joan Pérez"
    }
  ],
  "success": true
}
```

### 👤 Obtenir Usuari per ID
```bash
curl -X GET http://localhost:8080/api/usuaris/{id}
```

**Exemple:**
```bash
curl -X GET http://localhost:8080/api/usuaris/1
```

### ➕ Crear Usuari (Recomanat)
```bash
POST http://localhost:8080/api/usuaris/create
Content-Type: application/json

{
  "nom": "Anna",
  "cognom": "Martín",
  "email": "anna@bicifood.com",
  "contrasenya": "123456"
}
```

**Exemple amb curl:**
```bash
curl -X POST http://localhost:8080/api/usuaris/create \
  -H "Content-Type: application/json" \
  -d '{"nom":"Anna","cognom":"Martín","email":"anna@test.com"}'
```

**Resposta:**
```json
{
  "success": true,
  "data": {
    "id": 3,
    "nom": "Anna",
    "cognom": "Martín",
    "email": "anna@bicifood.com",
    "dataRegistre": "2025-10-30T10:45:00.123456",
    "actiu": true,
    "verificat": false,
    "rol": "CLIENT",
    "nomComplet": "Anna Martín"
  },
  "message": "Usuari creat correctament"
}
```

### 🔄 Actualitzar Usuari
```bash
PUT http://localhost:8080/api/usuaris/{id}
Content-Type: application/json

{
  "nom": "Joan",
  "cognom": "Pérez Updated",
  "email": "joan.updated@test.com"
}
```

**Exemple:**
```bash
curl -X PUT http://localhost:8080/api/usuaris/1 \
  -H "Content-Type: application/json" \
  -d '{"nom":"Joan","cognom":"Pérez Updated","email":"joan.updated@test.com"}'
```

### 🗑️ Eliminar Usuari
```bash
DELETE http://localhost:8080/api/usuaris/{id}
```

**Exemple:**
```bash
curl -X DELETE http://localhost:8080/api/usuaris/2
```

---

## 🏷️ Gestió de Categories

### 📋 Llistar Categories
```bash
curl -X GET http://localhost:8080/api/categories
```

**Resposta:**
```json
{
  "data": [
    {
      "id": 1,
      "nom": "CARNS",
      "descripcio": "Plats de carn variats"
    },
    {
      "id": 2,
      "nom": "PEIXOS",
      "descripcio": "Especialitats marines"
    }
  ],
  "success": true,
  "count": 2
}
```

### ➕ Crear Categoria
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{"nom":"POSTRES","descripcio":"Dolços casolans"}'
```

**Format de dades:**
```json
{
  "nom": "POSTRES",
  "descripcio": "Dolços casolans"
}
```

---

## 🍽️ Gestió de Productes

### 📋 Llistar Productes
```bash
curl -X GET http://localhost:8080/api/productes
```

**Paràmetres opcionals:**
- `categoria`: Filtrar per ID de categoria
- `actiu`: Filtrar per estat actiu (true/false)
- `page`: Paginació
- `size`: Mida de pàgina

**Exemple amb filtres:**
```bash
curl -X GET "http://localhost:8080/api/productes?categoria=1&actiu=true"
```

### 👁️ Obtenir Producte per ID
```bash
curl -X GET http://localhost:8080/api/productes/{id}
```

**Exemple:**
```bash
curl -X GET http://localhost:8080/api/productes/1
```

### ➕ Crear Producte
```bash
curl -X POST http://localhost:8080/api/productes \
  -H "Content-Type: application/json" \
  -d '{"nom":"Paella Valenciana","descripcio":"Paella tradicional amb pollastre i verdures","preu":16.50,"categoria":{"id":4},"actiu":true,"disponible":true}'
```

**Format de dades:**
```json
{
  "nom": "Paella Valenciana",
  "descripcio": "Paella tradicional amb pollastre i verdures",
  "preu": 16.50,
  "categoria": {"id": 4},
  "actiu": true,
  "disponible": true
}
```

---

## 🛒 Gestió de Comandes

### 📋 Llistar Comandes
```bash
curl -X GET http://localhost:8080/api/comandes
```

**Paràmetres opcionals:**
- `usuari`: ID de l'usuari
- `estat`: Estat de la comanda (PENDENT, PREPARANT, EN_RUTA, LLIURADA)
- `data`: Filtrar per data

**Exemple amb filtres:**
```bash
curl -X GET "http://localhost:8080/api/comandes?usuari=1&estat=PENDENT"
```

### 👁️ Obtenir Comanda per ID
```bash
curl -X GET http://localhost:8080/api/comandes/{id}
```

**Exemple:**
```bash
curl -X GET http://localhost:8080/api/comandes/1
```

### ➕ Crear Comanda
```bash
curl -X POST http://localhost:8080/api/comandes \
  -H "Content-Type: application/json" \
  -d '{"usuari":{"id":1},"productes":[{"producte":{"id":1},"quantitat":2,"preuUnitari":12.50}],"adreca":"Carrer d'\''Aribau, 64","observacions":"Sense ceba"}'
```

**Format de dades:**
```json
{
  "usuari": {"id": 1},
  "productes": [
    {
      "producte": {"id": 1},
      "quantitat": 2,
      "preuUnitari": 12.50
    }
  ],
  "adreca": "Carrer d'Aribau, 64",
  "observacions": "Sense ceba"
}
```

---

## 🧪 Endpoints de Test i Debug

### 🔍 Test JSON General
```bash
curl -X POST http://localhost:8080/api/test/json \
  -H "Content-Type: application/json" \
  -d '{"prova":"OK","nom":"Test","data":"2025-10-30"}'
```

**Format de dades:**
```json
{
  "prova": "OK",
  "nom": "Test",
  "data": "2025-10-30"
}
```

### 👋 Salutació de Test
```bash
curl -X GET http://localhost:8080/api/test/hello
```

### 🧪 Test d'Usuaris
```bash
curl -X POST http://localhost:8080/api/usuaris/test \
  -H "Content-Type: application/json" \
  -d '{"nom":"Test User","email":"test@test.com"}'
```

**Format de dades:**
```json
{
  "nom": "Test User",
  "email": "test@test.com"
}
```

---

## 🔧 Configuració i Eines

### 📱 CORS
Configurat per acceptar peticions des de:
- `http://localhost:3000` (Frontend)
- `http://localhost:8080` (Same origin)
- `http://127.0.0.1:3000` (Alternatiu)

### 🗄️ Base de Dades
- **Tipus**: H2 Database (en memòria per desenvolupament)
- **Consola**: `http://localhost:8080/h2-console` (si habilitada)
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (buit)

### 🔐 Seguretat
- **Spring Security**: Configurat per desenvolupament (accés lliure)
- **CSRF**: Deshabilitada
- **Encriptació**: BCrypt per contrasenyes

---

## 📊 Codis de Resposta HTTP

| Codi | Descripció | Quan s'usa |
|------|------------|-------------|
| 200 | OK | GET, PUT exitosos |
| 201 | Created | POST exitós |
| 400 | Bad Request | Dades incorrectes o incompletes |
| 404 | Not Found | Recurs no trobat |
| 415 | Unsupported Media Type | Content-Type incorrecte |
| 500 | Internal Server Error | Error del servidor |

---

## 🚀 Exemples Pràctics amb curl

### 🎯 Workflow Complet d'Usuari

```bash
# 1. Verificar que l'API funcioni
curl -X GET http://localhost:8080/api/health

# 2. Llistar usuaris existents
curl -X GET http://localhost:8080/api/usuaris

# 3. Crear un usuari nou
curl -X POST http://localhost:8080/api/usuaris/create \
  -H "Content-Type: application/json" \
  -d '{"nom":"Pere","cognom":"Sala","email":"pere@bicifood.com"}'

# 4. Obtenir l'usuari creat (assumint ID=3)
curl -X GET http://localhost:8080/api/usuaris/3

# 5. Actualitzar l'usuari
curl -X PUT http://localhost:8080/api/usuaris/3 \
  -H "Content-Type: application/json" \
  -d '{"nom":"Pere","cognom":"Sala Updated","email":"pere.updated@bicifood.com"}'

# 6. Llistar categories disponibles
curl -X GET http://localhost:8080/api/categories

# 7. Llistar productes
curl -X GET http://localhost:8080/api/productes
```

### 🛒 Workflow de Comanda

```bash
# 1. Crear usuari per la comanda
curl -X POST http://localhost:8080/api/usuaris/create \
  -H "Content-Type: application/json" \
  -d '{"nom":"Client","cognom":"Prova","email":"client@test.com"}'

# 2. Veure productes disponibles
curl -X GET http://localhost:8080/api/productes

# 3. Crear una comanda (si hi ha productes)
curl -X POST http://localhost:8080/api/comandes \
  -H "Content-Type: application/json" \
  -d '{"usuari":{"id":4},"observacions":"Primera comanda de prova"}'

# 4. Consultar l'estat de la comanda
curl -X GET http://localhost:8080/api/comandes
```

---

## 🔍 Troubleshooting

### ❌ Error 415: Unsupported Media Type
**Solució**: Assegura't d'incloure el header:
```bash
-H "Content-Type: application/json"
```

### ❌ Error 400: Bad Request
**Possibles causes**:
- Camps obligatoris que falten (nom, cognom, email per usuaris)
- Format JSON incorrecte
- Tipus de dades incorrectes

### ❌ Error de connexió
**Verificacions**:
1. Comprovar que el backend estigui funcionant:
   ```bash
   curl -X GET http://localhost:8080/api/health
   ```
2. Verificar que el port 8080 estigui lliure:
   ```bash
   lsof -i :8080
   ```

### ⚡ Reiniciar el Backend
```bash
# Des del directori del projecte
cd backend
pkill -f "spring-boot:run"
mvn clean compile
nohup mvn spring-boot:run > ../backend.log 2>&1 &

# Verificar que funcioni
sleep 5 && curl -X GET http://localhost:8080/api/health
```

---

## 📝 Notes de Desenvolupament

- **Environment**: Desenvolupament local
- **Data de creació**: 30 octubre 2025
- **Versió API**: 1.0.0
- **Spring Boot**: 3.2.1
- **Java**: 17
- **Port Backend**: 8080
- **Port Frontend**: 3000

---

*Última actualització: 30 octubre 2025*
*Per a més informació, consulta el README.md del projecte.*