function registerPasswordVisiblityToggle() {
    const toggleIcon = document.querySelector("#toggle");
    const password = document.querySelector("#password");
    console.log(toggleIcon)

    toggleIcon.addEventListener('click', (e) => {
        e.preventDefault()
        console.log('ciic3')
        const type = password.getAttribute("type") === "password" ? "text" : "password";
        password.setAttribute("type", type);
        const icon = type == 'password' ? 'show' : 'hide'
        const passwordToggleIcon = `<img src='../assets/password_${icon}.svg' />`
        toggleIcon.innerHTML = ''
        toggleIcon.innerHTML = passwordToggleIcon
    });
}

registerPasswordVisiblityToggle()