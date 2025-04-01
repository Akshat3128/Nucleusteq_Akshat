// Function to dynamically get userId from localStorage or query params
function getUserId() {
    let userId = localStorage.getItem("userId");
    if (!userId) {
        const urlParams = new URLSearchParams(window.location.search);
        userId = urlParams.get("userId") || 1; // Default to 1 if not found
        localStorage.setItem("userId", userId);
    }
    return userId;
}

const userId = getUserId();
const apiUrl = `http://localhost:8080/wallet/${userId}`;

async function fetchWalletBalance() {
    try{
        const response = await fetch(`http://localhost:8080/wallet/${userId}/balance`);
        const balance = await response.json();
        document.getElementById("wallet-balance").textContent = `${balance.walletBalance.toFixed(2)}`;
    }catch(e){
        console.error("Error fetching wallet balance:", e);
        document.getElementById("wallet-balance").textContent = "Error";
    }
    
}

async function fetchTransactionHistory() {
    const response = await fetch(`http://localhost:8080/wallet/${userId}/transactions`);
    const transactions = await response.json();
    const transactionList = document.getElementById("transaction-list");
    transactionList.innerHTML = '';

    transactions.forEach(tx => {
        // Convert timestamp array to a proper Date object
        const [year, month, day, hour, minute, second] = tx.timestamp;
        const date = new Date(year, month - 1, day, hour, minute, second); // Month is 0-based in JS

        const div = document.createElement("div");
        div.className = `transaction-item ${tx.transactionType.toLowerCase()}`;
        div.textContent = `${tx.transactionType}: $${tx.amount.toFixed(2)} (${date.toLocaleString()})`;

        transactionList.appendChild(div);
    });
}

async function addFunds() {
    try{
        const amount = parseFloat(document.getElementById("amount").value);
        if (isNaN(amount) || amount <= 0) return alert("Enter a valid amount");

        const response = await fetch(`http://localhost:8080/wallet/${userId}/add-funds`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ userId, amount })
        });

        const result = await response.json();
        alert(result.message);
        
    }catch(err) {
        console.error("Error adding funds - Daily Limit Reached:", err);
    }finally{
        console.log("Internal Server Error");
    }
    fetchWalletBalance();
    fetchTransactionHistory();
}

async function deductFunds() {
    const amount = parseFloat(document.getElementById("amount").value);
    if (isNaN(amount) || amount <= 0) return alert("Enter a valid amount");

    const response = await fetch(`http://localhost:8080/wallet/${userId}/deduct-funds`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ amount })
    });

    const result = await response.json();
    alert(result.message);
    fetchWalletBalance();
    fetchTransactionHistory();
}

document.querySelector(".home-btn").addEventListener("click", function (event) {
    event.preventDefault(); // Prevent default anchor navigation

    const user = localStorage.getItem("userId"); // Check if user is logged in

    if (user) {
        window.location.href = "customer_dashboard.html"; // Redirect to Home if logged in
    } else {
        window.location.href = "../index.html"; // Redirect to Login if not logged in
    }
});

// Fetch data on load
fetchWalletBalance();
fetchTransactionHistory();
