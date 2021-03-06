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
    <title><fmt:message key="user.page.myAccounts"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <link rel="shortcut icon" href="<c:url value="/images/favicon-white.ico"/>" type="image/x-icon">
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap-formhelpers.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style_userShowAccounts.css"/>">
</head>
<body>
<div class="main">
    <jsp:include page="../template/header.jsp"/>

    <!-- Alerts unableGetData and showUserAccountsError -->
    <c:if test="${response eq 'unableGetData' || response eq 'showUserAccountsError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertUnableGetData"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert searchAccountsSuccess -->
    <c:if test="${response eq 'searchAccountsSuccess'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p>
                <fmt:message key="admin.page.alertSearchAccountsSuccess"/>
                    ${numberOfAccounts}
                <fmt:message key="admin.user_accounts.accounts"/>.
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert searchAccountsWarning -->
    <c:if test="${response eq 'searchAccountsWarning'}">
        <div id="alert" class="alert alert-warning fade show" role="alert">
            <p>
                <fmt:message key="admin.page.alertSearchAccountsWarning"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert searchAccountsError -->
    <c:if test="${response eq 'searchAccountsError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="admin.page.alertSearchAccountsError"/>
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
                <fmt:message key="user.page.myAccounts" var="myAccounts"/>
                <fmt:message key="user.page.myPayments" var="myPayments"/>
                <fmt:message key="user.account_info.balance" var="balance"/>
                <fmt:message key="user.account_info.status.active" var="statusActive"/>
                <fmt:message key="user.account_info.status.blocked" var="statusBlocked"/>
                <fmt:message key="user.page.settings" var="settings"/>
                <fmt:message key="user.create_account.numberNewAccount" var="numberAccount"/>
                <fmt:message key="admin.user_accounts.balanceRange" var="balanceRange"/>
                <fmt:message key="registration.tooltipOnlyLetters" var="tooltipOnlyLetters"/>
                <fmt:message key="registration.tooltipOnlyDigits" var="tooltipOnlyDigits"/>
                <fmt:message key="admin.user_accounts.searchCriteria" var="searchCriteria"/>
                <fmt:message key="admin.user_accounts.searchButton" var="searchButton"/>

                <div class="page-content container-fluid">
                    <div class="row">
                        <div class="col-xl-12">
                            <div class="login-wrapper">
                                <div class="box">

                                    <div class="card-header">
                                        <ul class="nav nav-tabs card-header-tabs justify-content-lg-center"
                                            role="tablist">
                                            <li class="nav-item-active">
                                                <a href="${pageContext.request.contextPath}/show-accounts" class="nav-link">
                                                    <img src="<c:url value="/images/show-accounts.png"/>" class="icon-sidebar"
                                                         style="width: 20px; height: 20px;" alt=""/>
                                                    ${myAccounts}
                                                </a>
                                            </li>
                                            <li class="nav-item">
                                                <a href="${pageContext.request.contextPath}/show-payments/${user.userId}" class="nav-link">
                                                    <img src="<c:url value="/images/show-payments.png"/>"
                                                         class="icon-sidebar" style="height: 17px" alt=""/>
                                                    ${myPayments}
                                                </a>
                                            </li>
                                        </ul>
                                    </div>

                                    <c:choose>
                                        <c:when test="${accountsEmpty == false}">

                                            <div class="card-body card-body-main">
                                                <div class="row">
                                                    <div class="col-lg-3 col-xl-3">
                                                        <div class="search-block">
                                                            <label>
                                                                    ${searchCriteria}:
                                                            </label>

                                                            <c:url value="/show-accounts" var="var"/>
                                                            <form:form action="${var}" method="POST" role="form" modelAttribute="accountList">

                                                                <!-- AccountDto Number -->
                                                                <div>
                                                                    <input id="number" name="accountNumber" type="text"
                                                                           class="form-control"
                                                                           data-toggle="tooltip-left"
                                                                           data-title="${tooltipOnlyDigits}"
                                                                           maxlength="20"
                                                                           onkeypress="inputOnlyNumbers();"
                                                                           placeholder="${numberAccount}"
                                                                           value="${numberValue}"/>
                                                                </div>

                                                                <!-- Min value Balance -->
                                                                <input type="hidden" id="min-value"
                                                                       name="min_value"
                                                                       value="${minValue}"/>

                                                                <!-- Max value Balance -->
                                                                <input type="hidden" id="max-value"
                                                                       name="max_value"
                                                                       value="${maxValue}"/>

                                                                <!-- Balance Range -->
                                                                <div>
                                                                    <input id="amount" type="text"
                                                                           class="for-form-label"
                                                                           readonly="readonly"/>
                                                                    <div id="slider-range"
                                                                         data-toggle="tooltip-left"
                                                                         data-title="${balanceRange}"></div>
                                                                    <label for="slider-range"
                                                                           class="default-label"> </label>
                                                                </div>

                                                                <!-- Currency -->
                                                                <input type="hidden" id="currency"
                                                                       name="currency"
                                                                       value="${currencyValue}"/>

                                                                <!-- Select Currency -->
                                                                <div>
                                                                    <div class="bfh-selectbox bfh-currencies"
                                                                         data-flags="true" data-currency="">
                                                                    </div>
                                                                    <label class="default-label">&nbsp;</label>
                                                                </div>

                                                                <div class="action" style="padding: 10px 0 5px 0">
                                                                    <button id="search" type="submit"
                                                                            class="btn btn-primary signup">
                                                                            ${searchButton}
                                                                    </button>
                                                                </div>
                                                            </form:form>
                                                        </div>
                                                    </div>

                                                    <div class="col-lg-9 col-xl-9">
                                                        <div class="col-xl-12">
                                                            <div class="form-row">
                                                                <div class="card-container">
                                                                    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-2 row-cols-xl-2">

                                                                        <c:forEach items="${accountList}" var="account">
                                                                            <div class="col mb-4">
                                                                                <div class="card bg-light">
                                                                                    <div class="card-header">
                                                                                        <c:choose>
                                                                                            <c:when test="${account.blocked}">
                                                                                                <small class="text-danger float-right">
                                                                                                        ${statusBlocked}
                                                                                                </small>
                                                                                            </c:when>
                                                                                            <c:otherwise>
                                                                                                <small class="text-success float-right">
                                                                                                        ${statusActive}
                                                                                                </small>
                                                                                            </c:otherwise>
                                                                                        </c:choose>
                                                                                    </div>
                                                                                    <div class="card-body"
                                                                                         style="padding: 0.75rem 1.25rem;">
                                                                                        <p class="card-title text-muted">
                                                                                                ${account.number}<br/>
                                                                                                ${balance}: ${account.balance} ${account.currency}

                                                                                            <!-- Show AccountDto Info -->
                                                                                            <a href="/account-setting/${account.number}"
                                                                                               class="float-right">
                                                                                                <img src="<c:url value="/images/info.png"/>"
                                                                                                     alt="${settings}"/>
                                                                                            </a>
                                                                                        </p>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </c:forEach>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="card-body" style="min-height: 325px; padding: 35px;">
                                                <div class="message-block">
                                                    <span>
                                                        <label>
                                                            <fmt:message key="user.page.accountsEmpty"/>
                                                            <a href="?command=createAccount" class="alert-link">
                                                                <fmt:message key="user.page.create"/>
                                                            </a>
                                                            <fmt:message
                                                                    key="user.page.acceptPaymentsFromAllOverTheWorld"/>
                                                        </label>
                                                    </span>
                                                    <div class="w-100" style="height:172px;">
                                                        <img src="<c:url value="/images/profit_2.png"/>" alt=""
                                                             style="width: 172px; height: 172px; position: absolute; bottom: 35px; right: 55px;"/>
                                                    </div>
                                                </div>
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
    <jsp:include page="../template/footer.jsp"/>
</div>
</body>
<script src="<c:url value="/js/searcher_userShowAccounts.js"/>"></script>
</html>