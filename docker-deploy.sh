#!/bin/bash

# 🐳 Script de Desplegament amb Docker per BiciFood
# Aquest script simplifica el desplegament amb Docker

echo "🐳 === BICIFOOD - DESPLEGAMENT DOCKER === 🍔"

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Verificar si Docker està instal·lat
check_docker() {
    if ! command -v docker &> /dev/null; then
        echo -e "${RED}❌ Docker no està instal·lat${NC}"
        echo -e "${YELLOW}📥 Instal·la Docker des de: https://www.docker.com/get-started${NC}"
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        echo -e "${RED}❌ Docker no està executant-se${NC}"
        echo -e "${YELLOW}🔧 Inicia Docker i torna a provar${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}✅ Docker disponible${NC}"
}

# Mostrar menú
show_menu() {
    echo -e "\n${BLUE}Selecciona una opció:${NC}"
    echo -e "1) 🚀 ${GREEN}Construir i Executar (Primera vegada)${NC}"
    echo -e "2) ▶️  ${BLUE}Només Executar (si ja està construït)${NC}"
    echo -e "3) 🔄 ${YELLOW}Reconstruir des de zero${NC}"
    echo -e "4) 🔍 ${BLUE}Veure Logs${NC}"
    echo -e "5) 📊 ${BLUE}Estat del Container${NC}"
    echo -e "6) 🌐 ${BLUE}Obrir URLs${NC}"
    echo -e "7) 🛑 ${RED}Parar Container${NC}"
    echo -e "8) 🗑️  ${RED}Eliminar Tot (Neteja completa)${NC}"
    echo -e "9) ❌ ${RED}Sortir${NC}"
    echo -e "\n${YELLOW}Opció [1-9]: ${NC}"
}

# Construir i executar
build_and_run() {
    echo -e "\n${BLUE}🔧 Construint imatge Docker...${NC}"
    docker-compose --profile full build
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Imatge construïda correctament${NC}"
        run_container
    else
        echo -e "${RED}❌ Error construint la imatge${NC}"
        exit 1
    fi
}

# Executar container
run_container() {
    echo -e "\n${BLUE}🚀 Executant BiciFood...${NC}"
    
    # Parar container anterior si existeix
    docker-compose down > /dev/null 2>&1
    
    # Executar amb profile 'full' per incloure l'aplicació
    docker-compose --profile full up -d
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Container executant-se${NC}"
        show_urls
        echo -e "\n${YELLOW}⏳ Esperant que els serveis estiguin llests...${NC}"
        sleep 10
        check_services
    else
        echo -e "${RED}❌ Error executant el container${NC}"
    fi
}

# Verificar serveis
check_services() {
    echo -e "\n${BLUE}🔍 Verificant serveis...${NC}"
    
    # Verificar backend
    for i in {1..10}; do
        if curl -s http://localhost:8080/api/health > /dev/null; then
            echo -e "${GREEN}✅ Backend: http://localhost:8080${NC}"
            break
        fi
        if [ $i -eq 10 ]; then
            echo -e "${YELLOW}⚠️  Backend encara no està llest${NC}"
        else
            sleep 3
        fi
    done
    
    # Verificar frontend
    if curl -s http://localhost:3000 > /dev/null; then
        echo -e "${GREEN}✅ Frontend: http://localhost:3000${NC}"
    else
        echo -e "${YELLOW}⚠️  Frontend encara no està llest${NC}"
    fi
}

# Mostrar URLs d'accés
show_urls() {
    echo -e "\n${BLUE}🌐 === URLs D'ACCÉS === ${NC}"
    echo -e "${BLUE}📱 Frontend Principal:${NC} http://localhost:3000"
    echo -e "${BLUE}🔗 Backend API:${NC} http://localhost:8080"
    echo -e "${BLUE}✅ Health Check:${NC} http://localhost:8080/api/health"
    echo -e "${BLUE}📚 Documentació API:${NC} http://localhost:3000/info-api.md"
    echo -e "${BLUE}🎨 Templates:${NC}"
    echo -e "   - Plantilla V2: http://localhost:3000/html/Plantilla%20V2/index.html"
    echo -e "   - Plantilla V3: http://localhost:3000/html/Plantilla%20V3/index.html"
    echo -e "   - Versió Final: http://localhost:3000/html/versio-final-TEA3/index.html"
}

# Veure logs
show_logs() {
    echo -e "\n${BLUE}📊 === LOGS DEL CONTAINER === ${NC}"
    echo -e "${YELLOW}Premeu Ctrl+C per sortir dels logs${NC}\n"
    docker-compose logs -f bicifood
}

# Estat del container
show_status() {
    echo -e "\n${BLUE}📊 === ESTAT DEL CONTAINER === ${NC}"
    docker-compose ps
    
    echo -e "\n${BLUE}📈 === ÚS DE RECURSOS === ${NC}"
    docker stats --no-stream bicifood-app 2>/dev/null || echo "Container no està executant-se"
}

# Parar container
stop_container() {
    echo -e "\n${YELLOW}🛑 Aturant container...${NC}"
    docker-compose down
    echo -e "${GREEN}✅ Container aturat${NC}"
}

# Neteja completa
cleanup() {
    echo -e "\n${RED}🗑️  Neteja completa...${NC}"
    docker-compose down
    docker system prune -f
    docker volume prune -f
    echo -e "${GREEN}✅ Neteja completada${NC}"
}

# Obrir URLs al navegador
open_urls() {
    echo -e "\n${BLUE}🌐 Obrint URLs al navegador...${NC}"
    
    # Detectar sistema operatiu
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        open http://localhost:3000
        open http://localhost:8080/api/health
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Linux
        xdg-open http://localhost:3000 2>/dev/null &
        xdg-open http://localhost:8080/api/health 2>/dev/null &
    else
        echo -e "${YELLOW}⚠️  Obre manualment:${NC}"
        show_urls
    fi
}

# Funció principal
main() {
    check_docker
    
    while true; do
        show_menu
        read -r choice
        
        case $choice in
            1)
                build_and_run
                ;;
            2)
                run_container
                ;;
            3)
                echo -e "\n${YELLOW}🔄 Reconstruint des de zero...${NC}"
                docker-compose down
                docker-compose --profile full build --no-cache
                run_container
                ;;
            4)
                show_logs
                ;;
            5)
                show_status
                ;;
            6)
                open_urls
                ;;
            7)
                stop_container
                ;;
            8)
                cleanup
                ;;
            9)
                echo -e "\n${GREEN}👋 Adéu!${NC}"
                exit 0
                ;;
            *)
                echo -e "${RED}❌ Opció invàlida${NC}"
                ;;
        esac
        
        echo -e "\n${YELLOW}Premeu Enter per continuar...${NC}"
        read -r
    done
}

# Executar script
main