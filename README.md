# BiciFood

> **Plataforma de comandes de menjar a domicili amb repartiment sostenible en bicicleta**

BiciFood és una aplicació web completa que permet als usuaris realitzar comandes de menjar amb repartiment ecològic en bicicleta. El projecte integra un backend robust amb Spring Boot i un frontend modern i responsiu.

## Característiques Principals

- **Sistema de comandes**: Catàleg de productes amb categories i carret de compra
- **Responsive Design**: Interfície adaptativa amb Bootstrap 5
- **Multi-idioma**: Suport per català, castellà i anglès
- **API REST**: Endpoints RESTful ben documentats amb Swagger
- **Base de dades**: H2 en memòria amb estructura completa
- **Inicialització automàtica**: Dades de prova precarregades

## Stack Tecnològic

### Backend
- **Framework**: Spring Boot 3.3.5
- **Runtime**: Java 21
- **Persistència**: Spring Data JPA
- **Validació**: Spring Validation
- **Base de dades**: H2 Database (en memòria)
- **API Docs**: SpringDoc OpenAPI 3 (Swagger)
- **Monitorització**: Spring Actuator
- **Build Tool**: Maven

### Frontend
- **Core**: HTML5, CSS3, JavaScript (ES6+)
- **Framework CSS**: Bootstrap 5.3.3
- **Servidor**: Python SimpleHTTPServer

## Requisits Previs

- **Java 21** o superior
- **Python 3** (per al servidor frontend)
- **Maven** (opcional, el projecte inclou Maven Wrapper)

## Iniciar l'Aplicació

### Opció 1: Scripts d'Execució (Recomanat)

#### macOS / Linux

```bash
# Navegar a la carpeta de scripts
cd scripts-execucio/unix-scripts

# Iniciar BiciFood
./start-bicifood.sh

# Aturar BiciFood
./stop-bicifood.sh
```

### Opció 2: Docker

```bash
# Construir la imatge
docker build -t bicifood .

# Executar el contenidor
docker run -d -p 8080:8080 -p 3000:3000 --name bicifood bicifood

# Gestió posterior
docker start bicifood    # Iniciar
docker stop bicifood     # Aturar
docker logs bicifood     # Veure logs
```

> [!TIP]
> Per a més detalls sobre Docker, consultar [`DOCKER.md`](DOCKER.md)

## Accés a l'Aplicació

### Frontend
- **Aplicació Web**: http://localhost:3000/frontend/html/TEA5/index.html

### Backend API
- **API Base**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui/index.html
- **API Docs (JSON)**: http://localhost:8080/api/v1/v3/api-docs
- **Health Check**: http://localhost:8080/api/v1/actuator/health
- **H2 Console**: http://localhost:8080/api/v1/h2-console
  - JDBC URL: `jdbc:h2:mem:bicifood_db`
  - Username: `sa`
  - Password: *(buit)*

### Endpoints Principals

| Endpoint | Descripció |
|----------|------------|
| `/api/v1/products` | Gestió de productes |
| `/api/v1/categories` | Gestió de categories |
| `/api/v1/orders` | Gestió de comandes |
| `/api/v1/auth/login` | Autenticació d'usuaris |
| `/api/v1/auth/register` | Registre d'usuaris |

## Estructura del Projecte

```
biciFood/
├── backend/              # API Spring Boot
│   ├── src/main/java/com/bicifood/
│   │   ├── config/      # Configuració (Security, CORS, etc.)
│   │   ├── controller/  # Controllers REST
│   │   ├── dto/         # Data Transfer Objects
│   │   ├── entity/      # Entitats JPA
│   │   ├── repository/  # Repositoris
│   │   ├── security/    # JWT, filtres de seguretat
│   │   └── service/     # Serveis de negoci
│   └── pom.xml
├── frontend/            # Aplicació web
│   ├── css/            # Estils
│   ├── html/           # Pàgines HTML
│   ├── img/            # Imatges
│   └── js/             # Scripts JavaScript
├── docs/               # Documentació i wireframes
├── scripts-execucio/   # Scripts d'inici/parada
├── DOCKER.md           # Guia Docker
├── MANUAL_TECNIC.md    # Manual tècnic complet
└── README.md           # Aquest fitxer
```
