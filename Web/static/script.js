function validateLogin() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    if (username === "admin" && password === "password") {
        alert("Login successful!");
        return true;
    } else {
        document.getElementById("loginMessage").innerHTML = "Invalid username or password.";
        return false;
    }
}

function validateContact() {
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const message = document.getElementById("message").value;
    if (name !== "" && email !== "" && message !== "") {
        alert("Message sent successfully!");
        return true;
    } else {
        document.getElementById("contactMessage").innerHTML = "Please fill in all fields.";
        return false;
    }
}


document.addEventListener("DOMContentLoaded", () => {
    const getStartedBtn = document.getElementById("getStartedBtn");
    
    getStartedBtn.addEventListener("click", () => {
        alert("Get Started! Please proceed to the login page.");
        window.location.href = "login.html"; // Redirect to login page
    });
});


