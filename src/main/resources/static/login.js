const loginForm =
    document.getElementById("loginForm");

loginForm.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();

        const email =
            document.getElementById("email").value.trim();

        const password =
            document.getElementById("password").value;

        const message =
            document.getElementById("message");

        const loginData = {
            email: email,
            password: password
        };

        try {

            const response = await fetch(
                "/api/auth/login",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(loginData)
                }
            );

            const data = await response.json();

            if (response.ok) {

                message.style.color = "green";

                message.textContent = data.message;

                localStorage.setItem(
                    "userName",
                    data.name
                );

                localStorage.setItem(
                    "userEmail",
                    data.email
                );

                setTimeout(function () {

                    window.location.href =
                        "dashboard.html";

                }, 1000);

            } else {

                message.style.color = "red";

                message.textContent = data.message;
            }

        } catch (error) {

            message.style.color = "red";

            message.textContent =
                "Server connection failed";
        }
    }
);