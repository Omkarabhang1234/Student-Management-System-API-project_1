const registerForm =
    document.getElementById("registerForm");

registerForm.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();

        const name =
            document.getElementById("name").value.trim();

        const email =
            document.getElementById("email").value.trim();

        const password =
            document.getElementById("password").value;

        const confirmPassword =
            document.getElementById("confirmPassword").value;

        const message =
            document.getElementById("message");

        if (password !== confirmPassword) {

            message.style.color = "red";

            message.textContent =
                "Password and Confirm Password do not match";

            return;
        }

        const user = {
            name: name,
            email: email,
            password: password
        };

        try {

            const response = await fetch(
                "/api/auth/register",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(user)
                }
            );

            const data = await response.json();

            if (response.ok) {

                message.style.color = "green";

                message.textContent = data.message;

                registerForm.reset();

                setTimeout(function () {

                    window.location.href = "login.html";

                }, 1500);

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