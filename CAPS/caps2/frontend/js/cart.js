document.addEventListener("DOMContentLoaded", async function () {
    const userId = localStorage.getItem("userId") ;  // Replace with dynamic user ID
    const cartContainer = document.getElementById("cart-container");
    const walletBalanceSpan = document.getElementById("wallet-balance");
    const checkoutBtn = document.getElementById("checkout-btn");

    let cartItems = [];
    let walletBalance = 0;

    // Fetch cart items
    async function fetchCart() {
        try {
            const response = await fetch(`http://localhost:8080/cart/view/${userId}`);
            const data = await response.json();
            cartItems = data.cartItems;
            displayCart(cartItems);
        } catch (error) {
            console.error("Error fetching cart:", error);
            cartContainer.innerHTML = "<p>Cart Empty!.</p>";
        }
    }

    // Fetch wallet balance
    async function fetchWalletBalance() {
        try {
            const response = await fetch(`http://localhost:8080/wallet/${userId}/balance`);
            const data = await response.json();
            walletBalance = data.walletBalance;
            walletBalanceSpan.textContent = `$${walletBalance}`;
            updateCheckoutButton();
        } catch (error) {
            console.error("Error fetching wallet balance:", error);
        }
    }

    // Display cart items dynamically
    function displayCart(items) {
        cartContainer.innerHTML = "";
        if (items.length === 0) {
            cartContainer.innerHTML = "<p>Your cart is empty.</p>";
            return;
        }

        items.forEach(item => {
            const cartItem = document.createElement("div");
            cartItem.classList.add("cart-item");

            cartItem.innerHTML = `
                <h3>${item.menuItem.name}</h3>
                <p>Price: $${item.menuItem.price}</p>
                <p>Quantity: ${item.quantity}</p>
                <button class="remove-btn" data-id="${item.menuItem.id}">Remove</button>
            `;

            cartContainer.appendChild(cartItem);
        });

        document.querySelectorAll(".remove-btn").forEach(button => {
            button.addEventListener("click", removeFromCart);
        });

        updateCheckoutButton();
    }

    // Calculate total cart value & update checkout button
    function updateCheckoutButton() {
        const totalCartValue = cartItems.reduce((sum, item) => sum + item.menuItem.price * item.quantity, 0);
        checkoutBtn.disabled = walletBalance < totalCartValue;
    }

    // Remove item from cart (Updated with correct API endpoint & payload)
    async function removeFromCart(event) {
        const menuItemId = event.target.dataset.id;
        try {
            await fetch(`http://localhost:8080/cart/remove`, {
                method: "DELETE",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ userId, menuItemId }),
            });
            fetchCart(); // Refresh cart after removal
            fetchWalletBalance(); // Update balance after removal
        } catch (error) {
            console.error("Error removing item:", error);
        }
    }

    // Checkout process (Updated API call with correct payload)
    async function checkout() {
        try {
            const customerId = userId;
            const response = await fetch(`http://localhost:8080/orders/place`, {
                method: "POST",
                
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ customerId }),
            });
            const result = await response.json();

            if (result.status == "SUCCESS") {
                alert("Order placed successfully!");
                fetchCart();
                fetchWalletBalance();
            } else {
                alert("Checkout failed. Previous Order Pending. Try again!");
            }
        } catch (error) {
            console.error("Error during checkout:", error);
        }
    }

    // Attach checkout event listener
    checkoutBtn.addEventListener("click", checkout);

    // Initial API Calls on Page Load
    fetchCart();
    fetchWalletBalance();
});
