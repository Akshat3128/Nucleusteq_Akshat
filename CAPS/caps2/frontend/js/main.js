document.addEventListener("DOMContentLoaded", function() {
    const restaurantContainer = document.getElementById("restaurant-container");
    
    // Sample restaurant data (Replace with API fetch later)
    const restaurants = [
        { name: "Spicy Bites",  rating: "4.5" },
        { name: "Tandoori Delight", rating: "4.7" },
        { name: "Gourmet Haven",  rating: "4.6" },
        { name: "Pizza Palace",  rating: "4.4" }
    ];

    function loadRestaurants() {
        restaurantContainer.innerHTML = "";
        restaurants.forEach(restaurant => {
            const card = document.createElement("div");
            card.classList.add("restaurant-card");
            card.innerHTML = `
                <img src="${restaurant.image}" alt="${restaurant.name}" class="restaurant-img">
                <h3>${restaurant.name}</h3>
                <p>⭐ ${restaurant.rating}</p>
            `;
            restaurantContainer.appendChild(card);
        });
    }

    loadRestaurants();
    
    // Search functionality
    document.getElementById("search-bar").addEventListener("input", function(e) {
        const query = e.target.value.toLowerCase();
        const filteredRestaurants = restaurants.filter(res => res.name.toLowerCase().includes(query));
        restaurantContainer.innerHTML = "";
        filteredRestaurants.forEach(restaurant => {
            const card = document.createElement("div");
            card.classList.add("restaurant-card");
            card.innerHTML = `
                <img src="${restaurant.image}" alt="${restaurant.name}" class="restaurant-img">
                <h3>${restaurant.name}</h3>
                <p>⭐ ${restaurant.rating}</p>
            `;
            restaurantContainer.appendChild(card);
        });
    });
});

// document.addEventListener("DOMContentLoaded", function () {
//     const themeToggleBtn = document.createElement("button");
//     themeToggleBtn.classList.add("theme-toggle");
//     document.body.appendChild(themeToggleBtn);

//     // Function to update button text based on theme
//     function updateThemeButton() {
//         if (document.body.classList.contains("dark-theme")) {
//             themeToggleBtn.innerText = "Light Mode";
//         } else {
//             themeToggleBtn.innerText = "Dark Mode";
//         }
//     }

//     // Apply Theme from localStorage
//     if (localStorage.getItem("theme") === "dark") {
//         document.body.classList.add("dark-theme");
//     }
//     updateThemeButton();

//     themeToggleBtn.addEventListener("click", () => {
//         document.body.classList.toggle("dark-theme");
//         if (document.body.classList.contains("dark-theme")) {
//             localStorage.setItem("theme", "dark");
//         } else {
//             localStorage.setItem("theme", "light");
//         }
//         updateThemeButton();
//     });

//     // Custom Cursor Effect
//     const cursor = document.createElement("div");
//     cursor.classList.add("custom-cursor");
//     document.body.appendChild(cursor);

//     document.addEventListener("mousemove", (e) => {
//         cursor.style.left = `${e.clientX}px`;
//         cursor.style.top = `${e.clientY}px`;
//     });

//     document.addEventListener("mouseover", () => {
//         cursor.style.transform = "scale(1.5)";
//     });

//     document.addEventListener("mouseleave", () => {
//         cursor.style.transform = "scale(1)";
//     });
// });


// Dynamically fetch restaurant all
document.addEventListener("DOMContentLoaded", function () {
    const restaurantContainer = document.getElementById("restaurant-container");

    async function fetchRestaurants() {
        try {
            // Show a loading spinner while fetching
            restaurantContainer.innerHTML = `<div class="loader"></div>`;

            const response = await fetch("http://localhost:8080/restaurants/all");
            if (!response.ok) throw new Error(`HTTP Error: ${response.status}`);

            const data = await response.json();
            const activeRestaurants = data.filter(restaurant => restaurant.active);

            // Delay for smooth transition
            setTimeout(() => displayRestaurants(activeRestaurants), 500);
        } catch (error) {
            console.error("Error fetching restaurants:", error);
            restaurantContainer.innerHTML = "<p class='error'>Failed to load restaurants. Try again later.</p>";
        }
    }

    function displayRestaurants(restaurants) {
        restaurantContainer.innerHTML = ""; // Clear the loader

        if (restaurants.length === 0) {
            restaurantContainer.innerHTML = "<p>No active restaurants available.</p>";
            return;
        }

        restaurants.forEach((restaurant, index) => {
            const restaurantCard = document.createElement("div");
            restaurantCard.classList.add("restaurant-card");
            restaurantCard.dataset.id = restaurant.id; // Store restaurant ID

            // Generate random rating
            const randomRating = (Math.random() * (5 - 3) + 3).toFixed(1);
            const stickerType = Math.random() > 0.5 ? "Popular" : "New";

            restaurantCard.innerHTML = `
                <div class="sticker">${stickerType}</div>
                <h3>${restaurant.name}</h3>
                <p>Status: <span class="status">${restaurant.active ? "Open" : "Closed"}</span></p>
                <p class="rating">⭐ ${randomRating}</p>
            `;

            // Set initial opacity for smooth fade-in effect
            restaurantCard.style.opacity = "0";
            restaurantCard.style.transform = "translateY(10px)";

            // Attach click event to navigate to restaurant details
            restaurantCard.addEventListener("click", function () {
                window.location.href = `./pages/restaurant.html?id=${restaurant.id}`;
            });

            restaurantContainer.appendChild(restaurantCard);

            // Apply fade-in effect with slight delay
            setTimeout(() => {
                restaurantCard.style.opacity = "1";
                restaurantCard.style.transform = "translateY(0)";
            }, index * 150);
        });
    }

    fetchRestaurants();
});
