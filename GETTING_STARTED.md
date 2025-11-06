# 🚴‍♂️ BiciFood - Aplicació de Menjar a Domicili

## Scripts Disponibles

### `./start-bicifood.sh` (RECOMANAT)
**Script complet per iniciar tota l'aplicació:**
- ✅ Inicia el backend automàticament
- ✅ Verifica que funciona correctament  
- ✅ Obre l'aplicació web al navegador
- ✅ Mostra tota la informació necessària

```bash
./start-bicifood.sh
```

### `./start-backend.sh`
**Script només per al backend:**
- Inicia només el servidor backend
- No obre el navegador
- Útil per desenvolupament

```bash
./start-backend.sh
```

## Com utilitzar l'aplicació

1. **Executar l'aplicació completa:**
   ```bash
   ./start-bicifood.sh
   ```

2. **S'obrirà automàticament al navegador:**
   - Pàgina principal amb productes
   - Pots afegir productes al carret
   - Anar a la cistella per veure els productes

3. **URLs importants:**
   - **Aplicació Web:** `file:///.../html/versio-final-TEA3/index.html`
   - **API Backend:** `http://localhost:8080`
   - **Documentació API:** `http://localhost:8080/api/v1/swagger-ui.html`

## Aturar l'aplicació

Per aturar l'aplicació, prem `Ctrl+C` al terminal on s'executa.

## Funcionalitats

- 🛍️ **Catàleg de productes** - Veure tots els productes disponibles
- 🛒 **Carret de compra** - Afegir/eliminar productes
- 📱 **Interfície responsive** - Funciona en mòbil i desktop
- 🔄 **Temps real** - Les dades es carreguen des de l'API

## Tecnologies

- **Backend:** Spring Boot 3.3.5 + H2 Database
- **Frontend:** HTML5 + JavaScript ES6 + Bootstrap 5.3.3
- **Build:** Maven 3.9.11
- **Java:** 21