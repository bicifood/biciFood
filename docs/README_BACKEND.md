# 🚴‍♀️ BiciFood Backend - Maven Web Application

## 🏗️ Arquitectura del Sistema

### **Estructura Maven Web Completa**

```
backend/
├── pom.xml                                    # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/bicifood/
│   │   │   ├── config/
│   │   │   │   └── CorsFilter.java            # CORS configuration
│   │   │   ├── controller/
│   │   │   │   └── ProducteController.java    # REST API endpoints
│   │   │   ├── model/
│   │   │   │   ├── Categoria.java             # JPA Entity
│   │   │   │   ├── Producte.java              # JPA Entity
│   │   │   │   ├── Usuari.java                # JPA Entity
│   │   │   │   ├── Comanda.java               # JPA Entity
│   │   │   │   ├── DetallComanda.java         # JPA Entity
│   │   │   │   └── MetodePagament.java        # JPA Entity
│   │   │   ├── repository/
│   │   │   │   └── ProducteRepository.java    # Spring Data JPA Repository
│   │   │   └── service/
│   │   │       ├── ProducteService.java       # Service Interface
│   │   │       └── impl/
│   │   │           └── ProducteServiceImpl.java # Service Implementation
│   │   ├── resources/
│   │   │   └── database.properties            # DB configuration
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           ├── web.xml                    # Servlet configuration
│   │           └── spring/
│   │               ├── applicationContext.xml # Spring root context
│   │               ├── dispatcher-servlet.xml # Spring MVC context
│   │               └── security-context.xml   # Spring Security
│   └── test/java/                             # Unit tests
└── README_BACKEND.md                          # This file
```

## 🛠️ Stack Tecnològic

### **Framework Principal**
- **Spring MVC 6.1.14**: Web framework i REST APIs
- **Spring Data JPA 3.2.1**: Data access layer
- **Spring Security 6.2.1**: Autenticació i autorització

### **Persistència**
- **Hibernate 6.4.1**: JPA implementation
- **MySQL 8.0**: Base de dades
- **HikariCP 5.1.0**: Connection pool

### **JSON i Validació**
- **Jackson 2.17.0**: JSON serialization
- **Hibernate Validator 8.0.1**: Bean validation

### **Build i Deploy**
- **Maven 3.x**: Build automation
- **Java 17**: Runtime environment
- **Tomcat 9+**: Application server

## 📦 Entitats JPA Implementades

### **1. Categoria**
```java
@Entity
@Table(name = "categoria")
public class Categoria {
    @Id @GeneratedValue
    private Long id;
    
    @NotBlank @Size(max = 100)
    private String nom;
    
    @Size(max = 255)
    private String descripcio;
    
    @OneToMany(mappedBy = "categoria")
    private List<Producte> productes;
}
```

### **2. Producte**
```java
@Entity
@Table(name = "producte")
public class Producte {
    @Id @GeneratedValue
    private Long id;
    
    @NotBlank @Size(max = 150)
    private String nom;
    
    @NotNull @DecimalMin("0.0")
    private BigDecimal preu;
    
    @Min(0)
    private Integer stock;
    
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
```

## 🌐 REST API Endpoints

### **Productes API** (`/api/productes`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/productes` | Obtenir tots els productes |
| `GET` | `/api/productes?categoria={id}` | Productes per categoria |
| `GET` | `/api/productes/{id}` | Producte específic |
| `POST` | `/api/productes` | Crear nou producte |
| `PUT` | `/api/productes/{id}` | Actualitzar producte |
| `DELETE` | `/api/productes/{id}` | Eliminar producte |
| `PATCH` | `/api/productes/{id}/stock` | Actualitzar stock |
| `GET` | `/api/productes/cerca?q={query}` | Cercar productes |

### **Exemple Response JSON**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "nom": "Vedella amb arròs de l'hort",
      "descripcio": "Carn de vedella, arròs, verdures fresques",
      "preu": 13.95,
      "stock": 15,
      "imatgeUrl": "../images/VedellaAmbArros.png",
      "categoria": {
        "id": 1,
        "nom": "CARNS"
      }
    }
  ],
  "count": 1
}
```

## ⚙️ Configuració Spring

### **ApplicationContext (Root)**
- **DataSource**: HikariCP connection pool
- **EntityManagerFactory**: JPA configuration
- **TransactionManager**: JPA transactions
- **Component Scan**: Services, repositories

### **DispatcherServlet (Web)**
- **Controllers**: REST endpoints
- **View Resolver**: JSP support
- **Message Converters**: JSON handling
- **CORS Configuration**: Frontend integration
- **Exception Handling**: Error responses

### **Security Context**
- **Public APIs**: `/api/**` accessible
- **Admin Area**: `/admin/**` protected
- **CSRF Disabled**: For REST APIs
- **Form Login**: Web interface

## 🔨 Com Utilitzar

### **1. Configurar Base de Dades**
```properties
# database.properties
db.url=jdbc:mysql://localhost:3306/bicifood_db_v5_0
db.username=root
db.password=
```

### **2. Build amb Maven**
```bash
cd backend/
mvn clean compile
mvn package
```

### **3. Deploy a Tomcat**
```bash
# Copiar WAR a Tomcat
cp target/bicifood.war $TOMCAT_HOME/webapps/

# O utilitzar Maven plugin
mvn tomcat7:run
```

### **4. Provar APIs**
```bash
# Obtenir productes
curl http://localhost:8080/bicifood/api/productes

# Crear producte
curl -X POST http://localhost:8080/bicifood/api/productes \
  -H "Content-Type: application/json" \
  -d '{"nom":"Nou Producte","preu":15.50,"stock":10}'
```

## 🔄 Integració amb Frontend

### **CORS Configuration**
- **Allowed Origins**: `localhost:3000`, `localhost:8080`, `127.0.0.1:5500`
- **Allowed Methods**: `GET, POST, PUT, DELETE, OPTIONS`
- **Credentials**: Enabled

### **Frontend Integration**
```javascript
// Exemple cridada des del frontend
async function loadProductes() {
  const response = await fetch('http://localhost:8080/bicifood/api/productes');
  const result = await response.json();
  
  if (result.success) {
    renderProductes(result.data);
  }
}
```

## 🧪 Testing

### **Unit Tests**
```bash
mvn test
```

### **Integration Tests**
```bash
mvn verify
```

## 🚀 Next Steps

1. **Completar Controllers**: CategoriaController, ComandaController
2. **Implementar Seguretat**: JWT tokens, role-based access
3. **Afegir Caching**: Redis per millor performance  
4. **Documentació API**: Swagger/OpenAPI
5. **Testing**: Unit i integration tests
6. **Monitoring**: Actuator endpoints

---

**🎯 Objectiu**: Backend professional amb Spring MVC per integrar amb el frontend BiciFood existent.