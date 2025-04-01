document.addEventListener("DOMContentLoaded", async function () {
    const logoutBtn = document.getElementById("logout-btn");
    const restaurantName = document.getElementById("restaurant-name");
    const restaurantStatus = document.getElementById("restaurant-status");
    const menuContainer = document.getElementById("menu-container");
    const addItemBtn = document.getElementById("add-item-btn");
    const menuFormContainer = document.getElementById("menu-form-container");
    const createMenuBtn = document.getElementById("create-menu-btn");
    const cancelFormBtn = document.getElementById("cancel-form-btn");

    const itemNameInput = document.getElementById("item-name");
    const itemPriceInput = document.getElementById("item-price");
    const itemCategoryInput = document.getElementById("item-category");
    const itemAvailableInput = document.getElementById("item-available");

    const ordersContainer = document.getElementById("orders-container");

    const inventoryTab = document.getElementById("inventory-tab");
    const ordersTab = document.getElementById("orders-tab");
    const menuSection = document.getElementById("menu-management");
    const ordersSection = document.getElementById("order-management");


    const role = localStorage.getItem("userRole");
    const userId = localStorage.getItem("userId");
    const restaurantId = localStorage.getItem("restaurantId");
    if (!restaurantId) {
        console.error("❌ No restaurant ID found. Nothing to delete.");
        return;
    }else{
        const restaurantId = localStorage.getItem("restaurantId");
    }
    

    if (!userId || role !== "RESTAURANT_OWNER" || !restaurantId) {
        alert("Unauthorized access! Redirecting to login...");
        window.location.href = "login.html";
        return;
    }

    // Tab management logic
    function showTab(tab) {
        if (tab === "inventory") {
            menuSection.style.display = "block";
            ordersSection.style.display = "none";
        } else if (tab === "orders") {
            menuSection.style.display = "none";
            ordersSection.style.display = "block";
        }
    }

    inventoryTab.addEventListener("click", () => showTab("inventory"));
    ordersTab.addEventListener("click", () => showTab("orders"));

    async function fetchRestaurantDetails() {
        const restaurantId = localStorage.getItem("restaurantId"); 
        if (!restaurantId) {
            console.error("❌ No restaurant ID found. Nothing to delete.");
            return;
        }
        try {
            const response = await fetch(`http://localhost:8080/restaurants/${restaurantId}`);
            if (!response.ok) throw new Error("Failed to fetch restaurant details");
            const data = await response.json();
            restaurantName.textContent = data.name;
            restaurantStatus.textContent = data.active ? "🟢 Open" : "🔴 Closed";
            fetchMenuItems();
            fetchOrders();
        } catch (error) {
            console.error("Error:", error);
            restaurantName.textContent = "Error loading details";
        }
    }
    async function createMenuItem(name, price, category, available) {
        try {
            const response = await fetch("http://localhost:8080/menu-items/create", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name, price, category, available })
            });

            if (!response.ok) throw new Error("Failed to create menu item");
            const newItem = await response.json();
            return newItem.id; // Returning menuItem ID
        } catch (error) {
            console.error("Error:", error);
            alert("Failed to create menu item.");
            return null;
        }
    }

    async function addMenuItemToRestaurant(menuItemId) {
        const userId = Number(localStorage.getItem("userId"));  // Convert userId to a number
    
        if (!userId) {
            alert("Invalid user ID! Please log in again.");
            window.location.href = "login.html";
            return;
        }
    
        const requestBody = { userId };
    
        console.log("📤 Sending Request:", `POST /restaurants/${restaurantId}/add-menu-item/${menuItemId}`, requestBody);
    
        try {
            const response = await fetch(`http://localhost:8080/restaurants/${restaurantId}/add-menu-item/${menuItemId}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody),
            });
    
            const result = await response.json();
            console.log("✅ Response:", result);
    
            if (!response.ok) throw new Error(result.error || "Failed to add menu item");
    
            alert(result.message);
            fetchMenuItems();
        } catch (error) {
            console.error("❌ Error:", error);
            alert("Failed to add menu item to restaurant.");
        }
    }
    
    async function removeMenuItemFromRestaurant(menuItemId) {
        const userId = Number(localStorage.getItem("userId")); // Ensure userId is a number
    
        if (!userId) {
            alert("Invalid user ID! Please log in again.");
            window.location.href = "login.html";
            return;
        }
    
        const requestBody = { userId };
    
        console.log("📤 Sending Request:", `DELETE /restaurants/${restaurantId}/remove-menu-item/${menuItemId}`, requestBody);
    
        try {
            const response = await fetch(`http://localhost:8080/restaurants/${restaurantId}/remove-menu-item/${menuItemId}`, {
                method: "DELETE",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody),
            });
    
            const result = await response.json();
            console.log("✅ Response:", result);
    
            if (!response.ok) throw new Error(result.error || "Failed to remove menu item");
    
            alert(result.message);
            fetchMenuItems();  // Refresh menu list
        } catch (error) {
            console.error("❌ Error:", error);
            alert("Failed to remove menu item.");
        }
    }
    
    async function fetchMenuItems() {
        
        if (!restaurantId) {
            console.error("�� No restaurant ID found. Nothing to delete.");
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/restaurants/${restaurantId}/menu-items`);
            if (!response.ok) throw new Error("Failed to fetch menu items");
            
            const menuItems = await response.json();
            menuContainer.innerHTML = ""; // Clear old menu items
            
            menuItems.forEach(item => {
                const itemDiv = document.createElement("div");
                itemDiv.classList.add("menu-item");
    
                itemDiv.innerHTML = `
                    <p>${item.name} - $${item.price.toFixed(2)}</p>
                    <button class="btn remove-item" data-id="${item.id}">❌ Remove</button>
                `;
    
                menuContainer.appendChild(itemDiv);
            });
    
            // Attach event listener to all "Remove" buttons
            document.querySelectorAll(".remove-item").forEach(button => {
                button.addEventListener("click", function () {
                    const menuItemId = this.getAttribute("data-id");
                    removeMenuItemFromRestaurant(menuItemId);
                });
            });
    
        } catch (error) {
            console.error("Error:", error);
            menuContainer.innerHTML = "<p>Failed to load menu.</p>";
        }
    }
    
    createMenuBtn.addEventListener("click", async function () {
        const name = itemNameInput.value;
        const price = itemPriceInput.value;
        const category = itemCategoryInput.value;
        const available = itemAvailableInput.checked;

        if (!name || !price) {
            alert("Please fill all fields!");
            return;
        }

        const menuItemId = await createMenuItem(name, price, category, available);
        if (menuItemId) {
            await addMenuItemToRestaurant(menuItemId);
            menuFormContainer.style.display = "none";
            itemNameInput.value = "";
            itemPriceInput.value = "";
        }
    });

    addItemBtn.addEventListener("click", () => {
        menuFormContainer.style.display = "block";
    });

    cancelFormBtn.addEventListener("click", () => {
        menuFormContainer.style.display = "none";
    });

    logoutBtn.addEventListener("click", () => {
        localStorage.clear();
        window.location.href = "login.html";
    });

    //order
    async function fetchOrders() {
        try {
            const response = await fetch(`http://localhost:8080/orders/restaurant/${restaurantId}`);
            if (!response.ok) throw new Error("Failed to fetch orders");
    
            const orders = await response.json();
            orderList.innerHTML = ""; // Clear previous entries
    
            orders.forEach(order => {
                // Convert timestamp array to a proper Date object
                const [year, month, day, hour, minute, second] = order.orderTime;
                const date = new Date(year, month - 1, day, hour, minute, second); // Month is 0-based in JS
    
                const row = document.createElement("tr");
    
                row.innerHTML = `
                    <td>${order.id}</td>
                    <td>${order.customer.email}</td>
                    <td>${date.toLocaleString()}</td>
                    <td>$${order.totalPrice.toFixed(2)}</td>
                    <td class="order-status">${order.status}</td>
                    <td>
                        <button class="btn mark-delivered" data-id="${order.id}">✅ Delivered</button>
                        <button class="btn mark-cancelled" data-id="${order.id}">❌ Cancel</button>
                    </td>
                `;
    
                orderList.appendChild(row);
            });
    
            // Attach event listeners for order status updates
            document.querySelectorAll(".mark-delivered").forEach(button => {
                button.addEventListener("click", function () {
                    const orderId = this.getAttribute("data-id");
                    updateOrderStatus(orderId, "delivered");
                });
            });
    
            document.querySelectorAll(".mark-cancelled").forEach(button => {
                button.addEventListener("click", function () {
                    const orderId = this.getAttribute("data-id");
                    updateOrderStatus(orderId, "cancel");
                });
            });
    
        } catch (error) {
            console.error("Error:", error);
            orderList.innerHTML = "<tr><td colspan='6'>Failed to load orders.</td></tr>";
        }
    }
    
    async function updateOrderStatus(orderId, action) {
        try {
            const response = await fetch(`http://localhost:8080/orders/${orderId}/${action}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
            });

            if (!response.ok) throw new Error("Failed to update order status");

            alert(`Order ${orderId} marked as ${action}`);
            fetchOrders();
        } catch (error) {
            console.error("Error:", error);
            alert("Failed to update order status.");
        }
    }

    console.log("🔥 Owner Dashboard Loaded");

    // Get tab elements
    const menuManagementTab = document.getElementById("inventory-tab");
    const orderTrackingTab = document.getElementById("orders-tab");

    // Get content sections
    const menuManagementContent = document.getElementById("menu-management");
    const orderTrackingContent = document.getElementById("order-tracking");

    // Function to switch tabs
    function showTab(tabId) {
        console.log(`📌 Switching to tab: ${tabId}`);

        // Hide all tabs
        document.querySelectorAll(".tab-content").forEach(tab => {
            tab.classList.remove("active");
        });

        // Show the selected tab
        document.getElementById(tabId).classList.add("active");
    }

    // Attach event listeners to tabs
    menuManagementTab.addEventListener("click", function () {
        showTab("menu-management");
    });

    orderTrackingTab.addEventListener("click", function () {
        showTab("order-tracking");
    });

    logoutBtn.addEventListener("click", () => {
        localStorage.clear();
        window.location.href = "login.html";
    });
    
        fetchRestaurantDetails();
        
});


document.addEventListener("DOMContentLoaded", function () {
    const settingsTab = document.getElementById("settings-tab");
    if (settingsTab) {
        settingsTab.addEventListener("click", openSettings);
    } else {
        console.error("❌ Settings tab not found in the DOM.");
    }
});
function openSettings() {
    const userId = localStorage.getItem("userId");
    const restaurantId = localStorage.getItem("restaurantId");

    console.log("🛠️ Opening Settings - userId:", userId, "restaurantId:", restaurantId);

    const popupBody = document.getElementById("popup-body");

    if (restaurantId && restaurantId !== "undefined") {
        // ✅ If restaurant exists, show Delete UI
        popupBody.innerHTML = `
            <h3>Delete Your Restaurant</h3>
            <p>Are you sure you want to delete your restaurant?</p>
            <button onclick="deleteRestaurant()">🗑️ Delete</button>
        `;
    } else {
        // 🚨 If restaurant does NOT exist, show Create UI
        popupBody.innerHTML = `
            <h3>Create Your Restaurant</h3>
            <input type="text" id="restaurant-name" placeholder="Enter restaurant name">
            <button onclick="createRestaurant()">✅ Create</button>
        `;
    }

    document.getElementById("settings-popup").style.display = "block";
}


async function createRestaurant() {
    const userId = localStorage.getItem("userId");
    if (!userId) {
        console.error("❌ No user ID found in localStorage.");
        return;
    }

    const restaurantData = {
        name: `My New Restaurant ${userId}`, // You can take input from the user
        userId: userId
    };

    console.log("📤 Sending Request: Creating restaurant...", restaurantData);

    try {
        const response = await fetch("http://localhost:8080/restaurants/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(restaurantData)
        });

        const result = await response.json();

        if (response.ok) {
            console.log("✅ Restaurant Created Successfully:", result);

            // **Store the restaurant ID in localStorage**
            localStorage.setItem("restaurantId", result.restaurant.id);

            alert("🎉 Restaurant Created Successfully!");

            // **Reload to reflect changes**
            location.reload();

            fetchRestaurantDetails();
        } else {
            console.error("❌ Error creating restaurant:", result);
            alert("❌ Failed to create restaurant.");
        }
    } catch (error) {
        console.error("❌ Network error:", error);
    }
}


async function deleteRestaurant() {
    const userId = localStorage.getItem("userId");
    const restaurantId = localStorage.getItem("restaurantId");

    if (!restaurantId) {
        console.error("❌ No restaurant ID found. Nothing to delete.");
        return;
    }

    const apiUrl = `http://localhost:8080/restaurants/${restaurantId}/deactivate`;
    const payload = { userId: userId };

    console.log("📤 Deleting restaurant...", payload);

    try {
        const response = await fetch(apiUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            console.log("✅ Restaurant Deleted Successfully");
            alert("🎉 Restaurant Deactivated!");

            // **Remove restaurantId from localStorage**
            localStorage.removeItem("restaurantId");

            location.reload();
        } else {
            console.error("❌ Error deleting restaurant:", await response.json());
            alert("❌ Failed to delete restaurant.");
        }
    } catch (error) {
        console.error("❌ Network error:", error);
    }
}


function closePopup() {
    const popup = document.getElementById("settings-popup");
    if (popup) {
        popup.style.display = "none";
    } else {
        console.error("❌ Settings popup not found.");
    }
}

