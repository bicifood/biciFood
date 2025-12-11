/**
 * BiciFood API Client
 * Gestiona totes les comunicacions amb l'API backend
 */

class BiciFoodAPI {
    constructor() {
        this.baseURL = 'http://localhost:8080/api/v1';
        this.headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };
    }

    /**
     * Fa una petició HTTP genèrica
     */
    async makeRequest(endpoint, method = 'GET', data = null) {
        const config = {
            method,
            headers: this.headers
        };

        if (data) {
            config.body = JSON.stringify(data);
        }

        try {
            const response = await fetch(`${this.baseURL}${endpoint}`, config);
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const result = await response.json();
            return result;
        } catch (error) {
            console.error('Error en la petició API:', error);
            throw error;
        }
    }

    /**
     * Obté totes les categories
     */
    async getCategories() {
        return await this.makeRequest('/categories');
    }

    /**
     * Obté tots els productes
     */
    async getProducts(page = 0, size = 20) {
        return await this.makeRequest(`/products?page=${page}&size=${size}`);
    }

    /**
     * Obté productes per categoria
     */
    async getProductsByCategory(categoryId, page = 0, size = 20) {
        return await this.makeRequest(`/products/category/${categoryId}?page=${page}&size=${size}`);
    }

    /**
     * Obté un producte per ID
     */
    async getProductById(id) {
        return await this.makeRequest(`/products/${id}`);
    }

    /**
     * Cerca productes per nom
     */
    async searchProducts(query, page = 0, size = 20) {
        return await this.makeRequest(`/products/search?name=${encodeURIComponent(query)}&page=${page}&size=${size}`);
    }
}

// Instància global de l'API
const api = new BiciFoodAPI();

/**
 * Utilitats per al DOM
 */
const Utils = {
    /**
     * Carrega les categories al dropdown del navbar
     */
    async loadCategoriesDropdown() {
        try {
            const categories = await api.getCategories();
            const dropdown = document.getElementById('categoriesDropdown');
            
            if (dropdown) {
                dropdown.innerHTML = '';
                categories.forEach(category => {
                    const li = document.createElement('li');
                    li.innerHTML = `<a class="dropdown-item" href="categories.html?id=${category.id}">${category.nom}</a>`;
                    dropdown.appendChild(li);
                });
            }
        } catch (error) {
            console.error('Error carregant categories:', error);
        }
    },

    /**
     * Carrega productes en un contenidor
     */
    async loadProducts(containerId, categoryId = null, page = 0, size = 20) {
        try {
            let products;
            if (categoryId) {
                products = await api.getProductsByCategory(categoryId, page, size);
            } else {
                products = await api.getProducts(page, size);
            }

            const container = document.getElementById(containerId);
            if (container) {
                container.innerHTML = '';
                
                if (products.content && products.content.length > 0) {
                    products.content.forEach(product => {
                        const productCard = this.createProductCard(product);
                        container.appendChild(productCard);
                    });
                    // Afegir event listeners després de crear les targetes
                    addCartEventListeners();
                } else {
                    container.innerHTML = '<p class="text-center">No hi ha productes disponibles.</p>';
                }
            }
        } catch (error) {
            console.error('Error carregant productes:', error);
            const container = document.getElementById(containerId);
            if (container) {
                container.innerHTML = '<p class="text-center text-danger">Error carregant els productes.</p>';
            }
        }
    },

    /**
     * Crea una targeta de producte
     */
    createProductCard(product) {
        const card = document.createElement('div');
        card.className = 'col-md-4 col-lg-3 mb-4';
        
        const stockLabel = window.i18n ? window.i18n.translate('product.stock') : 'Stock';
        const addToCartLabel = window.i18n ? window.i18n.translate('product.addToCart') : 'Afegir a la cistella';
        const outOfStockLabel = window.i18n ? window.i18n.translate('product.outOfStock') : 'Esgotat';
        const imgPath = DetallUtils.getProductImagePath(product);
        console.log('Ruta de imagen (card):', imgPath);

        card.innerHTML = `
            <div class="card h-100 product-card">
                <a href="detall_product.html?id=${
                  product.id
                }">
                <img src="${imgPath}" 
                     class="card-img-top product-image" 
                     alt="${product.nom}"
                     style="height: 200px; object-fit: cover;"
                     onerror="this.src='../../images/placeholder-product.jpg'; this.alt='Imatge no disponible'"></a>
                <div class="card-body d-flex flex-column">
                    <h6 class="card-title">${product.nom}</h6>
                    <p class="card-text text-muted small">${product.descripcio}</p>
                    <div class="mt-auto">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="fw-bold text-success">${product.preu}€</span>
                            <span class="small text-muted">${stockLabel}: ${product.stock}</span>
                        </div>
                        <button
                            class="btn btn-primary w-100 add-to-cart-btn" 
                            data-product-id="${product.id}"
                            data-product-name="${product.nom.replace(/"/g, "&quot;")}"
                            data-product-price="${product.preu}"
                            data-product-image="${DetallUtils.getProductImagePath(
                              product
                            ).replace(/"/g, "&quot;")}"
                            ${product.stock <= 0 ? "disabled" : ""}>
                            ${product.stock <= 0 ? outOfStockLabel : addToCartLabel}
                        </button>
                    </div>
                </div>
            </div>
        `;
        
        return card;
    },

    /**
     * Gestiona les imatges dels productes amb fallback
     */
    getProductImagePath(imagePath) {
        if (!imagePath) return '../../images/placeholder-product.jpg';
        
        // Si ja té la ruta completa, la utilitzem
        if (imagePath.startsWith('../../images/')) return imagePath;
        
        // Si té el prefix 'images/', el treiem
        const cleanPath = imagePath.replace('images/', '');
        return `../../images/${cleanPath}`;
    },

    /**
     * Afegeix gestió d'errors per a imatges
     */
    handleImageError(img) {
        img.onerror = function() {
            this.src = '../../images/placeholder-product.jpg';
            this.alt = 'Imatge no disponible';
        };
    },

    /**
     * Obté paràmetres de la URL
     */
    getURLParameter(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    },

    /**
     * Carrega els botons de filtres de categoria
     */
    async loadCategoryFilters() {
        const filterContainer = document.getElementById('category-filters');
        if (!filterContainer) return;

        try {
            const categories = await api.getCategories();
            
            categories.forEach(category => {
                const button = document.createElement('button');
                button.type = 'button';
                button.className = 'btn btn-brown';
                button.textContent = category.nom;
                button.onclick = () => {
                    // Eliminar classe active de tots els botons
                    filterContainer.querySelectorAll('.btn').forEach(btn => btn.classList.remove('active'));
                    // Afegir classe active al botó actual
                    button.classList.add('active');
                    // Carregar productes de la categoria
                    Utils.loadProducts('products-container', category.id);
                };
                
                filterContainer.appendChild(button);
            });
        } catch (error) {
            console.error('Error carregant filtres de categoria:', error);
        }
    },

    /**
     * Filtra tots els productes (elimina filtres de categoria)
     */
    filterAllProducts() {
        const filterContainer = document.getElementById('category-filters');
        if (filterContainer) {
            // Eliminar classe active de tots els botons
            filterContainer.querySelectorAll('.btn').forEach(btn => btn.classList.remove('active'));
            // Afegir classe active al botó "Tots"
            filterContainer.querySelector('.btn').classList.add('active');
        }
        // Carregar tots els productes
        Utils.loadProducts('products-container');
    },

    /**
     * Mostra missatges d'alerta personalitzats
     */
    showAlert(message, type = 'info') {
        // Crear l'alerta personalitzada
        const alertDiv = document.createElement('div');
        alertDiv.className = `custom-alert ${type}`;
        alertDiv.textContent = message;
        
        // Afegir al body
        document.body.appendChild(alertDiv);
        
        // Eliminar automàticament després de 2 segons
        setTimeout(() => {
            alertDiv.classList.add('fade-out');
            // Eliminar del DOM després de l'animació
            setTimeout(() => {
                if (alertDiv.parentNode) {
                    alertDiv.parentNode.removeChild(alertDiv);
                }
            }, 500);
        }, 2000);
    }
};
/**
 * Creació del detall del producte
 */
const DetallUtils = {
  fillDetallProduct(product) {
    // Omplir el select de quantitat segons l'stock
    const quantitySelect = document.getElementById("quantity");
    quantitySelect.innerHTML = "";
    for (let i = 0; i < product.stock; i++) {
      const option = document.createElement("option");
      option.value = i + 1;
      option.textContent = i + 1;
      quantitySelect.appendChild(option);
    }

    document.getElementById("product-image").src =
      DetallUtils.getProductImagePath(product);
  },

  createDetallCard(product) {
    const detallContainer = document.getElementById("detall-producte");
    detallContainer.className = "row mt-4";

    const stockLabel = window.i18n
      ? window.i18n.translate("product.stock")
      : "Stock";
    const addToCartLabel = window.i18n
      ? window.i18n.translate("product.addToCart")
      : "Afegir a la cistella";
    const outOfStockLabel = window.i18n
      ? window.i18n.translate("product.outOfStock")
      : "Esgotat";

    let quantityOptions = "";
    for (let i = 0; i < product.stock; i++) {
      quantityOptions += `<option value="${i + 1}">${i + 1}</option>`;
    }

    detallContainer.innerHTML = `
  <div class="row g-4 align-items-start">
   
    <!-- Columna izquierda: imagen -->
    <div class="col-md-6 text-center">
      <img id="product-image" src="${DetallUtils.getProductImagePath(
        product
      )}" class="img-fluid imatge" alt="${product.nom}">
    </div>

    <!-- Columna dreta: info del producte -->
    <div class="col-md-6 d-flex flex-column gap-3">
      <!-- Título y precio -->
      <div class="d-flex justify-content-between align-items-center">
        <h2 id="product-name" class="mb-0">${product.nom}</h2>
        <p id="product-price" class="fw-bold fs-4 mb-0">${product.preu.toFixed(
          2
        )}€</p>
      </div>

      <hr>

      <!-- Quantitat -->
      <div class="d-flex align-items-center gap-2">
        <label for="quantity" class="mb-0">Quantitat</label>
        <select id="quantity" class="form-select" style="width: 70px; height: 40px;">
          ${[...Array(product.stock).keys()]
            .slice(0, 10)
            .map((n) => `<option value="${n + 1}">${n + 1}</option>`)
            .join("")}
        </select>
      </div>

      <!-- Descripció -->
      <p id="product-description" data-i18n="product.description">${
        product.descripcio || "Sense descripció disponible"}</p>

      <!-- Botó afegir a la cistella -->
        <button
            class="btn btn-primary w-50 add-to-cart-btn" 
            data-product-id="${product.id}"
            data-product-name="${product.nom.replace(/"/g, "&quot;")}"
            data-product-price="${product.preu}"
            data-product-image="${DetallUtils.getProductImagePath(
              product
            ).replace(/"/g, "&quot;")}"
            ${product.stock <= 0 ? "disabled" : ""}>
            ${product.stock <= 0 ? outOfStockLabel : addToCartLabel}
        </button>
        
    </div>
    <h4 data-i18n="product.relatedProducts">Productes relacionats</h4>
  </div>
`;
    addCartEventListeners();
    return detallContainer;
  },

  createRelacionatsCard(product) {
    const cardRelacionats = document.createElement("div");
    cardRelacionats.className = "relacionats__card";
    cardRelacionats.style.width = "200px";

    cardRelacionats.innerHTML = `
    <div class="bg-light border border-light-subtle rounded relacionats__box">
          <a href="detall_product.html?id=${product.id}">
          <img class="img-thumbnail relacionats__imatge" src="${DetallUtils.getProductImagePath(
                product)}" alt=""></a>
    </div>`;

    return cardRelacionats;
  },

  // Obté productes relacionats per categoria
  async getProductsRelacionats(categoriaId, product) {
    let productosCategoria = [];
    let descripcio = product.descripcio;
    let descripcioIgual = [];
    try {
      let response = await api.getProductsByCategory(categoriaId, 0, 20);
      const productos = response.content || [];
      if (descripcio.includes() === product.descripcio) {
        descripcioIgual.push(product);
      }
      for (let i = 0; i < productos.length; i++) {
        if (productos[i].categoriaId === categoriaId) {
          productosCategoria.push(productos[i]);
          if (descripcioIgual[i]) {
            productosCategoria.push(descripcioIgual[i]);
          }
          if (i === 5) {
            break;
          }
        }
      }
      return productosCategoria.sort(() => Math.random() - 0.5).slice(0, 6);
    } catch (error) {
      console.error("Error obtenint productes relacionats:", error);
      return [];
    }
  },
// Afegeix els productes relacionats al DOM
  nouRelacionat(categoria) {
    const containerRelacionats = document.getElementById(
      "relacionats__quadricula"
    );
    containerRelacionats.innerHTML = "";
    containerRelacionats.innerHTML = "";
    containerRelacionats.style.display = "flex";         // fila horizontal
    containerRelacionats.style.flexWrap = "nowrap";     // sin saltos de línea
    containerRelacionats.style.gap = "1rem";           // espacio entre tarjetas
    containerRelacionats.style.overflowX = "auto";     // scroll horizontal si no caben
    containerRelacionats.style.padding = "1rem 0";     
    categoria.forEach((product) => {
      const card = DetallUtils.createRelacionatsCard(product);
      card.style.flex = "0 0 auto"; // evitar que las tarjetas se reduzcan
      containerRelacionats.appendChild(card);
    });
  },
  /**
   * Gestiona les imatges dels productes amb fallback
   */
  getProductImagePath(product) {
    // Si el product no tiene imatgePath, devolvemos placeholder
    if (!product || !product.imatgePath) {
      return "../../images/placeholder-product.jpg";
    }

    let cleanPath = product.imatgePath.replace("images/", "");

    return `../../images/${cleanPath}`;
  },

  /**
   * Afegeix gestió d'errors per a imatges
   */
  handleImageError(img) {
    img.onerror = function () {
      this.src = "../../images/placeholder-product.jpg";
      this.alt = "Imatge no disponible";
    };
  },

  /**
   * Obté paràmetres de la URL
   */
  getURLParameter(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
  },

  /**
   * Carrega els botons de filtres de categoria
   */
  async loadCategoryFilters() {
    const filterContainer = document.getElementById("category-filters");
    if (!filterContainer) return;

    try {
      const categories = await api.getCategories();

      categories.forEach((category) => {
        const button = document.createElement("button");
        button.type = "button";
        button.className = "btn btn-brown";
        button.textContent = category.nom;
        button.onclick = () => {
          // Eliminar classe active de tots els botons
          filterContainer
            .querySelectorAll(".btn")
            .forEach((btn) => btn.classList.remove("active"));
          // Afegir classe active al botó actual
          button.classList.add("active");
          // Carregar productes de la categoria
          DetallUtils.loadProducts("detall-producte", category.id);
        };

        filterContainer.appendChild(button);
      });
    } catch (error) {
      console.error("Error carregant filtres de categoria:", error);
    }
  },

  /**
   * Filtra tots els productes (elimina filtres de categoria)
   */
  filterAllProducts() {
    const filterContainer = document.getElementById("category-filters");
    if (filterContainer) {
      // Eliminar classe active de tots els botons
      filterContainer
        .querySelectorAll(".btn")
        .forEach((btn) => btn.classList.remove("active"));
      // Afegir classe active al botó "Tots"
      filterContainer.querySelector(".btn").classList.add("active");
    }
    // Carregar tots els productes
    Utils.loadProducts("products-container");
  },

  /**
   * Mostra missatges d'alerta personalitzats
   */
  showAlert(message, type = "info") {
    // Crear l'alerta personalitzada
    const alertDiv = document.createElement("div");
    alertDiv.className = `custom-alert ${type}`;
    alertDiv.textContent = message;

    // Afegir al body
    document.body.appendChild(alertDiv);

    // Eliminar automàticament després de 2 segons
    setTimeout(() => {
      alertDiv.classList.add("fade-out");
      // Eliminar del DOM després de l'animació
      setTimeout(() => {
        if (alertDiv.parentNode) {
          alertDiv.parentNode.removeChild(alertDiv);
        }
      }, 500);
    }, 2000);
  },
};

/**
 * Gestió de la cistella de compra (localStorage)
 */
const Cart = {
    /**
     * Obté els ítems de la cistella
     */
    getItems() {
        const items = localStorage.getItem('bicifood_cart');
        return items ? JSON.parse(items) : [];
    },

    /**
     * Guarda els ítems a la cistella
     */
    saveItems(items) {
        localStorage.setItem('bicifood_cart', JSON.stringify(items));
        this.updateCartCounter();
    },

    /**
     * Afegeix un ítem a la cistella des d'un botó amb dades
     */
    addItemFromButton(button) {
        const id = parseInt(button.dataset.productId);
        const name = button.dataset.productName;
        const price = parseFloat(button.dataset.productPrice);
        const imagePath = button.dataset.productImage;
        
        this.addItem(id, name, price, 1, imagePath);
        
        if (!document.getElementById("detall-producte")) {
      // Mostrem alerta directament aquí també per assegurar-nos
      Utils.showAlert(`${name} afegit a la cistella!`, "success");
    } else {
      DetallUtils.showAlert(`${name} afegit a la cistella!`, "success");
    }
    },

    /**
     * Afegeix un ítem a la cistella
     */
    addItem(id, name, price, quantity = 1, imagePath = null) {
        const items = this.getItems();
        const existingItem = items.find(item => item.id === id);

        if (existingItem) {
            existingItem.quantity += quantity;
        } else {
            items.push({ id, name, price, quantity, imagePath });
        }

        this.saveItems(items);
        this.updateCartCounter();
        if (!document.getElementById("detall-producte")) {
      Utils.showAlert(`${name} afegit a la cistella!`, "success");
    } else {
      DetallUtils.showAlert(`${name} afegit a la cistella!`, "success");
    }
    },

    /**
     * Elimina un ítem de la cistella
     */
    removeItem(id) {
        const items = this.getItems();
        const filteredItems = items.filter(item => item.id !== id);
        this.saveItems(filteredItems);
        this.updateCartCounter();
    },

    /**
     * Actualitza el comptador de la cistella
     */
    updateCartCounter() {
        const items = this.getItems();
        const totalItems = items.reduce((sum, item) => sum + item.quantity, 0);
        
        const counter = document.getElementById('cart-counter');
        if (counter) {
            counter.textContent = totalItems;
            counter.style.display = totalItems > 0 ? 'inline' : 'none';
        }
    },

    /**
     * Neteja la cistella
     */
    clear() {
        localStorage.removeItem('bicifood_cart');
        this.updateCartCounter();
    }
};

/**
 * Inicialització quan es carrega la pàgina
 */
document.addEventListener('DOMContentLoaded', function() {
    // Actualitza el comptador de la cistella
    Cart.updateCartCounter();
    
    // Carrega categories al dropdown si existeix
    if (document.getElementById('categoriesDropdown')) {
        Utils.loadCategoriesDropdown();
    }
    
    // Carrega productes si estem a la pàgina de categories
    if (document.getElementById('products-container')) {
        // Carrega botons de filtres de categoria
        Utils.loadCategoryFilters();
        // Carrega productes segons categoria de la URL
        const categoryId = Utils.getURLParameter('id');
        Utils.loadProducts('products-container', categoryId);
    }
    
    // Carrega productes destacats a la pàgina principal
    if (document.getElementById('featured-products')) {
        Utils.loadProducts('featured-products', null, 0, 4);
    }
});

// Funcions globals per als botons de cerca
function searchProducts() {
    const query = document.getElementById('search-input')?.value;
    if (query) {
        window.location.href = `resultats_cerca.html?q=${encodeURIComponent(query)}`;
    }
}

// Afegeix event listeners per als botons d'afegir a cistella
function addCartEventListeners() {
    const buttons = document.querySelectorAll('.add-to-cart-btn');
    buttons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            Cart.addItemFromButton(this);
        });
    });
}

// Gestió del formulari de cerca
document.addEventListener('keypress', function(e) {
    if (e.key === 'Enter' && e.target.id === 'search-input') {
        searchProducts();
    }
});

/**
 * Inicialització de la carrega de la pàgina de detall del producte
 */
document.addEventListener("DOMContentLoaded", async () => {
  // Actualitza el comptador de la cistella
  Cart.updateCartCounter();

  if (!document.getElementById("detall-producte")) {
    return;
  }

  // Obtenim l'ID del producte des de l'URL
  const productId = DetallUtils.getURLParameter("id");
  if (!productId) return;

  try {
    // Llamada a la API para obtener el producto
    const product = await api.getProductById(productId);

    // Rellenamos los datos en la página
    DetallUtils.createDetallCard(product);

    DetallUtils.fillDetallProduct(product);

    const relacionados = await DetallUtils.getProductsRelacionats(
      product.categoriaId, product
    );
    DetallUtils.nouRelacionat(relacionados);
  } catch (error) {
    console.error("Error carregant producte:", error);
  }
});