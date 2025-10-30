#!/bin/bash

# 🚀 Script de Desplegament Automàtic per BiciFood
# Aquest script configura i executa l'aplicació completa

echo "🚴‍♀️ === BICIFOOD - DESPLEGAMENT AUTOMÀTIC === 🍔"

# Colors per terminal
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Directori base
BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_DIR="$BASE_DIR/backend"
FRONTEND_DIR="$BASE_DIR/html"

echo -e "${BLUE}📁 Directori base: $BASE_DIR${NC}"

# Funció per verificar si el port està ocupat
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
        echo -e "${YELLOW}⚠️  Port $port ja està en ús${NC}"
        return 1
    fi
    return 0
}

# Funció per compilar i executar el backend
start_backend() {
    echo -e "\n${BLUE}🔧 === CONFIGURANT BACKEND === ${NC}"
    
    cd "$BACKEND_DIR"
    
    echo -e "${GREEN}📦 Compilant l'aplicació...${NC}"
    mvn clean compile
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Backend compilat correctament${NC}"
        
        echo -e "${GREEN}🚀 Executant servidor Spring Boot...${NC}"
        echo -e "${YELLOW}💡 L'aplicació estarà disponible a: http://localhost:8080${NC}"
        echo -e "${YELLOW}💡 API REST disponible a: http://localhost:8080/api${NC}"
        
        # Executar en background
        nohup mvn spring-boot:run > backend.log 2>&1 &
        BACKEND_PID=$!
        echo $BACKEND_PID > backend.pid
        
        echo -e "${GREEN}✅ Backend executant-se amb PID: $BACKEND_PID${NC}"
    else
        echo -e "${RED}❌ Error compilant el backend${NC}"
        exit 1
    fi
}

# Funció per configurar el frontend
setup_frontend() {
    echo -e "\n${BLUE}🎨 === CONFIGURANT FRONTEND === ${NC}"
    
    # Crear un servidor simple per al frontend
    cd "$BASE_DIR"
    
    # Crear fitxer HTML principal que unifica tots els templates
    cat > index.html << 'EOF'
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BiciFood - Menjar Ràpid a Domicili</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .hero-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 60px 0;
        }
        .feature-card {
            transition: transform 0.3s;
            border: none;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .feature-card:hover {
            transform: translateY(-5px);
        }
        .btn-bicifood {
            background: linear-gradient(45deg, #FF6B6B, #4ECDC4);
            border: none;
            color: white;
        }
        .navbar-brand {
            font-weight: bold;
            font-size: 1.5rem;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="#"><i class="fas fa-bicycle"></i> BiciFood</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="#home">Inici</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#templates">Templates</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#api">API</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="http://localhost:8080" target="_blank">Backend</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section id="home" class="hero-section text-center">
        <div class="container">
            <h1 class="display-4 mb-4"><i class="fas fa-bicycle"></i> BiciFood</h1>
            <p class="lead mb-4">Plataforma completa de comandes de menjar ràpid amb lliurament sostenible</p>
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <div class="card feature-card text-center p-3">
                                <i class="fas fa-utensils fa-3x text-primary mb-3"></i>
                                <h5>Menjar Deliciós</h5>
                                <p class="small">Varietat de restaurants i plats</p>
                            </div>
                        </div>
                        <div class="col-md-4 mb-3">
                            <div class="card feature-card text-center p-3">
                                <i class="fas fa-bicycle fa-3x text-success mb-3"></i>
                                <h5>Lliurament Eco</h5>
                                <p class="small">Transport sostenible en bicicleta</p>
                            </div>
                        </div>
                        <div class="col-md-4 mb-3">
                            <div class="card feature-card text-center p-3">
                                <i class="fas fa-clock fa-3x text-warning mb-3"></i>
                                <h5>Servei Ràpid</h5>
                                <p class="small">Lliurament en 30 minuts</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Templates Section -->
    <section id="templates" class="py-5">
        <div class="container">
            <h2 class="text-center mb-5">🎨 Templates Disponibles</h2>
            <div class="row">
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title"><i class="fas fa-paint-brush"></i> Plantilla V2</h5>
                            <p class="card-text">Disseny modern amb interfície d'usuari millorada</p>
                            <a href="html/Plantilla V2/index.html" class="btn btn-bicifood" target="_blank">Veure Template</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title"><i class="fas fa-star"></i> Plantilla V3</h5>
                            <p class="card-text">Versió avançada amb funcionalitats extra</p>
                            <a href="html/Plantilla V3/index.html" class="btn btn-bicifood" target="_blank">Veure Template</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title"><i class="fas fa-rocket"></i> Versió Final TEA3</h5>
                            <p class="card-text">Template definitiu amb totes les funcionalitats</p>
                            <a href="html/versio-final-TEA3/detall_product.html" class="btn btn-bicifood" target="_blank">Veure Template</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- API Section -->
    <section id="api" class="py-5 bg-light">
        <div class="container">
            <h2 class="text-center mb-5">🔗 API REST Endpoints</h2>
            <div class="row">
                <div class="col-md-6">
                    <h4><i class="fas fa-users"></i> Usuaris</h4>
                    <ul class="list-group mb-4">
                        <li class="list-group-item">GET /api/usuaris - Llistar usuaris</li>
                        <li class="list-group-item">POST /api/usuaris - Crear usuari</li>
                        <li class="list-group-item">PUT /api/usuaris/{id} - Actualitzar</li>
                        <li class="list-group-item">DELETE /api/usuaris/{id} - Eliminar</li>
                    </ul>
                </div>
                <div class="col-md-6">
                    <h4><i class="fas fa-shopping-cart"></i> Comandes</h4>
                    <ul class="list-group mb-4">
                        <li class="list-group-item">GET /api/comandes - Llistar comandes</li>
                        <li class="list-group-item">POST /api/comandes - Crear comanda</li>
                        <li class="list-group-item">PUT /api/comandes/{id} - Actualitzar</li>
                        <li class="list-group-item">GET /api/comandes/usuari/{id} - Per usuari</li>
                    </ul>
                </div>
            </div>
            <div class="text-center">
                <a href="http://localhost:8080/api" class="btn btn-primary btn-lg" target="_blank">
                    <i class="fas fa-external-link-alt"></i> Accedir a l'API
                </a>
            </div>
        </div>
    </section>

    <!-- Status Section -->
    <section class="py-5">
        <div class="container">
            <h2 class="text-center mb-4">📊 Estat del Sistema</h2>
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-body">
                            <div class="row text-center">
                                <div class="col-md-6">
                                    <h5><i class="fas fa-server"></i> Backend</h5>
                                    <p id="backend-status" class="text-muted">Verificant...</p>
                                </div>
                                <div class="col-md-6">
                                    <h5><i class="fas fa-globe"></i> Frontend</h5>
                                    <p class="text-success"><i class="fas fa-check"></i> Actiu</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <footer class="bg-primary text-white text-center py-4">
        <div class="container">
            <p>&copy; 2025 BiciFood - Menjar ràpid, lliurament sostenible</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Verificar estat del backend
        async function checkBackendStatus() {
            try {
                const response = await fetch('http://localhost:8080/api/health');
                if (response.ok) {
                    document.getElementById('backend-status').innerHTML = '<i class="fas fa-check text-success"></i> Actiu (Port 8080)';
                } else {
                    throw new Error('Backend no disponible');
                }
            } catch (error) {
                document.getElementById('backend-status').innerHTML = '<i class="fas fa-times text-danger"></i> Inactiu';
            }
        }

        // Verificar cada 5 segons
        setInterval(checkBackendStatus, 5000);
        checkBackendStatus();
    </script>
</body>
</html>
EOF

    echo -e "${GREEN}✅ Fitxer HTML principal creat${NC}"
}

# Funció per iniciar el servidor web per al frontend
start_frontend_server() {
    echo -e "\n${BLUE}🌐 === INICIANT SERVIDOR FRONTEND === ${NC}"
    
    cd "$BASE_DIR"
    
    # Verificar si Python està disponible
    if command -v python3 &> /dev/null; then
        echo -e "${GREEN}🐍 Utilitzant servidor HTTP de Python...${NC}"
        echo -e "${YELLOW}💡 Frontend disponible a: http://localhost:3000${NC}"
        
        nohup python3 -m http.server 3000 > frontend.log 2>&1 &
        FRONTEND_PID=$!
        echo $FRONTEND_PID > frontend.pid
        
        echo -e "${GREEN}✅ Frontend executant-se amb PID: $FRONTEND_PID${NC}"
    else
        echo -e "${YELLOW}⚠️  Python no disponible. Instal·la un servidor web manual${NC}"
    fi
}

# Funció per mostrar informació del desplegament
show_info() {
    echo -e "\n${GREEN}🎉 === DESPLEGAMENT COMPLETAT === ${NC}"
    echo -e "${BLUE}📱 Aplicació Principal: http://localhost:3000${NC}"
    echo -e "${BLUE}🔧 Backend API: http://localhost:8080${NC}"
    echo -e "${BLUE}📚 Templates disponibles:${NC}"
    echo -e "   • Plantilla V2: http://localhost:3000/html/Plantilla V2/index.html"
    echo -e "   • Plantilla V3: http://localhost:3000/html/Plantilla V3/index.html"
    echo -e "   • Versió Final: http://localhost:3000/html/versio-final-TEA3/detall_product.html"
    echo -e "\n${YELLOW}📋 Gestió del Sistema:${NC}"
    echo -e "   • Parar backend: kill \$(cat backend.pid)"
    echo -e "   • Parar frontend: kill \$(cat frontend.pid)"
    echo -e "   • Logs backend: tail -f backend.log"
    echo -e "   • Logs frontend: tail -f frontend.log"
}

# Menú principal
echo -e "\n${YELLOW}Selecciona una opció:${NC}"
echo "1) Desplegament complet (Backend + Frontend)"
echo "2) Només Backend"
echo "3) Només Frontend"
echo "4) Parar tot"
echo "5) Mostrar estat"

read -p "Opció (1-5): " choice

case $choice in
    1)
        setup_frontend
        start_backend
        sleep 3
        start_frontend_server
        show_info
        ;;
    2)
        start_backend
        echo -e "${GREEN}✅ Backend executant-se a http://localhost:8080${NC}"
        ;;
    3)
        setup_frontend
        start_frontend_server
        echo -e "${GREEN}✅ Frontend executant-se a http://localhost:3000${NC}"
        ;;
    4)
        if [ -f backend.pid ]; then
            kill $(cat backend.pid) 2>/dev/null && echo -e "${GREEN}✅ Backend parat${NC}"
            rm backend.pid
        fi
        if [ -f frontend.pid ]; then
            kill $(cat frontend.pid) 2>/dev/null && echo -e "${GREEN}✅ Frontend parat${NC}"
            rm frontend.pid
        fi
        ;;
    5)
        echo -e "${BLUE}📊 Estat actual:${NC}"
        if [ -f backend.pid ] && kill -0 $(cat backend.pid) 2>/dev/null; then
            echo -e "Backend: ${GREEN}✅ Actiu (PID: $(cat backend.pid))${NC}"
        else
            echo -e "Backend: ${RED}❌ Inactiu${NC}"
        fi
        if [ -f frontend.pid ] && kill -0 $(cat frontend.pid) 2>/dev/null; then
            echo -e "Frontend: ${GREEN}✅ Actiu (PID: $(cat frontend.pid))${NC}"
        else
            echo -e "Frontend: ${RED}❌ Inactiu${NC}"
        fi
        ;;
    *)
        echo -e "${RED}❌ Opció no vàlida${NC}"
        ;;
esac