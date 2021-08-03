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
    <title><fmt:message key="login.title"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <link rel="shortcut icon" href="<c:url value="/images/favicon-white.ico"/>" type="image/x-icon">
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style-fixed-footer.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style_errorPage.css"/>">
</head>
<body>
<div class="main">
    <jsp:include page="template/header.jsp"/>

    <div class="page-content container-fluid">
        <div class="row justify-content-center">
            <div class="col-xl-8">
                <div class="error_detail">
                    <h3 class="code">
                        <fmt:message key="registration.activationMessage"/>
                    </h3>

                <div></div>

                    <a href="${pageContext.request.contextPath}/" class="back-homepage">
                        <fmt:message key="error.goBackHome"/>
                        ${goBackHome}<img src="<c:url value="/images/back-home.png"/>" class="icon" alt=""/>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="template/footer.jsp"/>
</div>
</body>
</html>