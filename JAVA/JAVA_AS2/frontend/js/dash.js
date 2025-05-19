const API_URL = "http://localhost:8080/api/employees";
    const tableBody = document.querySelector("#employeeTable tbody");
    const formContainer = document.getElementById("employeeFormContainer");
    const form = document.getElementById("employeeForm");

    function showToast(message, isError = false) {
      const toast = document.getElementById("toast");
      toast.textContent = message;
      toast.className = "toast" + (isError ? " error" : "");
      toast.style.display = "block";
      setTimeout(() => { toast.style.display = "none"; }, 3000);
    }

    async function loadEmployees() {
      const res = await fetch(API_URL);
      const employees = await res.json();
      tableBody.innerHTML = "";
      employees.forEach(e => {
        tableBody.innerHTML += `
          <tr>
            <td>${e.id}</td><td>${e.name}</td><td>${e.department}</td><td>${e.email}</td><td>${e.salary}</td>
            <td>
              <button onclick='editEmployee(${JSON.stringify(e)})'>Edit</button>
              <button onclick='deleteEmployee(${e.id})'>Delete</button>
            </td>
          </tr>`;
      });
    }

    form.addEventListener("submit", async e => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(form));
      const method = data.id ? "PUT" : "POST";
      const url = data.id ? `${API_URL}/${data.id}` : API_URL;

      await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });
      showToast("Employee saved successfully");
      closeForm();
      loadEmployees();
    });

    function showAddForm() {
      form.reset();
      form.id.value = "";
      formContainer.style.display = "block";
    }

    function editEmployee(emp) {
      form.name.value = emp.name;
      form.department.value = emp.department;
      form.email.value = emp.email;
      form.salary.value = emp.salary;
      form.id.value = emp.id;
      formContainer.style.display = "block";
    }

    function closeForm() {
      formContainer.style.display = "none";
    }

    async function deleteEmployee(id) {
      await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
      showToast("Employee deleted");
      loadEmployees();
    }

    loadEmployees();