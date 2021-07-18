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
    <title><fmt:message key="user.update_data.title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <link rel="shortcut icon" href="<c:url value="/images/favicon-white.ico"/>" type="image/x-icon">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/intlTelInput.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
</head>
<body>

<!-- Modal window -->
<div id="deleteUserModal" class="modal fade" tabindex="-1" role="dialog" onfocus="this.blur();">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">
                    <fmt:message key="user.update_data.modalHeader"/>
                </h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                <fmt:message key="user.update_data.modalBody"/>
                <a href="?command=showAccounts">
                    <fmt:message key="user.page.viewAccounts"/>
                </a>
            </div>
            <div class="modal-footer">
                <div class="btn-group">
                    <button type="button" class="btn btn-default closeButton" data-dismiss="modal">
                        <fmt:message key="user.page.closeButton"/>
                    </button>
                    <div style="margin-left: 10px; border-left: 1px solid #e5e5e5;"></div>
                    <form action="" method="POST" role="form">
                        <input type="hidden" name="command" value="deleteProfile"/>
                        <button type="submit" class="btn btn-primary confirmButton" onfocus="this.blur();">
                            <fmt:message key="user.page.confirmButton"/>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="main">
    <jsp:include page="../template/header.jsp"/>

    <!-- Alert Success -->
    <c:if test="${updateInfo eq 'Update data successfully'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="user.page.success"/>!</strong>
                <fmt:message key="user.page.alertDataUpdatedSuccess"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert passwordNotMatchError -->
    <c:if test="${updateInfo eq 'passwordNotMatchError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertPasswordNotMatchError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert invalidData -->
    <c:if test="${updateInfo eq 'invalidData'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertInvalidDataError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert phoneExistError -->
    <c:if test="${updateInfo eq 'This phone already exist'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertPhoneExistError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert emailExistError -->
    <c:if test="${updateInfo eq 'This email already exist'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertEmailExistError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert dataUpdatedError -->
    <c:if test="${updateInfo eq 'Update data error'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertDataUpdatedError"/>
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
                <fmt:message key="user.update_data.formHeader" var="formHeader"/>
                <fmt:message key="registration.name" var="name"/>
                <fmt:message key="registration.surname" var="surname"/>
                <fmt:message key="registration.email" var="email"/>
                <fmt:message key="user.update_data.password" var="password"/>
                <fmt:message key="user.update_data.updateData" var="updateData"/>
                <fmt:message key="user.update_data.changePassword" var="changePassword"/>
                <fmt:message key="registration.nameError" var="nameError"/>
                <fmt:message key="registration.surnameError" var="surnameError"/>
                <fmt:message key="registration.phoneError" var="loginError"/>
                <fmt:message key="registration.emailError" var="emailError"/>
                <fmt:message key="registration.passwordError" var="passwordError"/>
                <fmt:message key="registration.tooltipOnlyLetters" var="tooltipOnlyLetters"/>
                <fmt:message key="registration.tooltipOnlyDigits" var="tooltipPhone"/>
                <fmt:message key="registration.tooltipEmail" var="tooltipEmail"/>
                <fmt:message key="user.update_data.tooltipPassword" var="tooltipPassword"/>
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

                                        <c:url value="/profile-info/${user.userId}" var="var"/>
                                        <form:form action="${var}" method="POST" role="form" modelAttribute="user">
                                            <input type="hidden" name="userId" value="${user.userId}"/>
                                            <input type="hidden" name="username" value="${user.username}"/>

                                            <div class="form-row">

                                                <!-- Name -->
                                                <div class="col-md-6">
                                                    <input id="name" name="name" type="text"
                                                                class="form-control"
                                                                data-toggle="tooltip-left"
                                                                data-title="${tooltipOnlyLetters}"
                                                                maxlength="24" placeholder="${name}*"
                                                                value="${user.name}"/>
                                                    <label for="name" class="default-label">
                                                        <span id="valid-msg-name" class="valid-msg invisible">
                                                            ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                        </span>
                                                        <span id="error-msg-name" class="error-msg invisible">
                                                            ${nameError}
                                                        </span>
                                                    </label>
                                                </div>

                                                <!-- Surname -->
                                                <div class="col-md-6">
                                                    <input id="surname" name="surname"
                                                                type="text" class="form-control"
                                                                data-toggle="tooltip"
                                                                data-title="${tooltipOnlyLetters}"
                                                                maxlength="32" placeholder="${surname}*"
                                                                value="${user.surname}"/>
                                                    <label for="surname" class="default-label">
                                                        <span id="valid-msg-surname" class="valid-msg invisible">
                                                            ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                        </span>
                                                        <span id="error-msg-surname" class="error-msg invisible">
                                                            ${surnameError}
                                                        </span>
                                                    </label>
                                                </div>
                                            </div>

                                            <!-- Phone -->
                                            <div class="row justify-content-center">
                                                <div class="col-md-9" style="margin-top: 8px">
                                                    <input id="phone" name="phone"
                                                           type="tel" class="form-control"
                                                           data-toggle="tooltip"
                                                           data-title="${tooltipPhone}"
                                                           value="${user.phone}"/>
                                                    <label for="phone" class="default-label">
                                                        <span id="valid-msg-phone" class="valid-msg invisible">
                                                            ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                        </span>
                                                        <span id="error-msg-phone" class="error-msg invisible">
                                                            ${loginError}
                                                        </span>
                                                    </label>
                                                </div>
                                            </div>

                                            <!-- Email -->
                                            <div class="row justify-content-center">
                                                <div class="col-md-9">
                                                    <input id="email" name="email" type="email" class="form-control"
                                                           data-toggle="tooltip"
                                                           data-title="${tooltipEmail}"
                                                           maxlength="45" placeholder="${email}"
                                                           value="${user.email}"/>
                                                    <label for="email" class="default-label">
                                                        <span id="valid-msg-email" class="valid-msg invisible">
                                                            ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                        </span>
                                                        <span id="error-msg-email" class="error-msg invisible">
                                                            ${emailError}
                                                        </span>
                                                    </label>
                                                </div>
                                            </div>

                                            <!-- Password -->
                                            <div class="row justify-content-center">
                                                <div class="col-md-9">
                                                    <div class="password-input">
                                                        <input id="password" name="password" type="password"
                                                               class="form-control"
                                                               data-toggle="tooltip"
                                                               data-title="${tooltipPassword}"
                                                               minlength="6" maxlength="255"
                                                               placeholder="${password}*"
                                                               value="${passwordValue}"/>
                                                        <a href="#" class="password-control" style="top: 9px;"
                                                           onfocus="this.blur();"
                                                           onclick="return toggle_password(this);"></a>
                                                    </div>
                                                    <label for="password" class="default-label">
                                                        <span id="valid-msg-password" class="valid-msg invisible">
                                                            ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                        </span>
                                                        <span id="error-msg-password" class="error-msg invisible">
                                                            ${passwordError}
                                                        </span>
                                                    </label>
                                                </div>
                                            </div>

                                            <!-- Submit -->
                                            <div class="action" style="padding: 25px 0 10px 0">
                                                <button id="submit" type="submit" class="btn btn-primary signup"
                                                        style="width: 56%;">
                                                    ${updateData}
                                                </button>
                                            </div>
                                        </form:form>

                                        <!-- Change Password Button -->
                                        <div class="action back-btn">
                                            <form action="${pageContext.request.contextPath}/update-password/${userId}" method="GET" role="form">
                                                <button type="submit" class="btn btn-primary signup btn-default"
                                                        style="width: 56%;">
                                                    ${changePassword}
                                                </button>
                                            </form>
                                        </div>

                                        <div class="block-questions" style="margin-top: 0;">
                                            <p style="margin-bottom: 0;">
                                                <fmt:message key="user.update_data.wantDeleteAccount"/>
                                            </p>
                                            <a href="#deleteUserModal" onclick="showDeleteUserModal();"
                                               onfocus="this.blur();">
                                                <fmt:message key="user.update_data.delete"/>
                                            </a>
                                        </div>
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
<script src="<c:url value="/js/validator_userUpdatePersonalData.js"/>"></script>
<script src="<c:url value="/js/modalWindow_userUpdatePersonalData.js"/>"></script>
</html>