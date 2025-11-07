#!/bin/bash

# Script per iniciar l'aplicació BiciFood completa
echo "🚴‍♂️ INICIANT BICIFOOD - Aplicació completa"
echo "========================================"
echo "Aquest script farà el següent:"
echo "  1. 📦 Compilar el backend (si cal)"
echo "  2. 🚀 Iniciar el backend Spring Boot"
echo "  3. 🌐 Iniciar el servidor web del frontend"
echo "  4. 🌍 Obrir l'aplicació al navegador"
echo ""

# Canviar al directori del projecte
PROJECT_DIR="/Users/dadiazpr/Documents/github_repos/biciFood"
cd "$PROJECT_DIR"

# Verificar que el JAR existeix
JAR_FILE="backend/target/bicifood-api-1.0.0.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "📦 No s'ha trobat el fitxer JAR. Compilant el projecte..."
    echo "   Executant: mvn clean package -DskipTests"
    
    cd "$PROJECT_DIR/backend"
    if mvn clean package -DskipTests; then
        echo "✅ Compilació exitosa!"
        cd "$PROJECT_DIR"
    else
        echo "❌ Error en la compilació del projecte"
        exit 1
    fi
else
    echo "✅ JAR trobat: $JAR_FILE"
fi

# Iniciar el backend
echo "🚀 Iniciant el backend de BiciFood..."
echo "   Port: 8080"
echo "   Swagger UI: http://localhost:8080/api/v1/swagger-ui.html"
echo ""

# Esperar una mica abans d'obrir el navegador
echo "⏳ Esperant que el backend s'inicialitzi..."

# Executar el backend en background
java -jar "$JAR_FILE" &
BACKEND_PID=$!

# Esperar que el backend estigui llest
sleep 8

# Verificar que el backend està funcionant
echo "🔍 Verificant que el backend està funcionant..."
if curl -s http://localhost:8080/api/v1/actuator/health > /dev/null; then
    echo "✅ Backend funcionant correctament!"
    
    # Iniciar servidor HTTP per servir el frontend des de l'arrel del projecte
    echo ""
    echo "🌐 Iniciant servidor web per al frontend..."
    cd "$PROJECT_DIR"
    python3 -m http.server 3000 > /dev/null 2>&1 &
    FRONTEND_PID=$!
    
    # Esperar un moment per què el servidor s'iniciï
    sleep 2
    
    # Obrir l'aplicació web al navegador
    WEB_URL="http://localhost:3000/frontend/html/TEA4/"
    echo "   Frontend servidor: http://localhost:3000/frontend/html/TEA4/"
    echo "   Backend API: http://localhost:8080/api/v1"
    echo ""
    echo "🌐 Obrint l'aplicació web al navegador..."
    
    # Obrir en el navegador per defecte
    open "$WEB_URL"
    
    echo ""
    echo "🎉 BICIFOOD ESTÀ LLEST!"
    echo "========================================"
    echo "✅ Frontend: http://localhost:3000/frontend/html/TEA4/"
    echo "✅ Backend: http://localhost:8080/api/v1"
    echo "✅ API Docs: http://localhost:8080/api/v1/swagger-ui.html"
    echo ""
    echo "📝 Per aturar els serveis:"
    echo "   • Backend: Ctrl+C al terminal del backend"
    echo "   • Frontend: pkill -f 'python3.*http.server.*3000'"
    echo ""
    
    # Guardar el PID del servidor frontend per facilitar l'aturada
    echo $FRONTEND_PID > "$PROJECT_DIR/.frontend.pid"
    
    # Mantenir el backend funcionant
    wait $BACKEND_PID
    
else
    echo "❌ Error: El backend no s'ha iniciat correctament"
    kill $BACKEND_PID 2>/dev/null
    exit 1
fi