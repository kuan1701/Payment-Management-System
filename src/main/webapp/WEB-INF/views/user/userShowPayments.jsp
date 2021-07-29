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
    <title><fmt:message key="user.page.myPayments"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <link rel="shortcut icon" href="<c:url value="/images/favicon-white.ico"/>" type="image/x-icon">
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="https://unpkg.com/gijgo@1.9.13/css/gijgo.min.css"/>">
    <script type="text/javascript" src="https://unpkg.com/gijgo@1.9.13/js/gijgo.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style_userShowPayments.css"/>">
</head>
<body>
<div class="main">
    <jsp:include page="../template/header.jsp"/>

    <!-- Alert unableGetData and showUserPaymentsError -->
    <c:if test="${response eq 'unableGetData' || response eq 'showUserPaymentsError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertUnableGetData"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert searchPaymentsSuccess -->
    <c:if test="${response eq 'searchPaymentsSuccess'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p>
                <fmt:message key="admin.page.alertSearchPaymentsSuccess"/>
                    ${numberOfPayments}
                <fmt:message key="admin.user_payments.platezhis"/>.
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert searchPaymentsWarning -->
    <c:if test="${response eq 'searchPaymentsWarning'}">
        <div id="alert" class="alert alert-warning fade show" role="alert">
            <p>
                <fmt:message key="admin.page.alertSearchPaymentsWarning"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert searchPaymentsError -->
    <c:if test="${response eq 'searchPaymentsError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="admin.page.failed"/></strong>
                <fmt:message key="admin.page.alertSearchPaymentsError"/>
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
                <fmt:message key="user.page.success" var="success"/>
                <fmt:message key="user.page.failed" var="failed"/>
                <fmt:message key="admin.payment_info.sentFunds" var="sentFunds"/>
                <fmt:message key="admin.payment_info.receivedFunds" var="receivedFunds"/>
                <fmt:message key="admin.payment_info.remained" var="remained"/>
                <fmt:message key="user.page.showInfo" var="showInfo"/>
                <fmt:message key="admin.user_payments.incomingPayments" var="incomingPayments"/>
                <fmt:message key="admin.user_payments.outgoingPayments" var="outgoingPayments"/>
                <fmt:message key="admin.user_payments.tooltipTypeOfPayment" var="tooltipTypeOfPayment"/>
                <fmt:message key="admin.support.tooltipStartDate" var="tooltipStartDate"/>
                <fmt:message key="admin.support.tooltipFinalDate" var="tooltipFinalDate"/>
                <fmt:message key="user.platezhis.repeat" var="repeat"/>
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
                                            <li class="nav-item">
                                                <a href="${pageContext.request.contextPath}/show-accounts" class="nav-link nav-link-hover">
                                                    <img src="<c:url value="/images/show-accounts.png"/>" class="icon-sidebar"
                                                         style="width: 20px; height: 20px;" alt=""/>
                                                    ${myAccounts}
                                                </a>
                                            </li>
                                            <li class="nav-item-active">
                                                <a href="${pageContext.request.contextPath}/show-payments" class="nav-link">
                                                    <img src="<c:url value="/images/show-payments.png"/>"
                                                         class="icon-sidebar" style="height: 17px" alt=""/>
                                                    ${myPayments}
                                                </a>
                                            </li>
                                        </ul>
                                    </div>

                                    <c:choose>
                                        <c:when test="${paymentEmpty == false}">

                                            <div class="card-body card-body-main">
                                                <div class="row">
                                                    <div class="col-lg-3 col-xl-3">
                                                        <div class="search-block">
                                                            <label>
                                                                    ${searchCriteria}:
                                                            </label>

                                                            <c:url value="/show-payments" var="var"/>
                                                            <form:form action="${var}"
                                                                       method="POST" role="form" modelAttribute="paymentList">

                                                                <input type="hidden" id="isIncoming" name="checkbox"
                                                                       value="isIncoming"/>

                                                                <input type="hidden" id="isOutgoing" name="checkbox"
                                                                       value="isOutgoing"/>

                                                                <!-- Choice of payment type -->
                                                                <div class="group-btn"
                                                                     data-toggle="tooltip-left-hover"
                                                                     data-title="${tooltipTypeOfPayment}">
                                                                    <label for="checkbox-isIncoming">
                                                                            ${incomingPayments}
                                                                        <input id="checkbox-isIncoming"
                                                                               type="checkbox"
                                                                               onfocus="this.blur();"/>
                                                                    </label>
                                                                    <label for="checkbox-isOutgoing">
                                                                            ${outgoingPayments}
                                                                        <input id="checkbox-isOutgoing"
                                                                               type="checkbox"
                                                                               onfocus="this.blur();"/>
                                                                    </label>
                                                                </div>

                                                                <!-- Min value Date -->
                                                                <input id="datepicker-start-date" name="startDate"
                                                                       data-toggle="tooltip-left"
                                                                       data-title="${tooltipStartDate}"
                                                                       readonly="readonly"
                                                                       value="${startDateValue}"/>
                                                                <label for="datepicker-start-date"
                                                                       class="default-label">&nbsp;</label>

                                                                <!-- Max value Date -->
                                                                <input id="datepicker-final-date" name="finalDate"
                                                                       data-toggle="tooltip-left"
                                                                       data-title="${tooltipFinalDate}"
                                                                       readonly="readonly"
                                                                       value="${finalDateValue}"/>
                                                                <label for="datepicker-final-date"
                                                                       class="default-label">&nbsp;</label>

                                                                <script>
                                                                    let today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
                                                                    $('#datepicker-start-date').datepicker({
                                                                        format: 'dd/mm/yyyy',
                                                                        minDate: '01/01/2020',
                                                                        maxDate: today,
                                                                        showRightIcon: true,
                                                                        uiLibrary: 'bootstrap4'
                                                                    });

                                                                    $('#datepicker-final-date').datepicker({
                                                                        format: 'dd/mm/yyyy',
                                                                        minDate: '01/01/2020',
                                                                        maxDate: today,
                                                                        showRightIcon: true,
                                                                        uiLibrary: 'bootstrap4'
                                                                    });
                                                                </script>

                                                                <div class="action" style="padding: 10px 0 5px 0;">
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

                                                                    <div class="row row-cols-1 row-cols-sm-1 row-cols-md-1 row-cols-lg-1 row-cols-xl-1">
                                                                        <c:forEach items="${paymentList}" var="payment">
                                                                            <div class="col mb-4">
                                                                                <div class="card bg-light">
                                                                                    <div class="card-header">
                                                                                        <small class="text-muted float-left">
                                                                                                ${payment.date}
                                                                                        </small>
                                                                                        <c:choose>
                                                                                            <c:when test="${payment.status}">
                                                                                                <small class="text-success float-right">
                                                                                                        ${success}
                                                                                                </small>
                                                                                            </c:when>
                                                                                            <c:otherwise>
                                                                                                <small class="text-danger float-right">
                                                                                                        ${failed}
                                                                                                </small>
                                                                                            </c:otherwise>
                                                                                        </c:choose>
                                                                                    </div>
                                                                                    <div class="card-body"
                                                                                         style="padding: 0.75rem 1.25rem;">

                                                                                        <!-- Outgoing and Incoming Payments -->
                                                                                        <c:choose>
                                                                                            <c:when test="${payment.outgoing}">

                                                                                                <!-- Sender and Recipient -->
                                                                                                <p class="card-title text-muted">
                                                                                                        ${payment.senderNumber}
                                                                                                    <span class="forward-right-link-img">&Longrightarrow;</span>
                                                                                                        ${payment.recipientNumber}
                                                                                                </p>

                                                                                                <!-- Sent Funds -->
                                                                                                <p class="card-title text-muted">
                                                                                                        ${sentFunds}: ${payment.senderAmount} ${payment.senderCurrency}
                                                                                                </p>

                                                                                                <!-- New balance -->
                                                                                                <p class="card-title text-muted new-balance">
                                                                                                    <span>
                                                                                                        ${remained}: ${payment.newBalance} ${payment.senderCurrency}
                                                                                                    </span>

                                                                                                    <!-- Show Payment Info -->
                                                                                                    <a href="/paymentInfo/${payment.paymentId}"
                                                                                                       class="float-right">
                                                                                                        <img src="<c:url value="/images/info.png"/>"
                                                                                                             alt="${showInfo}"/>
                                                                                                    </a>

                                                                                                    <!-- Repeat Payment -->
                                                                                                    <a href="${pageContext.request.contextPath}/make-payment"
                                                                                                       class="float-right">
                                                                                                        <img src="<c:url value="/images/repeat-payment.png"/>"
                                                                                                             alt="${repeat}"/>
                                                                                                    </a>
                                                                                                </p>
                                                                                            </c:when>
                                                                                            <c:otherwise>

                                                                                                <!-- Sender and Recipient -->
                                                                                                <p class="card-title text-muted">
                                                                                                        ${payment.recipientNumber}
                                                                                                    <span class="forward-left-link-img">&Longleftarrow;</span>
                                                                                                        ${payment.senderNumber}
                                                                                                </p>

                                                                                                <!-- Received Funds -->
                                                                                                <p class="card-title text-muted">
                                                                                                        ${receivedFunds}: ${payment.recipientAmount} ${payment.recipientCurrency}
                                                                                                </p>

                                                                                                <!-- New balance -->
                                                                                                <p class="card-title text-muted">
                                                                                                     <span>
                                                                                                        ${remained}: ${payment.newBalance} ${payment.recipientCurrency}
                                                                                                     </span>

                                                                                                    <!-- Show Payment Info -->
                                                                                                    <a href="/paymentInfo/${payment.paymentId}"
                                                                                                       class="float-right">
                                                                                                        <img src="<c:url value="/images/info.png"/>"
                                                                                                             alt="${showInfo}"/>
                                                                                                    </a>
                                                                                                </p>
                                                                                            </c:otherwise>
                                                                                        </c:choose>
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
                                                            <fmt:message key="user.platezhis.paymentEmpty"/>
                                                        </label>
                                                    </span>
                                                    <div class="w-100" style="height:172px;">
                                                        <img src="<c:url value="/images/profit_3.png"/>" alt=""
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
<script src="<c:url value="/js/searcher_userShowPayments.js"/>"></script>
</html>