// Register
const registerForm = document.getElementById("registerForm");
if (registerForm) {
    registerForm.addEventListener("submit", async function (event) {
        event.preventDefault();
        const message = document.getElementById("registerMessage");
        const name = document.getElementById("name").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value;

        message.innerHTML = "Processing...";
        message.style.color = "blue";

        try {
            const response = await fetch("/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ name, email, password })
            });

            const data = await response.json();
            if (response.ok) {
                message.innerHTML = data.message || "Registration Successful!";
                message.style.color = "green";
                message.style.fontWeight = "bold";
                registerForm.reset();
                setTimeout(function () {
                    window.location.href = "/login";
                }, 1500);
            } else {
                message.innerHTML = data.message || "Registration Failed.";
                message.style.color = "red";
            }
        } catch (error) {
            console.error("Error registering user:", error);
            message.innerHTML = "Server connection failed.";
            message.style.color = "red";
        }
    });
}

// Login
const loginForm = document.getElementById("loginForm");
if (loginForm) {
    // Show logout/expired messages from URL parameters if present
    const urlParams = new URLSearchParams(window.location.search);
    const loginMessage = document.getElementById("loginMessage");
    if (loginMessage) {
        if (urlParams.has("logout")) {
            loginMessage.innerHTML = "You have logged out successfully.";
            loginMessage.style.color = "green";
        } else if (urlParams.has("sessionExpired")) {
            loginMessage.innerHTML = "Your session expired. Please log in again.";
            loginMessage.style.color = "red";
        }
    }

    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();
        const message = document.getElementById("loginMessage");
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value;

        message.innerHTML = "Authenticating...";
        message.style.color = "blue";

        try {
            const response = await fetch("/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email, password })
            });

            if (response.ok) {
                const data = await response.json();
                message.innerHTML = data.message || "Login Successful!";
                message.style.color = "green";
                message.style.fontWeight = "bold";
                
                // Store user details for front-end templates
                localStorage.setItem("userName", data.name);
                localStorage.setItem("userEmail", data.email);
                localStorage.setItem("userRole", data.role);

                setTimeout(function () {
                    window.location.href = "/dashboard";
                }, 1000);
            } else {
                // Try parsing error message
                let errMsg = "Invalid email or password.";
                try {
                    const errorData = await response.json();
                    if (errorData && errorData.message) errMsg = errorData.message;
                } catch(e) {}
                message.innerHTML = errMsg;
                message.style.color = "red";
            }
        } catch (error) {
            console.error("Error logging in:", error);
            message.innerHTML = "Server connection failed.";
            message.style.color = "red";
        }
    });
}