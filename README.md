# BiciFood

Aplicació web per a comandes de menjar a domicili amb repartiment en bicicleta.

## Tecnologies

- **Backend**: Spring Boot 3.3.5 + Java 21
- **Frontend**: HTML, CSS, Bootstrap 5.3.3, JavaScript
- **Base de dades**: H2 (en memòria)

## Requisits previs

- Java 21
- Python 3
- Maven (opcional, el projecte inclou Maven Wrapper)

## Iniciar l'aplicació

### macOS / Linux

```bash
cd scripts-execucio/unix-scripts
./start-bicifood.sh
```

Per aturar:
```bash
./stop-bicifood.sh
```

### Windows

1. Obrir la carpeta `scripts-execucio/windows-scripts`
2. Fer doble clic a `start-bicifood.bat`

Per aturar:
- Fer doble clic a `stop-bicifood.bat`

**Nota**: Per a instruccions detallades de Windows, consultar `scripts-execucio/windows-scripts/WINDOWS_SETUP.md`

## Accés

- Frontend: http://localhost:3000/frontend/html/TEA5/index.html
- Backend API: http://localhost:8080/api/v1
