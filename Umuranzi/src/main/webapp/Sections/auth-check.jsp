<%@page import="com.mycompany.umuranzi.models.User"%>
<%
    User user = (User) session.getAttribute("user");
    if(user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>