document.addEventListener("DOMContentLoaded", function () {
    const signupForm = document.getElementById("signup-form");
    const loginForm = document.getElementById("login-form");

    // Handle Signup
    if (signupForm) {
        signupForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            const email = document.getElementById("signup-email").value;
            const password = document.getElementById("signup-password").value;
            const role = document.getElementById("signup-role").value;

            const response = await fetch("http://localhost:8080/auth/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password, role })
            });

            const data = await response.json();
            if (response.ok) {
                alert("Registration successful! Redirecting to login...");
                window.location.href = "login.html";
            } else {
                alert(data.message || "Signup failed. Try again.");
            }
        });
    }

    // Handle Login
    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            const email = document.getElementById("login-email").value;
            const password = document.getElementById("login-password").value;

            const response = await fetch("http://localhost:8080/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();
            if (response.ok) {
                alert("Login successful!");

                // Store user info correctly
                localStorage.setItem("userId", data.userId);
                localStorage.setItem("userRole", data.role);
                localStorage.setItem("userEmail", data.email);
                
                // Store restaurantId only if user is a RESTAURANT_OWNER
                if (data.role === "RESTAURANT_OWNER") {
                    localStorage.setItem("restaurantId", data.restaurantId);
                }

                // Redirect based on role
                if (data.role === "CUSTOMER") {
                    window.location.href = "../pages/customer_dashboard.html";
                } else if (data.role === "RESTAURANT_OWNER") {
                    window.location.href = "../pages/owner_dashboard.html";
                }
            } else {
                alert(data.message || "Invalid credentials!");
            }
        });
    }
});

