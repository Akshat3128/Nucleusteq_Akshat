
    document.getElementById("loginForm").addEventListener("submit", async (e) => {
      e.preventDefault();
      const email = e.target.email.value;
      const password = e.target.password.value;
      
      try {
        const res = await fetch(`http://localhost:8080/api/hr/login?email=${email}&password=${password}`, { method: 'POST' });
        
        if (!res.ok) {
          throw new Error('Network response was not ok');
        }

        const msg = await res.text();
        showToast(msg, !msg.includes("successful"));


        if (msg.includes("successful")) {
          window.location.href = "dashboard.html";
        }
      } catch (error) {
        document.getElementById("message").textContent = "Error: " + error.message;
      }
      function showToast(message, isError = false) {
        const toast = document.getElementById("toast");
        toast.textContent = message;
        toast.className = "toast" + (isError ? " error" : "");
        toast.style.display = "block";
        setTimeout(() => {
        toast.style.display = "none";
        }, 3000);
    }
    });
