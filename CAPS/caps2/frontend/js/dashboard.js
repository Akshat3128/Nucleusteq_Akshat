document.addEventListener("DOMContentLoaded", async function () {
    const userNameSpan = document.getElementById("user-name");
    const walletBalanceSpan = document.getElementById("wallet-balance");
    const ordersContainer = document.getElementById("orders-container");
    const logoutBtn = document.getElementById("logout-btn");

    // Retrieve stored user details
    const userId = localStorage.getItem("userId");
    const userEmail = localStorage.getItem("userEmail");

    if (!userId || !userEmail) {
        alert("User not logged in! Redirecting...");
        window.location.href = "login.html";
        return;
    }

    userNameSpan.textContent = userEmail.split('@')[0]; // Display username

    // Fetch Wallet Balance
    async function fetchWalletBalance() {
        try {
            const response = await fetch(`http://localhost:8080/wallet/${userId}/balance`);
            const data = await response.json();
            walletBalanceSpan.textContent = `$${data.walletBalance}`;
        } catch (error) {
            console.error("Error fetching wallet balance:", error);
            walletBalanceSpan.textContent = "Error";
        }
    }

    // Fetch Orders
    async function fetchOrders() {
        try {
            const response = await fetch(`http://localhost:8080/orders/customer/${userId}`);
            const orders = await response.json();

            ordersContainer.innerHTML = "";
            orders.forEach(order => {
                const orderDiv = document.createElement("div");
                orderDiv.classList.add("order");
                orderDiv.innerHTML = `<p>📦 Order #${order.id} - ${order.status}</p>`;
                ordersContainer.appendChild(orderDiv);
            });
        } catch (error) {
            console.error("Error fetching orders:", error);
            ordersContainer.innerHTML = "<p>Failed to load orders.</p>";
        }
    }

    // Logout
    logoutBtn.addEventListener("click", function () {
        localStorage.clear(); // Clear all user data
        window.location.href = "login.html";
    });

    fetchWalletBalance();
    fetchOrders();
});


