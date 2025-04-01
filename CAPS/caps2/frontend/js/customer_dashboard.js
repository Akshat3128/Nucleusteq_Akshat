document.addEventListener("DOMContentLoaded", async function () {
    const userNameSpan = document.getElementById("user-name");
    const walletBalanceSpan = document.getElementById("wallet-balance");
    const dynamicContentSection = document.getElementById("dynamic-content");
    const toggleOrdersBtn = document.getElementById("toggle-orders");
    const browseRestaurantsBtn = document.getElementById("browse-restaurants");
    const logoutBtn = document.getElementById("logout-btn");

    const userId = localStorage.getItem("userId");
    const userEmail = localStorage.getItem("userEmail");
    const userRole = localStorage.getItem("userRole");

    if (!userId || !userEmail ) {
        alert("User not logged in! as Customer Redirecting...");
        window.location.href = "login.html";
        return;
    }
    if(userRole !=="CUSTOMER"){
        alert("Unauthorized Acess !");
        window.location.href = "login.html";
        return;
    }

    userNameSpan.textContent = userEmail.split('@')[0];

    async function fetchWalletBalance() {
        try {
            const response = await fetch(`http://localhost:8080/wallet/${userId}/balance`);
            if (!response.ok) throw new Error("Failed to fetch wallet balance");

            const data = await response.json();
            walletBalanceSpan.textContent = `${data.walletBalance} Credits`;
        } catch (error) {
            console.error("Error fetching wallet balance:", error);
            walletBalanceSpan.textContent = "Error";
        }
    }

    async function fetchOrders() {
        try {
            const response = await fetch(`http://localhost:8080/orders/customer/${userId}`);
            if (!response.ok) throw new Error("Failed to fetch orders");

            const orders = await response.json();
            dynamicContentSection.innerHTML = "<h2>📦 Recent Orders</h2><div class='order-list'></div>";
            const orderListDiv = document.querySelector(".order-list");

            if (orders.length === 0) {
                dynamicContentSection.innerHTML += "<p>No recent orders found.</p>";
                return;
            }

            orders.forEach(order => {
                const orderDiv = document.createElement("div");
                orderDiv.classList.add("order");
                orderDiv.innerHTML = `
                    <p>📦 <strong>Order #${order.id}</strong> - <span style="color: ${order.status === 'CANCELLED' ? 'red' : 'green'}">${order.status}</span></p>
                    <p>🍽️ From: <strong>${order.restaurant.name}</strong></p>
                    <p>💵 Total: ${order.totalPrice} Credits</p>
                    ${order.status === "PENDING" ? `<button class="cancel-btn" onclick="cancelOrder(${order.id})">❌ Cancel Order</button>` : ""}
                `;
                orderListDiv.appendChild(orderDiv);
            });
        } catch (error) {
            console.error("Error fetching orders:", error);
            dynamicContentSection.innerHTML = "<p>Failed to load orders.</p>";
        }
    }

    async function fetchRestaurants() {
        try {
            const response = await fetch(`http://localhost:8080/restaurants/all`);
            if (!response.ok) throw new Error("Failed to fetch restaurants");

            const restaurants = await response.json();
            dynamicContentSection.innerHTML = "<h2>🍽️ Available Restaurants</h2><div class='restaurant-list'></div>";
            const restaurantListDiv = document.querySelector(".restaurant-list");

            if (restaurants.length === 0) {
                dynamicContentSection.innerHTML += "<p>No restaurants available.</p>";
                return;
            }

            restaurants.forEach(restaurant => {
                const restaurantDiv = document.createElement("div");
                restaurantDiv.classList.add("restaurant-card");
                restaurantDiv.innerHTML = `
                    <h3>${restaurant.name}</h3>
                    <p>Status: ${restaurant.active ? "🟢 Open" : "🔴 Closed"}</p>
                    <button onclick="viewMenu(${restaurant.id})">📜 View Menu</button>
                `;
                restaurantListDiv.appendChild(restaurantDiv);
            });
        } catch (error) {
            console.error("Error fetching restaurants:", error);
            dynamicContentSection.innerHTML = "<p>Failed to load restaurants.</p>";
        }
    }

    window.cancelOrder = async function (orderId) {
        if (!confirm("Are you sure you want to cancel this order?")) return;

        try {
            const response = await fetch(`http://localhost:8080/orders/${orderId}/cancel`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" }
            });

            if (!response.ok) throw new Error("Failed to cancel order");

            const data = await response.json();
            alert(`Order #${data.id} has been cancelled. Refund Processed!`);
            
            fetchWalletBalance(); // Update wallet balance
            fetchOrders(); // Refresh order list
        } catch (error) {
            console.error("Error cancelling order:", error);
            alert("Failed to cancel order. Try again!");
        }
    };

    browseRestaurantsBtn.addEventListener("click", function () {
        fetchRestaurants();
        toggleOrdersBtn.textContent = "📦 Show Orders";
    });

    toggleOrdersBtn.addEventListener("click", function () {
        fetchOrders();
        toggleOrdersBtn.textContent = "🍽️ Browse Restaurants";
    });

    logoutBtn.addEventListener("click", function () {
        localStorage.clear();
        localStorage.clear(userId);
        window.location.href = "login.html";
    });

    // Function to Navigate to Restaurant Menu Page
    window.viewMenu = function (restaurantId) {
        window.location.href = `restaurant.html?id=${restaurantId}`;
    };

    fetchWalletBalance();
    fetchOrders();
});
