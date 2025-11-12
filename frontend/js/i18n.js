// Sistema de traduccions i18n per BiciFood
class I18n {
    constructor() {
        this.currentLang = localStorage.getItem('bicifood_lang') || 'ca';
        this.translations = {};
        this.loadTranslations();
    }

    async loadTranslations() {
        try {
            // Carregar català
            const caResponse = await fetch('/frontend/js/translations/ca.json');
            this.translations.ca = await caResponse.json();
            
            // Carregar castellà
            const esResponse = await fetch('/frontend/js/translations/es.json');
            this.translations.es = await esResponse.json();
            
            // Carregar anglès
            const enResponse = await fetch('/frontend/js/translations/en.json');
            this.translations.en = await enResponse.json();
            
            // Aplicar traduccions inicials
            this.applyTranslations();
        } catch (error) {
            console.error('Error carregant traduccions:', error);
        }
    }

    setLanguage(lang) {
        if (this.translations[lang]) {
            this.currentLang = lang;
            localStorage.setItem('bicifood_lang', lang);
            this.applyTranslations();
            
            // Actualitzar selector visual
            const selector = document.getElementById('lang-selector');
            if (selector) {
                selector.value = lang;
            }
        }
    }

    getLanguage() {
        return this.currentLang;
    }

    translate(key) {
        const keys = key.split('.');
        let value = this.translations[this.currentLang];
        
        for (const k of keys) {
            if (value && value[k]) {
                value = value[k];
            } else {
                return key; // Retornar clau si no es troba traducció
            }
        }
        
        return value;
    }

    applyTranslations() {
        // Traduir elements amb data-i18n
        document.querySelectorAll('[data-i18n]').forEach(element => {
            const key = element.getAttribute('data-i18n');
            const translation = this.translate(key);
            
            // Decidir si actualitzar textContent o placeholder
            if (element.tagName === 'INPUT' || element.tagName === 'TEXTAREA') {
                if (element.hasAttribute('placeholder')) {
                    element.placeholder = translation;
                }
            } else {
                element.textContent = translation;
            }
        });

        // Traduir elements amb data-i18n-placeholder
        document.querySelectorAll('[data-i18n-placeholder]').forEach(element => {
            const key = element.getAttribute('data-i18n-placeholder');
            element.placeholder = this.translate(key);
        });

        // Traduir elements amb data-i18n-title
        document.querySelectorAll('[data-i18n-title]').forEach(element => {
            const key = element.getAttribute('data-i18n-title');
            element.title = this.translate(key);
        });
    }
}

// Crear instància global
const i18n = new I18n();

// Event listener per al selector d'idioma
document.addEventListener('DOMContentLoaded', () => {
    const langSelector = document.getElementById('lang-selector');
    if (langSelector) {
        // Establir valor inicial
        langSelector.value = i18n.getLanguage();
        
        // Listener per canvis
        langSelector.addEventListener('change', (e) => {
            i18n.setLanguage(e.target.value);
        });
    }
});
