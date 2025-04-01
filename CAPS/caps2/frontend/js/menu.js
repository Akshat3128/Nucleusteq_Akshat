document.addEventListener("DOMContentLoaded", function () {
    console.log("🔥 DOM fully loaded, initializing script...");

    const menuContainer = document.getElementById("menu-container");
    const cartCount = document.getElementById("cart-count");
    const themeToggle = document.getElementById("theme-toggle");

    if (!menuContainer) console.error("❌ menu-container not found!");
    if (!cartCount) console.error("❌ cart-count not found!");
    // if (!themeToggle) console.error("❌ theme-toggle not found!");

    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    // function updateCartCount() {
    //     cartCount.textContent = `🛒 ${cart.length}`;
    // }

    async function fetchMenu() {
        try {
            console.log("🔥 Fetching menu...");
            const urlParams = new URLSearchParams(window.location.search);
            const restaurantId = urlParams.get("id") ;
            
            const response = await fetch(`http://localhost:8080/restaurants/${restaurantId}/menu-items`);
            
            if (!response.ok) throw new Error("Failed to fetch menu");

            const menuItems = await response.json();
            console.log("✅ Menu fetched:", menuItems);

            displayMenu(menuItems);
        } catch (error) {
            console.error("❌ Error fetching menu:", error);
            menuContainer.innerHTML = "<p>Failed to load menu items.</p>";
        }
    }

    function displayMenu(menuItems) {
        menuContainer.innerHTML = ""; // Clear previous content
    
        if (!menuItems.length) {
            menuContainer.innerHTML = "<p>No items available.</p>";
            return;
        }
    
        // Retrieve cart from local storage
        const cartData = JSON.parse(localStorage.getItem("cart"));
        const cartItems = cartData?.cartItems || []; 
    
        menuItems.forEach(item => {
            const menuItem = document.createElement("div");
            menuItem.classList.add("menu-item");
    
            // Checking if the item is in the cart (using menuItem.id inside cartItems)
            const isInCart = cartItems.some(cartItem => cartItem.menuItem.id === item.id);
    
            menuItem.innerHTML = `
                
                <h3>${item.name}</h3>
                <p>Category: ${item.category}</p>
                <p>Price: ${item.price.toFixed(2)}</p>
                <button class="add-to-cart" data-id="${item.id}" data-name="${item.name}" data-price="${item.price}">Add to Cart</button>
                <button class="remove-from-cart" data-id="${item.id}" ${isInCart ? "" : "disabled"}>Remove from Cart</button>
            `;
    
            menuContainer.appendChild(menuItem);
        });
    
        document.querySelectorAll(".add-to-cart").forEach(button => {
            button.addEventListener("click", addToCart);
        });
    
        document.querySelectorAll(".remove-from-cart").forEach(button => {
            button.addEventListener("click", removeFromCart);
        });
    }
    
    function getRestaurantIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get("id");
    }
    
    function updateCartCount() {
        const cart = JSON.parse(localStorage.getItem("cart"));
        const cartItems = cart?.cartItems || [];
    
        // Calculate total quantity of all items in the cart
        const totalCount = cartItems.reduce((sum, item) => sum + item.quantity, 0);
    
        const cartCountElement = document.getElementById("cart-count"); // Ensure this element exists in HTML
        if (cartCountElement) {
            cartCountElement.textContent = `🛒 ${totalCount}`;
        }
    }
    
    
    async function addToCart(event) {
        const button = event.target;
        const menuItemId = button.dataset.id;
        const quantity = 1; // Default quantity to 1
    
        // Retrieve userId from local storage
        const userId = localStorage.getItem("userId");
        const restaurantId = getRestaurantIdFromUrl(); // Extract from URL
    
        if (!userId || !restaurantId) {
            console.error("User ID or Restaurant ID is missing.");
            alert("Please login first");
            return;
        }
    
        const payload = {
            userId: parseInt(userId),
            restaurantId: parseInt(restaurantId),
            menuItemId: parseInt(menuItemId),
            quantity: quantity
        };
    
        try {
            const response = await fetch("http://localhost:8080/cart/add", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });
    
            if (!response.ok) {
                throw new Error("Failed to add item to cart");
            }
    
            const data = await response.json();
            console.log("Cart updated:", data);
    
            // Store updated cart in local storage
            localStorage.setItem("cart", JSON.stringify(data));
    
            updateCartCount(); // Update UI with new cart count
            updateRemoveButton(menuItemId, true);
        } catch (error) {
            console.error("Error adding item to cart:", error);
        }
    }
    
    // Ensure cart count is updated when the page loads
    document.addEventListener("DOMContentLoaded", updateCartCount);
    

    async function removeFromCart(event) {
        const button = event.target;
        const menuItemId = button.dataset.id;
    
        // Retrieve userId from local storage
        const userId = localStorage.getItem("userId");
    
        if (!userId) {
            console.error("User ID is missing.");
            return;
        }
    
        const payload = {
            userId: parseInt(userId),
            menuItemId: parseInt(menuItemId)
        };
    
        try {
            const response = await fetch("http://localhost:8080/cart/remove", {
                method: "DELETE", 
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });
    
            if (!response.ok) {
                throw new Error("Failed to remove item from cart");
            }
    
            const data = await response.json();
            console.log("Cart updated after removal:", data);
    
            // Store updated cart in local storage
            localStorage.setItem("cart", JSON.stringify(data));
    
            updateCartCount();
            updateRemoveButton(menuItemId, false);
        } catch (error) {
            console.error("Error removing item from cart:", error);
        }
    }
    

    function updateRemoveButton(itemId, enabled) {
        const removeButton = document.querySelector(`.remove-from-cart[data-id="${itemId}"]`);
        if (removeButton) {
            removeButton.disabled = !enabled;
        }
    }

    // Theme Toggle
    // let darkMode = localStorage.getItem("darkMode") === "true";

    // // function applyTheme() {
    //     document.body.classList.toggle("light-mode", !darkMode);
    //     themeToggle.textContent = darkMode ? "Light Mode" : "Dark Mode";
    // }

    themeToggle?.addEventListener("click", function () {
        darkMode = !darkMode;
        localStorage.setItem("darkMode", darkMode);
        applyTheme();
    });

    // carticon funtionality
    document.getElementById("cart-count").addEventListener("click", function () {
        const user = localStorage.getItem("userId");     
        if (user) {
            window.location.href = "../pages/cart.html"; // Go to Cart Page
        } else {
            window.location.href = "../pages/login.html"; // Redirect to Login Page
        }
    });
    
    // Initialize Page
    fetchMenu();
    updateCartCount();
    applyTheme();
});


document.querySelector(".home-btn").addEventListener("click", function (event) {
    event.preventDefault(); // Prevent default anchor navigation

    const user = localStorage.getItem("userId"); // Check if user is logged in

    if (user) {
        window.location.href = "customer_dashboard.html"; // Redirect to Home if logged in
    } else {
        window.location.href = "../index.html"; // Redirect to Login if not logged in
    }
});


