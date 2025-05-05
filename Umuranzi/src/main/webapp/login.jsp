<%@include file="Sections/header.jsp" %>
<%@include file="Sections/navbar.jsp" %>

<div class="container mt-5">
    <%-- Error Message --%>
    <% if (request.getAttribute("errorMessage") != null) {%>
    <div class="alert alert-yellow alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>
        <%= request.getAttribute("errorMessage")%>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>
    <input type="hidden" name="csrfToken" value="${csrfToken}">
    <%-- Success Message --%>
    <% if (request.getAttribute("successMessage") != null) {%>
    <div class="alert alert-yellow alert-dismissible fade show" role="alert">
        <%= request.getAttribute("successMessage")%>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% }%>
    <div class="row justify-content-center vh-100">
        <div class="col-md-6">
            <div class="card shadow-lg">
                <div class="card-header bg-yellow text-black">
                    <h3 class="text-center mb-0">Sign In</h3>
                </div>
                <div class="card-body">
                    <form action="LoginServlet" method="POST">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                        <div class="mb-3">
                            <label for="email" class="form-label">Email address</label>
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <button type="submit" class="btn btn-warning w-100">Login</button>
                    </form>
                    <div class="text-center mt-3">
                        <p class="text-center mt-3">No Account <a href="signup.jsp" style="text-decoration: none;color: greenyellow">Sign up</a>,    By clicking login, you accept user terms and policies.</p>
                        <a href="register.jsp" class="text-black">Create new account</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="Sections/footer.jsp" %>