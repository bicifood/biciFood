#!/bin/bash
set -e

# Script per iniciar l'aplicació BiciFood completa (adaptat a Linux)
echo "INICIANT BICIFOOD - Aplicació completa"
echo "========================================"

# Ruta del projecte (ajusta si cal)
PROJECT_DIR="/home/isard/Documents/github_repos/biciFood"
cd "$PROJECT_DIR" || { echo "No existeix $PROJECT_DIR"; exit 1; }

# Comprovar eines requerides
for cmd in java mvn python3 curl xdg-open; do
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "Falta la comanda: $cmd. Instal·la-la abans d'executar."
    exit 1
  fi
done

# Cercar JAR (flexible amb comodí)
JAR_FILE=$(find "$PROJECT_DIR/backend/target/" -maxdepth 1 -type f -name "*bicifood*.jar" -print -quit || true)
if [ -z "$JAR_FILE" ]; then
  echo "📦 No s'ha trobat el fitxer JAR. Compilant el projecte..."
  cd "$PROJECT_DIR/backend"
  if mvn clean package -DskipTests; then
    echo "✅ Compilació exitosa!"
    JAR_FILE=$(find "$PROJECT_DIR/backend/target/" -maxdepth 1 -type f -name "*bicifood*.jar" -print -quit)
    if [ -z "$JAR_FILE" ]; then
      echo "❌ No s'ha produït el JAR esperat"
      exit 1
    fi
    cd "$PROJECT_DIR"
  else
    echo "❌ Error en la compilació del projecte"
    exit 1
  fi
else
  echo "JAR trobat: $JAR_FILE"
fi

# Iniciar backend en background
echo "Iniciant el backend (java -jar)..."
java -jar "$JAR_FILE" >/dev/null 2>&1 &
BACKEND_PID=$!

# Esperar que estigui ready (timeout)
echo "Esperant que el backend estigui llest (timeout 60s)..."
READY=0
for i in $(seq 1 30); do
  if curl -s -f http://localhost:8080/api/v1/actuator/health >/dev/null 2>&1; then
    READY=1
    break
  fi
  sleep 2
done

if [ "$READY" -ne 1 ]; then
  echo "❌ El backend no ha respost a /actuator/health."
  kill "$BACKEND_PID" 2>/dev/null || true
  exit 1
fi
echo "✅ Backend funcionant correctament!"

# Iniciar servidor per al frontend (port 3000)
echo "🌐 Iniciant servidor web per al frontend..."
python3 -m http.server 3000 >/dev/null 2>&1 &
FRONTEND_PID=$!
sleep 1

WEB_URL="http://localhost:3000/frontend/html/TEA5/"
echo "   Frontend: $WEB_URL"
echo "   Backend API: http://localhost:8080/api/v1"

# Obrir navegador (xdg-open a Linux, fallback a open)
if command -v xdg-open >/dev/null 2>&1; then
  xdg-open "$WEB_URL" >/dev/null 2>&1 || true
elif command -v open >/dev/null 2>&1; then
  open "$WEB_URL" >/dev/null 2>&1 || true
else
  echo "🔗 Obre manualment: $WEB_URL"
fi

echo "🎉 BICIFOOD ESTÀ LLEST!"
echo "Per aturar serveis executa: ./stop-bicifood.sh"
