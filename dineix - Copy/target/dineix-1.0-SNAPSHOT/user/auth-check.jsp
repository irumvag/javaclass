<%@page import="com.dineix.web.models.User"%>
<%
    User user = (User) session.getAttribute("userId");
    if(user == null) {
        response.sendRedirect("login");
        return;
    }
%>