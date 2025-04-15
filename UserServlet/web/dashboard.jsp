<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>USM | Dashboard</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%-- Add debug output --%>
        <p>Session ID: ${pageContext.session.id}</p>
        <p>User in session: ${sessionScope.user}</p>
        
        <c:if test="${empty sessionScope.user}">
            <c:redirect url="login.jsp"/>
        </c:if>

        <h1>Welcome, ${sessionScope.user}!</h1>
        <a href="logout">Logout</a>
    </body>
</html>
