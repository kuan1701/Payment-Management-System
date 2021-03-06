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
    <title><fmt:message key="user.create_account.title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <link rel="shortcut icon" href="<c:url value="/images/favicon-white.ico"/>" type="image/x-icon">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap-formhelpers.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style-fixed-footer.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style_userCreateAccount.css"/>">
</head>
<body>
<div class="main">
    <jsp:include page="../template/header.jsp"/>

    <!-- Alert unableGetData -->
    <c:if test="${accountError eq 'Create account error'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertUnableGetData"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert Success -->
    <c:if test="${accountError eq 'Create success'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="user.page.success"/>!</strong>
                <fmt:message key="user.page.alertAccountCreatedSuccess"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert manyAccountWithThisCurrencyError -->
    <c:if test="${response eq 'manyAccountWithThisCurrencyError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.manyAccountWithThisCurrencyError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert accountCreatedError -->
    <c:if test="${accountError eq 'Create error'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertAccountCreatedError"/>
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
                <fmt:message key="user.create_account.createNewAccount" var="formHeader"/>
                <fmt:message key="user.create_account.numberNewAccount" var="numberNewAccount"/>
                <fmt:message key="user.create_account.accountCurrency" var="accountCurrency"/>
                <fmt:message key="user.create_account.createAccount" var="createAccount"/>
                <fmt:message key="user.create_account.numberError" var="numberError"/>
                <fmt:message key="user.create_account.currencyError" var="currencyError"/>
                <fmt:message key="registration.correct" var="correct"/>

                <div class="page-content container-fluid">
                    <div class="row">
                        <div class="col-xl-6 offset-xl-2 mr-auto">
                            <div class="login-wrapper">
                                <div class="box">
                                    <div class="content-wrap">

                                        <h4>
                                            ${formHeader}
                                        </h4>

                                        <form:form action="" method="POST" role="form" modelAttribute="account">
                                            <input type="hidden" name="userId" value="${account.user.userId}"/>

                                            <!-- AccountDto Number -->
                                            <div>
                                                <label class="for-form-label">
                                                    ${numberNewAccount}:
                                                </label>
                                                <div class="form-group">
                                                    <form:input id="number" name="number" path="number"
                                                                type="text" class="form-control"
                                                                style="height: 46px; margin: 0 10px 0 0; text-align: center; font-size: 18px;"
                                                                readonly="readonly" maxlength="20"
                                                                value="${numberValue}"></form:input>
                                                    <img id="repeat" src="<c:url value="/images/repeat.png"/>"
                                                         alt="" class="glyphicon icon-repeat"/>
                                                </div>
                                                <label for="number" class="default-label">
                                                    <span id="valid-msg-accountNumber" class="valid-msg invisible">
                                                        ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                    </span>
                                                    <span id="error-msg-accountNumber" class="error-msg invisible">
                                                        ${numberError}
                                                    </span>
                                                </label>
                                            </div>

                                            <!-- Currency -->
                                            <form:input type="hidden" id="currency" name="currency" path="currency"></form:input>

                                            <!-- Select Currency -->
                                            <div style="margin-top: 16px;">
                                                <label class="for-form-label">
                                                    ${accountCurrency}:
                                                </label>
                                                <div class="bfh-selectbox bfh-currencies"
                                                     data-currency="USD" data-flags="true" data-blank="false">
                                                </div>
                                                <label for="currency" class="default-label">
                                                    <span id="valid-msg-currency" class="valid-msg invisible">
                                                        ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                    </span>
                                                    <span id="error-msg-currency" class="error-msg invisible">
                                                        ${currencyError}
                                                    </span>
                                                </label>
                                            </div>

                                            <!-- Submit -->
                                            <div class="action">
                                                <button id="submit" type="submit" class="btn btn-primary signup">
                                                    ${createAccount}
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
<script src="<c:url value="/js/validator_userCreateAccount.js"/>"></script>
</html>