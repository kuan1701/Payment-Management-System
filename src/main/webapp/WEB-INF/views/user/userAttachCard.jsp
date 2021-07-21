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
    <title><fmt:message key="user.attach_card.title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <link rel="shortcut icon" href="<c:url value="/images/favicon-white.ico"/>" type="image/x-icon">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap-formhelpers.min.css"/>">
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
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertUnableGetData"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert Success -->
    <c:if test="${cardError eq 'Card attached successfully'}">
        <div id="alert" class="alert alert-success fade show" role="alert">
            <p><strong><fmt:message key="user.page.success"/>!</strong>
                <fmt:message key="user.page.alertCardAttachedSuccess"/>
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
                <fmt:message key="user.page.alertInvalidDataError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert cardAlreadyAttachedError -->
    <c:if test="${cardError eq 'cardAlreadyAttachedError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/>!</strong>
                <fmt:message key="user.page.alertCardAlreadyAttachedError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert validityExpiredError -->
    <c:if test="${response eq 'validityExpiredError'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="registration.failed"/>!</strong>
                <fmt:message key="user.page.alertValidityExpiredError"/>
            </p>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Alert cardAttachedError -->
    <c:if test="${cardError eq 'Card attachment error'}">
        <div id="alert" class="alert alert-danger fade show" role="alert">
            <p><strong><fmt:message key="user.page.failed"/></strong>
                <fmt:message key="user.page.alertCardAttachedError"/>
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
                <fmt:message key="user.attach_card.attachNewCard" var="formHeader"/>
                <fmt:message key="user.attach_card.selectAccount" var="selectAccount"/>
                <fmt:message key="user.attach_card.number" var="number"/>
                <fmt:message key="user.attach_card.cvv" var="cvv"/>
                <fmt:message key="user.attach_card.validity" var="validity"/>
                <fmt:message key="user.attach_card.month" var="month"/>
                <fmt:message key="user.attach_card.year" var="year"/>
                <fmt:message key="user.attach_card.attachCard" var="attachCard"/>
                <fmt:message key="user.attach_card.accountIdError" var="accountIdError"/>
                <fmt:message key="user.attach_card.numberError" var="numberError"/>
                <fmt:message key="user.attach_card.cvvError" var="cvvError"/>
                <fmt:message key="user.attach_card.monthError" var="monthError"/>
                <fmt:message key="user.attach_card.yearError" var="yearError"/>
                <fmt:message key="user.attach_card.validityError" var="validityError"/>
                <fmt:message key="user.attach_card.validityExpiredError" var="validityExpiredError"/>
                <fmt:message key="user.attach_card.tooltipCardNumber" var="tooltipCardNumber"/>
                <fmt:message key="user.attach_card.tooltipCVV" var="tooltipCVV"/>
                <fmt:message key="user.attach_card.tooltipMonth" var="tooltipMonth"/>
                <fmt:message key="user.attach_card.tooltipYear" var="tooltipYear"/>
                <fmt:message key="registration.correct" var="correct"/>

                <div class="page-content container-fluid">
                    <div class="row">
                        <div class="col-xl-6 offset-xl-2">
                            <div class="login-wrapper">
                                <div class="box">
                                    <div class="content-wrap">

                                        <h4>
                                            ${formHeader}
                                        </h4>
                                        <c:url value="/attach-card" var="var"/>
                                        <form:form action="${var}" method="POST" role="form" modelAttribute="bankCard">
                                            <input type="hidden" name="accountId" value="${bankCard.account.accountId}">

                                            <!-- Select AccountId -->
                                            <div style="height: 61px; margin-bottom: 30px">
                                                <label class="for-form-label">
                                                        ${selectAccount}:
                                                </label>

                                                <select name="accountNumber" class="selectAccount">
                                                    <c:forEach items="${accounts}" var="account">
                                                        <option value="${account.accountId}">${account.number}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <!-- Number -->
                                            <div>
                                                <input id="cardNumber" name="number" type="text" class="form-control"
                                                       data-toggle="tooltip"
                                                       data-title="${tooltipCardNumber}"
                                                       maxlength="19" oninput="this.value=inputCardNumber(this.value);"
                                                       placeholder="${number}*"
                                                       value="${numberValue}"/>
                                                <label for="cardNumber" class="default-label">
                                                    <span id="valid-msg-cardNumber" class="valid-msg invisible">
                                                        ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                    </span>
                                                    <span id="error-msg-cardNumber" class="error-msg invisible">
                                                        ${numberError}
                                                    </span>
                                                </label>
                                            </div>

                                            <!-- CVV -->
                                            <div>
                                                <input id="CVV" name="CVV" type="text" class="form-control"
                                                       data-toggle="tooltip"
                                                       data-title="${tooltipCVV}"
                                                       maxlength="3" onkeypress="inputOnlyNumbers();"
                                                       placeholder="${cvv}*"
                                                       value="${cvvValue}"/>
                                                <label for="CVV" class="default-label">
                                                    <span id="valid-msg-cvv" class="valid-msg invisible">
                                                        ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                                    </span>
                                                    <span id="error-msg-cvv" class="error-msg invisible">
                                                            ${cvvError}
                                                    </span>
                                                </label>
                                            </div>

                                            <!-- Select Month and Year -->
                                            <div>
                                                <div class="form-group" id="expiration-date"
                                                     style="display: flex; justify-content: center;
                                                     align-items: center; margin: 0 0 0 0;">
                                                    <label class="for-form-label" style="width: 15%;">
                                                            ${validity}:
                                                    </label>

                                                    <!-- Select Month -->
                                                    <div style="width: 100px;">
                                                        <input id="month" name="month" type="text" class="form-control"
                                                               data-toggle="tooltip"
                                                               data-title="${tooltipMonth}"
                                                               maxlength="2" onkeypress="inputOnlyNumbers();"
                                                               placeholder="${month}*"/>
                                                        <label for="month" class="default-label">
                                                    <span id="valid-msg-month" class="valid-msg invisible">
                                                        ${correct}<img src="<c:url value="/images/correct.png"/>"
                                                                       alt=""/>
                                                    </span>
                                                            <span id="error-msg-month" class="error-msg invisible">
                                                                    ${monthError}
                                                            </span>
                                                        </label>
                                                    </div>

                                                    <div style="margin-left: 10px">
                                                        <h2>/</h2>
                                                    </div>

                                                    <!-- Select Year -->
                                                    <div style="width: 100px; margin-left: 10px;">
                                                        <input id="year" name="year" type="text" class="form-control"
                                                               data-toggle="tooltip"
                                                               data-title="${tooltipYear}"
                                                               maxlength="2" onkeypress="inputOnlyNumbers();"
                                                               placeholder="${year}*"/>
                                                        <label for="year" class="default-label">
                                                    <span id="valid-msg-year" class="valid-msg invisible">
                                                        ${correct}<img src="<c:url value="/images/correct.png"/>"
                                                                       alt=""/>
                                                    </span>
                                                            <span id="error-msg-year" class="error-msg invisible">
                                                                    ${yearError}
                                                            </span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>

                                            <!-- Submit -->
                                            <div class="action" style="padding: 20px 0 5px 0">
                                                <button id="submit" type="submit" class="btn btn-primary signup">
                                                        ${attachCard}
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
<script src="<c:url value="/js/validator_userAttachCard.js"/>"></script>
</html>