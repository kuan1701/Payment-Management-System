<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : 'en'}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <title><fmt:message key="user.update_password.title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <link rel="shortcut icon" href="<c:url value="/images/favicon-white.ico"/>" type="image/x-icon">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style-fixed-footer.css"/>">
</head>
<body>
<div class="main">
    <jsp:include page="../template/header.jsp"/>

    <!-- Alert unableGetData -->
    <c:if test="${response eq 'unableGetData'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="admin.page.failed"/>!</strong>
                <fmt:message key="admin.page.alertUnableGetUser"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert Success -->
    <c:if test="${passwordError eq 'Update password successfully.'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="user.page.success"/>!</strong>
                <fmt:message key="user.page.alertPasswordUpdatedSuccess"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert oldPasswordError -->
    <c:if test="${passwordError eq 'Your old password is incorrect.'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertOldPasswordNotMatch"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert newPasswordError -->
    <c:if test="${passwordError eq 'You new password must be different than the old one.'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertNewPasswordNotConfirmed"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert passwordUpdatedError -->
    <c:if test="${response eq 'passwordUpdatedError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertPasswordUpdatedError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <div class="page-content">
        <div class="row">
            <div class="col-lg-2">
                <jsp:include page="../template/sidebar.jsp"/>
            </div>

            <div class="col-lg-10">
                <fmt:message key="user.update_password.formHeader" var="formHeader"/>
                <fmt:message key="user.update_password.newPassword" var="newPassword"/>
                <fmt:message key="registration.confirmation" var="confirmation"/>
                <fmt:message key="user.update_password.oldPassword" var="oldPassword"/>
                <fmt:message key="user.update_data.changePassword" var="changePassword"/>
                <fmt:message key="registration.passwordError" var="passwordError"/>
                <fmt:message key="registration.passwordConfirmationError" var="passwordConfirmationError"/>
                <fmt:message key="user.update_password.oldPasswordError" var="oldPasswordError"/>
                <fmt:message key="registration.tooltipPassword" var="tooltipNewPassword"/>
                <fmt:message key="registration.tooltipPasswordConfirmation" var="tooltipPasswordConfirmation"/>
                <fmt:message key="user.update_password.tooltipOldPassword" var="tooltipOldPassword"/>
                <fmt:message key="registration.correct" var="correct"/>

                <div class="page-content container-fluid">
                    <div class="row justify-content-center">
                        <div class="col-xl-8 offset-xl-1 mr-auto">
                            <div class="login-wrapper">
                                <div class="box">
                                    <div class="content-wrap">

                                        <h4>
                                            ${formHeader}
                                        </h4>

                                        <form:form action="" method="POST" role="form" modelAttribute="user">

                                            <!-- Old Password -->
                                            <div class="password-input">
                                                <input id="oldPassword" name="oldPassword"
                                                       type="password" class="form-control"
                                                       style="height: 40px; margin: 8px 0 0 0;"
                                                       data-toggle="tooltip"
                                                       data-title="${tooltipOldPassword}"
                                                       minlength="6" maxlength="255"
                                                       placeholder="${oldPassword}*"
                                                       value="${oldPasswordValue}"/>
                                                <a href="#" class="password-control"
                                                   onfocus="this.blur();"
                                                   onclick="return toggle_oldPassword(this);"></a>
                                                <label for="oldPassword" class="default-label">
                                                    <span id="valid-msg-oldPassword" class="valid-msg invisible">
                                                        ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                    </span>
                                                    <span id="error-msg-oldPassword" class="error-msg invisible">
                                                        ${passwordError}
                                                    </span>
                                                </label>
                                            </div>

                                            <!-- New Password -->
                                            <div class="password-input">
                                                <input id="newPassword" name="newPassword"
                                                       type="password" class="form-control"
                                                       style="height: 40px; margin: 8px 0 0 0;"
                                                       data-toggle="tooltip"
                                                       data-title="${tooltipNewPassword}"
                                                       minlength="6" maxlength="255"
                                                       placeholder="${newPassword}*"
                                                       value="${newPasswordValue}"/>
                                                <a href="#" class="password-control"
                                                   onfocus="this.blur();"
                                                   onclick="return toggle_newPassword(this);"></a>
                                                <label for="newPassword" class="default-label">
                                                    <span id="valid-msg-newPassword" class="valid-msg invisible">
                                                        ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                    </span>
                                                    <span id="error-msg-newPassword" class="error-msg invisible">
                                                        ${passwordError}
                                                    </span>
                                                </label>
                                            </div>

                                            <!-- Password Confirmation -->
                                            <div class="password-input">
                                                <input id="passwordConfirmation" name="passwordConfirmation"
                                                       type="password" class="form-control"
                                                       style="height: 40px; margin: 8px 0 0 0;"
                                                       data-toggle="tooltip"
                                                       data-title="${tooltipPasswordConfirmation}"
                                                       minlength="6" maxlength="255"
                                                       placeholder="${confirmation}*"
                                                       value="${passwordConfirmationValue}"/>
                                                <a href="#" class="password-control"
                                                   onfocus="this.blur();"
                                                   onclick="return toggle_passwordConfirmation(this);"></a>
                                            </div>
                                            <label for="passwordConfirmation" class="default-label">
                                                <span id="valid-msg-passwordConfirmation" class="valid-msg invisible">
                                                    ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                </span>
                                                <span id="error-msg-passwordConfirmation" class="error-msg invisible">
                                                    ${passwordConfirmationError}
                                                </span>
                                            </label>

                                            <!-- Submit -->
                                            <div class="action" style="padding: 25px 0 5px 0">
                                                <button id="submit" type="submit" class="btn btn-primary signup">
                                                    ${changePassword}
                                                </button>
                                            </div>
                                        </form:form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../template/footer.jsp"/>
</div>
</body>
<script src="<c:url value="/js/validator_userUpdatePassword.js"/>"></script>
</html>