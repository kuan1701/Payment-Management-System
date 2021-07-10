<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : 'en'}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="message"/>

<div class="footer">
    <div class="container-fluid">
        <div class="social">
            <a href="https://github.com/kuan1701/Payment-Management-System" onfocus="this.blur()">
                <img id="github-icon" data-toggle="tooltip-top"
                     src="<c:url value="/images/GitHub-Mark-32px-white.ico"/>"
                     title="<fmt:message key="footer.github"/>"
                     alt="<fmt:message key="footer.github"/>"/>
            </a>
        </div>
        <span class="copyright">
            &copy; 2021 Payment Management System.<br/>
            <fmt:message key="footer.copyright"/>
        </span>
        <div class="time">
            <h3 id="currentDateTime" class="custom-date" data-toggle="tooltip-top"
                title="<fmt:message key="footer.time"/>">
            </h3>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.4.1.min.js" type="text/javascript"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
<script src="<c:url value="/bootstrap/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/bootstrap/js/bootstrap-formhelpers.min.js"/>"></script>
<script src="<c:url value="/js/intlTelInput.js"/>"></script>
<script src="<c:url value="/js/utils.js"/>"></script>
<script src="<c:url value="/js/input.js"/>"></script>
<script src="<c:url value="/js/counter.js"/>"></script>
<script src="<c:url value="/js/visibility.js"/>"></script>
<script src="<c:url value="/js/alert.js"/>"></script>
<script src="<c:url value="/js/tooltip.js"/>"></script>
<script src="<c:url value="/js/dynamicDateTime.js"/>"></script>
