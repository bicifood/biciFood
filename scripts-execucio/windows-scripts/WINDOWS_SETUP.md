# BiciFood - Guia d'Instal·lació per Windows

## Requisits

### 1. Java 21
- Descarregar de: https://adoptium.net/temurin/releases/?version=21
- Seleccionar: Windows x64 (.msi installer)
- Durant la instal·lació, marcar "Add to PATH"
- Verificar: Obrir CMD i executar `java -version`

### 2. Python 3
- Descarregar de: https://www.python.org/downloads/
- **IMPORTANT**: Marcar "Add Python to PATH" durant la instal·lació
- Verificar: Obrir CMD i executar `python --version` o `py --version`

### 3. Maven
- Descarregar de: https://maven.apache.org/download.cgi
- Seguir les instruccions d'instal·lació: https://maven.apache.org/install.html
- **IMPORTANT**: Afegir Maven a PATH
- Verificar: Obrir CMD i executar `mvn -version`

## Com executar l'aplicació

### Opció 1: Doble clic (Recomanat)
1. Anar a la carpeta `windows-scripts`
2. Fer doble clic a `start-bicifood.bat`
3. Esperar que s'iniciin els dos serveis
4. El navegador s'obrirà automàticament

### Opció 2: Línia de comandes
```cmd
cd windows-scripts
start-bicifood.bat
```

## Com aturar l'aplicació

### Opció 1: Doble clic
1. Anar a la carpeta `windows-scripts`
2. Fer doble clic a `stop-bicifood.bat`

### Opció 2: Tancar finestres
- Tancar les dues finestres CMD titulades "BiciFood Backend" i "BiciFood Frontend"

## Resolució de problemes

### "Java is not installed or not in PATH"
- Instal·lar Java 21 des de https://adoptium.net/
- Assegurar-se de marcar "Add to PATH" durant la instal·lació
- Reiniciar CMD/PowerShell després de la instal·lació

### "Python is not installed or not in PATH"
- Instal·lar Python 3 des de https://www.python.org/downloads/
- Durant la instal·lació, marcar "Add Python to PATH"
- Reiniciar CMD/PowerShell després de la instal·lació

### "Maven is not installed"
- Descarregar Maven des de https://maven.apache.org/download.cgi
- Descomprimir a C:\Program Files\Apache\maven
- Afegir a PATH: C:\Program Files\Apache\maven\bin
- Reiniciar CMD/PowerShell després de configurar
- Verificar amb: `mvn -version`

### Port ja en ús
- Backend (8080): Una altra aplicació Java pot estar executant-se
- Frontend (3000): Un altre servidor web pot estar executant-se
- Executar `stop-bicifood.bat` per aturar instàncies prèvies

### Permís denegat
- Clic dret a `start-bicifood.bat` → "Executar com a administrador"

## URLs d'accés

- **Frontend**: http://localhost:3000/html/TEA4/index.html
- **Backend API**: http://localhost:8080/api/v1
- **Documentació API**: http://localhost:8080/swagger-ui.html

## Primera execució

La primera vegada que executis l'aplicació:
1. Maven descarregarà les dependències (pot trigar 5-10 minuts)
2. Això és normal - les següents execucions seran molt més ràpides
3. Assegura't de tenir connexió estable a Internet
