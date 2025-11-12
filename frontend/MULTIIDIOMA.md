# Sistema Multiidioma - BiciFood

## 📋 Implementació actual

✅ Idiomes disponibles:
- **Català** (ca) - Idioma per defecte
- **Castellà** (es)

## 🚀 Com funciona

### 1. Estructura de fitxers

```
frontend/
├── js/
│   ├── i18n.js                    ← Sistema de traduccions
│   └── translations/
│       ├── ca.json                ← Traduccions català
│       └── es.json                ← Traduccions castellà
```

### 2. Incorporar a una pàgina HTML

Afegir al `<head>`:
```html
<script src="/frontend/js/i18n.js"></script>
```

Afegir selector d'idioma al header:
```html
<select id="lang-selector" class="form-select form-select-sm me-3" style="width: auto;">
    <option value="ca">🇪🇸 CA</option>
    <option value="es">🇪🇸 ES</option>
</select>
```

### 3. Marcar textos per traduir

#### Opció A: Text del element (textContent)
```html
<a class="nav-link" href="categories.html" data-i18n="header.categories">Categories</a>
```

#### Opció B: Placeholder d'input
```html
<input type="search" placeholder="Cerca..." data-i18n-placeholder="header.search">
```

#### Opció C: Títol (tooltip)
```html
<a href="cistella.html" data-i18n-title="header.cart">
    <i class="bi bi-cart-fill"></i>
</a>
```

### 4. Traduccions en JavaScript

Per contingut dinàmic (productes, missatges, etc.):

```javascript
// Obtenir traducció
const text = i18n.translate('search.viewDetails');

// Exemple en HTML dinàmic
const button = '<button>' + i18n.translate('product.addToCart') + '</button>';
```

### 5. Canviar idioma programàticament

```javascript
// Canviar a castellà
i18n.setLanguage('es');

// Obtenir idioma actual
const currentLang = i18n.getLanguage(); // 'ca' o 'es'
```

## 📝 Afegir noves traduccions

### 1. Editar fitxers JSON

Afegir la clau a ambdós fitxers (`ca.json` i `es.json`):

**ca.json:**
```json
{
  "product": {
    "newKey": "Text en català"
  }
}
```

**es.json:**
```json
{
  "product": {
    "newKey": "Texto en castellano"
  }
}
```

### 2. Usar en HTML

```html
<p data-i18n="product.newKey">Text en català</p>
```

## ✅ Pàgines implementades

- [x] `resultats_cerca.html` - Exemple complet implementat

## 📋 Pàgines pendents d'implementar

- [ ] `index.html`
- [ ] `categories.html`
- [ ] `detall_product.html`
- [ ] `cistella.html`
- [ ] `contacte.html`
- [ ] `nosaltres.html`
- [ ] `login.html`
- [ ] `registrat.html`
- [ ] Resta de pàgines...

## 🔧 Tasques per implementar completament

### Per cada pàgina HTML:

1. Afegir `<script src="/frontend/js/i18n.js"></script>` al head
2. Afegir selector d'idioma al header (copiar de `resultats_cerca.html`)
3. Marcar tots els textos amb `data-i18n`, `data-i18n-placeholder` o `data-i18n-title`
4. Si hi ha contingut dinàmic en JS, usar `i18n.translate('clau')`

### Temps estimat per pàgina: 30-45 minuts

## 💡 Bones pràctiques

1. **Nomenclatura de claus**: Usar estructura jeràrquica
   - ✅ `header.categories`
   - ✅ `product.addToCart`
   - ❌ `categoriesTitle`

2. **Consistència**: Mateix text → mateixa clau
   - "Contacte" sempre és `header.contact` i `footer.contact`

3. **Fallback**: Si falta traducció, es mostra la clau
   - Facilita detectar traduccions pendents

4. **LocalStorage**: L'idioma es guarda automàticament
   - L'usuari manté la seva preferència entre sessions

## 🧪 Testejar

1. Obrir `resultats_cerca.html`
2. Canviar selector d'idioma (CA ↔ ES)
3. Verificar que tots els textos canvien
4. Recarregar pàgina → Idioma es manté

## 🌍 Afegir més idiomes (futur)

1. Crear `frontend/js/translations/en.json`
2. Afegir opció al selector: `<option value="en">🇬🇧 EN</option>`
3. El sistema detectarà automàticament el nou idioma
