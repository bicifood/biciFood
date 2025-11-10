@echo off
setlocal enabledelayedexpansion

echo ========================================
echo  Starting BiciFood Application
echo ========================================
echo.

REM Obtenir el directori arrel del projecte
set "PROJECT_DIR=%~dp0.."
cd /d "%PROJECT_DIR%"

REM Verificar Java
echo [0/3] Checking requirements...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 21 from: https://adoptium.net/
    pause
    exit /b 1
)

REM Verificar Python
where python >nul 2>&1
if errorlevel 1 (
    where py >nul 2>&1
    if errorlevel 1 (
        echo ERROR: Python is not installed or not in PATH
        echo Please install Python 3 from: https://www.python.org/downloads/
        pause
        exit /b 1
    ) else (
        set PYTHON_CMD=py
    )
) else (
    set PYTHON_CMD=python
)

echo [1/3] Starting Backend (Spring Boot)...

REM Check if Maven Wrapper exists, otherwise use Maven
if exist "%PROJECT_DIR%\backend\mvnw.cmd" (
    start "BiciFood Backend" cmd /k "cd /d "%PROJECT_DIR%\backend" && mvnw.cmd spring-boot:run"
) else (
    where mvn >nul 2>&1
    if errorlevel 1 (
        echo ERROR: Maven is not installed and Maven Wrapper not found
        echo Please install Maven from: https://maven.apache.org/download.cgi
        pause
        exit /b 1
    ) else (
        start "BiciFood Backend" cmd /k "cd /d "%PROJECT_DIR%\backend" && mvn spring-boot:run"
    )
)

echo [2/3] Waiting for backend to initialize...
timeout /t 8 /nobreak >nul

echo [3/3] Starting Frontend (Python HTTP Server)...
cd "%PROJECT_DIR%\frontend"
start "BiciFood Frontend" cmd /k "%PYTHON_CMD% -m http.server 3000"

cd "%PROJECT_DIR%"
echo.
echo ========================================
echo  BiciFood started successfully!
echo ========================================
echo  Backend:  http://localhost:8080
echo  Frontend: http://localhost:3000/html/TEA4/
echo ========================================
echo.
echo Press any key to open the application in your browser...
pause >nul

start http://localhost:3000/html/TEA4/index.html

endlocal
