/**
 * BiciFood API Client
 * Gestiona totes les comunicacions amb l'API backend
 */

class BiciFoodAPI {
  constructor() {
    this.baseURL = "http://localhost:8080/api/v1";
    this.headers = {
      "Content-Type": "application/json",
      Accept: "application/json",
    };
  }

  /**
   * Fa una petició HTTP genèrica
   */
  async makeRequest(endpoint, method = "GET", data = null) {
    const config = {
      method,
      headers: this.headers,
    };

    if (data) {
      config.body = JSON.stringify(data);
    }

    try {
      const fullUrl = `${this.baseURL}${endpoint}`;
      console.log(`🔗 Fetching: ${fullUrl}`);
      const response = await fetch(fullUrl, config);

      if (!response.ok) {
        console.error(
          `❌ HTTP error! status: ${response.status} for ${fullUrl}`
        );
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      console.log(`✅ Response from ${endpoint}:`, result);
      return result;
    } catch (error) {
      console.error("❌ Error en la petició API:", error);
      throw error;
    }
  }

  /**
   * Obté totes les categories
   */
  async getCategories() {
    return await this.makeRequest("/categoria");
  }

  /**
   * Obté tots els productes
   */
  async getProducts(page = 0, size = 20) {
    return await this.makeRequest(`/producte?page=${page}&size=${size}`);
  }

  /**
   * Obté productes per categoria
   */
  async getProductsByCategory(categoryId, page = 0, size = 20) {
    return await this.makeRequest(
      `/producte/category/${categoryId}?page=${page}&size=${size}`
    );
  }

  /**
   * Obté un producte per ID
   */
  async getProductById(id) {
    return await this.makeRequest(`/producte/${id}`);
  }

  /**
   * Cerca productes per nom
   */
  async searchProducts(query, page = 0, size = 20) {
    return await this.makeRequest(
      `/producte/search?name=${encodeURIComponent(
        query
      )}&page=${page}&size=${size}`
    );
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
      const dropdown = document.getElementById("categoriesDropdown");

      if (dropdown) {
        dropdown.innerHTML = "";
        categories.forEach((category) => {
          const li = document.createElement("li");
          li.innerHTML = `<a class="dropdown-item" href="categories.html?id=${category.id}">${category.nom}</a>`;
          dropdown.appendChild(li);
        });
      }
    } catch (error) {
      console.error("Error carregant categories:", error);
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
      if (!container) {
        console.error(`❌ Container with id '${containerId}' not found`);
        return;
      }

      container.innerHTML = "";

      // Manejo de respuestas paginadas (tiene .content) o arrays simples
      const productList = products.content || products;

      if (productList && productList.length > 0) {
        productList.forEach((product) => {
          const productCard = this.createProductCard(product);
          container.appendChild(productCard);
        });
        // Afegir event listeners després de crear les targetes
        addCartEventListeners();
        console.log(`✅ ${productList.length} productos cargados`);
      } else {
        console.warn("⚠️ No hay productos disponibles");
        container.innerHTML =
          '<p class="text-center">No hi ha productes disponibles.</p>';
      }
    } catch (error) {
      console.error("❌ Error carregant productes:", error);
      const container = document.getElementById(containerId);
      if (container) {
        container.innerHTML =
          '<p class="text-center text-danger">Error carregant els productes: ' +
          error.message +
          "</p>";
      }
    }
  },

  /**
   * Crea una targeta de producte
   */
  createProductCard(product) {
    const card = document.createElement("div");
    card.className = "col-md-4 col-lg-3 mb-4";

    const stockLabel = window.i18n
      ? window.i18n.translate("product.stock")
      : "Stock";
    const addToCartLabel = window.i18n
      ? window.i18n.translate("product.addToCart")
      : "Afegir a la cistella";
    const outOfStockLabel = window.i18n
      ? window.i18n.translate("product.outOfStock")
      : "Esgotat";

    card.innerHTML = `
            <div class="card h-100 product-card">
                <img src="${this.getProductImagePath(product.imatgePath)}" 
                     class="card-img-top product-image" 
                     alt="${product.nom}"
                     style="height: 200px; object-fit: cover;"
                     onerror="this.src='../../images/placeholder-product.jpg'; this.alt='Imatge no disponible'">
                <div class="card-body d-flex flex-column">
                    <h6 class="card-title">${product.nom}</h6>
                    <p class="card-text text-muted small">${
                      product.descripcio
                    }</p>
                    <div class="mt-auto">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="fw-bold text-success">${
                              product.preu
                            }€</span>
                            <span class="small text-muted">${stockLabel}: ${
      product.stock
    }</span>
                        </div>
                        <button class="btn btn-primary btn-sm w-100 add-to-cart-btn" 
                                data-product-id="${product.id}"
                                data-product-name="${product.nom.replace(
                                  /"/g,
                                  "&quot;"
                                )}"
                                data-product-price="${product.preu}"
                                data-product-image="${(
                                  product.imatgePath || ""
                                ).replace(/"/g, "&quot;")}"
                                ${product.stock <= 0 ? "disabled" : ""}>
                            ${
                              product.stock <= 0
                                ? outOfStockLabel
                                : addToCartLabel
                            }
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
    if (!imagePath) return "../../images/placeholder-product.jpg";

    // Si ja té la ruta completa, la utilitzem
    if (imagePath.startsWith("../../images/")) return imagePath;

    // Si té el prefix 'images/', el treiem
    const cleanPath = imagePath.replace("images/", "");
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
          Utils.loadProducts("products-container", category.id);
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
    const items = localStorage.getItem("bicifood_cart");
    return items ? JSON.parse(items) : [];
  },

  /**
   * Guarda els ítems a la cistella
   */
  saveItems(items) {
    localStorage.setItem("bicifood_cart", JSON.stringify(items));
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

    // Mostrem alerta directament aquí també per assegurar-nos
    Utils.showAlert(`${name} afegit a la cistella!`, "success");
  },

  /**
   * Afegeix un ítem a la cistella
   */
  addItem(id, name, price, quantity = 1, imagePath = null) {
    const items = this.getItems();
    const existingItem = items.find((item) => item.id === id);

    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      items.push({ id, name, price, quantity, imagePath });
    }

    this.saveItems(items);
    this.updateCartCounter();
    Utils.showAlert(`${name} afegit a la cistella!`, "success");
  },

  /**
   * Elimina un ítem de la cistella
   */
  removeItem(id) {
    const items = this.getItems();
    const filteredItems = items.filter((item) => item.id !== id);
    this.saveItems(filteredItems);
    this.updateCartCounter();
  },

  /**
   * Actualitza el comptador de la cistella
   */
  updateCartCounter() {
    const items = this.getItems();
    const totalItems = items.reduce((sum, item) => sum + item.quantity, 0);

    const counter = document.getElementById("cart-counter");
    if (counter) {
      counter.textContent = totalItems;
      counter.style.display = totalItems > 0 ? "inline" : "none";
    }
  },

  /**
   * Neteja la cistella
   */
  clear() {
    localStorage.removeItem("bicifood_cart");
    this.updateCartCounter();
  },
};

/**
 * Inicialització quan es carrega la pàgina
 */
document.addEventListener("DOMContentLoaded", function () {
  // Actualitza el comptador de la cistella
  Cart.updateCartCounter();

  // Carrega categories al dropdown si existeix
  if (document.getElementById("categoriesDropdown")) {
    Utils.loadCategoriesDropdown();
  }

  // Carrega productes si estem a la pàgina de categories
  if (document.getElementById("products-container")) {
    // Carrega botons de filtres de categoria
    Utils.loadCategoryFilters();
    // Carrega productes segons categoria de la URL
    const categoryId = Utils.getURLParameter("id");
    Utils.loadProducts("products-container", categoryId);
  }

  // Carrega productes destacats a la pàgina principal
  if (document.getElementById("featured-products")) {
    Utils.loadProducts("featured-products", null, 0, 8);
  }
});

// Funcions globals per als botons de cerca
function searchProducts() {
  const query = document.getElementById("search-input")?.value;
  if (query) {
    window.location.href = `resultats_cerca.html?q=${encodeURIComponent(
      query
    )}`;
  }
}

// Afegeix event listeners per als botons d'afegir a cistella
function addCartEventListeners() {
  const buttons = document.querySelectorAll(".add-to-cart-btn");
  buttons.forEach((button) => {
    button.addEventListener("click", function (e) {
      e.preventDefault();
      Cart.addItemFromButton(this);
    });
  });
}

// Gestió del formulari de cerca
document.addEventListener("keypress", function (e) {
  if (e.key === "Enter" && e.target.id === "search-input") {
    searchProducts();
  }
});
