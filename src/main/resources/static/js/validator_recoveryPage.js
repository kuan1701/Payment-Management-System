/**
 * Elements on recovery.jsp page for validation
 */
let email = document.querySelector("#email");
let login = document.querySelector("#login");
let submitBtn = document.querySelector("#submit");

/**
 * Email validation
 */
let validMsgEmail = document.querySelector("#valid-msg-email"),
    errorMsgEmail = document.querySelector("#error-msg-email");

function resetEmail() {
    validMsgEmail.classList.add("invisible");
    errorMsgEmail.classList.add("invisible");
    email.classList.remove("valid-input");
    email.classList.remove("error-input");
}

function validEmail() {
    validMsgEmail.classList.remove("invisible");
    errorMsgEmail.classList.add("invisible");
    email.classList.add("valid-input");
    email.classList.remove("error-input");
}

function notValidEmail() {
    validMsgEmail.classList.add("invisible");
    errorMsgEmail.classList.remove("invisible");
    email.classList.remove("valid-input");
    email.classList.add("error-input");
}

email.addEventListener('click', resetEmail);
email.addEventListener('blur', validationEmail);
email.addEventListener('keyup', validationEmail);
email.addEventListener('change', validationEmail);

function validationEmail() {
    resetEmail();

    if (email.value.trim() === "") {
        notValidEmail();
    } else {
        if (email.value.trim().search(/[a-zA-Z0-9._-]+@[a-z0-9.-]+.[a-z]{2,}$/) === -1) {
            notValidEmail();
        } else {
            validEmail();
        }
    }
}
/**
 * Checks for errors on the page
 */
submitBtn.addEventListener('click', (event) => {

    validationEmail();
    if (email.classList.contains("error-input")) {
        event.preventDefault();
        notValidEmail();
        return false;
    }
});