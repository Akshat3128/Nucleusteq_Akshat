document.addEventListener("DOMContentLoaded", async function () {
    const urlParams = new URLSearchParams(window.location.search);
    const restaurantId = urlParams.get("id");

    if (!restaurantId) {
        document.getElementById("restaurant-name").innerText = "Restaurant Not Found";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/restaurants/${restaurantId}`);
        const data = await response.json();

        document.getElementById("restaurant-name").innerText = data.name;
        document.getElementById("restaurant-status").innerText = data.active ? "Open" : "Closed";
        document.getElementById("restaurant-status").style.color = data.active ? "lightgreen" : "red";

        // Generate a random rating
        const randomRating = (Math.random() * (5 - 3) + 3).toFixed(1);
        document.getElementById("restaurant-rating").innerText = randomRating;

        // View Menu Button Action
        document.getElementById("view-menu-btn").addEventListener("click", function () {
            window.location.href = `menu.html?id=${restaurantId}`;
        });

    } catch (error) {
        console.error("Error fetching restaurant details:", error);
        document.getElementById("restaurant-name").innerText = "Failed to load restaurant details.";
    }
});


document.addEventListener("DOMContentLoaded", async function () {
    const urlParams = new URLSearchParams(window.location.search);
    const restaurantId = urlParams.get("id");

    if (!restaurantId) {
        document.getElementById("restaurant-name").innerText = "Restaurant Not Found";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/restaurants/${restaurantId}`);
        const data = await response.json();

        document.getElementById("restaurant-name").innerText = data.name;
        document.getElementById("restaurant-status").innerText = data.active ? "Open" : "Closed";
        document.getElementById("restaurant-status").style.color = data.active ? "lightgreen" : "red";

        // Generate a random rating
        const randomRating = (Math.random() * (5 - 3) + 3).toFixed(1);
        document.getElementById("restaurant-rating").innerText = randomRating;

        // View Menu Button Action
        document.getElementById("view-menu-btn").addEventListener("click", function () {
            window.location.href = `menu.html?id=${restaurantId}`;
        });

    } catch (error) {
        console.error("Error fetching restaurant details:", error);
        document.getElementById("restaurant-name").innerText = "Failed to load restaurant details.";
    }

    // 🌟 Theme Toggle Functionality
    const themeToggle = document.getElementById("theme-toggle");
    const body = document.body;

    // Load previous theme from localStorage
    if (localStorage.getItem("theme") === "dark") {
        body.classList.add("dark-mode");
        themeToggle.innerText = "Light Mode";
    }

    // Toggle Theme on Click
    themeToggle.addEventListener("click", function () {
        body.classList.toggle("dark-mode");

        if (body.classList.contains("dark-mode")) {
            localStorage.setItem("theme", "dark");
            themeToggle.innerText = "Light Mode";
        } else {
            localStorage.setItem("theme", "light");
            themeToggle.innerText = "Dark Mode";
        }
    });

    // document.getElementsByClassName("home-btn").addEventListener("click", function () {
    //     const user = localStorage.getItem("userId"); // Assuming you store user data in localStorage
    
    //     if (user) {
    //         window.location.href = "customer_dashboard.html"; // Go to Cart Page
    //     } else {
    //         window.location.href = "index.html"; // Redirect to Login Page
    //     }
    // });
    

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