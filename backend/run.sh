#!/bin/bash

# 🚴‍♂️ BiciFood API - Script d'execució
# Aquest script comprova els prerequisits i executa l'aplicació Spring Boot

echo "🚴‍♂️ BiciFood API - Iniciant aplicació..."
echo "=========================================="

# Comprovar Java 21
echo "🔍 Comprovant Java..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "1\.\K\d+' || java -version 2>&1 | grep -oP 'version "\K\d+')
    echo "✅ Java versió detectada: $JAVA_VERSION"
    
    if [ "$JAVA_VERSION" -lt 21 ]; then
        echo "❌ Error: Es requereix Java 21 o superior"
        echo "   Versió actual: $JAVA_VERSION"
        exit 1
    fi
else
    echo "❌ Error: Java no està instal·lat"
    echo "   Instal·la Java 21 LTS des de: https://adoptium.net/"
    exit 1
fi

# Comprovar Maven
echo "🔍 Comprovant Maven..."
if command -v mvn &> /dev/null; then
    MAVEN_VERSION=$(mvn -version | grep "Apache Maven" | awk '{print $3}')
    echo "✅ Maven versió: $MAVEN_VERSION"
else
    echo "❌ Error: Maven no està instal·lat"
    echo "   Instal·la Maven des de: https://maven.apache.org/download.cgi"
    exit 1
fi

# Comprovar MySQL
echo "🔍 Comprovant connexió MySQL..."
if command -v mysql &> /dev/null; then
    echo "✅ MySQL client detectat"
    
    # Intentar connectar (opcional - només avís)
    if ! mysql -u root -e "SELECT 1;" &> /dev/null; then
        echo "⚠️  Advertència: No es pot connectar a MySQL amb usuari root sense contrasenya"
        echo "   Assegura't que MySQL està funcionant i configura application.properties"
    else
        echo "✅ Connexió MySQL correcta"
        
        # Comprovar si existeix la base de dades
        if mysql -u root -e "USE bicifood_db;" &> /dev/null; then
            echo "✅ Base de dades bicifood_db trobada"
        else
            echo "⚠️  Advertència: Base de dades bicifood_db no trobada"
            echo "   Executa l'script SQL: bicifood_db_v5.0/bicifood_db_v5.0.sql"
        fi
    fi
else
    echo "⚠️  MySQL client no detectat - assegura't que MySQL està funcionant"
fi

echo ""
echo "🚀 Iniciant compilació i execució..."
echo "======================================"

# Netejar i compilar
echo "🧹 Netejant projecte..."
mvn clean

echo "📦 Compilant projecte..."
if mvn compile; then
    echo "✅ Compilació exitosa"
else
    echo "❌ Error en la compilació"
    exit 1
fi

# Executar aplicació
echo ""
echo "🎯 Executant BiciFood API..."
echo "=============================="
echo ""
echo "📍 L'API estarà disponible a: http://localhost:8080/api/v1"
echo "📖 Documentació Swagger: http://localhost:8080/api/v1/swagger-ui.html"
echo "🏥 Health Check: http://localhost:8080/api/v1/actuator/health"
echo ""
echo "Press Ctrl+C per aturar l'aplicació"
echo ""

# Executar amb profiles de desenvolupament
mvn spring-boot:run -Dspring-boot.run.profiles=dev

echo ""
echo "👋 BiciFood API aturat. Fins aviat!"