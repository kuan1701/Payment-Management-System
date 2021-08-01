<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
    <link rel="stylesheet" href="<c:url value="/bootstrap/css/bootstrap.min.css"/>" type="text/css">
    <link rel="stylesheet" href="<c:url value="/css/intlTelInput.css"/>" type="text/css">
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>" type="text/css">
    <link rel="stylesheet" href="<c:url value="/css/style-fixed-footer.css"/>" type="text/css">
    <link rel="stylesheet" href="<c:url value="/css/style_indexPage.css"/>" type="text/css">

</head>
<body>
<div class="main">

    <!-- Header -->
    <div class="header header-without-margin">
        <div class="container-fluid">
            <div class="row">
                <div class="col-auto mr-auto">
                    <div class="logo">
                        <a href="/" onfocus="this.blur();">
                            <img src="<c:url value="/images/logo-white.png"/>" alt="Logo"/>
                        </a>
                        <h1>Payment Management System</h1>
                    </div>
                </div>
                <div class="col-auto ml-auto">
                    <nav class="navbar navbar-expand-lg">
                        <div class="collapse navbar-collapse show" role="navigation">
                            <div class="navbar-nav">
                                <div class="nav-item">
                                    <form class="language-form">
                                        <select id="language" name="language"
                                                onchange="submit();"
                                                onfocus="this.blur();">
                                            <option value="en" ${language == 'en' ? 'selected' : ''}>EN</option>
                                            <option value="ru" ${language == 'ru' ? 'selected' : ''}>RU</option>
                                            <option value="ua" ${language == 'ua' ? 'selected' : ''}>UA</option>
                                        </select>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </nav>
                </div>
            </div>
        </div>
    </div>

    <div class="login-bg">
        <div class="page-content container-fluid">
            <div class="row justify-content-center">
                <div class="col-md-3">
                    <fmt:message key="login.signin" var="signin"/>
                    <fmt:message key="login.password" var="password"/>
                    <fmt:message key="login.submit" var="submit"/>
                    <fmt:message key="login.loginError" var="loginError"/>
                    <fmt:message key="login.passwordError" var="passwordError"/>
                    <fmt:message key="login.correct" var="correct"/>
                    <fmt:message key="login.input" var="login"/>

                    <sec:authorize access="isAuthenticated()">
                        <% response.sendRedirect("/login"); %>
                    </sec:authorize>
                    <div class="login-wrapper">
                        <div class="box">
                            <div class="content-wrap">

                                <h4>
                                    ${signin}<br>
                                    Payment Management System
                                </h4>
                                <h4>
                                    Status: <abbr style="color: red">Beta</abbr>
                                </h4>

                                <div class="form-group group-btn" style="height: 60px; margin-bottom: 24px;">
                                    <form:form action="${pageContext.request.contextPath}/login" method="POST" role="form"
                                          class="beta-user">
                                        <input type="hidden" name="username" value="kuan17011993"/>
                                        <input type="hidden" name="email" value="kuan0077@mail.ru"/>
                                        <input type="hidden" name="password" value="123456"/>
                                        <button type="submit" class="btn btn-primary signup btn-default btn-shadow">
                                            Beta-User
                                        </button>
                                    </form:form>

                                    <form:form action="${pageContext.request.contextPath}/login" method="POST" role="form"
                                          class="beta-user">
                                        <input type="hidden" name="username" value="admin"/>
                                        <input type="hidden" name="email" value="admin@admin.com"/>
                                        <input type="hidden" name="password" value="123456"/>
                                        <button type="submit" class="btn btn-primary signup btn-default btn-shadow">
                                            Beta-Admin
                                        </button>
                                    </form:form>
                                </div>

                                <form action="${pageContext.request.contextPath}/login" method="POST">

                                    <!-- Login -->
                                    <div>
                                        <input name="username" type="text" id="username"
                                               class="form-control btn-shadow" autofocus="autofocus"
                                                    placeholder="${login}"/>

                                        <label for="username" class="default-label">
                                            <span id="valid-msg-login" class="valid-msg invisible">
                                                ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                            </span>
                                            <span id="error-msg-login" class="error-msg invisible">
                                                ${loginError}
                                            </span>
                                        </label>
                                    </div>

                                    <!-- Password -->
                                    <div class="password-input">
                                        <input id="password" name="password" type="password"
                                               class="form-control btn-shadow" style="margin-top: 10px;"
                                               minlength="6" maxlength="255"
                                               placeholder="${password}"/>
                                        <a href="#" class="password-control"
                                           onfocus="this.blur();"
                                           onclick="return toggle_password(this);"></a>

                                        <label for="password" class="default-label">
                                        <span id="valid-msg-password" class="valid-msg invisible">
                                            ${correct}<img src="<c:url value="/images/correct.png"/>" alt=""/>
                                        </span>
                                            <span id="error-msg-password" class="error-msg invisible">
                                                ${passwordError}
                                            </span>
                                        </label>
                                    </div>

                                    <!-- Sigh up via Google -->
                                    <div class="row">
                                        <div class="col-md-12">
                                            <a class="button btn-lg button-google btn-block text-uppercase btn-outline"
                                               href="${pageContext.request.contextPath}/oauth2/authorization/google">
                                                <img src="https://img.icons8.com/color/16/000000/google-logo.png">
                                                <fmt:message key="login.singUpWithGoogle"/>
                                            </a>
                                        </div>
                                    </div>

                                    <!-- Submit -->
                                    <div class="action" style="padding: 20px 0 0 0;">
                                        <button id="submit" type="submit"
                                                class="btn btn-primary signup">
                                            ${submit}
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- Block of Questions -->
                        <div class="block-questions">
                            <p style="margin-bottom: 6px;">
                                <fmt:message key="login.forgotPassword"/>
                            </p>
                            <a href="/forgot-password" onfocus="this.blur();">
                                <fmt:message key="login.recovery"/>
                            </a>
                            <br>
                            <p style="margin-bottom: 8px;">
                                <fmt:message key="login.dontHaveAccount"/>
                            </p>
                            <a href="/registration" onfocus="this.blur();">
                                <fmt:message key="login.signup"/>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="template/footer.jsp"/>
</div>
</body>
<script src="<c:url value="/js/validator_indexPage.js"/>"></script>
/html>

