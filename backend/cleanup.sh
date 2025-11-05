#!/bin/bash

# 🧹 BiciFood Cleanup Script
# Aquest script atura tots els serveis i neteja l'entorn de desenvolupament

set -e  # Sortir si hi ha errors

# Colors per output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_step() {
    echo -e "${BLUE}🧹 $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️ $1${NC}"
}

echo -e "${YELLOW}"
cat << "EOF"
 ____ _                        
/ ___| | ___  __ _ _ __  _   _ _ __
\___ \ |/ _ \/ _` | '_ \| | | | '_ \ 
 ___) | |  __/ (_| | | | | |_| | |_) |
|____/|_|\___|\__,_|_| |_|\__,_| .__/ 
                              |_|    
🧹 BiciFood Environment Cleanup 🧹
EOF
echo -e "${NC}"

# Aturar aplicació Spring Boot
print_step "Aturant aplicació BiciFood..."
if [ -f logs/app.pid ]; then
    APP_PID=$(cat logs/app.pid)
    if kill -0 "$APP_PID" 2>/dev/null; then
        kill "$APP_PID"
        print_success "Aplicació aturada (PID: $APP_PID)"
    else
        print_warning "Aplicació ja estava aturada"
    fi
    rm -f logs/app.pid
else
    # Fallback: aturar tots els processos Java relacionats
    pkill -f "spring-boot:run" 2>/dev/null || true
    pkill -f "bicifood-api" 2>/dev/null || true
    print_warning "Processos Java de BiciFood aturats (si existien)"
fi

# Aturar contenidors Docker
print_step "Aturant contenidors Docker..."
if docker-compose ps -q | grep -q .; then
    docker-compose down
    print_success "Contenidors Docker aturats"
else
    print_warning "No hi havia contenidors Docker funcionant"
fi

# Opció per eliminar volums (dades de MySQL)
read -p "Voleu eliminar també les dades de MySQL? (y/n): " remove_data
if [[ $remove_data =~ ^[Yy]$ ]]; then
    print_step "Eliminant volums de dades..."
    docker-compose down -v
    docker volume rm bicifood_mysql_data 2>/dev/null || true
    print_success "Dades de MySQL eliminades"
fi

# Netejar Maven
print_step "Netejant compilació Maven..."
mvn clean -q
print_success "Cache Maven netejat"

# Netejar logs
print_step "Netejant fitxers de log..."
if [ -d logs ]; then
    rm -f logs/*.log logs/*.pid
    print_success "Logs eliminats"
fi

# Netejar fitxers temporals
print_step "Netejant fitxers temporals..."
find . -name "*.tmp" -delete 2>/dev/null || true
find . -name ".DS_Store" -delete 2>/dev/null || true
print_success "Fitxers temporals eliminats"

# Informació de verificació
print_step "Verificant neteja..."

# Verificar que no hi ha processos Java
if pgrep -f "bicifood" > /dev/null; then
    print_warning "Encara hi ha processos BiciFood funcionant:"
    pgrep -f "bicifood" -l
else
    print_success "No hi ha processos BiciFood funcionant"
fi

# Verificar que no hi ha contenidors
if docker ps --format "table {{.Names}}" | grep -q bicifood; then
    print_warning "Encara hi ha contenidors BiciFood funcionant:"
    docker ps --filter "name=bicifood" --format "table {{.Names}}\t{{.Status}}"
else
    print_success "No hi ha contenidors BiciFood funcionant"
fi

# Verificar ports
if lsof -i :8080 > /dev/null 2>&1; then
    print_warning "El port 8080 encara està ocupat:"
    lsof -i :8080
else
    print_success "Port 8080 alliberat"
fi

if lsof -i :3306 > /dev/null 2>&1; then
    print_warning "El port 3306 encara està ocupat (pot ser MySQL local):"
    lsof -i :3306
else
    print_success "Port 3306 alliberat"
fi

echo ""
print_success "🎉 Entorn BiciFood netejat correctament!"
echo ""
echo -e "${BLUE}Per tornar a iniciar l'entorn:${NC}"
echo "  ./dev-setup.sh"
echo ""
echo -e "${BLUE}Per reiniciar només MySQL:${NC}"
echo "  docker-compose up -d mysql"
echo ""
echo -e "${BLUE}Per reiniciar només l'aplicació:${NC}"
echo "  mvn spring-boot:run"
echo ""