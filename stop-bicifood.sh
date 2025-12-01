#!/bin/bash

# =================================
# BiciFood - Script per aturar l'aplicació
# =================================

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "🛑 Aturant BiciFood..."

# Aturar el servidor frontend (HTTP server)
echo "🌐 Aturant servidor frontend..."
pkill -f 'python3.*http.server.*3000' 2>/dev/null
rm -f "$PROJECT_DIR/.frontend.pid" 2>/dev/null

# Aturar el backend (Java application)
echo "⚙️  Aturant servidor backend..."
pkill -f 'java.*bicifood-api' 2>/dev/null

echo ""
echo "✅ BiciFood aturat correctament!"
echo ""