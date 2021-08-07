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
    <title><fmt:message key="user.account_cards.title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <link rel="shortcut icon" href="<c:url value="/images/favicon-white.ico"/>" type="image/x-icon">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap-formhelpers.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style_userShowAccountCards.css"/>">
</head>
<body>

<!-- Modal window -->
<div id="detachCardModal" class="modal fade" tabindex="-1" role="dialog" onfocus="this.blur();">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">
                    <fmt:message key="user.account_cards.modalHeader"/>
                </h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                <fmt:message key="user.account_cards.modalBody"/>
                <br>
                <div style="display: flex; margin-top: 20px;">
                    <label for="cardNumberText" class="modal-label">
                        <fmt:message key="user.account_cards.modalCardLabel"/>
                    </label>
                    <input id="cardNumberText" class="form-control modal-form-control"
                           type="text" readonly="readonly"/>
                </div>
            </div>
            <div class="modal-footer">
                <div class="btn-group">
                    <button type="button" class="btn btn-default closeButton" style="border-radius: 5px;"
                            data-dismiss="modal" onfocus="this.blur();">
                        <fmt:message key="user.page.closeButton"/>
                    </button>
                    <div style="margin-left: 10px; border-left: 1px solid #e5e5e5;"></div>

                    <c:url value="/attached-cards/${account.number}/detach/${bankCard.cardId}" var="var"/>
                    <form action="${var}" method="POST" role="form">
                        <input type="hidden" name="accountId" value="${account.accountId}"/>
                        <input type="hidden" name="cardId" id="cardNumber"/>
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

    <!-- Alerts unableGetData and showAccountError and unableGetCards -->
    <c:if test="${response eq 'unableGetData' || response eq 'showAccountError' || response eq 'unableGetCards'}">
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

    <!-- Alert unableGetCardId -->
    <c:if test="${response eq 'unableGetCardId'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertUnableGetCardIdError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert cardBlockedSuccess -->
    <c:if test="${alert eq 'cardBlockedSuccess'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="user.page.success"/>!</strong>
                <fmt:message key="user.page.alertCardBlockedSuccess"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert cardBlockedError -->
    <c:if test="${response eq 'cardBlockedError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertCardBlockedError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert cardUnblockedSuccess -->
    <c:if test="${alert eq 'cardUnblockedSuccess'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="user.page.success"/>!</strong>
                <fmt:message key="user.page.alertCardUnblockedSuccess"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert cardUnblockedError -->
    <c:if test="${response eq 'cardUnblockedError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertCardUnblockedError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert cardDetachedSuccess -->
    <c:if test="${alert eq 'cardDetachedSuccess'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="user.page.success"/>!</strong>
                <fmt:message key="user.page.alertCardDetachedSuccess"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert cardDetachedError -->
    <c:if test="${response eq 'cardDetachedError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertCardDetachedError"/>
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
                <fmt:message key="user.page.showPaymentArchive" var="showPaymentArchive"/>
                <fmt:message key="admin.account_info.accountStatus" var="accountStatus"/>
                <fmt:message key="user.account_info.status.active" var="statusActive"/>
                <fmt:message key="user.account_info.status.blocked" var="statusBlocked"/>
                <fmt:message key="admin.account_info.accountNumber" var="accountNumber"/>
                <fmt:message key="admin.account_info.accountBalance" var="accountBalance"/>
                <fmt:message key="admin.account_info.allAttachedCards" var="allAttachedCards"/>
                <fmt:message key="admin.account_info.validity" var="validity"/>
                <fmt:message key="admin.account_info.blockCard" var="blockCard"/>
                <fmt:message key="admin.account_info.unblockCard" var="unblockCard"/>
                <fmt:message key="user.account_cards.detach" var="detach"/>
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
                                                    <a href="${pageContext.request.contextPath}/show-payments/${user.userId}" class="nav-link">
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
                                                            <div class="col-md-8 col-lg-8 col-xl-8">

                                                                <!-- AccountDto Status -->
                                                                <c:choose>
                                                                    <c:when test="${account.blocked}">
                                                                        <label class="for-form-label text-center"
                                                                               style="font-size: 18px;">
                                                                                ${accountStatus}:
                                                                            <span class="text-danger">
                                                                                    ${statusBlocked}
                                                                            </span>
                                                                        </label>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <label class="for-form-label text-center"
                                                                               style="font-size: 18px;">
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
                                                                           class="default-label">&nbsp;</label>
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

                                                            <div class="col-md-4 col-lg-4 col-xl-4"
                                                                 style="align-self: center;">

                                                                <div class="list-group">
                                                                    <div class="options">

                                                                        <!-- Show AccountDto Settings -->
                                                                        <div class="col-md-6">
                                                                            <span class="forward-top-link-img">
                                                                                <a href="/account-setting/${account.number}"
                                                                                   class="float-right">
                                                                                    <img src="<c:url value="/images/settings.png"/>"
                                                                                         alt=""/>
                                                                                    <h5>${settings}</h5>
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
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="col-xl-12">

                                                        <c:choose>
                                                            <c:when test="${bankCardsEmpty == false}">

                                                                <h4 style="margin-top: 10px;">
                                                                        ${allAttachedCards}
                                                                </h4>

                                                                <div class="card-container"
                                                                     style="width: 100% !important;">
                                                                    <div class="row row-cols-1 row-cols-sm-1 row-cols-md-2 row-cols-lg-2 row-cols-xl-2">
                                                                        <c:forEach items="${bankCardsList}" var="bankCard">
                                                                            <div class="col mb-4">
                                                                                <div class="card bg-light">
                                                                                    <div class="card-header">
                                                                                        <c:choose>
                                                                                            <c:when test="${bankCard.active}">
                                                                                                <small class="text-success float-right">
                                                                                                        ${statusActive}
                                                                                                </small>
                                                                                            </c:when>
                                                                                            <c:otherwise>
                                                                                                <small class="text-danger float-right">
                                                                                                        ${statusBlocked}
                                                                                                </small>
                                                                                            </c:otherwise>
                                                                                        </c:choose>
                                                                                    </div>
                                                                                    <div class="card-body"
                                                                                         style="padding: 0.75rem 1.25rem;">
                                                                                        <p class="card-title text-muted">
                                                                                                ${bankCard.number}<br/>
                                                                                                ${validity}: ${bankCard.validity}
                                                                                            <a href="#detachCardModal?cardId=${bankCard.cardId}&cardNumber=${bankCard.number}"
                                                                                               onclick="showDetachCardModal();"
                                                                                               class="float-right" role="tab">
                                                                                                <img src="<c:url value="/images/detach-card.png"/>"
                                                                                                     style="margin-left: 7px;"
                                                                                                     alt="${detach}" title="${detach}"/>
                                                                                            </a>
                                                                                            <c:choose>
                                                                                                <c:when test="${bankCard.active}">
                                                                                                    <a href="/attached-cards/${account.number}"
                                                                                                       class="float-right"
                                                                                                       role="tab"
                                                                                                       onclick="document.getElementById('form-blockCard').submit(); return false;">
                                                                                                        <img src="<c:url value="/images/block.png"/>"
                                                                                                             alt="${blockCard}" title="${blockCard}"/>
                                                                                                    </a>
                                                                                                    <form action="/attached-cards/${account.number}"
                                                                                                          method="POST"
                                                                                                          id="form-blockCard"
                                                                                                          role="form">
                                                                                                        <input type="hidden"
                                                                                                               name="cardId"
                                                                                                               value="${bankCard.cardId}"/>
                                                                                                    </form>
                                                                                                </c:when>

                                                                                                <c:otherwise>
                                                                                                    <a href="#"
                                                                                                             class="float-right"
                                                                                                             role="tab"
                                                                                                             onclick="document.getElementById('form-unblockCard').submit(); return false;">
                                                                                                        <img src="<c:url value="/images/unblock.png"/>"
                                                                                                             alt="${unblockCard}" title="${unblockCard}"/>
                                                                                                    </a>
                                                                                                    <form action="/attached-cards/${account.number}"
                                                                                                          method="POST"
                                                                                                          id="form-unblockCard"
                                                                                                          role="form">
                                                                                                        <input type="hidden"
                                                                                                               name="cardId"
                                                                                                               value="${bankCard.cardId}"/>
                                                                                                    </form>
                                                                                                </c:otherwise>
                                                                                            </c:choose>
                                                                                        </p>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </c:forEach>
                                                                    </div>
                                                                </div>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div class="message-block">
                                                                    <span class="title-label">
                                                                        <label>
                                                                           <fmt:message
                                                                                   key="admin.account_info.attachedCardsEmpty"/>
                                                                        </label>
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
            </div>
        </div>
    </div>
    <jsp:include page="../template/footer.jsp"/>
</div>
</body>
<script src="<c:url value="/js/modalWindow_userShowAccountCards.js"/>"></script>
</html>