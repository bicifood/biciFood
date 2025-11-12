# Scripts d'Execució BiciFood

Aquest directori conté els scripts per executar l'aplicació BiciFood en diferents sistemes operatius.

## 📁 Estructura

```
scripts-execucio/
├── unix-scripts/          # Scripts per macOS/Linux
│   ├── start-bicifood.sh
│   └── stop-bicifood.sh
└── windows-scripts/       # Scripts per Windows
    ├── start-bicifood.bat
    ├── stop-bicifood.bat
    └── WINDOWS_SETUP.md   # Guia completa per Windows
```

## 🚀 Inici Ràpid

### Windows
1. Obrir la carpeta `windows-scripts`
2. Llegir **WINDOWS_SETUP.md** per instal·lar els requisits
3. Fer doble clic a `start-bicifood.bat`

### macOS/Linux
1. Obrir terminal a la carpeta `unix-scripts`
2. Executar: `./start-bicifood.sh`

## 📋 Requisits

### Tots els Sistemes
- **Java 21**: https://adoptium.net/
- **Python 3**: https://www.python.org/downloads/
- **Maven**: https://maven.apache.org/download.cgi

### Verificar Instal·lació
```bash
# Comprovar Java
java -version

# Comprovar Python
python --version    # o py --version en Windows

# Comprovar Maven
mvn -version
```

## 🌐 URLs de l'Aplicació

Després d'executar els scripts:
- **Frontend**: http://localhost:3000/html/TEA4/index.html
- **Backend API**: http://localhost:8080/api/v1

## ❓ Ajuda

- **Windows**: Veure `windows-scripts/WINDOWS_SETUP.md`
- **Problemes comuns**: Verificar que Java, Python i Maven estiguin al PATH
- **Primera execució**: Maven descarregarà dependències (5-10 minuts)

## 🛑 Aturar l'Aplicació

### Windows
Fer doble clic a `stop-bicifood.bat` o tancar les finestres CMD

### macOS/Linux
Executar `./stop-bicifood.sh`
