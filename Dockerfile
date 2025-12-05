# Etapa 1: Compilació del backend
FROM maven:3.9-eclipse-temurin-21 AS backend-build

WORKDIR /app/backend

# Copiar pom.xml i descarregar dependències
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

# Copiar codi font i compilar
COPY backend/src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Execució
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Instal·lar nginx per servir el frontend
RUN apt-get update && \
    apt-get install -y nginx && \
    rm -rf /var/lib/apt/lists/*

# Copiar JAR del backend
COPY --from=backend-build /app/backend/target/bicifood-api-*.jar /app/backend.jar

# Copiar fitxers del frontend
COPY frontend /usr/share/nginx/html/frontend

# Crear configuració nginx
RUN echo 'server {\n\
    listen 3000;\n\
    server_name localhost;\n\
    root /usr/share/nginx/html;\n\
    index index.html;\n\
    location / {\n\
    try_files $uri $uri/ =404;\n\
    }\n\
    location /api {\n\
    proxy_pass http://localhost:8080/api;\n\
    proxy_set_header Host $host;\n\
    proxy_set_header X-Real-IP $remote_addr;\n\
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;\n\
    }\n\
    }' > /etc/nginx/sites-available/default

# Crear script d'inici
RUN echo '#!/bin/bash\n\
    nginx\n\
    exec java -jar /app/backend.jar' > /app/start.sh && \
    chmod +x /app/start.sh

# Exposar ports
EXPOSE 8080 3000

# Iniciar ambdós serveis
CMD ["/app/start.sh"]
