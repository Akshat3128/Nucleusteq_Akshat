document.addEventListener("DOMContentLoaded", fetchOrders);

function fetchOrders() {
    fetch("http://localhost:8080/orders/restaurant/1")
        .then(res => res.json())
        .then(data => {
            const orderList = document.getElementById("orderList");
            orderList.innerHTML = ""; // Clear previous data

            if (data.length === 0) {
                orderList.innerHTML = "<tr><td colspan='6'>No orders available.</td></tr>";
                return;
            }

            data.forEach(order => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${order.id}</td>
                    <td>${order.customer.email}</td>
                    <td>${formatOrderTime(order.orderTime)}</td>
                    <td>$${order.totalPrice}</td>
                    <td>${order.status}</td>
                    <td>
                        ${order.status === "PENDING" ? `
                            <button onclick="markDelivered(${order.id})">✅ Delivered</button>
                            <button onclick="cancelOrder(${order.id})">❌ Cancel</button>
                        ` : ""}
                    </td>
                `;
                orderList.appendChild(row);
            });
        })
        .catch(error => console.error("Error fetching orders:", error));
}

function markDelivered(orderId) {
    fetch(`http://localhost:8080/orders/${orderId}/delivered`, { method: "PUT" })
        .then(() => fetchOrders());
}

function cancelOrder(orderId) {
    fetch(`http://localhost:8080/orders/${orderId}/cancel`, { method: "PUT" })
        .then(() => fetchOrders());
}

function formatOrderTime(orderTime) {
    return new Date(orderTime.join("-")).toLocaleString();
}
