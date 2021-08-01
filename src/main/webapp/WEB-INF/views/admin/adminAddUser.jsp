<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : 'en'}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <title><fmt:message key="admin.add_user.title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <link rel="shortcut icon" href="<c:url value="/images/favicon-black.ico"/>" type="image/x-icon">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/intlTelInput.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style_adminAddUser.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style-fixed-footer.css"/>">
</head>
<body>
<div class="main">
    <jsp:include page="../template/header.jsp"/>

    <!-- Alert Success -->
    <c:if test="${response eq 'addUserSuccess'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="admin.page.success"/>!</strong>
                <fmt:message key="admin.page.alertUserAddedSuccess"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert phoneExistError -->
    <c:if test="${response eq 'phoneExistError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="admin.page.failed"/>!</strong>
                <fmt:message key="admin.page.alertPhoneExistError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert emailExistError -->
    <c:if test="${response eq 'emailExistError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="admin.page.failed"/>!</strong>
                <fmt:message key="admin.page.alertEmailExistError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert addUserError -->
    <c:if test="${response eq 'addUserError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="registration.failed"/>!</strong>
                <fmt:message key="admin.page.alertUserAddedError"/>
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
                <fmt:message key="admin.add_user.addNewUser" var="addNewUser"/>
                <fmt:message key="registration.name" var="name"/>
                <fmt:message key="registration.surname" var="surname"/>
                <fmt:message key="registration.email" var="email"/>
                <fmt:message key="admin.add_user.passwordGenerated" var="passwordGenerated"/>
                <fmt:message key="admin.add_user.addUserButton" var="addUserButton"/>
                <fmt:message key="admin.add_user.attachAccount" var="attachAccount"/>
                <fmt:message key="admin.add_user.nameError" var="nameError"/>
                <fmt:message key="admin.add_user.surnameError" var="surnameError"/>
                <fmt:message key="registration.phoneError" var="loginError"/>
                <fmt:message key="registration.emailError" var="emailError"/>
                <fmt:message key="registration.tooltipOnlyLetters" var="tooltipOnlyLetters"/>
                <fmt:message key="registration.tooltipOnlyDigits" var="tooltipPhone"/>
                <fmt:message key="registration.tooltipEmail" var="tooltipEmail"/>
                <fmt:message key="registration.correct" var="correct"/>

                <div class="page-content container-fluid">
                    <div class="row justify-content-center">
                        <div class="col-xl-8 offset-xl-1 mr-auto">
                            <div class="login-wrapper">
                                <div class="box">
                                    <div class="content-wrap">

                                        <h4>
                                            ${addNewUser}
                                        </h4>

                                        <form:form action="/admin/createUser"
                                                   method="POST" role="form" modelAttribute="newUser">

                                            <div class="form-row">

                                                <!-- Name -->
                                                <div class="col-md-6">
                                                    <form:input id="name" name="name" type="text" class="form-control"
                                                                path="name"
                                                                data-toggle="tooltip-left"
                                                                data-title="${tooltipOnlyLetters}"
                                                                maxlength="24" placeholder="${name}*"
                                                                value="${nameValue}"></form:input>
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
                                                    <form:input id="surname" name="surname" type="text" class="form-control"
                                                           data-toggle="tooltip" path="surname"
                                                           data-title="${tooltipOnlyLetters}"
                                                           maxlength="32" placeholder="${surname}*"
                                                           value="${surnameValue}"></form:input>
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
                                                    <form:input id="phone" name="phone" type="tel" class="form-control"
                                                           data-toggle="tooltip" path="phone"
                                                           data-title="${tooltipPhone}"
                                                           onkeypress="form:inputOnlyNumbers();"
                                                           value="${phoneValue}"></form:input>
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
                                                    <form:input id="email" name="email" type="email" class="form-control"
                                                           data-toggle="tooltip" path="email"
                                                           data-title="${tooltipEmail}"
                                                           maxlength="45" placeholder="${email}"
                                                                value="${emailValue}"></form:input>
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
                                                    <input id="password" name="password"
                                                           type="password" class="form-control"
                                                           readonly="readonly" disabled="disabled"
                                                           placeholder="${passwordGenerated}"/>
                                                    <label for="password" class="default-label">&nbsp;</label>
                                                </div>
                                            </div>

                                            <!-- Submit -->
                                            <div class="action" style="padding: 20px 0 10px 0;">
                                                <button id="submit" type="submit" class="btn btn-primary signup"
                                                        style="width: 54%;">
                                                    ${addUserButton}
                                                </button>
                                            </div>
                                        </form:form>

                                        <!-- Back Button -->
                                        <c:choose>
                                            <c:when test="${response eq 'addUserSuccess'}">
                                                <div class="action back-btn">
                                                    <form action="/admin/attachAccount/${newUser.userId}" method="GET" role="form">
                                                        <button type="submit" class="btn btn-primary signup btn-default"
                                                                style="width: 54%;">
                                                                ${attachAccount}
                                                        </button>
                                                    </form>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="action back-btn">
                                                    <form action="/admin/attachAccount/${newUser.userId}" method="GET" role="form">
                                                        <button type="submit"
                                                                class="btn btn-primary signup btn-default disabled"
                                                                style="width: 54%;">
                                                                ${attachAccount}
                                                        </button>
                                                    </form>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
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
<script src="<c:url value="/js/validator_adminAddUser.js"/>"></script>
</html>