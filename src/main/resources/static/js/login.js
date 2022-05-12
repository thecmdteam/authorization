registerPasswordVisiblityToggle()



function registerPasswordVisiblityToggle() {
    const toggleIcon = document.querySelector(".toggle_icon");
    const password = document.querySelector("#password");

    toggleIcon.addEventListener("click", function (e) {
        e.preventDefault()
        const type = password.getAttribute("type") === "password" ? "text" : "password";
        password.setAttribute("type", type);
        const icon = type == 'password' ? 'show' : 'hide'
        const passwordToggleIcon = `<img src='../assets/password_${icon}.svg' />`
        toggleIcon.innerHTML = ''
        toggleIcon.innerHTML = passwordToggleIcon
    });
}