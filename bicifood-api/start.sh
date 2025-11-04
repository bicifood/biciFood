#!/bin/bash

# 🚴‍♂️ Script d'execució estable per BiciFood API - Java 21 LTS
# Aquest script automatitza el procés d'aixecar l'API amb Virtual Threads

echo "🚴‍♂️ Iniciant BiciFood API (Java 21 LTS)..."

# Verificar versió Java
java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
echo "☕ Detectada Java: $java_version"

if [[ "$java_version" < "21" ]]; then
    echo "⚠️  Advertència: Es recomana Java 21+ per millor rendiment"
fi

# Directori del projecte
PROJECT_DIR="/Users/dadiazpr/Documents/github_repos/biciFood/bicifood-api"
cd "$PROJECT_DIR"

# Verificar que estem al directori correcte
if [ ! -f "pom.xml" ]; then
    echo "❌ Error: No s'ha trobat pom.xml. Verifica el directori."
    exit 1
fi

# Matar processos anteriors al port 8080
echo "🔄 Netejant processos anteriors..."
lsof -ti:8080 | xargs kill -9 2>/dev/null || echo "   No hi ha processos anteriors al port 8080"

# Compilar l'aplicació
echo "🔨 Compilant l'aplicació..."
mvn clean package -DskipTests -q

if [ $? -ne 0 ]; then
    echo "❌ Error en la compilació"
    exit 1
fi

# Verificar que el JAR existeix
JAR_FILE="target/bicifood-api-1.0.0.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "❌ Error: No s'ha trobat el JAR executable"
    exit 1
fi

# Executar l'aplicació en background
echo "🚀 Executant l'aplicació..."
nohup java -jar "$JAR_FILE" > app.log 2>&1 &
APP_PID=$!

echo "   PID de l'aplicació: $APP_PID"

# Esperar que l'aplicació s'iniciï
echo "⏳ Esperant que l'aplicació s'iniciï..."
sleep 8

# Verificar que l'aplicació està funcionant
echo "🧪 Verificant l'API..."
response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/v1/categories)

if [ "$response" = "200" ]; then
    echo "✅ API funcionant correctament!"
    echo ""
    echo "🌐 URLs disponibles:"
    echo "   • API: http://localhost:8080/api/v1/categories"
    echo "   • Swagger UI: http://localhost:8080/swagger-ui.html"
    echo "   • H2 Console: http://localhost:8080/h2-console"
    echo ""
    echo "📋 Per aturar l'aplicació:"
    echo "   kill $APP_PID"
    echo "   o bé: lsof -ti:8080 | xargs kill -9"
else
    echo "❌ L'API no respon correctament (HTTP: $response)"
    echo "📋 Revisa els logs: tail -f app.log"
    exit 1
fi