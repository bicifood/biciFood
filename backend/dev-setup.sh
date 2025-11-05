#!/bin/bash

# 🚴‍♂️ BiciFood Development Environment Setup Script
# Aquest script configura l'entorn complet de desenvolupament

set -e  # Sortir si hi ha errors

# Colors per output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Funcions d'utilitat
print_step() {
    echo -e "${BLUE}📋 $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Banner d'inici
echo -e "${GREEN}"
cat << "EOF"
 ____  _      _ _____              _ 
|  _ \(_)    (_|  ___|            | |
| |_) |_  ___ _| |_ ___   ___   __| |
|  _ <| |/ __| |  _/ _ \ / _ \ / _` |
| |_) | | (__| | || (_) | (_) | (_| |
|____/|_|\___|_\_| \___/ \___/ \__,_|

🚴‍♂️ Development Environment Setup 🚴‍♂️
EOF
echo -e "${NC}"

# Verificar prerequisits
print_step "Verificant prerequisits..."

# Verificar Java
if ! command -v java &> /dev/null; then
    print_error "Java no està instal·lat. Instal·la Java 21 LTS"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | grep "version" | awk '{print $3}' | tr -d '"' | cut -d'.' -f1)
if [ "$JAVA_VERSION" != "21" ]; then
    print_warning "Java $JAVA_VERSION detectat. Es recomana Java 21 LTS"
else
    print_success "Java 21 detectat correctament"
fi

# Verificar Maven
if ! command -v mvn &> /dev/null; then
    print_error "Maven no està instal·lat. Instal·la Maven 3.8+"
    exit 1
else
    print_success "Maven detectat correctament"
fi

# Verificar Docker
if ! command -v docker &> /dev/null; then
    print_error "Docker no està instal·lat. Instal·la Docker Desktop"
    exit 1
else
    print_success "Docker detectat correctament"
fi

# Verificar Docker Compose
if ! command -v docker-compose &> /dev/null; then
    print_error "Docker Compose no està instal·lat"
    exit 1
else
    print_success "Docker Compose detectat correctament"
fi

# Iniciar MySQL amb Docker
print_step "Iniciant MySQL amb Docker..."
docker-compose up -d mysql

# Esperar que MySQL estigui llest
print_step "Esperant que MySQL estigui llest..."
timeout=60
counter=0
until docker-compose exec mysql mysqladmin ping -h localhost -u root -pbicifood_root_2024 --silent &> /dev/null; do
    echo -n "."
    sleep 2
    counter=$((counter + 2))
    if [ $counter -ge $timeout ]; then
        print_error "MySQL no ha pogut inicialitzar-se en $timeout segons"
        exit 1
    fi
done
echo ""
print_success "MySQL està llest i funcionant!"

# Verificar que la base de dades existeix
print_step "Verificant base de dades bicifood_db..."
if docker-compose exec mysql mysql -u root -pbicifood_root_2024 -e "USE bicifood_db;" &> /dev/null; then
    print_success "Base de dades bicifood_db verificada correctament"
else
    print_warning "Base de dades bicifood_db no trobada o amb problemes"
fi

# Compilar el projecte
print_step "Compilant projecte Maven..."
mvn clean compile -q
if [ $? -eq 0 ]; then
    print_success "Projecte compilat correctament"
else
    print_error "Error compilant el projecte"
    exit 1
fi

# Opció per executar tests
read -p "Voleu executar els tests? (y/n): " run_tests
if [[ $run_tests =~ ^[Yy]$ ]]; then
    print_step "Executant tests..."
    mvn test -q
    if [ $? -eq 0 ]; then
        print_success "Tots els tests han passat!"
    else
        print_warning "Alguns tests han fallat, però continuem..."
    fi
fi

# Iniciar l'aplicació
print_step "Iniciant aplicació BiciFood..."
echo "L'aplicació s'està iniciant en background..."

# Crear log file si no existeix
mkdir -p logs
touch logs/application.log

# Iniciar aplicació en background
nohup mvn spring-boot:run > logs/application.log 2>&1 &
APP_PID=$!
echo $APP_PID > logs/app.pid

# Esperar que l'aplicació estigui llesta
print_step "Esperant que l'aplicació estigui llesta..."
timeout=120
counter=0
until curl -s http://localhost:8080/api/actuator/health &> /dev/null; do
    echo -n "."
    sleep 3
    counter=$((counter + 3))
    if [ $counter -ge $timeout ]; then
        print_error "L'aplicació no ha pogut inicialitzar-se en $timeout segons"
        print_step "Revisa els logs: tail -f logs/application.log"
        exit 1
    fi
done
echo ""

# Informació final
print_success "🎉 BiciFood Development Environment està llest!"
echo ""
echo -e "${BLUE}📱 Serveis disponibles:${NC}"
echo "  🌐 API Backend:         http://localhost:8080/api"
echo "  📖 Swagger UI:          http://localhost:8080/api/swagger-ui.html"  
echo "  📊 Base de Dades:       localhost:3306 (bicifood_db)"
echo "  🛠️ PhpMyAdmin:          http://localhost:8081"
echo ""
echo -e "${BLUE}🔧 Comandes útils:${NC}"
echo "  📋 Logs aplicació:      tail -f logs/application.log"
echo "  🗄️ Connectar MySQL:     mysql -h localhost -u bicifood_user -pbicifood_password bicifood_db"
echo "  🛑 Aturar tot:          ./cleanup.sh"
echo "  📊 Status serveis:      docker-compose ps"
echo ""
echo -e "${BLUE}🧪 Endpoints de test:${NC}"
echo "  🔍 Health Check:        curl http://localhost:8080/api/actuator/health"
echo "  📦 Llistar productes:   curl http://localhost:8080/api/productes"
echo ""
print_success "Happy coding! 🚴‍♂️💻"