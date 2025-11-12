@echo off
echo ========================================
echo  Stopping BiciFood Application
echo ========================================
echo.

echo Stopping Backend...
taskkill /FI "WindowTitle eq BiciFood Backend*" /F >nul 2>&1

echo Stopping Frontend...
taskkill /FI "WindowTitle eq BiciFood Frontend*" /F >nul 2>&1

echo.
echo ========================================
echo  BiciFood stopped successfully!
echo ========================================
echo.
pause
