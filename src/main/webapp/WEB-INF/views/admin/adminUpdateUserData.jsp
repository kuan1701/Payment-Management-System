<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : 'en'}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <title><fmt:message key="admin.update_user.title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <link rel="shortcut icon" href="<c:url value="/images/favicon-black.ico"/>" type="image/x-icon">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/intlTelInput.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style-fixed-footer.css"/>">
</head>
<body>
<div class="main">
    <jsp:include page="../template/header.jsp"/>

    <!-- Alert unableGetUserId -->
    <c:if test="${response eq 'unableGetUserId'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="admin.page.failed"/></strong>
                <fmt:message key="admin.page.alertUnableGetUserIdError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert Success -->
    <c:if test="${response eq 'dataUpdatedSuccess'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="admin.page.success"/>!</strong>
                <fmt:message key="admin.page.alertUserDataUpdatedSuccess"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert invalidData -->
    <c:if test="${response eq 'invalidData'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="registration.failed"/>!</strong>
                <fmt:message key="admin.page.alertInvalidDataError"/>
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
            <p><strong><fmt:message key="registration.failed"/>!</strong>
                <fmt:message key="admin.page.alertEmailExistError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert dataUpdatedError -->
    <c:if test="${response eq 'dataUpdatedError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="admin.page.failed"/></strong>
                <fmt:message key="admin.page.alertUserDataUpdatedError"/>
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
                <fmt:message key="admin.update_user.formHeader" var="formHeader"/>
                <fmt:message key="registration.name" var="name"/>
                <fmt:message key="registration.surname" var="surname"/>
                <fmt:message key="registration.email" var="email"/>
                <fmt:message key="user.update_data.updateData" var="updateDataButton"/>
                <fmt:message key="registration.nameError" var="nameError"/>
                <fmt:message key="registration.surnameError" var="surnameError"/>
                <fmt:message key="registration.phoneError" var="loginError"/>
                <fmt:message key="registration.emailError" var="emailError"/>
                <fmt:message key="registration.tooltipOnlyLetters" var="tooltipOnlyLetters"/>
                <fmt:message key="registration.tooltipOnlyDigits" var="tooltipPhone"/>
                <fmt:message key="registration.tooltipEmail" var="tooltipEmail"/>
                <fmt:message key="registration.correct" var="correct"/>
                <fmt:message key="admin.user.returnToUsers" var="returnToUsers"/>
                <fmt:message key="admin.attach_account.returnToUserProfile" var="returnToUserProfile"/>

                <div class="page-content container-fluid">
                    <div class="row justify-content-center">
                        <div class="col-xl-8 offset-xl-1 mr-auto">
                            <div class="login-wrapper">
                                <div class="box">
                                    <div class="content-wrap">

                                        <h4>
                                            ${formHeader}
                                        </h4>

                                        <c:choose>
                                            <c:when test="${response ne 'unableGetUserId'}">
                                                <form action="/admin/updateUserData/${updateUser.userId}" method="POST" role="form">

                                                    <div class="form-row">

                                                        <!-- Name -->
                                                        <div class="col-md-6">
                                                            <input id="name" name="name"
                                                                   type="text" class="form-control"
                                                                   data-toggle="tooltip-left"
                                                                   data-title="${tooltipOnlyLetters}"
                                                                   maxlength="24" placeholder="${name}*"
                                                                   value="${updateUser.name}"/>
                                                            <label for="name" class="default-label">
                                                                <span id="valid-msg-name" class="valid-msg invisible">
                                                                    ${correct}
                                                                    <img src="<c:url value="/images/correct.png"/>" alt=""/>
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
                                                                   value="${updateUser.surname}"/>
                                                            <label for="surname" class="default-label">
                                                                <span id="valid-msg-surname"
                                                                      class="valid-msg invisible">
                                                                    ${correct}
                                                                    <img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                                </span>
                                                                <span id="error-msg-surname"
                                                                      class="error-msg invisible">
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
                                                                   data-toggle="tooltip" data-title="${tooltipPhone}"
                                                                   onkeypress="inputOnlyNumbers();"
                                                                   value="${updateUser.phone}"/>
                                                            <label for="phone" class="default-label">
                                                                <span id="valid-msg-phone" class="valid-msg invisible">
                                                                    ${correct}
                                                                    <img src="<c:url value="/images/correct.png"/>" alt=""/>
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
                                                            <input id="email" name="email"
                                                                   type="email" class="form-control"
                                                                   data-toggle="tooltip"
                                                                   data-title="${tooltipEmail}"
                                                                   maxlength="45" placeholder="${email}"
                                                                   value="${updateUser.email}"/>
                                                            <label for="email" class="default-label">
                                                                <span id="valid-msg-email" class="valid-msg invisible">
                                                                    ${correct}
                                                                    <img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                                </span>
                                                                <span id="error-msg-email" class="error-msg invisible">
                                                                        ${emailError}
                                                                </span>
                                                            </label>
                                                        </div>
                                                    </div>

                                                    <div class="action" style="padding: 25px 0 10px 0">
                                                        <button id="submit" type="submit" class="btn btn-primary signup"
                                                                style="width: 56%;">
                                                                ${updateDataButton}
                                                        </button>
                                                    </div>
                                                </form>

                                                <div class="action back-btn">
                                                    <form action="/admin/userInfo/${updateUser.userId}" method="GET" role="form">
                                                        <input type="hidden" name="command" value="showUser"/>
                                                        <input type="hidden" name="userId" value="${userId}"/>
                                                        <button type="submit" class="btn btn-primary signup btn-default"
                                                                style="width: 56%;">
                                                                ${returnToUserProfile}
                                                        </button>
                                                    </form>
                                                </div>
                                            </c:when>
                                            <c:otherwise>

                                                <!-- Return to Users -->
                                                <div class="message-block">
                                                    <span class="title-label forward-left-link-img">
                                                        <a href="" class="float-left">
                                                            <img src="<c:url value="/images/return.png"/>"
                                                                 class="icon-return" alt=""/>
                                                                ${returnToUsers}
                                                        </a>
                                                    </span>
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
<script src="<c:url value="/js/validator_adminUpdateUserData.js"/>"></script>
</html>