
registerPasswordVisiblityToggle()

function registerPasswordVisiblityToggle() {
    const toggleIcon = document.querySelector("#toggle_icon");
    const confirmToggleIcon = document.querySelector("#confirm_toggle_icon");
    const password = document.querySelector("#password");
    const confrimPassword = document.querySelector("#confirm-password");

    toggleIcon.addEventListener("click", function () {
        const type = password.getAttribute("type") === "password" ? "text" : "password";
        password.setAttribute("type", type);
        const icon = type == 'password' ? 'show' : 'hide'
        const passwordToggleIcon = `<img src='../assets/password_${icon}.svg' />`
        toggleIcon.innerHTML = ''
        toggleIcon.innerHTML = passwordToggleIcon
    });

    confirmToggleIcon.addEventListener("click", function () {
        const type = confrimPassword.getAttribute("type") === "password" ? "text" : "password";
        confrimPassword.setAttribute("type", type);
        const icon = type == 'password' ? 'show' : 'hide'
        const passwordToggleIcon = `<img src='../assets/password_${icon}.svg' />`
        confirmToggleIcon.innerHTML = ''
        confirmToggleIcon.innerHTML = passwordToggleIcon
    });
}
