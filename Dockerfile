# Utilitzem una imatge base amb Java 17 i Maven
FROM openjdk:17-jdk-slim

# Instal·lar dependencies necessàries
RUN apt-get update && apt-get install -y \
    maven \
    python3 \
    python3-pip \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Establir directori de treball
WORKDIR /app

# Copiar tots els fitxers del projecte
COPY . .

# Compilar el backend Spring Boot
WORKDIR /app/backend
RUN mvn clean package -DskipTests

# Tornar al directori arrel
WORKDIR /app

# Exposar ports
EXPOSE 8080 3000

# Crear script d'inici que executa backend i frontend
RUN echo '#!/bin/bash\n\
echo "🚀 Iniciant BiciFood - Backend + Frontend"\n\
echo "📱 Frontend: http://localhost:3000"\n\
echo "🔗 Backend: http://localhost:8080"\n\
echo "📚 API Docs: http://localhost:3000/info-api.md"\n\
\n\
# Iniciar backend en background\n\
cd /app/backend\n\
java -jar target/*.jar &\n\
BACKEND_PID=$!\n\
echo "✅ Backend iniciat amb PID: $BACKEND_PID"\n\
\n\
# Esperar que el backend estigui llest\n\
echo "⏳ Esperant que el backend estigui llest..."\n\
for i in {1..30}; do\n\
  if curl -f http://localhost:8080/api/health > /dev/null 2>&1; then\n\
    echo "✅ Backend llest!"\n\
    break\n\
  fi\n\
  echo "   Esperant... ($i/30)"\n\
  sleep 2\n\
done\n\
\n\
# Iniciar frontend\n\
cd /app\n\
echo "✅ Iniciant servidor frontend..."\n\
python3 -m http.server 3000\n\
' > /app/start.sh && chmod +x /app/start.sh

# Comando per defecte
CMD ["/app/start.sh"]