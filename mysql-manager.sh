#!/bin/bash

# Colors per al output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Funcions d'utilitat
print_header() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}🗄️  GESTOR BASE DE DADES BICIFOOD${NC}"
    echo -e "${BLUE}========================================${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ️  $1${NC}"
}

# Funcions principals
start_mysql_only() {
    print_info "Iniciant només MySQL..."
    docker-compose -f docker-compose-mysql-only.yml up -d
    if [ $? -eq 0 ]; then
        print_success "MySQL iniciat correctament!"
        print_info "Accés: mysql -h localhost -P 3306 -u bicifood_user -p"
        print_info "Password: bicifood_pass123"
    else
        print_error "Error iniciant MySQL"
    fi
}

stop_mysql_only() {
    print_info "Aturant MySQL..."
    docker-compose -f docker-compose-mysql-only.yml down
    print_success "MySQL aturat"
}

connect_mysql() {
    print_info "Connectant a MySQL..."
    docker exec -it bicifood-mysql-only mysql -u bicifood_user -pbicifood_pass123 bicifood_db
}

view_logs() {
    print_info "Mostrant logs de MySQL..."
    docker logs bicifood-mysql-only -f
}

reset_database() {
    read -p "⚠️  Això eliminarà TOTES les dades. Continuar? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_info "Resetejant base de dades..."
        docker-compose -f docker-compose-mysql-only.yml down -v
        docker-compose -f docker-compose-mysql-only.yml up -d
        print_success "Base de dades resetejada!"
    fi
}

backup_database() {
    BACKUP_FILE="backup_bicifood_$(date +%Y%m%d_%H%M%S).sql"
    print_info "Creant backup: $BACKUP_FILE"
    docker exec bicifood-mysql-only mysqldump -u bicifood_user -pbicifood_pass123 bicifood_db > $BACKUP_FILE
    print_success "Backup creat: $BACKUP_FILE"
}

show_status() {
    print_info "Estat dels contenidors:"
    docker ps --filter "name=bicifood" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
}

# Menú principal
show_menu() {
    echo ""
    echo -e "${BLUE}Selecciona una opció:${NC}"
    echo "1. 🚀 Iniciar MySQL"
    echo "2. 🛑 Aturar MySQL" 
    echo "3. 🔗 Connectar a MySQL"
    echo "4. 📋 Veure logs"
    echo "5. 🔄 Resetear base de dades"
    echo "6. 💾 Crear backup"
    echo "7. 📊 Mostrar estat"
    echo "8. ❌ Sortir"
    echo ""
}

# Bucle principal
main() {
    print_header
    
    while true; do
        show_menu
        read -p "Opció: " choice
        
        case $choice in
            1) start_mysql_only ;;
            2) stop_mysql_only ;;
            3) connect_mysql ;;
            4) view_logs ;;
            5) reset_database ;;
            6) backup_database ;;
            7) show_status ;;
            8) 
                print_info "Sortint..."
                exit 0
                ;;
            *)
                print_error "Opció no vàlida. Si us plau, tria 1-8."
                ;;
        esac
        
        echo ""
        read -p "Prem Enter per continuar..."
    done
}

# Executar script
main "$@"