<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="Sections/header.jsp" %>
<%@include file="Sections/navbar.jsp" %>

<div class="container mt-5">
    <div class="alert alert-danger">
        <h2>Error Occurred</h2>
        <p>${errorMessage}</p>
        <a href="index.jsp" class="btn btn-warning">Return Home</a>
    </div>
</div>

<%@include file="Sections/footer.jsp" %>