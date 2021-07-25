<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : 'en'}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <title><fmt:message key="user.account_info.title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <link rel="shortcut icon" href="<c:url value="/images/favicon-white.ico"/>" type="image/x-icon">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap-formhelpers.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style-fixed-footer.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style_userShowAccountSettings.css"/>">
</head>
<body>

<!-- Modal window -->
<div id="deleteAccountModal" class="modal fade" tabindex="-1" role="dialog" onfocus="this.blur();">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">
                    <fmt:message key="user.account_info.modalHeader"/>
                </h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                <fmt:message key="user.account_info.modalBody"/><br>
            </div>
            <div class="modal-footer">
                <div class="btn-group">
                    <button type="button" class="btn btn-default closeButton" data-dismiss="modal">
                        <fmt:message key="user.page.closeButton"/>
                    </button>
                    <div style="margin-left: 10px; border-left: 1px solid #e5e5e5;"></div>
                    <c:url value="/account-setting/delete/${account.number}" var="var"/>
                    <form action="${var}" method="POST" role="form">
                        <input type="hidden" name="accountId" value="${account.accountId}"/>
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

    <!-- Alerts unableGetData and showAccountError -->
    <c:if test="${response eq 'unableGetData' || response eq 'showAccountError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertUnableGetData"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert unableGetAccountId -->
    <c:if test="${response eq 'unableGetAccountId'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertUnableGetAccountIdError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert accountBlockedSuccess -->
    <c:if test="${response eq 'accountBlockedSuccess'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="user.page.success"/>!</strong>
                <fmt:message key="user.page.alertAccountBlockedSuccess"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert accountBlockedError -->
    <c:if test="${response eq 'accountBlockedError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertAccountBlockedError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert accountUnblockedSuccess -->
    <c:if test="${response eq 'accountUnblockedSuccess'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="user.page.success"/>!</strong>
                <fmt:message key="user.page.alertAccountUnblockedSuccess"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert accountUnblockedError -->
    <c:if test="${response eq 'accountUnblockedError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertAccountUnblockedError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert accountDeletedSuccess -->
    <c:if test="${response eq 'accountDeletedSuccess'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="user.page.success"/>!</strong>
                <fmt:message key="user.page.alertAccountDeletedSuccess"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert accountHasFundsError -->
    <c:if test="${response eq 'accountHasFundsError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertAccountHasFundsError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert accountDeletedError -->
    <c:if test="${response eq 'accountDeletedError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertAccountDeletedError"/>
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
                <fmt:message key="user.page.settings" var="settings"/>
                <fmt:message key="user.page.showAttachedCards" var="showAttachedCards"/>
                <fmt:message key="user.page.showPaymentArchive" var="showPaymentArchive"/>
                <fmt:message key="user.account_info.status" var="accountStatus"/>
                <fmt:message key="user.account_info.status.active" var="statusActive"/>
                <fmt:message key="user.account_info.status.blocked" var="statusBlocked"/>
                <fmt:message key="user.account_info.accountNumber" var="accountNumber"/>
                <fmt:message key="admin.account_info.accountBalance" var="accountBalance"/>
                <fmt:message key="admin.account_info.blockAccount" var="blockAccount"/>
                <fmt:message key="admin.account_info.unblockAccount" var="unblockAccount"/>
                <fmt:message key="admin.account_info.deleteAccount" var="deleteAccount"/>
                <fmt:message key="user.account_info.returnToAllAccounts" var="returnToAllAccounts"/>

                <div class="page-content container-fluid">
                    <div class="row">
                        <div class="col-xl-12">
                            <div class="login-wrapper">
                                <div class="box">
                                    <div class="content-wrap">

                                        <div class="card-header">
                                            <ul class="nav nav-tabs card-header-tabs justify-content-lg-center"
                                                role="tablist">
                                                <li class="nav-item-home">
                                                    <a href="${pageContext.request.contextPath}/show-accounts" class="nav-link">
                                                        <img src="<c:url value="/images/show-accounts.png"/>" class="icon-sidebar"
                                                             style="width: 20px; height: 20px;" alt=""/>
                                                        ${myAccounts}
                                                    </a>
                                                </li>
                                                <li class="nav-item-home">
                                                    <a href="${pageContext.request.contextPath}/show-payments" class="nav-link">
                                                        <img src="<c:url value="/images/show-payments.png"/>"
                                                             class="icon-sidebar" style="height: 17px" alt=""/>
                                                        ${myPayments}
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>

                                        <div class="card-body card-body-main">

                                                <div class="row">
                                                    <div class="col-xl-12">
                                                        <div class="form-row">
                                                            <div class="col-md-7 col-lg-7 col-xl-7">

                                                                <!-- AccountDto Status -->
                                                                <c:choose>
                                                                    <c:when test="${account.blocked}">
                                                                        <label class="for-form-label text-center"
                                                                               style="margin-top:10px; font-size: 18px;">
                                                                                ${accountStatus}:
                                                                            <span class="text-danger">
                                                                                    ${statusBlocked}
                                                                            </span>
                                                                        </label>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <label class="for-form-label text-center"
                                                                               style="margin-top:10px; font-size: 18px;">
                                                                                ${accountStatus}:
                                                                            <span class="text-success">
                                                                                    ${statusActive}
                                                                            </span>
                                                                        </label>
                                                                    </c:otherwise>
                                                                </c:choose>

                                                                <!-- AccountDto Number -->
                                                                <div>
                                                                    <label class="for-form-label"
                                                                           style="margin-top: 12px;">
                                                                            ${accountNumber}:
                                                                    </label>
                                                                    <input id="number" name="number"
                                                                           type="text" class="form-control"
                                                                           readonly="readonly"
                                                                           value="${account.number}"/>
                                                                    <label for="number"
                                                                           class="default-label">&nbsp;
                                                                    </label>
                                                                </div>

                                                                <!-- AccountDto Balance and Currency -->
                                                                <div>
                                                                    <label class="for-form-label">
                                                                            ${accountBalance}:
                                                                    </label>
                                                                    <div style="display: flex; margin-bottom: 25px;">
                                                                        <input id="balance" name="balance"
                                                                               type="text" class="form-control"
                                                                               style="min-width: 49%; margin-right: 1%;"
                                                                               readonly="readonly"
                                                                               value="${account.balance}"/>
                                                                        <div id="currency"
                                                                             class="bfh-selectbox bfh-currencies"
                                                                             style="min-width: 49%; margin-left: 1%; pointer-events: none;"
                                                                             data-currency="${account.currency}"
                                                                             data-flags="true">
                                                                        </div>
                                                                        <label for="balance"
                                                                               class="default-label">&nbsp;</label>
                                                                    </div>
                                                                </div>
                                                            </div>

                                                            <div class="col-md-4 col-lg-4 col-xl-4 offset-md-1"
                                                                 style="align-self: center;">

                                                                <div class="list-group" id="list-tab" role="tablist">
                                                                    <div class="options">

                                                                        <!-- Show AccountDto Cards -->
                                                                        <div class="col-md-6">
                                                                            <span class="forward-top-link-img">
                                                                                 <a href="/attached-cards/${account.number}"
                                                                                    class="float-right">
                                                                                    <img src="<c:url value="/images/credit-cards.png"/>"
                                                                                         alt=""/>
                                                                                    <h5>${showAttachedCards}</h5>
                                                                                 </a>
                                                                            </span>
                                                                        </div>

                                                                        <!-- Show AccountDto Payments -->
                                                                        <div class="col-md-6">
                                                                            <span class="forward-top-link-img">
                                                                                <a href="/show-account-payments/${account.number}"
                                                                                   class="float-right">
                                                                                    <img src="<c:url value="/images/payments.png"/>"
                                                                                         alt=""/>
                                                                                    <h5>${showPaymentArchive}</h5>
                                                                                </a>
                                                                            </span>
                                                                        </div>
                                                                    </div>

                                                                    <c:choose>
                                                                        <c:when test="${account.blocked}">
                                                                            <a class="list-group-item list-group-item-action list-group-item-button-primary"
                                                                               id="list-unblockAccount-list" role="tab"
                                                                               href="/account-setting/${account.number}"
                                                                               onclick="document.getElementById('form-unblockAccount').submit(); return false;"
                                                                               aria-controls="unblockAccount">
                                                                                <span class="forward-right-link-img">
                                                                                    ${unblockAccount}
                                                                                    <img src="<c:url value="/images/forward-white.png"/>"
                                                                                         alt=""/>
                                                                                </span>
                                                                            </a>
                                                                            <form action="" method="POST"
                                                                                  id="form-unblockAccount" role="form">
                                                                                <input type="hidden" name="accountId"
                                                                                       value="${account.accountId}"/>
                                                                            </form>
                                                                        </c:when>

                                                                        <c:otherwise>
                                                                            <a class="list-group-item list-group-item-action list-group-item-button-primary"
                                                                               id="list-blockAccount-list" role="tab"
                                                                               href="?command=blockAccount"
                                                                               onclick="document.getElementById('form-blockAccount').submit(); return false;"
                                                                               aria-controls="blockAccount">
                                                                                <span class="forward-right-link-img">
                                                                                    ${blockAccount}
                                                                                    <img src="<c:url value="/images/forward-white.png"/>"
                                                                                         alt=""/>
                                                                                </span>
                                                                            </a>
                                                                            <form action="" method="POST"
                                                                                  id="form-blockAccount" role="form">
                                                                                <input type="hidden" name="command"
                                                                                       value="blockAccount"/>
                                                                                <input type="hidden" name="accountId"
                                                                                       value="${account.accountId}"/>
                                                                            </form>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    <a class="list-group-item list-group-item-action list-group-item-button-danger"
                                                                       id="list-deleteAccount-list" role="tab"
                                                                       href="#deleteAccountModal"
                                                                       onclick="showDeleteAccountModal();"
                                                                       aria-controls="deleteAccount">
                                                                        <span class="forward-right-link-img">
                                                                            ${deleteAccount}
                                                                            <img src="<c:url value="/images/forward-white.png"/>"
                                                                                 alt=""/>
                                                                        </span>
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
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../template/footer.jsp"/>
</div>
</body>
<script src="<c:url value="/js/modalWindow_userShowAccountSettings.js"/>"></script>
</html>